/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar la conexión a la base de datos Apache Derby.
 *
 * Esta clase proporciona un método estático para obtener una conexión JDBC
 * usando el driver cliente de Derby. Se conecta a una base de datos ubicada
 * en `localhost:1527` con credenciales predefinidas.
 *
 * La conexión debe ser cerrada manualmente por el consumidor tras su uso.
 *
 * Parámetros de conexión utilizados:
 * - URL: jdbc:derby://localhost:1527/pr2
 * - Usuario: pr2
 * - Contraseña: pr2
 *
 * Esta clase está pensada para ser utilizada por las capas DAO o de servicio
 * que requieran acceso directo a la base de datos.
 *
 * @author Kenny Alejandro / Lijie Yin
 */
public class ConexionDB {
    private static final String URL = "jdbc:derby://localhost:1527/pr2";
    private static final String USER = "pr2";
    private static final String PASSWORD = "pr2";
    
    /**
     * Obtiene una conexión activa a la base de datos Derby.
     *
     * Este método carga dinámicamente el driver JDBC si es necesario
     * y retorna una conexión lista para usarse.
     *
     * @return Objeto Connection activo hacia la base de datos.
     * @throws SQLException Si ocurre un error al cargar el driver o al establecer la conexión.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver de la base de datos.", e);
        }
    }
   
}



