package repository;

import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import config.MyPersistenceUnitInfo;
import entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class OrderRepository {
	private static final EntityManagerFactory emf = new HibernatePersistenceProvider()
			.createContainerEntityManagerFactory(new MyPersistenceUnitInfo(), new HashMap<>());

	public List<Order> getAllOrders() {
		EntityManager em = emf.createEntityManager();
		String jpql = "SELECT o FROM Order o";
		TypedQuery<Order> query = em.createQuery(jpql, Order.class);
		List<Order> orders = query.getResultList();
		em.close();
		return orders;
	}

	public List<Order> getOrdersByCustomer(int customerId) {
		EntityManager em = emf.createEntityManager();
		String jpql = "SELECT o FROM Order o WHERE o.customer.customerId = :customerId";
		TypedQuery<Order> query = em.createQuery(jpql, Order.class);
		query.setParameter("customerId", customerId);
		List<Order> orders = query.getResultList();
		em.close();
		return orders;
	}

	public Order findOrderById(int orderId) {
		EntityManager em = emf.createEntityManager();
		Order order = em.find(Order.class, orderId);
		em.close();
		return order;
	}

	public void insertOrder(Order order) {
	    EntityManager em = emf.createEntityManager();
	    EntityTransaction tx = em.getTransaction();
	    try {
	        tx.begin();
	        em.persist(order);
	        tx.commit();
	    } catch (Exception e) {
	        if (tx.isActive()) tx.rollback();
	        throw e;
	    } finally {
	        em.close();
	    }
	}

	public void updateOrder(Order order) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.merge(order);
		tx.commit();
		em.close();
	}

	public void deleteOrder(int orderId) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Order order = em.find(Order.class, orderId);
		if (order != null) {
			em.remove(order);
		}
		tx.commit();
		em.close();
	}
}
