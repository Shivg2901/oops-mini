
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private int id;
    private String title;
    private String content;
    private String description;
    private String status;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private User creator;
    private Category category;
    private Topic topic;
    private List<Tag> tags;
    private String version;

    public Document(int id, String title, String content, String description, String filePath, User creator) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.filePath = filePath;
        this.creator = creator;
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.tags = new ArrayList<>();
        this.version = "1.0";
        this.status = "Draft";
    }

    // Core document operations
    public void create() {
        // Document creation logic
    }

    public void update() {
        this.lastModified = LocalDateTime.now();
        // Document update logic
    }

    public void delete() {
        // Document deletion logic
    }

    // Tag management
    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    // Category and Topic management
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    // Version control
    public void updateVersion(String newVersion) {
        this.version = newVersion;
        this.lastModified = LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    } // Added missing getter

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public User getCreator() {
        return creator;
    }

    public Category getCategory() {
        return category;
    }

    public Topic getTopic() {
        return topic;
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    } // Added missing getter

    // Setters
    public void setTitle(String title) {
        this.title = title;
        this.lastModified = LocalDateTime.now();
    }

    public void setContent(String content) {
        this.content = content;
        this.lastModified = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.lastModified = LocalDateTime.now();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.lastModified = LocalDateTime.now();
    }

    public void setStatus(String status) {
        this.status = status;
        this.lastModified = LocalDateTime.now();
    }
}