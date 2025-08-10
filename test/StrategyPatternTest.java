// Test del pattern Strategy - StandardShipping e FreeShippingOver50
package test;

import strategy.*;
import model.*;

public class StrategyPatternTest {

    public static void main(String[] args) {
        System.out.println("=== Test Strategy Pattern ===");

        StrategyPatternTest test = new StrategyPatternTest();

        // Esegui tutti i test
        test.testStandardShipping();
        test.testFreeShippingBelow50();
        test.testFreeShippingAbove50();
        test.testStrategyChange();

        System.out.println("Tutti i test Strategy Pattern completati!");
    }

    public void testStandardShipping() {
        System.out.println("\nTest: Standard Shipping Strategy");

        // Test strategia standard: sempre $5
        ShippingCostStrategy strategy = new StandardShipping();

        double cost1 = strategy.calculateShipping(10.0);
        double cost2 = strategy.calculateShipping(100.0);
        double cost3 = strategy.calculateShipping(0.0);

        System.out.println("Standard shipping per $10: " + cost1);
        System.out.println("Standard shipping per $100: " + cost2);
        System.out.println("Standard shipping per $0: " + cost3);

        // Controlli 
        if (cost1 != 5.0) {
            System.out.println("ERRORE: Standard shipping non corretto per $10");
        }
        if (cost2 != 5.0) {
            System.out.println("ERRORE: Standard shipping non corretto per $100");
        }
        if (cost3 != 5.0) {
            System.out.println("ERRORE: Standard shipping non corretto per $0");
        }

        System.out.println("Test Standard Shipping: OK");
    }

    public void testFreeShippingBelow50() {
        System.out.println("\nTest: Free Shipping - Sotto $50");

        // Test strategia free shipping sotto i $50
        ShippingCostStrategy strategy = new FreeShippingOver50();

        double cost1 = strategy.calculateShipping(30.0);
        double cost2 = strategy.calculateShipping(49.99);
        double cost3 = strategy.calculateShipping(0.0);

        System.out.println("Free shipping per $30: " + cost1);
        System.out.println("Free shipping per $49.99: " + cost2);
        System.out.println("Free shipping per $0: " + cost3);

        // Controlli - sotto $50 deve costare $5
        if (cost1 != 5.0) {
            System.out.println("ERRORE: Free shipping sotto soglia non corretto per $30");
        }
        if (cost2 != 5.0) {
            System.out.println("ERRORE: Free shipping sotto soglia non corretto per $49.99");
        }
        if (cost3 != 5.0) {
            System.out.println("ERRORE: Free shipping sotto soglia non corretto per $0");
        }

        System.out.println("Test Free Shipping sotto soglia: OK");
    }

    public void testFreeShippingAbove50() {
        System.out.println("\nTest: Free Shipping - Sopra $50");

        // Test strategia free shipping sopra i $50
        ShippingCostStrategy strategy = new FreeShippingOver50();

        double cost1 = strategy.calculateShipping(50.0);
        double cost2 = strategy.calculateShipping(75.5);
        double cost3 = strategy.calculateShipping(1000.0);

        System.out.println("Free shipping per $50: " + cost1);
        System.out.println("Free shipping per $75.5: " + cost2);
        System.out.println("Free shipping per $1000: " + cost3);

        // Controlli - sopra $50 deve essere gratis
        if (cost1 != 0.0) {
            System.out.println("ERRORE: Free shipping sopra soglia non corretto per $50");
        }
        if (cost2 != 0.0) {
            System.out.println("ERRORE: Free shipping sopra soglia non corretto per $75.5");
        }
        if (cost3 != 0.0) {
            System.out.println("ERRORE: Free shipping sopra soglia non corretto per $1000");
        }

        System.out.println("Test Free Shipping sopra soglia: OK");
    }

    public void testStrategyChange() {
        System.out.println("\nTest: Cambio strategia durante runtime");

        // Test: cambio strategia su stesso ordine
        Order order = new Order(new StandardShipping());
        Product item = new Product("Test Item", 75.0);
        order.addItem(item);

        double priceStandard = order.getPrice();
        System.out.println("Prezzo con Standard Shipping: " + priceStandard);

        // Cambia strategia
        order.setShippingStrategy(new FreeShippingOver50());
        double priceFree = order.getPrice();
        System.out.println("Prezzo con Free Shipping: " + priceFree);

        // Controlli semplici
        if (priceStandard != 80.0) { // 75 + 5
            System.out.println("ERRORE: Prezzo con standard shipping non corretto");
        }
        if (priceFree != 75.0) { // 75 + 0 (free shipping)
            System.out.println("ERRORE: Prezzo con free shipping non corretto");
        }

        System.out.println("Test cambio strategia: OK");
    }
}
