package isdcm.isdcm.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.Video;
import modelo.dao.VideoDAO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Recurso REST que gestiona operaciones relacionadas con vídeos.
 * Proporciona funcionalidades CRUD y de búsqueda mediante llamadas HTTP.
 * 
 * Ruta base del recurso: /resources/videos
 * 
 * Métodos soportados:
 * - GET: listar, buscar, obtener por ID
 * - POST: registrar, visualizar
 * - PUT: actualizar
 * - DELETE: eliminar
 * 
 * Este recurso utiliza la capa DAO (VideoDAO) para acceder a la base de datos.
 * 
 * @author Kennny Alejandro/ Lijie Yin
 */
@Path("videos")
public class VideoResource {

    private VideoDAO videoDAO = new VideoDAO();

    /**
    * Endpoint para obtener una lista de vídeos ordenados por fecha de creación ascendente.
    * 
    * El número de resultados está limitado a un máximo de 1000. Si no se especifica el parámetro,
    * se devuelven 100 elementos por defecto.
    * 
    * Ejemplos:
    * - GET /resources/videos → devuelve 100 vídeos (por defecto)
    * - GET /resources/videos?top=50 → devuelve los 50 más antiguos
    * - GET /resources/videos?top=1200 → error 400 (límite máximo: 1000)
    *
    * @param top Número máximo de resultados a devolver (opcional)
    * @return Lista de vídeos en formato JSON o error si se supera el límite
    */
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllVideos(@QueryParam("top") Integer top) {
       try {
           int limite = (top == null) ? 100 : top;
           List<Video> videos = videoDAO.getAllVideos(limite);
           return Response.ok(videos).build();
       } catch (IllegalArgumentException e) {
           return Response.status(Response.Status.BAD_REQUEST)
                   .entity("El número máximo de vídeos permitidos es 1000.")
                   .build();
       }
   }

    /**
    * Endpoint para buscar vídeos aplicando filtros y ordenamiento dinámico.
    * 
    * Todos los parámetros son opcionales. Si no se especifica ningún filtro, 
    * se devuelven los vídeos ordenados por fecha de creación ascendente (por defecto),
    * con un máximo de 100 resultados.
    * 
    * Parámetros:
    * - titulo:     Filtro por coincidencia parcial en el título
    * - autor:      Filtro exacto por nombre de autor
    * - fecha:      Filtro por fecha de creación (formato yyyy, yyyy-MM o yyyy-MM-dd)
    * - top:        Número máximo de resultados (default: 100, máximo: 1000)
    * - orden:      Campo de ordenamiento ("fecha" → usa CREACIONTIMESTAMP, o "vistas")
    * - dir:        Dirección del orden ("asc" o "desc"), default: "asc"
    * 
    * Ejemplos:
    * - GET /resources/videos/buscar → top 100 vídeos más antiguos
    * - GET /resources/videos/buscar?titulo=java&orden=vistas&dir=desc → más vistos con "java" en el título
    * - GET /resources/videos/buscar?fecha=2024&top=20 → primeros 20 vídeos de 2024
    * 
    * @param titulo Filtro por título (opcional)
    * @param autor  Filtro por autor (opcional)
    * @param fecha  Filtro por fecha (opcional)
    * @param top    Límite de resultados (default: 100, máximo: 1000)
    * @param orden  Campo de orden: "fecha", "vistas" (default: "fecha")
    * @param dir    Dirección de orden: "asc" o "desc" (default: "asc")
    * @return Lista de vídeos que cumplen los criterios
    */
    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarVideos(
            @QueryParam("titulo") String titulo,
            @QueryParam("autor") String autor,
            @QueryParam("fecha") String fecha,
            @QueryParam("top") @DefaultValue("100") int top,
            @QueryParam("orden") @DefaultValue("fecha") String orden,
            @QueryParam("dir") @DefaultValue("asc") String dir
    ) {
        // Validar el formato de fecha si viene
        if (fecha != null) {
            boolean formatoValido = fecha.matches("\\d{4}") || fecha.matches("\\d{4}-\\d{2}") || fecha.matches("\\d{4}-\\d{2}-\\d{2}");
            if (!formatoValido) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de fecha inválido. Usa yyyy, yyyy-MM o yyyy-MM-dd.")
                        .build();
            }
        }

