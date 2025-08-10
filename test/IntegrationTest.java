
// Test di integrazione - tutti i pattern insieme
package test;

import model.*;
import strategy.*;
import dao.*;

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

        // Setup DAO
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.clearAll();

        // Crea ordine con decoratori
        Order order = new Order(new FreeShippingOver50());

        Product laptop = new Product("Gaming Laptop", 1200.0);
        OrderComponent decoratedLaptop = new InsuranceDecorator(
                new GiftWrapDecorator(laptop));
        order.addItem(decoratedLaptop);

        Product mouse = new Product("Gaming Mouse", 80.0);
        OrderComponent decoratedMouse = new InsuranceDecorator(mouse);
        order.addItem(decoratedMouse);

        // Calcola totale
        double totalPrice = order.getPrice();
        System.out.println("Prezzo totale calcolato: $" + totalPrice);

        // Salva ordine
        orderDAO.saveOrder(order);

        // Verifica salvataggio
        var savedOrders = orderDAO.loadAllOrders();
        if (savedOrders.size() == 1) {
            System.out.println("Ordine salvato con prezzo: $" + savedOrders.get(0).totalPrice);
        }

        System.out.println("Test flusso completo: OK");
    }

    public void testProductPersistence() {
        System.out.println("\nTest: Persistenza prodotti");

        ProductDAO productDAO = new ProductDAO();
        productDAO.clearAll();

        // Salva prodotti
        Product product1 = new Product("Test Product 1", 99.99);
        Product product2 = new Product("Test Product 2", 149.99);

        productDAO.saveProduct(product1);
        productDAO.saveProduct(product2);

        // Ricarica e verifica
        var loadedProducts = productDAO.loadAllProducts();
        System.out.println("Prodotti caricati: " + loadedProducts.size());

        // Verifica ricerca
        Product found = productDAO.findByName("Test Product 1");
        if (found != null) {
            System.out.println("Prodotto trovato: " + found.getDescription() + " - $" + found.getPrice());
        }

        System.out.println("Test persistenza prodotti: OK");
    }

    public void testAllPatternsTogether() {
        System.out.println("\nTest: Tutti i pattern insieme");

        // Pattern Strategy: due strategie diverse
        Order orderStandard = new Order(new StandardShipping());
        Order orderFree = new Order(new FreeShippingOver50());

        // Pattern Decorator: prodotto decorato
        Product phone = new Product("Smartphone", 500.0);
        OrderComponent decoratedPhone = new InsuranceDecorator(
                new GiftWrapDecorator(phone));

        // Pattern Composite: aggiungi agli ordini
        orderStandard.addItem(decoratedPhone);
        orderFree.addItem(decoratedPhone);

        double priceStandard = orderStandard.getPrice();
        double priceFree = orderFree.getPrice();

        System.out.println("Ordine con Standard Shipping: $" + priceStandard);
        System.out.println("Ordine con Free Shipping: $" + priceFree);

        System.out.println("Test tutti i pattern: OK");
    }
}
