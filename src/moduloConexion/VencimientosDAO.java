package moduloConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import moduloLogica.Apartado;

/**
 *
 * @author Danna Padilla
 */
public class VencimientosDAO {
    
    //RF - 05 VENCIMIENTO
    public List<Apartado> consultarApartadosVencidos(){
        List<Apartado> vencidos = new ArrayList<>();
        //COMPARA LA FECHA LIMITE DE APARTADO CON EL TIEMPO
        String sql = "SELECT a.id_apartado, c.nombre_completo, c.telefono, p.codigo_sku, " +
                 "a.fecha_limite, a.precio_apartado, a.id_producto, " +
                 "(SELECT IFNULL(SUM(monto_abono), 0) FROM abono_apartado WHERE id_apartado = a.id_apartado) as total_abonado " +
                 "FROM apartado a " +
                 "JOIN cliente c ON a.id_cliente = c.id_cliente " +
                 "JOIN producto p ON a.id_producto = p.id_producto " +
                 "WHERE a.fecha_limite < NOW() AND a.estado = 'Pendiente'";
        
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery())    {
            
            while (rs.next()){
                Apartado ap = new Apartado();
                ap.setIdApartado(rs.getInt("id_apartado"));
                ap.setNombreCliente(rs.getString("nombre_completo"));
                ap.setTelefono(rs.getString("telefono"));
                ap.setSku(rs.getString("codigo_sku"));
                ap.setFechaLimite(rs.getTimestamp("fecha_limite"));
                ap.setTotalPrenda(rs.getDouble("precio_apartado"));
                ap.setIdProducto(rs.getInt("id_producto")); 
                
                //saldo --> precio - abonos
                double precioTotal = rs.getDouble("precio_apartado");
                double yaAbonado = rs.getDouble("total_abonado");
                ap.setTotalPrenda(precioTotal - yaAbonado);
                vencidos.add(ap);
            }
        } catch (SQLException e) {
            System.err.println("Error en Monitor de Vencidos" +e.getMessage());
        }
        return vencidos;
    }
    
    public boolean liberarProductoVencido(int idApartado, int idProducto){
        String sqlApartado = "UPDATE apartado SET estado = 'Vencido' WHERE id_apartado = ?";
        String sqlStock = "UPDATE producto SET stock = stock + 1 WHERE id_producto = ?";
        
        Connection con = null;
        try { 
            con = Conexion.getConexion();
            con.setAutoCommit(false);
            
            try (PreparedStatement psA = con.prepareStatement(sqlApartado)) {
                psA.setInt(1, idApartado);
                if (psA.executeUpdate() == 0) throw new SQLException("Apartado no encontrado.");
            }
            
            try (PreparedStatement psS = con.prepareStatement(sqlStock)) {
                psS.setInt(1, idProducto);
                if (psS.executeUpdate() == 0) throw new SQLException("Producto no encontrado.");
            }
            con.commit();
            return true;
            
        }catch (SQLException e){
            if (con != null) try { con.rollback(); }catch (Exception ex){}
            System.err.println("Error al liberar: " + e.getMessage());
            return false;
        }
    }
    
    
}