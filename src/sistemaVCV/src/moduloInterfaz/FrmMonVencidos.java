package moduloInterfaz;

import GestorProcesos.GestorVencidos;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 * Alerta de color
 * Boton para regresar articulo al inventario --> Cancelar el Apartado
 * Boton para Liquidar el apartado 
 * @author Danna Paola
 */
public class FrmMonVencidos extends javax.swing.JInternalFrame {
    private GestorVencidos gestor = new GestorVencidos();
    private JTable tblVencidos;
    private JLabel lblAlerta;
    private JButton btnLiberar, btnLiquidar, btnRefrescar;
    
    public FrmMonVencidos() {
        super("Monitor de Vencimientos - AtelierOS", true, true, true, true);
        iniciarComponentes();
        gestor.actualizarTablaVencidos(tblVencidos, lblAlerta);
        this.setSize(900, 500);
    }
    
    private void iniciarComponentes() {
        this.setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        
        //PANEL ALERTAS
        JPanel pnlSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblAlerta = new JLabel("Verificando vencimientos...");
        lblAlerta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlSuperior.add(lblAlerta);
        
        //PANEL CENTRAL
        tblVencidos = new JTable();
        tblVencidos.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tblVencidos);
        
        //PANEL INFERIOR
        JPanel pnlInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        
        btnRefrescar = new JButton("Actualizar");
        btnRefrescar.addActionListener(e -> gestor.actualizarTablaVencidos(tblVencidos, lblAlerta));
        
        btnLiquidar = new JButton("Liquidar");
        btnLiquidar.setBackground(new Color(46, 204, 113)); //verde
        btnLiquidar.setForeground(Color.WHITE);
        btnLiquidar.addActionListener(e -> btnLiquidar());
        
        btnLiberar = new JButton("Regresar a Inventario");
        btnLiberar.setBackground(new Color(231, 76, 60)); //rojo
        btnLiberar.setForeground(Color.WHITE);
        btnLiberar.addActionListener(e -> btnLiberar());
        
        pnlInferior.add(btnRefrescar);
        pnlInferior.add(new JSeparator(JSeparator.VERTICAL));
        pnlInferior.add(btnLiquidar);
        pnlInferior.add(btnLiberar);
        
        this.add(pnlSuperior, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        this.add(pnlInferior, BorderLayout.SOUTH);
    }
    
    private void btnLiberar (){
        int fila = tblVencidos.getSelectedRow();
        if (fila != -1) {
            int idAp = (int) tblVencidos.getValueAt(fila, 0);
            int idProd = (int) tblVencidos.getValueAt(fila, 5);
            
            gestor.procesarLiberacion(idAp, idProd, this, tblVencidos, lblAlerta);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un apartado para liberar.");
        }
    }
    
    private void btnLiquidar() {
        int fila = tblVencidos.getSelectedRow();

        if (fila != -1) {
            try {
                int idAp = (int) tblVencidos.getValueAt(fila, 0);
                String cliente = tblVencidos.getValueAt(fila, 2).toString();

                String saldoStr = tblVencidos.getValueAt(fila, 6).toString();
                double saldo = Double.parseDouble(saldoStr);

                if (saldo <= 0) {
                    JOptionPane.showMessageDialog(this, "Este apartado ya no tiene saldo pendiente.");
                    return;
                }
                
                gestor.procesarLiquidacionVencido(idAp, saldo, this, tblVencidos, lblAlerta);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el saldo: formato no válido.");
            } catch (Exception e) {
                System.err.println("Error en liquidación: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un apartado vencido de la lista.");
        }
    }
    
    public void actualizarDatos() {
        gestor.actualizarTablaVencidos(tblVencidos, lblAlerta);
    }
}
