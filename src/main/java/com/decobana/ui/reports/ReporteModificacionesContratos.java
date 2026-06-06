package com.decobana.ui.reports;

import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class ReporteModificacionesContratos extends JDialog {
    private JTextField tfInicio, tfFin;
    private DefaultTableModel model;

    public ReporteModificacionesContratos(JFrame parent) {
        super(parent, "Modificaciones de Contratos", true);
        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Fecha inicio (YYYY-MM-DD):"));
        tfInicio = new JTextField(10);
        top.add(tfInicio);
        top.add(new JLabel("Fecha fin (YYYY-MM-DD):"));
        tfFin = new JTextField(10);
        top.add(tfFin);
        JButton btnBuscar = new JButton("Buscar");
        top.add(btnBuscar);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Contrato","Cliente","Evento","Modificación","Fecha Mod."}, 0);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscar());
    }

    private void buscar() {
        model.setRowCount(0);
        try {
            LocalDate ini = LocalDate.parse(tfInicio.getText().trim());
            LocalDate fin = LocalDate.parse(tfFin.getText().trim());
            String sql = "SELECT c.num_contrato, cl.nombre || ' ' || cl.apellidos AS cliente, e.nombre_evento, " +
                    "m.descripcion, m.fecha_mod " +
                    "FROM modificaciones_contratos m " +
                    "JOIN contratos c ON m.num_contrato = c.num_contrato " +
                    "JOIN clientes cl ON c.id_cliente = cl.id_cliente " +
                    "JOIN eventos e ON c.id_evento = e.id_evento " +
                    "WHERE m.fecha_mod BETWEEN ? AND ? " +
                    "ORDER BY m.fecha_mod, c.num_contrato";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ini.toString());
                ps.setString(2, fin.toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("num_contrato"),
                            rs.getString("cliente"),
                            rs.getString("nombre_evento"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_mod").toString()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}