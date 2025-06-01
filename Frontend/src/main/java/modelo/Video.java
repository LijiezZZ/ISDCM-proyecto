package modelo;

/**
 * Modelo de vídeo en el frontend (equivalente a VideoDTO en el backend).
 *
 * Representa un objeto de vídeo que se utiliza para el intercambio de datos
 * en formato JSON entre el cliente (frontend) y el servidor (backend).
 * 
 * Contiene metadatos como título, autor, formato, duración, localización del
 * archivo, y también campos de trazabilidad como timestamps y número de reproducciones.
 * 
 * Esta clase es comúnmente usada por servicios REST y vistas JSP para mostrar
 * o manipular información multimedia.
 * 
 * Formatos esperados:
 * - fechaCreacion: yyyy-MM-dd
 * - duracion: HH:mm:ss
 * - creacionTimestamp / modificacionTimestamp: yyyy-MM-dd'T'HH:mm:ss.SSSX
 * 
 * @author Kenny Alejandro
 */
public class Video {
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
     * Constructor vacío requerido para deserialización JSON.
     */
    public Video() {}

    /**
     * Constructor parcial para crear un vídeo con los campos más relevantes.
     *
     * @param titulo      Título del vídeo.
     * @param autor       Autor o creador del vídeo.
     * @param descripcion Breve descripción del contenido.
     */
    public Video(String titulo, String autor, String descripcion) {
        this.titulo = titulo;
        this.autor = autor;
        this.descripcion = descripcion;
    }

    /**
     * @return Identificador único del vídeo.
     */
    public Integer getId() { return id; }

    /**
     * @param id Identificador del vídeo.
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * @return Título del vídeo.
     */
    public String getTitulo() { return titulo; }

    /**
     * @param titulo Título del vídeo.
     */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /**
     * @return Autor del vídeo.
     */
    public String getAutor() { return autor; }

    /**
     * @param autor Nombre del autor o creador del vídeo.
     */
    public void setAutor(String autor) { this.autor = autor; }

    /**
     * @return Descripción del vídeo.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * @param descripcion Texto descriptivo del contenido del vídeo.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * @return Fecha de creación del vídeo en formato yyyy-MM-dd.
     */
    public String getFechaCreacion() { return fechaCreacion; }

    /**
     * @param fechaCreacion Fecha de creación del vídeo.
     */
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /**
     * @return Duración del vídeo en formato HH:mm:ss.
     */
    public String getDuracion() { return duracion; }

    /**
     * @param duracion Duración del vídeo.
     */
    public void setDuracion(String duracion) { this.duracion = duracion; }

    /**
     * @return Timestamp de creación con zona horaria.
     */
    public String getCreacionTimestamp() { return creacionTimestamp; }

    /**
     * @param creacionTimestamp Marca de tiempo de creación.
     */
    public void setCreacionTimestamp(String creacionTimestamp) { this.creacionTimestamp = creacionTimestamp; }

    /**
     * @return Timestamp de última modificación con zona horaria.
     */
    public String getModificacionTimestamp() { return modificacionTimestamp; }

    /**
     * @param modificacionTimestamp Marca de tiempo de modificación.
     */
    public void setModificacionTimestamp(String modificacionTimestamp) { this.modificacionTimestamp = modificacionTimestamp; }

    /**
     * @return Formato del archivo de vídeo (por ejemplo: mp4, avi).
     */
    public String getFormato() { return formato; }

    /**
     * @param formato Formato del archivo de vídeo.
     */
    public void setFormato(String formato) { this.formato = formato; }

    /**
     * @return Ruta lógica o física donde se encuentra almacenado el vídeo.
     */
    public String getLocalizacion() { return localizacion; }

    /**
     * @param localizacion Ruta del archivo de vídeo en el sistema.
     */
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    /**
     * @return Número total de veces que se ha reproducido el vídeo.
     */
    public Integer getNumReproducciones() { return numReproducciones; }

    /**
     * @param numReproducciones Total de reproducciones registradas.
     */
    public void setNumReproducciones(Integer numReproducciones) { this.numReproducciones = numReproducciones; }

    /**
     * @return ID del usuario propietario del vídeo.
     */
    public Integer getUserId() { return userId; }

    /**
     * @param userId Identificador del usuario al que pertenece el vídeo.
     */
    public void setUserId(Integer userId) { this.userId = userId; }
}
