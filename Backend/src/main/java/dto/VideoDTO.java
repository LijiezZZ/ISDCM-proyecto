package dto;

/**
 * Objeto de transferencia de datos para la entidad Video.
 * Utilizado para comunicación con el frontend (REST).
 *
 *
 * @author kennyalejandro
 */
public class VideoDTO {
    private Integer id;
    private String titulo;
    private String autor;
    private String descripcion;
    private String fechaCreacion;          // formato: "yyyy-MM-dd"
    private String duracion;               // formato: "HH:mm:ss"
    private String creacionTimestamp;      // formato: "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    private String modificacionTimestamp;  // mismo formato
    private String formato;
    private String localizacion;
    private Integer numReproducciones;
    private Integer userId;

    public VideoDTO() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getCreacionTimestamp() { return creacionTimestamp; }
    public void setCreacionTimestamp(String creacionTimestamp) { this.creacionTimestamp = creacionTimestamp; }

    public String getModificacionTimestamp() { return modificacionTimestamp; }
    public void setModificacionTimestamp(String modificacionTimestamp) { this.modificacionTimestamp = modificacionTimestamp; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getLocalizacion() { return localizacion; }
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    public Integer getNumReproducciones() { return numReproducciones; }
    public void setNumReproducciones(Integer numReproducciones) { this.numReproducciones = numReproducciones; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}
