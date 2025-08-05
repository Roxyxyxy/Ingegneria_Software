package dao;

import model.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DAO per gestire gli ordini su file.
 */
public class OrderDAO {
    private static final String FILE_PATH = "data/orders.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
     * Salva un ordine nel database.
     */
    public void saveOrder(Order order) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String strategy = order.getClass().getSimpleName();

            // Formato: timestamp|prezzo|descrizione|strategia|dettagli_prodotti
            StringBuilder line = new StringBuilder();
            line.append(timestamp).append("|")
                    .append(order.getPrice()).append("|")
                    .append(order.getDescription()).append("|")
                    .append(strategy).append("|");

            // Aggiungi dettagli sui prodotti
            List<OrderComponent> items = getOrderItems(order);
            for (int i = 0; i < items.size(); i++) {
                OrderComponent item = items.get(i);
                line.append(analyzeComponent(item));
                if (i < items.size() - 1) {
                    line.append(";");
                }
            }

            writer.println(line.toString());
        } catch (IOException e) {
            System.err.println("Errore nel salvare l'ordine: " + e.getMessage());
        }
    }

    /**
     * Analizza un componente per estrarre informazioni sui decoratori.
     */
    private String analyzeComponent(OrderComponent component) {
        StringBuilder info = new StringBuilder();

        String baseName = extractBaseName(component);
        double basePrice = extractBasePrice(component);
        boolean hasGiftWrap = hasDecorator(component, GiftWrapDecorator.class);
        boolean hasInsurance = hasDecorator(component, InsuranceDecorator.class);

        info.append(baseName).append(",")
                .append(basePrice).append(",")
                .append(hasGiftWrap).append(",")
                .append(hasInsurance);

        return info.toString();
    }

    /**
     * Estrae il nome del prodotto base da una catena di decoratori.
     */
    private String extractBaseName(OrderComponent component) {
        if (component instanceof Product) {
            return component.getDescription();
        } else if (component instanceof ProductDecorator) {
            return extractBaseName(getDecoratedComponent((ProductDecorator) component));
        }
        return "Unknown";
    }

    /**
     * Estrae il prezzo base del prodotto.
     */
    private double extractBasePrice(OrderComponent component) {
        if (component instanceof Product) {
            return component.getPrice();
        } else if (component instanceof GiftWrapDecorator) {
            return extractBasePrice(getDecoratedComponent((ProductDecorator) component));
        } else if (component instanceof InsuranceDecorator) {
            return extractBasePrice(getDecoratedComponent((ProductDecorator) component));
        }
        return 0.0;
    }

    /**
     * Verifica se un componente ha un decoratore specifico.
     */
    private boolean hasDecorator(OrderComponent component, Class<?> decoratorClass) {
        if (decoratorClass.isInstance(component)) {
            return true;
        } else if (component instanceof ProductDecorator) {
            return hasDecorator(getDecoratedComponent((ProductDecorator) component), decoratorClass);
        }
        return false;
    }

    /**
     * Ottiene il componente decorato usando reflection.
     */
    private OrderComponent getDecoratedComponent(ProductDecorator decorator) {
        try {
            java.lang.reflect.Field field = ProductDecorator.class.getDeclaredField("component");
            field.setAccessible(true);
            return (OrderComponent) field.get(decorator);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Ottiene gli items di un ordine usando reflection.
     */
    @SuppressWarnings("unchecked")
    private List<OrderComponent> getOrderItems(Order order) {
        try {
            java.lang.reflect.Field field = Order.class.getDeclaredField("items");
            field.setAccessible(true);
            return (List<OrderComponent>) field.get(order);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Carica tutti gli ordini dal database.
     */
    public List<OrderSummary> loadAllOrders() {
        List<OrderSummary> orders = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Scrive file vuoto
        } catch (IOException e) {
            System.err.println("Errore nel pulire gli ordini: " + e.getMessage());
        }
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
            return String.format("[%s] %s - €%.2f (%s)",
                    timestamp, description, totalPrice, strategy);
        }
    }
}
