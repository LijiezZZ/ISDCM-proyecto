/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import modelo.Video;
import modelo.dao.VideoDAO;

/**
 *
 * @author alumne
 */
//@WebServlet(name = "servletListadoVid", urlPatterns = {"/servletListadoVid"})

@WebServlet("/servletListadoVid")
public class servletListadoVid extends HttpServlet {

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
        
        // Crear una instancia del VideoDAO
        VideoDAO videoDAO = new VideoDAO();
        
        // Obtener la acción que viene en el request
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // Si la acción es "delete", eliminamos el video
            String videoIdParam = request.getParameter("id");
            if (videoIdParam != null) {
                int videoId = Integer.parseInt(videoIdParam);
                boolean isDeleted = videoDAO.deleteVideo(videoId);

                // Puedes agregar un mensaje o redirigir a la misma página
                if (isDeleted) {
                    request.setAttribute("message", "El video fue eliminado correctamente.");
                } else {
                    request.setAttribute("error", "Hubo un problema al eliminar el video.");
                }
            }
        }

        // Obtener todos los videos desde la base de datos
        List<Video> videos = videoDAO.getAllVideos();

        // Pasar los videos al JSP
        request.setAttribute("videos", videos);

        // Redirigir a la vista/listadoVid.jsp para mostrar los videos
        request.getRequestDispatcher("vista/listadoVid.jsp").forward(request, response);
        
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet servletListadoVid</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet servletListadoVid at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
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
        doGet(request, response);
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
