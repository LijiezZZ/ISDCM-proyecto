/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import modelo.Video;
import modelo.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de realizar operaciones CRUD sobre la entidad Video
 * en la base de datos. Se conecta utilizando la clase {@link ConexionDB}.
 * 
 * Métodos principales:
 * - Registrar video
 * - Comprobar existencia
 * - Obtener todos los videos
 * - Obtener uno por ID
 * - Actualizar
 * - Eliminar
 * - Verificar propiedad del video
 * 
 * Utiliza JDBC para la comunicación con la base de datos.
 * 
 * @author Kenny Alejandro/Lijie Yin
 */
public class VideoDAO {

    /**
     * Registra un nuevo video en la base de datos.
     * 
     * @param video Objeto Video con los datos a registrar
     * @return true si el registro fue exitoso, false si no
     */
    public boolean registerVideo(Video video) {
        boolean isRegistered = false;

        String sql = "INSERT INTO VIDEOS (TITULO, AUTOR, FECHACREACION, CREACIONTIMESTAMP, DURACION, NUMREPRODUCCIONES, DESCRIPCION, FORMATO, LOCALIZACION, USERID, MODIFICACIONTIMESTAMP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, video.getTitulo());
            stmt.setString(2, video.getAutor());
            stmt.setDate(3, video.getFechaCreacion());
            stmt.setTimestamp(4, currentTimestamp);
            stmt.setTime(5, video.getDuracion());
            stmt.setInt(6, video.getNumReproducciones());
            stmt.setString(7, video.getDescripcion());
            stmt.setString(8, video.getFormato());
            stmt.setString(9, video.getLocalizacion());
            stmt.setInt(10, video.getUserId());
            stmt.setTimestamp(11, currentTimestamp);

            int rowsInserted = stmt.executeUpdate();
            isRegistered = rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRegistered;
    }

    /**
     * Verifica si un video con el mismo título ya ha sido registrado por el mismo usuario.
     * 
     * @param title  Título del video
     * @param userId ID del usuario
     * @return true si el video ya existe, false si no
     */
    public boolean isVideoRegistered(String title, Integer userId) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM VIDEOS WHERE TITULO = ? AND USERID = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setInt(2, userId);

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
     * Obtiene la lista completa de videos registrados.
     * 
     * @return Lista de objetos Video
     */
    public List<Video> getAllVideos() {
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

    /**
     * Obtiene un video por su ID.
     * 
     * @param videoId ID del video a buscar
     * @return Objeto Video correspondiente, o vacío si no se encuentra
     */
    public Video getVideo(int videoId) {
        Video video = new Video();
        String sql = "SELECT * FROM VIDEOS WHERE ID = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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

    /**
     * Actualiza los datos principales de un video (título, autor, descripción).
     * 
     * @param id         ID del video a actualizar
     * @param titulo     Nuevo título
     * @param autor      Nuevo autor
     * @param descripcion Nueva descripción
     * @return true si se actualizó correctamente, false si no
     */
    public boolean updateVideo(int id, String titulo, String autor, String descripcion) {
        boolean isUpdated = false;
        String sql = "UPDATE VIDEOS SET TITULO = ?, AUTOR = ?, DESCRIPCION = ?, MODIFICACIONTIMESTAMP = ? WHERE ID = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, descripcion);
            stmt.setTimestamp(4, currentTimestamp);
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();
            isUpdated = rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    /**
     * Elimina un video por su ID.
     * 
     * @param videoId ID del video a eliminar
     * @return true si se eliminó correctamente, false si no
     */
    public boolean deleteVideo(int videoId) {
        boolean isDeleted = false;
        String sql = "DELETE FROM VIDEOS WHERE ID = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);

            int rowsAffected = stmt.executeUpdate();
            isDeleted = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    /**
     * Verifica si un usuario es el propietario de un video específico.
     * 
     * @param videoId ID del video
     * @param userId  ID del usuario
     * @return true si el usuario es propietario del video, false si no
     */
    public boolean isVideoOwner(int videoId, Integer userId) {
        boolean isVideoOwner = false;
        String sql = "SELECT * FROM VIDEOS WHERE ID = ? AND USERID = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                isVideoOwner = rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isVideoOwner;
    }
}
