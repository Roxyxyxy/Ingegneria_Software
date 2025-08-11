import model.*;
import strategy.*;
import dao.*;

/**
 * Classe di test per dimostrare l'uso dei 3 design pattern:
 * - Composite: Order contiene OrderComponent
 * - Decorator: GiftWrap e Insurance decorano i prodotti
 * - Strategy: Diverse strategie di spedizione
 */
public class TestDesignPatterns {

    public static void main(String[] args) {
        System.out.println("=== TEST DESIGN PATTERNS ===\n");

        // Test 1: COMPOSITE Pattern
        testCompositePattern();

        // Test 2: DECORATOR Pattern
        testDecoratorPattern();

        // Test 3: STRATEGY Pattern
        testStrategyPattern();

        // Test 4: Combinazione di tutti i pattern
        testAllPatternsTogether();
    }

    /**
     * Test del pattern COMPOSITE
     * Order può contenere piu OrderComponent (prodotti o altri ordini)
     */
    private static void testCompositePattern() {
        System.out.println("--- TEST COMPOSITE PATTERN ---");

        // Creo prodotti gaming
        Product laptop = new Product("Gaming Laptop", 1200.0);
        Product mouse = new Product("Gaming Mouse", 80.0);

        // Creo un ordine con strategia standard
        Order order = new Order(new StandardShipping());
        order.addItem(laptop);
        order.addItem(mouse);

        System.out.println("Ordine: " + order.getDescription());
        System.out.println("Prezzo totale: $" + order.getPrice());
        System.out.println("(Prodotti: $" + (laptop.getPrice() + mouse.getPrice()) + " + Spedizione: $5.0)\n");
    }

    /**
     * Test del pattern DECORATOR
     * Aggiunge funzionalità ai prodotti senza modificare la classe base
     */
    private static void testDecoratorPattern() {
        System.out.println("--- TEST DECORATOR PATTERN ---");

        // Prodotto gaming base
        Product console = new Product("Gaming Console", 500.0);
        System.out.println("Prodotto base: " + console.getDescription() + " - $" + console.getPrice());

        // Aggiungo gift wrap
        OrderComponent consoleWithGiftWrap = new GiftWrapDecorator(console);
        System.out.println(
                "Con confezione regalo: " + consoleWithGiftWrap.getDescription() + " - $"
                        + consoleWithGiftWrap.getPrice());

        // Aggiungo anche assicurazione
        OrderComponent consoleComplete = new InsuranceDecorator(consoleWithGiftWrap);
        System.out.println("Con confezione + assicurazione: " + consoleComplete.getDescription() + " - $"
                + consoleComplete.getPrice());
        System.out.println();
    }

    /**
     * Test del pattern STRATEGY
     * Diverse strategie di calcolo spedizione
     */
    private static void testStrategyPattern() {
        System.out.println("--- TEST STRATEGY PATTERN ---");

        Product headset = new Product("Gaming Headset", 120.0);

        // Strategia 1: Spedizione standard (sempre $5)
        Order orderStandard = new Order(new StandardShipping());
        orderStandard.addItem(headset);
        System.out.println("Spedizione standard: $" + orderStandard.getPrice() + " (prodotto $120 + spedizione $5)");

        // Strategia 2: Spedizione gratuita sopra $50
        Order orderFree = new Order(new FreeShippingOver50());
        orderFree.addItem(headset);
        System.out.println("Spedizione gratis >$50: $" + orderFree.getPrice() + " (sopra soglia, spedizione gratuita)");

        // Aggiungo più prodotti per mostrare la differenza
        orderFree.addItem(new Product("Gaming Controller", 60.0));
        System.out.println("Con più prodotti: $" + orderFree.getPrice() + " (sopra $50, spedizione gratuita)");
        System.out.println();
    }

    /**
     * Test che combina tutti e 3 i pattern insieme
     */
    private static void testAllPatternsTogether() {
        System.out.println("--- TEST TUTTI I PATTERN INSIEME ---");

        // DECORATOR: Creo prodotti con decoratori
        Product laptop = new Product("Gaming Laptop", 1200.0);
        OrderComponent laptopWithGift = new GiftWrapDecorator(laptop);
        OrderComponent laptopComplete = new InsuranceDecorator(laptopWithGift);

        Product mouse = new Product("Gaming Mouse", 80.0);
        OrderComponent mouseWithInsurance = new InsuranceDecorator(mouse);

        // COMPOSITE + STRATEGY: Creo ordine con strategia free shipping
        Order bigOrder = new Order(new FreeShippingOver50());
        bigOrder.addItem(laptopComplete); // Laptop decorato
        bigOrder.addItem(mouseWithInsurance); // Mouse decorato

        System.out.println("Ordine completo:");
        System.out.println("- " + laptopComplete.getDescription() + ": $" + laptopComplete.getPrice());
        System.out.println("- " + mouseWithInsurance.getDescription() + ": $" + mouseWithInsurance.getPrice());
        System.out.println("TOTALE: " + bigOrder.getDescription() + " - $" + bigOrder.getPrice());
        System.out.println("(Spedizione gratuita perche ordine > $50)");

        // Test salvataggio con DAO
        System.out.println("\n--- TEST DAO ---");
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.saveOrder(bigOrder);
        System.out.println("Ordine salvato nel database!");
    }
}
