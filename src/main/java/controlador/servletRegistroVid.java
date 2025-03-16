/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import modelo.Video;
import modelo.Usuario;
import modelo.dao.VideoDAO;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author alumne
 */
@MultipartConfig
@WebServlet("/servletRegistroVid")
public class servletRegistroVid extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Obtener los parámetros del formulario de registro de video
        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");
        String creationDate = request.getParameter("fechaCreacion");
        String duration = request.getParameter("duracion");
        String description = request.getParameter("descripcion");
        String format = request.getParameter("formato");

        // Conversión de los parámetros a los tipos correspondientes
        Date creationDateDateFormat = Date.valueOf(creationDate);
        Time durationTimeFormat = Time.valueOf(duration);
        
        // Ruta de localización del video
        String localization = "/videosRegistrados/" + title + "." + format;

        // Verificar si el usuario tiene una sesión activa
        HttpSession session = request.getSession(false); // false para no crear una nueva sesión si no existe
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("vista/login.jsp");
            return;
        }
        
        // Obtener el objeto Usuario de la sesión
        Usuario user = (Usuario) session.getAttribute("user");
        Integer userId = user.getId();

        // Crear el objeto Video con los datos obtenidos del formulario
        Video video = new Video(title, author, creationDateDateFormat, durationTimeFormat, 0, description, format, localization, userId);
        VideoDAO videoDAO = new VideoDAO();

        // Comprobar si el video ya ha sido registrado por este usuario
        if (videoDAO.isVideoRegistered(title, userId)) {
            request.setAttribute("error", "Este video ya ha sido registrado previamente por usted.");
            request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
            return;
        }
        
        // Intentar registrar el video en la base de datos
        if (videoDAO.registerVideo(video)) {
            response.sendRedirect(request.getContextPath() + "/servletListadoVid");
        } else {
            request.setAttribute("error", "No se pudo completar el registro");
            request.getRequestDispatcher("vista/registroVid.jsp").forward(request, response);
            return;
        }
    }

}
