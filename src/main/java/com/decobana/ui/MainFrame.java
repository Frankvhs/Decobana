package com.decobana.ui;

import com.decobana.ui.panels.*;
import com.decobana.ui.reports.*;

import javax.swing.*;
import java.awt.*;

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
        setJMenuBar(menuBar);
    }

    private void addReportMenuItem(JMenu menu, String title, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(listener);
        menu.add(item);
    }
}