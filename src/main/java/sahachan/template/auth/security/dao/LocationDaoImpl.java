package sahachan.template.auth.security.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sahachan.template.auth.security.entity.Location;
import sahachan.template.auth.security.repository.LocationRepository;

@Repository
public class LocationDaoImpl implements LocationDao {
    @Autowired
    LocationRepository locationRepository;

    @Override
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(Location location) {
        return locationRepository.save(location);
    }
}
