package UI;


import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import entity.Customer;
import entity.Order;
import entity.Product;
import service.CustomerService;
import service.OrderService;
import service.ProductService;

public class OrderForm extends JFrame {
    private JComboBox<Product> productComboBox;
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField customerPhoneField;
    private JTextField customerAddressField;
    private JTextField quantityField;
    private JButton orderButton;
    private JLabel lblTotalPrice;

    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;

    private int currentCustomerId = -1;

    public OrderForm() {
        customerService = new CustomerService();
        orderService = new OrderService();
        productService = new ProductService();

        setTitle("주문");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        JLabel productLabel = new JLabel("제품 선택:");
        productComboBox = new JComboBox<>();
        loadProducts(); // 상품 목록 불러오기

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

        lblTotalPrice = new JLabel("총 금액: 0원");

        JButton checkCustomerButton = new JButton("고객 인증");
        checkCustomerButton.addActionListener(e -> checkCustomerInfo());

        JButton applyCouponButton = new JButton("쿠폰 적용");
        applyCouponButton.addActionListener(e -> applyCoupon());

        orderButton = new JButton("주문");
        orderButton.addActionListener(e -> placeOrder());

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
        add(checkCustomerButton);
        add(lblTotalPrice);
        add(applyCouponButton);
        add(orderButton);
    }

    // 제품 목록 불러오기
    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    // 고객 인증
    private void checkCustomerInfo() {
        String customerName = customerNameField.getText();
        String customerPhone = customerPhoneField.getText();

        if (customerName.isEmpty() || customerPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "고객 이름과 전화번호는 필수 입력입니다.");
            return;
        }

        Customer customer = customerService.getCustomerByNameAndPhone(customerName, customerPhone);

        if (customer != null) {
            currentCustomerId = customer.getCustomerId();
            JOptionPane.showMessageDialog(this, "기존 고객 정보 확인 완료 (ID: " + currentCustomerId + ")");
        } else {
            JOptionPane.showMessageDialog(this, "등록되지 않은 고객입니다. 주문 시 자동 등록됩니다.");
            currentCustomerId = -1;
        }
    }

    // 주문 처리
    private void placeOrder() {
        try {
            String customerName = customerNameField.getText();
            String customerEmail = customerEmailField.getText();
            String customerPhone = customerPhoneField.getText();
            String customerAddress = customerAddressField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            Product selectedProduct = (Product) productComboBox.getSelectedItem();

            // 고객 찾거나 생성
            Customer customer = customerService.findOrCreateCustomer(customerName, customerEmail, customerPhone, customerAddress);
            currentCustomerId = customer.getCustomerId();

            // 가격 및 할인 계산
            BigDecimal unitPrice = BigDecimal.valueOf(selectedProduct.getPrice());
            BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal discountRate = new BigDecimal("0.10"); // 10% 할인 적용
            BigDecimal discountAmount = totalAmount.multiply(discountRate);
            BigDecimal finalAmount = totalAmount.subtract(discountAmount);

            // 주문 생성 (상품 정보 포함)
            Order newOrder = orderService.createOrder(customer, selectedProduct, quantity, unitPrice, finalAmount, discountAmount, customerAddress);

            // 재고 업데이트
            productService.updateStock(selectedProduct.getProductId(), selectedProduct.getStockQuantity() - quantity);

            JOptionPane.showMessageDialog(this, "주문이 완료되었습니다! 주문 ID: " + newOrder.getOrderId());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주문 처리 중 오류 발생!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }



    // 쿠폰 적용
    private void applyCoupon() {
        JOptionPane.showMessageDialog(this, "쿠폰 기능은 아직 구현되지 않았습니다.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderForm orderForm = new OrderForm();
            orderForm.setVisible(true);
        });
    }
}
