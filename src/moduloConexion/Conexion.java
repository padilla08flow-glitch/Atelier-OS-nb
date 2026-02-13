package moduloConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Modulo encargado de conectar base de datos con el sistema
 * @author Danna Paola
 */
public class Conexion {
    private static Connection contacto = null;
    
    private static final String user = "root"; //usuario con ALL PRIVILEGES
    private static final String pass = "tu_password";
    private static final String db = "sistema_ventas";
    private static final String url = "jdbc:mysql://localhost:3306/" + db;
    
    public static Connection getConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            contacto = DriverManager.getConnection(url, user, pass);
        }catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,"Error de conexion: " +e.getMessage());
        }
        return contacto;
    }
}
