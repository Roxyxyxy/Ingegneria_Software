package model;

/**
 * Componente base del pattern Composite.
 */
public abstract class OrderComponent {
    public abstract double getPrice();

    public abstract String getDescription();
}