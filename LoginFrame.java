//import java.awt.*;
//import javax.swing.*;
//
//public class LoginFrame extends JFrame {
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JButton loginButton;
//    private JButton signupButton;
//
//    public LoginFrame() {
//        this.setTitle("Enterprise Document Management - Login");
//        this.setSize(400, 300);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLayout(new GridBagLayout());
//        this.setLocationRelativeTo(null); // Center the window
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing
//
//        // Username Label
//        JLabel usernameLabel = new JLabel("Username:");
//        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        this.add(usernameLabel, gbc);
//
//        // Username Field
//        this.usernameField = new JTextField(20);
//        this.usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        this.add(this.usernameField, gbc);
//
//        // Password Label
//        JLabel passwordLabel = new JLabel("Password:");
//        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        this.add(passwordLabel, gbc);
//
//        // Password Field
//        this.passwordField = new JPasswordField(20);
//        this.passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        this.add(this.passwordField, gbc);
//
//        // Buttons Panel
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        this.loginButton = new JButton("Login");
//        this.signupButton = new JButton("Sign Up");
//        buttonPanel.add(this.loginButton);
//        buttonPanel.add(this.signupButton);
//        gbc.gridwidth = 2;
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        this.add(buttonPanel, gbc);
//
//        // Button Styling
//        this.loginButton.setBackground(new Color(0, 123, 255));
//        this.loginButton.setForeground(Color.WHITE);
//        this.loginButton.setFocusPainted(false);
//        this.loginButton.setFont(new Font("Arial", Font.BOLD, 14));
//        this.signupButton.setBackground(new Color(0, 123, 255));
//        this.signupButton.setForeground(Color.WHITE);
//        this.signupButton.setFocusPainted(false);
//        this.signupButton.setFont(new Font("Arial", Font.BOLD, 14));
//
//        // Button Action Listeners
//        this.loginButton.addActionListener((e) -> {
//            this.loginUser();
//        });
//        this.signupButton.addActionListener((e) -> {
//            this.registerUser();
//        });
//    }
//
//    private void loginUser() {
//        String username = this.usernameField.getText();
//        String password = new String(this.passwordField.getPassword());
//
//        if (username.isEmpty() || password.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please fill in both fields", "Error", JOptionPane.ERROR_MESSAGE);
//        } else if (User.login(username, password)) {
//            JOptionPane.showMessageDialog(this, "Login Successful");
//            (new DocumentManagementSystem()).setVisible(true);
//            this.dispose();
//        } else {
//            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void registerUser() {
//        String username = this.usernameField.getText();
//        String password = new String(this.passwordField.getPassword());
//
//        if (username.isEmpty() || password.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please fill in both fields", "Error", JOptionPane.ERROR_MESSAGE);
//        } else if (User.register(username, password)) {
//            JOptionPane.showMessageDialog(this, "Signup Successful");
//        } else {
//            JOptionPane.showMessageDialog(this, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new LoginFrame().setVisible(true);
//        });
//    }
//}