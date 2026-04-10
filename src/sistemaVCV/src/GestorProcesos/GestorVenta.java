package GestorProcesos;

import java.util.List;
import javax.swing.JOptionPane;
import java.awt.Component;
import moduloConexion.VentaDAO;
import moduloLogica.Venta;
import moduloLogica.VentaDetalle;
import moduloValidaciones.ValidadorVenta;
/**
 *
 * @author Danna Padilla
 */
public class GestorVenta {
    
    private VentaDAO ventaDao = new VentaDAO();
    
    public boolean procesarCobro (double total, String pagoTexto, List<VentaDetalle> carrito, 
                                 int idCliente, int idUsuario, Component padre) {
        //verificacion
        if (!ValidadorVenta.validar(total, pagoTexto, padre)) {
            return false; // va el mensaje de error
        }
        
        try {
            double pago = Double.parseDouble(pagoTexto);
            double cambio = pago - total;

            //confirmacion del usuario
            int confirmar = JOptionPane.showConfirmDialog(padre,
                "Resumen de Venta:\n" +
                "Total a Pagar: $" + total + "\n" +
                "Efectivo Recibido: $" + pago + "\n" +
                "Cambio: $" + cambio + "\n\n" +
                "¿Desea finalizar la transacción?", 
                "Confirmar Cobro", JOptionPane.YES_NO_OPTION);
            
            if (confirmar != JOptionPane.YES_OPTION) return false;
            //objetos de la db
            Venta nuevaVenta = new Venta();
            nuevaVenta.setIdCliente(idCliente);
            nuevaVenta.setIdUsuario(idUsuario);
            nuevaVenta.setTotal(total);
        
            //ejecutar transaccion 
            if (ventaDao.finalizarVenta(nuevaVenta, carrito)) {
                String textoTicket = generarTicket(nuevaVenta, carrito, pago, cambio);
                //formato
                javax.swing.JTextArea areaTicket = new javax.swing.JTextArea(textoTicket);
                areaTicket.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
                areaTicket.setEditable(false);
                //cuadro de dialogo
                JOptionPane.showMessageDialog(padre, new javax.swing.JScrollPane(areaTicket), 
                        "Ticket de Venta - AtelierOS", JOptionPane.INFORMATION_MESSAGE);
                return true;
                //sdsdklnasskldcdsñafnkawe
            } else {
                JOptionPane.showMessageDialog(padre, "Error al registrar la venta en DB.", "Error SQL", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(padre, "Por favor, ingrese un monto de pago válido (solo números).", 
                                  "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public String generarTicket(Venta venta, List<VentaDetalle> detalles, double pago, double cambio) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("========== ATELIER OS ==========\n");
        ticket.append("Fecha: ").append(new java.util.Date()).append("\n");
        ticket.append("Cliente ID: ").append(venta.getIdCliente()).append("\n");
        ticket.append("--------------------------------\n");
        ticket.append(String.format("%-15s %-5s %-10s\n", "Producto", "Cant", "Subtotal"));

        for (VentaDetalle d : detalles) {
            // Asumiendo que VentaDetalle tiene un método para obtener el nombre o SKU
            ticket.append(String.format("%-15s %-5d $%-10.2f\n", 
                "ID:" + d.getIdProducto(), 
                d.getCantidad(), 
                d.getCantidad() * d.getPrecioUnitario()));
        }

        ticket.append("--------------------------------\n");
        ticket.append(String.format("TOTAL:          $%.2f\n", venta.getTotal()));
        ticket.append(String.format("EFECTIVO:       $%.2f\n", pago));
        ticket.append(String.format("CAMBIO:         $%.2f\n", cambio));
        ticket.append("================================\n");
        ticket.append("   ¡Gracias por su compra!   \n");

        return ticket.toString();
    }
}
