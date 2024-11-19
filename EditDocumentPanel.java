import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class EditDocumentPanel extends JPanel {
    private User loggedInUser;
    private Document document;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> topicComboBox;
    private JTextField tagsField;
    private JButton saveButton;
    private JButton cancelButton;

    public EditDocumentPanel(User loggedInUser, Document document) {
        this.loggedInUser = loggedInUser;
        this.document = document;

        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Field
        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField(document.getTitle());
        formPanel.add(titleField);

        // Description Field (replacing "Content")
        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(document.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(descriptionArea));

        // Category Dropdown
        formPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(getCategories());
        categoryComboBox.setSelectedItem(document.getCategory() != null ? document.getCategory().getName() : null);
        formPanel.add(categoryComboBox);

        // Topic Dropdown
        formPanel.add(new JLabel("Topic:"));
        topicComboBox = new JComboBox<>(getTopics());
        topicComboBox.setSelectedItem(document.getTopic() != null ? document.getTopic().getName() : null);
        formPanel.add(topicComboBox);

        // Tags Field
        formPanel.add(new JLabel("Tags (comma-separated):"));
        tagsField = new JTextField(document.getTags().stream().map(Tag::getName).collect(Collectors.joining(", ")));
        formPanel.add(tagsField);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocument();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the documents view
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(EditDocumentPanel.this);
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ViewDocumentsPanel(loggedInUser));
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }

    private void saveDocument() {
        document.setTitle(titleField.getText());
        document.setDescription(descriptionArea.getText());

        // Set Category
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        document.setCategory(selectedCategory != null ? new Category(selectedCategory) : null);

        // Set Topic
        String selectedTopic = (String) topicComboBox.getSelectedItem();
        document.setTopic(selectedTopic != null ? new Topic(selectedTopic) : null);

        // Set Tags
        String[] tagNames = tagsField.getText().split(",");
        List<Tag> tags = List.of(tagNames).stream().map(String::trim).map(Tag::new).collect(Collectors.toList());
        document.setTags(tags);

        // Save the document (you might need to implement actual persistence here)
        JOptionPane.showMessageDialog(this, "Document saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Navigate back to the documents view
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.getContentPane().removeAll();
        currentFrame.getContentPane().add(new ViewDocumentsPanel(loggedInUser));
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    // Helper methods to get categories and topics (mock data here, replace with
    // real data as needed)
    private String[] getCategories() {
        return new String[] { "Technical", "Business", "Legal", "Other" };
    }

    private String[] getTopics() {
        return new String[] { "Development", "Design", "Requirements", "General" };
    }
}
