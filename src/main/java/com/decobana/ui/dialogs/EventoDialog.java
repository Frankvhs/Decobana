package com.decobana.ui.dialogs;

import com.decobana.dao.ClienteDAO;
import com.decobana.dao.ServicioProductoDAO;
import com.decobana.dao.EmpleadoDAO;
import com.decobana.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventoDialog extends JDialog {
    private JTextField tfNombre, tfTipo, tfUbicacion, tfInvitados;
    private JSpinner spFechaIni, spHoraIni, spFechaFin, spHoraFin;
    private JComboBox<String> cbCliente;
    private List<Cliente> clientes;
    private ClienteDAO clienteDAO = new ClienteDAO();

    // Sub lists
    private List<String> temas = new ArrayList<>();
    private List<EventoServicio> servicios = new ArrayList<>();
    private List<EventoEmpleado> empleados = new ArrayList<>();

    private JTable tableTemas, tableServicios, tableEmpleados;
    private DefaultTableModel temasModel, serviciosModel, empleadosModel;

    private boolean confirmed = false;
    private Evento evento;

    public EventoDialog(JFrame parent, Evento ev) {
        super(parent, ev == null ? "Agregar Evento" : "Editar Evento", true);
        this.evento = (ev != null) ? ev : new Evento();
        loadClientes();
        initComponents();
        setSize(700, 600);
        setLocationRelativeTo(parent);
    }

    private void loadClientes() {
        try {
            clientes = clienteDAO.findAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane mainTabs = new JTabbedPane();

        // General data panel
        JPanel panelGeneral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNombre = new JTextField(15);
        tfTipo = new JTextField(15);
        tfUbicacion = new JTextField(15);
        tfInvitados = new JTextField(5);

        // Spinners for date/time
        spFechaIni = new JSpinner(new SpinnerDateModel());
        spFechaIni.setEditor(new JSpinner.DateEditor(spFechaIni, "yyyy-MM-dd"));
        spHoraIni = new JSpinner(new SpinnerDateModel());
        spHoraIni.setEditor(new JSpinner.DateEditor(spHoraIni, "HH:mm"));

        spFechaFin = new JSpinner(new SpinnerDateModel());
        spFechaFin.setEditor(new JSpinner.DateEditor(spFechaFin, "yyyy-MM-dd"));
        spHoraFin = new JSpinner(new SpinnerDateModel());
        spHoraFin.setEditor(new JSpinner.DateEditor(spHoraFin, "HH:mm"));

        cbCliente = new JComboBox<>();
        if (clientes != null) {
            for (Cliente c : clientes) {
                cbCliente.addItem(c.getNombre() + " " + c.getApellidos() + " (ID:" + c.getIdCliente() + ")");
            }
        }

        if (evento.getIdEvento() != 0) {
            tfNombre.setText(evento.getNombreEvento());
            tfTipo.setText(evento.getTipoEvento());
            tfUbicacion.setText(evento.getUbicacion());
            tfInvitados.setText(String.valueOf(evento.getNumInvitados()));
            LocalDateTime ini = evento.getFechaHoraIni();
            spFechaIni.setValue(java.sql.Timestamp.valueOf(ini));
            spHoraIni.setValue(java.sql.Timestamp.valueOf(ini));
            LocalDateTime fin = evento.getFechaHoraFin();
            spFechaFin.setValue(java.sql.Timestamp.valueOf(fin));
            spHoraFin.setValue(java.sql.Timestamp.valueOf(fin));
            // cliente selection
            if (clientes != null) {
                for (int i = 0; i < clientes.size(); i++) {
                    if (clientes.get(i).getIdCliente() == evento.getIdCliente()) {
                        cbCliente.setSelectedIndex(i);
                        break;
                    }
                }
            }
            // copy sub lists
            if (evento.getTemas() != null) temas = new ArrayList<>(evento.getTemas());
            if (evento.getServicios() != null) servicios = new ArrayList<>(evento.getServicios());
            if (evento.getEmpleados() != null) empleados = new ArrayList<>(evento.getEmpleados());
        }

        int y = 0;
        addField(panelGeneral, "Nombre:", tfNombre, y++, gbc);
        addField(panelGeneral, "Tipo:", tfTipo, y++, gbc);
        addField(panelGeneral, "Ubicación:", tfUbicacion, y++, gbc);
        addField(panelGeneral, "Invitados:", tfInvitados, y++, gbc);
        addField(panelGeneral, "Fecha Inicio:", spFechaIni, y++, gbc);
        addField(panelGeneral, "Hora Inicio:", spHoraIni, y++, gbc);
        addField(panelGeneral, "Fecha Fin:", spFechaFin, y++, gbc);
        addField(panelGeneral, "Hora Fin:", spHoraFin, y++, gbc);
        addField(panelGeneral, "Cliente:", cbCliente, y++, gbc);

        mainTabs.addTab("Datos Generales", panelGeneral);

        // Temas tab
        temasModel = new DefaultTableModel(new Object[]{"Tema / Concepto"}, 0);
        tableTemas = new JTable(temasModel);
        JPanel panelTemas = new JPanel(new BorderLayout());
        panelTemas.add(new JScrollPane(tableTemas), BorderLayout.CENTER);
        JPanel btnTemas = new JPanel();
        JButton btnAddTema = new JButton("Agregar Tema");
        btnAddTema.addActionListener(e -> {
            String t = JOptionPane.showInputDialog(this, "Tema:");
            if (t != null && !t.trim().isEmpty()) {
                temas.add(t.trim());
                temasModel.addRow(new Object[]{t.trim()});
            }
        });
        JButton btnDelTema = new JButton("Eliminar Tema");
        btnDelTema.addActionListener(e -> {
            int row = tableTemas.getSelectedRow();
            if (row != -1) {
                temas.remove(row);
                temasModel.removeRow(row);
            }
        });
        btnTemas.add(btnAddTema);
        btnTemas.add(btnDelTema);
        panelTemas.add(btnTemas, BorderLayout.SOUTH);
        mainTabs.addTab("Temas", panelTemas);

        // Servicios tab
        serviciosModel = new DefaultTableModel(new Object[]{"Código Servicio","Cantidad"}, 0);
        tableServicios = new JTable(serviciosModel);
        JPanel panelServicios = new JPanel(new BorderLayout());
        panelServicios.add(new JScrollPane(tableServicios), BorderLayout.CENTER);
        JPanel btnServ = new JPanel();
        JButton btnAddServ = new JButton("Agregar Servicio");
        btnAddServ.addActionListener(e -> {
            String cod = JOptionPane.showInputDialog(this, "Código del servicio:");
            if (cod != null && !cod.trim().isEmpty()) {
                String cantStr = JOptionPane.showInputDialog(this, "Cantidad:");
                try {
                    int cant = Integer.parseInt(cantStr);
                    EventoServicio es = new EventoServicio();
                    es.setCodServicio(cod.trim());
                    es.setCantidad(cant);
                    servicios.add(es);
                    serviciosModel.addRow(new Object[]{cod.trim(), cant});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida");
                }
            }
        });
        JButton btnDelServ = new JButton("Eliminar Servicio");
        btnDelServ.addActionListener(e -> {
            int row = tableServicios.getSelectedRow();
            if (row != -1) {
                servicios.remove(row);
                serviciosModel.removeRow(row);
            }
        });
        btnServ.add(btnAddServ);
        btnServ.add(btnDelServ);
        panelServicios.add(btnServ, BorderLayout.SOUTH);
        mainTabs.addTab("Servicios", panelServicios);

        // Empleados tab
        empleadosModel = new DefaultTableModel(new Object[]{"ID Empleado","Responsabilidades"}, 0);
        tableEmpleados = new JTable(empleadosModel);
        JPanel panelEmpleados = new JPanel(new BorderLayout());
        panelEmpleados.add(new JScrollPane(tableEmpleados), BorderLayout.CENTER);
        JPanel btnEmp = new JPanel();
        JButton btnAddEmp = new JButton("Agregar Empleado");
        btnAddEmp.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID del empleado:");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int idEmp = Integer.parseInt(idStr);
                    String resp = JOptionPane.showInputDialog(this, "Responsabilidades:");
                    EventoEmpleado ee = new EventoEmpleado();
                    ee.setIdEmpleado(idEmp);
                    ee.setResponsabilidades(resp);
                    empleados.add(ee);
                    empleadosModel.addRow(new Object[]{idEmp, resp});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido");
                }
            }
        });
        JButton btnDelEmp = new JButton("Eliminar Empleado");
        btnDelEmp.addActionListener(e -> {
            int row = tableEmpleados.getSelectedRow();
            if (row != -1) {
                empleados.remove(row);
                empleadosModel.removeRow(row);
            }
        });
        btnEmp.add(btnAddEmp);
        btnEmp.add(btnDelEmp);
        panelEmpleados.add(btnEmp, BorderLayout.SOUTH);
        mainTabs.addTab("Empleados", panelEmpleados);

        add(mainTabs, BorderLayout.CENTER);

        // Buttons OK/Cancel
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

        // Populate sub tables from existing lists
        for (String t : temas) temasModel.addRow(new Object[]{t});
        for (EventoServicio es : servicios) serviciosModel.addRow(new Object[]{es.getCodServicio(), es.getCantidad()});
        for (EventoEmpleado ee : empleados) empleadosModel.addRow(new Object[]{ee.getIdEmpleado(), ee.getResponsabilidades()});
    }

    private boolean validar() {
        if (tfNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
            return false;
        }
        // optional further validations
        return true;
    }

    private void aplicarDatos() {
        evento.setNombreEvento(tfNombre.getText().trim());
        evento.setTipoEvento(tfTipo.getText().trim());
        evento.setUbicacion(tfUbicacion.getText().trim());
        try {
            evento.setNumInvitados(Integer.parseInt(tfInvitados.getText().trim()));
        } catch (NumberFormatException e) {
            evento.setNumInvitados(0);
        }

        // date/time
        java.util.Date fechaIni = (java.util.Date) spFechaIni.getValue();
        java.util.Date horaIni = (java.util.Date) spHoraIni.getValue();
        LocalDate dateIni = new java.sql.Date(fechaIni.getTime()).toLocalDate();
        LocalTime timeIni = new java.sql.Time(horaIni.getTime()).toLocalTime();
        evento.setFechaHoraIni(LocalDateTime.of(dateIni, timeIni));

        java.util.Date fechaFin = (java.util.Date) spFechaFin.getValue();
        java.util.Date horaFin = (java.util.Date) spHoraFin.getValue();
        LocalDate dateFin = new java.sql.Date(fechaFin.getTime()).toLocalDate();
        LocalTime timeFin = new java.sql.Time(horaFin.getTime()).toLocalTime();
        evento.setFechaHoraFin(LocalDateTime.of(dateFin, timeFin));

        // cliente
        if (cbCliente.getSelectedIndex() >= 0 && clientes != null) {
            evento.setIdCliente(clientes.get(cbCliente.getSelectedIndex()).getIdCliente());
        }

        evento.setTemas(new ArrayList<>(temas));
        evento.setServicios(new ArrayList<>(servicios));
        evento.setEmpleados(new ArrayList<>(empleados));
    }

    private void addField(JPanel panel, String label, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public boolean isConfirmed() { return confirmed; }
    public Evento getEvento() { return evento; }
}