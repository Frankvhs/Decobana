package com.decobana.ui.panels;

import com.decobana.dao.ContratoDAO;
import com.decobana.model.*;
import com.decobana.ui.dialogs.ContratoDialog;
import com.decobana.ui.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ContratoPanel extends JPanel {
    private JTable tableContratos;
    private DefaultTableModel contratosModel;
    private ContratoDAO dao = new ContratoDAO();

    private JTable tableLineas, tablePagos, tableModificaciones;
    private DefaultTableModel lineasModel, pagosModel, modsModel;

    private JTabbedPane subTabPane;
    private JButton btnAgregar, btnEditar, btnEliminar;

    private Contrato contratoSeleccionado;

    public ContratoPanel() {
        setLayout(new BorderLayout());

        contratosModel = new DefaultTableModel(new Object[]{"Nº Contrato","Fecha","Cliente","Evento"}, 0);
        tableContratos = new JTable(contratosModel);
        tableContratos.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
        JScrollPane scrollContratos = new JScrollPane(tableContratos);
        scrollContratos.setPreferredSize(new Dimension(600, 200));
        add(scrollContratos, BorderLayout.NORTH);

        subTabPane = new JTabbedPane();
        lineasModel = new DefaultTableModel(new Object[]{"Cód. Servicio","Cantidad","Precio Negociado"}, 0);
        tableLineas = new JTable(lineasModel);
        subTabPane.addTab("Líneas", new JScrollPane(tableLineas));

        pagosModel = new DefaultTableModel(new Object[]{"Fecha Pago","Monto"}, 0);
        tablePagos = new JTable(pagosModel);
        subTabPane.addTab("Pagos", new JScrollPane(tablePagos));

        modsModel = new DefaultTableModel(new Object[]{"Fecha","Descripción"}, 0);
        tableModificaciones = new JTable(modsModel);
        subTabPane.addTab("Modificaciones", new JScrollPane(tableModificaciones));

        add(subTabPane, BorderLayout.CENTER);

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

        cargarContratos();
    }

    public void cargarContratos() {
        contratosModel.setRowCount(0);
        try {
            List<Contrato> contratos = dao.findAll();
            for (Contrato c : contratos) {
                contratosModel.addRow(new Object[]{
                        c.getNumContrato(), c.getFechaContrato().toString(),
                        c.getIdCliente(), c.getIdEvento()
                });
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al cargar contratos");
        }
    }

    private void mostrarDetalles() {
        int row = tableContratos.getSelectedRow();
        if (row == -1) {
            limpiarDetalles();
            contratoSeleccionado = null;
            return;
        }
        String num = (String) contratosModel.getValueAt(row, 0);
        try {
            contratoSeleccionado = dao.findByNum(num);
            cargarDetalles(contratoSeleccionado);
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al cargar detalles del contrato");
        }
    }

    private void cargarDetalles(Contrato c) {
        lineasModel.setRowCount(0);
        if (c.getLineas() != null) {
            for (ContratoLinea l : c.getLineas()) {
                lineasModel.addRow(new Object[]{l.getCodServicio(), l.getCantidad(), l.getPrecioNegociado()});
            }
        }
        pagosModel.setRowCount(0);
        if (c.getPagos() != null) {
            for (Pago p : c.getPagos()) {
                pagosModel.addRow(new Object[]{p.getFechaPago().toString(), p.getMonto()});
            }
        }
        modsModel.setRowCount(0);
        if (c.getModificaciones() != null) {
            for (ModificacionContrato m : c.getModificaciones()) {
                modsModel.addRow(new Object[]{m.getFechaMod().toString(), m.getDescripcion()});
            }
        }
    }

    private void limpiarDetalles() {
        lineasModel.setRowCount(0);
        pagosModel.setRowCount(0);
        modsModel.setRowCount(0);
    }

    private void agregar() {
        ContratoDialog dialog = new ContratoDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getContrato());
                cargarContratos();
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al insertar contrato");
            }
        }
    }

    private void editar() {
        int row = tableContratos.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un contrato");
            return;
        }
        String num = (String) contratosModel.getValueAt(row, 0);
        try {
            Contrato c = dao.findByNum(num);
            ContratoDialog dialog = new ContratoDialog((JFrame) SwingUtilities.getWindowAncestor(this), c);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getContrato());
                cargarContratos();
                if (contratoSeleccionado != null && contratoSeleccionado.getNumContrato().equals(num)) {
                    contratoSeleccionado = dao.findByNum(num);
                    cargarDetalles(contratoSeleccionado);
                }
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al editar contrato");
        }
    }

    private void eliminar() {
        int row = tableContratos.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un contrato");
            return;
        }
        String num = (String) contratosModel.getValueAt(row, 0);
        if (UIUtils.showConfirm(this, "¿Eliminar contrato y sus líneas/pagos?", "Confirmar")) {
            try {
                dao.delete(num);
                cargarContratos();
                limpiarDetalles();
                contratoSeleccionado = null;
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al eliminar contrato");
            }
        }
    }
}