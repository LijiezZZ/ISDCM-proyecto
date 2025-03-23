<%-- 
    Document   : registroVid
    Created on : 2 mar 2025, 18:31:10
    Author     : alumne
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Video" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
//    Video video = request.getAttribute("video");
   
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
                <h3 class="text-center">Edición de Video</h3>
                <div class="error-container" style="min-height: 50px;">
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                    <div class="alert alert-danger text-center"><%= error %></div>
                    <% } %>
                </div>
                <!-- Formulario de edición de video -->
                <form action="<%= request.getContextPath() %>/servletEditarVid" method="post" enctype="multipart/form-data" id="videoFormEdi" novalidate>
                    
                    
                    <!-- Título (autocompletado)-->
                    <div class="mb-3">
                        <label for="titulo" class="form-label">Título del video</label>
                        <input type="text" class="form-control" id="titulo" name="titulo" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ0-9.-_ ]{3,50}" value=${video.titulo}>
                        <div class="invalid-feedback">El título es obligatorio y debe tener entre 3 y 50 caracteres y solo puede contener letras, números, espacios, puntos y guiones.</div>
                    </div>

                    <!-- Autor -->
                    <div class="mb-3">
                        <label for="autor" class="form-label">Autor</label>
                        <input type="text" class="form-control" id="autor" name="autor" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ0-9.-_ ]{2,50}" value=${video.autor}>
                        <div class="invalid-feedback">El autor es obligatorio y debe tener entre 2 y 50 caracteres y solo puede contener letras, números, espacios, puntos y guiones.</div>
                    </div>

                    <!-- Fecha de Creación (autocompletado) -->
                    <div class="mb-3">
                        <label for="fechaCreacion" class="form-label">Fecha de creación</label>
                        <input type="text" class="form-control" id="fechaCreacion" name="fechaCreacion" required readonly value=${video.fechaCreacion}>
                    </div>

                    <!-- Duración (autocompletado) -->
                    <div class="mb-3">
                        <label for="duracion" class="form-label">Duración</label>
                        <input type="text" class="form-control" id="duracion" name="duracion" required readonly value=${video.duracion}>
                    </div>

                    <!-- Descripción -->
                    <div class="mb-3">
                        <label for="descripcion" class="form-label">Descripción</label>
                        <textarea class="form-control" id="descripcion" name="descripcion" rows="3" pattern="[^<>]{0,500}">${video.descripcion}</textarea>
                        <div class="invalid-feedback">La descripción no puede superar los 500 caracteres.</div>
                    </div>

                    <!-- Formato (autocompletado) -->
                    <div class="mb-3">
                        <label for="formato" class="form-label">Formato</label>
                        <input type="text" class="form-control" id="formato" name="formato" required readonly value="${video.formato}">
                    </div>
                    
                    <!-- UserId-->
                    <input type="hidden" class="form-control" id="userId" name="userId" value="${video.userId}">
                    
                    <!--VideoId-->
                    <input type="hidden" class="form-control" id="videoId" name="videoId" value="${video.id}">
                    <button type="submit" class="btn btn-dark w-100">Actualizar video</button>
                </form>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-secondary w-100">Volver al listado</a>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validacionesVidEdi.js"></script>

    </body>
</html>

