/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author Kenny Alejandro/Lijie Yin
 */
public class Video {

    private Integer id;
    private String titulo;
    private String autor;
    private Date fechaCreacion;
    private Time duracion;
    private Integer numReproducciones;
    private String descripcion;
    private String formato;
    private String localizacion;
    private Timestamp creacionTimestamp;
    private Timestamp modificacionTimestamp;
    private Integer userId;

    public Video(){
    }
    // Para listar el video, información completa
    public Video(Integer id, String titulo, String autor, Date fechaCreacion, Time duracion, Integer numReproducciones, String descripcion, String formato, String localizacion, Timestamp creacionTimestamp, Timestamp modificacionTimestamp, Integer userId) {
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
    
    // Para registrar video
    public Video(String titulo, String autor, Date fechaCreacion, Time duracion, Integer numReproducciones, String descripcion, String formato, String localizacion, Integer userId) {
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.numReproducciones = numReproducciones;
        this.descripcion = descripcion;
        this.formato = formato;
        this.localizacion = localizacion;
        this.userId = userId;
    }
    
    // Para actualizar video
    public Video(String titulo, String autor, String descripcion) {
        this.titulo = titulo;
        this.autor = autor;
        this.descripcion = descripcion;
    }
    

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Time getDuracion() {
        return duracion;
    }

    public Integer getNumReproducciones() {
        return numReproducciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFormato() {
        return formato;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public Timestamp getCreacionTimestamp() {
        return creacionTimestamp;
    }

    public Timestamp getModificacionTimestamp() {
        return modificacionTimestamp;
    }

    public Integer getUserId() {
        return userId;
    }

}
