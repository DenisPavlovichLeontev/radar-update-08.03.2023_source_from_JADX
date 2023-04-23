package mil.nga.geopackage.attributes;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserDao;

public class AttributesDao extends UserDao<AttributesColumn, AttributesTable, AttributesRow, AttributesCursor> {
    private final AttributesConnection attributesDb;

    public AttributesDao(String str, GeoPackageConnection geoPackageConnection, AttributesConnection attributesConnection, AttributesTable attributesTable) {
        super(str, geoPackageConnection, attributesConnection, attributesTable);
        this.attributesDb = attributesConnection;
        if (attributesTable.getContents() == null) {
            StringBuilder sb = new StringBuilder();
            Class<AttributesTable> cls = AttributesTable.class;
            sb.append("AttributesTable");
            sb.append(" ");
            sb.append(attributesTable.getTableName());
            sb.append(" has null ");
            Class<Contents> cls2 = Contents.class;
            sb.append("Contents");
            throw new GeoPackageException(sb.toString());
        }
    }

    public BoundingBox getBoundingBox() {
        throw new GeoPackageException("Bounding Box not supported for Attributes");
    }

    public AttributesRow newRow() {
        return new AttributesRow((AttributesTable) getTable());
    }

    public AttributesConnection getAttributesDb() {
        return this.attributesDb;
    }
}
