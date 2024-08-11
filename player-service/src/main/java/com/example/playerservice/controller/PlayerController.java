package com.example.playerservice.controller;

import com.example.playerservice.model.Player;
import com.example.playerservice.dto.PlayerDTO;
import com.example.playerservice.dto.TeamDTO;
import com.example.playerservice.service.PlayerService;
import com.example.playerservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        return new ResponseEntity<>(playerService.createPlayer(playerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO playerDTO) {
        return ResponseEntity.ok(playerService.updatePlayer(id, playerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayer(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    // @GetMapping("/team")
    // public ResponseEntity<List<PlayerDTO>> getPlayersByTeamId(@RequestParam String teamId) {
    //     List<PlayerDTO> playerDTOs = playerService.getPlayersByTeamId(teamId);
    //     return ResponseEntity.ok(playerDTOs);
    // }

    @GetMapping("/team")
    public ResponseEntity<List<PlayerDTO>> getPlayersByTeam(@RequestParam(value = "teamId", required = false) String teamId) {
        List<PlayerDTO> players;
        if (teamId != null) {
            players = playerService.getPlayersByTeam(teamId);
        } else {
            players = playerService.getAllPlayers();
        }
        return ResponseEntity.ok(players);
    }


    @PostMapping("/create-and-update-team")
    public ResponseEntity<PlayerDTO> createPlayerAndUpdateTeam(@RequestBody PlayerDTO playerDTO) {
        try {
            // Create player
            PlayerDTO createdPlayer = playerService.createPlayer(playerDTO);

            if (createdPlayer == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Player creation failed");
            }

            // Fetch the team
            TeamDTO teamDTO = restTemplate.getForObject("http://team-service:8081/teams/{teamId}", TeamDTO.class, createdPlayer.getTeamId());

            if (teamDTO == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
            }

            // Update the team with new player ID
            teamDTO.getPlayerIds().add(createdPlayer.getId());
            restTemplate.put("http://team-service:8081/teams/{teamId}", teamDTO, createdPlayer.getTeamId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
        } catch (Exception e) {
            // Log detailed error information
            System.err.println("Error occurred in createPlayerAndUpdateTeam: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating player or updating team", e);
        }
    }


}