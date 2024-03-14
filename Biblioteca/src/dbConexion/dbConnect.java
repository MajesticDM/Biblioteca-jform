package dbConexion;

import clases.salida;

import javax.swing.plaf.synth.Region;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class dbConnect {
    public static String db = "jdbc:mysql://localhost:3306/bibliotecasena";
    public static String userName = "daniel";
    public static String password = "morenodanie";

    //Conectar a base de datos
    public static Connection conectar(){
        try{
            return DriverManager.getConnection(db, userName, password);
        }catch(Exception ex){
            return null;
        }
    }

    //Logearse
    public static ResultSet consultarLogin(String usuario, String contrasena) throws SQLException {
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();
        String query = "SELECT NOMBRE_USUARIO FROM usuarios WHERE USUARIO LIKE '%"+usuario+"%' AND CONTRASENA LIKE '%"+contrasena+"%'";

        return Mysql.executeQuery(query);
    }

    //Préstamos
    public static ResultSet  ConsultarLibrosPrestados() throws SQLException{
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();

        String query = "SELECT salida.ID_SALIDA AS ID, tipos_salida.NOMBRE_SALIDA AS TIPO,libros.NOMBRE_LIBRO AS LIBRO,salidas.NOMBRE_CLIENTE AS CLIENTE,salidas.FECHA_SALIDA AS FECHA,salidas.COMENTARIO FROM salidas INNER JOIN libros on salidas.IDX_LIBRO = libros.ID_LIBRO INNER JOIN tipos_salida on salidas.IDX_TIPO_SALIDA = tipos_salida.ID_TIPOS_SALIDA;";

        return Mysql.executeQuery(query);
    }

    public static String ActualizaSalida(salida salida) throws SQLException{
        Connection conexion = conectar();
        String query = "UPDATE salidas SET IDX_TIPO_SALIDA = ?, NOMBRE_CLIENTE = ?, CORREO_CLIENTE = ?, COMENTARIO = ? WHERE salidas.ID_SALIDA = ?";

        try{
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setString(1,salida.tipo);
            Mysql.setString(2,salida.cliente);
            Mysql.setString(3,salida.correo);
            Mysql.setString(4,salida.comentario);
            Mysql.setInt(5,salida.id_Salida);

            Mysql.executeUpdate();

            return "Salida actualizada";
        }
        catch (Exception ex){
            return "Hubo un error al tratar de actualizar la salida: " +ex;
        }
    }

    public static String InsertarSalida(salida salida) throws SQLException{
        Connection conexion = conectar();
        String query = "INSERT INTO salidas (IDX_TIPO_SALIDA,NOMBRE_CLIENTE, CORREO_CLIENTE, IDX_LIBRO, FECHA_SALIDA, COMENTARIO) VALUES (?,?,?,?,"+ LocalDateTime.now() +",?);";

        try {
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setInt(1,salida.id_Tipo);
            Mysql.setString(2,salida.cliente);
            Mysql.setString(3,salida.correo);
            Mysql.setInt(4,salida.id_Libro);
            Mysql.setString(5,salida.comentario);

            Mysql.executeUpdate();

            return "Salida agregada";
        }catch (Exception ex){

            return "Hubo un error al tratar de agregar la salida: " +ex;
        }
    }

    public static ResultSet cargarLibros() throws SQLException{
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();

        String query = "SELECT * FROM libros";

        return Mysql.executeQuery(query);
    }

    public static ResultSet cargarTipoSalida() throws SQLException{
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();

        String query = "SELECT * FROM tipos_salida";
        return Mysql.executeQuery(query);
    }

    public static ResultSet cargarGeneros() throws SQLException{
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();

        String query = "SELECT * FROM generos_libros";
        return Mysql.executeQuery(query);
    }
}
