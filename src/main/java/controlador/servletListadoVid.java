package controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import modelo.Usuario;
import modelo.Video;
import modelo.dao.VideoDAO;

/**
 * Servlet que maneja el listado de videos, así como las acciones de edición y eliminación.
 * Permite a los usuarios autenticados ver sus videos y gestionarlos.
 * 
 * URL: /servletListadoVid
 * 
 * Acciones soportadas:
 * - action=edit   → Redirige a la edición de un video
 * - action=delete → Elimina un video (si pertenece al usuario)
 * - action=get     o sin acción → Lista todos los videos
 * 
 * @author Kenny Alejandro/Lijie Yin
 */
@WebServlet("/servletListadoVid")
public class servletListadoVid extends HttpServlet {

    /**
     * Verifica si el usuario tiene una sesión activa y devuelve su objeto Usuario.
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @return Usuario autenticado o null si no hay sesión activa
     * @throws IOException en caso de redirección
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
     * Procesa las peticiones GET y POST según la acción: listar, editar o eliminar.
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @throws ServletException si ocurre un error en el reenvío
     * @throws IOException si ocurre un error de I/O
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Usuario user = obtenerUsuario(request, response);
        if (user == null) return;

        VideoDAO videoDAO = new VideoDAO();
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "edit":
                String videoIdEdit = request.getParameter("id");
                if (videoIdEdit != null) {
                    response.sendRedirect(request.getContextPath() + "/servletEditarVid?id=" + videoIdEdit);
                    return;
                }
                break;

            case "delete":
                String videoIdDel = request.getParameter("id");
                if (videoIdDel != null) {
                    try {
                        int videoId = Integer.parseInt(videoIdDel);

                        if (!videoDAO.isVideoOwner(videoId, user.getId())) {
                            request.setAttribute("error", "No dispone de permisos para eliminar este video.");
                        } else {
                            boolean eliminado = videoDAO.deleteVideo(videoId);
                            if (eliminado) {
                                request.setAttribute("message", "El video fue eliminado correctamente.");
                            } else {
                                request.setAttribute("error", "Hubo un problema al eliminar el video.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "ID de video inválido.");
                    }
                }
                break;

            case "get":
            default:
                // No hace falta hacer nada aquí, solo continuar con la carga
                break;
        }

        // Obtener y mostrar todos los videos
        List<Video> videos = videoDAO.getAllVideos();
        request.setAttribute("videos", videos);
        request.getRequestDispatcher("vista/listadoVid.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones GET.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Maneja las peticiones POST redirigiéndolas a GET.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Descripción corta del servlet.
     * 
     * @return Descripción textual
     */
    @Override
    public String getServletInfo() {
        return "Servlet que gestiona el listado, edición y eliminación de videos";
    }
}