package model;

import java.util.ArrayList;
import java.util.List;
import strategy.ShippingCostStrategy;

/**
 * Ordine che contiene prodotti (pattern Composite + Strategy).
 */
public class Order extends OrderComponent {
    private List<OrderComponent> items = new ArrayList<>();
    private ShippingCostStrategy shippingStrategy;

    public Order(ShippingCostStrategy strategy) {
        this.shippingStrategy = strategy;
    }

    public void addItem(OrderComponent item) {
        items.add(item);
    }

    @Override
    public double getPrice() {
        double subtotal = 0;
        for (OrderComponent item : items) {
            subtotal += item.getPrice();
        }
        return subtotal + shippingStrategy.calculateShipping(subtotal);
    }

    @Override
    public String getDescription() {
        return "Order with " + items.size() + " items";
    }

    /**
     * Metodo pubblico per ottenere la lista degli items.
     */
    public List<OrderComponent> getItems() {
        return items;
    }

    /**
     * Metodo pubblico per cambiare la strategia di spedizione.
     */
    public void setShippingStrategy(ShippingCostStrategy strategy) {
        this.shippingStrategy = strategy;
    }
}
