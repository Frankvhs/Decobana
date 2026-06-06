package com.decobana.ui;

import com.decobana.db.MockDataLoader;
import com.decobana.ui.panels.*;
import com.decobana.ui.reports.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ClientePanel clientePanel;
    private ProveedorPanel proveedorPanel;
    private EmpleadoPanel empleadoPanel;
    private ServicioProductoPanel servicioPanel;
    private EventoPanel eventoPanel;
    private ContratoPanel contratoPanel;

    public MainFrame() {
        setTitle("Decobana - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        clientePanel = new ClientePanel();
        proveedorPanel = new ProveedorPanel();
        empleadoPanel = new EmpleadoPanel();
        servicioPanel = new ServicioProductoPanel();
        eventoPanel = new EventoPanel();
        contratoPanel = new ContratoPanel();

        tabbedPane.addTab("Clientes", clientePanel);
        tabbedPane.addTab("Proveedores", proveedorPanel);
        tabbedPane.addTab("Empleados", empleadoPanel);
        tabbedPane.addTab("Servicios", servicioPanel);
        tabbedPane.addTab("Eventos", eventoPanel);
        tabbedPane.addTab("Contratos", contratoPanel);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Menú Reportes
        JMenuBar menuBar = new JMenuBar();
        JMenu reportMenu = new JMenu("Reportes");
        addReportMenuItem(reportMenu, "Ficha Empresa", e -> new ReporteFichaEmpresa(this).setVisible(true));
        addReportMenuItem(reportMenu, "Ficha Cliente", e -> new ReporteFichaCliente(this).setVisible(true));
        addReportMenuItem(reportMenu, "Ficha Proveedor", e -> new ReporteFichaProveedor(this).setVisible(true));
        addReportMenuItem(reportMenu, "Eventos en período", e -> new ReporteEventosPeriodo(this).setVisible(true));
        addReportMenuItem(reportMenu, "Servicios por evento", e -> new ReporteServiciosEvento(this).setVisible(true));
        addReportMenuItem(reportMenu, "Empleados por evento", e -> new ReporteEmpleadosEvento(this).setVisible(true));
        addReportMenuItem(reportMenu, "Contratos por cliente (período)", e -> new ReporteContratosCliente(this).setVisible(true));
        addReportMenuItem(reportMenu, "Estado proveedores", e -> new ReporteEstadoProveedores(this).setVisible(true));
        addReportMenuItem(reportMenu, "Consolidado anual", e -> new ReporteConsolidadoAnual(this).setVisible(true));
        addReportMenuItem(reportMenu, "Modificaciones contratos", e -> new ReporteModificacionesContratos(this).setVisible(true));
        menuBar.add(reportMenu);

        JButton btnMockData = new JButton("[ ! ] Cargar Datos de Prueba");
        btnMockData.addActionListener(e -> cargarDatosMock());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(btnMockData);

        setJMenuBar(menuBar);
    }

    private void addReportMenuItem(JMenu menu, String title, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(listener);
        menu.add(item);
    }

    private void cargarDatosMock() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Esta acción insertará datos de prueba en la base de datos.\n" +
                "Si ya existen algunos registros, se ignorarán duplicados.\n" +
                "¿Desea continuar?",
                "Cargar datos mock",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MockDataLoader.loadMockData();
                JOptionPane.showMessageDialog(this,
                        "Datos mock cargados exitosamente.\n" +
                        "Puede actualizar las pestañas para ver la nueva información.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                
                clientePanel.cargarTabla();
                proveedorPanel.cargarTabla();
                empleadoPanel.cargarTabla();
                servicioPanel.cargarTabla();
                eventoPanel.cargarEventos();
                contratoPanel.cargarContratos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar datos mock: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}