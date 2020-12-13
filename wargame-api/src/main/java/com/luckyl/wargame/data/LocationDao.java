package com.luckyl.wargame.data;

import com.luckyl.wargame.service.model.LocationId;
import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationDao extends JpaRepository<Location, LocationId> {
    @Query("SELECT l FROM Location l WHERE l.piece.color = :color")
    List<Location> getLocationsByColor(@Param("color") String color);

    @Query("SELECT l FROM Location l WHERE l.piece = :piece")
    List<Location> getLocationsByPiece(@Param("piece") Piece piece);
}
