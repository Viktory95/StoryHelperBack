package com.vi.StoryHelperBack.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table("story")
public class Story {
    @Id
    @PrimaryKey
    private UUID id;
    private String name;
    private List<UUID> genres;
    private List<UUID> characters;
    private List<UUID> nodes;
    private UUID style;
    private UUID stView;
    private String stUser;
    private boolean isPublic;
}
