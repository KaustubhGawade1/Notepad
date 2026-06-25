package context;

import event.EventManager;
import model.Document;
import service.FileManager;
import undo.UndoManager;

public class BeanFactory {

    public EventManager createEventManager() {
        return new EventManager();
    }

    public UndoManager createUndoManager() {
        return new UndoManager();
    }

    public FileManager createFileManager() {
        return new FileManager();
    }

    public Document createDocument(EventManager eventManager) {
        return new Document(eventManager);
    }
}
