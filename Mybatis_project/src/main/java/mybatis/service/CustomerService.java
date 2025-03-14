package mybatis.service;

import org.apache.ibatis.session.SqlSession;

import mybatis.common.MyBatisConfig;
import mybatis.dao.CustomerDAO;
import mybatis.dto.CustomerDTO;

public class CustomerService {
	public CustomerDTO findOrCreateCustomer(String name, String email, String phone, String address) {
        try (SqlSession session = MyBatisConfig.getSession()) {
            CustomerDAO customerDAO = session.getMapper(CustomerDAO.class);

            // 기존 고객 찾기
            CustomerDTO customer = customerDAO.findCustomerByNameAndPhone(name, phone);
            if (customer == null) {
                // 고객이 없으면 새로 추가
                customer = new CustomerDTO(name, email, phone, address);
                customerDAO.createCustomer(customer);
                session.commit();  // 데이터 저장 후 커밋
            }
            return customer;
        }
    }
}
