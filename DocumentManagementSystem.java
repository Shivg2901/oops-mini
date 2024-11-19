import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

public class DocumentManagementSystem extends JFrame {
    private static final String PERSON_DATABASE_FILE = "C:/Users/prana/OneDrive/Desktop/Pranav/College/OOM/DocumentManagementSystem/OOM_final/OOM 2/persons.txt";
    private static final String DOCUMENT_DATABASE_FILE = "C:/Users/prana/OneDrive/Desktop/Pranav/College/OOM/DocumentManagementSystem/OOM_final/OOM 2/files.txt";

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
        List<User> users = loadPersonsFromDatabase();
        List<Document> documents = loadDocumentsFromFile(); 

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(500, 400);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.add(new LoginPanel(users));
                loginFrame.setVisible(true);
            }
        });
    }

    private static List<User> loadPersonsFromDatabase() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PERSON_DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                users.add(new User(userData[0], userData[1]));
            }
        } catch (IOException e) {
            System.err.println("Error reading persons.txt: " + e.getMessage());
        }
        return users;
    }
    
    private static List<Document> loadDocumentsFromFile() {
        List<Document> documents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCUMENT_DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into components
                String[] documentData = line.split(",");
                
                // Ensure we have enough data to create a document
                if (documentData.length >= 5) {
                    int id = Integer.parseInt(documentData[0]);
                    String title = documentData[1];
                    String description = documentData[2];
                    String filePath = documentData[3];
                    
                    // Directly create a user if needed
                    User creator = new User(documentData[4], "defaultPassword");
                    
                    Document document = new Document(id, title, description, filePath, creator);
                    documents.add(document);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading files.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid document ID format: " + e.getMessage());
        }
        return documents;
    }

    private static void saveDocumentToDatabase(Document document) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCUMENT_DATABASE_FILE, true))) {
            // Use existing methods from the Document class
            writer.write(
                document.getId() + "," + 
                document.getTitle() + "," + 
                document.getDescription() + "," + 
                document.getFilePath() + "," + 
                document.getCreator().getUsername()
            );
            writer.newLine();
        } catch (IOException e) {
            // Handle file write error
            System.err.println("Error saving document to database: " + e.getMessage());
        }
    }
}