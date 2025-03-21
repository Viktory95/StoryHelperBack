package com.vi.StoryHelperBack.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table("character")
public class Character {
    @Id
    @PrimaryKey
    private UUID id;
    private String name;
    private Gender gender;
    private LocalDate birthday;
    private String appearance;
    private String features;
    private String characterDescription;
    private int importanceRate;
}
