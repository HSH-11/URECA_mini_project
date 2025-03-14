package mybatis.dao;

import mybatis.dto.ProductDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductDAO {

    // 모든 상품 조회
	@Select("SELECT product_id AS productId, name, price, stock_quantity AS stockQuantity FROM Products")
    List<ProductDTO> getAllProducts();
	
	// 제품명으로 검색
	@Select("SELECT product_id AS productId, name, price, stock_quantity AS stockQuantity FROM Products WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<ProductDTO> getProductsByName(String name);
	
	// 특정 상품 조회
    @Select("SELECT product_id AS productId, name, price, stock_quantity AS stockQuantity FROM Products WHERE product_id = #{productId}")
    ProductDTO getProductById(int productId);

    // 상품 추가
    @Insert("INSERT INTO Products (name, price, stock_quantity) VALUES (#{name}, #{price}, #{stockQuantity})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int insertProduct(ProductDTO product);

    // 상품 수정
    @Update("UPDATE Products SET name = #{name}, price = #{price}, stock_quantity = #{stockQuantity} WHERE product_id = #{productId}")
    int updateProduct(ProductDTO product);

    // 상품 삭제
    @Delete("DELETE FROM Products WHERE product_id = #{productId}")
    int deleteProduct(int productId);
	
//    상품 재고 업데이트
    @Update("UPDATE Products SET stock_quantity = #{newStock} WHERE product_id = #{productId}")
    void updateStock(@Param("productId") int productId, @Param("newStock") int newStock);
}
