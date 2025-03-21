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
@Table("node")
public class Node {
    @Id
    @PrimaryKey
    private UUID id;
    private String name;
    private String textt;
    private List<UUID> flags;
    private String description;
    private List<UUID> prev;
    private List<UUID> next;
}
