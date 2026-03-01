package moduloConexion;
import java.sql.*;
import java.util.List;
import moduloLogica.Venta;
import moduloLogica.VentaDetalle;

/**
 *
 * @author Danna Padilla
 */
public class VentaDAO {
    
        public boolean registrarVentaCompleta(Venta v, List<VentaDetalle> detalles) {
        Connection con = null;
        
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); //INICIAR TRANSACCION
            
            String sqlVenta = "INSERT INTO venta (id_cliente, nombre_cliente, total_venta, metodo_pago, fecha_venta) VALUES (?,?,?,?, NOW())";
            
            PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, v.getIdCliente()); //id por defecto --> publico general
            psVenta.setString(2, v.getNombreCliente());
            psVenta.setDouble(3, v.getTotal());
            psVenta.setString(4, v.getMetodoPago());
            psVenta.executeUpdate();

            //ID DE LA VENTA CREADA - obtener ID generado para los detalles
            ResultSet rs = psVenta.getGeneratedKeys();
            int idVenta = rs.next() ? rs.getInt(1) : 0;
            
            //REGISTRAR Y ACTUALIZAR STOCK
            String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario) VALUES (?,?,?,?)";
            String sqlStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
        
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            PreparedStatement psStock = con.prepareStatement(sqlStock);
            
            for (VentaDetalle d : detalles) {
                
                //REGISTRO DE DETALLES
                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, d.getIdProducto());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getPrecioUnitario());
                psDetalle.addBatch();
                //ACTUALIZAR STOCK
                psStock.setInt(1, d.getCantidad());
                psStock.setInt(2, d.getIdProducto());
                psStock.addBatch();
            }
            
            psDetalle.executeBatch();
            psStock.executeBatch();
            
            con.commit(); //SE EJECUTO CORRECTAMENTE
            return true;
            
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error en transacción de venta: " + e.getMessage());
            return false;
        }
    }
    
}