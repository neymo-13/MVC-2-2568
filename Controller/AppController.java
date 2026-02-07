package Controller;

import View.*;
import Model.*;

import javax.swing.*;

public class AppController {

    private ClaimMainFrame frame;
    private ClaimDataModel dataModel;
    private ClaimModel currentUser;

    public AppController() {
        dataModel = new ClaimDataModel();
        frame = new ClaimMainFrame();
    }

	// เริ่มต้นแอปพลิเคชัน
    public void start() {
        showLogin();
    }

	// แสดงหน้า Login โดยเรียนใช้ AuthController ในการจัดการ login และ registration
    private void showLogin() {
        LoginView loginView = new LoginView();
        AuthController authController = new AuthController(frame, dataModel);

        authController.setAuthCallback(new AuthController.AuthCallback() {
            @Override
            public void onLoginSuccess(ClaimModel user) {
                currentUser = user;
                showClaimForm();
            }
            @Override
            public void onAdminLogin() {
                showAdminClaimList();
            }
            @Override
            public void onRegisterSuccess() {
                JOptionPane.showMessageDialog(
                    frame,
                    "ลงทะเบียนสำเร็จ กรุณาเข้าสู่ระบบใหม่"
                );
                loginView.setFocusToUsername();
            }
        });
        frame.showPanel(authController.getLoginPanel(), "LOGIN");
    }

	// แสดงหน้าแบบฟอร์มคำขอเคลม โดยใช้ ClaimFormController
    private void showClaimForm() {
        ClaimFormView view = new ClaimFormView();
        ClaimFormController controller =
                new ClaimFormController(view, dataModel, currentUser);

        controller.setCallback(
			new ClaimFormController.ClaimFormCallback() {

				@Override
				public void onSubmitSuccess() {
					showUserClaimList();
				}
				@Override
				public void onViewClaims() {
					showUserClaimList();
				}
			}
		);
        frame.showPanel(view, "CLAIM_FORM");
    }

	// แสดงหน้ารายการคำขอเคลมของผู้ใช้ทั่วไป
    private void showUserClaimList() {
        ClaimListView view = new ClaimListView();
        ClaimListController controller =
                new ClaimListController(view, dataModel, currentUser);

        controller.setCallback(() -> showClaimForm());
        frame.showPanel(view, "CLAIM_LIST");
    }

	// แสดงหน้ารายการคำขอเคลมของผู้ดูแลระบบ
    private void showAdminClaimList() {
        ClaimListView view = new ClaimListView();
        ClaimListController controller =
                new ClaimListController(view, dataModel);

        controller.setCallback(() -> showLogin());
        frame.showPanel(view, "ADMIN_LIST");
    }
}
