package modelo.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Usuario;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UsuarioDAO {

    public Usuario authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario user = null;
        try {
            // Obtener el contexto inicial
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");

            // Buscar el DataSource por el nombre JNDI definido en context.xml y web.xml
            DataSource ds = (DataSource) envContext.lookup("jdbc/pr2");

            // Obtener conexión
            conn = ds.getConnection();

            // Consulta SQL de ejemplo (ajusta a tu tabla y columnas)
            String sql = "SELECT * FROM usuarios WHERE USERNAME = ? AND PASSWORD = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, encryptPassword(password));
            rs = ps.executeQuery();

            // Recorrer resultados
            if (rs.next()) {
                user = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getInt("indBaja"),
                        rs.getTimestamp("creacionTimestamp"),
                        rs.getTimestamp("modificacionTimestamp"),
                        rs.getInt("indFallos")
                );
            }
            return user;
        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
}
