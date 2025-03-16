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
 * @author alumne
 */
public class Video {
    
    private Integer id;
    private String titulo;
    private String autor;
    private Date fechaCreacion;
    private Timestamp creacionTimestamp;
    private Timestamp modificacionTimestamp;
    private Time duracion;
    private Integer numReproducciones;
    private String descripcion;
    private String formato;
    private String localizacion;
    private Integer userId;

    public Video(Integer id, String titulo, String autor, Date fechaCreacion, Timestamp creacionTimestamp,Timestamp modificacionTimestamp , Time duracion, Integer numReproducciones, String descripcion, String formato, String localizacion, Integer userId) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.creacionTimestamp = creacionTimestamp;
        this.modificacionTimestamp = modificacionTimestamp;
        this.duracion = duracion;
        this.numReproducciones = numReproducciones;
        this.descripcion = descripcion;
        this.formato = formato;
        this.localizacion = localizacion;
        this.userId = userId;
    }
    
    public Video(String titulo, String autor, Date fechaCreacion, Timestamp creacionTimestamp,Timestamp modificacionTimestamp, Time duracion, Integer numReproducciones, String descripcion, String formato, String localizacion, Integer userId) {
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.creacionTimestamp = creacionTimestamp;
        this.modificacionTimestamp = modificacionTimestamp;
        this.duracion = duracion;
        this.numReproducciones = numReproducciones;
        this.descripcion = descripcion;
        this.formato = formato;
        this.localizacion = localizacion;
        this.userId = userId;
    }
    
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

    public Timestamp getCreacionTimestamp() {
        return creacionTimestamp;
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

    public Integer getUserId() {
        return userId;
    }
    
    
}
