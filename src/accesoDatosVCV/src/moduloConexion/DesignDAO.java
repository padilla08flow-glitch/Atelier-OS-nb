package moduloConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import moduloLogica.Design;


/**
 *
 * @author Danna Padilla
 */
public class DesignDAO {
    
    
    public List<Design> listarTodos() {
        List<Design> lista = new ArrayList<>();
        String sql = "SELECT * FROM design ORDER BY nombre_modelo ASC";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            
            while (rs.next()) {                
                Design d = new Design();
                
                d.setIdDesign(rs.getInt("id_design"));
                d.setNombreModelo(rs.getString("nombre_modelo"));
                d.setMaterial(rs.getString("material"));
                d.setTecnica(rs.getString("tecnica"));
                d.setColor(rs.getString("color"));
                d.setRutaImagen(rs.getString("ruta_imagen"));
                
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar diseños: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean insertar(Design d){
        String sql = "INSERT INTO design (nombre_modelo, material, tecnica, color, ruta_imagen) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, d.getNombreModelo());
            ps.setString(2, d.getMaterial());
            ps.setString(3, d.getTecnica());
            ps.setString(4, d.getColor());
            ps.setString(5,d.getRutaImagen()); //ruta para la imagen
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar diseño: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar (Design d){
        String sql = "UPDATE design SET nombre_modelo = ?, material = ?, tecnica = ?, color = ?, ruta_imagen = ? WHERE id_design = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, d.getNombreModelo());
            ps.setString(2, d.getMaterial());
            ps.setString(3, d.getTecnica());
            ps.setString(4, d.getColor());
            ps.setString(5, d.getRutaImagen());
            ps.setInt(6, d.getIdDesign());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            System.err.println("Error al actualizar diseño: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM design WHERE id_design = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}
