package com.decobana.ui.dialogs;

import com.decobana.dao.CategoriaServicioProductoDAO;
import com.decobana.dao.ProveedorDAO;
import com.decobana.model.CategoriaServicioProducto;
import com.decobana.model.Proveedor;
import com.decobana.model.ServicioProducto;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ServicioProductoDialog extends JDialog {
    private JTextField tfCodigo, tfNombre, tfDescripcion, tfPrecio;
    private JComboBox<String> cbCategoria;
    private JCheckBox chkTercero, chkActivo;
    private JComboBox<String> cbProveedor;
    private boolean confirmed = false;
    private ServicioProducto servicio;
    private CategoriaServicioProductoDAO catDAO = new CategoriaServicioProductoDAO();
    private ProveedorDAO provDAO = new ProveedorDAO();
    private List<CategoriaServicioProducto> categorias;
    private List<Proveedor> proveedores;

    public ServicioProductoDialog(JFrame parent, ServicioProducto s) {
        super(parent, s == null ? "Agregar Servicio/Producto" : "Editar Servicio/Producto", true);
        this.servicio = (s != null) ? s : new ServicioProducto();
        loadCombos();
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void loadCombos() {
        try {
            categorias = catDAO.findAll();
            proveedores = provDAO.findAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfCodigo = new JTextField(15);
        tfNombre = new JTextField(15);
        tfDescripcion = new JTextField(15);
        tfPrecio = new JTextField(15);
        cbCategoria = new JComboBox<>();
        cbProveedor = new JComboBox<>();
        cbProveedor.addItem("(Ninguno)");
        if (categorias != null) categorias.forEach(c -> cbCategoria.addItem(c.getNombre()));
        if (proveedores != null) proveedores.forEach(p -> cbProveedor.addItem(p.getNombre() + " (ID:" + p.getIdProveedor() + ")"));
        chkTercero = new JCheckBox("Es de terceros");
        chkActivo = new JCheckBox("Activo", true);

        if (servicio.getCodServicio() != null) {
            tfCodigo.setText(servicio.getCodServicio());
            tfCodigo.setEditable(false);
            tfNombre.setText(servicio.getNombre());
            tfDescripcion.setText(servicio.getDescripcion());
            tfPrecio.setText(String.valueOf(servicio.getPrecioUnitario()));
            // set categoria
            if (categorias != null) {
                for (int i = 0; i < categorias.size(); i++) {
                    if (categorias.get(i).getIdCategoria() == servicio.getIdCategoria()) {
                        cbCategoria.setSelectedIndex(i);
                        break;
                    }
                }
            }
            chkTercero.setSelected(servicio.isEsTercero());
            chkActivo.setSelected(servicio.isEstadoActivo());
            if (servicio.getIdProveedor() != null && proveedores != null) {
                for (int i = 0; i < proveedores.size(); i++) {
                    if (proveedores.get(i).getIdProveedor() == servicio.getIdProveedor()) {
                        cbProveedor.setSelectedIndex(i + 1); // +1 because index 0 is "(Ninguno)"
                        break;
                    }
                }
            }
        }

        int y = 0;
        addField("Código:", tfCodigo, y++, gbc);
        addField("Nombre:", tfNombre, y++, gbc);
        addField("Descripción:", tfDescripcion, y++, gbc);
        addField("Precio Unitario:", tfPrecio, y++, gbc);
        addField("Categoría:", cbCategoria, y++, gbc);
        addField("Proveedor:", cbProveedor, y++, gbc);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(chkTercero, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(chkActivo, gbc); y++;
        gbc.gridwidth = 1;

        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        btnOk.addActionListener(e -> {
            servicio.setCodServicio(tfCodigo.getText());
            servicio.setNombre(tfNombre.getText());
            servicio.setDescripcion(tfDescripcion.getText());
            try {
                servicio.setPrecioUnitario(Double.parseDouble(tfPrecio.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio inválido");
                return;
            }
            if (cbCategoria.getSelectedIndex() >= 0 && categorias != null) {
                servicio.setIdCategoria(categorias.get(cbCategoria.getSelectedIndex()).getIdCategoria());
            }
            servicio.setEsTercero(chkTercero.isSelected());
            servicio.setEstadoActivo(chkActivo.isSelected());
            if (cbProveedor.getSelectedIndex() > 0 && proveedores != null) {
                servicio.setIdProveedor(proveedores.get(cbProveedor.getSelectedIndex() - 1).getIdProveedor());
            } else {
                servicio.setIdProveedor(null);
            }
            confirmed = true;
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());
    }

    private void addField(String label, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    public boolean isConfirmed() { return confirmed; }
    public ServicioProducto getServicio() { return servicio; }
}