package app.phone.dto;

public class CustomerDTO {
    private int customerId;  // 고객 ID
    private String name;     // 고객 이름
    private String email;    // 이메일
    private String phone;    // 전화번호
    private String address;  // 주소
    private String createdAt; // 가입 날짜

    // 생성자
    public CustomerDTO(int customerId, String name, String email, String phone, String address, String createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
    }

    // 기본 생성자
    public CustomerDTO() {
    }

    // Getter & Setter
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // toString() 메서드 (디버깅 및 로그 출력용)
    @Override
    public String toString() {
        return "CustomerDTO{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
