<%-- 
    Document   : listadoVid
    Created on : 2 mar 2025, 18:31:20
    Author     : Kenny/Lijie
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Video" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="utils.JwtUtils"%>
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
    
    String token = (String) session.getAttribute("jwt");
    if (token == null || JwtUtils.isTokenExpired(token)) {
        response.sendRedirect("login.jsp?error=" + URLEncoder.encode("Tu sesion ha expirado. Por favor, inicia sesion nuevamente.", "UTF-8"));
        return;
    }
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
                    <span class="text-light me-3">Usuario: <strong><a href="${pageContext.request.contextPath}/vista/perfilUsu.jsp" style="text-decoration: none; color: inherit;"><%= usuario %></a></strong></span>
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
                    setTimeout(function () {
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
                    setTimeout(function () {
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
                    setTimeout(function () {
                        var alertElement = document.querySelector('.alert-warning');
                        if (alertElement) {
                            alertElement.style.display = 'none';
                        }
                    }, 10000);
                </script>
            </c:if>
            <!--<div class="table-responsive" style="height: calc(100vh - 200px);">-->
            <div class="row align-items-center mb-3">
                <!-- Formulario de búsqueda -->
                <div class="col-lg-10 col-md-9 col-sm-12">
                    <form class="row gx-2 gy-2 align-items-center" method="get" action="${pageContext.request.contextPath}/servletListadoVid">
                        <div class="col-auto">
                            <input type="text" name="titulo" class="form-control form-control-sm" placeholder="Título" style="width: 150px;" value="${tituloBuscado != null ? tituloBuscado : ''}">
                        </div>
                        <div class="col-auto">
                            <input type="text" name="autor" class="form-control form-control-sm" placeholder="Autor" style="width: 150px;" value="${autorBuscado != null ? autorBuscado : ''}">
                        </div>
                        <div class="col-auto">
                            <input type="text" name="fecha" class="form-control form-control-sm" placeholder="Fecha" style="width: 170px;" value="${fechaBuscada != null ? fechaBuscada : ''}"
                                   data-bs-toggle="tooltip" data-bs-placement="top" title="La fecha puede ser YYYY-MM-DD, YYYY-MM o solo YYYY">
                        </div>

                        <div class="col-auto">
                            <button type="submit" class="btn btn-dark btn-sm">Buscar</button>
                        </div>
                        <div class="col-auto">
                            <a href="servletListadoVid" class="btn btn-secondary btn-sm">Limpiar</a>
                        </div>
                    </form>
                </div>

                <!-- Botón agregar video -->
                <div class="col-lg-2 col-md-3 col-sm-12 text-end mt-2 mt-md-0">

                    <a href="${pageContext.request.contextPath}/servletRegistroVid?titulo=${param.titulo}&autor=${param.autor}&fecha=${param.fecha}" class="btn btn-dark">
                        <i class="fas fa-plus"></i>
                    </a>
                    <a href="${pageContext.request.contextPath}/vista/xml.jsp" class="btn btn-secondary">
                        <i class="fas fa-lock"></i>
                    </a>
                </div>
            </div>


            <div class="custom-table-card p-3">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered table-dark mb-0">
                        <thead>
                            <tr>
                                <th class="text-center align-middle">TÍTULO</th>
                                <th class="text-center align-middle">AUTOR</th>
                                <th class="text-center align-middle">FECHA DE CREACIÓN</th>
                                <th class="text-center align-middle">DURACIÓN</th>
                                <th class="text-center align-middle">REPRODUCCCIONES</th>
                                <th class="text-center align-middle">DESCRIPCIÓN</th>
                                <th class="text-center align-middle">FORMATO</th>
                                <th class="text-center align-middle">LOCALIZACIÓN</th>
                                <th class="text-center align-middle">ACCIONES</th>
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
                                        <a href="${pageContext.request.contextPath}/servletReproduccionVid?id=${video.id}&titulo=${param.titulo}&autor=${param.autor}&fecha=${param.fecha}" class="btn btn-success btn-sm">
                                            <i class="fas fa-play"></i>
                                        </a>
                                        <c:if test="${video.userId == user.id}">
                                            <a href="<%= request.getContextPath() %>/servletListadoVid?action=edit&id=${video.id}&titulo=${param.titulo}&autor=${param.autor}&fecha=${param.fecha}" class="btn btn-warning btn-sm">

                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="<%= request.getContextPath() %>/servletListadoVid?action=delete&id=${video.id}&titulo=${param.titulo}&autor=${param.autor}&fecha=${param.fecha}" 
                                               class="btn btn-danger btn-sm" 
                                               onclick="return confirm('¿Estás seguro de que quieres eliminar este video?');">
                                                <i class="fas fa-trash-alt"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                   // Inicializar todos los tooltips
                                                   document.addEventListener('DOMContentLoaded', function () {
                                                       var tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
                                                       var tooltipList = [...tooltipTriggerList].map(function (tooltipTriggerEl) {
                                                           return new bootstrap.Tooltip(tooltipTriggerEl)
                                                       });
                                                   });
        </script>
    </body>
</html>

