package context;

import event.EventManager;
import model.Document;
import service.FileManager;
import undo.UndoManager;

public class ApplicationContext {

    private BeanFactory factory;

    private BeanRegistry registry;

    public ApplicationContext() {

        factory = new BeanFactory();

        registry = new BeanRegistry();

        initializeBeans();
    }

    private void initializeBeans() {

        EventManager eventManager =
                factory.createEventManager();

        registry.register(
                EventManager.class,
                eventManager
        );

        UndoManager undoManager =
                factory.createUndoManager();

        registry.register(
                UndoManager.class,
                undoManager
        );

        FileManager fileManager =
                factory.createFileManager();

        registry.register(
                FileManager.class,
                fileManager
        );

        Document document =
                factory.createDocument(
                        eventManager
                );

        registry.register(
                Document.class,
                document
        );
    }

    public <T> T getBean(
            Class<T> clazz
    ) {

        return registry.get(clazz);
    }
}
