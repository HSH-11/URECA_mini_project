package service;

import java.util.List;

import entity.Customer;
import repository.CustomerRepository;

public class CustomerService {
	
	private final CustomerRepository customerRepository = new CustomerRepository();
	
	public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }
	
    // 고객 찾기 (없으면 새로 생성)
    public Customer findOrCreateCustomer(String name, String email, String phone, String address) {
        if (name == null || name.isBlank() || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("고객 이름과 전화번호는 필수 입력 사항입니다.");
        }
        return customerRepository.findOrCreateCustomer(name, email, phone, address);
    }

    // 고객 정보 업데이트
    public boolean updateCustomer(Customer customer) {
        if (customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException("유효한 고객 ID가 필요합니다.");
        }
        return customerRepository.updateCustomer(customer);
    }

    // 고객 삭제
    public boolean deleteCustomer(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("유효한 고객 ID가 필요합니다.");
        }
        return customerRepository.deleteCustomer(customerId);
    }

    // 특정 고객 조회 (이름 & 전화번호)
    public Customer getCustomerByNameAndPhone(String name, String phone) {
        return customerRepository.findCustomerByNameAndPhone(name, phone);
    }
}
