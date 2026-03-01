package moduloInterfaz;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import moduloConexion.ApartadoDAO;
import moduloConexion.ProductoDAO;
import moduloLogica.Apartado;
import moduloLogica.Producto;
import moduloValidaciones.ValidadorApartado;

/**
 *
 * @author Danna Padilla
 */
public class FrmApartado extends JInternalFrame {

    private JTextField txtBuscarProducto, txtCliente, txtAbono;
    private JLabel lblPrecioTotal, lblFechaVencimiento;
    private Producto productoSeleccionado = null;
    private double precioActual = 0.0;

    
    public FrmApartado() {
        configurarVentana();
        iniciarComponentes();
    }
    
    public void configurarVentana() {
        setTitle("Gestión de Apartados - Casa Victoria");
        setClosable(true);
        setIconifiable(true);
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        // PANEL IZQUIERDO: BUSCADOR Y DATOS DEL PRODUCTO
        JPanel pnlDatos = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlDatos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlDatos.setPreferredSize(new Dimension(350, 0));

        txtBuscarProducto = new JTextField();
        txtBuscarProducto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese SKU");
        JButton btnBuscar = new JButton("Buscar Producto");
        
        txtCliente = new JTextField();
        txtCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre del Cliente");

        lblPrecioTotal = new JLabel("Precio Total: $0.00");
        lblFechaVencimiento = new JLabel("Vence en: (Seleccione producto)");
        txtAbono = new JTextField();

        JButton btnRegistrar = new JButton("REGISTRAR APARTADO");
        btnRegistrar.setBackground(new Color(41, 128, 185));
        btnRegistrar.setForeground(Color.WHITE);

        // Eventos
        btnBuscar.addActionListener(e -> accionBuscar());
        btnRegistrar.addActionListener(e -> accionGuardar());

        pnlDatos.add(new JLabel("1. Producto:")); pnlDatos.add(txtBuscarProducto); pnlDatos.add(btnBuscar);
        pnlDatos.add(new JLabel("2. Cliente:")); pnlDatos.add(txtCliente);
        pnlDatos.add(lblPrecioTotal); pnlDatos.add(lblFechaVencimiento);
        pnlDatos.add(new JLabel("3. Abono Inicial:")); pnlDatos.add(txtAbono);
        pnlDatos.add(btnRegistrar);

        add(pnlDatos, BorderLayout.WEST);
        add(new JPanel().add(new JLabel("Resumen")), BorderLayout.CENTER);
        
    }

    private void accionBuscar() {
        ProductoDAO pDao = new ProductoDAO();
        List<Producto> lista = pDao.buscar(txtBuscarProducto.getText().trim());

        if (!lista.isEmpty()) {
            productoSeleccionado = lista.get(0);
            if (productoSeleccionado.getStock() <= 0) {
                JOptionPane.showMessageDialog(this, "Sin stock disponible");
                return;
            }
            precioActual = productoSeleccionado.getPrecio();
            lblPrecioTotal.setText("Precio Total: $" + precioActual);
            
            // Mostrar fecha proyectada (Lógica de los 30 días)
            Apartado temp = new Apartado(); 
            lblFechaVencimiento.setText("Vence el: " + temp.getFechaLimite().toString().substring(0, 10));
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.");
        }
    }

    private void accionGuardar() {
        if (!ValidadorApartado.validarCamposCompletos(productoSeleccionado, txtCliente.getText(), this)) return;
        
        if (!ValidadorApartado.esAbonoValido(txtAbono.getText(), precioActual, this)) return;
        
        Apartado nuevo = new Apartado();
        nuevo.setIdCliente(1); 
        nuevo.setIdProducto(productoSeleccionado.getId());
        nuevo.setTotalPrenda(precioActual);
        nuevo.setPagoInicial(Double.parseDouble(txtAbono.getText()));

        if (new ApartadoDAO().registrarApartados(nuevo)) {
            JOptionPane.showMessageDialog(this, "Apartado exitoso.");
            limpiar();
        }
    }

    private void limpiar() {
        txtBuscarProducto.setText("");
        txtCliente.setText("");
        txtAbono.setText("");
        //lblPrecioTotal.setText("Precio Total: $0.00");
        //lblFechaVencimiento.setText("Vence en: (Seleccione producto)");
        productoSeleccionado = null;
    }
}
