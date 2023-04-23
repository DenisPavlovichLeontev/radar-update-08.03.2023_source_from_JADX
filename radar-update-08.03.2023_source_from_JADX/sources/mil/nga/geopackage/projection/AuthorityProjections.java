package mil.nga.geopackage.projection;

import java.util.HashMap;
import java.util.Map;

public class AuthorityProjections {
    private final String authority;
    private Map<String, Projection> projections = new HashMap();

    public AuthorityProjections(String str) {
        this.authority = str;
    }

    public String getAuthority() {
        return this.authority;
    }

    public Projection getProjection(String str) {
        return this.projections.get(str);
    }

    public void addProjection(Projection projection) {
        this.projections.put(projection.getCode(), projection);
    }

    public void clear() {
        this.projections.clear();
    }

    public void clear(long j) {
        this.projections.remove(String.valueOf(j));
    }

    public void clear(String str) {
        this.projections.remove(str);
    }
}
