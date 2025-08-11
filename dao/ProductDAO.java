package dao;

import model.Product;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Classe DAO (Data Access Object) per gestire i prodotti su file o database.
 * 
 * Questa classe si occupa di:
 * - Salvare i prodotti in un file di testo o database SQLite
 * - Caricare i prodotti dal file o database
 * - Cercare prodotti per nome
 * - Cancellare tutti i prodotti
 */
public class ProductDAO {
    // Il percorso del file dove salvare i prodotti
    private static final String FILE_PATH = "data/products.txt";
    private boolean useDatabase = true; // Imposta a false per usare file

    /**
     * Costruttore: quando creiamo un ProductDAO,
     * controlliamo che esista la cartella "data"
     */
    public ProductDAO() {
        createDataDirectory();
    }

    /**
     * Metodo privato che crea la cartella "data" se non esiste
     */
    private void createDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Salva un prodotto nel file o database.
     * Aggiunge il prodotto alla fine del file esistente o nel database.
     */
    public void saveProduct(Product product) {
        if (useDatabase) {
            saveProductToDatabase(product);
        } else {
            saveProductToFile(product);
        }
    }

    /**
     * Salva prodotto nel database SQLite
     */
    private void saveProductToDatabase(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getDescription());
            pstmt.setDouble(2, product.getPrice());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore nel salvare il prodotto nel database: " + e.getMessage());
        }
    }

    /**
     * Salva prodotto nel file (metodo originale)
     */
    private void saveProductToFile(Product product) {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH, true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println(product.getDescription() + "," + product.getPrice());
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Errore nel salvare il prodotto: " + e.getMessage());
        }
    }

    /**
     * Carica tutti i prodotti dal file o database.
     * Restituisce una lista vuota se il file non esiste.
     */
    public List<Product> loadAllProducts() {
        if (useDatabase) {
            return loadProductsFromDatabase();
        } else {
            return loadProductsFromFile();
        }
    }

    /**
     * Carica prodotti dal database SQLite
     */
    private List<Product> loadProductsFromDatabase() {
        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT name, price FROM products";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                products.add(new Product(name, price));
            }

        } catch (SQLException e) {
            System.err.println("Errore nel caricare i prodotti dal database: " + e.getMessage());
        }

        return products;
    }

    /**
     * Carica prodotti dal file (metodo originale)
     */
    private List<Product> loadProductsFromFile() {
        List<Product> products = new ArrayList<Product>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return products;
        }

        try {
            FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        String nome = parts[0];
                        double prezzo = Double.parseDouble(parts[1]);
                        products.add(new Product(nome, prezzo));
                    } catch (NumberFormatException e) {
                        System.err.println("Prezzo non valido nella riga: " + line);
                    }
                }
            }
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            System.err.println("Errore nel leggere il file: " + e.getMessage());
        }
        return products;
    }

    /**
     * Cerca un prodotto per nome usando un ciclo semplice.
     */
    public Product findByName(String name) {
        List<Product> products = loadAllProducts();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getDescription().equals(name)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Pulisce il database dei prodotti.
     */
    public void clearAll() {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            PrintWriter writer = new PrintWriter(fileWriter);
            // Scrive file vuoto
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Errore nel pulire i prodotti: " + e.getMessage());
        }
    }
}
