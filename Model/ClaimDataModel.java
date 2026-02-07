package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
	class นี้ใช้สำหรับจัดการข้อมูลการเคลม
	- อ่านข้อมูลผู้ขอเคลมจากไฟล์ CSV
	- อ่านข้อมูลการเคลมจากไฟล์ CSV
	- บันทึกผลลัพธ์การคำนวณค่าชดเชยลงในไฟล์ CSV
	- อัปเดตสถานะคำขอในไฟล์ CSV
	- ตรวจสอบว่า username ถูกใช้แล้วหรือไม่ สำหรับการลงทะเบียน
	- บันทึกข้อมูลผู้ขอเคลมใหม่ลงในไฟล์ CSV
*/
public class ClaimDataModel {

	/*
	 * READ DATA METHODS
	 */
	// อ่านข้อมูลผู้ขอเคลมจากไฟล์ CSV
	public List<ClaimModel> readClaimants(String filePath) {
		List<ClaimModel> claimants = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			br.readLine(); // ข้ามหัวข้อคอลัมน์
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				int claimantId = Integer.parseInt(parts[0]);
				String firstName = parts[1];
				String lastName = parts[2];
				double income = Double.parseDouble(parts[3]);
				String claimantType = parts[4];
				String username = parts[5];

				// สร้างวัตถุ ClaimModel ตามประเภทผู้ขอและรายได้
				ClaimModel claim = null;
				if (income < 6500 && claimantType.equals("LOW")) {
					claim = new LowIncomeClaim(claimantId, firstName, lastName, income, claimantType, username);
				} else if (income >= 6500 && income < 50000 && claimantType.equals("NORMAL")) {
					claim = new NormalClaim(claimantId, firstName, lastName, income, claimantType, username);
				} else if (income >= 50000 && claimantType.equals("HIGH")) {
					claim = new HighIncomeClaim(claimantId, firstName, lastName, income, claimantType, username);
				} else { // ข้อมูลไม่ถูกต้อง
					System.err.println(
							"Invalid data for claimantId=" + claimantId +
									" type=" + claimantType +
									" income=" + income);
					continue;
				}
				claimants.add(claim);
			}
		} catch (Exception e) {
			System.err.println("Error reading claimants: " + e.getMessage());
		}
		return claimants;
	}

	// อ่านข้อมูลการเคลมจากไฟล์ CSV
	public List<ClaimRecord> readClaimRecords(String filePath) {
		List<ClaimRecord> claimRecords = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			br.readLine(); // ข้ามหัวข้อคอลัมน์
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				String claimId = parts[0];
				int claimantId = Integer.parseInt(parts[1]);
				LocalDate submitDate = LocalDate.parse(parts[2]);
				String status = parts[3];

				// สร้าง ClaimRecord
				ClaimRecord record = new ClaimRecord(claimId, claimantId, submitDate, status);
				claimRecords.add(record);
			}
		} catch (Exception e) {
			System.err.println("Error reading claim records: " + e.getMessage());
		}
		return claimRecords;
	}

	/*
	 * WRITE DATA METHODS
	 */
	// บันทึกผลลัพธ์การคำนวณค่าชดเชยลงในไฟล์ CSV
	public void saveCompensationResults(String filePath, String claimId, double amount) {
		try (FileWriter writer = new FileWriter(filePath, true)) {
			writer.write(claimId + "," + amount + "," + LocalDate.now() + "\n");
		} catch (Exception e) {
			System.err.println("Error saving compensation results: " + e.getMessage());
		}
	}

	// บันทึกข้อมูลผู้ขอเคลมใหม่ลงในไฟล์ CSV
	public void saveNewClaimant(ClaimModel newClaimant) {
		try (FileWriter writer = new FileWriter("data/Claimants.csv", true)) {
			writer.write(
					newClaimant.getClaimantId() + "," +
							newClaimant.getFirstName() + "," +
							newClaimant.getLastName() + "," +
							newClaimant.getIncome() + "," +
							newClaimant.getClaimantType() + "," +
							newClaimant.getUsername() + "\n");
		} catch (Exception e) {
			System.err.println("Error saving new claimant: " + e.getMessage());
		}
	}

	// เพิ่มเมธอดสำหรับบันทึกคำขอใหม่
	public void saveClaimRecord(ClaimRecord record) {
		try (FileWriter writer = new FileWriter("data/claims.csv", true)) {
			File file = new File("data/claims.csv");
			if (file.length() == 0) {
				writer.write("claimId,claimantId,submitDate,status\n");
			}
			writer.write(
					record.getClaimId() + "," +
							record.getClaimantId() + "," +
							record.getSubmitDate() + "," +
							record.getStatus() + "\n");
		} catch (Exception e) {
			System.err.println("Error saving claim record: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// เพิ่มเมธอดสำหรับบันทึกผลการชดเชย
	public void saveCompensationResult(String claimId, double amount) {
		try (FileWriter writer = new FileWriter("data/compensations.csv", true)) {
			File file = new File("data/compensations.csv");
			if (file.length() == 0) {
				writer.write("claimId,amount,date\n");
			}
			writer.write(claimId + "," + amount + "," + LocalDate.now() + "\n");
		} catch (Exception e) {
			System.err.println("Error saving compensation: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// เมธอดสำหรับเขียนข้อมูลผู้ขอทั้งหมด
	private void writeAllClaimants(List<ClaimModel> claimants, String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write("claimantId,firstName,lastName,income,claimantType,username\n");
			for (ClaimModel claimant : claimants) {
				writer.write(
						claimant.getClaimantId() + "," +
								claimant.getFirstName() + "," +
								claimant.getLastName() + "," +
								claimant.getIncome() + "," +
								claimant.getClaimantType() + "," +
								claimant.getUsername() + "\n");
			}
		} catch (Exception e) {
			System.err.println("Error writing claimants: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// อัปเดตข้อมูลผู้ขอในไฟล์ CSV
	public void updateClaimant(ClaimModel updatedClaim) {
		List<ClaimModel> claimants = readClaimants("data/Claimants.csv");
		// หาและอัปเดตข้อมูลผู้ขอ
		for (int i = 0; i < claimants.size(); i++) {
			if (claimants.get(i).getClaimantId() == updatedClaim.getClaimantId()) {
				claimants.set(i, updatedClaim);
				break;
			}
		}
		// เขียนข้อมูลทั้งหมดกลับไปยังไฟล์
		writeAllClaimants(claimants, "data/Claimants.csv");
	}

	/*
		CHECK METHODS
	*/
	// ตรวจสอบว่า username ถูกใช้แล้วหรือไม่ สำหรับการลงทะเบียน
	public boolean isUsernameTaken(String username) {
		List<ClaimModel> claimants = readClaimants("data/Claimants.csv");
		for (ClaimModel claim : claimants) {
			if (claim.getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	// ดึงข้อมูลผู้ขอเคลมตาม username
	public ClaimModel getClaimByUsername(String username) {
		List<ClaimModel> claimants = readClaimants("data/Claimants.csv");
		for (ClaimModel claim : claimants) {
			if (claim.getUsername().equalsIgnoreCase(username)) {
				return claim;
			}
		}
		return null;
	}

	// สร้าง ClaimModel ตามประเภทผู้ขอเคลม
    public ClaimModel createClaimModel(int claimantId, String firstName, String lastName, 
                                       double income, String type, String username) {
        switch (type) {
            case "LOW":
                return new LowIncomeClaim(claimantId, firstName, lastName, income, type, username);
            case "NORMAL":
                return new NormalClaim(claimantId, firstName, lastName, income, type, username);
            case "HIGH":
                return new HighIncomeClaim(claimantId, firstName, lastName, income, type, username);
            default:
                return new NormalClaim(claimantId, firstName, lastName, income, type, username);
        }
    }
    

}
