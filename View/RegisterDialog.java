package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*
	สำหรับแสดงการลงทะเบียนผู้ใช้ใหม่
	- มีฟิลด์ให้กรอก Username, ชื่อ, นามสกุล
	- มีปุ่มสำหรับยืนยันการลงทะเบียนและยกเลิก
*/
public class RegisterDialog extends JDialog {

    private JTextField txtUsername;
    private JTextField txtFirstName;
    private JTextField txtLastName;

    private JButton btnSubmit;
    private JButton btnCancel;

    public RegisterDialog(JFrame parent) {
        super(parent, "ลงทะเบียนผู้ใช้ใหม่", true); // true = modal
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Username
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Username:"), gbc);

		gbc.gridx = 1;
		txtUsername = new JTextField(15);
		formPanel.add(txtUsername, gbc);

		// First name
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(new JLabel("ชื่อ:"), gbc);

		gbc.gridx = 1;
		txtFirstName = new JTextField(15);
		formPanel.add(txtFirstName, gbc);

		// Last name
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(new JLabel("นามสกุล:"), gbc);

		gbc.gridx = 1;
		txtLastName = new JTextField(15);
		formPanel.add(txtLastName, gbc);

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		btnSubmit = new JButton("ลงทะเบียน");
		styleButton(btnSubmit, new Color(34, 139, 34));
		btnCancel = new JButton("ยกเลิก");
		styleButton(btnCancel, new Color(178, 34, 34));
		buttonPanel.add(btnSubmit);
		buttonPanel.add(btnCancel);

		// Add buttons
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		formPanel.add(buttonPanel, gbc);

		add(formPanel, BorderLayout.CENTER);
		btnCancel.addActionListener(e -> dispose());
    }

    // ดึงข้อมูลจากฟอร์ม
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getFirstName() {
        return txtFirstName.getText().trim();
    }

    public String getLastName() {
        return txtLastName.getText().trim();
    }

    // event
    public void addSubmitListener(ActionListener listener) {
        btnSubmit.addActionListener(listener);
    }

	// แสดงข้อความผิดพลาด
    public void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Register Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

	// แสดงข้อความยืนยันการยื่นคำขอสำเร็จ
    public void showSuccess(String username, int claimantId) {
		JLabel label = new JLabel(
			"<html>ยื่นคำขอสำเร็จ!<br>Username: <b>" + username + "</b><br>รหัสคำขอ: <b>" + claimantId + "</b></html>"
		);
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JOptionPane.showMessageDialog(
			this,
			label,
			"สำเร็จ",
			JOptionPane.INFORMATION_MESSAGE
		);
	}

	// ปรับแต่งปุ่มให้สวย
	private void styleButton(JButton button, Color color) {
		button.setFont(new Font("Tahoma", Font.BOLD, 13));
		button.setPreferredSize(new Dimension(120, 35));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setBorderPainted(false);
	}

}
