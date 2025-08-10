// Test del pattern Decorator - GiftWrap e Insurance
package test;

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

        Product phone = new Product("Smartphone", 500.0);

        String description = phone.getDescription();
        double price = phone.getPrice();

        System.out.println("Prodotto base - Descrizione: " + description);
        System.out.println("Prodotto base - Prezzo: " + price);

        // Controlli semplici
        if (!description.equals("Smartphone")) {
            System.out.println("ERRORE: Descrizione base non corretta");
        }
        if (price != 500.0) {
            System.out.println("ERRORE: Prezzo base non corretto");
        }

        System.out.println("Test prodotto base: OK");
    }

    public void testGiftWrapDecorator() {
        System.out.println("\nTest: Gift Wrap Decorator");

        Product book = new Product("Libro Java", 25.0);
        OrderComponent wrappedBook = new GiftWrapDecorator(book);

        String description = wrappedBook.getDescription();
        double price = wrappedBook.getPrice();

        System.out.println("Libro con confezione - Descrizione: " + description);
        System.out.println("Libro con confezione - Prezzo: " + price);

        // Controlli semplici
        if (!description.equals("Libro Java + Gift Wrap")) {
            System.out.println("ERRORE: Descrizione gift wrap non corretta");
        }
        if (price != 27.5) { // 25.0 + 2.5
            System.out.println("ERRORE: Prezzo gift wrap non corretto");
        }

        System.out.println("Test Gift Wrap Decorator: OK");
    }

    public void testInsuranceDecorator() {
        System.out.println("\nTest: Insurance Decorator");

        Product laptop = new Product("Gaming Laptop", 1200.0);
        OrderComponent insuredLaptop = new InsuranceDecorator(laptop);

        String description = insuredLaptop.getDescription();
        double price = insuredLaptop.getPrice();

        System.out.println("Laptop assicurato - Descrizione: " + description);
        System.out.println("Laptop assicurato - Prezzo: " + price);

        // Controlli semplici
        if (!description.equals("Gaming Laptop + Insurance")) {
            System.out.println("ERRORE: Descrizione insurance non corretta");
        }
        if (price != 1204.0) { // 1200.0 + 4.0
            System.out.println("ERRORE: Prezzo insurance non corretto");
        }

        System.out.println("Test Insurance Decorator: OK");
    }

    public void testMultipleDecorators() {
        System.out.println("\nTest: Decoratori multipli");

        Product watch = new Product("Smartwatch", 300.0);

        // Applica entrambi i decoratori
        OrderComponent wrappedWatch = new GiftWrapDecorator(watch);
        OrderComponent fullDecorated = new InsuranceDecorator(wrappedWatch);

        String description = fullDecorated.getDescription();
        double price = fullDecorated.getPrice();

        System.out.println("Watch decorato - Descrizione: " + description);
        System.out.println("Watch decorato - Prezzo: " + price);

        // Controlli 
        String expectedDesc = "Smartwatch + Gift Wrap + Insurance";
        if (!description.equals(expectedDesc)) {
            System.out.println("ERRORE: Descrizione decoratori multipli non corretta");
        }
        if (price != 306.5) { // 300 + 2.5 + 4.0
            System.out.println("ERRORE: Prezzo decoratori multipli non corretto");
        }

        System.out.println("Test decoratori multipli: OK");
    }
}
