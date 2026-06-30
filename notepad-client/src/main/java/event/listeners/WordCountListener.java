package event.listeners;

import event.*;

public class WordCountListener
        implements EventListener {

    @Override
    public void update(
            Event event
    ) {

        if (!(event
                instanceof TextChangedEvent)) {
            return;
        }

        TextChangedEvent e =
                (TextChangedEvent) event;

        String text =
                e.getNewText();

        int words =
                text.trim()
                        .isEmpty()
                        ? 0
                        : text.trim()
                        .split("\\s+")
                        .length;

        System.out.println(
                "[WORD COUNT] "
                        + words
        );
    }
}
