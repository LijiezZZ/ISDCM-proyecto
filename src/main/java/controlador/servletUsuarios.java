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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("vista/login.jsp");
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
            default:
                response.sendRedirect("vista/login.jsp");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario user = usuarioDAO.authenticateUser(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("vista/listadoVid.jsp");
        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos.");
            request.getRequestDispatcher("vista/login.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("vista/login.jsp");
    }

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
        }

        Usuario user = new Usuario(firstName, lastName, email, username, password, 0);
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.isEmailRegistered(user.getEmail())) {
            request.setAttribute("error", "El email ya está registrado por otro usuario.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
        }

        if (usuarioDAO.isUsernameRegistered(user.getUsername())) {
            request.setAttribute("error", "El username ya está registrado por otro usuario.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
        }

        if (usuarioDAO.registerUser(user)) {
            response.sendRedirect("vista/login.jsp?success=1");
        } else {
            request.setAttribute("error", "No se pudo completar el registro.");
            request.getRequestDispatcher("vista/registroUsu.jsp").forward(request, response);
        }

    }
}
