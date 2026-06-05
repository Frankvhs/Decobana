package com.decobana.ui.panels;

import com.decobana.dao.CategoriaServicioProductoDAO;
import com.decobana.dao.ProveedorDAO;
import com.decobana.dao.ServicioProductoDAO;
import com.decobana.model.CategoriaServicioProducto;
import com.decobana.model.Proveedor;
import com.decobana.model.ServicioProducto;
import com.decobana.ui.dialogs.ServicioProductoDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ServicioProductoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ServicioProductoDAO dao = new ServicioProductoDAO();
    private CategoriaServicioProductoDAO catDAO = new CategoriaServicioProductoDAO();
    private ProveedorDAO provDAO = new ProveedorDAO();
    private JButton btnAgregar, btnEditar, btnEliminar;

    public ServicioProductoPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Código","Nombre","Categoría","Precio","Tercero","Activo"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnPanel.add(btnAgregar);
        btnPanel.add(btnEditar);
        btnPanel.add(btnEliminar);
        add(btnPanel, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarTabla();
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            List<ServicioProducto> lista = dao.findAll();
            for (ServicioProducto s : lista) {
                String categoria = "";
                try {
                    CategoriaServicioProducto cat = catDAO.findById(s.getIdCategoria());
                    categoria = (cat != null) ? cat.getNombre() : "?";
                } catch (Exception e) {}
                tableModel.addRow(new Object[]{
                        s.getCodServicio(), s.getNombre(), categoria,
                        s.getPrecioUnitario(), s.isEsTercero() ? "Sí" : "No",
                        s.isEstadoActivo() ? "Activo" : "Inactivo"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar servicios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregar() {
        ServicioProductoDialog dialog = new ServicioProductoDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getServicio());
                cargarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio");
            return;
        }
        String cod = (String) tableModel.getValueAt(row, 0);
        try {
            ServicioProducto s = dao.findByCod(cod);
            ServicioProductoDialog dialog = new ServicioProductoDialog((JFrame) SwingUtilities.getWindowAncestor(this), s);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getServicio());
                cargarTabla();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio");
            return;
        }
        String cod = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar servicio?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.delete(cod);
                cargarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}