package moduloValidaciones;
import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * reglas del inventario 
 * precio / stock
 * @author Danna Padilla
 */
public class ValidadorInventario {
    //verificar que los datos cumplan con los criterios
    
    public static boolean validarProductoFisico (String sku, String talla, String precio, String stock, Component padre) {
        if (sku.isEmpty() || talla.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "SKU y Talla son obligatorios.");
            return false;
        }
        try {
            if (Double.parseDouble(precio) <= 0) throw new Exception();
            if (Integer.parseInt(stock) < 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(padre, "Precio y Stock deben ser valores numéricos válidos.");
            return false;
        }
        return true;
    }       
}
