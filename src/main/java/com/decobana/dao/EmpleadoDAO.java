package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    public void insert(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, apellidos, num_documento, direccion, telefono, email, cargo, departamento) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidos());
            ps.setString(3, e.getNumDocumento());
            ps.setString(4, e.getDireccion());
            ps.setString(5, e.getTelefono());
            ps.setString(6, e.getEmail());
            ps.setString(7, e.getCargo());
            ps.setString(8, e.getDepartamento());
            ps.executeUpdate();
        }
    }

    public void update(Empleado e) throws SQLException {
        String sql = "UPDATE empleados SET nombre=?, apellidos=?, num_documento=?, direccion=?, telefono=?, email=?, cargo=?, departamento=? WHERE id_empleado=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidos());
            ps.setString(3, e.getNumDocumento());
            ps.setString(4, e.getDireccion());
            ps.setString(5, e.getTelefono());
            ps.setString(6, e.getEmail());
            ps.setString(7, e.getCargo());
            ps.setString(8, e.getDepartamento());
            ps.setInt(9, e.getIdEmpleado());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id_empleado=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Empleado findById(int id) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE id_empleado=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Empleado> findAll() throws SQLException {
        List<Empleado> list = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Empleado mapRow(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdEmpleado(rs.getInt("id_empleado"));
        e.setNombre(rs.getString("nombre"));
        e.setApellidos(rs.getString("apellidos"));
        e.setNumDocumento(rs.getString("num_documento"));
        e.setDireccion(rs.getString("direccion"));
        e.setTelefono(rs.getString("telefono"));
        e.setEmail(rs.getString("email"));
        e.setCargo(rs.getString("cargo"));
        e.setDepartamento(rs.getString("departamento"));
        return e;
    }
}