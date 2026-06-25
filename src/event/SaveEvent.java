package event;

public class SaveEvent
        extends Event {

    private String fileName;

    public SaveEvent(
            String fileName
    ) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
