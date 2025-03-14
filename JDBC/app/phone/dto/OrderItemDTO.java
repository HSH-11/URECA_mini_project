package app.phone.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private int orderItemId;      
    private int orderId;          
    private int productId;        
    private String productName;   
    private int quantity;         
    private BigDecimal unitPrice; 
    private BigDecimal discountedPrice; 
    private BigDecimal totalPrice; 

    // 기본 생성자
    public OrderItemDTO() {}

    // 생성자: 할인 가격이 있을 수도 있고 없을 수도 있음
    public OrderItemDTO(int orderItemId, int orderId, int productId, String productName, int quantity, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = price;  // 할인 가격이 없을 경우 기본 가격 사용
        this.discountedPrice = null; 
        this.totalPrice = calculateTotalPrice();
    }

    // 할인 가격이 있는 경우의 생성자
    public OrderItemDTO(int orderItemId, int orderId, int productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal discountedPrice) {
        this(orderItemId, orderId, productId, productName, quantity, unitPrice);
        this.discountedPrice = discountedPrice;
        this.totalPrice = calculateTotalPrice();
    }

    // 가격 계산 메서드
    private BigDecimal calculateTotalPrice() {
        BigDecimal priceToUse = (discountedPrice != null) ? discountedPrice : unitPrice;

        // null 방지: unitPrice, discountedPrice가 없으면 기본값 0으로 설정
        if (priceToUse == null) {
            priceToUse = BigDecimal.ZERO;
        }
        return priceToUse.multiply(BigDecimal.valueOf(quantity));
    }

    // Getter 및 Setter
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice(); // 수량 변경 시 총 가격 재계산
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice(); // 단가 변경 시 총 가격 재계산
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        if (discountedPrice != null && discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discounted price cannot be negative");
        }
        this.discountedPrice = discountedPrice;
        this.totalPrice = calculateTotalPrice(); // 할인 가격 변경 시 총 가격 재계산
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    // toString() 메서드
    @Override
    public String toString() {
        return "OrderItemDTO [orderItemId=" + orderItemId + ", orderId=" + orderId + ", productId=" + productId
                + ", productName=" + productName + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", discountedPrice="
                + discountedPrice + ", totalPrice=" + totalPrice + "]";
    }
}
