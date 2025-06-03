package dto;

/**
 * Objeto de transferencia de datos (DTO) para la entidad {@code Video}.
 * <p>
 * Este DTO es utilizado para comunicar datos de vídeo entre el backend y el frontend
 * a través de la API REST. Todos los campos de tipo fecha y hora están representados como
 * cadenas en formatos específicos compatibles con JSON.
 * </p>
 * 
 * <ul>
 *   <li><strong>fechaCreacion:</strong> "yyyy-MM-dd"</li>
 *   <li><strong>duracion:</strong> "HH:mm:ss"</li>
 *   <li><strong>creacionTimestamp / modificacionTimestamp:</strong> "yyyy-MM-dd'T'HH:mm:ss.SSSX"</li>
 * </ul>
 * 
 * @author Kenny Alejandro
 */
public class VideoDTO {

    private Integer id;
    private String titulo;
    private String autor;
    private String descripcion;
    private String fechaCreacion;
    private String duracion;
    private String creacionTimestamp;
    private String modificacionTimestamp;
    private String formato;
    private String localizacion;
    private Integer numReproducciones;
    private Integer userId;

    /**
     * Constructor vacío necesario para frameworks de serialización/deserialización.
     */
    public VideoDTO() {}

    /**
     * Obtiene el ID del vídeo.
     * @return ID del vídeo.
     */
    public Integer getId() { return id; }

    /**
     * Establece el ID del vídeo.
     * @param id ID del vídeo.
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * Obtiene el título del vídeo.
     * @return Título del vídeo.
     */
    public String getTitulo() { return titulo; }

    /**
     * Establece el título del vídeo.
     * @param titulo Título del vídeo.
     */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /**
     * Obtiene el autor del vídeo.
     * @return Autor del vídeo.
     */
    public String getAutor() { return autor; }

    /**
     * Establece el autor del vídeo.
     * @param autor Autor del vídeo.
     */
    public void setAutor(String autor) { this.autor = autor; }

    /**
     * Obtiene la descripción del vídeo.
     * @return Descripción del vídeo.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Establece la descripción del vídeo.
     * @param descripcion Descripción del vídeo.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene la fecha de creación del vídeo (formato: yyyy-MM-dd).
     * @return Fecha de creación como texto.
     */
    public String getFechaCreacion() { return fechaCreacion; }

    /**
     * Establece la fecha de creación del vídeo.
     * @param fechaCreacion Fecha de creación (formato: yyyy-MM-dd).
     */
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /**
     * Obtiene la duración del vídeo (formato: HH:mm:ss).
     * @return Duración como texto.
     */
    public String getDuracion() { return duracion; }

    /**
     * Establece la duración del vídeo.
     * @param duracion Duración (formato: HH:mm:ss).
     */
    public void setDuracion(String duracion) { this.duracion = duracion; }

    /**
     * Obtiene la marca de tiempo de creación del vídeo (formato: yyyy-MM-dd'T'HH:mm:ss.SSSX).
     * @return Timestamp de creación como texto.
     */
    public String getCreacionTimestamp() { return creacionTimestamp; }

    /**
     * Establece la marca de tiempo de creación del vídeo.
     * @param creacionTimestamp Timestamp de creación (formato: yyyy-MM-dd'T'HH:mm:ss.SSSX).
     */
    public void setCreacionTimestamp(String creacionTimestamp) { this.creacionTimestamp = creacionTimestamp; }

    /**
     * Obtiene la marca de tiempo de modificación del vídeo.
     * @return Timestamp de modificación como texto.
     */
    public String getModificacionTimestamp() { return modificacionTimestamp; }

    /**
     * Establece la marca de tiempo de modificación del vídeo.
     * @param modificacionTimestamp Timestamp de modificación (formato: yyyy-MM-dd'T'HH:mm:ss.SSSX).
     */
    public void setModificacionTimestamp(String modificacionTimestamp) { this.modificacionTimestamp = modificacionTimestamp; }

    /**
     * Obtiene el formato del vídeo.
     * @return Formato del archivo de vídeo (por ejemplo, MP4, AVI).
     */
    public String getFormato() { return formato; }

    /**
     * Establece el formato del vídeo.
     * @param formato Formato del archivo.
     */
    public void setFormato(String formato) { this.formato = formato; }

    /**
     * Obtiene la ubicación o URL del vídeo.
     * @return Localización del archivo de vídeo.
     */
    public String getLocalizacion() { return localizacion; }

    /**
     * Establece la ubicación o URL del vídeo.
     * @param localizacion Localización del vídeo.
     */
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    /**
     * Obtiene el número total de reproducciones del vídeo.
     * @return Número de reproducciones.
     */
    public Integer getNumReproducciones() { return numReproducciones; }

    /**
     * Establece el número de reproducciones del vídeo.
     * @param numReproducciones Número de veces que el vídeo ha sido visto.
     */
    public void setNumReproducciones(Integer numReproducciones) { this.numReproducciones = numReproducciones; }

    /**
     * Obtiene el ID del usuario propietario del vídeo.
     * @return ID del usuario.
     */
    public Integer getUserId() { return userId; }

    /**
     * Establece el ID del usuario propietario del vídeo.
     * @param userId ID del usuario.
     */
    public void setUserId(Integer userId) { this.userId = userId; }
}
