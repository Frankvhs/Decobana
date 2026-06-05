package com.decobana.ui.reports;

import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReporteEstadoProveedores extends JDialog {
    public ReporteEstadoProveedores(JFrame parent) {
        super(parent, "Estado de Proveedores", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Proveedor","Tipo Servicio","Estado Servicios"}, 0);
        String sql = "SELECT p.nombre, p.tipo_servicio, " +
                "GROUP_CONCAT(sp.nombre || ' (' || CASE WHEN sp.estado_activo THEN 'Activo' ELSE 'Inactivo' END || ')', ', ') AS estado " +
                "FROM proveedores p LEFT JOIN servicios_productos sp ON p.id_proveedor = sp.id_proveedor " +
                "GROUP BY p.id_proveedor " +
                "ORDER BY p.tipo_servicio, p.nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("nombre"), rs.getString("tipo_servicio"), rs.getString("estado")});
            }
        } catch (Exception ex) {
            model.addRow(new Object[]{"Error", ex.getMessage(), ""});
        }
        add(new JScrollPane(new JTable(model)));
    }
}