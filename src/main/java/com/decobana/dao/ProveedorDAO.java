package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    public void insert(Proveedor p) throws SQLException {
        String sql = "INSERT INTO proveedores (nombre, tipo_servicio, direccion, telefono, email, responsable) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipoServicio());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getResponsable());
            ps.executeUpdate();
        }
    }

    public void update(Proveedor p) throws SQLException {
        String sql = "UPDATE proveedores SET nombre=?, tipo_servicio=?, direccion=?, telefono=?, email=?, responsable=? WHERE id_proveedor=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipoServicio());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getResponsable());
            ps.setInt(7, p.getIdProveedor());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM proveedores WHERE id_proveedor=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Proveedor findById(int id) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE id_proveedor=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Proveedor> findAll() throws SQLException {
        List<Proveedor> list = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Proveedor mapRow(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setNombre(rs.getString("nombre"));
        p.setTipoServicio(rs.getString("tipo_servicio"));
        p.setDireccion(rs.getString("direccion"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setResponsable(rs.getString("responsable"));
        return p;
    }
}