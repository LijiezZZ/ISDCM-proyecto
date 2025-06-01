package isdcm.isdcm.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static isdcm.util.JwtConfig.EXPIRATION_TIME;
import static isdcm.util.JwtConfig.SECRET_KEY;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import modelo.Usuario;
import modelo.dao.UsuarioDAO;

/**
 * Recurso REST para autenticación de usuarios mediante JWT.
 */
@Path("login")
public class AuthResource {

    /**
     * POST method to login in the application
     * @param username
     * @param password
     * @return JWT si el usuario es válido y no está bloqueado
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(@FormParam("username") String username,
                          @FormParam("password") String password) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Validación inicial: ¿el usuario existe?
        if (!usuarioDAO.isUsernameRegistered(username)) {
            return error("Usuario o contraseña incorrectos.");
        }

        // ¿Está bloqueado?
        if (usuarioDAO.isUserBlocked(username)) {
            return error("Su cuenta está bloqueada, inténtelo más tarde.");
        }

        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

        // ¿Coincide la contraseña?
        Usuario user = usuarioDAO.authenticateUser(username, password);
        if (user != null) {
            usuarioDAO.resetIndFallos(username);

            // Crear JWT
            String jwt = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, key)
                    .compact();

            JsonObject json = Json.createObjectBuilder()
                    .add("JWT", jwt)
                    .build();

            return Response.ok(json).build();
        }

        // Si no coincide la contraseña
        usuarioDAO.incrementIndFallos(username);
        int fallos = usuarioDAO.getIndFallos(username);
        if (fallos >= 3 && !usuarioDAO.isUserBlocked(username)) {
            usuarioDAO.blockUser(username);
        }

        return error("Usuario o contraseña incorrectos.");
    }

    private Response error(String mensaje) {
        JsonObject json = Json.createObjectBuilder()
                .add("error", mensaje)
                .build();
        return Response.status(Response.Status.UNAUTHORIZED).entity(json).build();
    }
}

