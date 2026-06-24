package buffer;

import java.util.ArrayList;
import java.util.List;

public class TextBuffer {

    private StringBuilder buffer;

    public TextBuffer() {
        buffer = new StringBuilder();
    }

    public TextBuffer(String text) {
        buffer = new StringBuilder(text);
    }

    public void insert(int index, String text) {

        if (index < 0 || index > buffer.length()) {
            throw new IllegalArgumentException("Invalid index");
        }

        buffer.insert(index, text);
    }

    public void delete(int start, int end) {

        if (start < 0 || end > buffer.length() || start > end) {
            throw new IllegalArgumentException("Invalid range");
        }

        buffer.delete(start, end);
    }

    public void replace(int start, int end, String text) {

        if (start < 0 || end > buffer.length() || start > end) {
            throw new IllegalArgumentException("Invalid range");
        }

        buffer.replace(start, end, text);
    }

    public String getText() {
        return buffer.toString();
    }

    public int length() {
        return buffer.length();
    }

    public void clear() {
        buffer.setLength(0);
    }

    public int charCount() {
        return buffer.length();
    }

    public int wordCount() {

        String text = buffer.toString().trim();

        if (text.isEmpty()) {
            return 0;
        }

        return text.split("\\s+").length;
    }

    public int lineCount() {

        String text = buffer.toString();

        if (text.isEmpty()) {
            return 0;
        }

        return text.split("\n").length;
    }

    public int find(String word) {
        return buffer.indexOf(word);
    }

    public List<Integer> findAll(String word) {

        List<Integer> indexes = new ArrayList<>();

        int index = buffer.indexOf(word);

        while (index != -1) {

            indexes.add(index);

            index = buffer.indexOf(word,
                    index + word.length());
        }

        return indexes;
    }
}
