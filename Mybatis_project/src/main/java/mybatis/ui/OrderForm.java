package mybatis.ui;

import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.ibatis.session.SqlSession;

import mybatis.common.MyBatisConfig;
import mybatis.dao.CustomerDAO;
import mybatis.dao.OrderDAO;
import mybatis.dao.ProductDAO;
import mybatis.dto.CustomerDTO;
import mybatis.dto.OrderDTO;
import mybatis.dto.OrderItemDTO;
import mybatis.dto.ProductDTO;
import mybatis.service.CustomerService;

public class OrderForm extends JFrame {
    private JComboBox<ProductDTO> productComboBox;
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField customerPhoneField;
    private JTextField customerAddressField;
    private JTextField quantityField;
    private JButton orderButton;

    private int appliedCouponId = -1;
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private int currentCustomerId = -1;
    private JLabel lblTotalPrice = new JLabel("총 금액: 0원");

    public OrderForm() {
        setTitle("주문");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        JLabel productLabel = new JLabel("제품 선택:");
        productComboBox = new JComboBox<>(getProductsFromDB().toArray(new ProductDTO[0]));

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
        orderButton.addActionListener(e -> placeOrder());

        lblTotalPrice = new JLabel("총 금액: 0원");
        JButton checkCustomerButton = new JButton("고객 인증");
        checkCustomerButton.addActionListener(e -> checkCustomerInfo());

        JButton applyCouponButton = new JButton("쿠폰 적용");
        applyCouponButton.addActionListener(e -> applyCoupon());

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

    //  상품 목록 가져오기
    private List<ProductDTO> getProductsFromDB() {
        try (SqlSession session = MyBatisConfig.getSession()) {
            ProductDAO productDAO = session.getMapper(ProductDAO.class);
            return productDAO.getAllProducts();
        }
    }
    private OrderDTO createOrder(int customerId, ProductDTO product, int quantity) {
        OrderDTO newOrder = new OrderDTO();
        newOrder.setCustomerId(customerId);
        newOrder.setShippingAddress(customerAddressField.getText());

        BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
        totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));

        OrderItemDTO orderItem = new OrderItemDTO(null, null, product.getProductId(), product.getName(), quantity, unitPrice);
        newOrder.addOrderItem(orderItem);

        newOrder.setDiscountAmount(discountAmount);
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);
        newOrder.setTotalAmount(finalAmount);

        return newOrder;
    }

    // 주문 정보 입력 받기
    private void placeOrder() {
        try (SqlSession session = MyBatisConfig.getSession()) {
            CustomerService customerService = new CustomerService();
            OrderDAO orderDAO = session.getMapper(OrderDAO.class);
            ProductDAO productDAO = session.getMapper(ProductDAO.class);

            String customerName = customerNameField.getText();
            String customerEmail = customerEmailField.getText();
            String customerPhone = customerPhoneField.getText();
            String customerAddress = customerAddressField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            ProductDTO selectedProduct = (ProductDTO) productComboBox.getSelectedItem();

            // 기존 고객이 있으면 가져오고, 없으면 새로 추가
            CustomerDTO customer = customerService.findOrCreateCustomer(customerName, customerEmail, customerPhone, customerAddress);
            currentCustomerId = customer.getCustomerId();

            // 주문 생성 로직을 별도 메서드로 호출
            OrderDTO newOrder = createOrder(currentCustomerId, selectedProduct, quantity);

            // 주문 및 주문 아이템 저장
            int orderId = orderDAO.createOrder(newOrder);
            for (OrderItemDTO orderItem : newOrder.getOrderItems()) {
                orderItem.setOrderId(orderId);
                orderDAO.addOrderItem(orderItem);
            }

            // 재고 업데이트
            productDAO.updateStock(selectedProduct.getProductId(), selectedProduct.getStockQuantity() - quantity);

            session.commit();  // ✅ MyBatis 트랜잭션 완료

            JOptionPane.showMessageDialog(this, "주문이 완료되었습니다!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주문 처리 중 오류 발생!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    // 고객 정보 조회
    private void checkCustomerInfo() {
        try (SqlSession session = MyBatisConfig.getSession()) {
            CustomerDAO customerDAO = session.getMapper(CustomerDAO.class);
            String customerName = customerNameField.getText();
            String customerPhone = customerPhoneField.getText();

            if (customerName.isEmpty() || customerPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "고객 이름과 전화번호는 필수 입력입니다.");
                return;
            }

            CustomerDTO customer = customerDAO.findCustomerByNameAndPhone(customerName, customerPhone);
            if (customer != null) {
                currentCustomerId = customer.getCustomerId();
                JOptionPane.showMessageDialog(this, "기존 고객 정보 확인 완료 (ID: " + currentCustomerId + ")");
            } else {
                JOptionPane.showMessageDialog(this, "등록되지 않은 고객입니다. 주문 시 자동 등록됩니다.");
                currentCustomerId = -1;
            }
        }
    }

    // 기반 쿠폰 적용
    private void applyCoupon() {
        JOptionPane.showMessageDialog(this, "쿠폰 기능은 MyBatis 적용 후 추가 개발 예정입니다.");
    }

    public static void main(String[] args) {
        OrderForm orderForm = new OrderForm();
        orderForm.setVisible(true);
    }
}