public class Main {
    public static void main(String[] args) {
        OrderComponent product1 = new Product("T-Shirt", 20);
        OrderComponent product2 = new InsuranceDecorator(
                new GiftWrapDecorator(
                        new Product("Mug", 15)));

        Order order = new Order(new FreeShippingOver50());
        order.addItem(product1);
        order.addItem(product2);

        System.out.println("Total: " + order.getPrice());
        System.out.println("Description: " + order.getDescription());
    }
}