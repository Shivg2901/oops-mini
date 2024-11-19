import javax.swing.*;         // For GUI components like JFrame, JPanel, JLabel, etc.
import java.awt.*;            // For layout managers like GridLayout
import java.awt.event.*;      // For event handling like ActionListener
import java.io.*;             // For file handling like BufferedWriter and FileWriter
import java.util.List;   

public class SignupPanel extends JPanel {
    private static final String PERSON_DATABASE_FILE = "C:/Users/prana/OneDrive/Desktop/Pranav/College/OOM/DocumentManagementSystem/OOM_final/OOM 2/persons.txt";
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signupButton;
    private List<User> users;

    public SignupPanel(List<User> users) {
        this.users = users;  

        setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        signupButton = new JButton("Sign Up");

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpUser();
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(new JLabel(""));  
        add(signupButton);
    }
    
    private void signUpUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (User existingUser : users) {
            if (existingUser.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        User newUser = new User(username, password);
        users.add(newUser);

        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(PERSON_DATABASE_FILE, true))) {
            writer.write(username + "," + password);  // Store credentials as "username,password"
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving credentials to file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Sign Up Successful! You can now log in.");

        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(500, 400);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.add(new LoginPanel(users));  
            loginFrame.setVisible(true);

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(SignupPanel.this);
            topFrame.dispose();
        });
    }

    
//    private void signUpUser() {
//        String username = usernameField.getText();
//        String password = new String(passwordField.getPassword());
//        String confirmPassword = new String(confirmPasswordField.getPassword());
//
//        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        if (!password.equals(confirmPassword)) {
//            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        for (User existingUser : users) {
//            if (existingUser.getUsername().equals(username)) {
//                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//        }
//
//        User newUser = new User(username, password);
//        users.add(newUser);
//
//        JOptionPane.showMessageDialog(this, "Sign Up Successful! You can now log in.");
//
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                JFrame loginFrame = new JFrame("Login");
//                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                loginFrame.setSize(500, 400);
//                loginFrame.setLocationRelativeTo(null);
//                loginFrame.add(new LoginPanel(users));  
//                loginFrame.setVisible(true);
//
//                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(SignupPanel.this);
//                topFrame.dispose();
//            }
//        });
//    }
}
