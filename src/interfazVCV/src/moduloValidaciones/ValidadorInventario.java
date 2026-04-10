package moduloValidaciones;
import javax.swing.JOptionPane;
import java.awt.Component;
import moduloConexion.ProductoDAO;

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
    
    //
    public static boolean validarSkuDuplicado(String sku, ProductoDAO dao, Component padre) {
        if (dao.existeSKU(sku)) {
            JOptionPane.showMessageDialog(padre,
                    "Error: El SKU '" + sku + "' ya existe en el inventario.\n" +
            "Por favor, verifique la talla o el diseño seleccionado.", 
            "Conflicto de SKU", JOptionPane.ERROR_MESSAGE);
        return false;
        }
        return true;
    }
    
}
