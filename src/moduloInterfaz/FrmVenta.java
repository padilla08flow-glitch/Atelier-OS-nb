package moduloInterfaz;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import java.util.List;
import java.util.ArrayList;
import moduloConexion.ProductoDAO;
import moduloConexion.VentaDAO;
import moduloLogica.Venta;
import moduloLogica.VentaDetalle;
import moduloLogica.Producto;

/**
 * Formulario refactorizado para Sprint III
 * @author Danna Padilla
 */
public class FrmVenta extends JFrame{
    
    private JTextField txtBuscarProducto, txtCliente, txtCantidad;
    private JTable tablaVenta;
    private DefaultTableModel modelo;
    private JComboBox<String> cbMetodoPago;
    private JLabel lblTotal;
    private Producto productoSeleccionado = null; 
    private List<VentaDetalle> listaCarrito = new ArrayList<>();
    private double totalPagar = 0.0;
    
    public FrmVenta() {
        //titulo
        setTitle("AtelierOS - Nueva Venta");
        setSize(1000, 700);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(null);
        
        //llamar a la funcion
        iniciarComponentes();
    }
    
    private void iniciarComponentes() {
        
        //AREA SUPERIOR-DATOS DE LA VENTA
        JPanel pnlSuperior = new JPanel(new GridLayout(1, 4, 10, 10));
        pnlSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtCliente = new JTextField();
        txtCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre del Cliente");
        
        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        
        pnlSuperior.add(new JLabel("Cliente:"));
        pnlSuperior.add(txtCliente);
        pnlSuperior.add(new JLabel("Método Pago"));
        pnlSuperior.add(cbMetodoPago);
        add(pnlSuperior, BorderLayout.NORTH);
        
        //AREA CENTRAL-TABLA DE PRODUCTOS
        modelo = new DefaultTableModel(new Object[]{"ID", "SKU", "Descripción", "Cant", "P. Unitario", "Subtotal"},0);
        tablaVenta = new JTable(modelo);
        add(new JScrollPane(tablaVenta), BorderLayout.CENTER);
        
        //PANEL DERECHO --> BUSCADOR
        JPanel pnlDerecho = new JPanel();
        pnlDerecho.setPreferredSize(new Dimension(250, 0));
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));
        pnlDerecho.setBorder(BorderFactory.createTitledBorder("Añadir Productos"));
        
        txtBuscarProducto = new JTextField();
        txtBuscarProducto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Escanear SKU...");
        txtCantidad = new JTextField("1");
        JButton btnAgregar = new JButton("Agregar a Lista");
        
        pnlDerecho.add(new JLabel("Buscar Producto:"));
        pnlDerecho.add(txtBuscarProducto);
        pnlDerecho.add(Box.createVerticalStrut(10));
        pnlDerecho.add(new JLabel("Cantidad:"));
        pnlDerecho.add(txtCantidad);
        pnlDerecho.add(Box.createVerticalStrut(20));
        pnlDerecho.add(btnAgregar);
        add(pnlDerecho, BorderLayout.EAST);
        
        //PANEL INFERIOR-TOTAL DE LA VENTA
        JPanel pnlInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("TOTAL: $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JButton btnGenerarVenta = new JButton("FINALIZAR VENTA");
        btnGenerarVenta.setBackground(new Color(40, 180, 99));
        btnGenerarVenta.setForeground(Color.WHITE);
        
        //listener
        btnGenerarVenta.addActionListener(e -> finalizarVenta());
        btnAgregar.addActionListener(e -> agregarProductoALista());
        
        pnlInferior.add(lblTotal);
        pnlInferior.add(Box.createHorizontalStrut(30));
        pnlInferior.add(btnGenerarVenta);
        add(pnlInferior, BorderLayout.SOUTH);
   }
    
    //AGREGAR A LA LISTA 
    private void agregarProductoALista() {
        String sku = txtBuscarProducto.getText().trim();
        int cantidad; 
        
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresar una cantidad válida");
            return;
        }
        //BUSQUEDA DE PRODUCTO
        ProductoDAO pDao = new ProductoDAO();
        List<Producto> resultados = pDao.buscar(sku);
        
        if (!resultados.isEmpty()) {
            productoSeleccionado = resultados.get(0);
            
            if (productoSeleccionado.getStock() < cantidad) {
                JOptionPane.showMessageDialog(this, "Stock insuficientes. Unidades: " + productoSeleccionado.getStock());
                return;
            }
            
            double subtotal = productoSeleccionado.getPrecio() * cantidad;
            totalPagar += subtotal;
            
            //venta detalle
            VentaDetalle detalle = new VentaDetalle();
            detalle.setIdProducto(productoSeleccionado.getId());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(productoSeleccionado.getPrecio());
            listaCarrito.add(detalle);
            
            //AÑADIR A LA TABLA
            modelo.addRow(new Object[]{
                productoSeleccionado.getId(),
                productoSeleccionado.getSku(),
                productoSeleccionado.getNombre() + " (" + productoSeleccionado.getTalla() + ")",
                cantidad,
                productoSeleccionado.getPrecio(),
                subtotal
            });
            
            lblTotal.setText("TOTAL: $" + String.format("%.2f", totalPagar));
            txtBuscarProducto.setText("");
            productoSeleccionado = null;
        }else {
            JOptionPane.showMessageDialog(this, "Producto no Encontrado");
        }
    }
    
    //RECOLECTAR DATOS DE LA TABLA Y ENVIARLOS AL DAO
    private void finalizarVenta() {
    //VALIDACIONES
        if (txtCliente.getText().isEmpty() || modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un cliente y al menos un producto");
            return;
        }
        //SE CREA UN OBJETO --> VENTA
        Venta nuevaVenta = new Venta();
        nuevaVenta.setIdCliente(1); 
        nuevaVenta.setNombreCliente(txtCliente.getText().trim());
        nuevaVenta.setMetodoPago(cbMetodoPago.getSelectedItem().toString());
        nuevaVenta.setTotal(totalPagar);

        // sE LLAMA AL DAO PARA LA TRANSACCION
        //ENVIAMOS -OBJETO -->VENTA, LIST<VENTADETALLE>
        VentaDAO vDao = new VentaDAO();
        if (vDao.registrarVentaCompleta(nuevaVenta, listaCarrito)) {
            JOptionPane.showMessageDialog(this, "¡Venta Realizada con Éxito!\nEl inventario ha sido actualizado.");
            limpiarFormularioVenta();
            
            listaCarrito.clear();
        } else {
            JOptionPane.showMessageDialog(this, "Error al procesar la venta.");
        }
    }

    private void limpiarFormularioVenta() {
        txtCliente.setText("");
        modelo.setRowCount(0);
        totalPagar = 0.0;
        lblTotal.setText("TOTAL: $0.00");
        listaCarrito.clear();
    }
    
}
