package com.decobana.ui.panels;

import com.decobana.dao.ProveedorDAO;
import com.decobana.model.Proveedor;
import com.decobana.ui.dialogs.ProveedorDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProveedorPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProveedorDAO dao = new ProveedorDAO();
    private JButton btnAgregar, btnEditar, btnEliminar;

    public ProveedorPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID","Nombre","Tipo Servicio","Teléfono","Email","Responsable"}, 0);
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
            List<Proveedor> lista = dao.findAll();
            for (Proveedor p : lista) {
                tableModel.addRow(new Object[]{
                        p.getIdProveedor(), p.getNombre(), p.getTipoServicio(),
                        p.getTelefono(), p.getEmail(), p.getResponsable()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregar() {
        ProveedorDialog dialog = new ProveedorDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getProveedor());
                cargarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Proveedor p = dao.findById(id);
            ProveedorDialog dialog = new ProveedorDialog((JFrame) SwingUtilities.getWindowAncestor(this), p);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getProveedor());
                cargarTabla();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.delete(id);
                cargarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}