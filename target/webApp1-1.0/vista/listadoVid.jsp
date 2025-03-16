<%-- 
    Document   : listadoVid
    Created on : 2 mar 2025, 18:31:20
    Author     : alumne
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Video" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    //if (request.getAttribute("videos") == null) {
        //response.sendRedirect(request.getContextPath() + "/servletListadoVid");
       // return;
   // }
   
    List<Video> videos = (List<Video>) request.getAttribute("videos");
    String usuario = null;
    Usuario user = (Usuario) sessionUser.getAttribute("user");
    usuario = user.getUsername();
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Listado Videos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    </head>
    <body>
        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Gestor de Videos</a>
                <div class="d-flex align-items-center">
                    <span class="text-light me-3">Usuario: <strong><%= usuario%></strong></span>
                    <a href="<%= request.getContextPath() %>/servletUsuarios?action=logout" class="btn btn-danger">Cerrar sesión</a>
                </div>
            </div>
        </nav>

        <div class="container-fluid mt-4">
            <h2 class="text-center">Listado de Videos</h2>
            
            <!-- Mostrar mensaje de éxito -->
        <c:if test="${not empty message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                 ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <script>
                // Ocultar el mensaje después de 10 segundos
                setTimeout(function() {
                    var alertElement = document.querySelector('.alert-success');
                    if (alertElement) {
                        alertElement.style.display = 'none';
                    }
                }, 10000);
            </script>
        </c:if>

        <!-- Mostrar mensaje de error -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Error:</strong> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <script>
                // Ocultar el mensaje después de 10 segundos
                setTimeout(function() {
                    var alertElement = document.querySelector('.alert-danger');
                    if (alertElement) {
                        alertElement.style.display = 'none';
                    }
                }, 10000);
            </script>
        </c:if>

        <!-- Mostrar mensaje de advertencia -->
            <c:if test="${not empty warning}">
                <div class="alert alert-warning alert-dismissible fade show" role="alert">
                    <strong>Advertencia:</strong> ${warning}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <script>
                    // Ocultar el mensaje después de 10 segundos
                    setTimeout(function() {
                        var alertElement = document.querySelector('.alert-warning');
                        if (alertElement) {
                            alertElement.style.display = 'none';
                        }
                    }, 10000);
                </script>
            </c:if>
             <!-- Botón para agregar nuevo video -->
            <div class=" mb-1 text-end">
                <a href="vista/registroVid.jsp" class="btn btn-black">
                    <i class="fas fa-plus"></i>
                </a>
            </div>
             <div class="table-responsive" style="height: calc(100vh - 200px);">
                <table class="table table-striped table-bordered mt-3">
                    <thead class="table-dark">
                        <tr>
                            <th>Título</th>
                            <th>Autor</th>
                            <th>Fecha de creación</th>
                            <th>Duración</th>
                            <th>Número de reproducciones</th>
                            <th>Descripción</th>
                            <th>Formato</th>
                            <th>Localización</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty videos}">
                            <tr>
                                <td colspan="9" class="text-center">No hay videos para mostrar.</td>
                            </tr>
                        </c:if>

                        <c:forEach var="video" items="${videos}">
                            <tr>
                                <td>${video.titulo}</td>
                                <td>${video.autor}</td>
                                <td>${video.fechaCreacion}</td>
                                <td>${video.duracion}</td>
                                <td>${video.numReproducciones}</td>
                                <td>${video.descripcion}</td>
                                <td>${video.formato}</td>
                                <td>${video.localizacion}</td>
                                <td>
                                    <!-- Icono Editar -->
                                    <a href="formularioEditarVideo.jsp?id=${video.id}" class="btn btn-warning btn-sm">
                                        <i class="fas fa-edit"></i> <!-- Icono de lápiz -->
                                    </a>

                                    <!-- Icono Eliminar -->
                                    <a href="<%= request.getContextPath() %>/servletListadoVid?action=delete&id=${video.id}" class="btn btn-danger btn-sm" onclick="return confirm('¿Estás seguro de que quieres eliminar este video?');">
                                        <i class="fas fa-trash-alt"></i> <!-- Icono de tacho de basura -->
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
             </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

