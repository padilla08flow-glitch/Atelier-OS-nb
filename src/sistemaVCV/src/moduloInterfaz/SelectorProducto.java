package moduloInterfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import moduloLogica.Producto;


/**
 *
 * @author Danna Paola
 */
public class SelectorProducto extends JDialog{
    
    private Producto productoSeleccionado = null;
    private JTable tablaSelector;
    private DefaultTableModel modeloSelector;
    
    public SelectorProducto(Frame parent, List<Producto> coincidencias) {
        super(parent, "Seleccionar Producto", true);
        setLayout(new BorderLayout(10, 10));
        iniciarComponentes(coincidencias);
        
        setSize(700, 350);
        setLocationRelativeTo(parent);
    }
    
    private void iniciarComponentes (List<Producto> coincidencias) {
        String[] columnas = {"SKU", "Modelo", "Talla", "Material", "Color", "Stock", "Precio"};
        modeloSelector = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        for (Producto p : coincidencias) {
            modeloSelector.addRow(new Object[]{
                p.getSku(), p.getNombre(), p.getTalla(), 
                p.getMaterial(), p.getColor(), p.getStock(), p.getPrecio()
            });
        }
        tablaSelector = new JTable(modeloSelector);
        tablaSelector.setRowHeight(25);
        tablaSelector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
        //doble click
        tablaSelector.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaSelector.getSelectedRow();
                    if (fila != -1) {
                        productoSeleccionado = coincidencias.get(fila);
                        dispose();
                    }
                }
            }
        });
        
        //diseño
        add(new JLabel("  Seleccione el producto deseado con doble clic:"), BorderLayout.NORTH);
        add(new JScrollPane(tablaSelector), BorderLayout.CENTER);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.add(btnCancelar);
        add(pnlBotones, BorderLayout.SOUTH);
    }
    
    public Producto getProductoSeleccionado(){
        return productoSeleccionado;
    }
}
