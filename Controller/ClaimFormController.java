package Controller;

import View.ClaimFormView;
import Model.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class ClaimFormController {
    
    private ClaimFormView view;
    private ClaimDataModel dataModel;
    private ClaimModel currentUser;
    
    // Callback interface
    public interface ClaimFormCallback {
        void onSubmitSuccess();
        void onViewClaims();
    }
    
    private ClaimFormCallback callback;
    
    public ClaimFormController(ClaimFormView view, ClaimDataModel dataModel, ClaimModel user) {
        this.view = view;
        this.dataModel = dataModel;
        this.currentUser = user;
        
        view.setUserData(
            user.getClaimantId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getIncome()
        );
        setupEventListeners();
    }
    
    private void setupEventListeners() {
        // Calculate button
        view.addCalculateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCalculate();
            }
        });
        
        // Submit button
        view.addSubmitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        
        // View Claims button
        view.addViewClaimsListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (callback != null) {
                    callback.onViewClaims();
                }
            }
        });
    }
    
	/*
		เมื่อผู้ใช้กดปุ่ม คำนวณ:
		1. ตรวจสอบความถูกต้องของข้อมูลเงินเดือนที่กรอก
		2. กำหนดประเภทผู้ขอตามเงินเดือน
		3. สร้าง ClaimModel ชั่วคราวเพื่อคำนวณค่าชดเชย
		4. คำนวณค่าชดเชย
		5. แสดงผลลัพธ์การคำนวณใน View
	*/
    private void handleCalculate() {
        if (!view.validateIncome()) {
            return;
        }
        
        try {
            double newIncome = Double.parseDouble(view.getIncomeText());
            String claimantType = determineClaimantType(newIncome);
            
            // ใช้เมธอดจาก Model ในการสร้าง ClaimModel
            ClaimModel tempClaim = dataModel.createClaimModel(
                currentUser.getClaimantId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                newIncome,
                claimantType,
                currentUser.getUsername()
            );
            double compensationAmount = tempClaim.calculateCompensation();
            view.setCalculationResults(claimantType, compensationAmount);
        } catch (NumberFormatException e) {
            view.showError("กรุณากรอกเงินเดือนเป็นตัวเลขที่ถูกต้อง");
        }
    }
    
	/*
		เมื่อผู้ใช้กดปุ่ม Submit:
		1. ตรวจสอบความถูกต้องของข้อมูลเงินเดือนที่กรอก
		2. กำหนดประเภทผู้ขอตามเงินเดือน
		3. สร้างรหัสคำขอแบบสุ่ม
		4. อัปเดตข้อมูลผู้ขอเคลมในไฟล์ Claimants.csv
		5. บันทึกข้อมูลคำขอใหม่ลงในไฟล์ claims.csv
		6. บันทึกผลลัพธ์การคำนวณค่าชดเชยลงในไฟล์ compensations.csv
		7. แสดงข้อความยืนยันการยื่นคำขอสำเร็จ
		8. ล้างผลลัพธ์การคำนวณและฟิลด์เงินเดือนในฟอร์ม
		9. แจ้งผลลัพธ์ผ่าน callback
	*/
    private void handleSubmit() {
        if (!view.validateIncome()) {
            return;
        }
        try {
            double newIncome = Double.parseDouble(view.getIncomeText());
            String claimantType = determineClaimantType(newIncome);
            String claimId = generateClaimId();

			ClaimModel updatedClaim = dataModel.createClaimModel(
                currentUser.getClaimantId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                newIncome,
                claimantType,
                currentUser.getUsername()
            );
			// อัปเดตข้อมูลผู้ขอผ่าน Model
            dataModel.updateClaimant(updatedClaim);
            currentUser = updatedClaim;
            
            // สร้างและบันทึก ClaimRecord ผ่าน Model
            ClaimRecord claimRecord = new ClaimRecord(
                claimId,
                currentUser.getClaimantId(),
                LocalDate.now(),
                "SUBMITTED"
            );
            dataModel.saveClaimRecord(claimRecord);
            
            // คำนวณและบันทึกผลการชดเชยผ่าน Model
            double compensationAmount = currentUser.calculateCompensation();
            dataModel.saveCompensationResult(claimId, compensationAmount);
		
			// แสดงข้อความยืนยัน
            view.showSuccess(claimId);
            view.clearResults();
            view.clearIncomeField();
            if (callback != null) {
                callback.onSubmitSuccess();
            }
        } catch (Exception e) {
            view.showError("เกิดข้อผิดพลาดในการบันทึกข้อมูล: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
	// กำหนดประเภทผู้ขอตามเงินเดือน
    private String determineClaimantType(double income) {
        if (income < 6500) {
            return "LOW";
        } else if (income >= 6500 && income < 50000) {
            return "NORMAL";
        } else {
            return "HIGH";
        }
    }
    
	// สร้าง ClaimId แบบสุ่ม โดยตรวจสอบว่าไม่ซ้ำกับในไฟล์ claims.csv
    private String generateClaimId() {
        int min = 10000000;
        int max = 99999999;
        
        String claimId;
        boolean isUnique;
        
        do {
            claimId = String.valueOf(min + (int)(Math.random() * (max - min + 1)));
            
            // ตรวจสอบว่ารหัสคำขอนี้ไม่ซ้ำกับในไฟล์ claims.csv
            List<ClaimRecord> existingClaims = dataModel.readClaimRecords("data/claims.csv");
            isUnique = true;
            for (ClaimRecord record : existingClaims) {
                if (record.getClaimId().equals(claimId)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return claimId;
    }
    
    // Set callback
    public void setCallback(ClaimFormCallback callback) {
        this.callback = callback;
    }
    
    // Get current user (updated)
    public ClaimModel getCurrentUser() {
        return currentUser;
    }
}