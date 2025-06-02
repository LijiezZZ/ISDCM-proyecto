<%-- 
    Document   : registroVid
    Created on : 2 mar 2025, 18:31:10
    Author     : Kenny/Lijie
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="utils.JwtUtils"%>
<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("vista/login.jsp");
        return;
    }
    String usuario = null;
    Usuario user = (Usuario) sessionUser.getAttribute("user");
    usuario = user.getUsername();
    
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
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Registrar Video</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Registrar video</a>
                <div class="d-flex align-items-center">
                    <span class="text-light me-3">Usuario: <strong><a href="${pageContext.request.contextPath}/vista/perfilUsu.jsp" style="text-decoration: none; color: inherit;"><%= usuario %></a></strong></span>
                    <a href="<%= request.getContextPath() %>/servletUsuarios?action=logout" class="btn btn-danger">Cerrar sesión</a>
                </div>
            </div>
        </nav>

        <div class="container mt-4">
            <div class="register-container">
                <h3 class="text-center">Registro de Video</h3>
                <div class="error-container" style="min-height: 50px;">
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                    <div class="alert alert-danger text-center"><%= error %></div>
                    <% } %>
                </div>
                <!-- Formulario de registro de video -->
                <form action="<%= request.getContextPath() %>/servletRegistroVid" method="post" enctype="multipart/form-data" id="videoForm" novalidate>

                    <!-- Entrada de archivo -->
                    <div class="mb-3">
                        <label for="videoFile" class="form-label">Subir video</label>
                        <input type="file" class="form-control" id="videoFile" name="videoFile" accept="video/*" required>
                        <div class="invalid-feedback">Por favor, selecciona un archivo de video.</div>
                    </div>

                    <!-- Título (autocompletado) -->
                    <div class="mb-3">
                        <label for="titulo" class="form-label">Título del video</label>
                        <input type="text" class="form-control" id="titulo" name="titulo" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ0-9.-_ ]{3,50}">
                        <div class="invalid-feedback">El título es obligatorio y debe tener entre 3 y 50 caracteres y solo puede contener letras, números, espacios, puntos y guiones.</div>
                    </div>

                    <!-- Autor -->
                    <div class="mb-3">
                        <label for="autor" class="form-label">Autor</label>
                        <input type="text" class="form-control" id="autor" name="autor" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ0-9.-_ ]{2,50}">
                        <div class="invalid-feedback">El autor es obligatorio y debe tener entre 2 y 50 caracteres y solo puede contener letras, números, espacios, puntos y guiones.</div>
                    </div>

                    <!-- Fecha de Creación (autocompletado) -->
                    <div class="mb-3">
                        <label for="fechaCreacion" class="form-label">Fecha de creación</label>
                        <input type="text" class="form-control" id="fechaCreacion" name="fechaCreacion" required readonly>
                    </div>

                    <!-- Duración (autocompletado) -->
                    <div class="mb-3">
                        <label for="duracion" class="form-label">Duración</label>
                        <input type="text" class="form-control" id="duracion" name="duracion" required readonly>
                    </div>

                    <!-- Descripción -->
                    <div class="mb-3">
                        <label for="descripcion" class="form-label">Descripción</label>
                        <textarea class="form-control" id="descripcion" name="descripcion" rows="3" pattern="[^<>]{0,500}"></textarea>
                        <div class="invalid-feedback">La descripción no puede superar los 500 caracteres.</div>
                    </div>

                    <!-- Formato (autocompletado) -->
                    <div class="mb-3">
                        <label for="formato" class="form-label">Formato</label>
                        <input type="text" class="form-control" id="formato" name="formato" required readonly>
                    </div>

                    <button type="submit" class="btn btn-dark w-100">Registrar video</button>
                    
                    <input type="hidden" name="tituloBuscado" value="${tituloBuscado}" />
                    <input type="hidden" name="autorBuscado" value="${autorBuscado}" />
                    <input type="hidden" name="fechaBuscada" value="${fechaBuscada}" />
                    
                </form>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/servletListadoVid?titulo=${tituloBuscado != null ? tituloBuscado : ''}&autor=${autorBuscado != null ? autorBuscado : ''}&fecha=${fechaBuscada != null ? fechaBuscada : ''}" class="btn btn-secondary w-100">Volver al listado</a>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validacionesVid.js"></script>

    </body>
</html>

