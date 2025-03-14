package mybatis.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer orderItemId;  // int → Integer 변경 (AUTO_INCREMENT 대응)
    private Integer orderId;      // int → Integer 변경 (주문이 생성된 후 할당됨)
    private int productId;        
    private String productName;   
    private int quantity;         
    private BigDecimal unitPrice; 
    private BigDecimal discountedPrice; 
    private BigDecimal totalPrice; 

    // 기본 생성자
    public OrderItemDTO() {}

    // 조회용 생성자 (orderItemId, orderId 포함)
    public OrderItemDTO(Integer orderItemId, Integer orderId, int productId, String productName, int quantity, BigDecimal unitPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountedPrice = null;
        this.totalPrice = calculateTotalPrice();
    }

    // 주문 아이템 생성용 생성자 (orderItemId 없이)
    public OrderItemDTO(Integer orderId, int productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal discountedPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountedPrice = discountedPrice;
        this.totalPrice = calculateTotalPrice();
    }

    // 가격 계산 메서드 (할인 가격이 있으면 할인 가격 적용)
    private BigDecimal calculateTotalPrice() {
        BigDecimal priceToUse = (discountedPrice != null) ? discountedPrice : unitPrice;
        if (priceToUse == null) {
            priceToUse = BigDecimal.ZERO;
        }
        return priceToUse.multiply(BigDecimal.valueOf(quantity));
    }

    // Getter & Setter
    public Integer getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Integer orderItemId) { this.orderItemId = orderItemId; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        this.totalPrice = calculateTotalPrice(); // 수량 변경 시 총 가격 재계산
    }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { 
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice(); // 단가 변경 시 총 가격 재계산
    }

    public BigDecimal getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(BigDecimal discountedPrice) { 
        if (discountedPrice != null && discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discounted price cannot be negative");
        }
        this.discountedPrice = discountedPrice;
        this.totalPrice = calculateTotalPrice(); // 할인 가격 변경 시 총 가격 재계산
    }

    public BigDecimal getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return "OrderItemDTO [orderItemId=" + orderItemId + ", orderId=" + orderId + ", productId=" + productId
                + ", productName=" + productName + ", quantity=" + quantity + ", unitPrice=" + unitPrice 
                + ", discountedPrice=" + discountedPrice + ", totalPrice=" + totalPrice + "]";
    }
}
