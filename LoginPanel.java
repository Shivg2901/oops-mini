import javax.swing.*;          // For GUI components like JFrame, JPanel, JOptionPane, etc.
import java.awt.*;             // For layout managers like GridLayout
import java.awt.event.*;       // For handling events like ActionListener
import java.io.*;              // For file operations like BufferedReader, FileReader
import java.util.List;         // For handling the list of users (if still used elsewhere)


public class LoginPanel extends JPanel {
    private static final String PERSON_DATABASE_FILE = "C:/Users/prana/OneDrive/Desktop/Pranav/College/OOM/DocumentManagementSystem/OOM_final/OOM 2/persons.txt";
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    private List<User> users;

    public LoginPanel(List<User> persons) {
        this.users = persons;
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignupPage();
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(signupButton);
    }
    private void loginUser() {
        String enteredUsername = usernameField.getText();
        String enteredPassword = new String(passwordField.getPassword());
        boolean isValidUser = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(PERSON_DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(","); // Assuming format is "username,password"
                if (credentials.length == 2) {
                    String username = credentials[0];
                    String password = credentials[1];

                    if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
                        isValidUser = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the credentials file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isValidUser) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            SwingUtilities.invokeLater(() -> {
                JFrame homePageFrame = new JFrame("Home Page");
                homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                homePageFrame.setSize(800, 600);
                homePageFrame.setLocationRelativeTo(null);
                homePageFrame.add(new HomePagePanel(new User(enteredUsername, enteredPassword)));  
                homePageFrame.setVisible(true);

                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(LoginPanel.this);
                topFrame.dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

//    private void loginUser() {
//        String enteredUsername = usernameField.getText();
//        String enteredPassword = new String(passwordField.getPassword());
//    
//        boolean isValidUser = false;
//        final User[] loggedInUser = new User[1]; 
//    
//        for (User user : users) {
//            if (user.getUsername().equals(enteredUsername) && user.getPassword().equals(enteredPassword)) {
//                isValidUser = true;
//                loggedInUser[0] = user;  
//                break;
//            }
//        }
//    
//        if (isValidUser && loggedInUser[0] != null) {  
//            JOptionPane.showMessageDialog(this, "Login successful!");
//    
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    JFrame homePageFrame = new JFrame("Home Page");
//                    homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    homePageFrame.setSize(800, 600);
//                    homePageFrame.setLocationRelativeTo(null);
//                    homePageFrame.add(new HomePagePanel(loggedInUser[0]));  
//                    homePageFrame.setVisible(true);
//    
//                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(LoginPanel.this);
//                    topFrame.dispose(); 
//                }
//            });
//        } else {
//            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
    
    private void showSignupPage() {
        JFrame signupFrame = new JFrame("Sign Up");
        signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signupFrame.setSize(500, 400);
        signupFrame.setLocationRelativeTo(null);
        signupFrame.add(new SignupPanel(users));
        signupFrame.setVisible(true);

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
    }
}
