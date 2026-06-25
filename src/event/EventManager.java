package event;

import java.util.*;

public class EventManager {

    private Map<Class<? extends Event>,
            List<EventListener>>
            listeners;

    public EventManager() {

        listeners =
                new HashMap<>();
    }

    public void subscribe(
            Class<? extends Event> eventType,
            EventListener listener
    ) {

        listeners
                .computeIfAbsent(
                        eventType,
                        k -> new ArrayList<>()
                )
                .add(listener);
    }

    public void unsubscribe(
            Class<? extends Event> eventType,
            EventListener listener
    ) {

        List<EventListener> list =
                listeners.get(eventType);

        if (list != null) {

            list.remove(listener);
        }
    }

    public void notify(
            Event event
    ) {

        List<EventListener> list =
                listeners.get(
                        event.getClass()
                );

        if (list == null) {
            return;
        }

        for (EventListener listener
                : list) {

            listener.update(event);
        }
    }
}