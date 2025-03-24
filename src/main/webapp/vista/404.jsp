<%-- 
    Document   : 404
    Created on : Mar 20, 2025, 12:48:42 AM
    Author     : Kenny/Lijie
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Página no encontrada - Error 404</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">

    <div class="container text-center py-5">
        <h1 class="display-1">404</h1>
        <p class="lead">La página que estás buscando no existe.</p>
        <a href="${pageContext.request.contextPath}/servletListadoVid" class="btn btn-light mt-3">Volver al inicio</a>
    </div>

</body>
</html>