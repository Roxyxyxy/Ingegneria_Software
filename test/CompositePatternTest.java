// Test del pattern Composite - Order e Product

import model.*;
import strategy.*;

public class CompositePatternTest {

    public static void main(String[] args) {
        System.out.println("=== Test Composite Pattern ===");

        CompositePatternTest test = new CompositePatternTest();

        // Esegui tutti i test
        test.testProductAsLeaf();
        test.testOrderAsComposite();
        test.testEmptyOrder();
        test.testMultipleItems();

        System.out.println("Tutti i test Composite Pattern completati!");
    }

    public void testProductAsLeaf() {
        System.out.println("\nTest: Product come leaf");

        Product desktop = new Product("Desktop PC", 800.0);

        String description = desktop.getDescription();
        double price = desktop.getPrice();

        System.out.println("Descrizione: " + description);
        System.out.println("Prezzo: " + price);

        if (!description.equals("Desktop PC")) {
            System.out.println("ERRORE: Descrizione non corretta");
        }
        if (price != 800.0) {
            System.out.println("ERRORE: Prezzo non corretto");
        }

        System.out.println("Test Product leaf: OK");
    }

    public void testOrderAsComposite() {
        System.out.println("\nTest: Order come composite");

        Order order = new Order(new StandardShipping());
        Product keyboard = new Product("Tastiera Meccanica", 120.0);
        Product mouse = new Product("Mouse Gaming", 75.0);

        order.addItem(keyboard);
        order.addItem(mouse);

        String description = order.getDescription();
        double totalPrice = order.getPrice();

        System.out.println("Descrizione ordine: " + description);
        System.out.println("Prezzo totale: " + totalPrice);

        if (!description.equals("Order with 2 items")) {
            System.out.println("ERRORE: Descrizione ordine non corretta");
        }
        if (totalPrice != 200.0) {
            System.out.println("ERRORE: Prezzo totale non corretto");
        }

        if (order.getItems().size() != 2) {
            System.out.println("ERRORE: Numero items non corretto");
        }

        System.out.println("Test Order composite: OK");
    }

    public void testEmptyOrder() {
        System.out.println("\nTest: Ordine vuoto");

        Order emptyOrder = new Order(new StandardShipping());

        String description = emptyOrder.getDescription();
        double price = emptyOrder.getPrice();

        System.out.println("Descrizione ordine vuoto: " + description);
        System.out.println("Prezzo ordine vuoto: " + price);

        if (!description.equals("Order with 0 items")) {
            System.out.println("ERRORE: Descrizione ordine vuoto non corretta");
        }
        if (price != 5.0) {
            System.out.println("ERRORE: Prezzo ordine vuoto non corretto");
        }

        System.out.println("Test ordine vuoto: OK");
    }

    public void testMultipleItems() {
        System.out.println("\nTest: Ordine con tanti elementi");

        Order bigOrder = new Order(new FreeShippingOver50());

        bigOrder.addItem(new Product("Monitor", 250.0));
        bigOrder.addItem(new Product("Webcam", 85.0));
        bigOrder.addItem(new Product("Speakers", 120.0));
        bigOrder.addItem(new Product("Laptop", 800.0));

        double totalPrice = bigOrder.getPrice();
        int itemCount = bigOrder.getItems().size();

        System.out.println("Numero elementi: " + itemCount);
        System.out.println("Prezzo totale: " + totalPrice);

        if (itemCount != 4) {
            System.out.println("ERRORE: Conteggio elementi non corretto");
        }
        if (totalPrice != 1255.0) {
            System.out.println("ERRORE: Calcolo prezzo non corretto");
        }

        System.out.println("Test ordine multiplo: OK");
    }
}
