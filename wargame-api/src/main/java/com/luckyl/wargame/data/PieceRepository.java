package com.luckyl.wargame.data;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.PieceId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PieceRepository {
    private final PieceDao dao;

    @Transactional
    public Piece save(@NonNull Piece piece) {
       return dao.save(piece);
    }

    @Transactional
    public void delete(@NonNull Piece piece) {
        dao.delete(piece);
    }

    public Optional<Piece> getPiece(@NonNull PieceId id) { return dao.findById(id); }

    public Optional<Piece> getPiece(@NonNull Color color, @NonNull Integer rank) {
        final PieceId id = new PieceId(color.getRgb(), rank);
        return getPiece(id);
    }

    public List<Piece> getPieces() { return dao.findAll(); }

    public List<Piece> getPiecesByColor(@NonNull Color color) {
        return dao.getPiecesByColor(color.getRgb());
    }

    public List<Piece> getPiecesByRank(@NonNull Integer rank) {
        return dao.getPiecesByRank(rank);
    }

    public List<Piece> getDepletedPieces(@NonNull Color color) {
        val colorRgb = color.getRgb();
        val flippedRgb = Color.fromRgb(colorRgb).flip().getRgb();
        val flipped = dao.getDepletedPieces(flippedRgb);
        val depleted = dao.getDepletedPieces(colorRgb);

        return Stream.of(flipped, depleted)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    public List<Piece> getRemainingPieces(@NonNull Color color) {
        val colorRgb = color.getRgb();
        val flippedRgb = Color.fromRgb(colorRgb).flip().getRgb();
        val flipped = dao.getDepletedPieces(flippedRgb);
        val remaining = dao.getRemainingPieces(colorRgb);

        return Stream.of(flipped, remaining)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    public long count() {
        return dao.count();
    }
}
