package com.vi.StoryHelperBack.repository;

import com.vi.StoryHelperBack.domain.Log;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends CassandraRepository<Log, UUID> {
}
