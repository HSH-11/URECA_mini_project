package app.phone.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.phone.dao.AdminUserDAO;
import app.phone.dto.AdminUserDTO;

// 관리자 용도 로그인
public class LoginFrame extends JFrame {
    private JTextField adminIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;  
    private AdminUserDAO adminUserDAO = new AdminUserDAO();

    public LoginFrame() {
        setTitle("관리자 로그인");
        setSize(300, 200);  
        setLayout(new GridLayout(4, 2));  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("관리자 ID:"));
        adminIdField = new JTextField();
        add(adminIdField);

        add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> login());
        add(loginButton);

        registerButton = new JButton("관리자 등록");  
        registerButton.addActionListener(e -> openRegisterDialog());
        add(registerButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        String adminId = adminIdField.getText();
        String password = new String(passwordField.getPassword());

        AdminUserDTO admin = adminUserDAO.login(adminId, password);
        if (admin != null) {
            JOptionPane.showMessageDialog(this, admin.getName() + "님 환영합니다.");
            dispose();
            new ProductManager().setVisible(true); // 로그인 성공시 상품 관리 화면으로
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패! 관리자 정보를 확인하세요.");
        }
    }

    private void openRegisterDialog() {
        new AdminUserRegisterDialog(this);  
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
