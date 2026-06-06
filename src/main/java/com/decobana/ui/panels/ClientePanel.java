package com.decobana.ui.panels;

import com.decobana.dao.ClienteDAO;
import com.decobana.model.Cliente;
import com.decobana.ui.dialogs.ClienteDialog;
import com.decobana.ui.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClientePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ClienteDAO dao = new ClienteDAO();
    private JButton btnAgregar, btnEditar, btnEliminar;

    public ClientePanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID","Nombre","Apellidos","Documento","Teléfono","Email","Trato Pref."}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarTabla();
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            List<Cliente> clientes = dao.findAll();
            for (Cliente c : clientes) {
                tableModel.addRow(new Object[]{
                        c.getIdCliente(), c.getNombre(), c.getApellidos(),
                        c.getNumDocumento(), c.getTelefono(), c.getEmail(),
                        c.isTratoPref() ? "Sí" : "No"
                });
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al cargar clientes");
        }
    }

    private void agregar() {
        ClienteDialog dialog = new ClienteDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getEntity());
                cargarTabla();
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al insertar cliente");
            }
        }
    }

    private void editar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un cliente");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Cliente c = dao.findById(id);
            ClienteDialog dialog = new ClienteDialog((JFrame) SwingUtilities.getWindowAncestor(this), c);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getEntity());
                cargarTabla();
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al editar cliente");
        }
    }

    private void eliminar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un cliente");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        if (UIUtils.showConfirm(this, "¿Eliminar cliente?", "Confirmar")) {
            try {
                dao.delete(id);
                cargarTabla();
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al eliminar cliente");
            }
        }
    }
}