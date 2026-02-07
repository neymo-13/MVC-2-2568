package Model;

import java.time.LocalDate;
/*
	class นี้ใช้สำหรับเก็บข้อมูลการเคลม
	- มีข้อมูลรหัสคำขอ รหัสผู้ขอ วันที่ส่งคำขอ และสถานะคำขอ
*/
public class ClaimRecord {
	private String claimId;
    private int claimantId;
    private LocalDate submitDate;
    private String status;
	
	public ClaimRecord(String claimId, int claimantId, LocalDate submitDate, String status) {
		this.claimId = claimId;
		this.claimantId = claimantId;
		this.submitDate = submitDate;
		this.status = status;
	}	
	
	public String getClaimId() {
		return claimId;
	}

	public int getClaimantId() {
		return claimantId;
	}
	
	public LocalDate getSubmitDate() {
		return submitDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}