package model;

public class Document {
    private String content = "";
    private String name = "Untitled";
    private boolean modified = false; // Tracks unsaved changes

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.modified = true; // Any change flags the document as modified
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
