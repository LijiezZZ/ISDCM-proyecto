package modelo;

import java.sql.Timestamp;

/**
 * Modelo de entidad que representa un usuario dentro del sistema.
 *
 * Esta clase contiene los atributos necesarios para registrar, autenticar y
 * gestionar usuarios, incluyendo información personal, credenciales, estado
 * de baja y registros de creación/modificación.
 *
 * Es utilizada principalmente en la lógica de negocio, controladores y
 * sesiones de usuario.
 *
 * Campos clave:
 * - username: identificador único de acceso
 * - indBaja: indica si el usuario está activo (0) o dado de baja (1)
 * - indFallos: número de intentos fallidos de login
 * - timestamps: fechas de creación y modificación del registro
 *
 * @author Kenny Alejandro / Lijie Yin
 */
public class Usuario {

    private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private String username;
    private String password;
    private Integer indBaja;
    private Timestamp creacionTimestamp;
    private Timestamp modificacionTimestamp;
    private Integer indFallos;

    /**
     * Constructor utilizado durante el registro de nuevos usuarios.
     *
     * @param nombre      Nombre del usuario.
     * @param apellidos   Apellidos del usuario.
     * @param email       Correo electrónico.
     * @param username    Nombre de usuario (único).
     * @param password    Contraseña de acceso.
     */
    public Usuario(String nombre, String apellidos, String email, String username, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor utilizado al cargar usuarios desde la base de datos para sesiones u operaciones internas.
     *
     * @param id                    Identificador único.
     * @param nombre                Nombre del usuario.
     * @param apellidos             Apellidos del usuario.
     * @param email                 Correo electrónico.
     * @param username              Nombre de usuario.
     * @param indBaja               Indicador de baja lógica (0 = activo, 1 = dado de baja).
     * @param creacionTimestamp     Fecha y hora de creación del usuario.
     * @param modificacionTimestamp Fecha y hora de última modificación.
     * @param indFallos             Número de intentos fallidos de autenticación.
     */
    public Usuario(Integer id, String nombre, String apellidos, String email, String username, Integer indBaja,
                   Timestamp creacionTimestamp, Timestamp modificacionTimestamp, Integer indFallos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.indBaja = indBaja;
        this.creacionTimestamp = creacionTimestamp;
        this.modificacionTimestamp = modificacionTimestamp;
        this.indFallos = indFallos;
    }

    /**
     * @return ID único del usuario.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return Nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return Apellidos del usuario.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @return Correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Nombre de usuario utilizado para iniciar sesión.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Contraseña del usuario (almacenada en texto plano o cifrado, según implementación).
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return Indicador de baja lógica (0 = activo, 1 = dado de baja).
     */
    public Integer getIndBaja() {
        return indBaja;
    }

    /**
     * @return Fecha y hora en que se creó el registro de usuario.
     */
    public Timestamp getCreacionTimestamp() {
        return creacionTimestamp;
    }

    /**
     * @return Fecha y hora de la última modificación de datos del usuario.
     */
    public Timestamp getModificacionTimestamp() {
        return modificacionTimestamp;
    }

    /**
     * @return Número de intentos fallidos de autenticación registrados.
     */
    public Integer getIndFallos() {
        return indFallos;
    }
}