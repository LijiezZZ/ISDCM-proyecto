package isdcm.util;

/**
 * * Configuración centralizada para JWT.
 *  Contiene la clave secreta y el tiempo de expiración del token.
 * @author kennyalejandro
 */
public class JwtConfig {

    /**
     * Clave secreta para firmar y validar JWTs.
     * En producción, debe cargarse desde una variable de entorno o archivo externo.
     */
    public static final String SECRET_KEY = "0123456789012345678901234567890123456789012345678901234567890123";

    /**
     * Duración del token en milisegundos.
     * 30 minutos = 1000 * 60 * 30
     */
    public static final long EXPIRATION_TIME = 1000 * 60 * 30;
}
