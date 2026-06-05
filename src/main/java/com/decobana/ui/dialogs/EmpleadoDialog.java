package com.decobana.ui.dialogs;

import com.decobana.model.Empleado;

import javax.swing.*;
import java.awt.*;

public class EmpleadoDialog extends JDialog {
    private JTextField tfNombre, tfApellidos, tfDocumento, tfDireccion, tfTelefono, tfEmail, tfCargo, tfDepartamento;
    private boolean confirmed = false;
    private Empleado empleado;

    public EmpleadoDialog(JFrame parent, Empleado e) {
        super(parent, e == null ? "Agregar Empleado" : "Editar Empleado", true);
        this.empleado = (e != null) ? e : new Empleado();
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
        tfCargo = new JTextField(15);
        tfDepartamento = new JTextField(15);

        if (empleado.getIdEmpleado() != 0) {
            tfNombre.setText(empleado.getNombre());
            tfApellidos.setText(empleado.getApellidos());
            tfDocumento.setText(empleado.getNumDocumento());
            tfDireccion.setText(empleado.getDireccion());
            tfTelefono.setText(empleado.getTelefono());
            tfEmail.setText(empleado.getEmail());
            tfCargo.setText(empleado.getCargo());
            tfDepartamento.setText(empleado.getDepartamento());
        }

        int y = 0;
        addField("Nombre:", tfNombre, y++, gbc);
        addField("Apellidos:", tfApellidos, y++, gbc);
        addField("Documento:", tfDocumento, y++, gbc);
        addField("Dirección:", tfDireccion, y++, gbc);
        addField("Teléfono:", tfTelefono, y++, gbc);
        addField("Email:", tfEmail, y++, gbc);
        addField("Cargo:", tfCargo, y++, gbc);
        addField("Departamento:", tfDepartamento, y++, gbc);

        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        btnOk.addActionListener(e -> {
            empleado.setNombre(tfNombre.getText());
            empleado.setApellidos(tfApellidos.getText());
            empleado.setNumDocumento(tfDocumento.getText());
            empleado.setDireccion(tfDireccion.getText());
            empleado.setTelefono(tfTelefono.getText());
            empleado.setEmail(tfEmail.getText());
            empleado.setCargo(tfCargo.getText());
            empleado.setDepartamento(tfDepartamento.getText());
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
    public Empleado getEmpleado() { return empleado; }
}