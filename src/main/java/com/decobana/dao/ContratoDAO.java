package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {
    public void insert(Contrato c) throws SQLException {
        String sql = "INSERT INTO contratos (num_contrato, fecha_contrato, terminos_cond, id_cliente, id_evento) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getNumContrato());
                ps.setDate(2, Date.valueOf(c.getFechaContrato()));
                ps.setString(3, c.getTerminosCond());
                ps.setInt(4, c.getIdCliente());
                ps.setInt(5, c.getIdEvento());
                ps.executeUpdate();
            }
            if (c.getLineas() != null) {
                for (ContratoLinea l : c.getLineas()) {
                    insertLinea(c.getNumContrato(), l, conn);
                }
            }
            if (c.getPagos() != null) {
                for (Pago p : c.getPagos()) {
                    insertPago(c.getNumContrato(), p, conn);
                }
            }
            conn.commit();
        }
    }

    public void update(Contrato c) throws SQLException {
        String sql = "UPDATE contratos SET fecha_contrato=?, terminos_cond=?, id_cliente=?, id_evento=? WHERE num_contrato=?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(c.getFechaContrato()));
                ps.setString(2, c.getTerminosCond());
                ps.setInt(3, c.getIdCliente());
                ps.setInt(4, c.getIdEvento());
                ps.setString(5, c.getNumContrato());
                ps.executeUpdate();
            }
            // delete and reinsert lineas and pagos
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM contratos_lineas WHERE num_contrato=?")) {
                del.setString(1, c.getNumContrato());
                del.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM pagos WHERE num_contrato=?")) {
                del.setString(1, c.getNumContrato());
                del.executeUpdate();
            }
            if (c.getLineas() != null) {
                for (ContratoLinea l : c.getLineas()) insertLinea(c.getNumContrato(), l, conn);
            }
            if (c.getPagos() != null) {
                for (Pago p : c.getPagos()) insertPago(c.getNumContrato(), p, conn);
            }
            conn.commit();
        }
    }

    public void delete(String num) throws SQLException {
        String sql = "DELETE FROM contratos WHERE num_contrato=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, num);
            ps.executeUpdate();
        }
    }

    public Contrato findByNum(String num) throws SQLException {
        String sql = "SELECT * FROM contratos WHERE num_contrato=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Contrato c = mapRow(rs);
                c.setLineas(findLineas(num, conn));
                c.setPagos(findPagos(num, conn));
                c.setModificaciones(findModificaciones(num, conn));
                return c;
            }
        }
        return null;
    }

    public List<Contrato> findAll() throws SQLException {
        List<Contrato> list = new ArrayList<>();
        String sql = "SELECT * FROM contratos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Contrato c = mapRow(rs);
                c.setLineas(findLineas(c.getNumContrato(), conn));
                c.setPagos(findPagos(c.getNumContrato(), conn));
                c.setModificaciones(findModificaciones(c.getNumContrato(), conn));
                list.add(c);
            }
        }
        return list;
    }

    private Contrato mapRow(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        c.setNumContrato(rs.getString("num_contrato"));
        c.setFechaContrato(rs.getDate("fecha_contrato").toLocalDate());
        c.setTerminosCond(rs.getString("terminos_cond"));
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setIdEvento(rs.getInt("id_evento"));
        return c;
    }

    private void insertLinea(String num, ContratoLinea l, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contratos_lineas (num_contrato, cod_servicio, cantidad, precio_negociado) VALUES (?,?,?,?)")) {
            ps.setString(1, num);
            ps.setString(2, l.getCodServicio());
            ps.setInt(3, l.getCantidad());
            ps.setDouble(4, l.getPrecioNegociado());
            ps.executeUpdate();
        }
    }

    private void insertPago(String num, Pago p, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO pagos (num_contrato, fecha_pago, monto) VALUES (?,?,?)")) {
            ps.setString(1, num);
            ps.setDate(2, Date.valueOf(p.getFechaPago()));
            ps.setDouble(3, p.getMonto());
            ps.executeUpdate();
        }
    }

    private List<ContratoLinea> findLineas(String num, Connection conn) throws SQLException {
        List<ContratoLinea> list = new ArrayList<>();
        String sql = "SELECT * FROM contratos_lineas WHERE num_contrato=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ContratoLinea l = new ContratoLinea();
                l.setNumContrato(num);
                l.setCodServicio(rs.getString("cod_servicio"));
                l.setCantidad(rs.getInt("cantidad"));
                l.setPrecioNegociado(rs.getDouble("precio_negociado"));
                list.add(l);
            }
        }
        return list;
    }

    private List<Pago> findPagos(String num, Connection conn) throws SQLException {
        List<Pago> list = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE num_contrato=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pago p = new Pago();
                p.setIdPago(rs.getInt("id_pago"));
                p.setNumContrato(num);
                p.setFechaPago(rs.getDate("fecha_pago").toLocalDate());
                p.setMonto(rs.getDouble("monto"));
                list.add(p);
            }
        }
        return list;
    }

    private List<ModificacionContrato> findModificaciones(String num, Connection conn) throws SQLException {
        List<ModificacionContrato> list = new ArrayList<>();
        String sql = "SELECT * FROM modificaciones_contratos WHERE num_contrato=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModificacionContrato m = new ModificacionContrato();
                m.setIdMod(rs.getInt("id_mod"));
                m.setNumContrato(num);
                m.setFechaMod(rs.getDate("fecha_mod").toLocalDate());
                m.setDescripcion(rs.getString("descripcion"));
                list.add(m);
            }
        }
        return list;
    }

    // Additional methods for adding modifications
    public void addModificacion(ModificacionContrato m) throws SQLException {
        String sql = "INSERT INTO modificaciones_contratos (num_contrato, fecha_mod, descripcion) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNumContrato());
            ps.setDate(2, Date.valueOf(m.getFechaMod()));
            ps.setString(3, m.getDescripcion());
            ps.executeUpdate();
        }
    }
}