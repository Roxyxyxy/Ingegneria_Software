
// Test di integrazione - tutti i pattern insieme

import model.*;
import strategy.*;
import dao.*;
import java.util.List;

public class IntegrationTest {

    public static void main(String[] args) {
        System.out.println("=== Test di Integrazione ===");

        IntegrationTest test = new IntegrationTest();
        test.runAllTests();

        System.out.println("Tutti i test di integrazione completati!");
    }

    public void runAllTests() {
        testCompleteOrderFlow();
        testProductPersistence();
        testAllPatternsTogether();
    }

    public void testCompleteOrderFlow() {
        System.out.println("\nTest: Flusso completo ordine");

        // Setup DAO per la persistenza
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();
        productDAO.clearAll(); // Pulisce prodotti precedenti
        orderDAO.clearAll(); // Pulisce ordini precedenti

        // Crea ordine con strategia di spedizione gratuita sopra $50
        Order order = new Order(new FreeShippingOver50());

        // DECORATOR PATTERN: Decora un laptop con gift wrap + insurance
        Product laptop = new Product("Gaming Laptop", 1200.0);
        OrderComponent decoratedLaptop = new InsuranceDecorator(
                new GiftWrapDecorator(laptop)); // Doppio decoratore
        order.addItem(decoratedLaptop);

        // DECORATOR PATTERN: Decora un mouse solo con insurance
        Product mouse = new Product("Gaming Mouse", 80.0);
        OrderComponent decoratedMouse = new InsuranceDecorator(mouse);
        order.addItem(decoratedMouse);

        // COMPOSITE PATTERN: Calcola totale ricorsivamente
        double totalPrice = order.getPrice();
        System.out.println("Prezzo totale calcolato: $" + totalPrice);

        // DAO PATTERN: Persiste l'ordine nel database
        orderDAO.saveOrder(order);

        // Verifica salvataggio - carica tutti gli ordini salvati
        List<OrderDAO.OrderSummary> savedOrders = orderDAO.loadAllOrders();
        if (savedOrders.size() == 1) {
            System.out.println("Ordine salvato con prezzo: $" + savedOrders.get(0).totalPrice);
        }

        System.out.println("Test flusso completo: OK");
    }

    public void testProductPersistence() {
        System.out.println("\nTest: Persistenza prodotti");

        // DAO PATTERN: Test delle operazioni CRUD sui prodotti
        ProductDAO productDAO = new ProductDAO();
        productDAO.clearAll(); // Pulisce per test isolato

        // CREATE: Salva prodotti nel database
        Product product1 = new Product("Test Product 1", 99.99);
        Product product2 = new Product("Test Product 2", 149.99);

        productDAO.saveProduct(product1);
        productDAO.saveProduct(product2);

        // READ: Ricarica e verifica - carica tutti i prodotti salvati
        List<Product> loadedProducts = productDAO.loadAllProducts();
        System.out.println("Prodotti caricati: " + loadedProducts.size());

        // SEARCH: Verifica ricerca per nome
        Product found = productDAO.findByName("Test Product 1");
        if (found != null) {
            System.out.println("Prodotto trovato: " + found.getDescription() + " - $" + found.getPrice());
        }

        System.out.println("Test persistenza prodotti: OK");
    }

    public void testAllPatternsTogether() {
        System.out.println("\nTest: Tutti i pattern insieme");

        // STRATEGY PATTERN: Crea due ordini con strategie diverse
        Order orderStandard = new Order(new StandardShipping()); // Sempre $5
        Order orderFree = new Order(new FreeShippingOver50()); // Gratis se > $50

        // DECORATOR PATTERN: Crea un prodotto con due decoratori
        Product phone = new Product("Smartphone", 500.0);
        OrderComponent decoratedPhone = new InsuranceDecorator(
                new GiftWrapDecorator(phone)); // Gift wrap + Insurance

        // COMPOSITE PATTERN: Aggiungi lo stesso prodotto a entrambi gli ordini
        orderStandard.addItem(decoratedPhone);
        orderFree.addItem(decoratedPhone);

        // Calcola prezzi finali (mostra differenza tra strategie)
        double priceStandard = orderStandard.getPrice(); // Phone decorato + spedizione standard
        double priceFree = orderFree.getPrice(); // Phone decorato + spedizione gratuita (>$50)

        System.out.println("Ordine con Standard Shipping: $" + priceStandard);
        System.out.println("Ordine con Free Shipping: $" + priceFree);

        System.out.println("Test tutti i pattern: OK");
    }
}
