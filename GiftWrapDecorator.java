/**
 * Decorator concreto per aggiungere il servizio di confezione regalo.
 * Aggiunge 2.5€ al prezzo del componente decorato.
 */
public class GiftWrapDecorator extends ProductDecorator {
    /**
     * Costruttore che riceve il componente da decorare
     * 
     * @param component componente a cui aggiungere la confezione regalo
     */
    public GiftWrapDecorator(OrderComponent component) {
        super(component);
    }

    /**
     * Calcola il prezzo aggiungendo il costo della confezione regalo
     * 
     * @return prezzo del componente + 2.5€ per la confezione regalo
     */
    public double getPrice() {
        return component.getPrice() + 2.5;
    }

    /**
     * Aggiunge "Gift Wrap" alla descrizione del componente
     * 
     * @return descrizione originale + indicazione confezione regalo
     */
    public String getDescription() {
        return component.getDescription() + " + Gift Wrap";
    }
}
