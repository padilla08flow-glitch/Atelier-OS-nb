package moduloInterfaz;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Danna Padilla
 */
public class EscritorioConFondo extends JDesktopPane{
    private Image imagen;
    
    public EscritorioConFondo() {

        try {
            imagen = new ImageIcon(getClass().getResource("/iconos/fondo_atelier.png")).getImage();
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
