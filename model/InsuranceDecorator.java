package model;

/**
 * Aggiunge assicurazione (10% del prezzo).
 */
public class InsuranceDecorator extends ProductDecorator {

    public InsuranceDecorator(OrderComponent component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return Math.round(component.getPrice() * 1.10 * 100.0) / 100.0; // Arrotonda a 2 decimali
    }

    @Override
    public String getDescription() {
        return component.getDescription() + " + Insurance";
    }
}
