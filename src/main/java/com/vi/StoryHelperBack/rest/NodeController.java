package com.vi.StoryHelperBack.rest;

import com.vi.StoryHelperBack.domain.Action;
import com.vi.StoryHelperBack.domain.Log;
import com.vi.StoryHelperBack.domain.Node;
import com.vi.StoryHelperBack.repository.NodeRepository;
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
@RequestMapping("/api/v1/node")
@Slf4j
@AllArgsConstructor
public class NodeController {
    public final NodeRepository nodeRepository;
    public final TokenCheckerService tokenCheckerService;
    private KafkaTemplate<String, Log> kafkaTemplate;

    @Value("${spring.topics.logs-topic}")
    private String kafkaTopicName;

    //create or update
    @PutMapping
    public ResponseEntity<Node> save(@RequestParam("token") String token, @RequestParam("username") String username, @RequestBody Node entity) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (entity.getId() == null)
            entity.setId(UUID.randomUUID());

        boolean exists = nodeRepository.findById(entity.getId()).isPresent();
        Node result = nodeRepository.save(entity);

        kafkaTemplate.send(kafkaTopicName, new Log(UUID.randomUUID(), exists ? Action.CREATE_NODE : Action.EDIT_NODE, username, LocalDateTime.now(), "node", result.getId()));

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("token") String token, @RequestParam("username") String username, @PathVariable("id") UUID id) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Node not found with id : " + id));
        nodeRepository.deleteById(id);

        kafkaTemplate.send(kafkaTopicName, new Log(UUID.randomUUID(), Action.DELETE_NODE, username, LocalDateTime.now(), "node", id));

        return ResponseEntity.ok("Node was deleted successfully!");
    }
}
