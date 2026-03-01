package moduloInterfaz;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import moduloConexion.DesignDAO;
import moduloLogica.Design;
import java.util.List;
/**
 *
 * @author Danna Padilla
 */
public class PnlDiseno extends JPanel {
    private JTextField txtNombre, txtMaterial, txtTecnica;
    private JTable tablaDisenos;
    private DefaultTableModel modelo;
    private DesignDAO dDao = new DesignDAO();

    public PnlDiseno() {
        setLayout(new BorderLayout(10, 10));
        iniciarComponentes();
        cargarDatos();
    }

    private void iniciarComponentes() {
        // Formulario de Diseño (Izquierda)
        JPanel pnlForm = new JPanel(new GridLayout(8, 1, 5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Nuevo Modelo"));
        
        txtNombre = new JTextField();
        txtMaterial = new JTextField();
        txtTecnica = new JTextField();
        JButton btnGuardar = new JButton("Guardar Diseño");

        btnGuardar.addActionListener(e -> guardar());

        pnlForm.add(new JLabel("Nombre del Modelo:")); pnlForm.add(txtNombre);
        pnlForm.add(new JLabel("Material:")); pnlForm.add(txtMaterial);
        pnlForm.add(new JLabel("Técnica:")); pnlForm.add(txtTecnica);
        pnlForm.add(new JLabel("")); pnlForm.add(btnGuardar);

        add(pnlForm, BorderLayout.WEST);

        // Tabla de Diseños (Derecha)
        modelo = new DefaultTableModel(new Object[]{"ID", "Modelo", "Material", "Técnica"}, 0);
        tablaDisenos = new JTable(modelo);
        add(new JScrollPane(tablaDisenos), BorderLayout.CENTER);
    }

    private void guardar() {
        Design d = new Design();
        d.setNombreModelo(txtNombre.getText());
        d.setMaterial(txtMaterial.getText());
        d.setTecnica(txtTecnica.getText());

        if (dDao.insertar(d)) {
            JOptionPane.showMessageDialog(this, "Diseño agregado al catálogo.");
            cargarDatos();
            limpiar();
        }
    }
    
    public void cargarDatos() { /* ... código para llenar la tabla ... */ }
    private void limpiar() { /* ... resetear campos ... */ }
}