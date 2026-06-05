package com.decobana.ui.reports;

import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class ReporteContratosCliente extends JDialog {
    private JTextField tfInicio, tfFin;
    private DefaultTableModel model;

    public ReporteContratosCliente(JFrame parent) {
        super(parent, "Contratos por Cliente (Período)", true);
        setSize(900, 400);
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

        model = new DefaultTableModel(new String[]{"Cliente","Nº Contrato","Evento","Servicios","Precios","Pagos","Términos"}, 0);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscar());
    }

    private void buscar() {
        model.setRowCount(0);
        try {
            LocalDate ini = LocalDate.parse(tfInicio.getText().trim());
            LocalDate fin = LocalDate.parse(tfFin.getText().trim());
            String sql = "SELECT cl.nombre || ' ' || cl.apellidos AS cliente, c.num_contrato, e.nombre_evento, " +
                    "GROUP_CONCAT(l.cod_servicio || ' x' || l.cantidad, ', ') AS servicios, " +
                    "GROUP_CONCAT(l.precio_negociado, ', ') AS precios, " +
                    "GROUP_CONCAT(p.fecha_pago || ' $' || p.monto, ', ') AS pagos, " +
                    "c.terminos_cond " +
                    "FROM contratos c " +
                    "JOIN clientes cl ON c.id_cliente = cl.id_cliente " +
                    "JOIN eventos e ON c.id_evento = e.id_evento " +
                    "LEFT JOIN contratos_lineas l ON c.num_contrato = l.num_contrato " +
                    "LEFT JOIN pagos p ON c.num_contrato = p.num_contrato " +
                    "WHERE c.fecha_contrato BETWEEN ? AND ? " +
                    "GROUP BY c.num_contrato " +
                    "ORDER BY c.fecha_contrato, cliente";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ini.toString());
                ps.setString(2, fin.toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("cliente"),
                            rs.getString("num_contrato"),
                            rs.getString("nombre_evento"),
                            rs.getString("servicios"),
                            rs.getString("precios"),
                            rs.getString("pagos"),
                            rs.getString("terminos_cond")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}