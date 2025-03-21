package com.vi.StoryHelperBack.repository;

import com.vi.StoryHelperBack.domain.View;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ViewRepository extends CassandraRepository<View, UUID> {
    List<View> findByName(String name);
}
