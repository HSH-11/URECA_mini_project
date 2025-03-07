package app.phone.ui;

import javax.swing.*;
import java.awt.*;
import app.phone.dao.AdminUserDAO;
import app.phone.dto.AdminUserDTO;

public class AdminUserRegisterDialog extends JDialog {
    private JTextField idField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private AdminUserDAO adminUserDAO = new AdminUserDAO();

    public AdminUserRegisterDialog(JFrame parent) {
        super(parent, "관리자 등록", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("관리자 ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("이름:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("비밀번호 확인:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        JButton registerButton = new JButton("등록");
        registerButton.addActionListener(e -> registerAdmin());
        add(new JLabel());  
        add(registerButton);

        setVisible(true);
    }

    private void registerAdmin() {
        String adminId = idField.getText().trim();
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (adminId.isEmpty() || name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 정보를 입력하세요.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        AdminUserDTO admin = new AdminUserDTO(adminId, password, name);
        boolean success = adminUserDAO.registerAdmin(admin);
        if (success) {
            JOptionPane.showMessageDialog(this, "관리자 등록 완료!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "관리자 등록 실패! (ID 중복 등)");
        }
    }
}

