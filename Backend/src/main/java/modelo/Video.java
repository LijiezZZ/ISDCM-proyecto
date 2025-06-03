package modelo;

import jakarta.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Modelo que representa un vídeo dentro de la aplicación.
 * 
 * Incluye información como título, autor, duración, y metadatos como número de reproducciones y marcas de tiempo.
 * Utiliza anotaciones JSON-B para el formato correcto de fechas y horas en la serialización JSON.
 * 
 * @author Kenny Alejandro / Lijie Yin
 */
public class Video {

    private Integer id;
    private String titulo;
    private String autor;

    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate fechaCreacion;

    @JsonbDateFormat("HH:mm:ss")
    private LocalTime duracion;

    private Integer numReproducciones;
    private String descripcion;
    private String formato;
    private String localizacion;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime creacionTimestamp;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime modificacionTimestamp;

    private Integer userId;

    /**
     * Constructor por defecto.
     */
    public Video() {}

    /**
     * Constructor completo para inicializar todos los atributos del vídeo.
     *
     * @param id Identificador único del vídeo.
     * @param titulo Título del vídeo.
     * @param autor Autor o creador del vídeo.
     * @param fechaCreacion Fecha de creación del vídeo.
     * @param duracion Duración del vídeo.
     * @param numReproducciones Número total de veces que el vídeo ha sido reproducido.
     * @param descripcion Descripción textual del contenido del vídeo.
     * @param formato Formato del archivo de vídeo (por ejemplo, MP4, AVI).
     * @param localizacion Ruta o URL de almacenamiento del vídeo.
     * @param creacionTimestamp Marca de tiempo de creación del registro.
     * @param modificacionTimestamp Marca de tiempo de la última modificación del registro.
     * @param userId ID del usuario propietario del vídeo.
     */
    public Video(Integer id, String titulo, String autor, LocalDate fechaCreacion, LocalTime duracion,
                 Integer numReproducciones, String descripcion, String formato, String localizacion,
                 OffsetDateTime creacionTimestamp, OffsetDateTime modificacionTimestamp, Integer userId) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.numReproducciones = numReproducciones;
        this.descripcion = descripcion;
        this.formato = formato;
        this.localizacion = localizacion;
        this.creacionTimestamp = creacionTimestamp;
        this.modificacionTimestamp = modificacionTimestamp;
        this.userId = userId;
    }

    /** @return ID del vídeo. */
    public Integer getId() { return id; }

    /** @param id ID del vídeo. */
    public void setId(Integer id) { this.id = id; }

    /** @return Título del vídeo. */
    public String getTitulo() { return titulo; }

    /** @param titulo Título del vídeo. */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /** @return Autor o creador del vídeo. */
    public String getAutor() { return autor; }

    /** @param autor Autor o creador del vídeo. */
    public void setAutor(String autor) { this.autor = autor; }

    /** @return Fecha de creación del vídeo. */
    public LocalDate getFechaCreacion() { return fechaCreacion; }

    /** @param fechaCreacion Fecha de creación del vídeo. */
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /** @return Duración del vídeo. */
    public LocalTime getDuracion() { return duracion; }

    /** @param duracion Duración del vídeo. */
    public void setDuracion(LocalTime duracion) { this.duracion = duracion; }

    /** @return Número de veces que el vídeo ha sido reproducido. */
    public Integer getNumReproducciones() { return numReproducciones; }

    /** @param numReproducciones Número de reproducciones del vídeo. */
    public void setNumReproducciones(Integer numReproducciones) { this.numReproducciones = numReproducciones; }

    /** @return Descripción del vídeo. */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion Descripción del vídeo. */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return Formato del archivo de vídeo. */
    public String getFormato() { return formato; }

    /** @param formato Formato del archivo de vídeo. */
    public void setFormato(String formato) { this.formato = formato; }

    /** @return Ubicación del archivo de vídeo. */
    public String getLocalizacion() { return localizacion; }

    /** @param localizacion Ubicación o URL del vídeo. */
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    /** @return Fecha y hora de creación del registro. */
    public OffsetDateTime getCreacionTimestamp() { return creacionTimestamp; }

    /** @param creacionTimestamp Timestamp de creación del registro. */
    public void setCreacionTimestamp(OffsetDateTime creacionTimestamp) { this.creacionTimestamp = creacionTimestamp; }

    /** @return Fecha y hora de la última modificación del registro. */
    public OffsetDateTime getModificacionTimestamp() { return modificacionTimestamp; }

    /** @param modificacionTimestamp Timestamp de la última modificación. */
    public void setModificacionTimestamp(OffsetDateTime modificacionTimestamp) { this.modificacionTimestamp = modificacionTimestamp; }

    /** @return ID del usuario propietario del vídeo. */
    public Integer getUserId() { return userId; }

    /** @param userId ID del usuario propietario del vídeo. */
    public void setUserId(Integer userId) { this.userId = userId; }
}
