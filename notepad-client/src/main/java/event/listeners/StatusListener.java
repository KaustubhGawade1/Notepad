package event.listeners;

import event.*;

public class StatusListener
        implements EventListener {

    @Override
    public void update(
            Event event
    ) {

        if (event
                instanceof TextChangedEvent) {

            System.out.println(
                    "[STATUS] Document Modified"
            );
        }

        if (event
                instanceof SaveEvent) {

            System.out.println(
                    "[STATUS] File Saved"
            );
        }
    }
}
