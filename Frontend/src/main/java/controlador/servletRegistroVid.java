package controlador;

import modelo.Video;
import modelo.Usuario;
import modelo.dao.VideoDAO;

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

import java.sql.Date;
import java.sql.Time;

/**
 * Servlet encargado de procesar el registro de nuevos videos por parte de usuarios autenticados.
 * 
 * Funcionalidades:
 * - Verifica sesión activa del usuario
 * - Valida duplicados por título y usuario
 * - Guarda el video en la base de datos
 * - Sube el archivo del video al sistema de archivos
 * 
 * Mapea en: /servletRegistroVid
 * 
 * Requiere: Formulario con campos (titulo, autor, fechaCreacion, duracion, descripcion, formato, videoFile)
 * 
 * Redirige a:
 * - vista/login.jsp si no hay sesión activa
 * - vista/registroVid.jsp en caso de error
 * - servletListadoVid en caso de éxito
 * 
 * Guarda el archivo en: /videosRegistrados dentro del proyecto
 * 
 * @author Kenny Alejandro/Lijie Yin
 */
@MultipartConfig
@WebServlet("/servletRegistroVid")
public class servletRegistroVid extends HttpServlet {

    /**
     * Procesa la solicitud POST proveniente del formulario de registro de video.
     *
     * @param request  Petición HTTP con datos del formulario
     * @param response Respuesta HTTP
     * @throws ServletException si ocurre un error interno
     * @throws IOException      si hay un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Obtener parámetros del formulario
        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");
        String creationDate = request.getParameter("fechaCreacion");
        String duration = request.getParameter("duracion");
        String description = request.getParameter("descripcion");
        String format = request.getParameter("formato");

        // Convertir a tipos adecuados
        Date creationDateDateFormat = Date.valueOf(creationDate);
        Time durationTimeFormat = Time.valueOf(duration);

        // Definir ruta de guardado del archivo (relativa al sistema)
        String localization = "/videosRegistrados/" + title + "." + format;

        // Verificar sesión activa
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("vista/login.jsp");
            return;
        }

        // Obtener usuario de sesión
        Usuario user = (Usuario) session.getAttribute("user");
        Integer userId = user.getId();

        // Crear objeto Video
        Video video = new Video(title, author, creationDateDateFormat, durationTimeFormat, 0, description, format, localization, userId);
        VideoDAO videoDAO = new VideoDAO();

        // Validar duplicado
        if (videoDAO.isVideoRegistered(title, userId)) {
            request.setAttribute("error", "Este video ya ha sido registrado previamente por usted.");
            request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
            return;
        }

        // Registrar video en base de datos y guardar archivo físico
        if (videoDAO.registerVideo(video)) {
            saveVideoFile(request.getPart("videoFile"), title + "." + format);
            response.sendRedirect(request.getContextPath() + "/servletListadoVid");
        } else {
            request.setAttribute("error", "No se pudo completar el registro.");
            request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
        }
    }

    /**
     * Guarda el archivo subido por el usuario en el sistema de archivos del servidor.
     *
     * @param filePart Parte del archivo recibido desde el formulario (tipo multipart)
     * @param filename Nombre con el que se guardará el archivo en disco
     * @throws ServletException si hay un error en el servlet
     * @throws IOException      si hay un error de escritura/lectura de archivos
     */
    private void saveVideoFile(Part filePart, String filename) throws ServletException, IOException {
        String uploadsDir = "/home/alumne/NetBeansProjects/ISDCM-proyecto/videosRegistrados";
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
}