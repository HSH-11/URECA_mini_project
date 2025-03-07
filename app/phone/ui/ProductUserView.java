package app.phone.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import app.phone.dao.CustomerCouponDAO;
import app.phone.dao.ProductDAO;
import app.phone.dto.CouponDTO;
import app.phone.dto.CustomerDTO;
import app.phone.dto.ProductDTO;

// 사용자 전용 UI (상품 조회, 주문 조회 및 삭제, 보유 쿠폰 확인 가능)

public class ProductUserView extends JFrame {
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField searchWordField;
	private JButton searchButton, orderButton, listButton, checkOrderButton, checkCouponButton;
	private ProductDAO productDAO = new ProductDAO();

	public ProductUserView() {
		setTitle("상품 조회 및 주문");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		tableModel = new DefaultTableModel(new Object[] { "Product ID", "Name", "Price", "Stock" }, 0);
		table = new JTable(tableModel);
		listProducts();

		// 검색 필드 설정
		Dimension textFieldSize = new Dimension(400, 28);
		searchWordField = new JTextField();
		searchWordField.setPreferredSize(textFieldSize);

		searchButton = new JButton("검색");
		searchButton.addActionListener(e -> {
			String searchWord = searchWordField.getText();
			if (!searchWord.isBlank()) {
				listProducts(searchWord);
			}
		});

		JPanel searchPanel = new JPanel();
		searchPanel.add(new JLabel("제품명 검색"));
		searchPanel.add(searchWordField);
		searchPanel.add(searchButton);
		
		
		orderButton = new JButton("상품 주문");
		orderButton.addActionListener(e -> openOrderForm());
		
		listButton = new JButton("새로고침");
		listButton.addActionListener(e -> listProducts());
		
		checkOrderButton = new JButton("내 주문 내역 조회");
		checkOrderButton.addActionListener(e -> openOrderHistory());

		checkCouponButton = new JButton("내 쿠폰 조회");
		checkCouponButton.addActionListener(e -> openCouponHistory());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(orderButton);
		bottomPanel.add(listButton);
		bottomPanel.add(checkOrderButton);
		bottomPanel.add(checkCouponButton);
		
		setLayout(new BorderLayout());
		add(searchPanel, BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void listProducts() {
		clearTable();
		List<ProductDTO> products = productDAO.getAllProducts();
		for (ProductDTO product : products) {
			tableModel.addRow(new Object[] { product.getProductId(), product.getName(), product.getPrice(),
					product.getStockQuantity() });
		}
	}
	private void listProducts(String searchWord) {
		clearTable();
		List<ProductDTO> productList = productDAO.getProductsByName(searchWord);
		for (ProductDTO product : productList) {
			tableModel.addRow(new Object[] { product.getProductId(), product.getName(), product.getPrice(),
					product.getStockQuantity()});
		}
	}
	private void clearTable() {
		tableModel.setRowCount(0);
	}

	private void openOrderForm() {
		new OrderForm().setVisible(true);
	}

	private void openOrderHistory() {
		CustomerDTO customer = lookupCustomer();
		if (customer != null) {
			new MyOrderHistoryDialog(this, customer.getCustomerId()).setVisible(true);
		}
	}

	private void openCouponHistory() {
		CustomerDTO customer = lookupCustomer();
		if (customer != null) {
			showCustomerCoupons(customer.getCustomerId());
		}
	}

	private CustomerDTO lookupCustomer() {
		CustomerLookupDialog dialog = new CustomerLookupDialog(this);
		return dialog.getFoundCustomer();
	}

	private void showCustomerCoupons(int customerId) {
		CustomerCouponDAO dao = new CustomerCouponDAO();
		List<CouponDTO> coupons = dao.getCustomerCoupons(customerId);

		if (coupons.isEmpty()) {
			JOptionPane.showMessageDialog(this, "보유한 쿠폰이 없습니다.");
			return;
		}

		StringBuilder message = new StringBuilder("보유 쿠폰 목록:\n");
		for (CouponDTO coupon : coupons) {
			message.append("- ").append(coupon.getCouponName()).append(" (").append(coupon.getDiscountRate())
					.append("% 할인)\n");
		}

		JOptionPane.showMessageDialog(this, message.toString(), "내 쿠폰 조회", JOptionPane.INFORMATION_MESSAGE);
	}
}
