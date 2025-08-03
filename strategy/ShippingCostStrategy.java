package strategy;

/**
 * Interfaccia per strategie di spedizione.
 */
public interface ShippingCostStrategy {
    double calculateShipping(double orderTotal);
}
