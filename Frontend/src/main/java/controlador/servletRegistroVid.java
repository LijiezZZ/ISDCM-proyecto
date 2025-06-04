package controlador;

import modelo.Video;
import modelo.Usuario;
import servicio.ServicioVideoREST;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import servicio.ServicioHelper;

/**
 * Servlet encargado de procesar el registro de nuevos videos por parte de
 * usuarios autenticados.
 *
 * Funcionalidades: - Verifica sesión activa del usuario - Valida duplicados por
 * título y usuario - Guarda el video en la base de datos vía API REST - Sube el
 * archivo del video al sistema de archivos
 *
 * Mapea en: /servletRegistroVid
 *
 * Requiere: Formulario con campos (titulo, autor, fechaCreacion, duracion,
 * descripcion, formato, videoFile)
 *
 * Redirige a: - vista/login.jsp si no hay sesión activa - vista/registroVid.jsp
 * en caso de error - servletListadoVid en caso de éxito
 *
 * Guarda el archivo en: /videosRegistrados dentro del proyecto
 *
 * @author Kenny Alejandro/Lijie Yin
 */
@MultipartConfig
@WebServlet("/servletRegistroVid")
public class servletRegistroVid extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("tituloBuscado", request.getParameter("titulo"));
        request.setAttribute("autorBuscado", request.getParameter("autor"));
        request.setAttribute("fechaBuscada", request.getParameter("fecha"));

        request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
    }

    /**
     * Procesa la solicitud POST proveniente del formulario de registro de
     * video.
     *
     * @param request Petición HTTP con datos del formulario
     * @param response Respuesta HTTP
     * @throws ServletException si ocurre un error interno
     * @throws IOException si hay un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("tituloBuscado", request.getParameter("tituloBuscado"));
        request.setAttribute("autorBuscado", request.getParameter("autorBuscado"));
        request.setAttribute("fechaBuscada", request.getParameter("fechaBuscada"));

        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");
        String creationDate = request.getParameter("fechaCreacion");
        String duration = request.getParameter("duracion");
        String description = request.getParameter("descripcion");
        String format = request.getParameter("formato");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("vista/login.jsp");
            return;
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Integer userId = user.getId();

        // Formatear fecha actual como OffsetDateTime (para creacion y modificacion)
        OffsetDateTime now = OffsetDateTime.now();
        String nowFormatted = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String storedFilename = timestamp + "." + format;
        String basePath = "/VideosRegistrados/";
        String localization = basePath + storedFilename;

        // Construcción del objeto compatible con VideoDTO
        Video video = new Video();
        video.setTitulo(title);
        video.setAutor(author);
        video.setFechaCreacion(creationDate);
        video.setDuracion(duration);
        video.setDescripcion(description);
        video.setFormato(format);
        video.setLocalizacion(localization);
        video.setUserId(userId);
        video.setNumReproducciones(0);
        video.setCreacionTimestamp(nowFormatted);
        video.setModificacionTimestamp(nowFormatted);

        ServicioVideoREST servicioVideo = ServicioHelper.getServicioVideo(request);
        
        if (servicioVideo.existeVideo(title, userId)) {
            request.setAttribute("error", "Ya ha registrado un video con este título previamente");
            request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
            return;
        }

        if (servicioVideo.registrarVideo(video)) {
            saveVideoFile(request.getPart("videoFile"), storedFilename);

            String tituloFiltro = request.getParameter("tituloBuscado");
            String autorFiltro = request.getParameter("autorBuscado");
            String fechaFiltro = request.getParameter("fechaBuscada");
            String redirectUrl = String.format("%s/servletListadoVid?titulo=%s&autor=%s&fecha=%s",
                    request.getContextPath(),
                    tituloFiltro != null ? tituloFiltro : "",
                    autorFiltro != null ? autorFiltro : "",
                    fechaFiltro != null ? fechaFiltro : "");
            response.sendRedirect(redirectUrl);

        } else {
            request.setAttribute("error", "No se pudo completar el registro.");

            request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
        }
    }

    /**
     * Guarda el archivo de vídeo subido por el usuario en el sistema de
     * archivos del servidor.
     *
     * Este método recibe el archivo enviado desde el formulario (multipart) y
     * lo almacena físicamente dentro del directorio persistente del proyecto:
     *
     * <p>
     * <strong>Ruta esperada:</strong> {@code /Frontend/VideosRegistrados}</p>
     *
     * Por ejemplo, en entorno local:
     * {@code /home/alumne/NetBeansProjects/ISDCM-proyecto/Frontend/VideosRegistrados}
     *
     * <p>
     * Si el directorio de destino no existe, se crea automáticamente. El
     * archivo se sobrescribirá si ya existe uno con el mismo nombre.
     * </p>
     *
     * @param filePart Parte del archivo recibido desde el formulario HTML
     * (campo de tipo file)
     * @param filename Nombre técnico con el que se guardará el archivo en disco
     * (ej. {@code 20250511210000.mp4})
     *
     * @throws ServletException si ocurre un error en el proceso del servlet
     * @throws IOException si hay un error al leer el archivo recibido o
     * escribir en disco
     */
    /*
    private void saveVideoFile(Part filePart, String filename) throws ServletException, IOException {
        String realPath = getServletContext().getRealPath("/");
        String basePath = realPath.split("target")[0];
        String uploadsDir = basePath + "VideosRegistrados";
        // Ejemplo base "/home/alumne/NetBeansProjects/ISDCM-proyecto/Frontend/videosRegistrados";
        System.out.println("Ruta de los archivos: " + uploadsDir);
        File uploadDir = new File(uploadsDir);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        if (filePart == null) {
            System.out.println("No se recibió ningún archivo.");
            return;
        }

        File uploadFile = new File(uploadDir, filename);

        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo subido exitosamente: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar el archivo.");
        }
    }
     */
    private void saveVideoFile(Part filePart, String filename) throws ServletException, IOException {
        String realPath = getServletContext().getRealPath("/");
        String basePath = realPath.split("target")[0];
        String uploadsDir = basePath + "VideosRegistrados";
        File uploadDir = new File(uploadsDir);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        if (filePart == null) {
            System.out.println("No se recibió ningún archivo.");
            return;
        }

        File encryptedFile = new File(uploadDir, filename);

        try (
                InputStream inputStream = filePart.getInputStream(); FileOutputStream fileOut = new FileOutputStream(encryptedFile);) {
            // Clave AES (puedes cargarla de un archivo seguro o base de datos en producción)
            String keyString = loadAESKey();
            Key secretKey = new SecretKeySpec(keyString.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            try (CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    cipherOut.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Archivo subido y cifrado exitosamente: " + filename);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cifrar y guardar el archivo.");
        }
    }

    private String loadAESKey() throws IOException {
        String realPath = getServletContext().getRealPath("/");
        String basePath = realPath.split("target")[0];
        String folderPath = basePath + "Clave";
        Path keyPath = Paths.get(folderPath, "claveVideo.key");
        return Files.readString(keyPath).trim();
    }

}
