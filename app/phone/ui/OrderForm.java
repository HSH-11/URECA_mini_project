package app.phone.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import app.phone.dao.CustomerCouponDAO;
import app.phone.dao.CustomerDAO;
import app.phone.dao.OrderDAO;
import app.phone.dao.ProductDAO;
import app.phone.dto.CouponDTO;
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

	private int appliedCouponId = -1;
	private BigDecimal discountAmount = BigDecimal.ZERO;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private int currentCustomerId = -1;
	private JLabel lblTotalPrice = new JLabel("총 금액: 0원");

	public OrderForm() {
		productDAO = new ProductDAO();
		customerDAO = new CustomerDAO();
		orderDAO = new OrderDAO();

		setTitle("주문");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new GridLayout(10, 1));

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
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				placeOrder();
			}
		});

		lblTotalPrice = new JLabel("총 금액: 0원");
		JButton checkCustomerButton = new JButton("고객 정보 확인");
		checkCustomerButton.addActionListener(e -> checkCustomerInfo());
		add(checkCustomerButton);
		
		// 추가: 쿠폰 적용 버튼
		JButton applyCouponButton = new JButton("쿠폰 적용");
		applyCouponButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentCustomerId <= 0) {
					JOptionPane.showMessageDialog(OrderForm.this, "먼저 고객 정보를 입력하고 주문을 진행해주세요.");
					return;
				}
				applyCoupon(currentCustomerId);
			}
		});
		
		// 필드 사이즈 조절
		productLabel.setPreferredSize(new Dimension(100, 30));
		productComboBox.setPreferredSize(new Dimension(200, 30));
		customerNameField.setPreferredSize(new Dimension(200, 30));
		customerEmailField.setPreferredSize(new Dimension(200, 30));
		customerPhoneField.setPreferredSize(new Dimension(200, 30));
		customerAddressField.setPreferredSize(new Dimension(200, 30));
		quantityField.setPreferredSize(new Dimension(200, 30));

		// 순서대로 컴포넌트 추가
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

	private void placeOrder() {
		String customerName = customerNameField.getText();
		String customerEmail = customerEmailField.getText();
		String customerPhone = customerPhoneField.getText();
		String customerAddress = customerAddressField.getText();
		int quantity = Integer.parseInt(quantityField.getText());
		ProductDTO selectedProduct = (ProductDTO) productComboBox.getSelectedItem();

		CustomerDTO customer = customerDAO.findOrCreateCustomer(customerName, customerEmail, customerPhone,
				customerAddress);
		currentCustomerId = customer.getCustomerId();

		OrderDTO newOrder = new OrderDTO();
		newOrder.setCustomerId(customer.getCustomerId());
		newOrder.setShippingAddress(customerAddress);

		BigDecimal unitPrice = BigDecimal.valueOf(selectedProduct.getPrice());
		totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity)); // 총액 계산

		OrderItemDTO orderItem = new OrderItemDTO(0, 0, selectedProduct.getProductId(), selectedProduct.getName(),
				quantity, unitPrice);
		newOrder.addOrderItem(orderItem);
		
		newOrder.setDiscountAmount(discountAmount);  // 적용된 할인 금액 저장
	    BigDecimal finalAmount = totalAmount.subtract(discountAmount);
	    newOrder.setTotalAmount(finalAmount);  // 할인 적용된 최종 금액 저장

		// 주문 저장 (쿠폰 적용 금액 포함)
		int orderId = orderDAO.createOrder(newOrder, appliedCouponId);
		orderItem.setOrderId(orderId);
		
		productDAO.updateStock(selectedProduct.getProductId(), selectedProduct.getStockQuantity() - quantity);

		JOptionPane.showMessageDialog(this, "주문이 완료되었습니다!");
	}

	private void applyCoupon(int customerId) {
		CustomerCouponDAO customerCouponDAO = new CustomerCouponDAO();
		List<CouponDTO> coupons = customerCouponDAO.getCustomerCoupons(customerId);

		if (coupons.isEmpty()) {
			JOptionPane.showMessageDialog(this, "사용 가능한 쿠폰이 없습니다.");
			return;
		}

		CouponDTO selectedCoupon = (CouponDTO) JOptionPane.showInputDialog(this, "적용할 쿠폰을 선택하세요", "쿠폰 선택",
				JOptionPane.QUESTION_MESSAGE, null, coupons.toArray(), null);

		if (selectedCoupon != null) {
			double discountRate = selectedCoupon.getDiscountRate() / 100.0;
			discountAmount = totalAmount.multiply(BigDecimal.valueOf(discountRate));
			BigDecimal discountedPrice = totalAmount.subtract(discountAmount);

			lblTotalPrice.setText(String.format("총 금액: %,d원 (할인 적용됨)", discountedPrice.longValue()));

			appliedCouponId = selectedCoupon.getCouponId();
		}
	}
	private void checkCustomerInfo() {
	    String customerName = customerNameField.getText();
	    String customerEmail = customerEmailField.getText();
	    String customerPhone = customerPhoneField.getText();
	    String customerAddress = customerAddressField.getText();

	    if (customerName.isEmpty() || customerEmail.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "고객 이름과 이메일은 필수 입력입니다.");
	        return;
	    }

	    CustomerDTO customer = customerDAO.findOrCreateCustomer(customerName, customerEmail, customerPhone, customerAddress);
	    currentCustomerId = customer.getCustomerId();

	    JOptionPane.showMessageDialog(this, "고객 정보 확인 완료 (ID: " + currentCustomerId + ")");
	    calculateTotalAmount();
	}
	private void calculateTotalAmount() {
	    try {
	        ProductDTO selectedProduct = (ProductDTO) productComboBox.getSelectedItem();
	        int quantity = Integer.parseInt(quantityField.getText());
	        BigDecimal unitPrice = BigDecimal.valueOf(selectedProduct.getPrice());
	        totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
	        lblTotalPrice.setText(String.format("총 금액: %,d원", totalAmount.longValue()));
	    } catch (Exception e) {
	        totalAmount = BigDecimal.ZERO;
	        lblTotalPrice.setText("총 금액: 0원");
	    }
	}


	public static void main(String[] args) {
		OrderForm orderForm = new OrderForm();
		orderForm.setVisible(true);
	}

}
