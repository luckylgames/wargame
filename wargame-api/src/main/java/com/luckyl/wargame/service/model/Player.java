package com.luckyl.wargame.service.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.UUID;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private Long version;

    @Column
    private String name;

    @Column
    private String color;

    @Column
    private Integer wins;
}
