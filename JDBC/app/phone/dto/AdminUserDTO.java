package app.phone.dto;

public class AdminUserDTO {
	private String adminId;
	private String password;
	private String name;
	private String role;

	public AdminUserDTO() {
	}

	public AdminUserDTO(String adminId, String password, String name) {
		super();
		this.adminId = adminId;
		this.password = password;
		this.name = name;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
