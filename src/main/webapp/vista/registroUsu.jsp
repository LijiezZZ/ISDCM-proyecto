<%-- 
    Document   : registroUsu
    Created on : 2 mar 2025, 18:31:00
    Author     : alumne
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Registro de usuario</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>

        <div class="container">
            <div class="register-container">
                <h3 class="text-center">Registro de Usuario</h3>
                <div class="error-container" style="min-height: 50px;">
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                    <div class="alert alert-danger text-center"><%= error %></div>
                    <% } %>
                </div>
                <form action="<%= request.getContextPath() %>/servletUsuarios?action=register" method="post" id="registroForm" novalidate>
                    <div class="mb-3">
                        <label for="nombre" class="form-label">Nombre</label>
                        <input type="text" class="form-control" id="nombre" name="nombre" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,50}" title="Solo letras, mínimo 2 caracteres">
                        <div class="invalid-feedback">Debe contener solo letras y al menos 2 caracteres.</div>
                    </div>

                    <div class="mb-3">
                        <label for="apellidos" class="form-label">Apellidos</label>
                        <input type="text" class="form-control" id="apellidos" name="apellidos" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,50}" title="Solo letras, mínimo 2 caracteres">
                        <div class="invalid-feedback">Debe contener solo letras y al menos 2 caracteres.</div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Correo Electrónico</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                        <div class="invalid-feedback">Introduce un correo válido.</div>
                    </div>

                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" required pattern="[A-Za-z0-9_]{4,15}" title="Debe contener solo letras, números o guiones bajos y tener entre 4 y 15 caracteres">
                        <div class="invalid-feedback">Debe tener entre 4 y 15 caracteres, sin espacios ni caracteres especiales.</div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="password" name="password" required minlength="6" title="Debe tener al menos 6 caracteres">
                        <div class="invalid-feedback">Debe tener al menos 6 caracteres.</div>
                    </div>

                    <div class="mb-3">
                        <label for="confirm_password" class="form-label">Confirmar Contraseña</label>
                        <input type="password" class="form-control" id="confirm_password" name="confirm_password" required>
                        <div class="invalid-feedback">Las contraseñas no coinciden.</div>
                    </div>

                    <button type="submit" class="btn btn-dark w-100">Registrarse</button>
                </form>

                <div class="text-center mt-3">
                    <p>¿Ya tienes una cuenta? <a href="${pageContext.request.contextPath}/vista/login.jsp">Inicia sesión aquí</a></p>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validaciones.js"></script>

    </body>
</html>


