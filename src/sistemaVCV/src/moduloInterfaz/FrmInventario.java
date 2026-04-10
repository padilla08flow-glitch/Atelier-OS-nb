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
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(1100, 650);

        iniciarComponentes();
        try {
            this.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    //METODOS :P
    private void iniciarComponentes() {
        setLayout(new BorderLayout());
        pestañas = new JTabbedPane();

        // Inicializamos los paneles 
        moduloDiseno = new PnlDiseno();
        moduloProducto = new PnlProducto();
        
        //ya para mostrsr en pantalla
        pestañas.add("Catálogo de Diseños", moduloDiseno);
        pestañas.addTab("Stock Físico / Productos",moduloProducto);

        pestañas.addChangeListener(e -> {
            if (pestañas.getSelectedIndex() == 1) {
                moduloProducto.actualizarListaDisenos();
            }
        });

        add(pestañas, BorderLayout.CENTER);
    }
    
}