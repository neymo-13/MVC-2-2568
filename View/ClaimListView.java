package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/*
	สำหรับแสดงรายการคำขอเงินเยียวยา
	- มีตารางแสดงรายการคำขอทั้งหมด
	- มีปุ่มสำหรับกลับหน้าหลักและรีเฟรชข้อมูล
	- รองรับโหมดผู้ใช้ทั่วไปและโหมดผู้ดูแลระบบ (Admin)
*/
public class ClaimListView extends JPanel {
    
    private JTable claimsTable;
    private DefaultTableModel tableModel;
    private JButton btnBack;
    private JButton btnRefresh;
    private JLabel titleLabel;
    private boolean isAdminMode = false;
    
    public ClaimListView() {
        setLayout(new BorderLayout(10, 10));
        
        // Title
        titleLabel = new JLabel("รายการคำขอเงินเยียวยา", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table setup
        String[] columns = {"รหัสคำขอ", "วันที่ยื่น", "สถานะ", "จำนวนเงิน (บาท)", "ประเภทผู้ขอ"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        claimsTable = new JTable(tableModel);
        claimsTable.setRowHeight(30);
        claimsTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        claimsTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(claimsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("รายการคำขอทั้งหมด"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnBack = new JButton("กลับหน้าหลัก");
        btnBack.setBackground(new Color(169, 169, 169));
        btnBack.setForeground(Color.WHITE);
        buttonPanel.add(btnBack);
        
        btnRefresh = new JButton("รีเฟรช");
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        buttonPanel.add(btnRefresh);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // สำหรับผู้ใช้ทั่วไป
    public void setUserMode() {
        isAdminMode = false;
        titleLabel.setText("รายการคำขอเงินเยียวยาของคุณ");
        String[] columns = {"รหัสคำขอ", "วันที่ยื่น", "สถานะ", "จำนวนเงิน (บาท)", "ประเภทผู้ขอ"};
        tableModel.setColumnIdentifiers(columns);
    }
    
    // สำหรับ Admin
    public void setAdminMode() {
        isAdminMode = true;
        titleLabel.setText("รายการคำขอเงินเยียวยาทั้งหมด (Admin)");
        String[] columns = {"รหัสคำขอ", "Claimant ID", "ชื่อ-นามสกุล", "ประเภท", "วันที่ยื่น", "สถานะ", "เงินเยียวยา (บาท)"};
        tableModel.setColumnIdentifiers(columns);
    }
    
    // Clear and set table data
    public void setTableData(Object[][] data) {
        tableModel.setRowCount(0); // Clear existing data
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    // Event listeners
    public void addBackListener(java.awt.event.ActionListener listener) {
        btnBack.addActionListener(listener);
    }
    
    public void addRefreshListener(java.awt.event.ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }
    
    // Show message
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "ข้อมูล",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // Show no data message
    public void showNoDataMessage() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"ไม่มีข้อมูล", "", "", "", "", "", ""});
    }
    
    // Check if admin mode
    public boolean isAdminMode() {
        return isAdminMode;
    }
}