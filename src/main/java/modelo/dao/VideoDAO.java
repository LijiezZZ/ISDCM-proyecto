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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VideoDAO {

    public boolean registerVideo(Video video) {
        boolean isRegistered = false;

        String sql = "INSERT INTO VIDEOS (TITULO, AUTOR, FECHACREACION, CREACIONTIMESTAMP, DURACION, NUMREPRODUCCIONES, DESCRIPCION, FORMATO, LOCALIZACION, USERID, MODIFICACIONTIMESTAMP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getTitulo());
            stmt.setString(2, video.getAutor());
            stmt.setDate(3, video.getFechaCreacion());
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(4, currentTimestamp);
            stmt.setTime(5, video.getDuracion());
            stmt.setInt(6, video.getNumReproducciones());
            stmt.setString(7, video.getDescripcion());
            stmt.setString(8, video.getFormato());
            stmt.setString(9, video.getLocalizacion());
            stmt.setInt(10, video.getUserId());
            stmt.setTimestamp(11, currentTimestamp);

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

        String sql = "SELECT COUNT(*) FROM VIDEOS WHERE TITULO = ? AND USERID = ?";

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
    

    public List<Video> getAllVideos() {
        System.out.println("Obtener Videos ");  // Depuración

        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM VIDEOS";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Video video = new Video(
                        rs.getInt("ID"),
                        rs.getString("TITULO"),
                        rs.getString("AUTOR"),
                        rs.getDate("FECHACREACION"),
                        rs.getTime("DURACION"),
                        rs.getInt("NUMREPRODUCCIONES"),
                        rs.getString("DESCRIPCION"),
                        rs.getString("FORMATO"),
                        rs.getString("LOCALIZACION"),
                        rs.getTimestamp("CREACIONTIMESTAMP"),
                        rs.getTimestamp("MODIFICACIONTIMESTAMP"),
                        rs.getInt("USERID")
                );
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return videos;
    }
    
    public Video getVideo(int videoId) {
        System.out.println("Obtener Video ");

        Video video = new Video(); 
        String sql = "SELECT * FROM VIDEOS WHERE ID = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                video = new Video(
                        rs.getInt("ID"),
                        rs.getString("TITULO"),
                        rs.getString("AUTOR"),
                        rs.getDate("FECHACREACION"),
                        rs.getTime("DURACION"),
                        rs.getInt("NUMREPRODUCCIONES"),
                        rs.getString("DESCRIPCION"),
                        rs.getString("FORMATO"),
                        rs.getString("LOCALIZACION"),
                        rs.getTimestamp("CREACIONTIMESTAMP"),
                        rs.getTimestamp("MODIFICACIONTIMESTAMP"),
                        rs.getInt("USERID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return video;
    }
    
    public boolean updateVideo(int id, String titulo, String autor, String descripcion) {
        boolean isUpdated = false;

        String sql = "UPDATE VIDEOS SET TITULO = ?, AUTOR = ?, DESCRIPCION = ?, MODIFICACIONTIMESTAMP = ? WHERE ID = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, descripcion);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(4, currentTimestamp);
            
            stmt.setInt(5, id); // Suponiendo que el ID es un int y tienes un getId()

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                isUpdated = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    public boolean deleteVideo(int videoId) {
        boolean isDeleted = false;
        String sql = "DELETE FROM VIDEOS WHERE ID = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                isDeleted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    } 
}
