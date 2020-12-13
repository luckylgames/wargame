package com.luckyl.wargame;

import com.luckyl.wargame.exception.MoveException;
import com.luckyl.wargame.service.model.LocationId;
import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.Location;

public interface GameService {
    public void reset();
    public void attack(Location myLocation, Location opponentLocation) throws MoveException;
    public void placePiece(Location location, Piece piece);
    public void addPiece(Piece piece);
    public void removePiece(Piece piece);
    public void addLocation(Location location);
    public void removeLocation(Location location);
    public boolean isGameOver();
}
