package moduloValidaciones;
import javax.swing.JOptionPane;
import java.awt.Component;
/**
 *
 * @author Danna Padilla
 */
public class ValidadorApartado {
    
    public static boolean esAbonoValido(String abonoTexto, double precioTotal, Component padre){
        try{
            double abono = Double.parseDouble(abonoTexto);
            if (abono <= 0) {
                JOptionPane.showMessageDialog(padre, "El abono no puede ser cero.");
                return false;
            }
            
            if(abono > precioTotal){
                JOptionPane.showMessageDialog(padre, "Error: El abono ($" + abono + ") supera el precio total ($" + precioTotal + ").");
                return false;
            }
            return true;
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(padre, "Ingrese un monto válido para el abono.");
            return false;
        }
        
    }
    
    public static boolean validarCamposCompletos(Object producto, String cliente, Component padre){
        if (producto == null) {
            JOptionPane.showMessageDialog(padre, "Debe seleccionar un producto antes de continuar.");
            return false;
        }
        if (cliente == null || cliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El nombre del cliente es obligatorio.");
            return false;
        }
        return true;
    }
}
