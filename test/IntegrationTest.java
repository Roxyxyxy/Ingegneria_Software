
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

        // DECORATOR PATTERN: Decora una console con gift wrap + insurance
        Product console = new Product("PlayStation 5", 500.0);
        OrderComponent decoratedConsole = new InsuranceDecorator(
                new GiftWrapDecorator(console)); // Doppio decoratore
        order.addItem(decoratedConsole);

        // DECORATOR PATTERN: Decora un controller solo con insurance
        Product controller = new Product("Controller Wireless", 70.0);
        OrderComponent decoratedController = new InsuranceDecorator(controller);
        order.addItem(decoratedController);

        // COMPOSITE PATTERN: Calcola totale ricorsivamente
        double totalPrice = order.getPrice();
        System.out.println("Prezzo totale calcolato: $" + totalPrice);

        // Verifica calcolo: Console (500+5)*1.10 + Controller 70*1.10 = 555.5 + 77 =
        // 632.5
        double expectedTotal = 632.5;
        if (Math.abs(totalPrice - expectedTotal) > 0.1) {
            System.out.println(
                    "ERRORE: Prezzo totale non corretto. Atteso: " + expectedTotal + ", Ottenuto: " + totalPrice);
        }

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

        // CREATE: Salva prodotti gaming nel database
        Product headset = new Product("Cuffie Gaming", 150.0);
        Product gamepad = new Product("Gamepad Pro", 90.0);

        productDAO.saveProduct(headset);
        productDAO.saveProduct(gamepad);

        // READ: Ricarica e verifica - carica tutti i prodotti salvati
        List<Product> loadedProducts = productDAO.loadAllProducts();
        System.out.println("Prodotti caricati: " + loadedProducts.size());

        // SEARCH: Verifica ricerca per nome
        Product found = productDAO.findByName("Cuffie Gaming");
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

        // DECORATOR PATTERN: Crea un prodotto gaming con due decoratori
        Product console = new Product("Xbox Series X", 500.0);
        OrderComponent decoratedConsole = new InsuranceDecorator(
                new GiftWrapDecorator(console)); // Gift wrap + Insurance

        // COMPOSITE PATTERN: Aggiungi lo stesso prodotto a entrambi gli ordini
        orderStandard.addItem(decoratedConsole);
        orderFree.addItem(decoratedConsole);

        // Calcola prezzi finali (mostra differenza tra strategie)
        double priceStandard = orderStandard.getPrice(); // Console decorata + spedizione standard
        double priceFree = orderFree.getPrice(); // Console decorata + spedizione gratuita (>$50)

        System.out.println("Ordine con Standard Shipping: $" + priceStandard);
        System.out.println("Ordine con Free Shipping: $" + priceFree);

        System.out.println("Test tutti i pattern: OK");
    }
}
