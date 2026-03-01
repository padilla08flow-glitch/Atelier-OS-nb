package moduloValidaciones;
import javax.swing.JOptionPane;
import java.awt.Component;
/**
 *
 * @author Danna Padilla
 */
public class ValidadorDiseño {
    public static boolean validarNuevoDiseno(String nombre, String material, Component padre) {
        if (nombre.trim().isEmpty() || material.trim().isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El nombre del modelo y el material son obligatorios para el catálogo.");
            return false;
        }
        // Aquí podrías agregar validaciones de longitud de texto o caracteres prohibidos
        return true;
    }
}
