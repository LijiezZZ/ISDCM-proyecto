package modelo;

import jakarta.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Modelo de vídeo utilizado en la API REST.
 * Incluye formato explícito para fechas y horas compatible con JSON-B.
 * 
 * @author 
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

    public Video() {}

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

    // Getters y Setters completos

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalTime getDuracion() { return duracion; }
    public void setDuracion(LocalTime duracion) { this.duracion = duracion; }

    public Integer getNumReproducciones() { return numReproducciones; }
    public void setNumReproducciones(Integer numReproducciones) { this.numReproducciones = numReproducciones; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getLocalizacion() { return localizacion; }
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    public OffsetDateTime getCreacionTimestamp() { return creacionTimestamp; }
    public void setCreacionTimestamp(OffsetDateTime creacionTimestamp) { this.creacionTimestamp = creacionTimestamp; }

    public OffsetDateTime getModificacionTimestamp() { return modificacionTimestamp; }
    public void setModificacionTimestamp(OffsetDateTime modificacionTimestamp) { this.modificacionTimestamp = modificacionTimestamp; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
