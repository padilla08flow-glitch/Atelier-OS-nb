package moduloInterfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import moduloConexion.DesignDAO;
import moduloConexion.ProductoDAO;
import moduloLogica.Design;
import moduloLogica.Producto;
import moduloValidaciones.ValidadorInventario;

/**
 * Panel de Gestión de Productos (Stock Físico)
 * Optimizado: Herencia de imagen desde Diseño.
 * @author Danna Padilla
 */

public class PnlProducto extends JPanel{
    // Componentes de Interfaz
    private JLabel lblFoto;
    private JComboBox<Design> cbDisenos;
    private JComboBox<String> cbTalla;
    private JTextField txtSku, txtPrecio, txtStock;
    private JTable tablaProductos;
    private DefaultTableModel modelo;
    private JButton btnGuardar;
    
    //logica
    private final ProductoDAO pDao = new ProductoDAO();
    private final DesignDAO dDao = new DesignDAO();
    private List<Producto> listaProductosActual;
    private int idProductoSeleccionado = -1;
    
    //
    private final Color COLOR_INSERTAR = new Color(46, 204, 113);
    private final Color COLOR_ACTUALIZAR = new Color(52, 152, 219);
    private final Color COLOR_ELIMINAR = new Color(231, 76, 60);
    
    public PnlProducto(){
        this.setLayout(new BorderLayout(10, 10));
        iniciarComponentes();
        cargarDatosTabla();
    }
    
