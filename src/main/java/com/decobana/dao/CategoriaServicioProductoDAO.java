package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.CategoriaServicioProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaServicioProductoDAO {
    public List<CategoriaServicioProducto> findAll() throws SQLException {
        List<CategoriaServicioProducto> list = new ArrayList<>();
        String sql = "SELECT * FROM categorias_servicios_productos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CategoriaServicioProducto c = new CategoriaServicioProducto();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                list.add(c);
            }
        }
        return list;
    }

    public CategoriaServicioProducto findById(int id) throws SQLException {
        String sql = "SELECT * FROM categorias_servicios_productos WHERE id_categoria=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CategoriaServicioProducto c = new CategoriaServicioProducto();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                return c;
            }
        }
        return null;
    }
}