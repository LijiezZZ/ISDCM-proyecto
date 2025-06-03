package modelo;

import java.sql.Timestamp;

/**
 * Representa un usuario del sistema, incluyendo sus datos de autenticación,
 * información personal y metadatos de auditoría.
 * 
 * Esta clase se utiliza tanto para registrar nuevos usuarios como para gestionar
 * datos de sesión y persistencia en base de datos.
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
     * Constructor utilizado para registrar un nuevo usuario.
     * 
     * @param nombre Nombre del usuario.
     * @param apellidos Apellidos del usuario.
     * @param email Correo electrónico del usuario.
     * @param username Nombre de usuario para autenticación.
     * @param password Contraseña del usuario.
     */
    public Usuario(String nombre, String apellidos, String email, String username, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor utilizado para cargar los datos completos de un usuario,
     * generalmente desde la base de datos.
     * 
     * @param id Identificador único del usuario.
     * @param nombre Nombre del usuario.
     * @param apellidos Apellidos del usuario.
     * @param email Correo electrónico del usuario.
     * @param username Nombre de usuario para autenticación.
     * @param indBaja Indicador de baja lógica (1 = dado de baja, 0 = activo).
     * @param creacionTimestamp Fecha y hora de creación del registro.
     * @param modificacionTimestamp Fecha y hora de la última modificación.
     * @param indFallos Número de intentos fallidos de inicio de sesión.
     */
    public Usuario(Integer id, String nombre, String apellidos, String email, String username, Integer indBaja, Timestamp creacionTimestamp, Timestamp modificacionTimestamp, Integer indFallos) {
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
     * Obtiene el identificador del usuario.
     * @return ID del usuario.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return Nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     * @return Apellidos del usuario.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return Email del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene el nombre de usuario para autenticación.
     * @return Username del usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el indicador de baja lógica del usuario.
     * @return 1 si el usuario está dado de baja, 0 si está activo.
     */
    public Integer getIndBaja() {
        return indBaja;
    }

    /**
     * Obtiene la fecha y hora de creación del usuario.
     * @return Timestamp de creación.
     */
    public Timestamp getCreacionTimestamp() {
        return creacionTimestamp;
    }

    /**
     * Obtiene la fecha y hora de la última modificación del usuario.
     * @return Timestamp de modificación.
     */
    public Timestamp getModificacionTimestamp() {
        return modificacionTimestamp;
    }

    /**
     * Obtiene el número de intentos fallidos de inicio de sesión.
     * @return Contador de fallos de autenticación.
     */
    public Integer getIndFallos() {
        return indFallos;
    }
}
