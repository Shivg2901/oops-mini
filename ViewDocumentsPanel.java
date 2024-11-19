import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewDocumentsPanel extends JPanel {
    private User loggedInUser;
    private JTable documentsTable;
    private DefaultTableModel tableModel;
    private List<Document> originalDocuments;
    private List<Document> documents;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private JTextField searchField;

    public ViewDocumentsPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.originalDocuments = loggedInUser.getDocuments();
        this.documents = new ArrayList<>(originalDocuments); // Initialize displayed list
        setLayout(new BorderLayout());

        // Create top panel with title
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("My Documents", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(20.0f));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Create table with columns matching your Document class
        String[] columnNames = { "ID", "Title", "Description", "Status", "Version", "Created Date", "Last Modified",
                "Category", "Topic", "Tags" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        documentsTable = new JTable(tableModel);
        documentsTable.setRowHeight(25);
        documentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(documentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton viewButton = new JButton("View Details");
        JButton viewContentButton = new JButton("View Content");

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        buttonPanel.add(viewContentButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load documents
        loadDocuments();

        // Button listeners
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = documentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    Document selectedDoc = documents.get(selectedRow);
                    showDocumentDetails(selectedDoc);
                } else {
                    JOptionPane.showMessageDialog(ViewDocumentsPanel.this,
                            "Please select a document to view",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = documentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    Document selectedDoc = documents.get(selectedRow);
                    editDocument(selectedDoc);
                } else {
                    JOptionPane.showMessageDialog(ViewDocumentsPanel.this,
                            "Please select a document to edit",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = documentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(ViewDocumentsPanel.this,
                            "Are you sure you want to delete this document?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteDocument(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(ViewDocumentsPanel.this,
                            "Please select a document to delete",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to home page
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(ViewDocumentsPanel.this);
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new HomePagePanel(loggedInUser));
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });

        viewContentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = documentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    Document selectedDoc = documents.get(selectedRow);
                    showDocumentContent(selectedDoc);
                } else {
                    JOptionPane.showMessageDialog(ViewDocumentsPanel.this,
                            "Please select a document to view content",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDocuments();
            }
        });
    }

    private void loadDocuments() {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Filter documents based on the search query
        String searchQuery = searchField.getText().toLowerCase();
        documents = originalDocuments.stream()
                .filter(doc -> doc.getTitle().toLowerCase().contains(searchQuery)
                        || doc.getDescription().toLowerCase().contains(searchQuery)
                        || (doc.getCategory() != null
                                && doc.getCategory().getName().toLowerCase().contains(searchQuery))
                        || (doc.getTopic() != null && doc.getTopic().getName().toLowerCase().contains(searchQuery))
                        || doc.getTags().stream().anyMatch(tag -> tag.getName().toLowerCase().contains(searchQuery)))
                .collect(Collectors.toList());

        // Populate the table with filtered documents
        for (Document doc : documents) {
            Object[] row = {
                    doc.getId(),
                    doc.getTitle(),
                    doc.getDescription(),
                    doc.getStatus(),
                    doc.getVersion(),
                    doc.getCreatedAt().format(DATE_FORMATTER),
                    doc.getLastModified().format(DATE_FORMATTER),
                    doc.getCategory() != null ? doc.getCategory().getName() : "N/A",
                    doc.getTopic() != null ? doc.getTopic().getName() : "N/A",
                    doc.getTags() != null
                            ? String.join(", ", doc.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                            : "N/A"
            };
            tableModel.addRow(row);
        }
    }

    private void showDocumentContent(Document doc) {
        JDialog contentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Document Content", true);
        contentDialog.setLayout(new BorderLayout());

        JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pathPanel.add(new JLabel("File Path:"));
        pathPanel.add(new JLabel(doc.getFilePath() != null ? doc.getFilePath() : "N/A"));
        contentDialog.add(pathPanel, BorderLayout.NORTH);

        if (doc.getFilePath() != null && !doc.getFilePath().isEmpty()) {
            try {
                String content = Files.readString(Paths.get(doc.getFilePath()));
                JTextArea contentArea = new JTextArea(content);
                contentArea.setEditable(false);
                contentArea.setLineWrap(true);
                contentArea.setWrapStyleWord(true);
                JScrollPane contentScrollPane = new JScrollPane(contentArea);
                contentScrollPane.setPreferredSize(new Dimension(480, 300));

                contentDialog.add(contentScrollPane, BorderLayout.CENTER);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Unable to read file content: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> contentDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        contentDialog.add(buttonPanel, BorderLayout.SOUTH);

        contentDialog.setSize(500, 400);
        contentDialog.setLocationRelativeTo(this);
        contentDialog.setVisible(true);
    }

    private void showDocumentDetails(Document doc) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Document Details", true);
        dialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add all relevant document details
        detailsPanel.add(new JLabel("ID:"));
        detailsPanel.add(new JLabel(String.valueOf(doc.getId())));

        detailsPanel.add(new JLabel("Title:"));
        detailsPanel.add(new JLabel(doc.getTitle()));

        if (doc.getCategory() != null) {
            detailsPanel.add(new JLabel("Category:"));
            detailsPanel.add(new JLabel(doc.getCategory().getName()));
        }

        if (doc.getTopic() != null) {
            detailsPanel.add(new JLabel("Topic:"));
            detailsPanel.add(new JLabel(doc.getTopic().getName()));
        }

        detailsPanel.add(new JLabel("Status:"));
        detailsPanel.add(new JLabel(doc.getStatus()));

        detailsPanel.add(new JLabel("File Path:"));
        detailsPanel.add(new JLabel(doc.getFilePath() != null ? doc.getFilePath() : "N/A"));

        detailsPanel.add(new JLabel("Version:"));
        detailsPanel.add(new JLabel(doc.getVersion()));

        detailsPanel.add(new JLabel("Created At:"));
        detailsPanel.add(new JLabel(doc.getCreatedAt().format(DATE_FORMATTER)));

        detailsPanel.add(new JLabel("Last Modified:"));
        detailsPanel.add(new JLabel(doc.getLastModified().format(DATE_FORMATTER)));

        detailsPanel.add(new JLabel("Tags:"));
        detailsPanel.add(new JLabel(doc.getTags() != null
                ? String.join(", ", doc.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                : "N/A"));

        detailsPanel.add(new JLabel("Description:"));
        JTextArea descArea = new JTextArea(doc.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        detailsPanel.add(new JScrollPane(descArea));

        dialog.add(detailsPanel, BorderLayout.CENTER);

        // Read and display file content if available
        if (doc.getFilePath() != null && !doc.getFilePath().isEmpty()) {
            try {
                String content = Files.readString(Paths.get(doc.getFilePath()));
                JTextArea contentArea = new JTextArea(content);
                contentArea.setEditable(false);
                contentArea.setLineWrap(true);
                contentArea.setWrapStyleWord(true);
                JScrollPane contentScrollPane = new JScrollPane(contentArea);
                contentScrollPane.setPreferredSize(new Dimension(480, 200));

                dialog.add(contentScrollPane, BorderLayout.SOUTH);
            } catch (IOException e) {
                // JOptionPane.showMessageDialog(this,
                // "Unable to read file content: " + e.getMessage(),
                // "Error",
                // JOptionPane.ERROR_MESSAGE);
            }
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editDocument(Document doc) {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.getContentPane().removeAll();
        currentFrame.getContentPane().add(new EditDocumentPanel(loggedInUser, doc));
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    private void deleteDocument(int selectedRow) {
        Document docToDelete = documents.get(selectedRow);
        docToDelete.delete(); // Call the document's delete method

        // Remove from both the displayed list and the original master list
        originalDocuments.remove(docToDelete);
        documents.remove(selectedRow);
        tableModel.removeRow(selectedRow);

        JOptionPane.showMessageDialog(this,
                "Document deleted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
