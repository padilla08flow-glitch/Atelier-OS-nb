package moduloInterfaz;
import java.awt.Font;
import javax.swing.*;

/**
 *
 * @author Danna Padilla
 */
public class FrmMenuPrincipal extends JFrame{
    private String rolUsuario;
    //botones
    private JButton btnGestionarProductos;
    private JButton btnNuevaVenta;
    private JButton btnConfiguracion;
    private JButton btnCerrarSesion;
    private JLabel lblAviso;
        
    public FrmMenuPrincipal (String rol){
        this.rolUsuario = rol;
        
        setTitle("AtelierOS - Casa Victoria");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        iniciarComponentes();
        aplicarPermisos();
    }
    
    //metodos
    private void iniciarComponentes(){
        //parte superior --> Titulo 
        JLabel lblTitulo = new JLabel("PANEL DE CONTROL - ATELIER OS");
        lblTitulo.setBounds(30, 20, 500, 30);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitulo);
        
        //Boton para nueva Venta
        btnNuevaVenta = new JButton("Nueva Venta");
        btnNuevaVenta.setBounds(50, 100, 200,40);
        btnNuevaVenta.addActionListener(e -> {
            new FrmVenta().setVisible(true);
        });
        add(btnNuevaVenta);
        
        //Botones --> gestionar el Inventario
        btnGestionarProductos = new JButton("Gestionar Inventario");
        btnGestionarProductos.setBounds(50, 100, 200, 40);
        btnGestionarProductos.addActionListener(e -> {
            new FrmInventario().setVisible(true);
        });
        add(btnGestionarProductos);
        
        // --- BOTÓN GESTIONAR INVENTARIO (EL QUE FALTABA) ---
        btnGestionarProductos = new JButton("Gestionar Inventario");
        btnGestionarProductos.setBounds(50, 160, 200, 40);
        btnGestionarProductos.addActionListener(e -> {
            FrmInventario inv = new FrmInventario(); 
            inv.setVisible(true);
        });
        add(btnGestionarProductos);
        
        //Boton de configuracion
        btnConfiguracion = new JButton("Configuración");
        btnConfiguracion.setBounds(50, 160, 200, 40);
        add(btnConfiguracion);//aun no hace nada //YA
        
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
