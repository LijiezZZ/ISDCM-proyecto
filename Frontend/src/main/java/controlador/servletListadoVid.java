package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import modelo.Usuario;
import modelo.Video;
import servicio.ServicioVideoREST;

import java.io.IOException;
import java.util.List;

/**
 * Servlet que maneja el listado de videos, así como las acciones de edición y
 * eliminación. Permite a los usuarios autenticados ver sus videos y
 * gestionarlos.
 *
 * URL: /servletListadoVid
 *
 * Acciones soportadas: - action=edit → Redirige a la edición de un video -
 * action=delete → Elimina un video (si pertenece al usuario) - action=get o sin
 * acción → Lista todos los videos
 *
 * @author Kenny Alejandro/Lijie Yin
 */
@WebServlet("/servletListadoVid")
public class servletListadoVid extends HttpServlet {

    private final ServicioVideoREST servicioVideo = new ServicioVideoREST();

    /**
     * Verifica si el usuario tiene una sesión activa y devuelve su objeto
     * Usuario.
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
     * Procesa las peticiones GET y POST según la acción: listar, editar o
     * eliminar.
     *
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @throws ServletException si ocurre un error en el reenvío
     * @throws IOException si ocurre un error de I/O
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario user = obtenerUsuario(request, response);
        if (user == null) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");
        String date = request.getParameter("fecha");
        title = (title != null && !title.trim().isEmpty()) ? title : null;
        author = (author != null && !author.trim().isEmpty()) ? author : null;
        date = (date != null && !date.trim().isEmpty()) ? date : null;

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

                        if (!servicioVideo.esPropietario(videoId, user.getId())) {
                            request.setAttribute("error", "No dispone de permisos para eliminar este video.");
                        } else {
                            boolean eliminado = servicioVideo.eliminarVideo(videoId);
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
                // Sin acción específica, continuar con listado
                break;
        }

        try {
            List<Video> videos = servicioVideo.buscarVideos(title, author, date);

            if (videos != null && !videos.isEmpty()) {
                request.setAttribute("videos", videos);
            } else {
                request.setAttribute("warning", "No se encontraron videos que coincidan con los filtros.");
            }

        } catch (IOException e) {
            request.setAttribute("error", "Ocurrió un error al cargar los videos desde el backend.");
            e.printStackTrace(); // Opcional: útil si estás depurando
        }

// Mostrar la vista del listado
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
