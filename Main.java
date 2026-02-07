import javax.swing.SwingUtilities;
import Controller.AppController;

public class Main {
    public static void main(String[] args) {
		// เริ่นต้นแอปพลิเคชัน
        SwingUtilities.invokeLater(() -> {
            new AppController().start();
        });
    }
}
