package moduloInterfaz;
import javax.swing.*;
import java.awt.*;
import moduloConexion.Conexion;
import moduloConexion.UsuarioDAO;
/**
 *
 * @author Danna Paola
 */
public class FrmLogin extends JFrame{
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    
    public FrmLogin(){
        setTitle("AtelierOS - Acceso");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);//centrar la ventana
        
        iniciarComponentes();
    }
    private void iniciarComponentes(){
        JLabel lblTitulo = new JLabel("Iniciar Sesion", SwingConstants.CENTER);
        lblTitulo.setBounds(50, 40, 300, 40);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitulo);
        
        txtUsuario = new JTextField();
        //mostrar u ocultar el texto 
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingrese su nombre de usuario");
        //borrar el texto
        txtUsuario.putClientProperty("JTextField.showClearButton", true);
        txtUsuario.setBounds(50, 150, 300, 40);
        txtUsuario.setBorder(BorderFactory.createTitledBorder("Usuario"));
        add(txtUsuario);
        
        txtPassword = new JPasswordField(); 
        //ver contraeña
        txtPassword.putClientProperty("JTextField.placeholderText", "Ingrese su contraseña");
        txtPassword.putClientProperty("JTextField.showRevealButton", true);
        txtPassword.setBounds(50, 220, 300, 40); 
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        add(txtPassword);
        
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(50, 320, 300, 50);
        btnIngresar.putClientProperty("JButton.buttonType", "roundRect");//resaltar el boton
        btnIngresar.setBackground(new Color(40, 116, 166));//azul basicon
        btnIngresar.setForeground(Color.WHITE);//
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnIngresar.addActionListener(e -> {
            String user = txtUsuario.getText();
            String pass = new String(txtPassword.getPassword());
            
            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa los campos de texto.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            UsuarioDAO dao = new UsuarioDAO();
            String rol = dao.validarIngreso(user, pass);
            
            if(rol != null){
                JOptionPane.showMessageDialog(this, "Bienvenid@ " + user + "\nRol: "+rol);
                FrmMenuPrincipal menu = new FrmMenuPrincipal(rol);
                menu.setVisible(true);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this,"Error. Credenciales incorrectas o Usuario inactivo", "Error de Ingreso", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnIngresar);
        this.getRootPane().setDefaultButton(btnIngresar);
        
    }
}
