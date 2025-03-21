package com.vi.StoryHelperBack.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table("log")
public class Log {
    @Id
    @PrimaryKey
    private UUID id;
    private Action action;
    private String user;
    private LocalDateTime date;
    private String tableName;
    private UUID objectId;
}
