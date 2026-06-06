package com.decobana.ui.reports;

import com.decobana.dao.ClienteDAO;
import com.decobana.model.Cliente;
import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class ReporteFichaCliente extends JDialog {
    private JComboBox<String> cbCliente;
    private DefaultTableModel model;
    private JTable table;
    private List<Cliente> clientes;

    public ReporteFichaCliente(JFrame parent) {
        super(parent, "Ficha de Cliente", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        cbCliente = new JComboBox<>();
        loadClientes();
        topPanel.add(new JLabel("Cliente:"));
        topPanel.add(cbCliente);
        JButton btnVer = new JButton("Ver");
        topPanel.add(btnVer);
        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Campo","Valor"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnVer.addActionListener(e -> cargarFicha());
    }

    private void loadClientes() {
        try {
            clientes = new ClienteDAO().findAll();
            for (Cliente c : clientes) {
                cbCliente.addItem(c.getNombre() + " " + c.getApellidos() + " (" + c.getNumDocumento() + ")");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarFicha() {
        model.setRowCount(0);
        if (cbCliente.getSelectedIndex() < 0) return;
        Cliente cl = clientes.get(cbCliente.getSelectedIndex());
        try (Connection conn = DatabaseConnection.getConnection()) {
            model.addRow(new Object[]{"Nombre", cl.getNombre() + " " + cl.getApellidos()});
            model.addRow(new Object[]{"Documento", cl.getNumDocumento()});
            model.addRow(new Object[]{"Dirección", cl.getDireccion()});
            model.addRow(new Object[]{"Teléfono", cl.getTelefono()});
            model.addRow(new Object[]{"Email", cl.getEmail()});
            model.addRow(new Object[]{"Trato Preferencial", cl.isTratoPref() ? "Sí" : "No"});
            String sql = "SELECT e.nombre_evento, e.fecha_hora_ini FROM eventos e WHERE e.id_cliente=? ORDER BY e.fecha_hora_ini DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cl.getIdCliente());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{"Evento anterior", rs.getString("nombre_evento") + " - " + rs.getTimestamp("fecha_hora_ini").toString()});
                }
            }
        } catch (Exception e) {
            model.addRow(new Object[]{"Error", e.getMessage()});
        }
    }
}