package moduloInterfaz;

import javax.swing.*;
import java.awt.*;
import moduloConexion.DesignDAO;
import moduloLogica.Design;
/**
 *
 * @author Danna Padilla
 */
public class RegistroDesign extends javax.swing.JDialog{
    
    private JTextField txtNombre, txtMaterial, txtTecnica, txtColor;
    private JButton btnGuardar, btnCancelar;
    private boolean guardadoExitoso = false;
    private DesignDAO dDao = new DesignDAO();
    
    public RegistroDesign(java.awt.Frame parent, boolean modal){
        super(parent, modal);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel pnlCampos = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        txtNombre = new JTextField();
        txtMaterial = new JTextField();
        txtTecnica = new JTextField();
        txtColor = new JTextField();
        
        pnlCampos.add(new JLabel("Nombre Modelo:")); pnlCampos.add(txtNombre);
        pnlCampos.add(new JLabel("Material:"));      pnlCampos.add(txtMaterial);
        pnlCampos.add(new JLabel("Técnica:"));       pnlCampos.add(txtTecnica);
        pnlCampos.add(new JLabel("Color Base:"));    pnlCampos.add(txtColor);

        JPanel pnlBotones = new JPanel();
        btnGuardar = new JButton("Guardar Diseño");
        btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);

        add(pnlCampos, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }
    
    private void guardar() {
        if (txtNombre.getText().isEmpty() || txtMaterial.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Material son obligatorios");
            return;
        }

        Design d = new Design();
        d.setNombreModelo(txtNombre.getText().trim());
        d.setMaterial(txtMaterial.getText().trim());
        d.setTecnica(txtTecnica.getText().trim());
        d.setColor(txtColor.getText().trim());

        if (dDao.insertar(d)) {
            guardadoExitoso = true;
            JOptionPane.showMessageDialog(this, "Modelo registrado correctamente");
            dispose();
        }
    }

    public boolean isGuardadoExitoso() { return guardadoExitoso; }
}
