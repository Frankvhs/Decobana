package com.decobana.ui.dialogs;

import com.decobana.model.Proveedor;

import javax.swing.*;

public class ProveedorDialog extends BaseEntityDialog<Proveedor> {
    private JTextField tfNombre, tfTipoServicio, tfDireccion, tfTelefono, tfEmail, tfResponsable;

    public ProveedorDialog(JFrame parent, Proveedor p) {
        super(parent, p == null ? "Agregar Proveedor" : "Editar Proveedor", p);
    }

    @Override
    protected Proveedor createNewEntity() {
        return new Proveedor();
    }

    @Override
    protected boolean entityExists() {
        return entity != null && entity.getIdProveedor() != 0;
    }

    @Override
    protected void addSpecificFields() {
        tfNombre = new JTextField(15);
        tfTipoServicio = new JTextField(15);
        tfDireccion = new JTextField(15);
        tfTelefono = new JTextField(15);
        tfEmail = new JTextField(15);
        tfResponsable = new JTextField(15);

        addField("Nombre:", tfNombre);
        addField("Tipo Servicio:", tfTipoServicio);
        addField("Dirección:", tfDireccion);
        addField("Teléfono:", tfTelefono);
        addField("Email:", tfEmail);
        addField("Responsable:", tfResponsable);
    }

    @Override
    protected void loadEntityData() {
        tfNombre.setText(entity.getNombre());
        tfTipoServicio.setText(entity.getTipoServicio());
        tfDireccion.setText(entity.getDireccion());
        tfTelefono.setText(entity.getTelefono());
        tfEmail.setText(entity.getEmail());
        tfResponsable.setText(entity.getResponsable());
    }

    @Override
    protected void saveToEntity() {
        entity.setNombre(tfNombre.getText());
        entity.setTipoServicio(tfTipoServicio.getText());
        entity.setDireccion(tfDireccion.getText());
        entity.setTelefono(tfTelefono.getText());
        entity.setEmail(tfEmail.getText());
        entity.setResponsable(tfResponsable.getText());
    }
}