/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Usuario;
import modelo.Video;
import modelo.dao.VideoDAO;

/**
 * Servlet que permite editar la información de un video existente.
 * Este servlet maneja tanto peticiones GET (para mostrar el formulario)
 * como POST (para actualizar los datos en la base de datos).
 * 
 * Requiere que el usuario esté autenticado y sea propietario del video.
 * 
 * URL: /servletEditarVid
 * 
 * @author Kenny Alejandro
 */
@MultipartConfig
@WebServlet("/servletEditarVid")
public class servletEditarVid extends HttpServlet {

    /**
     * Verifica si el usuario tiene una sesión activa y devuelve su objeto Usuario.
     * Si no hay sesión activa, redirige al login.
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @return Objeto Usuario si hay sesión válida; null si no
     * @throws IOException si hay un error de redirección
     */
    private Usuario obtenerUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("vista/login.jsp");
            return null;
        }
        return (Usuario) session.getAttribute("user");
    }

    /**
     * Muestra el formulario de edición con un mensaje de error.
     * Intenta cargar el video desde la base de datos usando el ID del formulario.
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @param errorMsg Mensaje de error que se mostrará en el JSP
     * @throws ServletException si hay un error de reenvío
     * @throws IOException si hay un error de entrada/salida
     */
    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, String errorMsg)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("videoId"); // viene del formulario POST
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                VideoDAO videoDAO = new VideoDAO();
                Video video = videoDAO.getVideo(id);
                if (video != null) {
                    request.setAttribute("video", video);
                }
            } catch (NumberFormatException e) {
                // Ignorar error, ya se maneja más arriba
            }
        }

        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
    }

    /**
     * Maneja la petición GET para mostrar el formulario de edición de video.
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @throws ServletException si hay un error de reenvío
     * @throws IOException si hay un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario user = obtenerUsuario(request, response);
        if (user == null) return;

        String videoIdParam = request.getParameter("id");

        if (videoIdParam != null) {
            try {
                int videoId = Integer.parseInt(videoIdParam);
                VideoDAO videoDAO = new VideoDAO();
                Video video = videoDAO.getVideo(videoId);

                if (video != null) {
                    request.setAttribute("video", video);
                } else {
                    request.setAttribute("error", "No se localizó ningún video con ese ID.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de video inválido.");
            }
        } else {
            request.setAttribute("error", "No se indicó el ID del video.");
        }

        request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
    }

    /**
     * Maneja la petición POST para actualizar los datos de un video.
     * Verifica si el usuario tiene permiso de editarlo.
     * 
     * @param request Petición HTTP con datos del formulario
     * @param response Respuesta HTTP
     * @throws ServletException si hay un error de reenvío
     * @throws IOException si hay un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario user = obtenerUsuario(request, response);
        if (user == null) return;

        String idVideo = request.getParameter("videoId");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String descripcion = request.getParameter("descripcion");
        String videoUserId = request.getParameter("userId");

        int vidUserId, idVid;

        try {
            vidUserId = Integer.parseInt(videoUserId);
            idVid = Integer.parseInt(idVideo);
        } catch (NumberFormatException e) {
            mostrarFormularioConError(request, response, "ID inválido del video o del usuario.");
            return;
        }

        if (vidUserId != user.getId()) {
            mostrarFormularioConError(request, response, "No dispone de permisos para efectuar este cambio.");
            return;
        }

        if (titulo == null || titulo.trim().isEmpty() ||
            autor == null || autor.trim().isEmpty() ||
            descripcion == null || descripcion.trim().isEmpty()) {

            request.setAttribute("titulo", titulo);
            request.setAttribute("autor", autor);
            request.setAttribute("descripcion", descripcion);
            mostrarFormularioConError(request, response, "Todos los campos son obligatorios.");
            return;
        }

        VideoDAO videoDAO = new VideoDAO();
        boolean actualizado = videoDAO.updateVideo(idVid, titulo, autor, descripcion);

        if (actualizado) {
            response.sendRedirect(request.getContextPath() + "/servletListadoVid");
        } else {
            request.setAttribute("titulo", titulo);
            request.setAttribute("autor", autor);
            request.setAttribute("descripcion", descripcion);
            mostrarFormularioConError(request, response, "No se pudo completar la actualización.");
        }
    }

    /**
     * Devuelve una breve descripción del servlet.
     * 
     * @return Descripción del servlet
     */
    @Override
    public String getServletInfo() {
        return "Servlet que maneja la edición de videos";
    }
}
