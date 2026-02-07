package Controller;

import View.ClaimListView;
import Model.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/*
	คลาสนี้ใช้สำหรับควบคุมการแสดงรายการคำขอเคลม
	- รองรับโหมดผู้ใช้ทั่วไปและโหมดผู้ดูแลระบบ
	- โหลดข้อมูลคำขอจาก Model และแสดงผลใน View
	- มี callback สำหรับจัดการเหตุการณ์เมื่อผู้ใช้กดปุ่มกลับ
	- มีการรีเฟรชข้อมูลคำขอเมื่อผู้ใช้กดปุ่มรีเฟรช
*/
public class ClaimListController {
    
    private ClaimListView view;
    private ClaimDataModel dataModel;
    private ClaimModel currentUser;
    private boolean isAdminMode;
    
	// สำหรับ callback เมื่อผู้ใช้กดปุ่มกลับ
    public interface ClaimListCallback {
        void onBack();
    }
    private ClaimListCallback callback;
    
	// คอนสตรัคเตอร์สำหรับโหมดผู้ใช้ทั่วไป
    public ClaimListController(ClaimListView view, ClaimDataModel dataModel, ClaimModel user) {
        this.view = view;
        this.dataModel = dataModel;
        this.currentUser = user;
        this.isAdminMode = false;
        
        view.setUserMode();
        loadUserClaimData();
        setupEventListeners();
    }
    
	// คอนสตรัคเตอร์สำหรับโหมดผู้ดูแลระบบ
    public ClaimListController(ClaimListView view, ClaimDataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;
        this.isAdminMode = true;
        
        view.setAdminMode();
        loadAllClaimData();
        setupEventListeners();
    }
    
	// ตั้งค่าการฟังเหตุการณ์ของปุ่มต่างๆ
    private void setupEventListeners() {
        view.addBackListener(e -> handleBack());
        view.addRefreshListener(e -> handleRefresh());
    }
    
	// จัดการเมื่อผู้ใช้กดปุ่มกลับ
    private void handleBack() {
        if (callback != null) {
            callback.onBack();
        }
    }
    
	// จัดการเมื่อผู้ใช้กดปุ่มรีเฟรช
    private void handleRefresh() {
        if (isAdminMode) {
            loadAllClaimData();
        } else {
            loadUserClaimData();
        }
        view.showMessage("ข้อมูลถูกรีเฟรชแล้ว");
    }
    
	// โหลดข้อมูลคำขอของผู้ใช้ปัจจุบัน
    private void loadUserClaimData() {
        try {
            List<ClaimRecord> claimRecords = dataModel.readClaimRecords("data/claims.csv");
            Map<String, Double> compensationMap = readCompensations();
            
            java.util.List<Object[]> tableData = new java.util.ArrayList<>();
            
            for (ClaimRecord record : claimRecords) {
                if (record.getClaimantId() == currentUser.getClaimantId()) {
                    double amount = compensationMap.getOrDefault(record.getClaimId(), 0.0);
                    String type = getClaimantTypeForClaim(record);
                    String typeName = getTypeName(type);
                    
                    tableData.add(new Object[]{
                        record.getClaimId(),
                        record.getSubmitDate().toString(),
                        record.getStatus(),
                        String.format("%,.2f", amount),
                        typeName
                    });
                }
            }
            
            if (tableData.isEmpty()) {
                view.showNoDataMessage();
            } else {
                view.setTableData(tableData.toArray(new Object[0][0]));
            }
            
        } catch (Exception e) {
            view.showNoDataMessage();
        }
    }
    
	// โหลดข้อมูลคำขอทั้งหมด (สำหรับ Admin)
    private void loadAllClaimData() {
        try {
            List<ClaimRecord> claimRecords = dataModel.readClaimRecords("data/claims.csv");
            Map<String, Double> compensationMap = readCompensations();
            Map<Integer, ClaimModel> claimantMap = new HashMap<>();
            
            List<ClaimModel> claimants = dataModel.readClaimants("data/Claimants.csv");
            for (ClaimModel claimant : claimants) {
                claimantMap.put(claimant.getClaimantId(), claimant);
            }
            
            java.util.List<Object[]> tableData = new java.util.ArrayList<>();
            
            for (ClaimRecord record : claimRecords) {
                ClaimModel claimant = claimantMap.get(record.getClaimantId());
                
                if (claimant != null) {
                    double amount = compensationMap.getOrDefault(record.getClaimId(), 0.0);
                    String fullName = claimant.getFirstName() + " " + claimant.getLastName();
                    String typeName = getTypeName(claimant.getClaimantType());
                    
                    tableData.add(new Object[]{
                        record.getClaimId(),
                        claimant.getClaimantId(),
                        fullName,
                        typeName,
                        record.getSubmitDate().toString(),
                        record.getStatus(),
                        String.format("%,.2f", amount)
                    });
                }
            }
            
            if (tableData.isEmpty()) {
                view.showNoDataMessage();
            } else {
                view.setTableData(tableData.toArray(new Object[0][0]));
            }
            
        } catch (Exception e) {
            view.showNoDataMessage();
        }
    }
    
	// ดึงประเภทผู้ขอจาก ClaimRecord
    private String getClaimantTypeForClaim(ClaimRecord record) {
        List<ClaimModel> claimants = dataModel.readClaimants("data/Claimants.csv");
        for (ClaimModel claimant : claimants) {
            if (claimant.getClaimantId() == record.getClaimantId()) {
                return claimant.getClaimantType();
            }
        }
        return "N/A";
    }
    
	// อ่านผลลัพธ์การคำนวณค่าชดเชยจากไฟล์ CSV
    private Map<String, Double> readCompensations() {
        Map<String, Double> map = new HashMap<>();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("data/compensations.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String claimId = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    map.put(claimId, amount);
                }
            }
        } catch (Exception e) {
            // File might not exist yet
        }
        return map;
    }
    
	// แปลงรหัสประเภทผู้ขอเป็นชื่อประเภท
    private String getTypeName(String type) {
        switch (type) {
            case "LOW": return "รายได้น้อย";
            case "NORMAL": return "รายได้ปานกลาง";
            case "HIGH": return "รายได้สูง";
            default: return type;
        }
    }
    
	// ตั้งค่า callback
    public void setCallback(ClaimListCallback callback) {
        this.callback = callback;
    }
}