package Model;
/*
	class นี้เป็นตัวแทนของผู้ขอเคลมที่มีรายได้สูง
	- คำนวณค่าชดเชยจาก รายได้หาร 5 แต่ไม่เกิน 20000
*/
public class HighIncomeClaim extends ClaimModel {

	public HighIncomeClaim(int claimantId, String firstName, String lastName, double income, String claimantType, String username) {
		super(claimantId, firstName, lastName, income, claimantType, username);
	}

	@Override
	public double calculateCompensation() {
		// รายได้หาร 5 แต่ไม่เกิน 20000
		return Math.min(income / 5, 20000);
	}
	
}
