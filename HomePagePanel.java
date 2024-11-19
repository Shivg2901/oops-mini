import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePagePanel extends JPanel {
    private User loggedInUser;

    public HomePagePanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(24.0f));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        JButton viewDocumentsButton = new JButton("View Documents");
        JButton createDocumentButton = new JButton("Create Document");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(viewDocumentsButton);
        buttonPanel.add(createDocumentButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

        viewDocumentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(HomePagePanel.this);
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ViewDocumentsPanel(loggedInUser));
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });

        createDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(HomePagePanel.this);
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new CreateDocumentPanel(loggedInUser));
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logoutUser();
            }
        });
    }

    private void logoutUser() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(800, 600);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.add(new LoginPanel(new java.util.ArrayList<User>()));
                loginFrame.setVisible(true);

                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(HomePagePanel.this);
                topFrame.dispose();
            }
        });
    }
}
