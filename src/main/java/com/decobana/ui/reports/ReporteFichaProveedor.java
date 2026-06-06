package com.decobana.ui.reports;

import com.decobana.dao.ProveedorDAO;
import com.decobana.model.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReporteFichaProveedor extends JDialog {
    private JComboBox<String> cbProv;
    private DefaultTableModel model;
    private List<Proveedor> proveedores;

    public ReporteFichaProveedor(JFrame parent) {
        super(parent, "Ficha de Proveedor", true);
        setSize(600, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        cbProv = new JComboBox<>();
        try {
            proveedores = new ProveedorDAO().findAll();
            for (Proveedor p : proveedores) cbProv.addItem(p.getNombre());
        } catch (Exception e) {}
        top.add(new JLabel("Proveedor:"));
        top.add(cbProv);
        JButton btnVer = new JButton("Ver");
        top.add(btnVer);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Campo","Valor"}, 0);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnVer.addActionListener(e -> cargarFicha());
    }

    private void cargarFicha() {
        model.setRowCount(0);
        if (cbProv.getSelectedIndex() < 0) return;
        Proveedor p = proveedores.get(cbProv.getSelectedIndex());
        model.addRow(new Object[]{"Nombre", p.getNombre()});
        model.addRow(new Object[]{"Tipo Servicio", p.getTipoServicio()});
        model.addRow(new Object[]{"Dirección", p.getDireccion()});
        model.addRow(new Object[]{"Teléfono", p.getTelefono()});
        model.addRow(new Object[]{"Email", p.getEmail()});
        model.addRow(new Object[]{"Responsable", p.getResponsable()});
    }
}