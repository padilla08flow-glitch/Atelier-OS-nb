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
 *
 * @author Danna Padilla
 */
public class PnlProducto {
    private JComboBox<Design> cbDisenos;
    private JTextField txtSku, txtTalla, txtPrecio, txtStock;
    private JTable tablaProductos;
    private DefaultTableModel modelo;
    private ProductoDAO pDao = new ProductoDAO();
    private DesignDAO dDao = new DesignDAO();
    
    public PnlProducto(){
        
    }
    
    private void iniciarComponentes(){
        JPanel pnlForm = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Entrada de Stock"));
        pnlForm.setPreferredSize(new Dimension(300, 0));
        
        cbDisenos = new JComboBox<>();
        txtSku = new JTextField();
        txtTalla = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        JButton btnGuardar = new JButton("Registrar Stock");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        
        btnGuardar.addActionListener(e -> guardarProducto());
        
        pnlForm.add(new JLabel("Seleccionar Diseño del Catálogo:")); pnlForm.add(cbDisenos);
        pnlForm.add(new JLabel("Código SKU:")); pnlForm.add(txtSku);
        pnlForm.add(new JLabel("Talla (CH, M, G, etc.):")); pnlForm.add(txtTalla);
        pnlForm.add(new JLabel("Precio de Venta:")); pnlForm.add(txtPrecio);
        pnlForm.add(new JLabel("Cantidad en Stock:")); pnlForm.add(txtStock);
        pnlForm.add(new JLabel("")); pnlForm.add(btnGuardar);
        
        add(pnlForm, BorderLayout.WEST);
        
        //TABLA DE PRODUCTOS
        modelo = new DefaultTableModel(new Object[]{"ID", "Diseño", "SKU", "Talla", "Precio", "Stock"}, 0);
        tablaProductos = new JTable(modelo);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        
        actualizarListaDisenos(); //CARGAR DISEÑOS DISPONIBLES
        
    }
    
    public void actualizarListaDisenos() {
        cbDisenos.removeAllItems();
        List<Design> lista = dDao.listarTodos();
        for (Design d : lista) {
            cbDisenos.addItem(d);
        }
    }
    
    private void guardarProducto() {
        if (!ValidadorInventario.validarProductoFisico(txtSku.getText(), txtTalla.getText(), txtPrecio.getText(), txtStock.getText(), this)) return;
        Design seleccionado = (Design) cbDisenos.getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un diseño base.");
            return;
        }
        
        //objeto
        Producto p = new Producto();
        p.setIdDesign(seleccionado.getIdDesign());
        p.setSku(txtSku.getText());
        p.setTalla(txtTalla.getText());
        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
        p.setStock(Integer.parseInt(txtStock.getText()));
        
        //
        if (pDao.insertar(p)) {
            JOptionPane.showMessageDialog(this, "Stock actualizado correctamente.");
            cargarDatosTabla();
            limpiar();
        }
    }
    private void cargarDatosTabla() {
        modelo.setRowCount(0);
        List<Producto> lista = pDao.listar();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getSku(), p.getTalla(), p.getPrecio(), p.getStock()});
        }
    }
    
    private void limpiar() {
        txtSku.setText(""); txtTalla.setText(""); txtPrecio.setText(""); txtStock.setText("");
    }
}
