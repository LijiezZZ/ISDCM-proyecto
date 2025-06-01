package servicio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import modelo.Video;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Servicio cliente REST que gestiona la comunicación entre el frontend y la API REST de vídeos.
 * Encapsula todas las operaciones relacionadas con vídeos, haciendo uso de peticiones HTTP.
 * 
 * URL base esperada del backend: http://localhost:8180/Backend/resources/videos
 * 
 * Requiere la dependencia Jackson para la conversión JSON ↔ Java.
 * 
 * @author Kenny Alejandro
 */
public class ServicioVideoREST {

    private static final String API_BASE_URL = "http://localhost:8180/Backend/resources/videos";
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Obtiene todos los vídeos registrados en el sistema desde la API REST.
     * 
     * @return Lista de objetos Video
     * @throws IOException si hay problemas de conexión o lectura del backend
     */
    public List<Video> obtenerTodos() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return mapper.readValue(reader, new TypeReference<List<Video>>() {});
        }
    }

    /**
     * Realiza una búsqueda de vídeos por título, autor y/o fecha.
     * 
     * Ejemplo de uso:
     * buscarVideos("java", "kenny", "2025-04") → buscará vídeos de Kenny con "java" en el título creados en abril 2025.
     * 
     * @param titulo Título parcial o completo (puede ser null)
     * @param autor  Autor exacto (puede ser null)
     * @param fecha  Fecha en formato yyyy, yyyy-MM o yyyy-MM-dd (puede ser null)
     * @return Lista de vídeos que cumplen los filtros
     * @throws IOException si hay error al conectar con la API
     */
    public List<Video> buscarVideos(String titulo, String autor, String fecha) throws IOException {
        StringBuilder query = new StringBuilder("?");
        if (titulo != null) query.append("titulo=").append(titulo).append("&");
        if (autor != null) query.append("autor=").append(autor).append("&");
        if (fecha != null) query.append("fecha=").append(fecha).append("&");

        String urlFinal = API_BASE_URL + "/buscar" + query.toString();

        HttpURLConnection conn = (HttpURLConnection) new URL(urlFinal).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return mapper.readValue(reader, new TypeReference<List<Video>>() {});
        }
    }

    /**
     * Obtiene los datos de un vídeo a partir de su ID.
     * 
     * @param id Identificador del vídeo
     * @return Objeto Video con la información del recurso
     * @throws IOException si no se puede conectar o parsear la respuesta
     */
    public Video getVideoPorId(int id) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return mapper.readValue(reader, Video.class);
        }
    }

    /**
     * Registra un nuevo vídeo enviando su información a la API REST.
     * Requiere al menos: título, autor, fechaCreacion, duracion, formato, localizacion y userId.
     * 
     * @param video Objeto Video con los datos a registrar
     * @return true si se creó correctamente, false si no
     * @throws IOException si hay error de conexión o respuesta
     */
    public boolean registrarVideo(Video video) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            mapper.writeValue(os, video);
        }

        return conn.getResponseCode() == HttpURLConnection.HTTP_CREATED;
    }

    /**
     * Actualiza los datos de un vídeo existente.
     * 
     * @param id              ID del vídeo a modificar
     * @param videoActualizado Objeto Video con los nuevos datos
     * @return true si la actualización fue exitosa
     * @throws IOException si la petición falla
     */
    public boolean actualizarVideo(int id, Video videoActualizado) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            mapper.writeValue(os, videoActualizado);
        }

        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    /**
     * Elimina un vídeo a partir de su ID.
     * 
     * @param id ID del vídeo
     * @return true si fue eliminado correctamente
     * @throws IOException si ocurre error al llamar a la API
     */
    public boolean eliminarVideo(int id) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("DELETE");
        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    /**
     * Aumenta el número de reproducciones de un vídeo.
     * 
     * @param id ID del vídeo a visualizar
     * @return Objeto Video actualizado tras la visualización
     * @throws IOException si ocurre un error de conexión
     */
    public Video visualizarVideo(int id) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL + "/visualizar/" + id).openConnection();
        conn.setRequestMethod("POST");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return mapper.readValue(reader, Video.class);
        }
    }

    /**
     * Verifica si un usuario es el propietario de un vídeo.
     * 
     * @param videoId ID del vídeo
     * @param userId  ID del usuario
     * @return true si el usuario es propietario, false si no (devuelto como texto "true"/"false")
     * @throws IOException si hay error al verificar
     */
    public boolean esPropietario(int videoId, int userId) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL + "/" + videoId + "/propietario?userId=" + userId).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return Boolean.parseBoolean(reader.readLine());
        }
    }

    /**
     * Comprueba si un vídeo con un título ya ha sido registrado por el usuario.
     * 
     * @param titulo Título del vídeo
     * @param userId ID del usuario
     * @return true si ya existe, false si no
     * @throws IOException si ocurre un error durante la consulta
     */
    public boolean existeVideo(String titulo, int userId) throws IOException {
        titulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
        HttpURLConnection conn = (HttpURLConnection) new URL(API_BASE_URL + "/existe?titulo=" + titulo + "&userId=" + userId).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return Boolean.parseBoolean(reader.readLine());
        }
    }
}