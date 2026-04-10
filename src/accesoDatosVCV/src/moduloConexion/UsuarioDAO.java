package moduloConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Consulta en base de datos para login seguro
 * @author Danna Padilla
 */
public class UsuarioDAO {
    
    public String validarIngreso(String user, String pass){
        String rol = null;
        String sql = "SELECT rol FROM usuario WHERE user_name = ? AND password = ? AND activo = 1";
        
        try (Connection con = Conexion.getConexion();
                PreparedStatement pst = con.prepareStatement(sql)){
            
            pst.setString(1, user);
            pst.setString(2, pass);
            
            try (ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    rol = rs.getString("rol");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de Login: "+e.getMessage());
        }
        return rol;
    }
}
