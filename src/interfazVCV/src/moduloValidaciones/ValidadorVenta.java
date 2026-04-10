package moduloValidaciones;
import javax.swing.JOptionPane;
import java.awt.Component;

/**
 *
 * @author Danna Padilla
 */
public class ValidadorVenta {
    public static boolean validar(double total, String pagoTexto,Component padre){
        
        //monto vacio
        if (pagoTexto == null || pagoTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(padre, "Ingresa el monto del pago.");
            return false;
        }
        
        try {
            double pago = Double.parseDouble(pagoTexto);
            if (pago < total) {
                JOptionPane.showMessageDialog(padre,"Pago insuficioente. Faltan: $" + (total - pago));
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(padre, "Ingresa un monto válido.");
            return false;
        }
        return true;
    }
}
