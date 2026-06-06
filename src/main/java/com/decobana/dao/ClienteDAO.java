package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void insert(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, apellidos, num_documento, direccion, telefono, email, trato_pref) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getNumDocumento());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getEmail());
            ps.setBoolean(7, c.isTratoPref());
            ps.executeUpdate();
        }
    }

    public void update(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET nombre=?, apellidos=?, num_documento=?, direccion=?, telefono=?, email=?, trato_pref=? WHERE id_cliente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getNumDocumento());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getEmail());
            ps.setBoolean(7, c.isTratoPref());
            ps.setInt(8, c.getIdCliente());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Cliente findById(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id_cliente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Cliente> findAll() throws SQLException {
        List<Cliente> list = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setNombre(rs.getString("nombre"));
        c.setApellidos(rs.getString("apellidos"));
        c.setNumDocumento(rs.getString("num_documento"));
        c.setDireccion(rs.getString("direccion"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setTratoPref(rs.getBoolean("trato_pref"));
        return c;
    }
}