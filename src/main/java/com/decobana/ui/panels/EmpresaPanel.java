package com.decobana.ui.panels;

import com.decobana.dao.EmpresaDAO;
import com.decobana.model.Empresa;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class EmpresaPanel extends JPanel {
    private JTextField tfNombre, tfDireccion, tfTelefono, tfEmail;
    private JTextField tfDirGeneral, tfJefeRRHH, tfJefeCont, tfSecSind;
    private JButton btnGuardar;
    private EmpresaDAO dao = new EmpresaDAO();
    private Empresa empresa;

    public EmpresaPanel() {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNombre = new JTextField(20);
        tfDireccion = new JTextField(20);
        tfTelefono = new JTextField(20);
        tfEmail = new JTextField(20);
        tfDirGeneral = new JTextField(20);
        tfJefeRRHH = new JTextField(20);
        tfJefeCont = new JTextField(20);
        tfSecSind = new JTextField(20);

        int row = 0;
        addRow(formPanel, gbc, "Nombre:", tfNombre, row++);
        addRow(formPanel, gbc, "Dirección:", tfDireccion, row++);
        addRow(formPanel, gbc, "Teléfono:", tfTelefono, row++);
        addRow(formPanel, gbc, "Email:", tfEmail, row++);
        addRow(formPanel, gbc, "Director General:", tfDirGeneral, row++);
        addRow(formPanel, gbc, "Jefe RRHH:", tfJefeRRHH, row++);
        addRow(formPanel, gbc, "Jefe Contabilidad:", tfJefeCont, row++);
        addRow(formPanel, gbc, "Sec. Sindicato:", tfSecSind, row++);

        btnGuardar = new JButton("Guardar");
        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(btnGuardar, gbc);

        add(formPanel, BorderLayout.NORTH);

        btnGuardar.addActionListener(e -> guardar());
        cargarDatos();
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void cargarDatos() {
        try {
            empresa = dao.findFirst();
            if (empresa != null) {
                tfNombre.setText(empresa.getNombre());
                tfDireccion.setText(empresa.getDireccion());
                tfTelefono.setText(empresa.getTelefono());
                tfEmail.setText(empresa.getEmail());
                tfDirGeneral.setText(empresa.getDirectorGeneral());
                tfJefeRRHH.setText(empresa.getJefeRRHH());
                tfJefeCont.setText(empresa.getJefeContabilidad());
                tfSecSind.setText(empresa.getSecretarioSindicato());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        if (empresa == null) empresa = new Empresa();
        empresa.setNombre(tfNombre.getText());
        empresa.setDireccion(tfDireccion.getText());
        empresa.setTelefono(tfTelefono.getText());
        empresa.setEmail(tfEmail.getText());
        empresa.setDirectorGeneral(tfDirGeneral.getText());
        empresa.setJefeRRHH(tfJefeRRHH.getText());
        empresa.setJefeContabilidad(tfJefeCont.getText());
        empresa.setSecretarioSindicato(tfSecSind.getText());
        try {
            dao.save(empresa);
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}