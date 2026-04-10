package moduloConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import moduloLogica.AbonoApartado;

/**
 *
 * @author Danna Padilla
 */
public class AbonoDAO {
    
    public boolean registrarAbonoYLiquidacion(int idApartado, double newMonto){
        String sqlInsert = "INSERT INTO abono_apartado (id_apartado, monto_abono, fecha_abono) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE apartado SET estado = 'Liquidado' WHERE id_apartado = ?";
        String sqlCheck = "SELECT precio_apartado, " +
                "(SELECT IFNULL(SUM(monto_abono), 0) FROM abono_apartado WHERE id_apartado = ?) as total_abonado " +
                "FROM apartado WHERE id_apartado = ?";
        
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            //insertar el abono
            try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                psIns.setInt(1, idApartado);
                psIns.setDouble(2, newMonto);
                psIns.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                psIns.executeUpdate(); 
            } 
            double precioTotal = 0;
            double yaAbonado = 0;
            
            //calcular si cumple el total
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)){
                psCheck.setInt(1, idApartado);
                psCheck.setInt(2, idApartado);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        precioTotal = rs.getDouble("precio_apartado");
                    yaAbonado = rs.getDouble("total_abonado");
                    }
                }
            }
            
            //Actualizar estado si el pago esta completo
            if (yaAbonado >= precioTotal - 0.01) {
                try (PreparedStatement psUp = con.prepareStatement(sqlUpdate)){
                    psUp.setInt(1, idApartado);
                    psUp.executeUpdate();
                }
            }
            
            con.commit();
            return true;
            
       } catch (SQLException e) {
           if (con != null) try { con.rollback(); } catch (SQLException ex) {ex.printStackTrace();}
            System.err.println("Error al procesar abono: " + e.getMessage());
            return false;
       } 
    }
    
    public List<AbonoApartado> listarAbonosPorApartado(int idApartado) {
        List<AbonoApartado> lista = new ArrayList<>();
        String sql = "SELECT * FROM abono_apartado WHERE id_apartado = ? ORDER BY fecha_abono DESC";
        
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idApartado);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AbonoApartado ab = new AbonoApartado();
                    ab.setIdAbono(rs.getInt("id_abono"));
                    ab.setIdApartado(rs.getInt("id_apartado"));
                    ab.setMontoAbono(rs.getDouble("monto_abono"));
                    ab.setFechaAbono(rs.getTimestamp("fecha_abono"));
                    lista.add(ab);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar abonos: " + e.getMessage());
        }
        return lista;
    }
    
    
}
