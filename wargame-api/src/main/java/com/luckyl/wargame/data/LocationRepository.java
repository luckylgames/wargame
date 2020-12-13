package com.luckyl.wargame.data;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.LocationId;
import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.Location;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationRepository {
    private final LocationDao dao;

    @Transactional
    public Location save(@NonNull Location location) {
        return dao.save(location);
    }

    @Transactional
    public void delete(@NonNull Location location) {
        dao.delete(location);
    }

    public LocationId getId(@NonNull Location location) {
        return new LocationId(location.getPiece_row(), location.getPiece_col());
    }

    public Optional<Location> getLocation(@NonNull LocationId id) { return dao.findById(id); }

    public List<Location> getLocationsByColor(@NonNull Color color) {
        return dao.getLocationsByColor(color.getRgb());
    }

    public List<Location> getLocationsByPiece(@NonNull Piece piece) {
        return dao.getLocationsByPiece(piece);
    }

    public List<Location> getLocations() {
        return dao.findAll();
    }

    public long count() {
        return dao.count();
    }
}
