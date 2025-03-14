package service;

import entity.Product;
import repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository = new ProductRepository();

    // 모든 상품 조회
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts(); 
    }

    // 특정 이름 포함된 상품 조회
    public List<Product> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름을 입력하세요.");
        }
        return productRepository.getProductsByName(name);
    }

    // 상품 추가
    public void insertProduct(String name, double price, int stockQuantity) {
        if (name == null || name.trim().isEmpty() || price <= 0 || stockQuantity < 0) {
            throw new IllegalArgumentException("올바른 상품 정보를 입력하세요.");
        }
        Product product = new Product(name, price, stockQuantity);
        productRepository.insertProduct(product);
    }

    // 상품 수정
    public void updateProduct(int productId, String name, double price, int stockQuantity) {
        Product product = productRepository.getProductsByName(name).stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다."));
        
        product.setName(name);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        productRepository.updateProduct(product);
    }

    // 재고 업데이트
    public void updateStock(int productId, int newStock) {
        if (newStock < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
        }
        productRepository.updateStock(productId, newStock);
    }
}
