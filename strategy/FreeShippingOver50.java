package strategy;

/**
 * Spedizione: gratuita sopra 50€, altrimenti 7€.
 */
public class FreeShippingOver50 implements ShippingCostStrategy {

    @Override
    public double calculateShipping(double orderTotal) {
        return orderTotal > 50 ? 0.0 : 7.0;
    }
}
