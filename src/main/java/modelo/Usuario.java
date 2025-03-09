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
    private  Integer id;
    private  String nombre;
    private  String apellidos;
    private  String email;
    private  String username;
    private  String password;

    public Usuario(Integer id, String nombre, String apellidos, String email, String username, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public Usuario(Integer id, String nombre, String apellidos, String email, String username) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() {return apellidos; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
