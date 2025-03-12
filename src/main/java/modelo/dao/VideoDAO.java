/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import modelo.Video;
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

public class VideoDAO {

    public boolean registerVideo(Video video) {
        boolean isRegistered = false;

        String sql = "INSERT INTO VIDEOS (TITULO, AUTOR, FECHACREACION, CREACIONTIMESTAMP, DURACION, NUMREPRODUCCIONES, DESCRIPCION, FORMATO, LOCALIZACION, USERID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getTitulo());
            stmt.setString(2, video.getAutor());
            stmt.setDate(3, video.getFechaCreacion());
            stmt.setTimestamp(4, video.getCreacionTimestamp());
            stmt.setTime(5, video.getDuracion());
            stmt.setInt(6, video.getNumReproducciones());
            stmt.setString(7, video.getDescripcion());
            stmt.setString(8, video.getFormato());
            stmt.setString(9, video.getLocalizacion());
            stmt.setInt(10, video.getUserId());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                isRegistered = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRegistered;
    }

    public boolean isVideoRegistered(String title, Integer userId) {
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM VIDEOS WHERE titulo = ? AND userId = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setInt(2, userId);

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

}
