package app.phone.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import app.phone.dao.OrderDAO;
import app.phone.dto.OrderDTO;
import app.phone.dto.OrderItemDTO;

public class MyOrderHistoryDialog extends JDialog {
    private JTable table;
    private DefaultTableModel tableModel;
    private OrderDAO orderDAO = new OrderDAO();
    private int customerId;

    public MyOrderHistoryDialog(Frame owner, int customerId) {
        super(owner, "내 주문 내역", true);
        this.customerId = customerId;

        setSize(600, 450);
        setLocationRelativeTo(owner);

        tableModel = new DefaultTableModel(new Object[]{
                "주문번호", "상품명", "수량", "가격", "총 금액", "할인 금액", "배송지"
        }, 0);
        table = new JTable(tableModel);
        loadOrderHistory();

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("주문 삭제");
        deleteButton.addActionListener(e -> deleteSelectedOrder());
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);  
    }

    private void loadOrderHistory() {
        tableModel.setRowCount(0);  // 기존 데이터 클리어
        List<OrderDTO> orders = orderDAO.getOrdersByCustomerId(customerId);
        for (OrderDTO order : orders) {
            for (OrderItemDTO item : order.getOrderItems()) {
                tableModel.addRow(new Object[]{
                        order.getOrderId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        order.getTotalAmount(),
                        order.getDiscountAmount(),
                        order.getShippingAddress()
                });
            }
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 주문을 선택하세요.");
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "정말 이 주문을 삭제하시겠습니까?", "주문 삭제", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (orderDAO.deleteOrder(orderId)) {
            JOptionPane.showMessageDialog(this, "주문이 삭제되었습니다.");
            loadOrderHistory();  // 새로고침
        } else {
            JOptionPane.showMessageDialog(this, "주문 삭제에 실패했습니다.");
        }
    }
}
