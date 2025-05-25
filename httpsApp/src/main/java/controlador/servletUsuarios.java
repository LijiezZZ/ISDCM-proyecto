/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import modelo.Usuario;
import modelo.dao.UsuarioDAO;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet que gestiona las operaciones relacionadas con los usuarios: login,
 * logout, registro y actualización de contraseña.
 *
 * URL principal: /servletUsuarios
 *
 * Acciones soportadas mediante parámetro "action": - login - logout - register
 * - update
 *
 * Requiere y gestiona sesión de usuario.
 *
 * @author Kenny Alejandro/Lijie Yin
 */
@WebServlet("/servletUsuarios")
public class servletUsuarios extends HttpServlet {

    private static final String LOGIN = "login";

    /**
     * Maneja peticiones POST relacionadas con login, registro y actualización.
     *
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @throws ServletException en caso de error interno
     * @throws IOException en caso de redirección o reenvío fallido
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("error", "Acción no válida.");
            request.getRequestDispatcher("vista/login.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case LOGIN:
                handleLogin(request, response);
                break;
            default:
                response.sendRedirect("vista/login.jsp");
        }
    }

    /**
     * Procesa el inicio de sesión del usuario, gestiona fallos y bloqueo por
     * intentos fallidos.
     *
     * @param request Petición con username y password
     * @param response Redirección a listado de videos o login con error
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario user = usuarioDAO.authenticateUser(username, password);
        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/vista/homepage.jsp");
        }

    }
}
