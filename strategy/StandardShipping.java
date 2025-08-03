package strategy;

/**
 * Spedizione standard: costo fisso 5€.
 */
public class StandardShipping implements ShippingCostStrategy {

    @Override
    public double calculateShipping(double orderTotal) {
        return 5.0;
    }
}
