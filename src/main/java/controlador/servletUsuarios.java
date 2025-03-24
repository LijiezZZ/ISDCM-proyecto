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
 * Servlet que gestiona las operaciones relacionadas con los usuarios:
 * login, logout, registro y actualización de contraseña.
 * 
 * URL principal: /servletUsuarios
 * 
 * Acciones soportadas mediante parámetro "action":
 * - login
 * - logout
 * - register
 * - update
 * 
 * Requiere y gestiona sesión de usuario.
 * 
 * @author Kenny Alejandro/Lijie Yin
 */
@WebServlet("/servletUsuarios")
public class servletUsuarios extends HttpServlet {

    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String REGISTER = "register";
    private static final String UPDATE = "update";

    /**
     * Maneja peticiones GET, principalmente para realizar logout.
     *
     * @param request  Petición HTTP
     * @param response Respuesta HTTP
     * @throws ServletException en caso de error interno
     * @throws IOException      en caso de error de redirección
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (LOGOUT.equals(action)) {
            handleLogout(request, response);
        } else {
            response.sendRedirect("vista/login.jsp");
        }
    }

    /**
     * Maneja peticiones POST relacionadas con login, registro y actualización.
     *
     * @param request  Petición HTTP
     * @param response Respuesta HTTP
     * @throws ServletException en caso de error interno
     * @throws IOException      en caso de redirección o reenvío fallido
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
            case LOGOUT:
                handleLogout(request, response);
                break;
            case REGISTER:
                handleRegister(request, response);
                break;
            case UPDATE:
                handleUpdate(request, response);
                break;
            default:
                response.sendRedirect("vista/login.jsp");
        }
    }

    /**
     * Procesa el inicio de sesión del usuario, gestiona fallos y bloqueo por intentos fallidos.
     *
     * @param request  Petición con username y password
     * @param response Redirección a listado de videos o login con error
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (!usuarioDAO.isUsernameRegistered(username)) {
            redirectToLoginWithError(request, response, "Usuario o contraseña incorrectos.");
            return;
        }

        Usuario user = usuarioDAO.authenticateUser(username, password);
        if (user != null) {
            if (usuarioDAO.isUserBlocked(username)) {
                redirectToLoginWithError(request, response, "Su cuenta está bloqueada, inténtelo más tarde.");
                return;
            }

            usuarioDAO.resetIndFallos(username);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/servletListadoVid");
        } else {
            usuarioDAO.incrementIndFallos(username);
            int fallos = usuarioDAO.getIndFallos(username);

            if (fallos >= 3 && !usuarioDAO.isUserBlocked(username)) {
                usuarioDAO.blockUser(username);
            }

            redirectToLoginWithError(request, response, "Usuario o contraseña incorrectos.");
        }
    }

    /**
     * Procesa el cierre de sesión del usuario actual.
     *
     * @param request  Petición actual
     * @param response Redirige al login
     * @throws IOException si falla la redirección
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("vista/login.jsp");
    }

    /**
     * Procesa el registro de un nuevo usuario, validando que los datos sean correctos y no duplicados.
     *
     * @param request  Petición con datos del formulario de registro
     * @param response Reenvía a login si es exitoso, o muestra errores
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("nombre");
        String lastName = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (password == null || !password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

        Usuario user = new Usuario(firstName, lastName, email, username, password);
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.isEmailRegistered(user.getEmail())) {
            request.setAttribute("error", "El email ya está registrado por otro usuario.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

        if (usuarioDAO.isUsernameRegistered(user.getUsername())) {
            request.setAttribute("error", "El username ya está registrado por otro usuario.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

        if (usuarioDAO.registerUser(user)) {
            response.sendRedirect("vista/login.jsp?success=1");
        } else {
            request.setAttribute("error", "No se pudo completar el registro.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
        }
    }

    /**
     * Procesa la actualización de contraseña para el usuario autenticado.
     *
     * @param request  Contiene username, contraseña antigua y nueva
     * @param response Redirige a login si es exitoso, o muestra errores
     */
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String oldPassword = request.getParameter("old_password");
        String newPassword = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.authenticateUser(username, oldPassword) != null) {
            if (usuarioDAO.updateUser(username, newPassword)) {
                request.getSession().invalidate();
                response.sendRedirect("vista/login.jsp?success=2");
            } else {
                request.setAttribute("error", "No se pudo actualizar la contraseña.");
                request.getRequestDispatcher("vista/perfilUsu.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "La contraseña actual es incorrecta.");
            request.getRequestDispatcher("vista/perfilUsu.jsp").forward(request, response);
        }
    }

    /**
     * Método auxiliar para redirigir al login con un mensaje de error visible.
     *
     * @param request       Petición HTTP
     * @param response      Respuesta HTTP
     * @param errorMessage  Mensaje que se mostrará en el login
     */
    private void redirectToLoginWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("vista/login.jsp").forward(request, response);
    }
}