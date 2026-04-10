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
            
            String sqlVenta = "INSERT INTO venta (id_cliente, nombre_cliente, total_venta, fecha_venta) VALUES (?,?,?,?, NOW())";
            
            PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, v.getIdCliente()); //id por defecto --> publico general
            psVenta.setString(2, v.getNombreCliente());
            psVenta.setDouble(3, v.getTotal());
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
            if (e.getMessage().contains("foreign key constraint fails")) {
                System.err.println("ERROR: El ID del Cliente o Usuario no existe en la base de datos.");
            } else {
                System.err.println("Error de Transacción: " + e.getMessage());
            }
            return false;
        }
        
    }
        
    public boolean finalizarVenta(Venta venta, List<VentaDetalle> detalles) {
        String sqlVenta = "INSERT INTO venta (id_cliente, id_usuario, total_venta, nombre_cliente_ticket, fecha_venta) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";
        
            try (Connection con = Conexion.getConexion()){
                con.setAutoCommit(false); //transaccion segura
                
                int idVentaGenerado = -1;
                
                try (PreparedStatement psV = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)){
                    psV.setInt(1, venta.getIdCliente());
                    psV.setInt(2, venta.getIdUsuario());
                    psV.setDouble(3, venta.getTotal());
                    //para el ticket
                    psV.setString(4, venta.getNombreCliente());
                    psV.executeUpdate();
                    
                    ResultSet rs = psV.getGeneratedKeys();
                    if (rs.next()) idVentaGenerado = rs.getInt(1);   
                }
                
                if (idVentaGenerado == -1) throw new SQLException("No se pudo obtener el ID de la venta.");
                
                //valida y actualiza el stock
                for (VentaDetalle d : detalles) {
                    try (PreparedStatement psS = con.prepareStatement(sqlStock)){
                        psS.setInt(1, d.getCantidad());
                        psS.setInt(2, d.getIdProducto());
                        psS.setInt(3, d.getCantidad());
                        
                        int filasAfectadas = psS.executeUpdate();
                        if (filasAfectadas == 0) {
                            throw new SQLException("Stock insuficiente del producto: "+d.getIdProducto());
                        }
                    } 
                    //registra detalle
                    try (PreparedStatement psD = con.prepareStatement(sqlDetalle)) {
                        psD.setInt(1, idVentaGenerado);
                        psD.setInt(2, d.getIdProducto());
                        psD.setInt(3, d.getCantidad());
                        psD.setDouble(4, d.getPrecioUnitario());
                        psD.executeUpdate();
                    }
                }
                con.commit(); //guardar cambios
                return true;
                  
            } catch (SQLException e) {
                System.err.println("Error de Transacción: " + e.getMessage());
                return false;
            }
        }
    
    
    
}