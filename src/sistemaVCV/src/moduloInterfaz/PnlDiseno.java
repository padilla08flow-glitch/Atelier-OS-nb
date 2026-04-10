package moduloInterfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;
import moduloConexion.DesignDAO;
import moduloLogica.Design;

public class PnlDiseno extends JPanel {
    private JTable tablaDisenos;
    private DefaultTableModel modelo;
    private DesignDAO dDao = new DesignDAO();

    public PnlDiseno() {
        setLayout(new BorderLayout(10, 10));
        iniciarComponentes();
        cargarDatos();
    }

    private void iniciarComponentes() {
        // --- PANEL DE ACCIONES
        JPanel pnlAcciones = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlAcciones.setBorder(BorderFactory.createTitledBorder("Gestión de Catálogo"));
        pnlAcciones.setPreferredSize(new Dimension(200, 0));
        
        JButton btnNuevo = new JButton("Nuevo Diseño");
        JButton btnEditar = new JButton("Editar Seleccionado");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");

        // Estilo para eliminar
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);

        // Eventos
        btnNuevo.addActionListener(e -> abrirRegistroNuevo());
        btnEditar.addActionListener(e -> abrirEditor());
        btnEliminar.addActionListener(e -> eliminarRegistro());

        pnlAcciones.add(btnNuevo);
        pnlAcciones.add(btnEditar);
        pnlAcciones.add(btnEliminar);

        add(pnlAcciones, BorderLayout.WEST);

        // --- TABLA DE DISEÑOS---
        modelo = new DefaultTableModel(new Object[]{"ID", "Modelo", "Material", "Técnica", "Color", "Imagen"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaDisenos = new JTable(modelo);
        tablaDisenos.setRowHeight(25);
        tablaDisenos.getTableHeader().setReorderingAllowed(false);
        
        configurarAnchoColumnas();
        
        tablaDisenos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) abrirEditor();
            }
        });

        add(new JScrollPane(tablaDisenos), BorderLayout.CENTER);
    }

    private void configurarAnchoColumnas() {
        int[] anchos = {40, 200, 100, 150, 100, 70};
        for (int i = 0; i < tablaDisenos.getColumnCount(); i++) {
            TableColumn columna = tablaDisenos.getColumnModel().getColumn(i);
            columna.setPreferredWidth(anchos[i]);
            
            if (i == 0) {
                columna.setMaxWidth(60);
                columna.setMinWidth(30);
            }
        }
    }
    private void abrirRegistroNuevo() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        RegistroDesign nuevo = new RegistroDesign((Frame) parentWindow, true, null);
        nuevo.setVisible(true);

        if (nuevo.isGuardadoExitoso()) {
            cargarDatos();
        }
    }

    public void cargarDatos() {
        modelo.setRowCount(0);
        List<Design> lista = dDao.listarTodos();
        for (Design d : lista) {
            // Verificar si tiene imagen
            String tieneImg = (d.getRutaImagen() == null || d.getRutaImagen().equals("default.png")) ? "❌ No" : "✅ Sí";
            
            modelo.addRow(new Object[]{
                d.getIdDesign(),
                d.getNombreModelo(),
                d.getMaterial(),
                d.getTecnica(),
                d.getColor(),
                tieneImg
            });
        }
    }

    private void abrirEditor() {
        int fila = tablaDisenos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un diseño para editar");
            return;
        }
        //ID de la tabla
        int id = (int) modelo.getValueAt(fila, 0);
        
        Design seleccionado = null;
        for(Design d : dDao.listarTodos()) {
            if(d.getIdDesign() == id) {
                seleccionado = d;
                break;
            }
        }

        if (seleccionado != null) {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            RegistroDesign editor = new RegistroDesign((Frame) parentWindow, true, seleccionado);
            editor.setVisible(true);

            if (editor.isGuardadoExitoso()) {
                cargarDatos();
            }
        }
    }

    private void eliminarRegistro() {
        int fila = tablaDisenos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un diseño de la tabla.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = modelo.getValueAt(fila, 1).toString();

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro de eliminar el diseño: " + nombre + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            if (dDao.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Diseño eliminado correctamente.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar porque tiene stock vinculado.",
                        "Error de integridad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}