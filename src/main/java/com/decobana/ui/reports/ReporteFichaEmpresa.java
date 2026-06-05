package com.decobana.ui.reports;

import com.decobana.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReporteFichaEmpresa extends JDialog {
    public ReporteFichaEmpresa(JFrame parent) {
        super(parent, "Ficha de la Empresa", true);
        setSize(600, 300);
        setLocationRelativeTo(parent);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Campo","Valor"}, 0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM empresas LIMIT 1")) {
            if (rs.next()) {
                String[] labels = {"Nombre","Dirección","Teléfono","Email","Director General","Jefe RRHH","Jefe Contabilidad","Secretario Sindicato"};
                String[] fields = {"nombre","direccion","telefono","email","director_general","jefe_rrhh","jefe_contabilidad","secretario_sindicato"};
                for (int i=0; i<labels.length; i++) {
                    model.addRow(new Object[]{labels[i], rs.getString(fields[i])});
                }
            } else {
                model.addRow(new Object[]{"Sin datos",""});
            }
        } catch (Exception ex) {
            model.addRow(new Object[]{"Error", ex.getMessage()});
        }
        JTable table = new JTable(model);
        add(new JScrollPane(table));
    }
}