package mapper;

import dto.VideoDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import modelo.Video;

/**
 * Mapper para convertir entre Video y VideoDTO.
 * 
 * @author Kenny Alejandro
 */
public class VideoMapper {

    /**
     * Convierte un VideoDTO recibido desde el frontend a una entidad Video.
     *
     * @param dto el objeto recibido
     * @return una instancia de Video con los datos convertidos
     */
    public static Video fromDTO(VideoDTO dto) {
        return new Video(
                dto.getId(),
                dto.getTitulo(),
                dto.getAutor(),
                LocalDate.parse(dto.getFechaCreacion()),
                LocalTime.parse(dto.getDuracion()),
                dto.getNumReproducciones(),
                dto.getDescripcion(),
                dto.getFormato(),
                dto.getLocalizacion(),
                OffsetDateTime.parse(dto.getCreacionTimestamp()),
                OffsetDateTime.parse(dto.getModificacionTimestamp()),
                dto.getUserId()
        );
    }

    /**
     * Convierte un objeto Video a su representación DTO para enviar al frontend.
     *
     * @param video el modelo de base de datos
     * @return DTO listo para serializar a JSON
     */
    public static VideoDTO toDTO(Video video) {
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setTitulo(video.getTitulo());
        dto.setAutor(video.getAutor());
        dto.setDescripcion(video.getDescripcion());
        dto.setFechaCreacion(video.getFechaCreacion().toString());
        dto.setDuracion(video.getDuracion().toString());
        dto.setCreacionTimestamp(video.getCreacionTimestamp().toString());
        dto.setModificacionTimestamp(video.getModificacionTimestamp().toString());
        dto.setFormato(video.getFormato());
        dto.setLocalizacion(video.getLocalizacion());
        dto.setNumReproducciones(video.getNumReproducciones());
        dto.setUserId(video.getUserId());
        return dto;
    }
} 