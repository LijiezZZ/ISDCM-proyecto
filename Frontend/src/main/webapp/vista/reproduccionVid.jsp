<%-- 
    Document   : reproduccion
    Created on : 14 abr 2025, 20:27:31
    Author     : alumne
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Video" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="utils.JwtUtils"%>

<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("vista/login.jsp");
        return;
    }

    Video video = (Video) request.getAttribute("video");
    Usuario user = (Usuario) sessionUser.getAttribute("user");
    String usuario = user.getUsername();
    
    String token = (String) session.getAttribute("jwt");
    if (token == null || JwtUtils.isTokenExpired(token)) {
        response.sendRedirect("vista/login.jsp?error=" + URLEncoder.encode("Tu sesion ha expirado. Por favor, inicia sesion nuevamente.", "UTF-8"));
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Reproducción Video</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap & Video.js CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://vjs.zencdn.net/8.22.0/video-js.css" rel="stylesheet" />

        <!-- Estilos personalizados -->
        <style>
            body {
                background-color: #121212;
                color: #f8f9fa;
            }
            .card {
                background-color: #1e1e1e;
                border: none;
                max-width: 800px;
                margin: 0 auto;
            }
            .video-container {
                max-width: 800px;
                margin: 0 auto;
            }
            .video-js {
                border-radius: 12px;
            }
            .btn-danger {
                background-color: #ff3b30;
                border: none;
                color: #fff;
            }
        </style>
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Reproductor de Video</a>
                <div class="d-flex align-items-center">
                    <span class="text-light me-3">Usuario: <strong><a href="${pageContext.request.contextPath}/vista/perfilUsu.jsp" style="text-decoration: none; color: inherit;"><%= usuario %></a></strong></span>
                    <a href="<%= request.getContextPath() %>/servletUsuarios?action=logout" class="btn btn-danger">Cerrar sesión</a>
                </div>
            </div>
        </nav>

        <!-- Contenido principal -->
        <div class="container py-5">
            <div class="video-container mb-4">
                <video id="video-player" class="video-js" autoplay controls preload="auto" width="100%" height="auto" data-setup="{}">
                    <!--<source src="<%= request.getContextPath() + video.getLocalizacion() %>" type="video/mp4">-->
                    <source src="<%= request.getContextPath() %>/servletVid?localizacion=<%= video.getLocalizacion() %>" type="video/mp4">

                    Tu navegador no soporta la reproducción de video.
                </video>
            </div>

            <div class="card text-light">
                <div class="card-body">
                    <h3 class="card-title"><%= video.getTitulo() %></h3>
                    <p class="card-text"><strong>Autor:</strong> <%= video.getAutor() %></p>
                    <p class="card-text"><strong>Fecha de creación:</strong> <%= video.getFechaCreacion() %></p>
                    <p class="card-text"><strong>Duración:</strong> <%= video.getDuracion() %></p>
                    <p class="card-text"><strong>Reproducciones:</strong> <%= video.getNumReproducciones() %></p>
                    <p class="card-text"><strong>Descripción:</strong> <%= video.getDescripcion() %></p>
                    <p class="card-text"><strong>Formato:</strong> <%= video.getFormato() %></p>
                    <a href="<%= request.getContextPath() %>/servletListadoVid?titulo=${tituloBuscado != null ? tituloBuscado : ''}&autor=${autorBuscado != null ? autorBuscado : ''}&fecha=${fechaBuscada != null ? fechaBuscada : ''}" class="btn btn-outline-light mt-3">Volver al listado</a>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://vjs.zencdn.net/8.22.0/video.min.js"></script>

    </body>
</html>



