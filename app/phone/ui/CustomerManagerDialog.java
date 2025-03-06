package app.phone.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.phone.dao.CustomerDAO;
import app.phone.dto.CustomerDTO;

public class CustomerManagerDialog extends JDialog {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO = new CustomerDAO();

    public CustomerManagerDialog(JFrame parent) {
        super(parent, "고객 관리", true);

        
        setSize(600, 400);
        setLocationRelativeTo(parent);

        
        tableModel = new DefaultTableModel(new Object[]{"Customer ID", "Name", "Email", "Phone", "Address", "Created At"}, 0);
        customerTable = new JTable(tableModel);

        
        listCustomers();

        
        JScrollPane scrollPane = new JScrollPane(customerTable);

        // 버튼 설정
        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");
        JButton viewOrderButton = new JButton("주문 정보 보기");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewOrderButton);

        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        
        editButton.addActionListener(e -> openEditCustomerDialog());
        deleteButton.addActionListener(e -> deleteCustomer());
        viewOrderButton.addActionListener(e -> viewCustomerOrders());
    }

    private void listCustomers() {
        List<CustomerDTO> customerList = customerDAO.getAllCustomers();
        for (CustomerDTO customer : customerList) {
            tableModel.addRow(new Object[]{customer.getCustomerId(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress(), customer.getCreatedAt()});
        }
    }

    private void openEditCustomerDialog() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int customerId = (int) customerTable.getValueAt(selectedRow, 0);
            EditCustomerDialog editDialog = new EditCustomerDialog(this, this.tableModel, selectedRow);
            editDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "수정할 고객을 선택하세요.");
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int customerId = (int) customerTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "정말로 이 고객을 삭제하시겠습니까?", "고객 삭제", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean result = customerDAO.deleteCustomer(customerId);
                if (result) {
                    JOptionPane.showMessageDialog(this, "고객이 삭제되었습니다.");
                    tableModel.removeRow(selectedRow); 
                } else {
                    JOptionPane.showMessageDialog(this, "고객 삭제에 실패했습니다.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 고객을 선택하세요.");
        }
    }

    private void viewCustomerOrders() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int customerId = (int) customerTable.getValueAt(selectedRow, 0);
            CustomerOrderDialog orderDialog = new CustomerOrderDialog(this, customerId); // 고객 ID 전달
            orderDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "주문 정보를 확인할 고객을 선택하세요.");
        }
    }
}
