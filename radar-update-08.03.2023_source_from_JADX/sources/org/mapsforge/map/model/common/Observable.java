package org.mapsforge.map.model.common;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Observable implements ObservableInterface {
    private static final String OBSERVER_MUST_NOT_BE_NULL = "observer must not be null";
    private final List<Observer> observers = new CopyOnWriteArrayList();

    public final void addObserver(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException(OBSERVER_MUST_NOT_BE_NULL);
        } else if (!this.observers.contains(observer)) {
            this.observers.add(observer);
        } else {
            throw new IllegalArgumentException("observer is already registered: " + observer);
        }
    }

    public final void removeObserver(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException(OBSERVER_MUST_NOT_BE_NULL);
        } else if (this.observers.contains(observer)) {
            this.observers.remove(observer);
        } else {
            throw new IllegalArgumentException("observer is not registered: " + observer);
        }
    }

    public final void notifyObservers() {
        for (Observer onChange : this.observers) {
            onChange.onChange();
        }
    }
}