    private void iniciarComponentes() {
        // --- PANEL LATERAL IZQUIERDO: FORMULARIO ---
        JPanel pnlForm = new JPanel(new GridLayout(12, 1, 5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Entrada de Stock"));
        pnlForm.setPreferredSize(new Dimension(260, 0));

        cbDisenos = new JComboBox<>();
        txtSku = new JTextField();
        txtSku.setEditable(false);
        txtSku.setBackground(new Color(230, 230, 230));
        //la modificación pedida sobre el SKU
        txtSku.setToolTipText("Generado automáticamente según el diseño y talla");
        
        String[] tallas = {"XS", "S", "CH", "M", "G", "L", "XL", "XG", "UNITALLA"};
        cbTalla = new JComboBox<>(tallas);
        txtPrecio = new JTextField();
        txtStock = new JTextField();

        cbDisenos.addActionListener(e -> dispararGeneracionSKU());
        cbTalla.addActionListener(e -> dispararGeneracionSKU());

        // Botones
        btnGuardar = new JButton("Registrar Stock");
        btnGuardar.setBackground(COLOR_INSERTAR);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarProducto());

        JButton btnLimpiar = new JButton("Nuevo/Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());

        JButton btnEliminar = new JButton("Eliminar Producto");
        btnEliminar.setBackground(COLOR_ELIMINAR);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarProducto());

        pnlForm.add(new JLabel("Diseño base:")); pnlForm.add(cbDisenos);
        pnlForm.add(new JLabel("SKU (Auto):")); pnlForm.add(txtSku);
        pnlForm.add(new JLabel("Talla:")); pnlForm.add(cbTalla);
        pnlForm.add(new JLabel("Precio de Venta:")); pnlForm.add(txtPrecio);
        pnlForm.add(new JLabel("Cantidad en Stock:")); pnlForm.add(txtStock);
        pnlForm.add(new JLabel(""));
        pnlForm.add(btnGuardar);
        pnlForm.add(btnLimpiar);
        pnlForm.add(btnEliminar);

        // ---PANEL CENTRAL
        modelo = new DefaultTableModel(new Object[]{"ID", "Diseño", "SKU", "Talla", "Precio", "Stock"}, 0);
        tablaProductos = new JTable(modelo);
        tablaProductos.setRowHeight(25);
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionado();
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Inventario Actual"));

        // ---PANEL DERECHO
        JPanel pnlFoto = new JPanel(new BorderLayout());
        pnlFoto.setBorder(BorderFactory.createTitledBorder("Vista Previa"));
        pnlFoto.setPreferredSize(new Dimension(220, 0));
        
        lblFoto = new JLabel("Sin Imagen", SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        pnlFoto.add(lblFoto, BorderLayout.CENTER);
        
        this.add(pnlForm, BorderLayout.WEST);
        this.add(scrollTabla, BorderLayout.CENTER);
        this.add(pnlFoto, BorderLayout.EAST);

        actualizarListaDisenos();
        ajustarColumnasTabla();
    }
    
    private void ajustarColumnasTabla() {
        if (tablaProductos.getColumnCount() > 0) {
            tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
            tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(180); // Diseño
            tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100); // SKU
        }
    }
    
    private void cargarDatosTabla() {
        modelo.setRowCount(0);
        listaProductosActual = pDao.listar();
        for (Producto p : listaProductosActual) {
            modelo.addRow(new Object[]{
                p.getId(), p.getNombreModelo(), p.getSku(), 
                p.getTalla(), p.getPrecio(), p.getStock()
            });
        }
    }
    
    private void cargarSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila != -1 && listaProductosActual != null) {
            Producto p = listaProductosActual.get(fila);
            idProductoSeleccionado = p.getId();
            txtSku.setText(p.getSku());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtStock.setText(String.valueOf(p.getStock()));
            cbTalla.setSelectedItem(p.getTalla());

            for (int i = 0; i < cbDisenos.getItemCount(); i++) {
                if (cbDisenos.getItemAt(i).getIdDesign() == p.getIdDesign()) {
                    cbDisenos.setSelectedIndex(i);
                    break;
                }
            }
            
            colocarImagen(p.getRutaImagen());
            btnGuardar.setText("Actualizar Producto");
            btnGuardar.setBackground(COLOR_ACTUALIZAR);
        }
    }
    
    private void guardarProducto() {
        String talla = (String) cbTalla.getSelectedItem();
        if (!ValidadorInventario.validarProductoFisico(txtSku.getText(), talla, txtPrecio.getText(), txtStock.getText(), this)) return;

        if (idProductoSeleccionado == -1 && !ValidadorInventario.validarSkuDuplicado(txtSku.getText(), pDao, this)) return;

        Design seleccionado = (Design) cbDisenos.getSelectedItem();
        try {
            Producto p = new Producto();
            p.setIdDesign(seleccionado.getIdDesign());
            p.setSku(txtSku.getText());
            p.setTalla(talla);
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));
            p.setGenero("Unisex");
            
            boolean exito;
            if(idProductoSeleccionado == -1){
                exito = pDao.insertar(p);
            }else {
                p.setId(idProductoSeleccionado);
                exito = pDao.actualizar(p);
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, "Guardado con éxito.");
                cargarDatosTabla();
                limpiar();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio o Stock inválidos.");
        }
    }
    
    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar producto?" , "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (pDao.eliminar(id)) {
                cargarDatosTabla();
                limpiar();
            }
        }
    }
    
    private void limpiar() {
        idProductoSeleccionado = -1;
        txtPrecio.setText("");
        txtStock.setText("");
        if (cbDisenos.getItemCount() > 0) cbDisenos.setSelectedIndex(0);
        cbTalla.setSelectedIndex(0);
        btnGuardar.setText("Registrar Stock");
        btnGuardar.setBackground(COLOR_INSERTAR);
        dispararGeneracionSKU();
    }
    
    private void dispararGeneracionSKU() {
        Design seleccionado = (Design) cbDisenos.getSelectedItem();
        if (seleccionado == null) {
            txtSku.setText("");
            lblFoto.setIcon(null);
            lblFoto.setText("Sin selección");
            return;
        }
        String talla = (String) cbTalla.getSelectedItem();
        txtSku.setText(pDao.generarSKUAutomatico(seleccionado.getIdDesign(), talla));
        colocarImagen(seleccionado.getRutaImagen());
    }
    
    private void colocarImagen(String ruta) {
        if (ruta != null && !ruta.isEmpty() && !ruta.equals("null")) {
            try {
                ImageIcon img = new ImageIcon(ruta);
                if (img.getIconWidth() == -1) {
                    lblFoto.setIcon(null);
                    lblFoto.setText("No encontrada");
                } else {
                    Image escala = img.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(escala));
                    lblFoto.setText("");
                }
            } catch (Exception e) {
                lblFoto.setText("Error");
            }
        } else {
            lblFoto.setIcon(null);
            lblFoto.setText("Sin Imagen");
        }
    }
    
    public void actualizarListaDisenos() {
        cbDisenos.removeAllItems();
        dDao.listarTodos().forEach(cbDisenos::addItem);
        dispararGeneracionSKU();
    } 
}