        try {
            List<Video> resultados = videoDAO.buscarVideos(titulo, autor, fecha, top, orden, dir);
            return Response.ok(resultados).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Obtiene los detalles de un vídeo específico a partir de su ID.
     * 
     * @param id ID del vídeo
     * @return Objeto Video si se encuentra, o error 404 si no existe
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideoById(@PathParam("id") int id) {
        Video video = videoDAO.getVideo(id);
        if (video.getId() == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Video no encontrado.")
                    .build();
        }
        return Response.ok(video).build();
    }

    /**
     * Registra un nuevo vídeo en la base de datos.
     * 
     * @param video Objeto Video con los datos del nuevo vídeo
     * @return Código 201 si se crea correctamente, o error 500 en caso contrario
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
    public Response registrarVideo(Video video) {
        boolean creado = videoDAO.registerVideo(video);
        if (creado) {
            return Response.status(Response.Status.CREATED).entity(video).build();
        } else {
            return Response.serverError().entity("No se pudo registrar el video.").build();
        }
    }

    /**
     * Visualiza un vídeo, lo cual incrementa su contador de reproducciones.
     * 
     * @param id ID del vídeo
     * @return Objeto Video actualizado o error si no se encuentra o falla la operación
     */
    @POST
    @Path("/visualizar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response visualizarVideo(@PathParam("id") int id) {
        Video video = videoDAO.getVideo(id);
        if (video.getId() == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Video no encontrado.")
                    .build();
        }

        int nuevasReproducciones = video.getNumReproducciones() + 1;
        boolean actualizado = videoDAO.updateReproducciones(id, nuevasReproducciones);

        if (actualizado) {
            Video actualizadoVideo = videoDAO.getVideo(id);
            return Response.ok(actualizadoVideo).build();
        } else {
            return Response.serverError().entity("Error al actualizar reproducciones.").build();
        }
    }

    /**
     * Actualiza los datos de un vídeo (título, autor, descripción).
     * 
     * @param id ID del vídeo a modificar
     * @param videoActualizado Objeto Video con los nuevos valores
     * @return Objeto actualizado si tiene éxito, o error 404 si no existe
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarVideo(@PathParam("id") int id, Video videoActualizado) {
        boolean actualizado = videoDAO.updateVideo(id, videoActualizado.getTitulo(), videoActualizado.getAutor(), videoActualizado.getDescripcion());

        if (actualizado) {
            Video video = videoDAO.getVideo(id);
            return Response.ok(video).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se pudo actualizar el video con ID " + id)
                    .build();
        }
    }

    /**
     * Elimina un vídeo a partir de su ID.
     * 
     * @param id ID del vídeo
     * @return Mensaje de confirmación o error 404 si no se encuentra
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarVideo(@PathParam("id") int id) {
        boolean eliminado = videoDAO.deleteVideo(id);
        if (eliminado) {
            return Response.ok("Video eliminado correctamente.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró video con ID " + id)
                    .build();
        }
    }

    /**
     * Verifica si un usuario es propietario de un vídeo.
     * 
     * @param id ID del vídeo
     * @param userId ID del usuario
     * @return true si es propietario, false si no
     */
    @GET
    @Path("/{id}/propietario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarPropiedad(@PathParam("id") int id, @QueryParam("userId") int userId) {
        boolean esPropietario = videoDAO.isVideoOwner(id, userId);
        return Response.ok(esPropietario).build();
    }

    /**
     * Verifica si un vídeo con un título ya ha sido registrado por un usuario.
     * 
     * @param titulo Título del vídeo
     * @param userId ID del usuario
     * @return true si el vídeo existe para ese usuario, false si no
     */
    @GET
    @Path("/existe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarExiste(@QueryParam("titulo") String titulo, @QueryParam("userId") int userId) {
        boolean existe = videoDAO.isVideoRegistered(titulo, userId);
        return Response.ok(existe).build();
    }
}
