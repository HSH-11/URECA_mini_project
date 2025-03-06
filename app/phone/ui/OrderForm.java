package app.phone.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import app.phone.dao.CustomerDAO;
import app.phone.dao.OrderDAO;
import app.phone.dao.ProductDAO;
import app.phone.dto.CustomerDTO;
import app.phone.dto.OrderDTO;
import app.phone.dto.OrderItemDTO;
import app.phone.dto.ProductDTO;

public class OrderForm extends JFrame {
    private JComboBox<ProductDTO> productComboBox;
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField customerPhoneField;  
    private JTextField customerAddressField; 
    private JTextField quantityField;
    private JButton orderButton;
    
    private ProductDAO productDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    public OrderForm() {
        productDAO = new ProductDAO();
        customerDAO = new CustomerDAO();
        orderDAO = new OrderDAO();
        
        setTitle("주문");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(7, 1));  

        JLabel productLabel = new JLabel("제품 선택:");
        productComboBox = new JComboBox<>(productDAO.getAllProducts().toArray(new ProductDTO[0]));

        // 고객 정보 입력 필드
        JLabel nameLabel = new JLabel("고객 이름:");
        customerNameField = new JTextField();

        JLabel emailLabel = new JLabel("고객 이메일:");
        customerEmailField = new JTextField();

        JLabel phoneLabel = new JLabel("고객 전화번호:");  
        customerPhoneField = new JTextField();  

        JLabel addressLabel = new JLabel("고객 주소:");  
        customerAddressField = new JTextField();  

        JLabel quantityLabel = new JLabel("수량:");
        quantityField = new JTextField();

        orderButton = new JButton("주문");
        
        // 주문 정보 테이블 반영
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });
        
        // 필드 사이즈 조절 개빡샘
        productLabel.setPreferredSize(new Dimension(100, 30)); 
        productComboBox.setPreferredSize(new Dimension(200, 30)); 
        customerNameField.setPreferredSize(new Dimension(200, 30)); 
        customerEmailField.setPreferredSize(new Dimension(200, 30));
        customerPhoneField.setPreferredSize(new Dimension(200, 30)); 
        customerAddressField.setPreferredSize(new Dimension(200, 30)); 
        quantityField.setPreferredSize(new Dimension(200, 30)); 
        
        // 이제 라벨을 붙이기
        add(productLabel);
        add(productComboBox);
        add(nameLabel);
        add(customerNameField);
        add(emailLabel);
        add(customerEmailField);
        add(phoneLabel);
        add(customerPhoneField);  
        add(addressLabel);
        add(customerAddressField);  
        add(quantityLabel);
        add(quantityField);
        add(new JLabel());  
        add(orderButton);
    }

    private void placeOrder() {
        // 고객 정보 입력받기
        String customerName = customerNameField.getText();
        String customerEmail = customerEmailField.getText();
        String customerPhone = customerPhoneField.getText();  
        String customerAddress = customerAddressField.getText();  
        int quantity = Integer.parseInt(quantityField.getText());
        ProductDTO selectedProduct = (ProductDTO) productComboBox.getSelectedItem();

        // 고객 정보 확인 및 등록
        CustomerDTO customer = customerDAO.findOrCreateCustomer(customerName, customerEmail, customerPhone, customerAddress);

        // 주문 생성
        OrderDTO newOrder = new OrderDTO();
        newOrder.setCustomerId(customer.getCustomerId());
        newOrder.setShippingAddress(customerAddress);  // 배송지 추가

        BigDecimal unitPrice = BigDecimal.valueOf(selectedProduct.getPrice());

        // OrderItemDTO 생성 (필요한 값들을 맞춰주기)
        OrderItemDTO orderItem = new OrderItemDTO(
            0,  // orderItemId는 생성 시 자동 증가되므로 0으로 설정
            0,  // orderId는 추후 생성 후 할당될 예정
            selectedProduct.getProductId(),
            selectedProduct.getName(),
            quantity,
            unitPrice
        );

        // 새로운 주문 항목 추가
        newOrder.addOrderItem(orderItem);

        // 주문 저장 (주문을 DB에 추가)
        int orderId = orderDAO.createOrder(newOrder);  // createOrder가 orderId를 반환한다고 가정

        // 주문 항목에 orderId 설정
        orderItem.setOrderId(orderId);
        
        // 재고 감소
        productDAO.updateStock(selectedProduct.getProductId(), selectedProduct.getStockQuantity() - quantity);

        // 사용자에게 성공 메시지 표시
        JOptionPane.showMessageDialog(this, "주문이 완료되었습니다!");
    }

    public static void main(String[] args) {
        OrderForm orderForm = new OrderForm();
        orderForm.setVisible(true);
    }
}
