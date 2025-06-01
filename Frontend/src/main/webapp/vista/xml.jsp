<%-- 
    Document   : xml
    Created on : 25 may 2025, 16:51:36
    Author     : alumne
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Usuario user = (Usuario) sessionUser.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Encriptar / Desencriptar XML</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Encriptar / Desencriptar XML</a>
                <div class="d-flex align-items-center">
                    <span class="text-light me-3">Usuario: <strong><a href="${pageContext.request.contextPath}/vista/perfilUsu.jsp" style="text-decoration: none; color: inherit;"><%= user.getUsername() %></a></strong></span>
                    <a href="<%= request.getContextPath() %>/servletUsuarios?action=logout" class="btn btn-danger">Cerrar sesión</a>
                </div>
            </div>
        </nav>

        <div class="container py-5">
            <h2 class="text-center mb-4">Encriptar / Desencriptar archivos XML</h2>

            <div class="d-flex justify-content-center gap-3 mb-4">
                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#encryptModal">Encriptar XML</button>
                <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#decryptModal">Desencriptar XML</button>
                <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-secondary">Volver a Listado</a>
            </div>

            <% if (request.getAttribute("xmlResult") != null) { %>
            <div class="card">
                <div class="card-header"><%= request.getAttribute("xmlFileName") %></div>
                <div class="card-body">
                    <pre><%= request.getAttribute("xmlResult") %></pre>
                </div>
            </div>
            <% } %>
        </div>

        <!-- Modal Encriptar -->
        <div class="modal fade" id="encryptModal" tabindex="-1" aria-labelledby="encryptModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form method="post" action="${pageContext.request.contextPath}/servletXML?action=encrypt" enctype="multipart/form-data">
                        <div class="modal-header">
                            <h5 class="modal-title" id="encryptModalLabel">Seleccionar XML a encriptar</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="file" name="xmlfile" accept=".xml" class="form-control" required>
                            <br>
                            <div class="mb-3"> 
                                <label for="encrypt-mode" class="form-label">Modo de encriptación</label> 
                                <select class="form-select" id="encrypt-mode" name="mode" required>
                                    <option value="content">Contenido de un elemento</option>
                                    <option value="element" selected>Elemento</option>
                                    <option value="document">Todo el documento</option> 
                                </select> 
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-dark">Encriptar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal Desencriptar -->
        <div class="modal fade" id="decryptModal" tabindex="-1" aria-labelledby="decryptModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form method="post" action="${pageContext.request.contextPath}/servletXML?action=decrypt" enctype="multipart/form-data">
                        <div class="modal-header">
                            <h5 class="modal-title" id="decryptModalLabel">Seleccionar XML a desencriptar</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="file" name="xmlfile" accept=".xml" class="form-control" required>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-dark">Desencriptar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
