import java.util.ArrayList;
import java.util.List;

public class Order extends OrderComponent {
    private List<OrderComponent> items = new ArrayList<>();
    private ShippingCostStrategy shippingStrategy;

    public Order(ShippingCostStrategy strategy) {
        this.shippingStrategy = strategy;
    }

    public void addItem(OrderComponent item) {
        items.add(item);
    }

    public double getPrice() {
        double subtotal = 0;
        for (OrderComponent item : items) {
            subtotal += item.getPrice();
        }
        return subtotal + shippingStrategy.calculateShipping(subtotal);
    }

    public String getDescription() {
        return "Order with " + items.size() + " items";
    }
}