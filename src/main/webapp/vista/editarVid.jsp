<%-- 
    Document   : registroVid
    Created on : 2 mar 2025, 18:31:10
    Author     : Kenny/Lijie
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Verificación de sesión (lógica de respaldo)
    jakarta.servlet.http.HttpSession sessionUser = request.getSession(false);
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
    <title>Edición de Video</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="container mt-4">
    <div class="register-container">
        <h3 class="text-center">Edición de Video</h3>

        <!-- Mensaje de error -->
        <div class="error-container" style="min-height: 50px;">
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">${error}</div>
            </c:if>
        </div>

        <!-- Formulario de edición de video -->
        <form action="${pageContext.request.contextPath}/servletEditarVid" method="post" enctype="multipart/form-data" id="videoFormEdi" novalidate>

            <!-- Título -->
            <div class="mb-3">
                <label for="titulo" class="form-label">Título del video</label>
                <input type="text" class="form-control" id="titulo" name="titulo"
                       required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ0-9 .\\-_]{3,50}"
                       value="${not empty param.titulo ? param.titulo : (not empty titulo ? titulo : video.titulo)}">
                <div class="invalid-feedback">El título es obligatorio y debe tener entre 3 y 50 caracteres.</div>
            </div>

            <!-- Autor -->
            <div class="mb-3">
                <label for="autor" class="form-label">Autor</label>
                <input type="text" class="form-control" id="autor" name="autor"
                       required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ0-9 .\\-_]{2,50}"
                       value="${not empty param.autor ? param.autor : (not empty autor ? autor : video.autor)}">
                <div class="invalid-feedback">El autor es obligatorio y debe tener entre 2 y 50 caracteres.</div>
            </div>

            <!-- Fecha de Creación -->
            <div class="mb-3">
                <label for="fechaCreacion" class="form-label">Fecha de creación</label>
                <input type="text" class="form-control" id="fechaCreacion" name="fechaCreacion" readonly
                       value="${video.fechaCreacion}">
            </div>

            <!-- Duración -->
            <div class="mb-3">
                <label for="duracion" class="form-label">Duración</label>
                <input type="text" class="form-control" id="duracion" name="duracion" readonly
                       value="${video.duracion}">
            </div>

            <!-- Descripción -->
            <div class="mb-3">
                <label for="descripcion" class="form-label">Descripción</label>
                <textarea class="form-control" id="descripcion" name="descripcion" rows="3"
                          pattern="[^<>]{0,500}">${not empty param.descripcion ? param.descripcion : (not empty descripcion ? descripcion : video.descripcion)}</textarea>
                <div class="invalid-feedback">La descripción no puede superar los 500 caracteres.</div>
            </div>

            <!-- Formato -->
            <div class="mb-3">
                <label for="formato" class="form-label">Formato</label>
                <input type="text" class="form-control" id="formato" name="formato" readonly
                       value="${video.formato}">
            </div>

            <!-- Campos ocultos -->
            <input type="hidden" name="userId" value="${video.userId}">
            <input type="hidden" name="videoId" value="${video.id}">

            <!-- Botón de envío -->
            <button type="submit" class="btn btn-dark w-100">Actualizar video</button>
        </form>

        <!-- Volver al listado -->
        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-secondary w-100">Volver al listado</a>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validacionesVidEdi.js"></script>

</body>
</html>

