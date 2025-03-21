package com.vi.StoryHelperBack.rest;

import com.vi.StoryHelperBack.domain.View;
import com.vi.StoryHelperBack.repository.ViewRepository;
import com.vi.StoryHelperBack.service.TokenCheckerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/view")
@Slf4j
@AllArgsConstructor
public class ViewController {
    public final ViewRepository viewRepository;
    public final TokenCheckerService tokenCheckerService;

    //create or update
    @PutMapping
    public ResponseEntity<View> save(@RequestParam("token") String token, @RequestParam("username") String username, @RequestBody View entity) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(viewRepository.save(entity));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("token") String token, @RequestParam("username") String username, @PathVariable("id") UUID id) {
        boolean tokenIsValid = tokenCheckerService.checkToken(token, username);
        if (!tokenIsValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        viewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("View not found with id : " + id));

        viewRepository.deleteById(id);
        return ResponseEntity.ok("View was deleted successfully!");
    }
}
