/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author alumne
 */
public class Usuario {

    private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private String username;
    private String password;
    private Integer indBaja;
    private Timestamp creacionTimestamp;
    private Timestamp modificacionTimestamp;
    private Integer indFallos;

    // Para registrar usuario
    public Usuario(String nombre, String apellidos, String email, String username, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Para guardar en una sesión
    public Usuario(Integer id, String nombre, String apellidos, String email, String username, Integer indBaja, Timestamp creacionTimestamp, Timestamp modificacionTimestamp, Integer indFallos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.indBaja = indBaja;
        this.creacionTimestamp = creacionTimestamp;
        this.modificacionTimestamp = modificacionTimestamp;
        this.indFallos = indFallos;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public Integer getIndBaja() {
        return indBaja;
    }
    
    public Timestamp getCreacionTimestamp() {
        return creacionTimestamp;
    }
    
    public Timestamp getModificacionTimestamp() {
        return modificacionTimestamp;
    }
    
    public Integer getIndFallos() {
        return indFallos;
    }
}
