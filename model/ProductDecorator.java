package model;

/**
 * Decoratore base del pattern Decorator.
 */
public abstract class ProductDecorator extends OrderComponent {
    protected OrderComponent component;

    public ProductDecorator(OrderComponent component) {
        this.component = component;
    }

    /**
     * Metodo pubblico per ottenere il componente decorato.
     */
    public OrderComponent getComponent() {
        return component;
    }
}
