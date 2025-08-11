import dao.DatabaseConfig;

/**
 * Classe per inizializzare il database SQLite.
 * Esegui questo prima di usare il database.
 */
public class InitDatabase {
    public static void main(String[] args) {
        System.out.println("Inizializzazione database SQLite...");
        DatabaseConfig.initDatabase();
        System.out.println("Database pronto!");
        System.out.println("File database creato: data/database.db");
    }
}
