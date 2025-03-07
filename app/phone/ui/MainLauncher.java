package app.phone.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

// 관리자 사용자 모드를 선택하는 MainLauncher
// 관리자는 관리자 로그인 인증 수행
// 사용자는 상품 조회, 주문, 주문 정보 확인, 보유 쿠폰 확인 페이지로 이동

public class MainLauncher extends JFrame {

	public MainLauncher() {
        setTitle("모드 선택");
        setSize(562, 456);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(562, 456));

        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/intro.png"));
        backgroundLabel.setBounds(0, 0, 562, 456);

        JButton adminButton = new JButton("관리자 모드");
        JButton userButton = new JButton("사용자 모드");

        adminButton.setBounds(180, 300, 200, 40);
        userButton.setBounds(180, 350, 200, 40);
        
        // 관리자 모드 이벤트
        adminButton.addActionListener(e -> {
            dispose();
            new LoginFrame(); // 관리자 로그인 수행
        });
        
        // 사용자 모드 이벤트 
        userButton.addActionListener(e -> {
            dispose();
            new ProductUserView().setVisible(true); // 상품 조회, 주문, 주문 정보 확인, 보유 쿠폰 확인 페이지
        });

        layeredPane.add(backgroundLabel, Integer.valueOf(0));  
        layeredPane.add(adminButton, Integer.valueOf(1));      
        layeredPane.add(userButton, Integer.valueOf(1));       

        add(layeredPane, BorderLayout.CENTER);

        setVisible(true);
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainLauncher::new);
	}
}
