package model;

/**
 * Aggiunge confezione regalo (+2.5€).
 */
public class GiftWrapDecorator extends ProductDecorator {

    public GiftWrapDecorator(OrderComponent component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return component.getPrice() + 2.5;
    }

    @Override
    public String getDescription() {
        return component.getDescription() + " + Gift Wrap";
    }
}
