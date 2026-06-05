package com.decobana.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Check if tables already exist
            var rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='empresas'");
            if (rs.next()) {
                rs.close();
                return; // Already initialized
            }
            rs.close();

            // Read SQL script from resources
            var is = DatabaseInitializer.class.getResourceAsStream("/db/migration/initial_db.sql");
            if (is == null) {
                throw new RuntimeException("No se encontró el script de migración en classpath");
            }
            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Execute each statement
            for (String part : sql.split(";")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }
}