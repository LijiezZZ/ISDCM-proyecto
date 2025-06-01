package servicio;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 ** /**
 * Servicio cliente REST para obtener un JWT desde el backend autenticando al usuario.
 * 
 * Este servicio NO valida las credenciales, solo obtiene el token si son válidas.
 * Se usa después de que la autenticación local (UsuarioDAO) haya sido exitosa.
 * 
 * @author kenny alejandro
 */
public class ServicioAuthREST {

    private static final String LOGIN_URL = "http://localhost:8180/Backend/resources/login";

    /**
     * Realiza una petición POST al endpoint /login con las credenciales.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return El JWT recibido desde la API REST, o null si hubo error
     */
    public String obtenerTokenDesdeAPI(String username, String password) {
        try {
            String data = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                        + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream is = conn.getInputStream();
                     JsonReader jsonReader = Json.createReader(is)) {
                    JsonObject json = jsonReader.readObject();
                    return json.getString("JWT", null);
                }
            } else {
                System.err.println("Error al obtener JWT: " + conn.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}