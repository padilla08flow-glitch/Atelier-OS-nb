package moduloConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import moduloLogica.Cliente;

/**
 * Gestion de Cliente
 * @author Danna Padilla
 */
public class ClienteDAO {
    
    private Connection con;
    private final Conexion cn = new Conexion();
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Cliente> buscar(String criterio) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nombre_completo LIKE ? OR telefono LIKE ?";
        
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + criterio + "%");
            ps.setString(2, "%" + criterio + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                c.setNombreCompleto(rs.getString("nombre_completo"));
                c.setTelefono(rs.getString("telefono"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error en buscar cliente: " + e.getMessage());
        } finally {
            cn.cerrar(con);
        }
        return lista;
    }

    //Registrar un nuevo cliente
    public int registrar(Cliente c) {
        String sql = "INSERT INTO cliente (nombre_completo, telefono) VALUES (?, ?)";
        int idGenerado = -1;
        
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getNombreCompleto());
            ps.setString(2, c.getTelefono());
            
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
        } finally {
            cn.cerrar(con);
        }
        return idGenerado;
    }
}
