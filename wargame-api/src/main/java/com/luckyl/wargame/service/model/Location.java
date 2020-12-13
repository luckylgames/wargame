package com.luckyl.wargame.service.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@IdClass(LocationId.class)
public class Location {
    @Id
    private Integer piece_row;

    @Id
    private Integer piece_col;

    @Version
    private Long version;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Piece piece;
}
