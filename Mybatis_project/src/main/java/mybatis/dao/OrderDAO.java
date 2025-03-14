package mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import mybatis.dto.OrderDTO;
import mybatis.dto.OrderItemDTO;

@Mapper
public interface OrderDAO {
	
	// 고객 ID로 주문 목록 조회
	@Select("SELECT o.order_id, o.customer_id, o.order_status, o.payment_status, o.shipping_address, "
            + "o.total_amount, o.discount_amount "
            + "FROM Orders o WHERE o.customer_id = #{customerId}")
    @Results({
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "orderStatus", column = "order_status"),
        @Result(property = "paymentStatus", column = "payment_status"),
        @Result(property = "shippingAddress", column = "shipping_address"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "discountAmount", column = "discount_amount"),
        @Result(property = "orderItems", column = "order_id", javaType = List.class, 
                many = @Many(select = "mybatis.dao.OrderDAO.getOrderItems"))
    })
    List<OrderDTO> getOrdersByCustomerId(int customerId);
	
	
	// 특정 주문의 상품 목록 조회 (위 `getOrdersByCustomerId`에서 사용됨)
    @Select("SELECT order_item_id, order_id, product_id, quantity, unit_price "
            + "FROM OrderItems WHERE order_id = #{orderId}")
    @Results({
        @Result(property = "orderItemId", column = "order_item_id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "productId", column = "product_id"),
        @Result(property = "quantity", column = "quantity"),
        @Result(property = "unitPrice", column = "unit_price")
    })
    List<OrderItemDTO> getOrderItems(int orderId);
    
 // 주문 생성 (Order 테이블)
    @Insert("INSERT INTO Orders (customer_id, order_status, total_amount, discount_amount, shipping_address) "
            + "VALUES (#{customerId}, #{orderStatus}, #{totalAmount}, #{discountAmount}, #{shippingAddress})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId") // MyBatis가 자동 생성된 ID 저장
    int createOrder(OrderDTO order);

    // 주문 아이템 추가 (AUTO_INCREMENT 적용)
    @Insert("INSERT INTO OrderItems (order_id, product_id, quantity, unit_price) "
            + "VALUES (#{orderId}, #{productId}, #{quantity}, #{unitPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "orderItemId")  // 자동 생성된 order_item_id 저장
    void addOrderItem(OrderItemDTO orderItem);

    // 주문 삭제 (주문 항목 삭제 후 주문 삭제)
    @Delete("DELETE FROM OrderItems WHERE order_id = #{orderId}")
    void deleteOrderItems(int orderId);

    @Delete("DELETE FROM Orders WHERE order_id = #{orderId}")
    int deleteOrder(int orderId);

    // 주문 취소 시 재고 복원
    @Update("UPDATE Products SET stock_quantity = stock_quantity + #{quantity} WHERE product_id = #{productId}")
    void restoreStock(@Param("productId") int productId, @Param("quantity") int quantity);
}
