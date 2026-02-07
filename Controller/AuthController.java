package Controller;

import View.LoginView;
import View.RegisterDialog;
import Model.ClaimDataModel;
import Model.ClaimModel;
import Model.LowIncomeClaim;
import javax.swing.*;

/*
	คลาสนี้ใช้สำหรับควบคุมการทำงานของระบบการยืนยันตัวตน (Login/Registration)
	- จัดการการเข้าสู่ระบบของผู้ใช้ทั่วไปและผู้ดูแลระบบ
	- จัดการการลงทะเบียนผู้ใช้ใหม่
	- ใช้ callback เพื่อแจ้งผลลัพธ์ของการยืนยันตัวตน
	- มีการตรวจสอบความถูกต้องของข้อมูลที่กรอก
	- มีการจัดการกรณีที่ username ซ้ำกันในการลงทะเบียน
	- ล้างข้อมูลฟอร์มหลังการเข้าสู่ระบบหรือการลงทะเบียน
*/
public class AuthController {
    private LoginView loginView;
    private RegisterDialog registerDialog;
    private ClaimDataModel dataModel;
    private JFrame parentFrame;
    private AuthCallback authCallback;
    
	// จัดการ callback สำหรับผลลัพธ์การยืนยันตัวตน
    public interface AuthCallback {
        void onLoginSuccess(ClaimModel userClaim);
        void onAdminLogin();
        void onRegisterSuccess();
    }
    
    public AuthController(JFrame parentFrame, ClaimDataModel dataModel) {
        this.parentFrame = parentFrame;
        this.dataModel = dataModel;
        this.loginView = new LoginView();
        setupLoginListeners();
    }
    
	// ตั้งค่าการฟังเหตุการณ์ของปุ่มในหน้า Login
    private void setupLoginListeners() {
        loginView.addLoginListener(e -> handleLogin());
        loginView.addRegisListener(e -> showRegisterDialog());
    }
    
	/*
		จัดการการเข้าสู่ระบบ
		- ตรวจสอบความถูกต้องของข้อมูลที่กรอก
		- ตรวจสอบกรณีผู้ดูแลระบบ และผู้ใช้ทั่วไป
		- แจ้งผลลัพธ์ผ่าน callback
		- ล้างข้อมูลฟอร์มหลังการเข้าสู่ระบบ
	*/
    private void handleLogin() {
        String username = loginView.getUsername();
        if (username.isEmpty()) {
            loginView.showError("กรุณากรอก Username");
            return;
        }
        if (username.equalsIgnoreCase("admin")) {
            if (authCallback != null) {
                authCallback.onAdminLogin();
            }
            loginView.clearFields();
            return;
        }
        ClaimModel userClaim = dataModel.getClaimByUsername(username);
        if (userClaim == null) {
            loginView.showError("ไม่พบ Username นี้ในระบบ");
            return;
        }
        if (authCallback != null) {
            authCallback.onLoginSuccess(userClaim);
        }
        loginView.clearFields();
    }
    
	/*
		แสดงหน้าต่างการลงทะเบียน
		- ตั้งค่าการฟังเหตุการณ์ของปุ่มในหน้า Register
		- จัดการการลงทะเบียนผู้ใช้ใหม่
	*/
    private void showRegisterDialog() {
        registerDialog = new RegisterDialog(parentFrame);
        registerDialog.addSubmitListener(e -> handleRegistration());
        registerDialog.setVisible(true);
    }
    
	/*
		จัดการการลงทะเบียนผู้ใช้ใหม่
		- ตรวจสอบความถูกต้องของข้อมูลที่กรอก
		- ตรวจสอบความซ้ำซ้อนของ Username
		- สร้าง Claimant ID แบบสุ่ม
		- บันทึกข้อมูลผู้ใช้ใหม่ลงในฐานข้อมูล
		- แจ้งผลลัพธ์ผ่าน callback
	*/
    private void handleRegistration() {
        String username = registerDialog.getUsername().trim();
        String firstName = registerDialog.getFirstName().trim();
        String lastName = registerDialog.getLastName().trim();
        
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            registerDialog.showError("กรุณากรอกข้อมูลให้ครบทุกช่อง");
            return;
        }
        if (dataModel.isUsernameTaken(username)) {
            registerDialog.showError("Username นี้มีผู้ใช้งานแล้ว");
            return;
        }
        if (username.equalsIgnoreCase("admin")) {
            registerDialog.showError("ไม่สามารถใช้ Username นี้ได้");
            return;
        }
        
        int claimantId = generateClaimantId();
        double defaultIncome = 0.0;
        String defaultType = "LOW";
        
        ClaimModel newClaimant = new LowIncomeClaim( // ใช้ LowIncomeClaim เป็นค่าเริ่มต้น
            claimantId, 
            firstName, 
            lastName, 
            defaultIncome, 
            defaultType, 
            username
        );
        
        dataModel.saveNewClaimant(newClaimant);
		registerDialog.showSuccess(username, claimantId);
        
		// ปิดหน้าต่างลงทะเบียน
        registerDialog.dispose();
		// แจ้งผลลัพธ์การลงทะเบียนผ่าน callback หมายเหตุ: เพิ่มบรรทัดนี้ เข้าไป ใน method handleRegistration
		if (authCallback != null) {
            authCallback.onRegisterSuccess();
        }
    }
    
	// สร้าง Claimant ID แบบสุ่ม
    private int generateClaimantId() {
        int min = 10000;
        int max = 99999;
        return (int)(Math.random() * (max - min + 1)) + min;
    }
    
	// ตั้งค่า AuthCallback
    public void setAuthCallback(AuthCallback callback) {
        this.authCallback = callback;
        System.out.println("AuthCallback set");
    }
    
    public JPanel getLoginPanel() {
        return loginView;
    }
    
}