import dao.OrderDAO;

/**
 * Utility per rimuovere ordini duplicati dal database.
 */
public class CleanDuplicates {
    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();

        System.out.println("=== PULIZIA DUPLICATI ===");
        System.out.println("Ordini prima della pulizia:");
        for (OrderDAO.OrderSummary summary : orderDAO.loadAllOrders()) {
            System.out.println(summary);
        }

        orderDAO.removeDuplicates();

        System.out.println("\nOrdini dopo la pulizia:");
        for (OrderDAO.OrderSummary summary : orderDAO.loadAllOrders()) {
            System.out.println(summary);
        }

        System.out.println("\n=== PULIZIA COMPLETATA ===");
    }
}
