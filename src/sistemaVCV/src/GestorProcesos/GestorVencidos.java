package GestorProcesos;

import java.awt.*;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import moduloLogica.Apartado;
import moduloConexion.ApartadoDAO;
import moduloConexion.VencimientosDAO;
import moduloConexion.AbonoDAO;

/**
 *
 * @author Danna Padilla
 */
public class GestorVencidos {
    
    private VencimientosDAO daoVencidos = new VencimientosDAO();
    private AbonoDAO daoAbono = new AbonoDAO();
    
    public void actualizarTablaVencidos(JTable tblVencidos, JLabel lblAlerta) {
        //definir columnas
        String[] columnas = {"ID", "SKU", "Cliente", "Teléfono", "Venció el", "ID_PROD", "Saldo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        List<Apartado> lista = daoVencidos.consultarApartadosVencidos();

        tblVencidos.setModel(modelo);
        tblVencidos.getColumnModel().getColumn(3).setMinWidth(0);
        tblVencidos.getColumnModel().getColumn(3).setMaxWidth(0);
        tblVencidos.getColumnModel().getColumn(3).setPreferredWidth(0);

        
        for (Apartado a : lista) {
            
            modelo.addRow(new Object[]{
                a.getIdApartado(),     // Columna 0
            a.getSku(),            // Columna 1 (¡Ahora visible!)
            a.getNombreCliente(),  // Columna 2
            a.getTelefono(),       // Columna 3
            a.getFechaLimite().toString().substring(0, 10), // Columna 4 (Solo fecha)
            a.getIdProducto(),     // Columna 5 (Se ocultará)
            String.format("%.2f", a.getTotalPrenda())         
            });
        }
        tblVencidos.setModel(modelo);
        
        tblVencidos.getColumnModel().getColumn(5).setMinWidth(0);
        tblVencidos.getColumnModel().getColumn(5).setMaxWidth(0);
        tblVencidos.getColumnModel().getColumn(5).setPreferredWidth(0);
        tblVencidos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblVencidos.getColumnModel().getColumn(0).setMaxWidth(60);
        tblVencidos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblVencidos.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        if (!lista.isEmpty()) {
            lblAlerta.setText("Hay " + lista.size() + " apartados caducados.");
            lblAlerta.setForeground(Color.RED);
        }else {
            lblAlerta.setText("NO hay apartados vencidos.");
            lblAlerta.setForeground(new Color(0, 153, 51));

        }
    }
    //cambiar estado del apartado de PENDIENTE a VENCIDO
    //Incrementar el stock +1 en la tabla prodcuto para regresar el prodcuto a stock
    
    public void procesarLiberacion(int idApartado, int idProducto, Component padre, JTable tbl, JLabel lbl) {
        int confirmar = JOptionPane.showConfirmDialog (padre, 
                "¿Confirmar liberación de Producto?\nEsto devolverá la prenda al Inventario y el cliente perderá su abono.",
                "Liberar Stock", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            if (daoVencidos.liberarProductoVencido(idApartado, idProducto)) {
                JOptionPane.showMessageDialog(padre,"Producto liberado! Stock Actualizado.");
                actualizarTablaVencidos(tbl, lbl);
            }else {
                JOptionPane.showMessageDialog(padre, "Error al liberar Producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //liquidar el monto restabre de un apartado vencido
    public void procesarLiquidacionVencido (int idApartado, double saldo, Component padre, JTable tbl, JLabel lbl) {
        int confirmar = JOptionPane.showConfirmDialog(padre, 
                "¿Desea liquidar el saldo pendiente de $" + saldo + "?\nEl estado cambiará a 'Liquidado'.",
                "Liquidación Extemporánea", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            // reutilzamos metodo de AbonoDAO
            
            if (daoAbono.registrarAbonoYLiquidacion(idApartado, saldo)) {
                JOptionPane.showMessageDialog(padre, "¡Venta completada con éxito!");
                actualizarTablaVencidos(tbl, lbl);
            } else {
                JOptionPane.showMessageDialog(padre, "Error al procesar el pago final.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
