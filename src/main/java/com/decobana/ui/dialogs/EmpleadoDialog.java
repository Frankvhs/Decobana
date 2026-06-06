package com.decobana.ui.dialogs;

import com.decobana.model.Empleado;

import javax.swing.*;

public class EmpleadoDialog extends BaseEntityDialog<Empleado> {
    private JTextField tfNombre, tfApellidos, tfDocumento, tfDireccion, tfTelefono, tfEmail, tfCargo, tfDepartamento;

    public EmpleadoDialog(JFrame parent, Empleado e) {
        super(parent, e == null ? "Agregar Empleado" : "Editar Empleado", e);
    }

    @Override
    protected Empleado createNewEntity() {
        return new Empleado();
    }

    @Override
    protected boolean entityExists() {
        return entity != null && entity.getIdEmpleado() != 0;
    }

    @Override
    protected void addSpecificFields() {
        tfNombre = new JTextField(15);
        tfApellidos = new JTextField(15);
        tfDocumento = new JTextField(15);
        tfDireccion = new JTextField(15);
        tfTelefono = new JTextField(15);
        tfEmail = new JTextField(15);
        tfCargo = new JTextField(15);
        tfDepartamento = new JTextField(15);

        addField("Nombre:", tfNombre);
        addField("Apellidos:", tfApellidos);
        addField("Documento:", tfDocumento);
        addField("Dirección:", tfDireccion);
        addField("Teléfono:", tfTelefono);
        addField("Email:", tfEmail);
        addField("Cargo:", tfCargo);
        addField("Departamento:", tfDepartamento);
    }

    @Override
    protected void loadEntityData() {
        tfNombre.setText(entity.getNombre());
        tfApellidos.setText(entity.getApellidos());
        tfDocumento.setText(entity.getNumDocumento());
        tfDireccion.setText(entity.getDireccion());
        tfTelefono.setText(entity.getTelefono());
        tfEmail.setText(entity.getEmail());
        tfCargo.setText(entity.getCargo());
        tfDepartamento.setText(entity.getDepartamento());
    }

    @Override
    protected void saveToEntity() {
        entity.setNombre(tfNombre.getText());
        entity.setApellidos(tfApellidos.getText());
        entity.setNumDocumento(tfDocumento.getText());
        entity.setDireccion(tfDireccion.getText());
        entity.setTelefono(tfTelefono.getText());
        entity.setEmail(tfEmail.getText());
        entity.setCargo(tfCargo.getText());
        entity.setDepartamento(tfDepartamento.getText());
    }
}