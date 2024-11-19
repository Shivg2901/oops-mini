import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JDialog;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;

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
        formPanel.add(new JLabel("Selected Tags:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel tagsPanel = new JPanel(new BorderLayout());
        DefaultListModel<Tag> selectedTagsModel = new DefaultListModel<>();
        JList<Tag> selectedTagsList = new JList<>(selectedTagsModel);
        JButton selectTagsButton = new JButton("Select Tags");
        tagsPanel.add(new JScrollPane(selectedTagsList), BorderLayout.CENTER);
        tagsPanel.add(selectTagsButton, BorderLayout.EAST);
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
        // gbc.gridx = 0;
        // gbc.gridy = 7;
        // gbc.weightx = 0.0;
        // formPanel.add(new JLabel("Content:"), gbc);

        // gbc.gridx = 1;
        // gbc.weightx = 1.0;
        // contentArea = new JTextArea(10, 30);
        // contentArea.setLineWrap(true);
        // contentArea.setWrapStyleWord(true);
        // JScrollPane contentScrollPane = new JScrollPane(contentArea);
        // formPanel.add(contentScrollPane, gbc);

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
        selectTagsButton.addActionListener(e -> {
            // Create a dialog for tag selection
            JDialog tagDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Tags", true);
            tagDialog.setLayout(new BorderLayout());

            // Create a list with checkboxes
            JPanel checkBoxPanel = new JPanel();
            checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

            Tag[] availableTags = getSampleTags();
            List<JCheckBox> checkBoxes = new ArrayList<>();

            for (Tag tag : availableTags) {
                JCheckBox checkBox = new JCheckBox(tag.getName());
                checkBox.setSelected(selectedTags.contains(tag));
                checkBoxes.add(checkBox);
                checkBoxPanel.add(checkBox);
            }

            // Add scroll pane for many tags
            JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
            tagDialog.add(scrollPane, BorderLayout.CENTER);

            // Add OK and Cancel buttons
            JPanel buttonPanel2 = new JPanel();
            JButton okButton = new JButton("OK");
            JButton cancelButton2 = new JButton("Cancel");

            okButton.addActionListener(evt -> {
                selectedTags.clear();
                selectedTagsModel.clear();

                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isSelected()) {
                        Tag selectedTag = availableTags[i];
                        selectedTags.add(selectedTag);
                        selectedTagsModel.addElement(selectedTag);
                    }
                }
                tagDialog.dispose();
            });

            cancelButton2.addActionListener(evt -> tagDialog.dispose());

            buttonPanel2.add(okButton);
            buttonPanel2.add(cancelButton2);
            tagDialog.add(buttonPanel2, BorderLayout.SOUTH);

            tagDialog.setSize(300, 400);
            tagDialog.setLocationRelativeTo(this);
            tagDialog.setVisible(true);
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
        // if (contentArea.getText().trim().isEmpty()) {
        // showError("Please enter content");
        // return false;
        // }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void saveDocument() {
        try {

            int newId = (int) (Math.random() * 10000);

            Document newDoc = new Document(
                    newId,
                    titleField.getText().trim(),

                    descriptionArea.getText().trim(),
                    selectedFilePath != null ? selectedFilePath : "",
                    loggedInUser);

            newDoc.setCategory((Category) categoryComboBox.getSelectedItem());
            newDoc.setTopic((Topic) topicComboBox.getSelectedItem());
            newDoc.setStatus((String) statusComboBox.getSelectedItem());

            for (Tag tag : selectedTags) {
                newDoc.addTag(tag);
            }

            newDoc.create();

            loggedInUser.addDocument(newDoc);

            JOptionPane.showMessageDialog(this,
                    "Document created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

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

    private Category[] getSampleCategories() {
        return new Category[] {
                new Category("Technical", "Technical documentation"),
                new Category("Business", "Business documents"),
                new Category("Legal", "Legal documents"),
                new Category("Other", "Other documents")
        };
    }

    private Topic[] getSampleTopics() {
        return new Topic[] {
                new Topic("Development", "Software development"),
                new Topic("Design", "Design documents"),
                new Topic("Requirements", "Requirements documents"),
                new Topic("General", "General topics")
        };
    }

    private Tag[] getSampleTags() {
        return new Tag[] {
                new Tag("Java", "Java programming"),
                new Tag("Python", "Python programming"),
                new Tag("Documentation", "Documentation related"),
                new Tag("Tutorial", "Tutorial content"),
                new Tag("API", "API documentation"),
                new Tag("Database", "Database related"),
                new Tag("Security", "Security topics"),
                new Tag("Testing", "Testing related")
        };
    }
}