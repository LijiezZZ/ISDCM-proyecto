/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

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
    private int indBaja;

    public Usuario(String nombre, String apellidos, String email, String username, String password, int indBaja) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.password = password;
        this.indBaja = indBaja;
    }

    public Usuario(Integer id, String nombre, String apellidos, String email, String username, int indBaja) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.indBaja = indBaja;
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
    
    public int getIndBaja() {
        return indBaja;
    }
}
