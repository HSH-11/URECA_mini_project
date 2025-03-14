package repository;

import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import config.MyPersistenceUnitInfo;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class ProductRepository {
	private static final EntityManagerFactory emf = new HibernatePersistenceProvider()
			.createContainerEntityManagerFactory(new MyPersistenceUnitInfo(), new HashMap<>());

	public List<Product> getAllProducts() {
		EntityManager em = emf.createEntityManager();
		String jpql = "SELECT p FROM Product p";
		TypedQuery<Product> query = em.createQuery(jpql, Product.class);
		List<Product> products = query.getResultList();
		em.close();
		return products;
	}

	public List<Product> getProductsByName(String name) {
		EntityManager em = emf.createEntityManager();

		try {
			String jpql = "SELECT p FROM Product p WHERE p.name LIKE :name";
			TypedQuery<Product> query = em.createQuery(jpql, Product.class);
			query.setParameter("name", "%" + name + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public void insertProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            em.persist(product);
            tx.commit(); 
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
	
	public void updateProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            em.merge(product); 
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
	
	public void updateStock(int productId, int newStock) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Product product = em.find(Product.class, productId);
            if (product != null) {
                product.setStockQuantity(newStock);
                em.merge(product);
                tx.commit();
            } else {
                System.out.println("⚠️ 해당 상품이 존재하지 않습니다.");
                tx.rollback();
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
	
}
