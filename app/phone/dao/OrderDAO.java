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

	public List<OrderDTO> getOrdersByCustomerId(int customerId) {
	    List<OrderDTO> orders = new ArrayList<>();
	    String query = "SELECT o.order_id, o.order_status, o.payment_status, o.shipping_address, "
	                 + "o.total_amount, o.discount_amount, "  // 추가된 부분
	                 + "oi.order_item_id, oi.product_id, p.name AS product_name, oi.quantity, oi.unit_price "
	                 + "FROM Orders o "
	                 + "JOIN OrderItems oi ON o.order_id = oi.order_id "
	                 + "JOIN Products p ON oi.product_id = p.product_id "
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
	                currentOrder = new OrderDTO(orderId, customerId,
	                        rs.getString("order_status"), 
	                        rs.getString("payment_status"),
	                        rs.getString("shipping_address"));

	                // 할인 금액 및 총 금액 추가
	                currentOrder.setTotalAmount(rs.getBigDecimal("total_amount"));  // 추가
	                currentOrder.setDiscountAmount(rs.getBigDecimal("discount_amount"));  // 추가

	                orders.add(currentOrder);
	            }

	            // 주문 항목 추가
	            currentOrder.addOrderItem(new OrderItemDTO(
	                    rs.getInt("order_item_id"),
	                    orderId,
	                    rs.getInt("product_id"),
	                    rs.getString("product_name"),
	                    rs.getInt("quantity"),
	                    rs.getBigDecimal("unit_price")
	            ));
	        }
	    } catch (SQLException e) {
	        System.err.println("오류 발생: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return orders;
	}


	// 주문을 생성하는 메서드
	public int createOrder(OrderDTO order, int appliedCouponId) {
	    String orderQuery = "INSERT INTO Orders (customer_id, order_status, total_amount, discount_amount, shipping_address) " +
	                        "VALUES (?, ?, ?, ?, ?)";
	    String orderItemQuery = "INSERT INTO OrderItems (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
	    int generatedOrderId = -1;

	    try (Connection connection = DBManager.getConnection();
	         PreparedStatement psOrder = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {

	        psOrder.setInt(1, order.getCustomerId());
	        psOrder.setString(2, "Pending");
	        psOrder.setBigDecimal(3, order.getTotalAmount());  // 할인된 최종 금액 저장
	        psOrder.setBigDecimal(4, order.getDiscountAmount());  // 할인 금액 저장
	        psOrder.setString(5, order.getShippingAddress());
	        psOrder.executeUpdate();

	        ResultSet generatedKeys = psOrder.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            generatedOrderId = generatedKeys.getInt(1);
	            order.setOrderId(generatedOrderId);

	            // 주문 아이템 저장
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

	            // 쿠폰 사용 처리
	            if (appliedCouponId > 0) {
	                useCoupon(order.getCustomerId(), appliedCouponId, connection);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return generatedOrderId;
	}

	private void useCoupon(int customerId, int couponId, Connection connection) throws SQLException {
	    String sql = "DELETE FROM CustomerCoupons WHERE customer_id = ? AND coupon_id = ? LIMIT 1";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, customerId);
	        pstmt.setInt(2, couponId);
	        pstmt.executeUpdate();
	    }
	}
	
	public BigDecimal calculateTotalAmount(OrderDTO order) {
	    BigDecimal totalAmount = BigDecimal.ZERO;
	    for (OrderItemDTO item : order.getOrderItems()) {
	        BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
	        totalAmount = totalAmount.add(itemTotal);
	    }
	    return totalAmount;
	}




	
	
}
