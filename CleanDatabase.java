import dao.*;

public class CleanDatabase {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        productDAO.clearAll();
        orderDAO.clearAll();

        System.out.println("Database completamente pulito!");
        System.out.println("Prodotti rimasti: " + productDAO.loadAllProducts().size());
        System.out.println("Ordini rimasti: " + orderDAO.loadAllOrders().size());
    }
}
