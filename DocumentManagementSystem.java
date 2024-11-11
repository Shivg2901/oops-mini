import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class DocumentManagementSystem extends JFrame {
    public DocumentManagementSystem() {
        super("Document Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(24.0f));
        add(welcomeLabel);
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(800, 600);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.add(new LoginPanel(users));
                loginFrame.setVisible(true);
            }
        });
    }
}
