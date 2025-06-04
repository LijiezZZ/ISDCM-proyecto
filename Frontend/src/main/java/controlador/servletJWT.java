package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.keys.AesKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;

/**
 * Servlet encargado de encriptar y desencriptar JWT usando JOSE4J y clave desde archivo.
 *
 * La clave se carga desde el archivo 'clave.key' ubicado en la carpeta raíz del proyecto.
 *
 * URL de acceso: /servletJWT
 * 
 * @author kennyalejandro
 */
@WebServlet("/servletJWT")
public class servletJWT extends HttpServlet {

    private static final String AES_ALGORITHM = "AES";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("encrypt".equalsIgnoreCase(action)) {
            handleEncryption(request, response);
        } else if ("decrypt".equalsIgnoreCase(action)) {
            handleDecryption(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    private void handleEncryption(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jwt = request.getParameter("jwt");

        if (jwt == null || jwt.trim().isEmpty()) {
            request.setAttribute("result", "JWT no proporcionado.");
            request.getRequestDispatcher("vista/jwt.jsp").forward(request, response);
            return;
        }

        try {
            JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setPayload(jwt);
            jwe.setAlgorithmHeaderValue("A256KW");
            jwe.setEncryptionMethodHeaderParameter("A256CBC-HS512");
            jwe.setKey(loadKeyFromFile(request));
            jwe.setContentTypeHeaderValue("JWT");

            String encryptedToken = jwe.getCompactSerialization();
            request.setAttribute("result", encryptedToken);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "Error al encriptar el JWT: " + e.getMessage());
        }

        request.getRequestDispatcher("vista/jwt.jsp").forward(request, response);
    }

    private void handleDecryption(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jweToken = request.getParameter("jwe");

        if (jweToken == null || jweToken.trim().isEmpty()) {
            request.setAttribute("result", "JWE no proporcionado.");
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
            return;
        }

        try {
            JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setCompactSerialization(jweToken);
            jwe.setKey(loadKeyFromFile(request));

            String decryptedJwt = jwe.getPayload();
            request.setAttribute("result", decryptedJwt);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "Error al desencriptar el JWE: " + e.getMessage());
        }

        request.getRequestDispatcher("vista/jwt.jsp").forward(request, response);
    }

    /**
     * Carga la clave AES desde el archivo 'claveJWE.key' ubicado en la carpeta raíz del proyecto.
     *
     * @param request Objeto HttpServletRequest necesario para obtener la ruta base del servidor.
     * @return Objeto SecretKey creado a partir de la cadena del archivo.
     * @throws Exception Si el archivo no existe, no se puede leer, o la clave no tiene 16 caracteres.
     */
    private SecretKey loadKeyFromFile(HttpServletRequest request) throws Exception {
        String basePath = getServletContext().getRealPath("/");
        String keyPath = basePath.split("target")[0] + "Clave/claveJWE.key";
        String keyStr = Files.readString(Paths.get(keyPath)).trim();

        if (keyStr.length() != 16 && keyStr.length() != 32) {
            throw new Exception("La clave debe tener 16 o 32 caracteres para AES-128 o AES-256.");
        }

        return new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
    }
}
