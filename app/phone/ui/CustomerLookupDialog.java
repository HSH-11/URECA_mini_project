package app.phone.ui;

import javax.swing.*;
import java.awt.*;
import app.phone.dao.CustomerDAO;
import app.phone.dto.CustomerDTO;

public class CustomerLookupDialog extends JDialog {
    private JTextField nameField;
    private JTextField phoneField;
    private JButton searchButton;
    private CustomerDAO customerDAO = new CustomerDAO();
    private CustomerDTO foundCustomer;

    public CustomerLookupDialog(JFrame parent) {
        super(parent, "고객 조회", true);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));
        setLocationRelativeTo(parent);

        add(new JLabel("이름:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("전화번호:"));
        phoneField = new JTextField();
        add(phoneField);

        searchButton = new JButton("조회");
        searchButton.addActionListener(e -> searchCustomer());
        add(searchButton);

        setVisible(true);
    }
    
    // 이름이랑 전화번호로 고객 구분
    private void searchCustomer() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        foundCustomer = customerDAO.findCustomerByNameAndPhone(name, phone);
        if (foundCustomer != null) {
            JOptionPane.showMessageDialog(this, "고객 확인: " + foundCustomer.getName());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "일치하는 고객이 없습니다.");
        }
    }

    public CustomerDTO getFoundCustomer() {
        return foundCustomer;
    }
}
