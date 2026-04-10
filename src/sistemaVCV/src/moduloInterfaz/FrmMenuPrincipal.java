package moduloInterfaz;
import java.awt.*;
import java.beans.PropertyVetoException;
import javax.swing.*;

/**
 *
 * @author Danna Padilla
 */
public class FrmMenuPrincipal extends JFrame{
    private EscritorioConFondo escritorio;
    private String rolUsuario;
    //botones
    private JButton btnGestionarProductos;
    private JButton btnNuevaVenta;
    private JButton btnGestionarApartados;
    private JButton btnConfiguracion;
    private JButton btnCerrarSesion;
    private JLabel lblAviso;
        
    public FrmMenuPrincipal (String rol){
        this.rolUsuario = rol;
        
        setTitle("AtelierOS - Casa Victoria");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        iniciarComponentes();
        aplicarPermisos();
    }
    
    //metodos
    private void iniciarComponentes(){
        //escritorio
        escritorio = new EscritorioConFondo();
        escritorio.setBounds(260, 70, 810, 630); 
        escritorio.setBackground(new Color(245, 245, 245));
        add(escritorio);
        
        //TITULO
        JLabel lblTitulo = new JLabel("PANEL DE CONTROL - ATELIER OS");
        lblTitulo.setBounds(30, 20, 500, 30);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitulo);
        
        // -BOTON PARA NUEVA VENTA
        btnNuevaVenta = new JButton("Nueva Venta");
        btnNuevaVenta.setBounds(40, 100, 200, 50); 
        btnNuevaVenta.setBackground(new Color(230, 227, 227)); 
        //btnNuevaVenta.setForeground(Color.WHITE);
        btnNuevaVenta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNuevaVenta.addActionListener(e -> {
            FrmVenta v = new FrmVenta();
            escritorio.add(v); 
            v.setVisible(true);
            try {
                v.setMaximum(true);
            } catch (PropertyVetoException ex) {
                ex.printStackTrace();
            }
        });
        add(btnNuevaVenta);
        
        //BOTON DE APARTADO 
        btnGestionarApartados = new JButton("Gestionar Apartados");
        btnGestionarApartados.setBounds(40, 170, 200, 50);
        btnGestionarApartados.setBackground(new Color(230, 227, 227)); 
        btnGestionarApartados.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGestionarApartados.addActionListener(e -> {
            FrmApartado ap = new FrmApartado();
            escritorio.add(ap);
            ap.setVisible(true);
            try {
                ap.setMaximum(true);
            } catch (java.beans.PropertyVetoException ex) {}
        });
        add(btnGestionarApartados);
        
        //BOTON PARA EL INVENTARIO
        btnGestionarProductos = new JButton("Gestionar Inventario");
        btnGestionarProductos.setBounds(40, 240, 200, 50);
        btnGestionarProductos.setBackground(new Color(230, 227, 227)); 
        btnGestionarProductos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGestionarProductos.addActionListener(e -> {
            FrmInventario inv = new FrmInventario(); 
            escritorio.add(inv);
            inv.setVisible(true);
        });
        add(btnGestionarProductos);
        
        /*/BOTON DE CONFIGURACION
        btnConfiguracion = new JButton("Configuración");
        btnConfiguracion.setBounds(40, 310, 200, 50);
        btnConfiguracion.setBackground(new Color(230, 227, 227)); 
        btnConfiguracion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConfiguracion.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Módulo de Configuración en desarrollo.\nPróximamente: Gestión de Usuarios.");
        });
        add(btnConfiguracion);//aun no hace nada 
        */
        
        //Boton para cerrar la sesion
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(700, 20, 150, 30);
        btnCerrarSesion.addActionListener(e -> {
            this.dispose();//cerrar
            new FrmLogin().setVisible(true); //volver al Login
        });
        add(btnCerrarSesion);
        
        //estado de la sesion 
        lblAviso = new JLabel("");
        lblAviso.setBounds(30, 500, 400, 40);
        lblAviso.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        add(lblAviso);
        
    }
    public void aplicarPermisos(){
        if (rolUsuario != null && rolUsuario.equalsIgnoreCase("Vendedor")) {
            //Sprint semana 1 --> botones limitados por ahora
            btnGestionarProductos.setEnabled(false);//se desabilta esta opcion
            btnConfiguracion.setVisible(false);
            
            lblAviso.setText("Sesión: Vendedor - Acceso Limitado");
            lblAviso.setForeground(java.awt.Color.RED);
         
        }else{
            lblAviso.setText("Sesión: Administrador - Acceso Total");
            lblAviso.setForeground(new java.awt.Color(34, 139, 34));
        }
    }
}
