package dbConexion;

import clases.genero;
import clases.libro;
import clases.salida;
import com.mysql.cj.exceptions.StreamingNotifiable;

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

        String query = "SELECT salidas.ID_SALIDA AS ID, tipos_salida.NOMBRE_SALIDA AS TIPO,libros.NOMBRE_LIBRO AS LIBRO,salidas.NOMBRE_CLIENTE AS CLIENTE,salidas.FECHA_SALIDA AS FECHA,salidas.COMENTARIO, salidas.CORREO_CLIENTE, libros.ID_LIBRO,salidas.IDX_TIPO_SALIDA FROM salidas INNER JOIN libros on salidas.IDX_LIBRO = libros.ID_LIBRO INNER JOIN tipos_salida on salidas.IDX_TIPO_SALIDA = tipos_salida.ID_TIPOS_SALIDA;";

        return Mysql.executeQuery(query);
    }

    public static String ActualizaLibro(libro libro) throws SQLException{
        Connection conexion = conectar();
        String query = "UPDATE libros SET IDX_GENERO_LIBRO = ?, NOMBRE_LIBRO = ?, ESTADO = ? WHERE libros.ID_LIBRO = ?;";

        try{
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setInt(1,libro.idx_genero);
            Mysql.setString(2,libro.libro);
            Mysql.setBoolean(3,libro.bitEstado);
            Mysql.setInt(4,libro.id_libro);

            Mysql.executeUpdate();

            return "Género actualizado";
        }
        catch (Exception ex){
            return "Hubo un error al tratar de actualizar el género: " +ex;
        }
    }
    public static String ActualizaGenero(genero genero) throws SQLException{
        Connection conexion = conectar();
        String query = "UPDATE generos_libros SET GENERO_DESCRIPCION = ?, ESTADO = ? WHERE generos_libros.ID_GENERO_LIBRO = ?;";

        try{
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setString(1,genero.genero);
            Mysql.setBoolean(2,genero.bitEstado);
            Mysql.setInt(3,genero.id_genero);

            Mysql.executeUpdate();

            return "Género actualizado";
        }
        catch (Exception ex){
            return "Hubo un error al tratar de actualizar el género: " +ex;
        }
    }

    //No voy a usar este método, para que no se actualicen las salidas.
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

    public static String InsertarSalida(salida salida){
        Connection conexion = conectar();

        //Feo pero funciona
        LocalDateTime FechaSalida = LocalDateTime.now();
        String FechaSalida1 = FechaSalida.toString().replace("T"," ");

        String query = "INSERT INTO salidas (IDX_TIPO_SALIDA,NOMBRE_CLIENTE, CORREO_CLIENTE, IDX_LIBRO, FECHA_SALIDA, COMENTARIO) VALUES (?,?,?,?,'"+ FechaSalida1 +"',?);";

        try {
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setInt(1,(salida.id_Salida + 1));
            Mysql.setString(2,salida.cliente);
            Mysql.setString(3,salida.correo);
            Mysql.setInt(4,(salida.id_Libro + 1));
            Mysql.setString(5,salida.comentario);

            Mysql.executeUpdate();

            return "Salida agregada";
        }catch (Exception ex){

            return "Hubo un error al tratar de agregar la salida: " +ex;
        }
    }

    public static String InsertarNuevoGenero(genero genero){
        Connection conexion = conectar();
        String query = "INSERT INTO generos_libros (GENERO_DESCRIPCION, ESTADO) VALUES (?,?);";

        try {
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setString(1,genero.genero);
            Mysql.setBoolean(2,genero.bitEstado);

            Mysql.executeUpdate();

            return "Género agregado";
        }catch (Exception ex){

            return "Hubo un error al tratar de agregar el Género: " +ex;
        }
    }

    public static String InsertarNuevoLibro(libro libro){
        Connection conexion = conectar();
        String query = "INSERT INTO libros (NOMBRE_LIBRO,IDX_GENERO_LIBRO,ESTADO) VALUES (?,?,?);";

        try {
            PreparedStatement Mysql = conexion.prepareStatement(query);
            Mysql.setString(1,libro.libro);
            Mysql.setInt(2,libro.idx_genero);
            Mysql.setBoolean(3,libro.bitEstado);

            Mysql.executeUpdate();

            return "Libro agregado";
        }catch (Exception ex){

            return "Hubo un error al tratar de agregar el Libro: " +ex;
        }
    }

    public static ResultSet cargarLibros() throws SQLException{
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();

        String query = "SELECT *,generos_libros.GENERO_DESCRIPCION FROM libros INNER JOIN generos_libros ON libros.IDX_GENERO_LIBRO = generos_libros.ID_GENERO_LIBRO";

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

    public static ResultSet cargarDatosInicio() throws  SQLException{
        Connection conexion = conectar();
        Statement Mysql = conexion.createStatement();

        String query = "SELECT SUM(CASE WHEN IDX_TIPO_SALIDA = 1 THEN 1 ELSE 0 END) AS `VENTAS`,SUM(CASE WHEN IDX_TIPO_SALIDA = 2 THEN 1 ELSE 0 END) AS `PRESTAMOS`,SUM(CASE WHEN IDX_TIPO_SALIDA = 3 THEN 1 ELSE 0 END) AS `OBSEQUIOS` FROM salidas;";

        return Mysql.executeQuery(query);
    }

}
