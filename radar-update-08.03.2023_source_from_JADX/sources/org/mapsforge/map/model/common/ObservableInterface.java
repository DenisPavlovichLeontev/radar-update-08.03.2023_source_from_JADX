package org.mapsforge.map.model.common;

public interface ObservableInterface {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);
}
