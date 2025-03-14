// 관리자 & 사용자 모드를 선택하는 메인 화면

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import UI.ProductUserView;


public class MainLauncher extends JFrame {

    public MainLauncher() {
        setTitle("휴대폰 판매 관리 시스템");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));
        setLocationRelativeTo(null);

        JButton adminButton = new JButton("관리자 모드");
        JButton userButton = new JButton("사용자 모드");

//        // 관리자 모드 (로그인 화면 실행)
//        adminButton.addActionListener(e -> {
//            dispose();
//            new LoginFrame();
//        });

        // 사용자 모드 (상품 조회 화면 실행)
        userButton.addActionListener(e -> {
            dispose();
            new ProductUserView().setVisible(true);
        });

        add(adminButton);
        add(userButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainLauncher::new);
    }
}
