import model.*;
import strategy.*;
import dao.*;

/**
 * Demo completa dei design pattern con database.
 * Mostra tutti e 3 i pattern con multiple entità.
 */
public class Main {
    public static void main(String[] args) {
        // DAO PATTERN: Crea oggetti per accesso ai dati
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        System.out.println("=== DEMO COMPLETA DESIGN PATTERNS CON DATABASE ===\n");

        // PULIZIA: Inizia con database pulito per demo chiara
        productDAO.clearAll();
        orderDAO.clearAll();
        System.out.println("Database pulito per demo fresca.\n");

        // CREATE: Crea una gamma di componenti PC
        Product gpu = new Product("Scheda Grafica RTX", 1200.0);
        Product cpu = new Product("Processore i7", 400.0);
        Product ssd = new Product("SSD 1TB", 150.0);
        Product ram = new Product("RAM 32GB", 300.0);
        Product motherboard = new Product("Scheda Madre", 180.0);

        System.out.println("=== SALVANDO PRODOTTI NEL DATABASE ===");
        productDAO.saveProduct(gpu);
        productDAO.saveProduct(cpu);
        productDAO.saveProduct(ssd);
        productDAO.saveProduct(ram);
        productDAO.saveProduct(motherboard);

        // READ: Mostra tutti i prodotti nel database
        System.out.println("\nProdotti disponibili nel database:");
        for (Product p : productDAO.loadAllProducts()) {
            System.out.println("- " + p.getDescription() + ": $" + p.getPrice());
        }

        System.out.println("\n=== CREANDO ORDINI CON DIVERSI PATTERN ===");

        // ORDINE 1: Build PC base con spedizione standard
        System.out.println("\n--- ORDINE 1: Build PC Base ---");
        Order order1 = new Order(new StandardShipping());
        order1.addItem(gpu);
        order1.addItem(cpu);

        System.out.println("Ordine 1 - " + order1.getDescription());
        System.out.println("Totale: $" + order1.getPrice() + " (include spedizione standard di $5)");

        // ORDINE 2: Setup completo con decoratori e spedizione gratuita
        System.out.println("\n--- ORDINE 2: Setup Completo Decorato ---");
        Order order2 = new Order(new FreeShippingOver50());

        // DECORATOR PATTERN: Applica decoratori multipli
        OrderComponent decoratedRAM = new InsuranceDecorator(
                new GiftWrapDecorator(ram)); // RAM con confezione regalo + assicurazione
        OrderComponent decoratedMotherboard = new GiftWrapDecorator(motherboard); // Solo confezione regalo

        order2.addItem(ssd);
        order2.addItem(decoratedRAM);
        order2.addItem(decoratedMotherboard);

        System.out.println("Ordine 2 - " + order2.getDescription());
        System.out.println("Totale: $" + order2.getPrice() + " (spedizione gratuita > $50)");

        // ORDINE 3: Acquisto singolo che paga spedizione
        System.out.println("\n--- ORDINE 3: Componente Singolo ---");
        Order order3 = new Order(new FreeShippingOver50());
        OrderComponent decoratedCPU = new InsuranceDecorator(cpu); // CPU con assicurazione
        order3.addItem(decoratedCPU);

        System.out.println("Ordine 3 - " + order3.getDescription());
        System.out.println("Totale: $" + order3.getPrice() + " (sotto $50, spedizione $5)");

        // PERSISTENCE: Salva tutti gli ordini nel database
        System.out.println("\n=== SALVANDO ORDINI NEL DATABASE ===");
        orderDAO.saveOrder(order1);
        orderDAO.saveOrder(order2);
        orderDAO.saveOrder(order3);
        System.out.println("Tutti gli ordini salvati nel database.");

        // READ: Mostra cronologia completa ordini
        System.out.println("\n=== CRONOLOGIA COMPLETA ORDINI ===");
        for (OrderDAO.OrderSummary summary : orderDAO.loadAllOrders()) {
            System.out.println(summary);
        }

        // STATISTICHE: Mostra statistiche database
        System.out.println("\n=== STATISTICHE DATABASE ===");
        System.out.println("Prodotti totali: " + productDAO.loadAllProducts().size());
        System.out.println("Ordini totali: " + orderDAO.loadAllOrders().size());

        System.out.println("\n=== DEMO COMPLETATA ===");
        System.out.println("Tutti e 3 i design pattern dimostrati con successo!");
        System.out.println("- COMPOSITE: Ordini contengono componenti");
        System.out.println("- DECORATOR: Prodotti decorati con servizi extra");
        System.out.println("- STRATEGY: Diverse strategie di spedizione");
        System.out.println("- DAO: Persistenza su database SQLite");
    }
}