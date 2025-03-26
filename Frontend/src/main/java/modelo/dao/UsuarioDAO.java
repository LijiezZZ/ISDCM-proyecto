package modelo.dao;

import modelo.Usuario;
import modelo.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase DAO que gestiona el acceso a la base de datos para la entidad {@link Usuario}.
 * 
 * Funcionalidades principales:
 * - Autenticación de usuarios
 * - Registro de nuevos usuarios
 * - Verificación de existencia de email o username
 * - Actualización de contraseñas
 * - Control de intentos fallidos e inicios de sesión
 * - Gestión de bloqueo temporal por múltiples fallos
 * 
 * Utiliza conexiones JDBC a través de la clase {@link ConexionDB}.
 * 
 * Todas las contraseñas se almacenan en la base de datos con hashing SHA-256.
 * 
 * @author Kenny Alejandro/Lijie Yin
 */
public class UsuarioDAO {

    /**
     * Autentica un usuario comparando su username y contraseña encriptada.
     *
     * @param username Nombre de usuario
     * @param password Contraseña sin encriptar
     * @return Objeto Usuario si la autenticación es correcta, null si no
     */
    public Usuario authenticateUser(String username, String password) {
        String sql = "SELECT * FROM USUARIOS WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, encryptPassword(password));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encripta una contraseña usando el algoritmo SHA-256.
     *
     * @param password Contraseña original
     * @return Contraseña encriptada en formato hexadecimal
     */
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

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Objeto Usuario con los datos del nuevo registro
     * @return true si el registro fue exitoso, false si no
     */
    public boolean registerUser(Usuario user) {
        boolean isRegistered = false;

        String sql = "INSERT INTO USUARIOS (NOMBRE, APELLIDOS, EMAIL, USERNAME, PASSWORD, CREACIONTIMESTAMP, MODIFICACIONTIMESTAMP) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getApellidos());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUsername());
            stmt.setString(5, encryptPassword(user.getPassword()));
            stmt.setTimestamp(6, now);
            stmt.setTimestamp(7, now);

            isRegistered = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRegistered;
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param username Nombre de usuario
     * @param password Nueva contraseña sin encriptar
     * @return true si se actualizó correctamente, false si no
     */
    public boolean updateUser(String username, String password) {
        boolean updated = false;
        String sql = "UPDATE USUARIOS SET PASSWORD = ? WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, encryptPassword(password));
            stmt.setString(2, username);

            updated = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    /**
     * Verifica si un email ya está registrado.
     *
     * @param email Email a comprobar
     * @return true si el email ya existe, false si no
     */
    public boolean isEmailRegistered(String email) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE EMAIL = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Verifica si un username ya está registrado.
     *
     * @param username Nombre de usuario
     * @return true si el username ya existe, false si no
     */
    public boolean isUsernameRegistered(String username) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Verifica si un usuario está actualmente bloqueado.
     *
     * @param username Nombre de usuario
     * @return true si el usuario está bloqueado, false si no
     */
    public boolean isUserBlocked(String username) {
        boolean blocked = false;
        String sql = "SELECT BLOQUEOFINTIMESTAMP FROM USUARIOS WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp bloqueo = rs.getTimestamp("BLOQUEOFINTIMESTAMP");
                    blocked = bloqueo != null && bloqueo.after(new Timestamp(System.currentTimeMillis()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blocked;
    }

    /**
     * Bloquea temporalmente a un usuario por 5 minutos.
     *
     * @param username Nombre de usuario a bloquear
     */
    public void blockUser(String username) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Timestamp bloqueadoHasta = new Timestamp(calendar.getTimeInMillis());

        String sql = "UPDATE USUARIOS SET BLOQUEOFINTIMESTAMP = ? WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, bloqueadoHasta);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Incrementa en 1 el número de intentos fallidos de inicio de sesión del usuario.
     *
     * @param username Nombre de usuario
     */
    public void incrementIndFallos(String username) {
        String sql = "UPDATE USUARIOS SET INDFALLOS = INDFALLOS + 1 WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reinicia el contador de intentos fallidos del usuario a 0.
     *
     * @param username Nombre de usuario
     */
    public void resetIndFallos(String username) {
        String sql = "UPDATE USUARIOS SET INDFALLOS = 0 WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el número de intentos fallidos actuales de un usuario.
     *
     * @param username Nombre de usuario
     * @return Número de intentos fallidos (INDFALLOS)
     */
    public Integer getIndFallos(String username) {
        int indFallos = 0;
        String sql = "SELECT INDFALLOS FROM USUARIOS WHERE USERNAME = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    indFallos = rs.getInt("INDFALLOS");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indFallos;
    }
}
