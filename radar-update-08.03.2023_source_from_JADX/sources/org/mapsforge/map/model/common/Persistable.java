package org.mapsforge.map.model.common;

public interface Persistable {
    void init(PreferencesFacade preferencesFacade);

    void save(PreferencesFacade preferencesFacade);
}
