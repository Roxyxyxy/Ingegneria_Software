package dao;

import model.*;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * DAO per gestire gli ordini su file o database.
 */
public class OrderDAO {
    private static final String FILE_PATH = "data/orders.txt";
    private boolean useDatabase = true; // Imposta a false per usare file

    public OrderDAO() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Salva un ordine su file o database.
     */
    public void saveOrder(Order order) {
        if (useDatabase) {
            saveOrderToDatabase(order);
        } else {
            saveOrderToFile(order);
        }
    }

    /**
     * Salva ordine nel database SQLite
     */
    private void saveOrderToDatabase(Order order) {
        // Controlla se l'ordine è duplicato nel database
        if (isDuplicateOrderInDatabase(order)) {
            System.out.println("Ordine già esistente nel database, non salvato.");
            return;
        }

        String sql = "INSERT INTO orders (timestamp, total_price, description, strategy, details) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String timestamp = "2025-08-10 " + System.currentTimeMillis() % 86400000 / 1000;
            String strategy = order.getClass().getSimpleName();

            pstmt.setString(1, timestamp);
            pstmt.setDouble(2, order.getPrice()); // Usa getPrice() invece di getTotalPrice()
            pstmt.setString(3, order.getDescription());
            pstmt.setString(4, strategy);
            pstmt.setString(5, order.getDescription()); // Dettagli come descrizione
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore nel salvare l'ordine nel database: " + e.getMessage());
        }
    }

    /**
     * Salva ordine su file (metodo originale)
     */
    private void saveOrderToFile(Order order) {
        // Controlla se l'ordine è duplicato
        if (isDuplicateOrder(order)) {
            System.out.println("Ordine già esistente, non salvato.");
            return;
        }

        try {
            FileWriter writer = new FileWriter(FILE_PATH, true);
            PrintWriter printWriter = new PrintWriter(writer);

            // Usa timestamp semplice
            String timestamp = "2025-08-10 " + System.currentTimeMillis() % 86400000 / 1000;
            String strategy = order.getClass().getSimpleName();

            // Crea la riga da salvare
            String line = timestamp + "|" + order.getPrice() + "|" +
                    order.getDescription() + "|" + strategy + "|";

            // Aggiungi dettagli sui prodotti
            List<OrderComponent> items = getOrderItems(order);
            for (int i = 0; i < items.size(); i++) {
                OrderComponent item = items.get(i);
                line = line + analyzeComponent(item);
                if (i < items.size() - 1) {
                    line = line + ";";
                }
            }

            printWriter.println(line);
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Errore nel salvare l'ordine: " + e.getMessage());
        }
    }

    /**
     * Verifica se un ordine è duplicato confrontando prezzo e descrizione.
     */
    private boolean isDuplicateOrder(Order order) {
        List<OrderSummary> existingOrders = loadAllOrders();

        // Confronta prezzo e descrizione con ciclo semplice
        for (int i = 0; i < existingOrders.size(); i++) {
            OrderSummary existing = existingOrders.get(i);
            if (existing.totalPrice == order.getPrice() &&
                    existing.description.equals(order.getDescription())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Analizza un componente per estrarre le informazioni sui decoratori.
     */
    private String analyzeComponent(OrderComponent component) {
        // Usa nome e prezzo totale del componente
        String name = component.getDescription();
        double price = component.getPrice();

        // Rileva decoratori dal nome della descrizione
        boolean hasGiftWrap = name.contains("Gift Wrap");
        boolean hasInsurance = name.contains("Insurance");

        return name + "," + price + "," + hasGiftWrap + "," + hasInsurance;
    }

    /**
     * Ottiene gli items di un ordine in modo diretto.
     */
    private List<OrderComponent> getOrderItems(Order order) {
        return order.getItems();
    }

    /**
     * Carica tutti gli ordini dal file.
     */
    public List<OrderSummary> loadAllOrders() {
        List<OrderSummary> orders = new ArrayList<OrderSummary>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return orders;
        }

        try {
            FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    OrderSummary summary = parseOrderLine(line);
                    if (summary != null) {
                        orders.add(summary);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel parsare la riga: " + line);
                }
            }
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            System.err.println("Errore nel caricare gli ordini: " + e.getMessage());
        }
        return orders;
    }

    /**
     * Parsa una riga del file ordini.
     */
    private OrderSummary parseOrderLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 4) {
            return new OrderSummary(
                    parts[0], // timestamp
                    Double.parseDouble(parts[1]), // prezzo
                    parts[2], // descrizione
                    parts[3], // strategia
                    parts.length > 4 ? parts[4] : "" // dettagli
            );
        }
        return null;
    }

    /**
     * Pulisce il database degli ordini.
     */
    public void clearAll() {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            PrintWriter printWriter = new PrintWriter(writer);
            // Scrive file vuoto
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Errore nel pulire gli ordini: " + e.getMessage());
        }
    }

    /**
     * Rimuove ordini duplicati dal database mantenendo solo il primo di ogni
     * gruppo.
     */
    public void removeDuplicates() {
        List<OrderSummary> allOrders = loadAllOrders();
        List<String> seen = new ArrayList<String>();
        List<OrderSummary> uniqueOrders = new ArrayList<OrderSummary>();

        for (int i = 0; i < allOrders.size(); i++) {
            OrderSummary order = allOrders.get(i);
            // Chiave basata su prezzo e descrizione
            String key = order.totalPrice + "|" + order.description;

            // Controlla se abbiamo già visto questa chiave
            boolean found = false;
            for (int j = 0; j < seen.size(); j++) {
                if (seen.get(j).equals(key)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                seen.add(key);
                uniqueOrders.add(order);
            }
        }

        // Riscrivi il file con solo gli ordini unici
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            PrintWriter writer = new PrintWriter(fileWriter);
            for (int i = 0; i < uniqueOrders.size(); i++) {
                OrderSummary order = uniqueOrders.get(i);
                writer.println(order.timestamp + "|" + order.totalPrice + "|" +
                        order.description + "|" + order.strategy + "|" + order.details);
            }
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Errore nel rimuovere i duplicati: " + e.getMessage());
        }

        System.out.println("Rimossi " + (allOrders.size() - uniqueOrders.size()) + " ordini duplicati.");
    }

    /**
     * Classe per rappresentare un riassunto dell'ordine.
     */
    public static class OrderSummary {
        public final String timestamp;
        public final double totalPrice;
        public final String description;
        public final String strategy;
        public final String details;

        public OrderSummary(String timestamp, double totalPrice, String description,
                String strategy, String details) {
            this.timestamp = timestamp;
            this.totalPrice = totalPrice;
            this.description = description;
            this.strategy = strategy;
            this.details = details;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s - $%.2f (%s)",
                    timestamp, description, totalPrice, strategy);
        }
    }

    /**
     * Controlla se un ordine è duplicato nel database
     */
    private boolean isDuplicateOrderInDatabase(Order order) {
        String sql = "SELECT COUNT(*) FROM orders WHERE description = ? AND total_price = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, order.getDescription());
            pstmt.setDouble(2, order.getPrice()); // Usa getPrice() invece di getTotalPrice()

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Errore nel controllo duplicati database: " + e.getMessage());
        }

        return false;
    }
}
