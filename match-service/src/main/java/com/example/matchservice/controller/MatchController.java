package com.example.matchservice.controller;

import com.example.matchservice.model.Match;
import com.example.matchservice.dto.MatchDTO;
import com.example.matchservice.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO matchDTO) {
        return new ResponseEntity<>(matchService.createMatch(matchDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchDTO> updateMatch(@PathVariable String id, @RequestBody MatchDTO matchDTO) {
        return ResponseEntity.ok(matchService.updateMatch(id, matchDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatch(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getMatch(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable String id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        List<MatchDTO> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }
}