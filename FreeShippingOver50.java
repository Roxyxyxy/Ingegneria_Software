public class FreeShippingOver50 implements ShippingCostStrategy {
    public double calculateShipping(double orderTotal) {
        return orderTotal > 50 ? 0.0 : 7.0;
    }
}