package app.phone.dao;

import app.phone.common.DBManager;
import app.phone.dto.ProductDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
	
	// 모든 상품 목록 조회
	public List<ProductDTO> getAllProducts() {
		List<ProductDTO> productList = new ArrayList<>();
		String query = "SELECT * FROM Products";

		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				int productId = rs.getInt("product_id");
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				int stockQuantity = rs.getInt("stock_quantity");
				

				// ProductDTO 객체 생성 후 리스트에 추가
				ProductDTO product = new ProductDTO(productId, name, price, stockQuantity);
				productList.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productList;
	}

	// 특정 상품 조회 (상품 ID로)
	public List<ProductDTO> getProductsByName(String name) {
		List<ProductDTO> productList = new ArrayList<>();
		String query = "SELECT * FROM Products WHERE name LIKE ?";

		try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setString(1, "%" + name + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int productId = rs.getInt("product_id");
					String productName = rs.getString("name");
					double price = rs.getDouble("price");
					int stockQuantity = rs.getInt("stock_quantity");
		

					ProductDTO product = new ProductDTO(productId, productName, price, stockQuantity);
					productList.add(product);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productList;
	}

	public void insertProduct(ProductDTO product) {
		String query = "INSERT INTO Products (name, price, stock_quantity) VALUES (?, ?, ?)";

		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, product.getName());
			pstmt.setDouble(2, product.getPrice());
			pstmt.setInt(3, product.getStockQuantity());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				// 새로 추가된 상품의 ID를 반환받기 위해 자동 생성된 키를 가져온다.
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						product.setProductId(generatedKeys.getInt(1)); // 새로 생성된 상품 ID
					}
				}
			} else {
				System.out.println("상품 추가 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 상품 수정 메서드
	public void updateProduct(ProductDTO product) {
		String query = "UPDATE Products SET name = ?, price = ?, stock_quantity = ? WHERE product_id = ?";

		try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setString(1, product.getName());
			pstmt.setDouble(2, product.getPrice());
			pstmt.setInt(3, product.getStockQuantity());
			pstmt.setInt(4, product.getProductId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 상품 삭제 메서드 (외래키 제약 때문에 실패하는 게 맞음)
	public boolean deleteProduct(int productId) {
		String query = "DELETE FROM Products WHERE product_id = ?";

		try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setInt(1, productId);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("상품 삭제 성공");
				return true; 
			} else {
				System.out.println("삭제할 상품이 존재하지 않거나 실패");
				return false; 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false; 
		}
	}
	
	// 재고 업데이트
    public void updateStock(int productId, int newStock) {
        String query = "UPDATE Products SET stock_quantity = ? WHERE product_id = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
