<%-- 
    Document   : datosUsu
    Created on : 17 mar 2025, 17:10:33
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
    String usuario = user.getUsername();
    String email = user.getEmail();
    String nombre = user.getNombre();
    String apellidos = user.getApellidos();
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Datos de Usuario</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Perfil de Usuario</a>
                <div class="d-flex align-items-center">
                    <span class="text-light me-3">Usuario: 
                        <strong><a href="${pageContext.request.contextPath}/vista/perfilUsu.jsp" style="text-decoration: none; color: inherit;"><%= usuario %></a></strong>
                    </span>
                    <a href="<%= request.getContextPath() %>/servletUsuarios?action=logout" class="btn btn-danger">Cerrar sesión</a>
                </div>
            </div>
        </nav>

        <!-- Contenedor principal -->
        <div class="container">
            <div class="register-container">
                <h3 class="text-center">Datos de Usuario</h3>

                <div class="error-container" style="min-height: 50px;">
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                    <div class="alert alert-danger text-center"><%= error %></div>
                    <% } %>
                </div>

                <!-- Formulario de actualización de datos -->
                <form action="<%= request.getContextPath() %>/servletUsuarios?action=update" method="post" id="registroForm" novalidate>

                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" value="<%= usuario %>" readonly>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Correo Electrónico</label>
                        <input type="email" class="form-control" id="email" name="email" value="<%= email %>" readonly>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Nombre</label>
                        <input type="email" class="form-control" id="email" name="email" value="<%= nombre %>" readonly>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Apellidos</label>
                        <input type="email" class="form-control" id="email" name="email" value="<%= apellidos %>" readonly>
                    </div>


                    <h4 class="text-center mt-4">Cambiar Contraseña</h4>

                    <div class="mb-3">
                        <label for="old_password" class="form-label">Contraseña Actual</label>
                        <input type="password" class="form-control" id="old_password" name="old_password" required>
                        <div class="invalid-feedback">Introduce tu contraseña actual.</div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Nueva Contraseña</label>
                        <input type="password" class="form-control" id="password" name="password" required minlength="6">
                        <div class="invalid-feedback">Debe tener al menos 6 caracteres.</div>
                    </div>

                    <div class="mb-3">
                        <label for="confirm_password" class="form-label">Confirmar Nueva Contraseña</label>
                        <input type="password" class="form-control" id="confirm_password" name="confirm_password" required>
                        <div class="invalid-feedback">Las contraseñas no coinciden.</div>
                    </div>

                    <button type="submit" class="btn btn-dark w-100">Actualizar contraseña</button>
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-secondary w-100">Volver al listado</a>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validaciones.js"></script>

    </body>
</html>

