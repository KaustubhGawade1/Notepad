package buffer;

import event.*;
import event.listeners.*;
import java.util.ArrayList;
import java.util.List;

public class TextBuffer {

    private StringBuilder buffer;

    private EventManager eventManager;

    public TextBuffer(
            EventManager eventManager
    ) {
        this.eventManager =
                eventManager;

        buffer =
                new StringBuilder();
    }

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

        String oldText =
                buffer.toString();

        buffer.insert(
                index,
                text
        );

        String newText =
                buffer.toString();

        eventManager.notify(
                new TextChangedEvent(
                        oldText,
                        newText
                )
        );    }

    public void delete(int start, int end) {

        if (start < 0 || end > buffer.length() || start > end) {
            throw new IllegalArgumentException("Invalid range");
        }
        String oldText =
                buffer.toString();

        buffer.delete(start, end);

        String newText =
                buffer.toString();

        eventManager.notify(
                new TextChangedEvent(
                        oldText,
                        newText
                ) );
    }

    public void replace(int start, int end, String text) {

        if (start < 0 || end > buffer.length() || start > end) {
            throw new IllegalArgumentException("Invalid range");
        }
        String oldText =
                buffer.toString();


        buffer.replace(start, end, text);



        String newText =
                buffer.toString();

        eventManager.notify(
                new TextChangedEvent(
                        oldText,
                        newText
                )        );
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
