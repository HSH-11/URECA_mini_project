package app.phone.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private int orderId;          
    private int customerId;       
    private String orderDate;     
    private String orderStatus;   
    private String paymentStatus; 
    private String shippingAddress; 
    private List<OrderItemDTO> orderItems = new ArrayList<>();  // 주문 항목 리스트

   
    public OrderDTO() {}

   
    public OrderDTO(int orderId, int customerId, String orderDate, String orderStatus, String paymentStatus, String shippingAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
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

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    @Override
    public String toString() {
        return "OrderDTO [orderId=" + orderId + ", customerId=" + customerId + ", orderDate=" + orderDate
                + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus + ", shippingAddress="
                + shippingAddress + ", orderItems=" + orderItems + "]";
    }
}
