package com.decobana.ui.reports;

import com.decobana.dao.EventoDAO;
import com.decobana.model.Evento;
import com.decobana.model.EventoServicio;
import com.decobana.dao.ServicioProductoDAO;
import com.decobana.model.ServicioProducto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReporteServiciosEvento extends JDialog {
    private JComboBox<String> cbEvento;
    private List<Evento> eventos;
    private DefaultTableModel model;
    private EventoDAO eventoDAO = new EventoDAO();
    private ServicioProductoDAO spDAO = new ServicioProductoDAO();

    public ReporteServiciosEvento(JFrame parent) {
        super(parent, "Servicios por Evento", true);
        setSize(700, 400);
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

        model = new DefaultTableModel(new String[]{"Código Servicio","Nombre Servicio","Descripción","Cantidad"}, 0);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnVer.addActionListener(e -> cargar());
    }

    private void cargar() {
        model.setRowCount(0);
        if (cbEvento.getSelectedIndex() < 0) return;
        Evento ev = eventos.get(cbEvento.getSelectedIndex());
        model.addRow(new Object[]{"Evento: " + ev.getNombreEvento(), "", "", ""});
        try {
            // Obtain services from evento
            Evento full = eventoDAO.findById(ev.getIdEvento());
            if (full.getServicios() != null) {
                for (EventoServicio es : full.getServicios()) {
                    ServicioProducto sp = spDAO.findByCod(es.getCodServicio());
                    model.addRow(new Object[]{
                            es.getCodServicio(),
                            sp != null ? sp.getNombre() : "?",
                            sp != null ? sp.getDescripcion() : "",
                            es.getCantidad()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}