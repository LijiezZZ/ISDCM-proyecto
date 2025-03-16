<%-- 
    Document   : registroVid
    Created on : 2 mar 2025, 18:31:10
    Author     : alumne
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
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
                        <input type="text" class="form-control" id="titulo" name="titulo" required>
                        <div class="invalid-feedback">El título es obligatorio.</div>
                    </div>

                    <!-- Autor -->
                    <div class="mb-3">
                        <label for="autor" class="form-label">Autor</label>
                        <input type="text" class="form-control" id="autor" name="autor" required>
                        <div class="invalid-feedback">El autor es obligatorio.</div>
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
                        <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required></textarea>
                        <div class="invalid-feedback">La descripción es obligatoria.</div>
                    </div>

                    <!-- Formato (autocompletado) -->
                    <div class="mb-3">
                        <label for="formato" class="form-label">Formato</label>
                        <input type="text" class="form-control" id="formato" name="formato" required readonly>
                    </div>

                    <button type="submit" class="btn btn-success w-100">Registrar video</button>
                </form>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-secondary w-100">Volver al listado</a>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validacionesVid.js"></script>

    </body>
</html>

