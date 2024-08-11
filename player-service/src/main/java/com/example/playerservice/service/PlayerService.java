package com.example.playerservice.service;

import com.example.playerservice.dto.PlayerDTO;
import com.example.playerservice.dto.TeamDTO;
import com.example.playerservice.model.Player;
import com.example.playerservice.repository.PlayerRepository;
import com.example.playerservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RestTemplate restTemplate;

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setTeamId(playerDTO.getTeamId());
        player = playerRepository.save(player);
        playerDTO.setId(player.getId());
        return playerDTO;
    }

    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found"));
        player.setName(playerDTO.getName());
        player.setTeamId(playerDTO.getTeamId());
        player = playerRepository.save(player);
        return playerDTO;
    }

    public PlayerDTO getPlayer(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found"));
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setTeamId(player.getTeamId());
        return playerDTO;
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> new PlayerDTO(player.getId(), player.getName(), player.getTeamId()))
                .collect(Collectors.toList());
    }

    // public List<PlayerDTO> getPlayersByTeamId(String teamId) {
    //     List<Player> players = playerRepository.findByTeamId(teamId);
    //     return players.stream().map(this::toDTO).collect(Collectors.toList());
    // }

    public List<PlayerDTO> getPlayersByTeam(String teamId) {
        return playerRepository.findByTeamId(teamId).stream()
                .map(player -> new PlayerDTO(player.getId(), player.getName(), player.getTeamId()))
                .collect(Collectors.toList());
    }

    private PlayerDTO toDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setTeamId(player.getTeamId());
        return playerDTO;
    }

    public PlayerDTO createPlayerAndUpdateTeam(PlayerDTO playerDTO) {
        // Create player
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setTeamId(playerDTO.getTeamId());
        player = playerRepository.save(player);
        playerDTO.setId(player.getId());

        // Fetch the team
        String teamUrl = "http://localhost:8081/teams/" + playerDTO.getTeamId();
        TeamDTO teamDTO = restTemplate.getForObject(teamUrl, TeamDTO.class);

        if (teamDTO == null) {
            throw new ResourceNotFoundException("Team not found");
        }

        // Update the team with new player ID
        List<Long> playerIds = teamDTO.getPlayerIds();
        if (playerIds == null) {
            playerIds = new ArrayList<>();
        } else {
            // Convert to a modifiable list if it is unmodifiable
            playerIds = new ArrayList<>(playerIds);
        }
        
        playerIds.add(playerDTO.getId());
        teamDTO.setPlayerIds(playerIds);

        restTemplate.put(teamUrl, teamDTO);

        return playerDTO;
    }


}
