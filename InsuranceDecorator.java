public class InsuranceDecorator extends ProductDecorator {
    public InsuranceDecorator(OrderComponent component) {
        super(component);
    }

    public double getPrice() {
        return component.getPrice() + 4.0;
    }

    public String getDescription() {
        return component.getDescription() + " + Insurance";
    }
}