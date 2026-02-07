package Model;
/*
	class นี้เป็นตัวแทนของผู้ขอเคลมปกติ
	- คำนวณค่าชดเชยจาก รายได้แต่ไม่เกิน 20000
*/
public class NormalClaim extends ClaimModel {

	public NormalClaim(int claimantId, String firstName, String lastName, double income, String claimantType, String username) {
		super(claimantId, firstName, lastName, income, claimantType, username);
	}

	@Override
	public double calculateCompensation() {
		// ได้ตามรายได้ แต่ไม่เกิน 20000
		return Math.min(income, 20000);
	}
	
}
