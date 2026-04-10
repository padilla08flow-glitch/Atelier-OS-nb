package moduloInterfaz;

import GestorProcesos.GestorCarrito;
import GestorProcesos.GestorVenta;
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
import moduloValidaciones.ValidadorVenta;
import moduloLogica.Cliente;
import moduloLogica.Usuario;

/**
 * Formulario refactorizado para Sprint III
 * @author Danna Padilla
 */
public class FrmVenta extends JInternalFrame{
    
    private JTextField txtBuscarProducto, txtCliente, txtCantidad, txtPago;
    private JTable tablaVenta;
    private DefaultTableModel modelo;
    private JComboBox<String> cbMetodoPago;
    private JLabel lblTotal;
    private Producto productoSeleccionado = null; 
    private List<VentaDetalle> listaCarrito = new ArrayList<>();
    private double totalPagar = 0.0;
    private double totalVenta = 0.0;
    
    private moduloLogica.Cliente clienteSeleccionado = new moduloLogica.Cliente(1, "Público General");
    private moduloLogica.Usuario usuarioActivo = new moduloLogica.Usuario(1, "Admin_Danna");
    
    
    public FrmVenta() {
        //titulo
        super("AtelierOS - Nueva Venta", true, true, true, true);
        //llamar a la funcion
        iniciarComponentes();
        
        try {
            this.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }
    
    private void iniciarComponentes() {
        Color rosaAtelier = new Color(255, 20, 147);
        //AREA SUPERIOR-DATOS DE LA VENTA
        JPanel pnlSuperior = new JPanel(new GridLayout(1, 4, 10, 10));
        pnlSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtCliente = new JTextField();
        txtCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre del Cliente");
        
        txtPago = new JTextField(10);
        txtPago.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "$ Pago con...");
        
        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        
        pnlSuperior.add(new JLabel("Cliente:"));
        pnlSuperior.add(txtCliente);
        pnlSuperior.add(new JLabel("Método Pago"));
        pnlSuperior.add(cbMetodoPago);
        add(pnlSuperior, BorderLayout.NORTH);
        
        //AREA CENTRAL - TABLA DE PRODUCTOS
        modelo = new DefaultTableModel(new Object[]{"ID", "SKU", "Descripción", "Cant", "P. Unitario", "Subtotal"},0);
        tablaVenta = new JTable(modelo);
        tablaVenta.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID: pequeño
        tablaVenta.getColumnModel().getColumn(1).setPreferredWidth(100); // SKU
        tablaVenta.getColumnModel().getColumn(2).setPreferredWidth(250); // Descripción: el más grande
        tablaVenta.getColumnModel().getColumn(3).setPreferredWidth(50);  // Cant: pequeño
        tablaVenta.getColumnModel().getColumn(4).setPreferredWidth(90);  // P. Unitario
        tablaVenta.getColumnModel().getColumn(5).setPreferredWidth(90);  // Subtotal
        add(new JScrollPane(tablaVenta), BorderLayout.CENTER);
        
        //PANEL DERECHO --> BUSCADOR
        JPanel pnlDerecho = new JPanel();
        pnlDerecho.setPreferredSize(new Dimension(250, 0));
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));
        pnlDerecho.setBorder(BorderFactory.createTitledBorder("Añadir Productos"));
        
        //PARA ELIMINAR
        JButton btnEliminar = new JButton("Quitar Seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        
        txtBuscarProducto = new JTextField();
        txtBuscarProducto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por SKU, modelo o material...");
        txtCantidad = new JTextField("1");
        txtBuscarProducto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        txtCantidad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JButton btnAgregar = new JButton("Agregar a Lista");
        btnAgregar.setBackground(rosaAtelier);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        pnlDerecho.add(Box.createVerticalStrut(10));
        pnlDerecho.add(btnEliminar);
        btnEliminar.addActionListener(e -> eliminarProductoDeLista());
        
        pnlDerecho.add(new JLabel("Buscar Producto:"));
        pnlDerecho.add(txtBuscarProducto);
        pnlDerecho.add(Box.createVerticalStrut(10));
        
        pnlDerecho.add(new JLabel("Cantidad:"));
        pnlDerecho.add(txtCantidad);
        pnlDerecho.add(Box.createVerticalStrut(20));
        pnlDerecho.add(btnAgregar);
        add(pnlDerecho, BorderLayout.EAST);

        //PANEL INFERIOR
        JPanel pnlInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlInferior.setBackground(new Color(245, 245, 245));
        pnlInferior.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        lblTotal = new JLabel("TOTAL: $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JButton btnGenerarVenta = new JButton("FINALIZAR VENTA");
        btnGenerarVenta.setBackground(new Color(40, 180, 99)); // Verde para éxito
        btnGenerarVenta.setForeground(Color.WHITE);
        btnGenerarVenta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        //listener
        btnAgregar.addActionListener(e -> agregarProductoALista());
        btnGenerarVenta.addActionListener(e -> btnCobrarAction(null));
        pnlInferior.add(new JLabel("Pago:"));
        pnlInferior.add(txtPago); 
        pnlInferior.add(lblTotal);
        pnlInferior.add(btnGenerarVenta);
        //esta pdjd faltaba
        add(pnlInferior, BorderLayout.SOUTH);
   }
    
    //AGREGAR A LA LISTA 
    private void agregarProductoALista() {
        //se movio de modulo
        String criterio = txtBuscarProducto.getText().trim();
        if (criterio.isEmpty()) return;
        int cantidad;
        
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad valida.");
            return;
        }
        
        //BUSCAR PRODUCTO
        ProductoDAO pDao = new ProductoDAO();
        List<Producto> resultados = pDao.buscar(criterio);
        
        //evaluar el resultado encontrado
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún producto con el criterio ingresado.");
            return;
        }
        
        Producto p = null;
        
        if (resultados.size() == 1) {
            p = resultados.get(0); //solo hay una coincidencia
        } else {
            //USAMOS LO QUE HAY EN SelectorProducto
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            SelectorProducto dialog = new SelectorProducto(parentFrame, resultados);
            dialog.setVisible(true);
            p = dialog.getProductoSeleccionado();
        }
        if (p != null) {
            GestorCarrito carritoHelper = new GestorCarrito(); //logica del carrito
            //validar stock
            int yaEnCarrito = carritoHelper.calcularCantidadEnCarrito(p.getId(), listaCarrito);
            if ((yaEnCarrito + cantidad) > p.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + p.getStock());
                return;
            }
            double subtotalRecibido = carritoHelper.agregarOActualizar(p, cantidad, listaCarrito, modelo);
            this.totalPagar += subtotalRecibido;
            lblTotal.setText("TOTAL: $" + String.format("%.2f", this.totalPagar));//limpiar y resetear
            txtBuscarProducto.setText("");
            txtCantidad.setText("1");
            txtBuscarProducto.requestFocus();
        }
  
    }
 
    private void eliminarProductoDeLista() {
        int fila = tablaVenta.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para quitarlo.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, 
            "¿Quitar este producto del carrito?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            GestorCarrito carritoHelper = new GestorCarrito();
            double montoARestar = carritoHelper.eliminarProducto(fila, listaCarrito, modelo);
            
            this.totalPagar -= montoARestar;
            this.totalVenta = this.totalPagar;
            
            //se actualiza por si el carrito queda vacio
            if (listaCarrito.isEmpty()) {
                this.totalPagar = 0.0;
                this.totalVenta = 0.0;
            }
            lblTotal.setText("TOTAL: $" + String.format("%.2f", totalPagar));
        }
    }
    
    private void limpiarFormularioVenta() {
        txtCliente.setText("");
        modelo.setRowCount(0);
        totalPagar = 0.0;
        lblTotal.setText("TOTAL: $0.00");
        listaCarrito.clear();
    }
    
    private void btnCobrarAction(java.awt.event.ActionEvent evt){
        this.totalVenta = this.totalPagar;
        
        if(listaCarrito == null || listaCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,"El carrito está vacío.");
            return;
        }
        if (txtPago.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el monto con el que paga el cliente.");
            return;
        }

        String nombreCliente = txtCliente.getText().trim();
        if(nombreCliente.isEmpty()){
            nombreCliente = "Público General";
        }
        
        clienteSeleccionado.setNombreCompleto(nombreCliente);
        GestorVenta gestor = new GestorVenta();
        
        boolean exito = gestor.procesarCobro(
            totalVenta,
            txtPago.getText(),
            listaCarrito, 
            clienteSeleccionado.getId(),
            usuarioActivo.getId(), 
            this
        );
        if (exito) {
            limpiarFormularioVenta();
            txtPago.setText("");
        }
    }
}
