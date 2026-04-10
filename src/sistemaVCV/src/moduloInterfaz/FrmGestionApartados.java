package moduloInterfaz;

import GestorProcesos.GestorApartados;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author Danna Padilla
 */
public class FrmGestionApartados extends javax.swing.JInternalFrame {
    /*
    private GestorApartados gestor = new GestorApartados();
    private JTable tblGestion;
    private JButton btnAbonar;
    private JTextField txtBuscador;
    private JScrollPane jScrollPane1;

    public FrmGestionApartados() {
        super("Gestión de Apartados - AtelierOS", true, true, true, true);
        iniciarComponentes();
        gestor.refreshTablaPrincipal(tblGestion);
        this.setSize(850, 500);
    }
    
    private void iniciarComponentes() {
        tblGestion = new JTable();
        jScrollPane1 = new JScrollPane(tblGestion);
        btnAbonar = new JButton("Registrar Abono");
        txtBuscador = new JTextField(20);
        JLabel lblBusqueda = new JLabel("Buscar Cliente:");
        
        //botones
        btnAbonar.setBackground(new Color(51, 153, 255));
        btnAbonar.setForeground(Color.WHITE);
        btnAbonar.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        //Panel Superior - Busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(lblBusqueda);
        panelBusqueda.add(txtBuscador);
        
        //panel inferior - Acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.add(btnAbonar);
        
        this.setLayout(new BorderLayout(10, 10));
        this.add(panelBusqueda, BorderLayout.NORTH);
        this.add(jScrollPane1, BorderLayout.CENTER);
        this.add(panelAcciones, BorderLayout.SOUTH);
        
        //eventos
        //boton - Abonar
        btnAbonar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnAbonarActionPerformed(evt);
            }
        });
        
        txtBuscador.addKeyListener(new KeyAdapter() {
            //@Override
            public void keyReleased(KeyEvent evt){
                gestor.filtrarTabla(txtBuscador.getText(), tblGestion);            
            }
        });
    }
    
    private void btnAbonarActionPerformed(ActionEvent evt) {
        int fila = tblGestion.getSelectedRow();
        if (fila != -1) {
            gestor.abonarApartado(fila, tblGestion, this);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un apartado de la tabla.");
        }
    }
*/
}
