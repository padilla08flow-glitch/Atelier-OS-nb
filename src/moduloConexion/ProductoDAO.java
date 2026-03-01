package moduloConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import moduloLogica.Producto; //Referencia --> Proyecto - logicaVCV

/**
 * metodo listar / buscar devuelven la lista Producto
 * Se debe llamar en vistaVCV
 * @author Danna Padilla
 */
public class ProductoDAO {
    //CRUD
    //METODO-INSERTAR (CREATE)
    public boolean insertar(Producto prodt){
        String sql = "INSERT INTO producto (id_design, codigo_sku, talla, genero, precio_venta, stock) VALUES (?,?,?,?,?,?)";
        
        try (Connection con = Conexion.getConexion(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, prodt.getIdDesign());
            pst.setString(2, prodt.getSku());
            pst.setString(3, prodt.getTalla());
            pst.setString(4, prodt.getGenero());
            pst.setDouble(5, prodt.getPrecio());
            pst.setInt(6, prodt.getStock());
            
            return pst.executeUpdate() > 0;
        }catch (SQLException e){
            System.err.println("Error al ingresar Producto" + e.getMessage());
            return false;
        } 
    }
    
    //METODO-LEER (READ)
    public List<Producto> listar(){
        
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.codigo_sku, d.nombre_modelo, p.talla, p.genero, p.precio_venta, p.stock " +
                     "FROM producto p " +
                     "INNER JOIN design d ON p.id_design = d.id_design";
        
        try (Connection con = Conexion.getConexion();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id_producto"));
                p.setSku(rs.getString("codigo_sku"));
                p.setNombre(rs.getString("nombre_modelo")); //se cambio el set
                p.setTalla(rs.getString("talla"));
                
                p.setGenero(rs.getString("genero"));
                p.setPrecio(rs.getDouble("precio_venta"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos - " + e.getMessage());
        }
        return lista;
    }
        
    //METODO-BUSQUEDA (NOMBRE / SKU)
    public List<Producto> buscar(String criterio) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, d.nombre_modelo, d.material, d.tecnica, d.color " +
                 "FROM producto p " +
                 "INNER JOIN design d ON p.id_design = d.id_design " +
                 "WHERE p.codigo_sku LIKE ? OR d.nombre_modelo LIKE ? " +
                 "OR d.material LIKE ? OR d.tecnica LIKE ? OR d.color LIKE ?";
        
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            String busqueda = "%" + criterio + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, busqueda);
            
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id_producto"));
                    p.setSku(rs.getString("codigo_sku"));
                    p.setTalla(rs.getString("talla"));
                    p.setPrecio(rs.getDouble("precio_venta"));
                    p.setStock(rs.getInt("stock"));
                    //ATRIBUTOS DE DISEÑO
                    p.setNombre(rs.getString("nombre_modelo"));
                    p.setMaterial(rs.getString("material"));
                    p.setTecnica(rs.getString("tecnica"));
                    p.setColor(rs.getString("color"));
                    lista.add(p);
                }
            }
        } catch(SQLException e) {
            System.err.println("Error en la búsqueda: " + e.getMessage());
        }
        return lista;
    }
    
    //METODO-ACTUALIZAR (UPDATE)
    public boolean actualizar(Producto prodt) {
        String sql = "UPDATE producto SET id_design=?, codigo_sku=?, talla=?, genero=?, precio_venta=?, stock=? WHERE id_producto=?";
        
        try (Connection con = Conexion.getConexion(); 
                PreparedStatement pst = con.prepareStatement(sql)){
            
            pst.setInt(1, prodt.getIdDesign());
            pst.setString(2, prodt.getSku());
            pst.setString(3, prodt.getTalla());
            pst.setString(4, prodt.getGenero());
            pst.setDouble(5, prodt.getPrecio());
            pst.setInt(6, prodt.getStock());
            pst.setInt(7, prodt.getId());
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e){
            
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
    //METODO-ELIMINAR (DELETE)
    public boolean eliminar(int id){
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        
        try (Connection con = Conexion.getConexion(); 
         PreparedStatement pst = con.prepareStatement(sql)){
            
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}   

