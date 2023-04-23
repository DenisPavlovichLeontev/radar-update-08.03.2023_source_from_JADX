package com.j256.ormlite.misc;

import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.p006db.DatabaseType;
import java.lang.reflect.Field;
import java.sql.SQLException;

public interface JavaxPersistenceConfigurer {
    DatabaseFieldConfig createFieldConfig(DatabaseType databaseType, Field field) throws SQLException;

    String getEntityName(Class<?> cls);
}
