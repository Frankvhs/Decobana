package com.decobana.ui.panels;

import com.decobana.dao.CategoriaServicioProductoDAO;
import com.decobana.dao.ServicioProductoDAO;
import com.decobana.model.CategoriaServicioProducto;
import com.decobana.model.ServicioProducto;
import com.decobana.ui.dialogs.ServicioProductoDialog;
import com.decobana.ui.utils.UIUtils;

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

    public void cargarTabla() {
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
            UIUtils.handleDatabaseException(this, ex, "Error al cargar servicios");
        }
    }

    private void agregar() {
        ServicioProductoDialog dialog = new ServicioProductoDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getEntity());
                cargarTabla();
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al insertar servicio");
            }
        }
    }

    private void editar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un servicio");
            return;
        }
        String cod = (String) tableModel.getValueAt(row, 0);
        try {
            ServicioProducto s = dao.findByCod(cod);
            ServicioProductoDialog dialog = new ServicioProductoDialog((JFrame) SwingUtilities.getWindowAncestor(this), s);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getEntity());
                cargarTabla();
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al editar servicio");
        }
    }

    private void eliminar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un servicio");
            return;
        }
        String cod = (String) tableModel.getValueAt(row, 0);
        if (UIUtils.showConfirm(this, "¿Eliminar servicio?", "Confirmar")) {
            try {
                dao.delete(cod);
                cargarTabla();
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al eliminar servicio");
            }
        }
    }
}