public class StandardShipping implements ShippingCostStrategy {
    public double calculateShipping(double orderTotal) {
        return 5.0;
    }
}