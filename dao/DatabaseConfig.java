package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configurazione database SQLite semplificata.
 * Crea un file database.db nella cartella data/
 */
public class DatabaseConfig {
    private static final String DB_PATH = "data/database.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    /**
     * Ottiene connessione al database SQLite
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Inizializza il database creando le tabelle se non esistono
     */
    public static void initDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Crea tabella prodotti
            String createProductsTable = """
                    CREATE TABLE IF NOT EXISTS products (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        price REAL NOT NULL
                    )
                    """;
            stmt.execute(createProductsTable);

            // Crea tabella ordini
            String createOrdersTable = """
                    CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        timestamp TEXT NOT NULL,
                        total_price REAL NOT NULL,
                        description TEXT NOT NULL,
                        strategy TEXT NOT NULL,
                        details TEXT NOT NULL
                    )
                    """;
            stmt.execute(createOrdersTable);

            System.out.println("Database inizializzato correttamente.");

        } catch (SQLException e) {
            System.err.println("Errore inizializzazione database: " + e.getMessage());
        }
    }
}
