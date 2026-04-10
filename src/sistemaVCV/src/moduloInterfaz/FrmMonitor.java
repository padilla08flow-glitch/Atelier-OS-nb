package moduloInterfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import GestorProcesos.GestorVencidos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * hacer clic en liberar
 * monitoreo de apartados Vencidos
 * @author Danna Padilla
 */
public class FrmMonitor extends javax.swing.JInternalFrame {
    
/*/instanciamiento de la clase
    private GestorVencidos gestor = new GestorVencidos();
    private JTable tblVencidos;
    private JLabel lblAlerta;
    private JButton btnLiberar;
    private JScrollPane jScrollPane1;

    public FrmMonitor() {
        super("Monitor de Vencimientos - AtelierOS", true, true, true, true);
        this.setLayout(new BorderLayout());
        initComponent();
        gestor.actualizarTablaVencidos(tblVencidos, lblAlerta);
        this.setSize(800, 500);
    }
    
    private void initComponent() {
        //instanciar los objetos --> ya
        tblVencidos = new JTable();
        tblVencidos.setFillsViewportHeight(true);
        
        jScrollPane1 = new JScrollPane(tblVencidos);
        lblAlerta = new JLabel("Estado: Buscando vencidos...");
        lblAlerta.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        btnLiberar = new JButton("Liberar Producto");
        btnLiberar.setBackground(new Color(255, 102, 102));
        btnLiberar.setForeground(Color.WHITE);
        btnLiberar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        //diseño basicon
        this.add(jScrollPane1, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panelInferior.add(lblAlerta, BorderLayout.WEST);
        panelInferior.add(btnLiberar, BorderLayout.EAST);
        
        this.add(panelInferior, BorderLayout.SOUTH);
        
        //conectar con el boton
        btnLiberar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLiberarActionPerformed(e);
            }
        });
    }
    
    private void btnLiberarActionPerformed(java.awt.event.ActionEvent evt) {
        //obtener la fila
        int fila = tblVencidos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un apartado de la tabla.");
            return;
        }
        try {
            //obtener id para la base de datos
            int idApartado = Integer.parseInt(tblVencidos.getValueAt(fila, 0).toString());
            int idProducto = obtenerIdProductoDeFila(fila);
            
            //liberacion y refrescar tabla
            gestor.procesarLiberacion(idApartado, idProducto, this, tblVencidos, lblAlerta);
            gestor.actualizarTablaVencidos(tblVencidos, lblAlerta);
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos: "+e.getMessage());
        }
    }
    
    private int obtenerIdProductoDeFila(int fila){
        return Integer.parseInt(tblVencidos.getValueAt(fila, 3).toString());
    }
    */
}
    
    
    
