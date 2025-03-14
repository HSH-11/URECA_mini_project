package service;

import java.math.BigDecimal;
import java.util.List;

import entity.Customer;
import entity.Order;
import entity.Product;
import repository.OrderRepository;

public class OrderService {
	private final OrderRepository orderRepository = new OrderRepository();

	// 모든 주문 조회
	public List<Order> getAllOrders() {
		return orderRepository.getAllOrders();
	}

	// 고객별 주문 조회
	public List<Order> getOrdersByCustomer(int customerId) {
		return orderRepository.getOrdersByCustomer(customerId);
	}

	// 주문 생성
	public Order createOrder(Customer customer, Product product, int quantity, BigDecimal unitPrice,
			BigDecimal totalAmount, BigDecimal discountAmount, String shippingAddress) {
		Order order = new Order(customer, product, quantity, unitPrice, totalAmount, discountAmount, shippingAddress);
		orderRepository.insertOrder(order);
		return order;
	}

	// 주문 삭제
	public void deleteOrder(int orderId) {
		orderRepository.deleteOrder(orderId);
	}
}
