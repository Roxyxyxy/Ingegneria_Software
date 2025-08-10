import model.*;
import strategy.*;
import dao.*;
import java.util.List;

/**
 * Demo dei design pattern con database.
 */
public class Main {
    public static void main(String[] args) {
        // DAO PATTERN: Crea oggetti per accesso ai dati
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        System.out.println("=== DEMO DESIGN PATTERNS CON DATABASE ===\n");

        // CREATE: Crea prodotti e salvali nel database
        Product product1 = new Product("Product1", 20);
        Product product2 = new Product("Product2", 15);

        productDAO.saveProduct(product1);
        productDAO.saveProduct(product2);
        System.out.println("Prodotti salvati nel database.");

        // READ: Carica prodotti dal database e mostrarli con ciclo tradizionale
        System.out.println("\nProdotti nel database:");
        for (Product p : productDAO.loadAllProducts()) {
            System.out.println("- " + p.getDescription() + ": $" + p.getPrice());
        }

        // DECORATOR PATTERN: Applica decoratori al prodotto
        OrderComponent decoratedProduct2 = new InsuranceDecorator(
                new GiftWrapDecorator(product2)); // Gift Wrap + Insurance

        // COMPOSITE + STRATEGY: Crea ordine con strategia di spedizione
        Order order = new Order(new FreeShippingOver50());
        order.addItem(product1); // Prodotto normale
        order.addItem(decoratedProduct2); // Prodotto decorato

        System.out.println("\n=== ORDINE CREATO ===");
        System.out.println("Total: " + order.getPrice());
        System.out.println("Description: " + order.getDescription());

        // PERSISTENCE: Salva l'ordine nel database
        orderDAO.saveOrder(order);
        System.out.println("\nOrdine salvato nel database.");

        // READ: Mostra cronologia ordini
        System.out.println("\n=== CRONOLOGIA ORDINI ===");
        for (OrderDAO.OrderSummary summary : orderDAO.loadAllOrders()) {
            System.out.println(summary);
        }

        System.out.println("\n=== DEMO COMPLETATA ===");
    }
}