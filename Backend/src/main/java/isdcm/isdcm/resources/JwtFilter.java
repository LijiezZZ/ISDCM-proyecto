package isdcm.isdcm.resources;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;

import io.jsonwebtoken.*;
import static isdcm.util.JwtConfig.SECRET_KEY;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Filtro de seguridad que intercepta las peticiones REST y valida el JWT.
 * 
 * @author Kennny Alejandro/ Lijie Yin
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {


    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = uriInfo.getPath(); // Ej: "videos", "login", etc.

        // Permitir acceso libre al endpoint de login
        if (path.equals("login")) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            denyAccess(requestContext, "Falta cabecera Authorization");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

            // Validar token (firma + expiración)
            Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token);

            // Opcional: puedes guardar el usuario en contexto si lo necesitas
            // String user = claims.getBody().getSubject();

        } catch (ExpiredJwtException e) {
            denyAccess(requestContext, "Token expirado");
        } catch (JwtException e) {
            denyAccess(requestContext, "Token inválido");
        }
    }

    private void denyAccess(ContainerRequestContext requestContext, String msg) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"" + msg + "\"}")
                        .build()
        );
    }
}
