package app.phone.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.phone.common.DBManager;
import app.phone.dto.OrderDTO;
import app.phone.dto.OrderItemDTO;

public class OrderDAO {

	// 고객 ID에 해당하는 주문 리스트를 가져오는 메서드
	public List<OrderDTO> getOrdersByCustomerId(int customerId) {
		List<OrderDTO> orders = new ArrayList<>();
		String query = "SELECT o.order_id, o.order_date, o.order_status, o.payment_status, o.shipping_address, "
				+ "oi.order_item_id, oi.product_id, p.name AS product_name, oi.quantity, "
				+ "COALESCE(oi.discounted_price, oi.unit_price) AS price " + "FROM Orders o "
				+ "JOIN OrderItems oi ON o.order_id = oi.order_id " + "JOIN Products p ON oi.product_id = p.product_id "
				+ "WHERE o.customer_id = ?";

		try (Connection connection = DBManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();

			OrderDTO currentOrder = null;
			while (rs.next()) {
				int orderId = rs.getInt("order_id");

				// 새로운 주문이면 OrderDTO 생성 후 리스트에 추가
				if (currentOrder == null || currentOrder.getOrderId() != orderId) {
					currentOrder = new OrderDTO(orderId, customerId, rs.getString("order_date"),
							rs.getString("order_status"), rs.getString("payment_status"),
							rs.getString("shipping_address"));
					orders.add(currentOrder);
				}

				// 주문 항목 추가
				currentOrder.addOrderItem(new OrderItemDTO(rs.getInt("order_item_id"), orderId, rs.getInt("product_id"),
						rs.getString("product_name"), rs.getInt("quantity"), rs.getBigDecimal("price")));
			}
		} catch (SQLException e) {
			System.err.println("오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return orders;
	}

	// 고객의 주문 항목들을 조회하는 메서드
	public List<OrderItemDTO> getOrderItemsByCustomerId(int customerId) {
		List<OrderItemDTO> orderItems = new ArrayList<>();
		String query = "SELECT oi.order_item_id, oi.order_id, oi.product_id, p.name AS product_name, oi.quantity, "
				+ "COALESCE(oi.discounted_price, oi.unit_price) AS price " + "FROM Orders o "
				+ "JOIN OrderItems oi ON o.order_id = oi.order_id " + "JOIN Products p ON oi.product_id = p.product_id "
				+ "WHERE o.customer_id = ?";

		try (Connection connection = DBManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				orderItems.add(
						new OrderItemDTO(rs.getInt("order_item_id"), rs.getInt("order_id"), rs.getInt("product_id"),
								rs.getString("product_name"), rs.getInt("quantity"), rs.getBigDecimal("price")));
			}
		} catch (SQLException e) {
			System.err.println("오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return orderItems;
	}

	// 주문을 생성하는 메서드
	public int createOrder(OrderDTO order) {
		String orderQuery = "INSERT INTO Orders (customer_id, order_date, order_status, total_amount, shipping_address) VALUES (?, ?, ?, ?, ?)";
		String orderItemQuery = "INSERT INTO OrderItems (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
		int generatedOrderId = -1;

		try (Connection connection = DBManager.getConnection();
				PreparedStatement psOrder = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {

			// 총액 계산
			BigDecimal totalAmount = calculateTotalAmount(order);

			// 주문 정보를 Orders 테이블에 삽입
			psOrder.setInt(1, order.getCustomerId());
			psOrder.setString(2, new java.sql.Date(System.currentTimeMillis()).toString());
			psOrder.setString(3, "Pending"); // 주문 상태는 "Pending"
			psOrder.setBigDecimal(4, totalAmount);
			psOrder.setString(5, order.getShippingAddress());
			psOrder.executeUpdate();

			// 생성된 키(주문 ID)를 가져옴
			ResultSet generatedKeys = psOrder.getGeneratedKeys();
			if (generatedKeys.next()) {
				generatedOrderId = generatedKeys.getInt(1); // 생성된 orderId 가져오기
				order.setOrderId(generatedOrderId); // OrderDTO에 orderId 설정

				// 주문 항목을 OrderItems 테이블에 삽입
				try (PreparedStatement psOrderItem = connection.prepareStatement(orderItemQuery)) {
					for (OrderItemDTO item : order.getOrderItems()) {
						psOrderItem.setInt(1, generatedOrderId);
						psOrderItem.setInt(2, item.getProductId());
						psOrderItem.setInt(3, item.getQuantity());
						psOrderItem.setBigDecimal(4, item.getUnitPrice());
						psOrderItem.addBatch();
					}
					psOrderItem.executeBatch();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generatedOrderId; // 생성된 orderId 반환
	}


	// 총액을 계산하는 메서드
	private BigDecimal calculateTotalAmount(OrderDTO order) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (OrderItemDTO item : order.getOrderItems()) {
			// unitPrice * quantity 계산
			BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			totalAmount = totalAmount.add(itemTotal);
		}
		return totalAmount;
	}
}
