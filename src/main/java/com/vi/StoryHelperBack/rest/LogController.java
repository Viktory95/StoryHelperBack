package com.vi.StoryHelperBack.rest;

import com.vi.StoryHelperBack.domain.Log;
import com.vi.StoryHelperBack.repository.LogRepository;
import com.vi.StoryHelperBack.service.TokenCheckerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/log")
@Slf4j
@AllArgsConstructor
public class LogController {
    public final LogRepository logRepository;
    public final TokenCheckerService tokenCheckerService;

    //create or update
    @PutMapping
    public ResponseEntity<Log> save(@RequestParam("token") String token, @RequestParam("username") String username, @RequestBody Log entity) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if(entity.getId() == null)
            entity.setId(UUID.randomUUID());

        return ResponseEntity.ok(logRepository.save(entity));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("token") String token, @RequestParam("username") String username, @PathVariable("id") UUID id) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id : " + id));

        logRepository.deleteById(id);
        return ResponseEntity.ok("Log was deleted successfully!");
    }
}
