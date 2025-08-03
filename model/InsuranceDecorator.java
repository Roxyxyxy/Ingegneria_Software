package model;

/**
 * Aggiunge assicurazione (+4€).
 */
public class InsuranceDecorator extends ProductDecorator {

    public InsuranceDecorator(OrderComponent component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return component.getPrice() + 4.0;
    }

    @Override
    public String getDescription() {
        return component.getDescription() + " + Insurance";
    }
}
