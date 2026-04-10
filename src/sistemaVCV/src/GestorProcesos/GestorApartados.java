package GestorProcesos;

import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import moduloConexion.ApartadoDAO;
import moduloConexion.AbonoDAO;
import moduloValidaciones.ValidadorApartado;
import moduloLogica.AbonoApartado;
import moduloLogica.Apartado;

/**
 *
 * @author Danna Padilla
 */
public class GestorApartados{
    
    private ApartadoDAO daoApartado = new ApartadoDAO();
    private AbonoDAO daoAbono = new AbonoDAO();
    
    public void refreshTablaPrincipal(JTable tabla) { //AAAA ERROR MAS TONTO
        String[] columnas = {"ID", "Cliente", "Teléfono", "Producto", "Total", "Abonado", "Saldo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column){ return false;}
        };
        
        List<Object[]> datos = daoApartado.consultarResumen();
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        ajustarDimensionesColumnas(tabla);
    }
    
    //Procesar el abono --> interfaz
    public void abonarApartado(int filaSeleccionada, JTable tabla, Component padre) {
        int idApartado = Integer.parseInt(tabla.getValueAt(filaSeleccionada, 0).toString());
        
        double saldoPendiente = Double.parseDouble(tabla.getValueAt(filaSeleccionada, 6).toString());
        String estado = tabla.getValueAt(filaSeleccionada, 7).toString();
        
        //Reglas de negocio
        if (estado.equalsIgnoreCase("Liquidado") || estado.equalsIgnoreCase("Vencido")) {
            JOptionPane.showMessageDialog(padre, "No se pueden agregar abonos a un apartado " + estado);
            return;
        }
        //cuando se pide el monto
        String montoStr = JOptionPane.showInputDialog(padre, 
            "Saldo actual: $" + saldoPendiente + "\n¿Cuánto desea abonar?");
        
        if (montoStr == null) return;
        
        //validar
        if (ValidadorApartado.esAbonoValido(montoStr, saldoPendiente, padre)) {
            double monto = Double.parseDouble(montoStr);
            
            if (daoAbono.registrarAbonoYLiquidacion(idApartado, monto)) {
                JOptionPane.showMessageDialog(padre, "¡Abono registrado!");
                refreshTablaPrincipal(tabla);
            } else {
                JOptionPane.showMessageDialog(padre, "Error al procesar el abono.");
            }
        }
    }

    public void filtrarTabla(String texto, JTable tabla) {
        String[] columnas = {"ID", "Cliente", "Producto", "Total", "Abonado", "Saldo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Llamamos metodo del DAO
        List<Object[]> datos = daoApartado.consultarResumenConFiltro(texto);
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
    }
    
    private void ajustarDimensionesColumnas(JTable tabla) {
        int[] anchos = {40, 180, 100, 150, 80, 80, 80, 90};

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

}
