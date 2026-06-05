package com.decobana.ui.dialogs;

import javax.swing.*;
import java.awt.*;

public abstract class BaseEntityDialog<T> extends JDialog {
    protected boolean confirmed = false;
    protected T entity;
    private GridBagConstraints gbc;
    private int currentY = 0;

    public BaseEntityDialog(JFrame parent, String title, T entity) {
        super(parent, title, true);
        this.entity = (entity != null) ? entity : createNewEntity();
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addSpecificFields();
        addButtons();
        pack();
        setLocationRelativeTo(parent);
        prepareForLoad();
        if (entityExists()) {
            loadEntityData();
        }
    }

    protected abstract T createNewEntity();
    protected abstract void loadEntityData();
    protected abstract void saveToEntity();
    protected abstract void addSpecificFields();

    protected void prepareForLoad() {}

    protected void addField(String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = currentY;
        gbc.gridwidth = 1;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
        currentY++;
    }

    protected void addFullWidthComponent(JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = currentY;
        gbc.gridwidth = 2;
        add(comp, gbc);
        currentY++;
    }

    private void addButtons() {
        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);
        gbc.gridx = 0;
        gbc.gridy = currentY;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);
        btnOk.addActionListener(e -> {
            saveToEntity();
            confirmed = true;
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());
    }

    protected boolean entityExists() {
        return false;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public T getEntity() {
        return entity;
    }
}