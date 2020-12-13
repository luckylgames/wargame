package com.luckyl.wargame.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieceId implements Serializable {
    @NonNull String color;
    @NonNull Integer rank;
}
