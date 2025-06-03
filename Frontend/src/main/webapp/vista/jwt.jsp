<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="utils.JwtUtils"%>
<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("vista/login.jsp");
        return;
    }
    Usuario user = (Usuario) sessionUser.getAttribute("user");
    
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
    <title>Encriptar / Desencriptar JWT con JOSE4J</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Encriptar / Desencriptar JWT con JOSE4J</a>
        <div class="d-flex align-items-center">
            <span class="text-light me-3">Usuario: <strong><a href="${pageContext.request.contextPath}/vista/perfilUsu.jsp" style="text-decoration: none; color: inherit;"><%= user.getUsername() %></a></strong></span>
            <a href="<%= request.getContextPath() %>/servletUsuarios?action=logout" class="btn btn-danger">Cerrar sesión</a>
        </div>
    </div>
</nav>

<div class="container py-5">
    <h2 class="text-center mb-4">Encriptar / Desencriptar JWT con JOSE4J</h2>

    <%-- JWT original con colores --%>
    <%
        String[] tokenParts = token.split("\\.");
        String h = tokenParts.length > 0 ? tokenParts[0] : "";
        String p = tokenParts.length > 1 ? tokenParts[1] : "";
        String s = tokenParts.length > 2 ? tokenParts[2] : "";
    %>
    <div class="alert alert-primary">
        <strong>Token JWT asignado:</strong>
        <div class="bg-white border rounded p-3 mt-2" style="font-family: monospace; word-wrap: break-word;">
            <span style="color: #007bff;"><%= h %></span>.
            <span style="color: #28a745;"><%= p %></span>.
            <span style="color: #dc3545;"><%= s %></span>
        </div>

        <!-- Oculto para copiar -->
        <textarea id="jwtOriginal" class="d-none"><%= token %></textarea>
        <button class="btn btn-outline-primary mt-2" type="button" onclick="copiarAlPortapapeles('jwtOriginal')">
            <i class="fas fa-copy"></i> Copiar
        </button>

        <p class="mt-2 mb-0">
            Este token es generado tras el login y se utiliza para autenticar tus peticiones a la API REST.
            Puedes analizarlo en <a href="https://jwt.io" target="_blank">jwt.io</a>.
        </p>
    </div>

    <!-- Botón Encriptar -->
    <form method="post" action="${pageContext.request.contextPath}/servletJWT?action=encrypt">
        <input type="hidden" name="jwt" value="<%= token %>">
        <button type="submit" class="btn btn-success mb-3">Encriptar JWT (JWE)</button>
    </form>

    <!-- Formulario para desencriptar -->
    <form method="post" action="${pageContext.request.contextPath}/servletJWT?action=decrypt">
        <div class="mb-3">
            <label for="jweToken" class="form-label">Introduce el JWT encriptado (JWE):</label>
            <textarea class="form-control" name="jwe" id="jweToken" rows="4" required></textarea>
        </div>
        <div class="d-flex gap-2 mt-3">
            <button type="submit" class="btn btn-warning">Desencriptar JWE</button>
            <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-secondary">Volver a Listado</a>
        </div>
    </form>

    <!-- Resultado -->
    <% if (request.getAttribute("result") != null) {
    String jwtResult = (String) request.getAttribute("result");
    String actionType = (String) request.getParameter("action");
    String[] parts = jwtResult.split("\\.");
    String rH = parts.length > 0 ? parts[0] : "";
    String rP = parts.length > 1 ? parts[1] : "";
    String rS = parts.length > 2 ? parts[2] : "";
%>
    <div class="alert alert-secondary mt-4">
        <h5>
            <% if ("encrypt".equals(actionType)) { %>
                JWT encriptado (JWE):
            <% } else { %>
                JWT desencriptado (JWS):
            <% } %>
        </h5>

        <div class="bg-white border rounded p-3 mt-2" style="font-family: monospace; word-wrap: break-word;">
            <%= jwtResult %>
        </div>

        <textarea id="jwtResultado" class="d-none"><%= jwtResult %></textarea>
        <button class="btn btn-outline-dark mt-2" type="button" onclick="copiarAlPortapapeles('jwtResultado')">
            <i class="fas fa-copy"></i> Copiar
        </button>

        <%
            boolean match = "decrypt".equals(actionType) && token != null && token.equals(jwtResult);
        %>
        <% if ("decrypt".equals(actionType)) {
            if (match) { %>
                <div class="alert alert-success mt-3">
                    El JWT desencriptado coincide con el token original de sesión.
                </div>
            <% } else { %>
                <div class="alert alert-danger mt-3">
                    El JWT desencriptado no coincide con el token original.
                </div>
        <% }} %>
    </div>
<% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function copiarAlPortapapeles(idElemento) {
        const texto = document.getElementById(idElemento).value;

        if (navigator.clipboard && window.isSecureContext) {
            // Navegador moderno y contexto seguro (HTTPS o localhost)
            navigator.clipboard.writeText(texto).then(() => {
                alert(" Copiado al portapapeles");
            }).catch(err => {
                alert(" Error al copiar: " + err);
            });
        } else {
            // Fallback para navegadores antiguos
            const textarea = document.createElement("textarea");
            textarea.value = texto;
            document.body.appendChild(textarea);
            textarea.select();
            try {
                document.execCommand('copy');
                alert(" Copiado al portapapeles");
            } catch (err) {
                alert(" Error al copiar");
            }
            document.body.removeChild(textarea);
        }
    }
</script>

</body>
</html>
