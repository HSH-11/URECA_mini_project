package app.phone.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
                "Order ID", "Order Status", "Payment Status", "Shipping Address",
                "Product Name", "Quantity", "Unit Price", "Discount", "Total Price"
            }, 0
        );

        orderTable = new JTable(tableModel);
        listCustomerOrders();

        JScrollPane scrollPane = new JScrollPane(orderTable);

        
        JButton closeButton = new JButton("닫기");
        JButton deleteOrderButton = new JButton("주문 삭제"); 

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteOrderButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());

        deleteOrderButton.addActionListener(e -> deleteSelectedOrder());
    }

    private void listCustomerOrders() {
        OrderDAO orderDAO = new OrderDAO();
        List<OrderDTO> orders = orderDAO.getOrdersByCustomerId(customerId);

        tableModel.setRowCount(0);  // 기존 데이터 초기화

        for (OrderDTO order : orders) {
            for (OrderItemDTO item : order.getOrderItems()) {
                Object[] row = new Object[]{
                    order.getOrderId(),
                    order.getOrderStatus(),
                    order.getPaymentStatus(),
                    order.getShippingAddress(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    order.getDiscountAmount(),
                    order.getTotalAmount()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 주문을 선택하세요.");
            return;
        }

        int orderId = (int) orderTable.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "정말로 이 주문을 삭제하시겠습니까?", "주문 삭제", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        if (orderDAO.deleteOrder(orderId)) {
            JOptionPane.showMessageDialog(this, "주문이 삭제되었습니다.");
            listCustomerOrders();  // 삭제 후 목록 새로고침
        } else {
            JOptionPane.showMessageDialog(this, "주문 삭제에 실패했습니다.");
        }
    }
}

