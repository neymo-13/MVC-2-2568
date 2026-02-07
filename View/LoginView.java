package View;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionListener;

/*
	ระบบเข้าสู่ระบบ (Login System)
	- มีฟิลด์ให้กรอก Username
	- มีปุ่มสำหรับเข้าสู่ระบบและลงทะเบียน
	- มีการแสดงข้อความแนะนำการใช้งาน
		- ผู้ใช้ทั่วไป: กรอก Username ที่ลงทะเบียนไว้
		- เจ้าหน้าที่: กรอกคำว่า "admin"
*/
public class LoginView extends JPanel {
	// Components
    private JTextField txtUsername;
    private JButton btnLogin;
    private JButton btnRegis;

    public LoginView() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel title = new JLabel("ระบบคำนวณเงินเยียวยาของรัฐ", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.GRAY),
			"เข้าสู่ระบบ"
		);
		border.setTitleFont(new Font("Tahoma", Font.PLAIN, 14));
		formPanel.setBorder(border);
				
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblUsername, gbc);
        
        // Username text field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(txtUsername, gbc);
        
        // Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		btnLogin = new JButton("เข้าสู่ระบบ");
		styleButton(btnLogin, new Color(70, 130, 180));
		btnRegis = new JButton("ลงทะเบียน");
		styleButton(btnRegis, new Color(34, 139, 34));
		buttonPanel.add(btnLogin);
		buttonPanel.add(btnRegis);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		formPanel.add(buttonPanel, gbc);
        
        // Instruction label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel lblInstruction = new JLabel(
            "<html><center>"
            + "• ผู้ใช้ทั่วไป: กรอก Username ที่ลงทะเบียนไว้<br>"
            + "• เจ้าหน้าที่: กรอกคำว่า \"admin\""
            + "</center></html>"
        );
        lblInstruction.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblInstruction.setForeground(Color.GRAY);
        lblInstruction.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblInstruction, gbc);

        add(formPanel, BorderLayout.CENTER);
        
        // เพิ่ม keyboard shortcut (Enter key)
        txtUsername.addActionListener(e -> btnLogin.doClick());
    }

	// getter methods
    public String getUsername() {
        return txtUsername.getText().trim();
    }

	// ปุ่มเข้าสู่ระบบ
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

	// ปุ่มลงทะเบียน
    public void addRegisListener(ActionListener listener) {
        btnRegis.addActionListener(listener);
    }

	// แสดงข้อความผิดพลาด
    public void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Login Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

	// ล้างข้อมูลในฟิลด์
    public void clearFields() {
        txtUsername.setText("");
    }
    
    // เพิ่ม method เพื่อให้ focus ที่ text field
    public void setFocusToUsername() {
        txtUsername.requestFocusInWindow();
    }

	// ปรับแต่งปุ่มให้สวย
	private void styleButton(JButton button, Color color) {
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setPreferredSize(new Dimension(150, 40));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setBorderPainted(false);
	}

}