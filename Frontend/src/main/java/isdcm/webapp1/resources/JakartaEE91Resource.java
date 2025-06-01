package isdcm.webapp1.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Recurso REST básico para probar la disponibilidad del servicio Jakarta EE.
 *
 * Este recurso expone un endpoint HTTP GET en la ruta `/jakartaee9`,
 * el cual responde con un mensaje simple de "ping Jakarta EE".
 * 
 * Sirve como punto de prueba para verificar que el backend Java EE está
 * desplegado y ejecutándose correctamente.
 *
 * Ejemplo de uso:
 * - Método: GET
 * - URL: http://localhost:8080/webApp1/webresources/jakartaee9
 * - Respuesta: "ping Jakarta EE"
 *
 * Este endpoint no requiere autenticación ni parámetros.
 *
 * @author Kenny Alejandro / Lijie Yin
 */
@Path("jakartaee9")
public class JakartaEE91Resource {

    /**
     * Endpoint de prueba que devuelve una respuesta HTTP 200 con el texto "ping Jakarta EE".
     *
     * @return Objeto Response con código 200 (OK) y mensaje de confirmación.
     */
    @GET
    public Response ping() {
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
}
