package moduloLogica;

/**
 *
 * @author Danna Padilla
 */
public class VentaDetalle {
    
    private int idDetalle;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    
    public VentaDetalle() {}
    
    public VentaDetalle(int idProducto, int cantidad, double precioUnitario){
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    public int getIdDetalle() {return idDetalle;}
    public int getIdVenta() {return idVenta;}
    public int getIdProducto() {return idProducto;}
    public int getCantidad (){return cantidad;}
    public double getPrecioUnitario() { return precioUnitario; }
    
    public void setIdDetalle(int idDetalle){this.idDetalle = idDetalle;}
    public void setIdVenta(int idVenta){this.idVenta = idVenta;}
    public void setIdProducto(int idProducto){this.idProducto = idProducto;}
    public void setCantidad(int cantidad){this.cantidad = cantidad;}
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    
}
