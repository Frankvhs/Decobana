package com.decobana.ui.reports;

import com.decobana.dao.EmpleadoDAO;
import com.decobana.dao.EventoDAO;
import com.decobana.model.Empleado;
import com.decobana.model.Evento;
import com.decobana.model.EventoEmpleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReporteEmpleadosEvento extends JDialog {
    private JComboBox<String> cbEvento;
    private List<Evento> eventos;
    private DefaultTableModel model;
    private EventoDAO eventoDAO = new EventoDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public ReporteEmpleadosEvento(JFrame parent) {
        super(parent, "Empleados por Evento", true);
        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        cbEvento = new JComboBox<>();
        try {
            eventos = eventoDAO.findAll();
            for (Evento e : eventos) cbEvento.addItem(e.getNombreEvento() + " (ID:" + e.getIdEvento() + ")");
        } catch (Exception ex) {}
        top.add(new JLabel("Evento:"));
        top.add(cbEvento);
        JButton btnVer = new JButton("Ver");
        top.add(btnVer);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Nombre Empleado","Cargo","Departamento","Responsabilidades"}, 0);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnVer.addActionListener(e -> cargar());
    }

    private void cargar() {
        model.setRowCount(0);
        if (cbEvento.getSelectedIndex() < 0) return;
        Evento ev = eventos.get(cbEvento.getSelectedIndex());
        model.addRow(new Object[]{"Evento: " + ev.getNombreEvento(), "", "", ""});
        try {
            Evento full = eventoDAO.findById(ev.getIdEvento());
            if (full.getEmpleados() != null) {
                for (EventoEmpleado ee : full.getEmpleados()) {
                    Empleado emp = empleadoDAO.findById(ee.getIdEmpleado());
                    model.addRow(new Object[]{
                            emp != null ? emp.getNombre() + " " + emp.getApellidos() : "?",
                            emp != null ? emp.getCargo() : "",
                            emp != null ? emp.getDepartamento() : "",
                            ee.getResponsabilidades()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}