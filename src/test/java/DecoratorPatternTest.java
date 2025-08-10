// Test per Decorator Pattern - approccio principiante  
// Testa GiftWrapDecorator e InsuranceDecorator

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
        test.testDecoratorChaining();
        
        System.out.println("Tutti i test Decorator Pattern completati!");
    }
    
    public void testBasicProduct() {
        System.out.println("\nTest: Prodotto base");
        
        Product phone = new Product("Smartphone", 500.0);
        
        String description = phone.getDescription();
        double price = phone.getPrice();
        
        System.out.println("Prodotto base - Descrizione: " + description);
        System.out.println("Prodotto base - Prezzo: " + price);
        
        // Controlli
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
        
        // Controlli  
        if (!description.equals("Libro Java with gift wrapping")) {
            System.out.println("ERRORE: Descrizione gift wrap non corretta");
        }
        if (price != 27.5) { // 25.0 + 2.5
            System.out.println("ERRORE: Prezzo gift wrap non corretto");
        }
        
        // Verifica accesso al componente
        OrderComponent component = ((GiftWrapDecorator)wrappedBook).getComponent();
        if (component != book) {
            System.out.println("ERRORE: getComponent() non funziona");
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
        
        // Controlli
        if (!description.equals("Gaming Laptop with insurance")) {
            System.out.println("ERRORE: Descrizione insurance non corretta");
        }
        if (price != 1204.0) { // 1200.0 + 4.0
            System.out.println("ERRORE: Prezzo insurance non corretto");
        }
        
        // Verifica accesso al componente
        OrderComponent component = ((InsuranceDecorator)insuredLaptop).getComponent();
        if (component != laptop) {
            System.out.println("ERRORE: getComponent() non funziona per insurance");
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
        String expectedDesc = "Smartwatch with gift wrapping with insurance";
        if (!description.equals(expectedDesc)) {
            System.out.println("ERRORE: Descrizione decoratori multipli non corretta");
            System.out.println("Atteso: " + expectedDesc);
            System.out.println("Ottenuto: " + description);
        }
        if (price != 306.5) { // 300 + 2.5 + 4.0
            System.out.println("ERRORE: Prezzo decoratori multipli non corretto");
        }
        
        System.out.println("Test decoratori multipli: OK");
    }
    
    public void testDecoratorChaining() {
        System.out.println("\nTest: Catena di decoratori");
        
        Product mouse = new Product("Gaming Mouse", 80.0);
        
        // Ordine diverso: prima insurance, poi gift wrap
        OrderComponent insuredMouse = new InsuranceDecorator(mouse);
        OrderComponent finalMouse = new GiftWrapDecorator(insuredMouse);
        
        String description = finalMouse.getDescription();
        double price = finalMouse.getPrice();
        
        System.out.println("Mouse catena - Descrizione: " + description);
        System.out.println("Mouse catena - Prezzo: " + price);
        
        // Controlli
        String expectedDesc = "Gaming Mouse with insurance with gift wrapping";
        if (!description.equals(expectedDesc)) {
            System.out.println("ERRORE: Catena decoratori non corretta");
        }
        if (price != 86.5) { // 80 + 4 + 2.5
            System.out.println("ERRORE: Prezzo catena non corretto");
        }
        
        // Test accesso ai componenti nella catena
        OrderComponent innerComponent = ((GiftWrapDecorator)finalMouse).getComponent();
        if (!(innerComponent instanceof InsuranceDecorator)) {
            System.out.println("ERRORE: Catena decoratori non mantenuta");
        }
        
        System.out.println("Test catena decoratori: OK");
    }
}
