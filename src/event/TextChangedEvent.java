package event;

public class TextChangedEvent
        extends Event {

    private String oldText;
    private String newText;

    public TextChangedEvent(
            String oldText,
            String newText
    ) {
        this.oldText = oldText;
        this.newText = newText;
    }

    public String getOldText() {
        return oldText;
    }

    public String getNewText() {
        return newText;
    }
}
