package com.vi.StoryHelperBack.repository;

import com.vi.StoryHelperBack.domain.Node;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NodeRepository extends CassandraRepository<Node, UUID> {
}
