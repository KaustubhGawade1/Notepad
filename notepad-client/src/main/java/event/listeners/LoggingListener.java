package event.listeners;

import event.Event;
import event.EventListener;

public class LoggingListener
        implements EventListener {

    @Override
    public void update(
            Event event
    ) {

        System.out.println(
                "[LOG] Event Received : "
                        + event
                        .getClass()
                        .getSimpleName()
        );

        System.out.println(
                "Timestamp : "
                        + event
                        .getTimestamp()
        );
    }
}