package servicio;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utilidad para crear instancias de servicios REST con configuración común,
 * como el token JWT almacenado en sesión.
 * @author kennyalejandro
 */
public class ServicioHelper {

    /**
     * Devuelve una instancia de ServicioVideoREST con el JWT seteado desde sesión.
     * 
     * @param request HttpServletRequest desde el servlet
     * @return ServicioVideoREST con el token JWT (si está presente)
     */
    public static ServicioVideoREST getServicioVideo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String jwt = (session != null) ? (String) session.getAttribute("jwt") : null;

        ServicioVideoREST servicio = new ServicioVideoREST();
        servicio.setJwtToken(jwt);
        return servicio;
    }
}
