import model.*;
import strategy.*;

/**
 * Test dei design pattern: Composite, Decorator, Strategy.
 */
public class Main {
    public static void main(String[] args) {
        // Prodotto semplice
        OrderComponent product1 = new Product("T-Shirt", 20);

        // Prodotto decorato
        OrderComponent product2 = new InsuranceDecorator(
                new GiftWrapDecorator(
                        new Product("Mug", 15)));

        // Ordine con strategia di spedizione
        Order order = new Order(new FreeShippingOver50());
        order.addItem(product1);
        order.addItem(product2);

        System.out.println("Total: " + order.getPrice());
        System.out.println("Description: " + order.getDescription());
    }
}