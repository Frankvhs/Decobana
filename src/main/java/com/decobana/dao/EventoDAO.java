package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    public void insert(Evento e) throws SQLException {
        String sql = "INSERT INTO eventos (nombre_evento, tipo_evento, fecha_hora_ini, fecha_hora_fin, ubicacion, num_invitados, id_cliente) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombreEvento());
            ps.setString(2, e.getTipoEvento());
            ps.setTimestamp(3, Timestamp.valueOf(e.getFechaHoraIni()));
            ps.setTimestamp(4, Timestamp.valueOf(e.getFechaHoraFin()));
            ps.setString(5, e.getUbicacion());
            ps.setInt(6, e.getNumInvitados());
            ps.setInt(7, e.getIdCliente());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                e.setIdEvento(keys.getInt(1));
            }
            // Insert temas
            if (e.getTemas() != null) {
                for (String tema : e.getTemas()) {
                    insertTema(e.getIdEvento(), tema, conn);
                }
            }
            // Insert servicios
            if (e.getServicios() != null) {
                for (EventoServicio es : e.getServicios()) {
                    insertEventoServicio(e.getIdEvento(), es, conn);
                }
            }
            // Insert empleados
            if (e.getEmpleados() != null) {
                for (EventoEmpleado ee : e.getEmpleados()) {
                    insertEventoEmpleado(e.getIdEvento(), ee, conn);
                }
            }
        }
    }

    public void update(Evento e) throws SQLException {
        String sql = "UPDATE eventos SET nombre_evento=?, tipo_evento=?, fecha_hora_ini=?, fecha_hora_fin=?, ubicacion=?, num_invitados=?, id_cliente=? WHERE id_evento=?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getNombreEvento());
                ps.setString(2, e.getTipoEvento());
                ps.setTimestamp(3, Timestamp.valueOf(e.getFechaHoraIni()));
                ps.setTimestamp(4, Timestamp.valueOf(e.getFechaHoraFin()));
                ps.setString(5, e.getUbicacion());
                ps.setInt(6, e.getNumInvitados());
                ps.setInt(7, e.getIdCliente());
                ps.setInt(8, e.getIdEvento());
                ps.executeUpdate();
            }
            // Eliminar y reinsertar temas, servicios, empleados
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM eventos_temas WHERE id_evento=?")) {
                del.setInt(1, e.getIdEvento());
                del.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM eventos_servicios WHERE id_evento=?")) {
                del.setInt(1, e.getIdEvento());
                del.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM eventos_empleados WHERE id_evento=?")) {
                del.setInt(1, e.getIdEvento());
                del.executeUpdate();
            }
            if (e.getTemas() != null) {
                for (String tema : e.getTemas()) {
                    insertTema(e.getIdEvento(), tema, conn);
                }
            }
            if (e.getServicios() != null) {
                for (EventoServicio es : e.getServicios()) {
                    insertEventoServicio(e.getIdEvento(), es, conn);
                }
            }
            if (e.getEmpleados() != null) {
                for (EventoEmpleado ee : e.getEmpleados()) {
                    insertEventoEmpleado(e.getIdEvento(), ee, conn);
                }
            }
            conn.commit();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM eventos WHERE id_evento=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Evento findById(int id) throws SQLException {
        String sql = "SELECT * FROM eventos WHERE id_evento=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Evento e = mapRow(rs);
                e.setTemas(findTemas(e.getIdEvento(), conn));
                e.setServicios(findServicios(e.getIdEvento(), conn));
                e.setEmpleados(findEmpleados(e.getIdEvento(), conn));
                return e;
            }
        }
        return null;
    }

    public List<Evento> findAll() throws SQLException {
        List<Evento> list = new ArrayList<>();
        String sql = "SELECT * FROM eventos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Evento e = mapRow(rs);
                e.setTemas(findTemas(e.getIdEvento(), conn));
                e.setServicios(findServicios(e.getIdEvento(), conn));
                e.setEmpleados(findEmpleados(e.getIdEvento(), conn));
                list.add(e);
            }
        }
        return list;
    }

    private Evento mapRow(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setIdEvento(rs.getInt("id_evento"));
        e.setNombreEvento(rs.getString("nombre_evento"));
        e.setTipoEvento(rs.getString("tipo_evento"));
        e.setFechaHoraIni(rs.getTimestamp("fecha_hora_ini").toLocalDateTime());
        e.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());
        e.setUbicacion(rs.getString("ubicacion"));
        e.setNumInvitados(rs.getInt("num_invitados"));
        e.setIdCliente(rs.getInt("id_cliente"));
        return e;
    }

    private void insertTema(int idEvento, String tema, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO eventos_temas (id_evento, tema) VALUES (?,?)")) {
            ps.setInt(1, idEvento);
            ps.setString(2, tema);
            ps.executeUpdate();
        }
    }

    private void insertEventoServicio(int idEvento, EventoServicio es, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO eventos_servicios (id_evento, cod_servicio, cantidad) VALUES (?,?,?)")) {
            ps.setInt(1, idEvento);
            ps.setString(2, es.getCodServicio());
            ps.setInt(3, es.getCantidad());
            ps.executeUpdate();
        }
    }

    private void insertEventoEmpleado(int idEvento, EventoEmpleado ee, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO eventos_empleados (id_evento, id_empleado, responsabilidades) VALUES (?,?,?)")) {
            ps.setInt(1, idEvento);
            ps.setInt(2, ee.getIdEmpleado());
            ps.setString(3, ee.getResponsabilidades());
            ps.executeUpdate();
        }
    }

    private List<String> findTemas(int idEvento, Connection conn) throws SQLException {
        List<String> temas = new ArrayList<>();
        String sql = "SELECT tema FROM eventos_temas WHERE id_evento=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) temas.add(rs.getString("tema"));
        }
        return temas;
    }

    private List<EventoServicio> findServicios(int idEvento, Connection conn) throws SQLException {
        List<EventoServicio> list = new ArrayList<>();
        String sql = "SELECT * FROM eventos_servicios WHERE id_evento=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventoServicio es = new EventoServicio();
                es.setIdEvento(idEvento);
                es.setCodServicio(rs.getString("cod_servicio"));
                es.setCantidad(rs.getInt("cantidad"));
                list.add(es);
            }
        }
        return list;
    }

    private List<EventoEmpleado> findEmpleados(int idEvento, Connection conn) throws SQLException {
        List<EventoEmpleado> list = new ArrayList<>();
        String sql = "SELECT * FROM eventos_empleados WHERE id_evento=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventoEmpleado ee = new EventoEmpleado();
                ee.setIdEvento(idEvento);
                ee.setIdEmpleado(rs.getInt("id_empleado"));
                ee.setResponsabilidades(rs.getString("responsabilidades"));
                list.add(ee);
            }
        }
        return list;
    }
}