import context.ApplicationContext;
import event.*;
import event.listeners.*;
import gui.NotepadFrame;
import controller.EditorController;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ApplicationContext();

        EditorController controller =
                new EditorController(context);

        EventManager eventManager =
                context.getBean(EventManager.class);

        eventManager.subscribe(
                TextChangedEvent.class,
                new LoggingListener()
        );

        eventManager.subscribe(
                TextChangedEvent.class,
                new WordCountListener()
        );

        eventManager.subscribe(
                TextChangedEvent.class,
                new StatusListener()
        );

        eventManager.subscribe(
                SaveEvent.class,
                new StatusListener()
        );

        SwingUtilities.invokeLater(() -> {

            new NotepadFrame(controller);

        });
    }
}