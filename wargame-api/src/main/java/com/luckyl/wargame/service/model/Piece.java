package com.luckyl.wargame.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Version;

@Entity
@Data
@IdClass(PieceId.class)
public class Piece implements Comparable<Piece> {
    @Id
    private String color;

    @Id
    private Integer rank;

    @Version
    private Long version;

    @Column
    private Integer current_count;

    @Column
    private Integer max_count;

    @Override
    public int compareTo(@NonNull Piece other) {
        return other.getRank() - rank;
    }
}