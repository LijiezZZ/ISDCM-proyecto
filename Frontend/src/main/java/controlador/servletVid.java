package controlador;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import modelo.Video;

/**
 * Servlet encargado de servir archivos de vídeo guardados fuera del WAR.
 *
 * Este servlet recibe una ruta lógica como parámetro (`localizacion`), extrae
 * el nombre del archivo, y lo busca dentro del directorio persistente del
 * proyecto (`/videosRegistrados` ubicado en la raíz del proyecto Frontend).
 *
 * Funciona como intermediario entre la lógica de la aplicación y el sistema de
 * archivos, permitiendo la reproducción de vídeos mediante URLs seguras y
 * controladas.
 *
 * URL de acceso: /servletVid?localizacion=/VideosRegistrados/archivo.mp4
 *
 * Ruta real de almacenamiento en disco (ejemplo en entorno local):
 * `/home/alumne/NetBeansProjects/ISDCM-proyecto/Frontend/videosRegistrados`
 *
 * El archivo debe haber sido previamente guardado en esta ruta durante el
 * registro del vídeo.
 *
 * Tipo de contenido servido: video/mp4
 *
 * Este servlet **no forma parte del flujo MVC clásico**, ya que actúa como un
 * componente técnico especializado en servir contenido binario.
 *
 * @author Kenny Alejandro
 */
@WebServlet("/servletVid")
public class servletVid extends HttpServlet {

    /**
     * Maneja solicitudes GET para servir archivos de vídeo.
     *
     * Extrae el nombre del archivo a partir del parámetro 'localizacion',
     * localiza el fichero en disco y lo escribe directamente en la respuesta
     * HTTP.
     *
     * @param request La solicitud HTTP entrante, que debe contener el parámetro
     * 'localizacion'
     * @param response La respuesta HTTP que servirá el archivo de vídeo
     * @throws ServletException Si ocurre un error en el procesamiento del
     * servlet
     * @throws IOException Si ocurre un error de lectura o escritura de archivos
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filePath = request.getParameter("localizacion"); // Ej: "/VideosRegistrados/20250511201530.mp4"
        String filename = new File(filePath).getName(); // Extrae "20250511201530.mp4"

        String realPath = getServletContext().getRealPath("/");
        String basePath = realPath.split("target")[0];
        String videoDir = basePath + "VideosRegistrados";

        if (filename == null || filename.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro 'localizacion'");
            return;
        }

        File videoFile = new File(videoDir, filename);

        if (!videoFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "El video no fue encontrado");
            return;
        }

        response.setContentType("video/mp4");
        response.setContentLengthLong(videoFile.length());

        try (
            FileInputStream fis = new FileInputStream(videoFile); OutputStream os = response.getOutputStream()) {
            String keyString = loadAESKey();
            SecretKey key = new SecretKeySpec(keyString.getBytes(), "AES");
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al desencriptar el video");
        }
    }
    
    private String loadAESKey() throws IOException {
        String realPath = getServletContext().getRealPath("/");
        String basePath = realPath.split("target")[0];
        String folderPath = basePath + "Clave";
        Path keyPath = Paths.get(folderPath, "claveVideo.key");
        return Files.readString(keyPath).trim();
    }

    /**
     * Devuelve una descripción corta del servlet.
     *
     * @return Cadena textual con la descripción del servlet
     */
    @Override
    public String getServletInfo() {
        return "Servlet para servir archivos de vídeo desde una ruta local del sistema de archivos.";
    }
}
