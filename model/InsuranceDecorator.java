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
        return component.getPrice() * 1.10; // 10% di assicurazione
    }

    @Override
    public String getDescription() {
        return component.getDescription() + " + Insurance";
    }
}
