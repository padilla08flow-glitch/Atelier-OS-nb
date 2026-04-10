package moduloInterfaz;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import moduloConexion.*;
import moduloLogica.*;
import moduloValidaciones.ValidadorApartado;
import GestorProcesos.*;

/**
 * Formulario Principal de Apartados
 * @author Danna Padilla
 */
public class FrmApartado extends JInternalFrame {
    
    private JTabbedPane pestañas;
    private JTextField txtBuscarProducto, txtBuscarCliente,txtAbono;
    private JLabel lblPrecioTotal, lblFechaVencimiento, lblClienteSeleccionado;
    private JTable tblGestion, tblVencidos;
    private JLabel lblAlertaVencidos;
    private FrmMonVencidos monitorVencidos;
    
    private Producto productoSeleccionado = null;
    private Cliente clienteSeleccionado = null; 
    private double precioActual = 0.0;
    
    private final GestorApartados gestorAbonos = new GestorApartados(); 
    
    public FrmApartado() {
        super("Módulo de Apartados - AtelierOS", true, true, true, true);
        configurarFrame();
        iniciarComponentes();
    }
    
    private void configurarFrame() {
        this.setLayout(new BorderLayout());
        this.setSize(900, 650);
    }

    private void iniciarComponentes() {
        pestañas = new JTabbedPane();
        monitorVencidos = new FrmMonVencidos();
        
        //pestañas 
        pestañas.addTab("1. Nuevo Apartado", crearPanelRegistro());
        pestañas.addTab("2. Gestión de Pagos", crearPanelGestion());
        pestañas.addTab("3. Monitor de Vencimientos", monitorVencidos.getContentPane());
        
        pestañas.addChangeListener(e -> {
            int index = pestañas.getSelectedIndex();
            if (index == 1) {
                gestorAbonos.refreshTablaPrincipal(tblGestion);
            } else if (index == 2) {
                monitorVencidos.actualizarDatos(); 
            }
        });
        
        this.add(pestañas, BorderLayout.CENTER);
    }
    
    //1- REGISTRO
    private JPanel crearPanelRegistro(){

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        JPanel pnlDatos = new JPanel(new GridBagLayout());
        pnlDatos.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridx = 0;

        txtBuscarProducto = new JTextField();
        txtBuscarProducto.setPreferredSize(new Dimension(380, 40));
        txtBuscarProducto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Presiona ENTER para buscar SKU o Modelo...");
        txtBuscarProducto.addActionListener(e -> accionBuscar());
        
        
        gbc.gridy = 0; pnlDatos.add(new JLabel("Producto:"), gbc);
        gbc.gridy = 1; pnlDatos.add(txtBuscarProducto, gbc);
        
        txtBuscarCliente = new JTextField();
        txtBuscarCliente.setPreferredSize(new Dimension(380, 40));
        txtBuscarCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre o Celular + ENTER...");
        txtBuscarCliente.addActionListener(e -> accionBuscarCliente());
    
        lblClienteSeleccionado = new JLabel("Seleccione un cliente...");
        lblClienteSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblClienteSeleccionado.setForeground(new Color(120, 120, 120));

        gbc.gridy = 2; pnlDatos.add(new JLabel("Cliente:"), gbc);
        gbc.gridy = 3; pnlDatos.add(txtBuscarCliente, gbc);
        gbc.gridy = 4; pnlDatos.add(lblClienteSeleccionado, gbc);
        
        lblPrecioTotal = new JLabel("TOTAL: $0.00");
        lblPrecioTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPrecioTotal.setForeground(new Color(180, 70, 70));

        lblFechaVencimiento = new JLabel("Vence: ");
        lblFechaVencimiento.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        gbc.gridy = 5; gbc.insets = new Insets(15, 5, 5, 5);
        pnlDatos.add(new JSeparator(), gbc);

        gbc.gridy = 6; pnlDatos.add(lblPrecioTotal, gbc);
        gbc.gridy = 7; pnlDatos.add(lblFechaVencimiento, gbc);
        
        txtAbono = new JTextField();
        txtAbono.setPreferredSize(new Dimension(380, 45));
        txtAbono.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtAbono.setHorizontalAlignment(JTextField.CENTER);
        
        gbc.gridy = 8; pnlDatos.add(new JLabel("ABONO INICIAL:"), gbc);
        gbc.gridy = 9; pnlDatos.add(txtAbono, gbc);
        
        JButton btnRegistrar = new JButton("CONFIRMAR APARTADO");
        btnRegistrar.setPreferredSize(new Dimension(380, 50));
        estilizarBtnPrincipal(btnRegistrar);
        btnRegistrar.addActionListener(e -> accionGuardar());

        gbc.gridy = 10; gbc.insets = new Insets(25, 5, 5, 5);
        pnlDatos.add(btnRegistrar, gbc);

        pnlPrincipal.add(pnlDatos, BorderLayout.WEST);
        pnlPrincipal.add(new JPanel(), BorderLayout.CENTER);
        return pnlPrincipal;
    }
    
    //2- GeSTION abonos
    private JPanel crearPanelGestion() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        tblGestion = new JTable();
        tblGestion.setRowHeight(25);
        
        JButton btnAbonar = new JButton("Registrar Abono");
        btnAbonar.setPreferredSize(new Dimension(0, 50));
        btnAbonar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        //evento
        btnAbonar.addActionListener(e -> {
            int fila = tblGestion.getSelectedRow();
            if (fila != -1) {
                gestorAbonos.abonarApartado(fila, tblGestion, this);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un registro.");
            }
        });
        
