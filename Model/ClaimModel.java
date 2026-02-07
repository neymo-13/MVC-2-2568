package Model;
//คำขอทั่วไป
/*
	class นี้เป็นฐานสำหรับคำขอเคลมต่างๆ
	- มีข้อมูลพื้นฐานของผู้ขอเคลม เช่น รหัสผู้ขอ ชื่อ รายได้ และประเภทผู้ขออ
	- มี method นามธรรมสำหรับคำนวณค่าชดเชยที่ต้องให้แต่ละประเภทของคำขอจะต้องกำหนดเอง
*/
public abstract class ClaimModel {
	protected int claimantId;
	protected String firstName;
	protected String lastName;
	protected double income = 0; // รายได้
	protected String claimantType = "NULL"; // ประเภทผู้ขอ
	protected double compensation; // ค่าชดเชย
	protected String username;

	public ClaimModel(int claimantId, String firstName, String lastName, double income, String claimantType, String username) {
		this.claimantId = claimantId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.income = income;
		this.claimantType = claimantType;
		this.username = username;
		this.compensation = calculateCompensation();
	}
	// คำนวณค่าชดเชย
	public abstract double calculateCompensation();
	
	public int getClaimantId() {
		return claimantId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public double getIncome() {
		return income;
	}
	
	public String getClaimantType() {
		return claimantType;
	}

	public double getCompensation() {
		return compensation;
	}

	public String getUsername() {
		return username;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public void setClaimantType(String claimantType) {
		this.claimantType = claimantType;
	}
}
