package service;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.GameService;
import com.luckyl.wargame.data.LocationRepository;
import com.luckyl.wargame.data.PieceRepository;
import com.luckyl.wargame.exception.MoveException;
import com.luckyl.wargame.service.model.Location;
import com.luckyl.wargame.service.model.LocationId;
import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.PieceId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameServiceImpl implements GameService {
    private final PieceRepository pieceRepository;
    private final LocationRepository locationRepository;

    //checked
    @Override
    public void reset() {
        pieceRepository.getPieces().forEach(this::resetPiece);
    }

    //checked
    @Override
    public void attack(@NonNull Location myLocation, @NonNull Location opponentLocation) throws MoveException {
        val myPiece = myLocation.getPiece();
        val opponent = opponentLocation.getPiece();

        // If can't locate piece throw exception
        if(myPiece == null || opponent == null)
            throw new MoveException();

        // If game is already over can't attack
        if(isGameOver())
            throw new MoveException();

        // Can't attack my team's piece
        if(myPiece.getColor().equals(opponent.getColor()))
            throw new MoveException();

        // If location where I am attacking is empty then move piece
        if(isLocationEmpty(myLocation))
            movePiece(myLocation, opponentLocation);

        // Attack enemy pieces
        else {
//            switch(attackOutcome(myPiece, opponent)) {
//                case TIE -> removePieces(myLocation, opponentLocation);
//                case WIN -> {
//                    movePiece(myLocation, opponentLocation);
//                    removePieces(myLocation);
//                }
//                case LOSE -> removePieces(myLocation);
//                default -> throw new MoveException();
//            }
        }
    }

    //checked
    @Override
    public void placePiece(@NonNull Location location, @NonNull Piece piece) {
       location.setPiece(piece);
       locationRepository.save(location);
    }

    //checked
    @Override
    public void addPiece(Piece piece) {
        if(getExistingPiece(piece).isEmpty())
            pieceRepository.save(piece);
    }

    //checked
    @Override
    public void removePiece(Piece piece) {
        val deletePiece = getExistingPiece(piece);
        if (deletePiece.isPresent())
            pieceRepository.delete(deletePiece.get());
    }

    //checked
    @Override
    public void addLocation(Location location) {
        if(getExistingLocation(location).isEmpty())
            locationRepository.save(location);
    }

    //checked
    @Override
    public void removeLocation(Location location) {
        val deleteLocation = getExistingLocation(location);
        if (deleteLocation.isPresent())
            locationRepository.delete(location);
    }

    //checked
    private Optional<Location> getExistingLocation(@NonNull Location location) {
        val id = new LocationId(location.getPiece_row(), location.getPiece_col());
        return locationRepository.getLocation(id);
    }

    //checked
    private Optional<Piece> getExistingPiece(@NonNull Piece piece) {
        val id = new PieceId(piece.getColor(), piece.getRank());
        return pieceRepository.getPiece(id);
    }

    //checked
    private void resetPiece(@NonNull Piece piece) {
        piece.setCurrent_count(0);
    }

    //checked
    @Override
    public boolean isGameOver() {
      val isRedGone = pieceRepository.getRemainingPieces(Color.RED).isEmpty();
      val isRedDownGone = pieceRepository.getRemainingPieces(Color.RED.flip()).isEmpty();
      val isBlueGone = pieceRepository.getRemainingPieces(Color.BLUE).isEmpty();
      val isBlueDownGone = pieceRepository.getRemainingPieces(Color.BLUE.flip()).isEmpty();

      return (isRedGone && isRedDownGone) || (isBlueGone && isBlueDownGone);
    }

    //checked
    private void movePiece(@NonNull Location from, @NonNull Location to) {
        val piece = from.getPiece();
        placePiece(to, piece);
    }

    //checked
    private boolean isLocationEmpty(@NonNull Location location) {
        LocationId locationId =
            new LocationId(location.getPiece_row(), location.getPiece_col());

        try {
            val testLocation = locationRepository.getLocation(locationId);
            return testLocation.isEmpty();
        }
        catch(NoSuchElementException e) {
            return true;
        }
    }

    //checked
    private void removePieces(Location... locations) {
        for(Location location : locations) {
            Piece piece = location.getPiece();
            int count = piece.getCurrent_count();
            piece.setCurrent_count(--count);
            pieceRepository.save(piece);
            removeLocation(location);
        }
    }

    //checked
    private GameState attackOutcome(Piece myPiece, Piece opponent) {
        int state = myPiece.compareTo(opponent);

//        if(state > 0) return GameState.WIN;
//        else if(state < 0 ) return GameState.LOSE;
//        return GameState.TIE;     //default
        return null;
    }
}
