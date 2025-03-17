/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import modelo.Usuario;
import modelo.dao.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alumne
 */
@WebServlet("/servletUsuarios")
public class servletUsuarios extends HttpServlet {

    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String REGISTER = "register";
    private static final String UPDATE = "update";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (LOGOUT.equals(action)) {
            handleLogout(request, response); // Llamar al handleLogout
        } else {
            response.sendRedirect("vista/login.jsp");
        }
    }

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

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario de login
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Verificar si el usuario está registrado
        if (!usuarioDAO.isUsernameRegistered(username)) {
            redirectToLoginWithError(request, response, "Usuario o contraseña incorrectos.");
            return;
        }

        // Autenticación de usuario
        Usuario user = usuarioDAO.authenticateUser(username, password);
        if (user != null) {
            // Comprobar si el usuario está bloqueado
            if (usuarioDAO.isUserBlocked(username)) {
                redirectToLoginWithError(request, response, "Su cuenta está bloqueada, inténtalo de nuevo más tarde.");
                return;
            }

            // Resetear indFallos a 0 al iniciar sesión
            usuarioDAO.resetIndFallos(username);

            // Crear sesión para el usuario autenticado
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirigir a la página principal
            response.sendRedirect(request.getContextPath() + "/servletListadoVid");
        } else {
            // Incrementar fallos e intentar bloquear al usuario si es necesario
            usuarioDAO.incrementIndFallos(username);

            // Obtener los fallos actuales
            int fallos = usuarioDAO.getIndFallos(username);

            if (fallos >= 3) {
                if (!usuarioDAO.isUserBlocked(username)) {
                    usuarioDAO.blockUser(username);
                }
                redirectToLoginWithError(request, response, "El número de intentos ha superado el límite de 3. Por favor, inténtalo nuevamente más tarde.");
            } else {
                redirectToLoginWithError(request, response, "Usuario o contraseña incorrectos.");
            }
        }
    }

    // Método auxiliar para redirigir con un mensaje de error
    private void redirectToLoginWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("vista/login.jsp").forward(request, response);
        return;
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("vista/login.jsp");
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario de registro
        String firstName = request.getParameter("nombre");
        String lastName = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        // COmprobar si las contraseñas coinciden
        if (password == null || !password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

        // Crear el objeto Usuario con los datos del formulario
        Usuario user = new Usuario(firstName, lastName, email, username, password);
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Comprobar si el email ya está registrado
        if (usuarioDAO.isEmailRegistered(user.getEmail())) {
            request.setAttribute("error", "El email ya está registrado por otro usuario.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

        // Comprobar si el username ya está registrado
        if (usuarioDAO.isUsernameRegistered(user.getUsername())) {
            request.setAttribute("error", "El username ya está registrado por otro usuario.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

        //INtentar registrar el usuario en la base de datos
        if (usuarioDAO.registerUser(user)) {
            response.sendRedirect("vista/login.jsp?success=1");
        } else {
            request.setAttribute("error", "No se pudo completar el registro.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
            return;
        }

    }

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
                return;
            }
        } else {
            request.setAttribute("error", "La contraseña actual es incorrecta. Inténtalo de nuevo.");
            request.getRequestDispatcher("vista/perfilUsu.jsp").forward(request, response);
            return;
        }

    }
}
