package com.vi.StoryHelperBack.rest;

import com.vi.StoryHelperBack.domain.Action;
import com.vi.StoryHelperBack.domain.Log;
import com.vi.StoryHelperBack.domain.Story;
import com.vi.StoryHelperBack.repository.LogRepository;
import com.vi.StoryHelperBack.repository.StoryRepository;
import com.vi.StoryHelperBack.service.TokenCheckerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/story")
@Slf4j
@AllArgsConstructor
public class StoryController {
    public final StoryRepository storyRepository;
    public final LogRepository logRepository;
    public final TokenCheckerService tokenCheckerService;

    //create or update
    @PutMapping
    public ResponseEntity<Story> save(@RequestParam("token") String token, @RequestParam("username") String username, @RequestBody Story entity) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if(entity.getId() == null)
            entity.setId(UUID.randomUUID());

        boolean exists = storyRepository.findById(entity.getId()).isPresent();
        Story result = storyRepository.save(entity);

        if (exists)
            logRepository.save(new Log(UUID.randomUUID(), Action.CREATE_STORY, username, LocalDateTime.now(), "story", result.getId()));
        else
            logRepository.save(new Log(UUID.randomUUID(), Action.EDIT_STORY, username, LocalDateTime.now(), "story", result.getId()));

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("token") String token, @RequestParam("username") String username, @PathVariable("id") UUID id) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            throw new RuntimeException("Token is not valid.");

        storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found with id : " + id));
        storyRepository.deleteById(id);

        logRepository.save(new Log(UUID.randomUUID(), Action.DELETE_STORY, username, LocalDateTime.now(), "story", id));

        return ResponseEntity.ok("Story was deleted successfully!");
    }
}
