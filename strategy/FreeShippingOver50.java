package strategy;

/**
 * Spedizione: gratuita sopra $50, altrimenti $5.
 */
public class FreeShippingOver50 implements ShippingCostStrategy {

    @Override
    public double calculateShipping(double orderTotal) {
        return orderTotal >= 50 ? 0.0 : 5.0;
    }
}
