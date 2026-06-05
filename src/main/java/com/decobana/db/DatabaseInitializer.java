package com.decobana.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            var rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='empresas'");
            if (rs.next()) {
                rs.close();
                applyInsertEmpresaIfNeeded(conn);
                return;
            }
            rs.close();

            // Execute all SQL migration files in order
            List<String> migrationFiles = getMigrationFiles();
            for (String file : migrationFiles) {
                executeSqlFile(file, conn);
            }
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }

    private static void applyInsertEmpresaIfNeeded(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM empresas");
            if (rs.next() && rs.getInt(1) == 0) {
                executeSqlFile("/db/migration/insert_empresa.sql", conn);
                System.out.println("Registro de empresa insertado.");
            }
            rs.close();
        }
    }

    private static List<String> getMigrationFiles() throws Exception {
        List<String> files = new ArrayList<>();
        // Use classloader to list resources in db/migration
        URL dir = DatabaseInitializer.class.getResource("/db/migration");
        if (dir == null) {
            throw new RuntimeException("No se encontró el directorio db/migration en classpath");
        }

        files.add("/db/migration/initial_db.sql");
        files.add("/db/migration/insert_empresa.sql");

        return files;
    }

    private static void executeSqlFile(String resourcePath, Connection conn) throws Exception {
        try (InputStream is = DatabaseInitializer.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("No se encontró el archivo de migración: " + resourcePath);
            }
            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));
            try (Statement stmt = conn.createStatement()) {
                for (String part : sql.split(";")) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        }
    }
}