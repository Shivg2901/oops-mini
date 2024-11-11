
// Update User.java to include documents
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Document> documents;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.documents = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
    }
}
