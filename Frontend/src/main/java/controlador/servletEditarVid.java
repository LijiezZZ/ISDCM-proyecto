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
    import servicio.ServicioVideoREST;

    /**
     * Servlet que permite editar la información de un video existente.
     * Este servlet maneja tanto peticiones GET (para mostrar el formulario)
     * como POST (para actualizar los datos en la base de datos vía API REST).
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
        * Servicio REST que permite interactuar con los recursos de vídeo del backend.
        *
        * Esta instancia se utiliza para realizar operaciones como obtener la lista de vídeos,
        * registrar nuevos vídeos, actualizar información o eliminar registros,
        * comunicándose con el backend mediante peticiones HTTP (GET, POST, PUT, DELETE).
        *
        * El servicio encapsula la lógica de acceso remoto y deserialización de respuestas JSON.
        */
        private final ServicioVideoREST servicio = new ServicioVideoREST();

        private Usuario obtenerUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("vista/login.jsp");
                return null;
            }
            return (Usuario) session.getAttribute("user");
        }

        private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, String errorMsg)
                throws ServletException, IOException {

            String idParam = request.getParameter("videoId");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    Video video = servicio.getVideoPorId(id);
                    if (video != null) {
                        request.setAttribute("video", video);
                    }
                } catch (NumberFormatException | IOException e) {
                    // Ignorado, mensaje de error ya proporcionado
                }
            }

            request.setAttribute("error", errorMsg);
            request.getRequestDispatcher("vista/editarVid.jsp").forward(request, response);
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            request.setAttribute("tituloBuscado", request.getParameter("titulo"));
            request.setAttribute("autorBuscado", request.getParameter("autor"));
            request.setAttribute("fechaBuscada", request.getParameter("fecha"));
            Usuario user = obtenerUsuario(request, response);
            if (user == null) return;

            String videoIdParam = request.getParameter("id");

            if (videoIdParam != null) {
                try {
                    int videoId = Integer.parseInt(videoIdParam);
                    Video video = servicio.getVideoPorId(videoId);

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

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            request.setAttribute("tituloBuscado", request.getParameter("tituloBuscado"));
            request.setAttribute("autorBuscado", request.getParameter("autorBuscado"));
            request.setAttribute("fechaBuscada", request.getParameter("fechaBuscada"));
            Usuario user = obtenerUsuario(request, response);
            if (user == null) return;

            String idVideo = request.getParameter("videoId");
            String titulo = request.getParameter("nuevoTitulo");
            String autor = request.getParameter("nuevoAutor");
            String descripcion = request.getParameter("nuevaDescripcion");
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

                request.setAttribute("nuevoTitulo", titulo);
                request.setAttribute("nuevoAutor", autor);
                request.setAttribute("nuevaDescripcion", descripcion);
                mostrarFormularioConError(request, response, "Todos los campos son obligatorios.");
                return;
            }

            Video actualizado = new Video(titulo, autor, descripcion);
            boolean ok = servicio.actualizarVideo(idVid, actualizado);

            if (ok) {
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
                request.setAttribute("nuevoTitulo", titulo);
                request.setAttribute("nuevoAutor", autor);
                request.setAttribute("nuevaDescripcion", descripcion);
                mostrarFormularioConError(request, response, "No se pudo completar la actualización.");
            }
        }

        @Override
        public String getServletInfo() {
            return "Servlet que maneja la edición de videos";
        }
    }
