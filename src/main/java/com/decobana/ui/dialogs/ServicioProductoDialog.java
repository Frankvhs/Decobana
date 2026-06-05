package com.decobana.ui.dialogs;

import com.decobana.dao.CategoriaServicioProductoDAO;
import com.decobana.dao.ProveedorDAO;
import com.decobana.model.CategoriaServicioProducto;
import com.decobana.model.Proveedor;
import com.decobana.model.ServicioProducto;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioProductoDialog extends BaseEntityDialog<ServicioProducto> {
    private JTextField tfCodigo, tfNombre, tfDescripcion, tfPrecio;
    private JComboBox<String> cbCategoria;
    private JCheckBox chkTercero, chkActivo;
    private JComboBox<String> cbProveedor;
    private List<CategoriaServicioProducto> categorias = new ArrayList<>();
    private List<Proveedor> proveedores = new ArrayList<>();
    private CategoriaServicioProductoDAO catDAO = new CategoriaServicioProductoDAO();
    private ProveedorDAO provDAO = new ProveedorDAO();

    public ServicioProductoDialog(JFrame parent, ServicioProducto s) {
        super(parent, s == null ? "Agregar Servicio/Producto" : "Editar Servicio/Producto", s);
    }

    @Override
    protected void prepareForLoad() {
        loadCombos(); // Carga datos ANTES de que loadEntityData() los necesite
    }

    private void loadCombos() {
        try {
            categorias = catDAO.findAll();
            proveedores = provDAO.findAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Si hay error, mantener listas vacías
        }
        // Actualizar los combos después de cargar los datos
        cbCategoria.removeAllItems();
        cbProveedor.removeAllItems();
        cbProveedor.addItem("(Ninguno)");
        if (categorias != null) {
            for (CategoriaServicioProducto cat : categorias) {
                cbCategoria.addItem(cat.getNombre());
            }
        }
        if (proveedores != null) {
            for (Proveedor p : proveedores) {
                cbProveedor.addItem(p.getNombre() + " (ID:" + p.getIdProveedor() + ")");
            }
        }
    }

    @Override
    protected ServicioProducto createNewEntity() {
        return new ServicioProducto();
    }

    @Override
    protected boolean entityExists() {
        return entity != null && entity.getCodServicio() != null && !entity.getCodServicio().isEmpty();
    }

    @Override
    protected void addSpecificFields() {
        tfCodigo = new JTextField(15);
        tfNombre = new JTextField(15);
        tfDescripcion = new JTextField(15);
        tfPrecio = new JTextField(15);
        cbCategoria = new JComboBox<>();
        cbProveedor = new JComboBox<>();

        chkTercero = new JCheckBox("Es de terceros");
        chkActivo = new JCheckBox("Activo", true);

        addField("Código:", tfCodigo);
        addField("Nombre:", tfNombre);
        addField("Descripción:", tfDescripcion);
        addField("Precio Unitario:", tfPrecio);
        addField("Categoría:", cbCategoria);
        addField("Proveedor:", cbProveedor);
        addFullWidthComponent(chkTercero);
        addFullWidthComponent(chkActivo);
    }

    @Override
    protected void loadEntityData() {
        tfCodigo.setText(entity.getCodServicio());
        tfCodigo.setEditable(false);
        tfNombre.setText(entity.getNombre());
        tfDescripcion.setText(entity.getDescripcion());
        tfPrecio.setText(String.valueOf(entity.getPrecioUnitario()));
        // Seleccionar categoría
        if (categorias != null) {
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).getIdCategoria() == entity.getIdCategoria()) {
                    cbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }
        chkTercero.setSelected(entity.isEsTercero());
        chkActivo.setSelected(entity.isEstadoActivo());
        // Seleccionar proveedor
        if (entity.getIdProveedor() != null && proveedores != null) {
            for (int i = 0; i < proveedores.size(); i++) {
                if (proveedores.get(i).getIdProveedor() == entity.getIdProveedor()) {
                    cbProveedor.setSelectedIndex(i + 1); // +1 por el item "(Ninguno)"
                    break;
                }
            }
        }
    }

    @Override
    protected void saveToEntity() {
        entity.setCodServicio(tfCodigo.getText());
        entity.setNombre(tfNombre.getText());
        entity.setDescripcion(tfDescripcion.getText());
        try {
            entity.setPrecioUnitario(Double.parseDouble(tfPrecio.getText()));
        } catch (NumberFormatException e) {
            entity.setPrecioUnitario(0.0);
        }
        if (cbCategoria.getSelectedIndex() >= 0 && categorias != null) {
            entity.setIdCategoria(categorias.get(cbCategoria.getSelectedIndex()).getIdCategoria());
        }
        entity.setEsTercero(chkTercero.isSelected());
        entity.setEstadoActivo(chkActivo.isSelected());
        if (cbProveedor.getSelectedIndex() > 0 && proveedores != null) {
            entity.setIdProveedor(proveedores.get(cbProveedor.getSelectedIndex() - 1).getIdProveedor());
        } else {
            entity.setIdProveedor(null);
        }
    }
}