import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class CreateDocumentPanel extends JPanel {
    private User loggedInUser;
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextArea descriptionArea;
    private JComboBox<Category> categoryComboBox;
    private JComboBox<Topic> topicComboBox;
    private JTextField tagsField;
    private JTextField filePathField;
    private JComboBox<String> statusComboBox;
    private List<Tag> selectedTags;
    private String selectedFilePath;

    public CreateDocumentPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.selectedTags = new ArrayList<>();
        setLayout(new BorderLayout());

        // Title Panel at the top
        JPanel titlePanel = new JPanel();
        JLabel headerLabel = new JLabel("Create New Document", SwingConstants.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(20.0f));
        titlePanel.add(headerLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        titleField = new JTextField(30);
        formPanel.add(titleField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        categoryComboBox = new JComboBox<>(getSampleCategories());
        formPanel.add(categoryComboBox, gbc);

        // Topic
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Topic:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        topicComboBox = new JComboBox<>(getSampleTopics());
        formPanel.add(topicComboBox, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] statuses = { "Draft", "In Review", "Published", "Archived" };
        statusComboBox = new JComboBox<>(statuses);
        formPanel.add(statusComboBox, gbc);

        // Tags
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tags:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel tagsPanel = new JPanel(new BorderLayout());
        tagsField = new JTextField();
        JButton addTagButton = new JButton("Add Tag");
        tagsPanel.add(tagsField, BorderLayout.CENTER);
        tagsPanel.add(addTagButton, BorderLayout.EAST);
        formPanel.add(tagsPanel, gbc);

        // File Selection
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("File:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel filePanel = new JPanel(new BorderLayout());
        filePathField = new JTextField();
        filePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(filePanel, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionArea = new JTextArea(3, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);

        // Content
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Content:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        formPanel.add(contentScrollPane, gbc);

        // Add form panel to center
        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button listeners
        addTagButton.addActionListener(e -> {
            String tagText = tagsField.getText().trim();
            if (!tagText.isEmpty()) {
                Tag newTag = new Tag(tagText, "");
                selectedTags.add(newTag);
                tagsField.setText("");
                JOptionPane.showMessageDialog(this, "Tag added: " + tagText);
            }
        });

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFilePath = selectedFile.getAbsolutePath();
                filePathField.setText(selectedFilePath);
            }
        });

        saveButton.addActionListener(e -> {
            if (validateForm()) {
                saveDocument();
            }
        });

        cancelButton.addActionListener(e -> {
            // Navigate back to home page
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            currentFrame.getContentPane().removeAll();
            currentFrame.getContentPane().add(new HomePagePanel(loggedInUser));
            currentFrame.revalidate();
            currentFrame.repaint();
        });
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            showError("Please enter a title");
            return false;
        }
        if (descriptionArea.getText().trim().isEmpty()) {
            showError("Please enter a description");
            return false;
        }
        if (contentArea.getText().trim().isEmpty()) {
            showError("Please enter content");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void saveDocument() {
        try {
            // Generate a simple ID (in production, this should be handled by a proper ID
            // generation system)
            int newId = (int) (Math.random() * 10000);

            Document newDoc = new Document(
                    newId,
                    titleField.getText().trim(),
                    contentArea.getText().trim(),
                    descriptionArea.getText().trim(),
                    selectedFilePath != null ? selectedFilePath : "",
                    loggedInUser);

            // Set additional properties
            newDoc.setCategory((Category) categoryComboBox.getSelectedItem());
            newDoc.setTopic((Topic) topicComboBox.getSelectedItem());
            newDoc.setStatus((String) statusComboBox.getSelectedItem());

            // Add tags
            for (Tag tag : selectedTags) {
                newDoc.addTag(tag);
            }

            // Call create method
            newDoc.create();

            // Add to user's documents
            loggedInUser.addDocument(newDoc);

            JOptionPane.showMessageDialog(this,
                    "Document created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Navigate back to home page
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            currentFrame.getContentPane().removeAll();
            currentFrame.getContentPane().add(new HomePagePanel(loggedInUser));
            currentFrame.revalidate();
            currentFrame.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error creating document: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to get sample categories (replace with your actual categories)
    private Category[] getSampleCategories() {
        return new Category[] {
                new Category("Technical", "Technical documentation"),
                new Category("Business", "Business documents"),
                new Category("Legal", "Legal documents"),
                new Category("Other", "Other documents")
        };
    }

    // Helper method to get sample topics (replace with your actual topics)
    private Topic[] getSampleTopics() {
        return new Topic[] {
                new Topic("Development", "Software development"),
                new Topic("Design", "Design documents"),
                new Topic("Requirements", "Requirements documents"),
                new Topic("General", "General topics")
        };
    }
}