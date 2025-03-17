/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import modelo.Usuario;
import modelo.ConexionDB;
/**
 *
 * @author alumne
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class UsuarioDAO {

    public Usuario authenticateUser(String username, String password) {

        String sql = "SELECT * FROM USUARIOS WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, encryptPassword(password));  // Encriptar antes de comparar

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario user = new Usuario(
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
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    public boolean registerUser(Usuario user) {
        boolean isRegistered = false;

        String sql = "INSERT INTO USUARIOS (NOMBRE, APELLIDOS, EMAIL, USERNAME, PASSWORD, CREACIONTIMESTAMP, MODIFICACIONTIMESTAMP) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getApellidos());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUsername());
            stmt.setString(5, encryptPassword(user.getPassword()));
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(6, currentTimestamp);
            stmt.setTimestamp(7, currentTimestamp);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                isRegistered = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRegistered;
    }
    
    public boolean updateUser(String username, String password) {
        boolean updated = false;
        String sql = "UPDATE USUARIOS SET PASSWORD = ? WHERE USERNAME = ?";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, encryptPassword(password));
            stmt.setString(2, username);

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                updated = true;
                System.out.println("Contraseña actualizada correctamente.");
            } else {
                System.out.println("Usuario no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public boolean isEmailRegistered(String email) {
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE EMAIL = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                    return exists;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean isUsernameRegistered(String username) {
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE USERNAME = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                    return exists;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    public boolean isUserBlocked(String username) {
        boolean blocked = false;
        
        String sql = "SELECT BLOQUEOFINTIMESTAMP FROM USUARIOS WHERE USERNAME = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp bloqueoTimestamp = rs.getTimestamp("BLOQUEOFINTIMESTAMP");
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    if (bloqueoTimestamp.after(currentTimestamp)) {
                        blocked = true;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blocked;
    }
    
    public void blockUser(String username) {
        
        // Obtener la fecha actual
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        
        // Sumar 5 minutos
        calendar.add(Calendar.MINUTE, 5);
        
        // Obtener la nueva fecha con 5 minutos sumados
        Date futureDate = calendar.getTime();
        
        // Convertir la fecha futura a Timestamp
        Timestamp futureTimestamp = new Timestamp(futureDate.getTime());
        
        String sql = "UPDATE USUARIOS SET BLOQUEOFINTIMESTAMP = ? WHERE USERNAME = ?";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, futureTimestamp);
            stmt.setString(2, username);

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("Usuario bloqueado.");
            } else {
                System.out.println("Usuario no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void incrementIndFallos(String username) {
        String sql = "UPDATE USUARIOS SET INDFALLOS = INDFALLOS + 1 WHERE USERNAME = ?";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("indFallos incrementado correctamente.");
            } else {
                System.out.println("Usuario no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetIndFallos(String username) {
        String sql = "UPDATE USUARIOS SET INDFALLOS = 0 WHERE USERNAME = ?";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("indFallos reseteado correctamente.");
            } else {
                System.out.println("Usuario no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Integer getIndFallos(String username) {
        Integer indFallos = 0;
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


