package moduloConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Modulo encargado de conectar base de datos con el sistema
 * @author Danna Padilla
 */
public class Conexion {
    private static Connection contacto = null;
    
    private static final String user = "vcv_user"; 
    private static final String pass = "Vcv_2026_Access";
    private static final String db = "sistema_ventas";
    private static final String url = "jdbc:mysql://localhost:3306/" + db + 
            "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    public static Connection getConexion(){
        try{
            if (contacto == null || contacto.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                contacto = DriverManager.getConnection(url, user, pass);
                System.out.println("Conexión exitosa a AtelierOS");
            }
        }catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error crítico de conexión: " + e.getMessage(), 
                                          "AtelierOS - Error", JOptionPane.ERROR_MESSAGE);
            contacto = null;
        }
        return contacto;
    }
}
