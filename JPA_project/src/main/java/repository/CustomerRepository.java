package repository;

import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import config.MyPersistenceUnitInfo;
import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class CustomerRepository {
	private static final EntityManagerFactory emf = new HibernatePersistenceProvider()
            .createContainerEntityManagerFactory(new MyPersistenceUnitInfo(), new HashMap<>());

	// 모든 고객 조회
	public List<Customer> getAllCustomers() {
		EntityManager em = emf.createEntityManager();
		String jpql = "SELECT c FROM Customer c";
		TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
		List<Customer> customers = query.getResultList();
		em.close();
		return customers;
	}
	
	// 고객 삭제
	public boolean deleteCustomer(int customerId) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Customer customer = em.find(Customer.class, customerId);
		if (customer != null) {
			em.remove(customer);
			tx.commit();
			em.close();
			return true;
		}

		tx.rollback();
		em.close();
		return false;
	}
	
	// 고객 정보 업데이트
    public boolean updateCustomer(Customer customer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(customer);
        tx.commit();
        em.close();
        return true;
    }
    
//    특정 고객 찾기 (이름 & 전화번호)
    public Customer findCustomerByNameAndPhone(String name, String phone) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT c FROM Customer c WHERE c.name = :name AND c.phone = :phone";
        TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
        query.setParameter("name", name);
        query.setParameter("phone", phone);

        List<Customer> resultList = query.getResultList();
        em.close();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    
//    고객을 찾거나 없으면 새로 생성
    public Customer findOrCreateCustomer(String name, String email, String phone, String address) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Customer customer = findCustomerByNameAndPhone(name, phone);
        if (customer == null) {
            customer = new Customer(name, email, phone, address);
            em.persist(customer); 
        }

        tx.commit();
        em.close();
        return customer;
    }
    
    public Customer findCustomerById(int customerId) {
        EntityManager em = emf.createEntityManager();
        Customer customer = em.find(Customer.class, customerId);
        em.close();
        return customer;
    }

}
