package app.phone.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.phone.dao.OrderDAO;
import app.phone.dto.OrderDTO;
import app.phone.dto.OrderItemDTO;

public class CustomerOrderDialog extends JDialog {
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private int customerId;

    public CustomerOrderDialog(CustomerManagerDialog parent, int customerId) {
        super(parent, "주문 정보", true);
        this.customerId = customerId;

        setSize(700, 500);
        setLocationRelativeTo(parent);

       
        tableModel = new DefaultTableModel(
        	    new Object[]{
        	        "Order ID", "Order Date", "Order Status", "Payment Status", "Shipping Address", 
        	        "Product Name", "Quantity", "Unit Price", "Total Price"
        	    }, 0);

        orderTable = new JTable(tableModel);

        
        listCustomerOrders();

        
        JScrollPane scrollPane = new JScrollPane(orderTable);

       
        JButton closeButton = new JButton("닫기");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    
        closeButton.addActionListener(e -> dispose());
    }

    private void listCustomerOrders() {
        OrderDAO orderDAO = new OrderDAO();
        List<OrderDTO> orders = orderDAO.getOrdersByCustomerId(customerId);  // 주문 정보와 주문 항목들을 가져옵니다.

        // 주문 정보를 테이블에 표시
        for (OrderDTO order : orders) {
            // 주문 항목들을 하나의 행에 결합하여 추가
            for (OrderItemDTO item : order.getOrderItems()) {
                // 주문 기본 정보와 항목 정보를 결합한 한 행
                Object[] row = new Object[]{
                    order.getOrderId(),        
                    order.getOrderDate(),      
                    order.getOrderStatus(),    
                    order.getPaymentStatus(),  
                    order.getShippingAddress(),
                    item.getProductName(),     
                    item.getQuantity(),        
                    item.getUnitPrice(),       
                    item.getTotalPrice()       
                };

                tableModel.addRow(row);  // 테이블에 한 행 추가
            }
        }
    }

}
