package mil.nga.geopackage.attributes;

import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.user.UserTable;

public class AttributesTable extends UserTable<AttributesColumn> {
    private Contents contents;

    public AttributesTable(String str, List<AttributesColumn> list) {
        super(str, list);
    }

    public Contents getContents() {
        return this.contents;
    }

    public void setContents(Contents contents2) {
        this.contents = contents2;
        if (contents2 != null) {
            ContentsDataType dataType = contents2.getDataType();
            if (dataType == null || dataType != ContentsDataType.ATTRIBUTES) {
                StringBuilder sb = new StringBuilder();
                sb.append("The ");
                Class<Contents> cls = Contents.class;
                sb.append("Contents");
                sb.append(" of a ");
                sb.append("AttributesTable");
                sb.append(" must have a data type of ");
                sb.append(ContentsDataType.ATTRIBUTES.getName());
                throw new GeoPackageException(sb.toString());
            }
        }
    }
}
