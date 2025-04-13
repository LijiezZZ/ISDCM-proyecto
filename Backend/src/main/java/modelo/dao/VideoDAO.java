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
import java.time.LocalDate;
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
    * Obtiene una lista de vídeos ordenados por fecha de creación ascendente.
    * Limita los resultados a un máximo de 1000 elementos.
    *
    * @param limite Número máximo de vídeos a retornar. Si es <= 0 se usa 100 por defecto.
    * @return Lista de objetos Video
    */
   public List<Video> getAllVideos(int limite) {
       List<Video> videos = new ArrayList<>();

       // Validación del límite
       if (limite <= 0) {
           limite = 100; // valor por defecto
       } else if (limite > 1000) {
           throw new IllegalArgumentException("El límite máximo permitido es 1000.");
       }

       // Orden ascendente por fecha de creación
       String sql = "SELECT * FROM VIDEOS ORDER BY CREACIONTIMESTAMP ASC FETCH FIRST ? ROWS ONLY";

       try (Connection conn = ConexionDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

           stmt.setInt(1, limite);

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
     * Busca vídeos aplicando filtros dinámicos (título, autor, fecha) y ordenamiento,
     * compatible con bases de datos Java DB / Derby.
     *
     * @param titulo     Filtro por coincidencia parcial del título (opcional)
     * @param autor      Filtro por autor exacto (opcional)
     * @param fecha      Fecha exacta o parcial (yyyy, yyyy-MM o yyyy-MM-dd)
     * @param limite     Número máximo de resultados (1-1000). Default: 100
     * @param ordenPor   Campo de orden: "fecha" o "vistas". Default: "fecha"
     * @param direccion  Dirección de orden: "asc" o "desc". Default: "asc"
     * @return Lista de vídeos que cumplen los filtros
     */
    public List<Video> buscarVideos(String titulo, String autor, String fecha, int limite, String ordenPor, String direccion) {
        List<Video> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM VIDEOS");
        List<String> condiciones = new ArrayList<>();

        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;

        // Filtros
        if (titulo != null) condiciones.add("LOWER(TITULO) LIKE ?");
        if (autor != null) condiciones.add("LOWER(AUTOR) = ?");
        if (fecha != null) {
            try {
                int length = fecha.length();
                if (length == 4) {
                    fechaInicio = LocalDate.parse(fecha + "-01-01");
                    fechaFin = fechaInicio.plusYears(1);
                } else if (length == 7) {
                    fechaInicio = LocalDate.parse(fecha + "-01");
                    fechaFin = fechaInicio.plusMonths(1);
                } else if (length == 10) {
                    fechaInicio = LocalDate.parse(fecha);
                    fechaFin = fechaInicio.plusDays(1);
                }
                if (fechaInicio != null && fechaFin != null) {
                    condiciones.add("FECHACREACION >= ? AND FECHACREACION < ?");
                }
            } catch (Exception e) {
                e.printStackTrace(); // O manejar error de fecha inválida
            }
        }

        // Añadir cláusula WHERE si hay condiciones
        if (!condiciones.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", condiciones));
        }

        // Ordenamiento
        String campoOrden = "CREACIONTIMESTAMP";
        if ("vistas".equalsIgnoreCase(ordenPor)) {
            campoOrden = "NUMREPRODUCCIONES";
        }

        String dir = "ASC";
        if ("desc".equalsIgnoreCase(direccion)) {
            dir = "DESC";
        }

        sql.append(" ORDER BY ").append(campoOrden).append(" ").append(dir);

        // Límite validado
        if (limite <= 0) {
            limite = 100;
        } else if (limite > 1000) {
            throw new IllegalArgumentException("El límite máximo permitido es 1000.");
        }

        sql.append(" FETCH FIRST ? ROWS ONLY");

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (titulo != null) stmt.setString(i++, "%" + titulo.toLowerCase() + "%");
            if (autor != null) stmt.setString(i++, autor.toLowerCase());
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(i++, Date.valueOf(fechaInicio));
                stmt.setDate(i++, Date.valueOf(fechaFin));
            }
            stmt.setInt(i, limite);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultados.add(new Video(
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
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
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
    /**
    * Actualiza el número de reproducciones de un vídeo específico y su timestamp de modificación.
    *
    * Este método se utiliza normalmente cuando un usuario visualiza un vídeo,
    * incrementando su contador de reproducciones. Además, actualiza el campo
    * MODIFICACIONTIMESTAMP al momento actual para reflejar la actividad.
    *
    * @param id ID del vídeo que se desea actualizar
    * @param nuevasReproducciones Nuevo valor del contador de reproducciones
    * @return true si la actualización fue exitosa (al menos una fila afectada), false en caso contrario o error
    */

    public boolean updateReproducciones(int id, int nuevasReproducciones) {
        String sql = "UPDATE VIDEOS SET NUMREPRODUCCIONES = ?, MODIFICACIONTIMESTAMP = ? WHERE ID = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevasReproducciones);
            stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
