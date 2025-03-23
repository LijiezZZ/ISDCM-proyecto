/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

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
import java.util.List;
import modelo.Usuario;
import modelo.Video;
import modelo.dao.VideoDAO;


/**
 *
 * @author kennyalejandro
 */
//@WebServlet(name = "servletEditarVid", urlPatterns = {"/servletEditarVid"})
@MultipartConfig
@WebServlet("/servletEditarVid")
public class servletEditarVid extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         // Verificar si el usuario tiene una sesión activa
        HttpSession session = request.getSession(false); // false para no crear una nueva sesión si no existe
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("vista/login.jsp");
            return;
        }

        // Crear una instancia del VideoDAO
        VideoDAO videoDAO = new VideoDAO();
        
        String videoIdParam = request.getParameter("id");
        if (videoIdParam != null) {
            int videoId = Integer.parseInt(videoIdParam);
            // Obtener videos desde la base de datos
            Video video = videoDAO.getVideo(videoId);
            // Pasar los videos al JSP
            request.setAttribute("video", video);

            if(video.getId() == null){
                request.setAttribute("error", "No se localizo ningun video con ese id");
            }
        }
        else{
            request.setAttribute("error", "No se indico el id del video");
        }
        request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
     // Obtener los parámetros del formulario de registro de video
     
        String idVideo = request.getParameter("videoId");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String descripcion = request.getParameter("descripcion");
        String videoUserId= request.getParameter("userId");
        
        int vidUserId = videoUserId != null ? Integer.parseInt(videoUserId):0;
        int idVid = (idVideo!= null)?Integer.parseInt(idVideo):0;
        
        if(vidUserId == 0 || idVid == 0){
            request.setAttribute("error", "No se dispone del id de video o userId");
            
            processRequest(request, response);
            //request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
            return;
        }
        
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
        VideoDAO videoDAO = new VideoDAO();
        
        // Comprobar si el video ya ha sido registrado por este usuario
        if (vidUserId != userId) {
            request.setAttribute("error", "No dispone de permisos para efectuar este cambio");
            processRequest(request, response);
            //request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
            return;
        }
        
        
        
        
        // Intentar registrar el video en la base de datos
        if (videoDAO.updateVideo(idVid,titulo,autor,descripcion)) {
            response.sendRedirect(request.getContextPath() + "/servletListadoVid");
        } else {
            request.setAttribute("error", "No se pudo completar el registro");
            processRequest(request, response);
//            request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
            return;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
     @Override
    public String getServletInfo() {
        return "Servlet que maneja el listado de videos";
    }
}
