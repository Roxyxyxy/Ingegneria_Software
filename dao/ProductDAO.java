package dao;

import model.Product;
import java.io.*;
import java.util.*;

/**
 * DAO per gestire i prodotti su file.
 */
public class ProductDAO {
    private static final String FILE_PATH = "data/products.txt";

    public ProductDAO() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Salva un prodotto nel database.
     */
    public void saveProduct(Product product) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(product.getDescription() + "," + product.getPrice());
        } catch (IOException e) {
            System.err.println("Errore nel salvare il prodotto: " + e.getMessage());
        }
    }

    /**
     * Carica tutti i prodotti dal database.
     */
    public List<Product> loadAllProducts() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        products.add(new Product(parts[0], Double.parseDouble(parts[1])));
                    } catch (NumberFormatException e) {
                        System.err.println("Formato prezzo non valido: " + parts[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nel caricare i prodotti: " + e.getMessage());
        }
        return products;
    }

    /**
     * Cerca un prodotto per nome.
     */
    public Product findByName(String name) {
        List<Product> products = loadAllProducts();
        return products.stream()
                .filter(p -> p.getDescription().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Pulisce il database dei prodotti.
     */
    public void clearAll() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Scrive file vuoto
        } catch (IOException e) {
            System.err.println("Errore nel pulire i prodotti: " + e.getMessage());
        }
    }
}
