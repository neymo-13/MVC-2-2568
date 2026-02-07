package Model;
/*
	class นี้เป็นตัวแทนของผู้ขอเคลมที่มีรายได้ต่ำ
	- คำนวณค่าชดเชยเป็นจำนวนคงที่ 6500 บาท
*/
public class LowIncomeClaim extends ClaimModel {

	public LowIncomeClaim(int claimantId, String firstName, String lastName, double income, String claimantType, String username) {
		super(claimantId, firstName, lastName, income, claimantType, username);
	}

	@Override
	public double calculateCompensation() {
		return 6500.0;
	}
	
}
