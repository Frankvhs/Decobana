package com.decobana.ui.dialogs;

import com.decobana.model.Cliente;

import javax.swing.*;

public class ClienteDialog extends BaseEntityDialog<Cliente> {
    private JTextField tfNombre, tfApellidos, tfDocumento, tfDireccion, tfTelefono, tfEmail;
    private JCheckBox chkTratoPref;

    public ClienteDialog(JFrame parent, Cliente c) {
        super(parent, c == null ? "Agregar Cliente" : "Editar Cliente", c);
    }

    @Override
    protected Cliente createNewEntity() {
        return new Cliente();
    }

    @Override
    protected boolean entityExists() {
        return entity != null && entity.getIdCliente() != 0;
    }

    @Override
    protected void addSpecificFields() {
        tfNombre = new JTextField(15);
        tfApellidos = new JTextField(15);
        tfDocumento = new JTextField(15);
        tfDireccion = new JTextField(15);
        tfTelefono = new JTextField(15);
        tfEmail = new JTextField(15);
        chkTratoPref = new JCheckBox("Trato Preferencial");

        addField("Nombre:", tfNombre);
        addField("Apellidos:", tfApellidos);
        addField("Documento:", tfDocumento);
        addField("Dirección:", tfDireccion);
        addField("Teléfono:", tfTelefono);
        addField("Email:", tfEmail);
        addFullWidthComponent(chkTratoPref);
    }

    @Override
    protected void loadEntityData() {
        tfNombre.setText(entity.getNombre());
        tfApellidos.setText(entity.getApellidos());
        tfDocumento.setText(entity.getNumDocumento());
        tfDireccion.setText(entity.getDireccion());
        tfTelefono.setText(entity.getTelefono());
        tfEmail.setText(entity.getEmail());
        chkTratoPref.setSelected(entity.isTratoPref());
    }

    @Override
    protected void saveToEntity() {
        entity.setNombre(tfNombre.getText());
        entity.setApellidos(tfApellidos.getText());
        entity.setNumDocumento(tfDocumento.getText());
        entity.setDireccion(tfDireccion.getText());
        entity.setTelefono(tfTelefono.getText());
        entity.setEmail(tfEmail.getText());
        entity.setTratoPref(chkTratoPref.isSelected());
    }
}