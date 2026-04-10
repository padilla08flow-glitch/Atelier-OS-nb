package moduloInterfaz;
import com.formdev.flatlaf.FlatLightLaf;

/**
 *
 * @author Danna Padilla
 */
public class CasaVictoriaApp {
    public static void main(String[] args) {
        try {
            // Aplicar el tema moderno
            FlatLightLaf.setup(); 
        } catch( Exception ex ) {
            System.err.println( "Error al iniciar FlatLaf" );
        }

        java.awt.EventQueue.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}
