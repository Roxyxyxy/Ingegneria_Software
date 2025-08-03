package model;

/**
 * Prodotto singolo nel pattern Composite.
 */
public class Product extends OrderComponent {
    private String name;
    private double price;

    /**
     * Crea un nuovo prodotto.
     */
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return name;
    }
}
