package com.decobana.ui.dialogs;

import com.decobana.model.Proveedor;

import javax.swing.*;
import java.awt.*;

public class ProveedorDialog extends JDialog {
    private JTextField tfNombre, tfTipoServicio, tfDireccion, tfTelefono, tfEmail, tfResponsable;
    private boolean confirmed = false;
    private Proveedor proveedor;

    public ProveedorDialog(JFrame parent, Proveedor p) {
        super(parent, p == null ? "Agregar Proveedor" : "Editar Proveedor", true);
        this.proveedor = (p != null) ? p : new Proveedor();
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNombre = new JTextField(15);
        tfTipoServicio = new JTextField(15);
        tfDireccion = new JTextField(15);
        tfTelefono = new JTextField(15);
        tfEmail = new JTextField(15);
        tfResponsable = new JTextField(15);

        if (proveedor.getIdProveedor() != 0) {
            tfNombre.setText(proveedor.getNombre());
            tfTipoServicio.setText(proveedor.getTipoServicio());
            tfDireccion.setText(proveedor.getDireccion());
            tfTelefono.setText(proveedor.getTelefono());
            tfEmail.setText(proveedor.getEmail());
            tfResponsable.setText(proveedor.getResponsable());
        }

        int y = 0;
        addField("Nombre:", tfNombre, y++, gbc);
        addField("Tipo Servicio:", tfTipoServicio, y++, gbc);
        addField("Dirección:", tfDireccion, y++, gbc);
        addField("Teléfono:", tfTelefono, y++, gbc);
        addField("Email:", tfEmail, y++, gbc);
        addField("Responsable:", tfResponsable, y++, gbc);

        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        btnOk.addActionListener(e -> {
            proveedor.setNombre(tfNombre.getText());
            proveedor.setTipoServicio(tfTipoServicio.getText());
            proveedor.setDireccion(tfDireccion.getText());
            proveedor.setTelefono(tfTelefono.getText());
            proveedor.setEmail(tfEmail.getText());
            proveedor.setResponsable(tfResponsable.getText());
            confirmed = true;
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());
    }

    private void addField(String label, JTextField field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    public boolean isConfirmed() { return confirmed; }
    public Proveedor getProveedor() { return proveedor; }
}