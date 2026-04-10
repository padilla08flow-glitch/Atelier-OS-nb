package moduloInterfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import moduloLogica.Cliente;

/**
 * seleccionar un cliente de la base de datos
 * @author Danna Padilla
 */
public class SelectorCliente extends JDialog {

    private Cliente clienteSeleccionado = null;
    private JTable tabla;
    private DefaultTableModel modelo;

    public SelectorCliente(Frame parent, List<Cliente> lista) {
        super(parent, "Seleccionar Cliente", true);
        setLayout(new BorderLayout(10, 10));
        
        String[] columnas = {"ID", "Nombre Completo", "Teléfono"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Cliente c : lista) {
            modelo.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getTelefono()});
        }

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tabla.getSelectedRow();
                    if (fila != -1) {
                        clienteSeleccionado = lista.get(fila);
                        dispose();
                    }
                }
            }
        });

        add(new JLabel("  Doble clic para seleccionar al cliente:"), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar, BorderLayout.SOUTH);

        setSize(500, 300);
        setLocationRelativeTo(parent);
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }
}