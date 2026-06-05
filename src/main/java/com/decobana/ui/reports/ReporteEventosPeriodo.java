package com.decobana.ui.reports;

import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class ReporteEventosPeriodo extends JDialog {
    private JTextField tfInicio, tfFin;
    private DefaultTableModel model;

    public ReporteEventosPeriodo(JFrame parent) {
        super(parent, "Eventos en un período", true);
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

        model = new DefaultTableModel(new String[]{"Nombre","Tipo","Inicio","Fin","Ubicación","Invitados","Temas"}, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscar());
    }

    private void buscar() {
        model.setRowCount(0);
        try {
            LocalDate ini = LocalDate.parse(tfInicio.getText().trim());
            LocalDate fin = LocalDate.parse(tfFin.getText().trim());
            String sql = "SELECT e.nombre_evento, e.tipo_evento, e.fecha_hora_ini, e.fecha_hora_fin, e.ubicacion, e.num_invitados, " +
                    "GROUP_CONCAT(et.tema, ', ') AS temas FROM eventos e " +
                    "LEFT JOIN eventos_temas et ON e.id_evento = et.id_evento " +
                    "WHERE date(e.fecha_hora_ini) BETWEEN ? AND ? " +
                    "GROUP BY e.id_evento ORDER BY e.fecha_hora_ini";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ini.toString());
                ps.setString(2, fin.toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("nombre_evento"),
                            rs.getString("tipo_evento"),
                            rs.getTimestamp("fecha_hora_ini").toString(),
                            rs.getTimestamp("fecha_hora_fin").toString(),
                            rs.getString("ubicacion"),
                            rs.getInt("num_invitados"),
                            rs.getString("temas")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}