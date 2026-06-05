package com.decobana.ui.panels;

import com.decobana.dao.EmpleadoDAO;
import com.decobana.model.Empleado;
import com.decobana.ui.dialogs.EmpleadoDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private EmpleadoDAO dao = new EmpleadoDAO();
    private JButton btnAgregar, btnEditar, btnEliminar;

    public EmpleadoPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID","Nombre","Apellidos","Documento","Cargo","Departamento"}, 0);
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
            List<Empleado> lista = dao.findAll();
            for (Empleado e : lista) {
                tableModel.addRow(new Object[]{
                        e.getIdEmpleado(), e.getNombre(), e.getApellidos(),
                        e.getNumDocumento(), e.getCargo(), e.getDepartamento()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregar() {
        EmpleadoDialog dialog = new EmpleadoDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getEmpleado());
                cargarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Empleado e = dao.findById(id);
            EmpleadoDialog dialog = new EmpleadoDialog((JFrame) SwingUtilities.getWindowAncestor(this), e);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getEmpleado());
                cargarTabla();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar empleado?", "Confirmar", JOptionPane.YES_NO_OPTION);
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