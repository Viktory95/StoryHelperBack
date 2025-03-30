package com.vi.StoryHelperBack.rest;

import com.vi.StoryHelperBack.domain.Action;
import com.vi.StoryHelperBack.domain.Flag;
import com.vi.StoryHelperBack.domain.Log;
import com.vi.StoryHelperBack.repository.FlagRepository;
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
@RequestMapping("/api/v1/flag")
@Slf4j
@AllArgsConstructor
public class FlagController {
    public final FlagRepository flagRepository;
    public final TokenCheckerService tokenCheckerService;
    private KafkaTemplate<String, Log> kafkaTemplate;

    @Value("${spring.topics.logs-topic}")
    private String kafkaTopicName;

    //create or update
    @PutMapping
    public ResponseEntity<Flag> save(@RequestParam("token") String token, @RequestParam("username") String username, @RequestBody Flag entity) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (entity.getId() == null)
            entity.setId(UUID.randomUUID());

        boolean exists = flagRepository.findById(entity.getId()).isPresent();
        Flag result = flagRepository.save(entity);

        kafkaTemplate.send(kafkaTopicName, new Log(UUID.randomUUID(), exists ? Action.CREATE_FLAG : Action.EDIT_FLAG, username, LocalDateTime.now(), "flag", result.getId()));

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("token") String token, @RequestParam("username") String username, @PathVariable("id") UUID id) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        flagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flag not found with id : " + id));
        flagRepository.deleteById(id);

        kafkaTemplate.send(kafkaTopicName, new Log(UUID.randomUUID(), Action.DELETE_FLAG, username, LocalDateTime.now(), "flag", id));

        return ResponseEntity.ok("Flag was deleted successfully!");
    }
}
