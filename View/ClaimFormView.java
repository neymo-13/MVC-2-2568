package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*
	แบบฟอร์มยื่นคำขอเงินเยียวยา
	- แสดงข้อมูลผู้ใช้ (Claimant ID, Username, ชื่อ, นามสกุล)
	- มีฟิลด์ให้กรอกข้อมูลรายได้ปัจจุบัน
	- มีปุ่มสำหรับคำนวณเงินเยียวยา
	- แสดงผลลัพธ์การคำนวณ (ประเภทผู้ขอ, จำนวนเงินเยียวยา)
	- มีปุ่มสำหรับยืนยันการยื่นคำขอ และดูรายการคำขอ
*/
public class ClaimFormView extends JPanel {
    
	// ข้อมูลผู้ใช้
    private JLabel lblClaimantId;
    private JLabel lblUsername;
    private JLabel lblFirstName;
    private JLabel lblLastName;
    private JTextField txtIncome;
    
	// ผลลัพธ์การคำนวณ
    private JLabel lblClaimantType;
    private JLabel lblCompensationAmount;
    
	// Buttons
    private JButton btnCalculate;
    private JButton btnSubmit;
    private JButton btnViewClaims;

	// Fonts
	private static final Font FONT_LABEL = new Font("Tahoma", Font.PLAIN, 14);
	private static final Font FONT_VALUE = new Font("Tahoma", Font.BOLD, 15);
	private static final Font FONT_SECTION = new Font("Tahoma", Font.BOLD, 16);

    
    public ClaimFormView() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Title
        JLabel title = new JLabel("แบบฟอร์มยื่นคำขอเงินเยียวยา", JLabel.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 22));
		add(title, BorderLayout.NORTH);
        
        // Main form panel
	    JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(createUserInfoPanel());
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(createIncomePanel());
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(createResultPanel());

		// เพิ่มปุ่ม
		add(mainPanel, BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
	// แสดงข้อมูลผู้ใช้ ในส่วนบนสุด ได้แก่ Claimant ID, Username, ชื่อ, นามสกุล
	private JPanel createUserInfoPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "ข้อมูลผู้ยื่นคำขอ",
            0, 0,
            FONT_SECTION
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.anchor = GridBagConstraints.WEST;

		lblClaimantId = new JLabel(); lblClaimantId.setFont(FONT_VALUE);
		lblUsername = new JLabel();	 lblUsername.setFont(FONT_VALUE);
		lblFirstName = new JLabel(); lblFirstName.setFont(FONT_VALUE);
		lblLastName = new JLabel(); lblLastName.setFont(FONT_VALUE);

		JLabel[] labels = {
			new JLabel("Claimant ID:"), lblClaimantId,
			new JLabel("Username:"), lblUsername,
			new JLabel("ชื่อ:"), lblFirstName,
			new JLabel("นามสกุล:"), lblLastName
		};
		labels[0].setFont(FONT_LABEL);
		labels[2].setFont(FONT_LABEL);
		labels[4].setFont(FONT_LABEL);
		labels[6].setFont(FONT_LABEL);

		// จัดวางป้ายกำกับและค่าต่างๆ ในกริด
		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = i % 2;
			gbc.gridy = i / 2;
			panel.add(labels[i], gbc);
		}

		return panel;
	}

	// แสดงช่องกรอกข้อมูลรายได้ และปุ่มคำนวณ
	private JPanel createIncomePanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBorder(BorderFactory.createTitledBorder("ข้อมูลรายได้"));

		panel.add(new JLabel("เงินเดือน (บาท):"));
		txtIncome = new JTextField(12);
		panel.add(txtIncome);

		btnCalculate = new JButton("คำนวณเงินเยียวยา");
		styleButton(btnCalculate, new Color(70, 130, 180));
		panel.add(btnCalculate);
		return panel;
	}

	// แสดงผลลัพธ์การคำนวณ ได้แก่ ประเภทผู้ขอ และ จำนวนเงินเยียวยา
	private JPanel createResultPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("ผลการคำนวณ"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.anchor = GridBagConstraints.WEST;

		lblClaimantType = new JLabel();
		lblClaimantType.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblClaimantType.setForeground(Color.BLUE);

		lblCompensationAmount = new JLabel();
		lblCompensationAmount.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCompensationAmount.setForeground(Color.RED);

		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(new JLabel("ประเภทผู้ขอ:"), gbc);
		gbc.gridx = 1;
		panel.add(lblClaimantType, gbc);

		gbc.gridx = 0; gbc.gridy = 1;
		panel.add(new JLabel("จำนวนเงินเยียวยา:"), gbc);
		gbc.gridx = 1;
		panel.add(lblCompensationAmount, gbc);
		return panel;
	}

	// สร้างแผงปุ่มที่ส่วนล่าง ได้แก่ ปุ่มดูรายการคำขอ และ ปุ่มยืนยันการยื่นคำขอ
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

		btnViewClaims = new JButton("ดูรายการคำขอ");
		styleButton(btnViewClaims, new Color(255, 140, 0));

		btnSubmit = new JButton("ยืนยันการยื่น");
		styleButton(btnSubmit, new Color(34, 139, 34));
		btnSubmit.setEnabled(false);

		panel.add(btnViewClaims);
		panel.add(btnSubmit);

		return panel;
	}

    // ตั้งค่าข้อมูลผู้ใช้ ในฟอร์ม
    public void setUserData(int claimantId, String username, String firstName, String lastName, double currentIncome) {
        lblClaimantId.setText(String.valueOf(claimantId));
        lblUsername.setText(username);
        lblFirstName.setText(firstName);
        lblLastName.setText(lastName);
        txtIncome.setText(String.valueOf(currentIncome));
    }
    
    // ดึงข้อมูลรายได้ที่กรอกจากฟิลด์
    public String getIncomeText() {
        return txtIncome.getText().trim();
    }
    
	// ดึง Claimant ID
    public int getClaimantId() {
        try {
            return Integer.parseInt(lblClaimantId.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    // ตั้งค่าผลลัพธ์การคำนวณ
    public void setCalculationResults(String claimantType, double compensationAmount) {
		if (claimantType.equals("LOW")) {
			claimantType = "รายได้น้อย";
		} else if (claimantType.equals("NORMAL")) {
			claimantType = "รายได้ปานกลาง";
		} else {
			claimantType = "รายได้สูง";
		}
		lblClaimantType.setText(claimantType);
        lblCompensationAmount.setText(String.format("%,.2f", compensationAmount));
        btnSubmit.setEnabled(true);
    }
    
    // ล้างผลลัพธ์การคำนวณ
    public void clearResults() {
        lblClaimantType.setText("");
        lblCompensationAmount.setText("");
        btnSubmit.setEnabled(false);
    }
    
    // Event listeners
    public void addCalculateListener(ActionListener listener) {
        btnCalculate.addActionListener(listener);
    }
    
    public void addSubmitListener(ActionListener listener) {
        btnSubmit.addActionListener(listener);
    }
    
    public void addViewClaimsListener(ActionListener listener) {
        btnViewClaims.addActionListener(listener);
    }
    
    // ตรวจสอบความถูกต้องของข้อมูลรายได้ที่กรอก ว่าเป็นตัวเลขบวกหรือไม่ และไม่เว้นว่าง
    public boolean validateIncome() {
        String incomeText = getIncomeText();
        if (incomeText.isEmpty()) {
            showError("กรุณากรอกเงินเดือน");
            return false;
        }
        
        try {
            double income = Double.parseDouble(incomeText);
            if (income < 0) {
                showError("เงินเดือนต้องไม่ต่ำกว่า 0");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError("กรุณากรอกเงินเดือนเป็นตัวเลขที่ถูกต้อง");
            return false;
        }
    }
    
    // แสดงข้อความผิดพลาด
    public void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "ข้อผิดพลาด",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    // แสดงข้อความยืนยันการยื่นคำขอสำเร็จ
    public void showSuccess(String claimId) {
		JLabel label = new JLabel(
			"<html>ยื่นคำขอสำเร็จ!<br>รหัสคำขอ: <b>" + claimId + "</b></html>"
		);
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JOptionPane.showMessageDialog(
			this,
			label,
			"สำเร็จ",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
    
    // Clear income field
    public void clearIncomeField() {
        txtIncome.setText("");
    }

	private void styleButton(JButton button, Color color) {
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setPreferredSize(new Dimension(180, 40));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setBorderPainted(false);
	}

}