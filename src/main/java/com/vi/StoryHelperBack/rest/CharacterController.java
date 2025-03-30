package com.vi.StoryHelperBack.rest;

import com.vi.StoryHelperBack.domain.Action;
import com.vi.StoryHelperBack.domain.Character;
import com.vi.StoryHelperBack.domain.Log;
import com.vi.StoryHelperBack.repository.CharacterRepository;
import com.vi.StoryHelperBack.service.TokenCheckerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/character")
@Slf4j
@AllArgsConstructor
public class CharacterController {
    public final CharacterRepository characterRepository;
    public final TokenCheckerService tokenCheckerService;
    private KafkaTemplate<String, Log> kafkaTemplate;

    @Value("${spring.topics.logs-topic}")
    private String kafkaTopicName;

    //create or update
    @PutMapping
    public ResponseEntity<Character> save(@RequestParam("token") String token, @RequestParam("username") String username, @RequestBody Character entity) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (entity.getId() == null)
            entity.setId(UUID.randomUUID());

        boolean exists = characterRepository.findById(entity.getId()).isPresent();
        Character result = characterRepository.save(entity);

        kafkaTemplate.send(kafkaTopicName, new Log(UUID.randomUUID(), exists ? Action.CREATE_CHARACTER : Action.EDIT_CHARACTER, username, LocalDateTime.now(), "character", result.getId()));

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("token") String token, @RequestParam("username") String username, @PathVariable("id") UUID id) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id : " + id));
        characterRepository.deleteById(id);

        kafkaTemplate.send(kafkaTopicName, new Log(UUID.randomUUID(), Action.DELETE_CHARACTER, username, LocalDateTime.now(), "character", id));

        return ResponseEntity.ok("Character was deleted successfully!");
    }
}
