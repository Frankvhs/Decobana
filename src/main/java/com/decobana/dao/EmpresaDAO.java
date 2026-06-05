package com.decobana.dao;

import com.decobana.db.DatabaseConnection;
import com.decobana.model.Empresa;

import java.sql.*;

public class EmpresaDAO {
    public Empresa findFirst() throws SQLException {
        String sql = "SELECT * FROM empresas LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public void save(Empresa e) throws SQLException {
        if (e.getCodEmp() == 0) {
            String sql = "INSERT INTO empresas (nombre, direccion, telefono, email, director_general, jefe_rrhh, jefe_contabilidad, secretario_sindicato) VALUES (?,?,?,?,?,?,?,?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getNombre());
                ps.setString(2, e.getDireccion());
                ps.setString(3, e.getTelefono());
                ps.setString(4, e.getEmail());
                ps.setString(5, e.getDirectorGeneral());
                ps.setString(6, e.getJefeRRHH());
                ps.setString(7, e.getJefeContabilidad());
                ps.setString(8, e.getSecretarioSindicato());
                ps.executeUpdate();
            }
        } else {
            String sql = "UPDATE empresas SET nombre=?, direccion=?, telefono=?, email=?, director_general=?, jefe_rrhh=?, jefe_contabilidad=?, secretario_sindicato=? WHERE cod_emp=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getNombre());
                ps.setString(2, e.getDireccion());
                ps.setString(3, e.getTelefono());
                ps.setString(4, e.getEmail());
                ps.setString(5, e.getDirectorGeneral());
                ps.setString(6, e.getJefeRRHH());
                ps.setString(7, e.getJefeContabilidad());
                ps.setString(8, e.getSecretarioSindicato());
                ps.setInt(9, e.getCodEmp());
                ps.executeUpdate();
            }
        }
    }

    private Empresa mapRow(ResultSet rs) throws SQLException {
        Empresa e = new Empresa();
        e.setCodEmp(rs.getInt("cod_emp"));
        e.setNombre(rs.getString("nombre"));
        e.setDireccion(rs.getString("direccion"));
        e.setTelefono(rs.getString("telefono"));
        e.setEmail(rs.getString("email"));
        e.setDirectorGeneral(rs.getString("director_general"));
        e.setJefeRRHH(rs.getString("jefe_rrhh"));
        e.setJefeContabilidad(rs.getString("jefe_contabilidad"));
        e.setSecretarioSindicato(rs.getString("secretario_sindicato"));
        return e;
    }
}