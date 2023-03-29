package sahachan.template.auth.security.dao;


import sahachan.template.auth.security.entity.Location;

public interface LocationDao {
    Location addLocation(Location location);
    Location updateLocation(Location location);
}
