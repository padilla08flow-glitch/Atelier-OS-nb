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
        // Cambia tu String sqlApartado por este:
        String sqlApartado = "INSERT INTO apartado (id_cliente, id_producto, id_usuario, fecha_inicio, fecha_limite, precio_apartado, estado) VALUES (?,?,?,?,?,?,?)";        
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
                psAp.setString(7, ap.getEstado());
                psAp.executeUpdate();
            }
            try (PreparedStatement psSt = con.prepareStatement(sqlStock)){
                psSt.setInt(1, ap.getIdProducto());
                if (psSt.executeUpdate() ==0) throw new SQLException("No hay stock disponible.");
            } 
            
            con.commit();
            return true;
            
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {ex.printStackTrace();}
            }
            System.err.println("Error al registrar apartado: " + e.getMessage());
            return false;
        }
    }
    
    public List<Object[]> consultarResumen(){
        List<Object[]> lista = new ArrayList<>();
        
        String sql = "SELECT a.id_apartado, c.nombre_completo, c.telefono, d.nombre_modelo, " +
                "a.precio_apartado, a.estado, " +
                "(SELECT IFNULL(SUM(monto_abono), 0) FROM abono_apartado WHERE id_apartado = a.id_apartado) as total_abonado " +
                "FROM apartado a " +
                "JOIN cliente c ON a.id_cliente = c.id_cliente " +
                "JOIN producto p ON a.id_producto = p.id_producto " +
                "JOIN design d ON p.id_design = d.id_design";
        
        try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
            
            while (rs.next()) {                
                double total = rs.getDouble("precio_apartado");
                double abonado = rs.getDouble("total_abonado");
                double saldo = total - abonado;
                
                lista.add(new Object[]{
                    rs.getInt("id_apartado"),
                    rs.getString("nombre_completo"),
                    rs.getString("telefono"),
                    rs.getString("nombre_modelo"),
                    total,
                    abonado,
                    saldo,
                    rs.getString("estado")
                });
            }
            
        } catch (SQLException e) {
            System.err.println("Error en resumen: " + e.getMessage());
        }
        return lista;
    }
    
    public int registrarApartadoCompleto(Apartado ap) {
        String sqlAp = "INSERT INTO apartado (id_cliente, id_producto, id_usuario, fecha_inicio, fecha_limite, precio_apartado, estado) VALUES (?,?,?,?,?,?,?)";
        String sqlStock = "UPDATE producto SET stock = stock - 1 WHERE id_producto = ?";
        String sqlAbono = "INSERT INTO abono_apartado (id_apartado, monto_abono, fecha_abono) VALUES (?,?,?)";
        
        Connection con = null;
        
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);
            
            //insertar apartado
            int idGenerado = -1;
            try (PreparedStatement psAp = con.prepareStatement(sqlAp, Statement.RETURN_GENERATED_KEYS)){
                psAp.setInt(1, ap.getIdCliente());
                psAp.setInt(2, ap.getIdProducto());
                psAp.setInt(3, ap.getIdUsuario());
                psAp.setTimestamp(4, ap.getFechaInicio());
                psAp.setTimestamp(5, ap.getFechaLimite());
                psAp.setDouble(6, ap.getTotalPrenda());
                psAp.setString(7, ap.getEstado());
                psAp.executeUpdate();
                
                ResultSet rs = psAp.getGeneratedKeys();
                if(rs.next()) idGenerado = rs.getInt(1);
            }
            
            //descontar stock
            try (PreparedStatement psSt = con.prepareStatement(sqlStock)){
                psSt.setInt(1, ap.getIdProducto());
                psSt.executeUpdate();
            }
            
            //Registrar un abono Inicial
            try (PreparedStatement psAb = con.prepareStatement(sqlAbono)){
                psAb.setInt(1, idGenerado);
                psAb.setDouble(2, ap.getPagoInicial());
                psAb.setTimestamp(3, ap.getFechaInicio());
                psAb.executeUpdate();
            }
            con.commit();
            return idGenerado;
            
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error en transacción: " + e.getMessage());
            return -1;
        }
    }
    
    public List<Object[]> consultarResumenConFiltro(String filtro) {
        List<Object[]> lista = new ArrayList<>();
        //filtrar por nombre del cliente
        String sql = "SELECT a.id_apartado, c.nombre_completo, c.telefono, d.nombre_modelo, " +
                    "a.precio_apartado, a.estado, " +
                    "(SELECT IFNULL(SUM(monto_abono), 0) FROM abono_apartado WHERE id_apartado = a.id_apartado) as total_abonado " +
                    "FROM apartado a " +
                    "JOIN cliente c ON a.id_cliente = c.id_cliente " +
                    "JOIN producto p ON a.id_producto = p.id_producto " +
                    "JOIN design d ON p.id_design = d.id_design " +
                    "WHERE c.nombre_completo LIKE ? OR c.telefono LIKE ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%"); // busqueda completa
            ps.setString(2, "%" + filtro + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double total = rs.getDouble("precio_apartado");
                double abonado = rs.getDouble("total_abonado");
                double saldo = total - abonado;

                lista.add(new Object[]{
                    rs.getInt("id_apartado"),
                    rs.getString("nombre_completo"),
                    rs.getString("telefono"),
                    rs.getString("nombre_modelo"),
                    total,
                    abonado,
                    saldo,
                    rs.getString("estado")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda filtrada: " + e.getMessage());
        }
        return lista;
    }
}

