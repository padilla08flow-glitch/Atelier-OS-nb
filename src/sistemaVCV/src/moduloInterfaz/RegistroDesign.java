package moduloInterfaz;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import moduloConexion.DesignDAO;
import moduloLogica.Design;

public class RegistroDesign extends javax.swing.JDialog {

    private JTextField txtNombre, txtMaterial, txtTecnica, txtColor;
    private JLabel lblPreview;
    private JButton btnGuardar, btnCancelar;
    private DesignDAO dDao = new DesignDAO();
    private Design designEdition = null;
    private String rutaImagenSeleccionada = "default.png";
    private boolean guardadoExitoso = false;

    public RegistroDesign(java.awt.Frame parent, boolean modal) {
        this(parent, modal, null);
    }

    public RegistroDesign(java.awt.Frame parent, boolean modal, Design d) {
        super(parent, modal);
        this.designEdition = d;

        setLayout(new BorderLayout(10, 10));
        setSize(550, 350); // Aumenté un poco el ancho para que quepa la imagen a la derecha
        setLocationRelativeTo(parent);

        iniciarComponentes();

        if (designEdition != null) {
            setTitle("Editar Diseño - AtelierOS");
            prellenarDatos(designEdition);
            btnGuardar.setText("Actualizar Cambios");
        } else {
            setTitle("Nuevo Diseño - AtelierOS");
        }
    }

    private void iniciarComponentes() {
        // --- PANEL DE FORMULARIO (CENTRO-IZQUIERDA) ---
        JPanel pnlCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        txtNombre = new JTextField();
        txtMaterial = new JTextField();
        txtTecnica = new JTextField();
        txtColor = new JTextField();
        JButton btnSubir = new JButton("Seleccionar Imagen");
        btnSubir.addActionListener(e -> seleccionarImagen());

        pnlCampos.add(new JLabel("Nombre Modelo:")); pnlCampos.add(txtNombre);
        pnlCampos.add(new JLabel("Material:"));      pnlCampos.add(txtMaterial);
        pnlCampos.add(new JLabel("Técnica:"));       pnlCampos.add(txtTecnica);
        pnlCampos.add(new JLabel("Color Base:"));    pnlCampos.add(txtColor);
        pnlCampos.add(new JLabel("Imagen:"));        pnlCampos.add(btnSubir);

        // --- PANEL DE VISTA PREVIA (DERECHA) ---
        lblPreview = new JLabel("Sin vista previa", SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(180, 180));
        lblPreview.setBorder(BorderFactory.createTitledBorder("Vista Previa"));

        // --- PANEL DE BOTONES (SUR) ---
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar Diseño");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);

        // --- AGREGAR AL DIÁLOGO ---
        add(pnlCampos, BorderLayout.CENTER);
        add(lblPreview, BorderLayout.EAST);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void prellenarDatos(Design d) {
        txtNombre.setText(d.getNombreModelo());
        txtMaterial.setText(d.getMaterial());
        txtTecnica.setText(d.getTecnica());
        txtColor.setText(d.getColor());
        
        // Si ya tiene imagen, mostrarla en el prellenado
        if (d.getRutaImagen() != null && !d.getRutaImagen().equals("default.png")) {
            this.rutaImagenSeleccionada = d.getRutaImagen();
            actualizarPreview(rutaImagenSeleccionada);
        }
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            this.rutaImagenSeleccionada = fc.getSelectedFile().getAbsolutePath();
            actualizarPreview(rutaImagenSeleccionada);
        }
    }
    
    // Método auxiliar para no repetir código de imagen
    private void actualizarPreview(String ruta) {
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblPreview.setIcon(new ImageIcon(img));
            lblPreview.setText("");
        } catch (Exception e) {
            lblPreview.setText("Error al cargar");
        }
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty() || txtMaterial.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Material son obligatorios");
            return;
        }

        Design d = (designEdition == null) ? new Design() : designEdition;
        d.setNombreModelo(txtNombre.getText().trim());
        d.setMaterial(txtMaterial.getText().trim());
        d.setTecnica(txtTecnica.getText().trim());
        d.setColor(txtColor.getText().trim());
        d.setRutaImagen(this.rutaImagenSeleccionada);

        boolean exito;
        if (designEdition == null) {
            exito = dDao.insertar(d);
        } else {
            exito = dDao.actualizar(d);
        }

        if (exito) {
            this.guardadoExitoso = true;
            JOptionPane.showMessageDialog(this, "Operación exitosa");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.");
        }
    }

    public boolean isGuardadoExitoso() { return guardadoExitoso; }
}