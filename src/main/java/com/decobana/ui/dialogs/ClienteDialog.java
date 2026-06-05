package com.decobana.ui.dialogs;

import com.decobana.model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClienteDialog extends JDialog {
    private JTextField tfNombre, tfApellidos, tfDocumento, tfDireccion, tfTelefono, tfEmail;
    private JCheckBox chkTratoPref;
    private boolean confirmed = false;
    private Cliente cliente;

    public ClienteDialog(JFrame parent, Cliente c) {
        super(parent, c == null ? "Agregar Cliente" : "Editar Cliente", true);
        this.cliente = (c != null) ? c : new Cliente();
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
        tfApellidos = new JTextField(15);
        tfDocumento = new JTextField(15);
        tfDireccion = new JTextField(15);
        tfTelefono = new JTextField(15);
        tfEmail = new JTextField(15);
        chkTratoPref = new JCheckBox("Trato Preferencial");

        if (cliente.getIdCliente() != 0) {
            tfNombre.setText(cliente.getNombre());
            tfApellidos.setText(cliente.getApellidos());
            tfDocumento.setText(cliente.getNumDocumento());
            tfDireccion.setText(cliente.getDireccion());
            tfTelefono.setText(cliente.getTelefono());
            tfEmail.setText(cliente.getEmail());
            chkTratoPref.setSelected(cliente.isTratoPref());
        }

        int y = 0;
        addField("Nombre:", tfNombre, y++, gbc);
        addField("Apellidos:", tfApellidos, y++, gbc);
        addField("Documento:", tfDocumento, y++, gbc);
        addField("Dirección:", tfDireccion, y++, gbc);
        addField("Teléfono:", tfTelefono, y++, gbc);
        addField("Email:", tfEmail, y++, gbc);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(chkTratoPref, gbc); y++;
        gbc.gridwidth = 1;

        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        btnOk.addActionListener(e -> {
            cliente.setNombre(tfNombre.getText());
            cliente.setApellidos(tfApellidos.getText());
            cliente.setNumDocumento(tfDocumento.getText());
            cliente.setDireccion(tfDireccion.getText());
            cliente.setTelefono(tfTelefono.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setTratoPref(chkTratoPref.isSelected());
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
    public Cliente getCliente() { return cliente; }
}