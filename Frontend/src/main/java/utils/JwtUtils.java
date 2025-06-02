package utils;

import java.util.Base64;
import org.json.JSONObject;

/**
 *
 * @author kennyalejandro
 */
public class JwtUtils {

    /**
     * Extrae la fecha de expiración (exp) de un JWT.
     *
     * @param jwt El token JWT en formato compacto (HEADER.PAYLOAD.SIGNATURE)
     * @return Timestamp de expiración en segundos desde epoch, o null si no se puede leer
     */
    public static Long getExpiration(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return null;

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JSONObject json = new JSONObject(payload);

            return json.has("exp") ? json.getLong("exp") : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Determina si el token ha expirado.
     *
     * @param jwt Token JWT
     * @return true si está expirado, false si es válido o no tiene exp
     */
    public static boolean isTokenExpired(String jwt) {
        Long exp = getExpiration(jwt);
        if (exp == null) return true;

        long now = System.currentTimeMillis() / 1000; // segundos
        return exp < now;
    }

    /**
     * Decodifica el payload del JWT y lo devuelve como JSONObject.
     *
     * @param jwt Token JWT
     * @return Objeto JSON con los campos del payload
     */
    public static JSONObject decodePayload(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return null;

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return new JSONObject(payload);
        } catch (Exception e) {
            return null;
        }
    }
}
