package com.luckyl.wargame.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationId implements Serializable {
    @NonNull Integer piece_row;
    @NonNull Integer piece_col;
}