        pnl.add( new JScrollPane(tblGestion), BorderLayout.CENTER);
        pnl.add(btnAbonar, BorderLayout.SOUTH);
        
        return pnl;
    }

    private void accionBuscar() {
        //mejorar la busqueda
        String criterio = txtBuscarProducto.getText().trim();
        if (criterio.isEmpty()) return;
        
        List<Producto> lista = new ProductoDAO().buscar(criterio);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el producto: " + criterio);
            txtBuscarProducto.selectAll();
            return;
        }

        //reutilizar el Selector 
        if (lista.size() > 1) {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            SelectorProducto dialog = new SelectorProducto(parent, lista);
            dialog.setVisible(true);
            productoSeleccionado = dialog.getProductoSeleccionado();
        } else {
            productoSeleccionado = lista.get(0);
        }

        if (productoSeleccionado != null) {
            txtBuscarProducto.setText(productoSeleccionado.getNombreModelo()+ " (" + productoSeleccionado.getSku() + ")");
            actualizarInfoProducto();
            txtBuscarCliente.requestFocus();
        }
    }
    
    private void accionBuscarCliente() {
        String criterio = txtBuscarCliente.getText().trim();
        if (criterio.isEmpty()) return;

        List<Cliente> resultados = new ClienteDAO().buscar(criterio);
        
        if (resultados.isEmpty()) {
            ofrecerRegistroNuevoCliente(criterio);
        } else if (resultados.size() == 1) {
            clienteSeleccionado = resultados.get(0);
        } else {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            SelectorCliente dialog = new SelectorCliente(parent, resultados);
            dialog.setVisible(true);
            clienteSeleccionado = dialog.getClienteSeleccionado();
        }
        actualizarInfoCliente();
    }

    private void accionGuardar() {
        if (!validarSeleccion()) return;
        if (!ValidadorApartado.esAbonoValido(txtAbono.getText(), precioActual, this)) return;

        Apartado nuevo = prepararObjetoApartado();
        int id = new ApartadoDAO().registrarApartadoCompleto(nuevo);

        if (id != -1) {
            JOptionPane.showMessageDialog(this, "¡Éxito! Folio: " + id);
            limpiar();
            gestorAbonos.refreshTablaPrincipal(tblGestion);
        }
    }
    
    //METODOS AUXILIARES
    private void actualizarInfoProducto() {
        if (productoSeleccionado != null) {
            if (productoSeleccionado.getStock() <= 0) {
                JOptionPane.showMessageDialog(this, "Sin stock.");
                productoSeleccionado = null;
                return;
            }
            precioActual = productoSeleccionado.getPrecio();
            lblPrecioTotal.setText("Precio Total: $" + String.format("%.2f", precioActual));
            lblFechaVencimiento.setText("Vence: " + new Apartado().getFechaLimite().toString().substring(0, 10));
            txtAbono.requestFocus();
        }
    }
    
    private void actualizarInfoCliente() {
        if (clienteSeleccionado != null) {
            lblClienteSeleccionado.setText("Cliente: " + clienteSeleccionado.getNombreCompleto());
            lblClienteSeleccionado.setForeground(new Color(40, 180, 99));
            txtBuscarCliente.setText("");
        }
    }
    
    private void ofrecerRegistroNuevoCliente(String nombre) {
        JPanel panelNuevo = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtNom = new JTextField(nombre);
        JTextField txtTel = new JTextField();
        
        panelNuevo.add(new JLabel("Nombre:")); panelNuevo.add(txtNom);
        panelNuevo.add(new JLabel("Teléfono:")); panelNuevo.add(txtTel);
        
        int res = JOptionPane.showConfirmDialog(this, panelNuevo, 
            "Cliente no encontrado - ¿Registrar?" , JOptionPane.OK_CANCEL_OPTION);
        
        if (res == JOptionPane.OK_OPTION) {
            Cliente nuevo = new Cliente();
            nuevo.setNombreCompleto(txtNom.getText());
            nuevo.setTelefono(txtTel.getText());

            int id = new ClienteDAO().registrar(nuevo);
            if (id != -1) {
                nuevo.setId(id);
                clienteSeleccionado = nuevo;
                actualizarInfoCliente();
                txtAbono.requestFocus(); // Salto al dinero
            }
        }
    }
    
    private boolean validarSeleccion() {
        if (productoSeleccionado == null || clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione Producto y Cliente.");
            return false;
        }
        return true;
    }
    
    private Apartado prepararObjetoApartado() {
        Apartado ap = new Apartado();
        ap.setIdCliente(clienteSeleccionado.getId());
        ap.setIdProducto(productoSeleccionado.getId());
        ap.setIdUsuario(1);
        ap.setTotalPrenda(precioActual);
        ap.setPagoInicial(Double.parseDouble(txtAbono.getText()));
        return ap;
    }
    
    private void estilizarBtnPrincipal(JButton btn) {
        btn.setBackground(new Color(235, 154, 178));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
    private void limpiar() {
        txtBuscarProducto.setText("");
        txtBuscarCliente.setText("");
        txtAbono.setText("");
        lblPrecioTotal.setText("Precio Total: $0.00");
        lblClienteSeleccionado.setText("Cliente: (No seleccionado)");
        lblClienteSeleccionado.setForeground(new Color(120, 120, 120));
        productoSeleccionado = null;
        clienteSeleccionado = null;
    }
}
