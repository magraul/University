package me.observer;

import me.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
