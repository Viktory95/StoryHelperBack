package com.vi.StoryHelperBack.repository;

import com.vi.StoryHelperBack.domain.Character;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterRepository extends CassandraRepository<Character, UUID> {
}
