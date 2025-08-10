// Test per Strategy Pattern - approccio principiante
// Testa le diverse strategie di spedizione

import strategy.*;
import model.*;

public class StrategyPatternTest {
    
    public static void main(String[] args) {
        System.out.println("=== Test Strategy Pattern ===");
        
        StrategyPatternTest test = new StrategyPatternTest();
        
        // Esegui tutti i test
        test.testStandardShipping();
        test.testFreeShippingOver50Below();
        test.testFreeShippingOver50Above();
        test.testStrategyChange();
        test.testEdgeCases();
        
        System.out.println("Tutti i test Strategy Pattern completati!");
    }
    
    public void testStandardShipping() {
        System.out.println("\nTest: Standard Shipping Strategy");
        
        // Test strategia standard: sempre $5
        ShippingCostStrategy strategy = new StandardShipping();
        
        double cost1 = strategy.calculateShippingCost(10.0);
        double cost2 = strategy.calculateShippingCost(100.0);
        double cost3 = strategy.calculateShippingCost(0.0);
        
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
    
    public void testFreeShippingOver50Below() {
        System.out.println("\nTest: Free Shipping Over 50 - Sotto soglia");
        
        // Test strategia free shipping sotto i $50
        ShippingCostStrategy strategy = new FreeShippingOver50();
        
        double cost1 = strategy.calculateShippingCost(30.0);
        double cost2 = strategy.calculateShippingCost(49.99);
        double cost3 = strategy.calculateShippingCost(0.0);
        
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
    
    public void testFreeShippingOver50Above() {
        System.out.println("\nTest: Free Shipping Over 50 - Sopra soglia");
        
        // Test strategia free shipping sopra i $50
        ShippingCostStrategy strategy = new FreeShippingOver50();
        
        double cost1 = strategy.calculateShippingCost(50.0);
        double cost2 = strategy.calculateShippingCost(75.5);
        double cost3 = strategy.calculateShippingCost(1000.0);
        
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
        
        // Controlli
        if (priceStandard != 80.0) { // 75 + 5
            System.out.println("ERRORE: Prezzo con standard shipping non corretto");
        }
        if (priceFree != 75.0) { // 75 + 0 (free shipping)
            System.out.println("ERRORE: Prezzo con free shipping non corretto");
        }
        
        System.out.println("Test cambio strategia: OK");
    }
    
    public void testEdgeCases() {
        System.out.println("\nTest: Casi limite");
        
        // Test casi limite per le soglie
        ShippingCostStrategy freeStrategy = new FreeShippingOver50();
        
        // Esattamente sulla soglia
        double exactlyFifty = freeStrategy.calculateShippingCost(50.0);
        double justBelow = freeStrategy.calculateShippingCost(49.999);
        double justAbove = freeStrategy.calculateShippingCost(50.001);
        
        System.out.println("Costo per $50.0: " + exactlyFifty);
        System.out.println("Costo per $49.999: " + justBelow);
        System.out.println("Costo per $50.001: " + justAbove);
        
        // Controlli
        if (exactlyFifty != 0.0) {
            System.out.println("ERRORE: Soglia $50 esatta non gestita correttamente");
        }
        if (justBelow != 5.0) {
            System.out.println("ERRORE: Appena sotto soglia non gestito correttamente");
        }
        if (justAbove != 0.0) {
            System.out.println("ERRORE: Appena sopra soglia non gestito correttamente");
        }
        
        // Test con ordine vuoto ma con strategia
        Order emptyOrder = new Order(new FreeShippingOver50());
        double emptyPrice = emptyOrder.getPrice();
        System.out.println("Prezzo ordine vuoto con free shipping: " + emptyPrice);
        
        if (emptyPrice != 5.0) { // Ordine vuoto = 0€, quindi sotto soglia
            System.out.println("ERRORE: Ordine vuoto non gestito correttamente");
        }
        
        System.out.println("Test casi limite: OK");
    }
}
