package model;

import buffer.TextBuffer;

public class Document {

    private String name;
    private boolean modified;

    private TextBuffer buffer;

    public Document() {

        this.name = "Untitled";
        this.modified = false;
        this.buffer = new TextBuffer();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextBuffer getBuffer() {
        return buffer;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
