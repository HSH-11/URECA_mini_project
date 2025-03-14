package mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import mybatis.dto.CustomerDTO;

@Mapper
public interface CustomerDAO {

    // 모든 고객 조회
    @Select("SELECT * FROM Customers")
    @Results({
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "name", column = "name"),
        @Result(property = "email", column = "email"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "address", column = "address")
    })
    List<CustomerDTO> getAllCustomers();

    // 고객 삭제 (주문 재고 복원 후 삭제)
    @Delete("DELETE FROM Customers WHERE customer_id = #{customerId}")
    int deleteCustomer(int customerId);

    // 고객 정보 업데이트
    @Update("UPDATE Customers SET name = #{name}, phone = #{phone}, address = #{address} WHERE customer_id = #{customerId}")
    int updateCustomer(CustomerDTO customer);

    // 고객 정보 조회 (이름 & 전화번호 기반)
    @Select("SELECT * FROM Customers WHERE name = #{name} AND phone = #{phone}")
    @Results({
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "name", column = "name"),
        @Result(property = "email", column = "email"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "address", column = "address")
    })
    CustomerDTO findCustomerByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

    // 고객 정보가 없으면 생성 (AUTO_INCREMENT 적용)
    @Insert("INSERT INTO Customers (name, email, phone, address) VALUES (#{name}, #{email}, #{phone}, #{address})")
    @Options(useGeneratedKeys = true, keyProperty = "customerId")  // 자동 생성된 customer_id 저장
    int createCustomer(CustomerDTO customer);
}
