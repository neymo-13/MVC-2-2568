package View;

import javax.swing.*;
import java.awt.*;

public class ClaimMainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
	

    public ClaimMainFrame() {
		// ตั้งค่า Font สำหรับภาษาไทยทั่วทั้งแอป
		Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);
		UIManager.put("Label.font", thaiFont);
		UIManager.put("Button.font", thaiFont);
		UIManager.put("TextField.font", thaiFont);
		UIManager.put("TitledBorder.font", thaiFont);

		// ตั้งค่าหน้าต่างหลัก
        setTitle("ระบบคำนวณเงินเยียวยาของรัฐ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
        setVisible(true);
    }

	// เพิ่ม JPanel ใหม่ลงใน CardLayout
    public void showPanel(JPanel panel, String name) {
        mainPanel.removeAll();
        mainPanel.add(panel, name);
        cardLayout.show(mainPanel, name);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
