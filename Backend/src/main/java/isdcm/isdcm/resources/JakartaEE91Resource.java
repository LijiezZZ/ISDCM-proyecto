package isdcm.isdcm.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

/**
 * Recurso RESTful que proporciona métodos de ejemplo utilizando Jakarta EE 9.
 * 
 * Permite recibir y mostrar información enviada mediante solicitudes HTTP GET y POST.
 * 
 * @author Kenny Alejandro / Lijie Yin
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    
    /**
     * Maneja solicitudes HTTP GET en la ruta <code>/jakartaee9/getInfo</code>.
     * 
     * Devuelve una página HTML simple que muestra la información y la fecha proporcionadas como parámetros de consulta.
     * 
     * @param info Texto informativo enviado por el cliente.
     * @param fecha Fecha asociada a la información proporcionada.
     * @return Respuesta HTML con los datos recibidos embebidos en el contenido.
     */
    @Path("getInfo")
    @GET    
    @Produces("text/html")
    public String getInfo (@QueryParam("info") String info, 
                            @QueryParam("fecha") String fecha) {
        
        return "<html><head></head> <body> Informaci&oacute;n recibida " + info + " en fecha " + fecha + " </body></html>";
    }

    /**
     * Maneja solicitudes HTTP POST en la ruta <code>/jakartaee9/postInfo</code>.
     * 
     * Recibe los datos mediante parámetros de formulario y genera una respuesta HTML con la información proporcionada.
     * 
     * @param info Texto informativo enviado por el cliente a través de un formulario.
     * @param fecha Fecha asociada a la información, también enviada por formulario.
     * @return Respuesta HTML con los datos recibidos embebidos en el contenido.
     */
    @Path("postInfo")   
    @POST    
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String postInfo (@FormParam("info") String info, 
                            @FormParam("fecha") String fecha) 
    {                
        return "<html><head></head> <body> Informaci&oacute;n recibida " + info + " en fecha " + fecha + " </body></html>";
    } 
}
