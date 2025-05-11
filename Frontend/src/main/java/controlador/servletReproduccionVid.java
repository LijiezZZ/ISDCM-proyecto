package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import modelo.Usuario;
import modelo.Video;
import servicio.ServicioVideoREST;

import java.io.IOException;

/**
 * Servlet que maneja la reproducción de video y aumento de visualizaciones.
 * Permite a los usuarios autenticados reproducir sus videos.
 *
 * URL: /servletReproduccionVid
 *
 * @author Kenny Alejandro/Lijie Yin
 */
@WebServlet("/servletReproduccionVid")
public class servletReproduccionVid extends HttpServlet {

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
     * Procesa las peticiones GET.
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
        request.setAttribute("tituloBuscado", request.getParameter("titulo"));
        request.setAttribute("autorBuscado", request.getParameter("autor"));
        request.setAttribute("fechaBuscada", request.getParameter("fecha"));

        String videoIdPlay = request.getParameter("id");
        int videoId = Integer.parseInt(videoIdPlay);

        if (videoIdPlay != null) {
            try {
                Video video = servicioVideo.getVideoPorId(videoId);
                if (video != null) {
                    servicioVideo.visualizarVideo(videoId);
                    request.setAttribute("video", video);
                } else {
                    request.setAttribute("error", "El video no existe.");
                }
            } catch (IOException e) {
                request.setAttribute("error", "No se pudo cargar el video.");
            }
            request.getRequestDispatcher("vista/reproduccionVid.jsp").forward(request, response);
        }
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
        return "Servlet que gestiona la reproduccion de video";
    }
}
