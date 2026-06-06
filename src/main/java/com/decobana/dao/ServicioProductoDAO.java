package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.ServicioProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioProductoDAO {
    public void insert(ServicioProducto s) throws SQLException {
        String sql = "INSERT INTO servicios_productos (cod_servicio, nombre, descripcion, id_categoria, precio_unitario, es_tercero, estado_activo, id_proveedor) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getCodServicio());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getDescripcion());
            ps.setInt(4, s.getIdCategoria());
            ps.setDouble(5, s.getPrecioUnitario());
            ps.setBoolean(6, s.isEsTercero());
            ps.setBoolean(7, s.isEstadoActivo());
            if (s.getIdProveedor() != null) ps.setInt(8, s.getIdProveedor());
            else ps.setNull(8, Types.INTEGER);
            ps.executeUpdate();
        }
    }

    public void update(ServicioProducto s) throws SQLException {
        String sql = "UPDATE servicios_productos SET nombre=?, descripcion=?, id_categoria=?, precio_unitario=?, es_tercero=?, estado_activo=?, id_proveedor=? WHERE cod_servicio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDescripcion());
            ps.setInt(3, s.getIdCategoria());
            ps.setDouble(4, s.getPrecioUnitario());
            ps.setBoolean(5, s.isEsTercero());
            ps.setBoolean(6, s.isEstadoActivo());
            if (s.getIdProveedor() != null) ps.setInt(7, s.getIdProveedor());
            else ps.setNull(7, Types.INTEGER);
            ps.setString(8, s.getCodServicio());
            ps.executeUpdate();
        }
    }

    public void delete(String cod) throws SQLException {
        String sql = "DELETE FROM servicios_productos WHERE cod_servicio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cod);
            ps.executeUpdate();
        }
    }

    public ServicioProducto findByCod(String cod) throws SQLException {
        String sql = "SELECT * FROM servicios_productos WHERE cod_servicio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cod);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<ServicioProducto> findAll() throws SQLException {
        List<ServicioProducto> list = new ArrayList<>();
        String sql = "SELECT * FROM servicios_productos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private ServicioProducto mapRow(ResultSet rs) throws SQLException {
        ServicioProducto s = new ServicioProducto();
        s.setCodServicio(rs.getString("cod_servicio"));
        s.setNombre(rs.getString("nombre"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setIdCategoria(rs.getInt("id_categoria"));
        s.setPrecioUnitario(rs.getDouble("precio_unitario"));
        s.setEsTercero(rs.getBoolean("es_tercero"));
        s.setEstadoActivo(rs.getBoolean("estado_activo"));
        int idProv = rs.getInt("id_proveedor");
        s.setIdProveedor(rs.wasNull() ? null : idProv);
        return s;
    }
}