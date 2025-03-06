package app.phone.ui;

import app.phone.dao.CustomerDAO;
import app.phone.dto.CustomerDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

public class EditCustomerDialog extends JDialog {
    private JTextField nameField, emailField, phoneField, addressField;
    private JButton updateButton, cancelButton;

    private DefaultTableModel tableModel;
    private int selectedRow;

    public EditCustomerDialog(JDialog parent, DefaultTableModel tableModel, int selectedRow) {
        super(parent, "고객 수정", true);
        this.tableModel = tableModel;
        this.selectedRow = selectedRow;

        setSize(400, 300);
        setLocationRelativeTo(parent);

        setLayout(new GridLayout(5, 2));

        add(new JLabel("이름:"));
        nameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        add(nameField);

        add(new JLabel("이메일:"));
        emailField = new JTextField((String) tableModel.getValueAt(selectedRow, 2));
        add(emailField);

        add(new JLabel("전화번호:"));
        phoneField = new JTextField((String) tableModel.getValueAt(selectedRow, 3));
        add(phoneField);

        add(new JLabel("주소:"));
        addressField = new JTextField((String) tableModel.getValueAt(selectedRow, 4));
        add(addressField);

        updateButton = new JButton("수정");
        cancelButton = new JButton("취소");

        add(updateButton);
        add(cancelButton);

        updateButton.addActionListener(this::updateCustomer);
        cancelButton.addActionListener(e -> dispose());
    }

    private void updateCustomer(ActionEvent e) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        // DB에서 고객 정보를 수정하는 로직 작성
        CustomerDTO updatedCustomer = new CustomerDTO(customerId, name, email, phone, address,"현재 시간");

        // CustomerDAO의 인스턴스를 생성하여 updateCustomer 호출
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.updateCustomer(updatedCustomer);

        // 테이블 내용 갱신
        tableModel.setValueAt(updatedCustomer.getName(), selectedRow, 1);
        tableModel.setValueAt(updatedCustomer.getEmail(), selectedRow, 2);
        tableModel.setValueAt(updatedCustomer.getPhone(), selectedRow, 3);
        tableModel.setValueAt(updatedCustomer.getAddress(), selectedRow, 4);

        dispose();
    }
}
