package com.decobana.ui.reports;

import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReporteConsolidadoAnual extends JDialog {
    private JTextField tfAnio;
    private DefaultTableModel model;

    public ReporteConsolidadoAnual(JFrame parent) {
        super(parent, "Consolidado Anual de Servicios/Productos", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Año:"));
        tfAnio = new JTextField(6);
        top.add(tfAnio);
        JButton btnVer = new JButton("Ver");
        top.add(btnVer);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Categoría","Cantidad Utilizada","Monto Facturado"}, 0);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnVer.addActionListener(e -> cargar());
    }

    private void cargar() {
        model.setRowCount(0);
        try {
            int anio = Integer.parseInt(tfAnio.getText().trim());
            String sql = "SELECT cat.nombre AS categoria, SUM(cl.cantidad) AS cantidad_total, SUM(cl.precio_negociado * cl.cantidad) AS monto_total " +
                    "FROM contratos c " +
                    "JOIN contratos_lineas cl ON c.num_contrato = cl.num_contrato " +
                    "JOIN servicios_productos sp ON cl.cod_servicio = sp.cod_servicio " +
                    "JOIN categorias_servicios_productos cat ON sp.id_categoria = cat.id_categoria " +
                    "WHERE strftime('%Y', c.fecha_contrato) = ? " +
                    "GROUP BY cat.id_categoria ORDER BY categoria";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, String.format("%04d", anio));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("categoria"),
                            rs.getInt("cantidad_total"),
                            rs.getDouble("monto_total")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}