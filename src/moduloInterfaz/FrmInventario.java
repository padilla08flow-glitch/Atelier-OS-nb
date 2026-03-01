package moduloInterfaz;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Danna Padilla
 */
    
public class FrmInventario extends JInternalFrame{
    //definir elementos
    private JTabbedPane pestañas;
    private PnlDiseno moduloDiseno; // Clase separada para el panel de diseño
    private PnlProducto moduloProducto; // Clase separada para el stock físico
    
    //controlar el estado 
    private int idSeleccionado = -1;

    public FrmInventario() {
        setTitle("Gestión de Inventario y Catálogo - AtelierOS");
        setClosable(true);
        setMaximizable(true);
        setSize(1100, 650);

        iniciarComponentes();
    }

    //METODOS :P
    private void iniciarComponentes() {
        pestañas = new JTabbedPane();

        // Inicializamos los paneles (que ahora son clases independientes)
        moduloDiseno = new PnlDiseno();
        moduloProducto = new PnlProducto();

        // Cada vez que se cambie a la pestaña de productos, refrescamos el ComboBox de diseños
        pestañas.addChangeListener(e -> {
            if (pestañas.getSelectedIndex() == 1) {
                moduloProducto.actualizarListaDisenos();
            }
        });

        add(pestañas, BorderLayout.CENTER);
    }
    
}