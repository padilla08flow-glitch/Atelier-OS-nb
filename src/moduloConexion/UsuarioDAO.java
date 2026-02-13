package moduloConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Consulta en base de datos para login seguro
 * @author Danna Paola
 */
public class UsuarioDAO {
    
    public String validarIngreso(String usuario, String clave){
        String sql = "SELECT rol FROM usuario WHERE username = ? AND password = ? AND activo = 1";
        String rolEncontrado = null;
        
        try (Connection con = Conexion.getConexion();
                PreparedStatement pst = con.prepareStatement(sql)){
            
            pst.setString(1, usuario);
            pst.setString(2, clave);
            
            try (ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    rolEncontrado = rs.getString("rol");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de Login: "+e.getMessage());
        }
        return rolEncontrado;
    }
}
