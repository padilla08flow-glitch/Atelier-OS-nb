package GestorProcesos;
import java.util.List;
import moduloLogica.Producto;
import moduloLogica.VentaDetalle;
import javax.swing.table.DefaultTableModel;

/**
 * Valida y agrega (o actualiza) un producto en la lista y en la tabla visual
 * 
 * @author Danna Padilla
 */
public class GestorCarrito {
    
    public double agregarOActualizar(Producto p, int cantidad, List<VentaDetalle> carrito, DefaultTableModel modelo) {
        int idActual = p.getId();
        double precio = p.getPrecio();
        double subtotalAgregado = precio * cantidad;
        
        String descripcionFinal = p.getNombre() + " [" + p.getMaterial() + " - " + p.getTalla() + "]";
        
        boolean existeEnLista = false;
        
        for (int i = 0; i < carrito.size(); i++) {
            VentaDetalle detalle = carrito.get(i);
            //esto falla...
            if (detalle.getIdProducto() == idActual) {
                //actualzar logica
                int nuevaCant = detalle.getCantidad() + cantidad;
                detalle.setCantidad(nuevaCant);
                
                //Actualizar la tabla visual
                double nuevoSubtotalFila = nuevaCant * precio;
                modelo.setValueAt(nuevaCant, i, 3);
                modelo.setValueAt(nuevoSubtotalFila, i, 5);
                
                existeEnLista = true;
                break;
            }
        }
        //SI ES NUEVO --> AGREGAR A FILA
        if (!existeEnLista) {
            VentaDetalle nuevoDetalle = new VentaDetalle();
            nuevoDetalle.setIdProducto(idActual);
            nuevoDetalle.setCantidad(cantidad);
            nuevoDetalle.setPrecioUnitario(precio);
            carrito.add(nuevoDetalle);

            String descripcionDetallada = p.getNombre() + " [" + p.getMaterial() + " - " + p.getTalla() + "]";
            
            modelo.addRow(new Object[]{
                p.getId(),              
                p.getSku(),             
                descripcionDetallada,   
                Integer.valueOf(cantidad),
                Double.valueOf(precio),    // 4
                Double.valueOf(subtotalAgregado)
            });
        }
        return subtotalAgregado;
    }
    
    public int calcularCantidadEnCarrito(int idProducto, List<VentaDetalle> carrito) {
        return carrito.stream()
                .filter(d -> d.getIdProducto() == idProducto)
                .mapToInt(VentaDetalle::getCantidad)
                .sum();
    }
    
    public double eliminarProducto(int fila, List<VentaDetalle> carrito, DefaultTableModel modelo) {
        if (fila < 0 || fila >= carrito.size()) return 0;

        // 1. Obtener el subtotal de la fila antes de borrarla para saber cuánto restar
        double subtotalARestar = (double) modelo.getValueAt(fila, 5);

        // 2. Eliminar de la lista 
        carrito.remove(fila);

        // 3. Eliminar de la tabla visual
        modelo.removeRow(fila);

        // actualizar el total general
        return subtotalARestar;
    }
    
    

}
