/**
 * Decoratore astratto che serve come base per tutti i decoratori concreti.
 * Parte del pattern Decorator che permette di aggiungere funzionalità
 * a un OrderComponent in modo dinamico senza modificare la sua struttura.
 */
public abstract class ProductDecorator extends OrderComponent {
    protected OrderComponent component; // Il componente da decorare

    /**
     * Costruttore che accetta il componente da decorare.
     * 
     * @param component il componente base (prodotto o altro decoratore) da arricchire
     */
    public ProductDecorator(OrderComponent component) {
        this.component = component;
    }
}