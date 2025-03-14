package mybatis.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
	private Integer orderId;          
    private int customerId;       
    private String orderStatus;   
    private String paymentStatus; 
    private String shippingAddress; 
    private List<OrderItemDTO> orderItems = new ArrayList<>();  // 주문 항목 리스트
    private BigDecimal discountAmount = BigDecimal.ZERO; 
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    public OrderDTO() {}

    // 조회용
    public OrderDTO(Integer orderId, int customerId, String orderStatus, String paymentStatus, String shippingAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.shippingAddress = shippingAddress;
    }
 // 주문 생성용 생성자 (orderId 없이)
    public OrderDTO(int customerId, String orderStatus, String paymentStatus, String shippingAddress) {
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.shippingAddress = shippingAddress;
    }

    // 주문 항목 추가 메서드
    public void addOrderItem(OrderItemDTO orderItem) {
        this.orderItems.add(orderItem);
    }

    // Getter 및 Setter 메서드
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

   
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    @Override
    public String toString() {
        return "OrderDTO [orderId=" + orderId + ", customerId=" + customerId
                + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus + ", shippingAddress="
                + shippingAddress + ", orderItems=" + orderItems + "]";
    }
}
