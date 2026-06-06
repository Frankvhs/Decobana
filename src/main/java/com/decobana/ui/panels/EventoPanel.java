package com.decobana.ui.panels;

import com.decobana.dao.EventoDAO;
import com.decobana.model.*;
import com.decobana.ui.dialogs.EventoDialog;
import com.decobana.ui.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventoPanel extends JPanel {
    private JTable tableEventos;
    private DefaultTableModel eventosModel;
    private EventoDAO dao = new EventoDAO();

    private JTable tableTemas, tableServicios, tableEmpleados;
    private DefaultTableModel temasModel, serviciosModel, empleadosModel;

    private JTabbedPane subTabPane;
    private JButton btnAgregar, btnEditar, btnEliminar;

    private Evento eventoSeleccionado;

    public EventoPanel() {
        setLayout(new BorderLayout());

        eventosModel = new DefaultTableModel(new Object[]{"ID","Nombre","Tipo","Inicio","Fin","Ubicación","Invitados"}, 0);
        tableEventos = new JTable(eventosModel);
        tableEventos.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
        JScrollPane scrollEventos = new JScrollPane(tableEventos);
        scrollEventos.setPreferredSize(new Dimension(600, 200));
        add(scrollEventos, BorderLayout.NORTH);

        subTabPane = new JTabbedPane();
        temasModel = new DefaultTableModel(new Object[]{"Tema"}, 0);
        tableTemas = new JTable(temasModel);
        subTabPane.addTab("Temas / Conceptos", new JScrollPane(tableTemas));

        serviciosModel = new DefaultTableModel(new Object[]{"Cód. Servicio","Cantidad"}, 0);
        tableServicios = new JTable(serviciosModel);
        subTabPane.addTab("Servicios", new JScrollPane(tableServicios));

        empleadosModel = new DefaultTableModel(new Object[]{"ID Empleado","Responsabilidades"}, 0);
        tableEmpleados = new JTable(empleadosModel);
        subTabPane.addTab("Empleados", new JScrollPane(tableEmpleados));

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

        cargarEventos();
    }

    private void cargarEventos() {
        eventosModel.setRowCount(0);
        try {
            List<Evento> eventos = dao.findAll();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Evento e : eventos) {
                eventosModel.addRow(new Object[]{
                        e.getIdEvento(), e.getNombreEvento(), e.getTipoEvento(),
                        e.getFechaHoraIni().format(fmt),
                        e.getFechaHoraFin().format(fmt),
                        e.getUbicacion(), e.getNumInvitados()
                });
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al cargar eventos");
        }
    }

    private void mostrarDetalles() {
        int row = tableEventos.getSelectedRow();
        if (row == -1) {
            limpiarDetalles();
            eventoSeleccionado = null;
            return;
        }
        int id = (int) eventosModel.getValueAt(row, 0);
        try {
            eventoSeleccionado = dao.findById(id);
            cargarDetalles(eventoSeleccionado);
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al cargar detalles del evento");
        }
    }

    private void cargarDetalles(Evento e) {
        temasModel.setRowCount(0);
        if (e.getTemas() != null) for (String t : e.getTemas()) temasModel.addRow(new Object[]{t});
        serviciosModel.setRowCount(0);
        if (e.getServicios() != null) for (EventoServicio es : e.getServicios()) serviciosModel.addRow(new Object[]{es.getCodServicio(), es.getCantidad()});
        empleadosModel.setRowCount(0);
        if (e.getEmpleados() != null) for (EventoEmpleado ee : e.getEmpleados()) empleadosModel.addRow(new Object[]{ee.getIdEmpleado(), ee.getResponsabilidades()});
    }

    private void limpiarDetalles() {
        temasModel.setRowCount(0);
        serviciosModel.setRowCount(0);
        empleadosModel.setRowCount(0);
    }

    private void agregar() {
        EventoDialog dialog = new EventoDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                dao.insert(dialog.getEvento());
                cargarEventos();
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al insertar evento");
            }
        }
    }

    private void editar() {
        int row = tableEventos.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un evento");
            return;
        }
        int id = (int) eventosModel.getValueAt(row, 0);
        try {
            Evento e = dao.findById(id);
            EventoDialog dialog = new EventoDialog((JFrame) SwingUtilities.getWindowAncestor(this), e);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                dao.update(dialog.getEvento());
                cargarEventos();
                if (eventoSeleccionado != null && eventoSeleccionado.getIdEvento() == id) {
                    eventoSeleccionado = dao.findById(id);
                    cargarDetalles(eventoSeleccionado);
                }
            }
        } catch (SQLException ex) {
            UIUtils.handleDatabaseException(this, ex, "Error al editar evento");
        }
    }

    private void eliminar() {
        int row = tableEventos.getSelectedRow();
        if (row == -1) {
            UIUtils.showWarning(this, "Seleccione un evento");
            return;
        }
        int id = (int) eventosModel.getValueAt(row, 0);
        if (UIUtils.showConfirm(this, "¿Eliminar evento y sus detalles?", "Confirmar")) {
            try {
                dao.delete(id);
                cargarEventos();
                limpiarDetalles();
                eventoSeleccionado = null;
            } catch (SQLException ex) {
                UIUtils.handleDatabaseException(this, ex, "Error al eliminar evento");
            }
        }
    }
}