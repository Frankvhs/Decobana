package com.decobana.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class MockDataLoader {
    private static final String MOCK_SQL_PATH = "/db/migration/mock_data.sql";

    public static void loadMockData() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = readSqlFromResource(MOCK_SQL_PATH);
            try (Statement stmt = conn.createStatement()) {
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            throw new SQLException("Error al cargar datos mock: " + e.getMessage(), e);
        }
    }

    private static String readSqlFromResource(String resourcePath) throws Exception {
        try (InputStream is = MockDataLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("No se encontró el archivo: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}