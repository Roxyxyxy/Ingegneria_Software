// Test del pattern Decorator

import model.*;

public class DecoratorPatternTest {

    public static void main(String[] args) {
        System.out.println("=== Test Decorator Pattern ===");

        DecoratorPatternTest test = new DecoratorPatternTest();

        // Esegui tutti i test
        test.testBasicProduct();
        test.testGiftWrapDecorator();
        test.testInsuranceDecorator();
        test.testMultipleDecorators();

        System.out.println("Tutti i test Decorator Pattern completati!");
    }

    public void testBasicProduct() {
        System.out.println("\nTest: Prodotto base");

        Product phone = new Product("iPhone Pro", 1100.0);

        String description = phone.getDescription();
        double price = phone.getPrice();

        System.out.println("Prodotto base - Descrizione: " + description);
        System.out.println("Prodotto base - Prezzo: " + price);

        // Controlli semplici
        if (!description.equals("iPhone Pro")) {
            System.out.println("ERRORE: Descrizione base non corretta");
        }
        if (price != 1100.0) {
            System.out.println("ERRORE: Prezzo base non corretto");
        }

        System.out.println("Test prodotto base: OK");
    }

    public void testGiftWrapDecorator() {
        System.out.println("\nTest: Gift Wrap Decorator");

        Product tablet = new Product("iPad", 650.0);
        OrderComponent wrappedTablet = new GiftWrapDecorator(tablet);

        String description = wrappedTablet.getDescription();
        double price = wrappedTablet.getPrice();

        System.out.println("Tablet con confezione - Descrizione: " + description);
        System.out.println("Tablet con confezione - Prezzo: " + price);

        // Controlli semplici
        if (!description.equals("iPad + Gift Wrap")) {
            System.out.println("ERRORE: Descrizione gift wrap non corretta");
        }
        if (price != 655.0) { // 650.0 + 5.0
            System.out.println("ERRORE: Prezzo gift wrap non corretto");
        }

        System.out.println("Test Gift Wrap Decorator: OK");
    }

    public void testInsuranceDecorator() {
        System.out.println("\nTest: Insurance Decorator");

        Product airpods = new Product("AirPods", 180.0);
        OrderComponent insuredAirpods = new InsuranceDecorator(airpods);

        String description = insuredAirpods.getDescription();
        double price = insuredAirpods.getPrice();

        System.out.println("AirPods assicurati - Descrizione: " + description);
        System.out.println("AirPods assicurati - Prezzo: " + price);

        // Controlli semplici
        if (!description.equals("AirPods + Insurance")) {
            System.out.println("ERRORE: Descrizione insurance non corretta");
        }
        if (price != 198.0) { // 180.0 * 1.10 (10%)
            System.out.println("ERRORE: Prezzo insurance non corretto");
        }

        System.out.println("Test Insurance Decorator: OK");
    }

    public void testMultipleDecorators() {
        System.out.println("\nTest: Decoratori multipli");

        Product watch = new Product("Apple Watch", 450.0);

        // Applica entrambi i decoratori
        OrderComponent wrappedWatch = new GiftWrapDecorator(watch);
        OrderComponent fullDecorated = new InsuranceDecorator(wrappedWatch);

        String description = fullDecorated.getDescription();
        double price = fullDecorated.getPrice();

        System.out.println("Watch decorato - Descrizione: " + description);
        System.out.println("Watch decorato - Prezzo: " + price);

        // Controlli
        String expectedDesc = "Apple Watch + Gift Wrap + Insurance";
        if (!description.equals(expectedDesc)) {
            System.out.println("ERRORE: Descrizione decoratori multipli non corretta");
        }
        if (price != 500.5) { // (450 + 5) * 1.10 = 455 * 1.10 = 500.5
            System.out.println("ERRORE: Prezzo decoratori multipli non corretto");
        }

        System.out.println("Test decoratori multipli: OK");
    }
}
