package com.decobana.ui.dialogs;

import com.decobana.dao.ClienteDAO;
import com.decobana.dao.EventoDAO;
import com.decobana.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ContratoDialog extends JDialog {
    private JTextField tfNumContrato, tfFecha, tfTerminos;
    private JComboBox<String> cbCliente, cbEvento;
    private List<Cliente> clientes;
    private List<Evento> eventos;
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EventoDAO eventoDAO = new EventoDAO();

    private List<ContratoLinea> lineas = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();

    private JTable tableLineas, tablePagos;
    private DefaultTableModel lineasModel, pagosModel;

    private boolean confirmed = false;
    private Contrato contrato;

    public ContratoDialog(JFrame parent, Contrato c) {
        super(parent, c == null ? "Agregar Contrato" : "Editar Contrato", true);
        this.contrato = (c != null) ? c : new Contrato();
        loadCombos();
        initComponents();
        setSize(700, 500);
        setLocationRelativeTo(parent);
    }

    private void loadCombos() {
        try {
            clientes = clienteDAO.findAll();
            eventos = eventoDAO.findAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Datos generales
        JPanel panelGeneral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNumContrato = new JTextField(15);
        tfFecha = new JTextField(10);
        tfTerminos = new JTextField(15);

        cbCliente = new JComboBox<>();
        cbEvento = new JComboBox<>();

        if (clientes != null) {
            for (Cliente cl : clientes)
                cbCliente.addItem(cl.getNombre() + " " + cl.getApellidos() + " (ID:" + cl.getIdCliente() + ")");
        }
        if (eventos != null) {
            for (Evento ev : eventos)
                cbEvento.addItem(ev.getNombreEvento() + " (ID:" + ev.getIdEvento() + ")");
        }

        if (contrato.getNumContrato() != null) {
            tfNumContrato.setText(contrato.getNumContrato());
            tfNumContrato.setEditable(false);
            tfFecha.setText(contrato.getFechaContrato().toString());
            tfTerminos.setText(contrato.getTerminosCond());
            if (clientes != null) {
                for (int i = 0; i < clientes.size(); i++) {
                    if (clientes.get(i).getIdCliente() == contrato.getIdCliente()) {
                        cbCliente.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (eventos != null) {
                for (int i = 0; i < eventos.size(); i++) {
                    if (eventos.get(i).getIdEvento() == contrato.getIdEvento()) {
                        cbEvento.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (contrato.getLineas() != null) lineas = new ArrayList<>(contrato.getLineas());
            if (contrato.getPagos() != null) pagos = new ArrayList<>(contrato.getPagos());
        }

        int y = 0;
        addField(panelGeneral, "Nº Contrato:", tfNumContrato, y++, gbc);
        addField(panelGeneral, "Fecha (YYYY-MM-DD):", tfFecha, y++, gbc);
        addField(panelGeneral, "Términos:", tfTerminos, y++, gbc);
        addField(panelGeneral, "Cliente:", cbCliente, y++, gbc);
        addField(panelGeneral, "Evento:", cbEvento, y++, gbc);

        tabbedPane.addTab("General", panelGeneral);

        // Líneas
        lineasModel = new DefaultTableModel(new Object[]{"Código Servicio","Cantidad","Precio"}, 0);
        tableLineas = new JTable(lineasModel);
        JPanel panelLineas = new JPanel(new BorderLayout());
        panelLineas.add(new JScrollPane(tableLineas), BorderLayout.CENTER);
        JPanel btnLineas = new JPanel();
        JButton btnAddLinea = new JButton("Agregar Línea");
        btnAddLinea.addActionListener(e -> {
            String cod = JOptionPane.showInputDialog(this, "Código servicio:");
            if (cod != null && !cod.trim().isEmpty()) {
                String cantStr = JOptionPane.showInputDialog(this, "Cantidad:");
                String precioStr = JOptionPane.showInputDialog(this, "Precio negociado:");
                try {
                    int cant = Integer.parseInt(cantStr);
                    double precio = Double.parseDouble(precioStr);
                    ContratoLinea l = new ContratoLinea();
                    l.setCodServicio(cod.trim());
                    l.setCantidad(cant);
                    l.setPrecioNegociado(precio);
                    lineas.add(l);
                    lineasModel.addRow(new Object[]{cod.trim(), cant, precio});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valor numérico inválido");
                }
            }
        });
        JButton btnDelLinea = new JButton("Eliminar Línea");
        btnDelLinea.addActionListener(e -> {
            int row = tableLineas.getSelectedRow();
            if (row != -1) {
                lineas.remove(row);
                lineasModel.removeRow(row);
            }
        });
        btnLineas.add(btnAddLinea);
        btnLineas.add(btnDelLinea);
        panelLineas.add(btnLineas, BorderLayout.SOUTH);
        tabbedPane.addTab("Líneas", panelLineas);

        // Pagos
        pagosModel = new DefaultTableModel(new Object[]{"Fecha Pago","Monto"}, 0);
        tablePagos = new JTable(pagosModel);
        JPanel panelPagos = new JPanel(new BorderLayout());
        panelPagos.add(new JScrollPane(tablePagos), BorderLayout.CENTER);
        JPanel btnPagos = new JPanel();
        JButton btnAddPago = new JButton("Agregar Pago");
        btnAddPago.addActionListener(e -> {
            String fechaStr = JOptionPane.showInputDialog(this, "Fecha (YYYY-MM-DD):");
            if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                String montoStr = JOptionPane.showInputDialog(this, "Monto:");
                try {
                    LocalDate fecha = LocalDate.parse(fechaStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                    double monto = Double.parseDouble(montoStr);
                    Pago p = new Pago();
                    p.setFechaPago(fecha);
                    p.setMonto(monto);
                    pagos.add(p);
                    pagosModel.addRow(new Object[]{fecha.toString(), monto});
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos");
                }
            }
        });
        JButton btnDelPago = new JButton("Eliminar Pago");
        btnDelPago.addActionListener(e -> {
            int row = tablePagos.getSelectedRow();
            if (row != -1) {
                pagos.remove(row);
                pagosModel.removeRow(row);
            }
        });
        btnPagos.add(btnAddPago);
        btnPagos.add(btnDelPago);
        panelPagos.add(btnPagos, BorderLayout.SOUTH);
        tabbedPane.addTab("Pagos", panelPagos);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        bottomPanel.add(btnOk);
        bottomPanel.add(btnCancel);
        add(bottomPanel, BorderLayout.SOUTH);

        btnOk.addActionListener(e -> {
            if (validar()) {
                aplicarDatos();
                confirmed = true;
                dispose();
            }
        });
        btnCancel.addActionListener(e -> dispose());

        // Populate existing data
        for (ContratoLinea l : lineas) lineasModel.addRow(new Object[]{l.getCodServicio(), l.getCantidad(), l.getPrecioNegociado()});
        for (Pago p : pagos) pagosModel.addRow(new Object[]{p.getFechaPago().toString(), p.getMonto()});
    }

    private boolean validar() {
        if (tfNumContrato.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Número de contrato obligatorio");
            return false;
        }
        return true;
    }

    private void aplicarDatos() {
        contrato.setNumContrato(tfNumContrato.getText().trim());
        try {
            contrato.setFechaContrato(LocalDate.parse(tfFecha.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (Exception e) {
            contrato.setFechaContrato(LocalDate.now());
        }
        contrato.setTerminosCond(tfTerminos.getText().trim());
        if (cbCliente.getSelectedIndex() >= 0 && clientes != null) {
            contrato.setIdCliente(clientes.get(cbCliente.getSelectedIndex()).getIdCliente());
        }
        if (cbEvento.getSelectedIndex() >= 0 && eventos != null) {
            contrato.setIdEvento(eventos.get(cbEvento.getSelectedIndex()).getIdEvento());
        }
        contrato.setLineas(new ArrayList<>(lineas));
        contrato.setPagos(new ArrayList<>(pagos));
    }

    private void addField(JPanel panel, String label, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public boolean isConfirmed() { return confirmed; }
    public Contrato getContrato() { return contrato; }
}