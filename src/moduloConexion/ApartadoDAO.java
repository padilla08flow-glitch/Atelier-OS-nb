package moduloConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import moduloLogica.Apartado;

/**
 * gestionar apartados y stock
 * @author Danna Padilla
 */
public class ApartadoDAO {
    
    public boolean registrarApartados (Apartado ap){
        String sqlApartado = "INSERT INTO apartado (id_cliente, id_producto, id_usuario.fecha_inicio, fecha_limite, total_prenda, estado) VALUES (?,?,?,?,?,?,?,?)";
        String sqlStock = "UPDATE producto SET stock = stock - 1 WHERE id_producto = ?";
        
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); //transaccion
            
            try (PreparedStatement psAp = con.prepareStatement(sqlApartado)){
                psAp.setInt(1, ap.getIdCliente());
                psAp.setInt(2, ap.getIdProducto());
                psAp.setInt(3, ap.getIdUsuario());
                psAp.setTimestamp(4, ap.getFechaInicio());
                psAp.setTimestamp(5, ap.getFechaLimite());
                psAp.setDouble(6, ap.getTotalPrenda());
                psAp.setDouble(7, ap.getPagoInicial());
                psAp.setString(8, ap.getEstado());
                psAp.executeUpdate();
            }
            try (PreparedStatement psSt = con.prepareStatement(sqlStock)){
                psSt.setInt(1, ap.getIdProducto());
                psSt.executeUpdate();
            } 
            
            con.commit();
            return true;
            
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error al registrar apartado: " + e.getMessage());
            return false;
        }
    }
    
    //RF - 05 VENCIMIENTO
    public List<Apartado> listarVencidos(){
        List<Apartado> lista = new ArrayList<>();
        String sql = "SELECT * FROM apartado WHERE fecha_limite < CURRENT_TIMESTAMP AND estado = 'Pendiente'";
        
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while (rs.next()) {                
                Apartado ap = new Apartado();
                ap.setIdApartado(rs.getInt("id_apartado"));
                ap.setTotalPrenda(rs.getDouble("total_prenda"));
                ap.setFechaLimite(rs.getTimestamp("fecha_limite"));
                lista.add(ap);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al consultar vencidos: " + e.getMessage());
        }
        return lista;
    }
}
