package dao;

import model.Product;
import java.io.*;
import java.util.*;

/**
 * Classe DAO (Data Access Object) per gestire i prodotti su file.
 * 
 * Questa classe si occupa di:
 * - Salvare i prodotti in un file di testo
 * - Caricare i prodotti dal file
 * - Cercare prodotti per nome
 * - Cancellare tutti i prodotti
 */
public class ProductDAO {
    // Il percorso del file dove salvare i prodotti
    private static final String FILE_PATH = "data/products.txt";

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
     * Salva un prodotto nel file.
     * Aggiunge il prodotto alla fine del file esistente.
     */
    public void saveProduct(Product product) {
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
     * Carica tutti i prodotti dal file.
     * Restituisce una lista vuota se il file non esiste.
     */
    public List<Product> loadAllProducts() {
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
