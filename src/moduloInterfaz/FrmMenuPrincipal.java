package moduloInterfaz;

import java.awt.Font;
import javax.swing.*;

/**
 *
 * @author Danna Paola
 */
public class FrmMenuPrincipal extends JFrame{
    private String rolUsuario;
    //botones
    private JButton btnGestionarProductos;
    private JButton btnConfiguracion;
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
        //Botones
        btnGestionarProductos = new JButton("Gestionar Inventario");
        btnGestionarProductos.setBounds(50, 100, 200, 40);
        add(btnGestionarProductos);
        
        //estado de la sesion 
        lblAviso = new JLabel("");
        lblAviso.setBounds(30, 520, 400, 40);
        lblAviso.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        add(lblAviso);
        
    }
    public void aplicarPermisos(){
        if (rolUsuario.equalsIgnoreCase("Vendedor")) {
            //Sprint semana 1 --> botones limitados por ahora
            btnGestionarProductos.setEnabled(false);//se desabilta esta opción
            btnConfiguracion.setVisible(false);
            lblAviso.setText("Ingreso: Vendedor - Acceso Limitado");
         
        }else{
            lblAviso.setText("Ingreso, Administrador - Acceso Total");
        }
    }
}
