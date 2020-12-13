package com.luckyl.wargame.data;

import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.PieceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PieceDao extends JpaRepository<Piece, PieceId> {
    @Query("SELECT p FROM Piece p WHERE p.color = :color")
    List<Piece> getPiecesByColor(@Param("color") String color);

    @Query("SELECT p FROM Piece p WHERE p.rank = :rank")
    List<Piece> getPiecesByRank(@Param("rank") Integer rank);

    @Query("SELECT p FROM Piece p WHERE p.color = :color " +
                "AND p.current_count > 0 " +
                "AND p.current_count = p.max_count")
    List<Piece> getDepletedPieces(@Param("color") String color);

    @Query("SELECT p FROM Piece p WHERE p.color = :color " +
        "AND p.current_count < p.max_count")
    List<Piece> getRemainingPieces(@Param("color") String color);

}
