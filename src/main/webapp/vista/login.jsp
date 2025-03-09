<%-- 
    Document   : login
    Created on : 2 mar 2025, 18:30:41
    Author     : alumne
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Iniciar la sesión</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

    </head>
    <body>

        <div class="container">
            <div class="login-container">
                <h3 class="text-center">Iniciar Sesión</h3>
                <div class="error-container" style="min-height: 50px;">
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                    <div class="alert alert-danger text-center"><%= error %></div>
                    <% } %>
                </div>
                <form action="<%= request.getContextPath() %>/servletUsuarios?action=login" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Entrar</button>
                </form>
                <div class="text-center mt-3">
                    <p>¿No tienes una cuenta? <a href="${pageContext.request.contextPath}/vista/registroUsu.jsp">Regístrate aquí</a></p>
                </div>
                <div class="success-container" style="min-height: 50px;">
                    <% String success = request.getParameter("success"); %>
                    <% if ("1".equals(success)) { %>
                    <div class="alert alert-success text-center">¡Te has registrado correctamente! Ahora puedes iniciar sesión.</div>
                    <% } %>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



