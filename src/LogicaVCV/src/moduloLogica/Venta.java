package moduloLogica;

import java.sql.Timestamp;

/**
 *
 * @author Danna Paola
 */
public class Venta {
    private int idUsuario;
    private int idVenta;
    private int idCliente;
    private String nombreCliente;
    private double total;
    private String metodoPago;
    private Timestamp fecha;
    
    public Venta() {}
    
    //GETTERS Y SETTERS
    
    public int getIdVenta() { return idVenta; }
    public int getIdCliente() { return idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public double getTotal() { return total; }
    public String getMetodoPago() { return metodoPago; }
    public Timestamp getFecha() { return fecha; }
    
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setTotal(double total) { this.total = total; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
    
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}
