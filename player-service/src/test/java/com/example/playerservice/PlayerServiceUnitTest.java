package com.example.playerservice;

import com.example.playerservice.dto.PlayerDTO;
import com.example.playerservice.dto.TeamDTO;
import com.example.playerservice.exception.ResourceNotFoundException;
import com.example.playerservice.model.Player;
import com.example.playerservice.repository.PlayerRepository;
import com.example.playerservice.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceUnitTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlayer() {
        PlayerDTO playerDTO = new PlayerDTO(null, "John Doe", "team1");
        Player savedPlayer = new Player();
        savedPlayer.setId(1L);
        savedPlayer.setName("John Doe");
        savedPlayer.setTeamId("team1");

        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        PlayerDTO result = playerService.createPlayer(playerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("team1", result.getTeamId());
    }

    @Test
    void testUpdatePlayer() {
        Player existingPlayer = new Player();
        existingPlayer.setId(1L);
        existingPlayer.setName("John Doe");
        existingPlayer.setTeamId("team1");

        PlayerDTO playerDTO = new PlayerDTO(1L, "Jane Doe", "team2");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(existingPlayer);

        PlayerDTO result = playerService.updatePlayer(1L, playerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals("team2", result.getTeamId());
    }

    @Test
    void testUpdatePlayerNotFound() {
        PlayerDTO playerDTO = new PlayerDTO(1L, "Jane Doe", "team2");

        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.updatePlayer(1L, playerDTO));
    }

    @Test
    void testGetPlayer() {
        Player player = new Player();
        player.setId(1L);
        player.setName("John Doe");
        player.setTeamId("team1");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        PlayerDTO result = playerService.getPlayer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("team1", result.getTeamId());
    }

    @Test
    void testGetPlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getPlayer(1L));
    }

    @Test
    void testDeletePlayer() {
        Player player = new Player();
        player.setId(1L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        doNothing().when(playerRepository).deleteById(1L);

        playerService.deletePlayer(1L);

        verify(playerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllPlayers() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("John Doe");
        player1.setTeamId("team1");

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Jane Doe");
        player2.setTeamId("team2");

        when(playerRepository.findAll()).thenReturn(Arrays.asList(player1, player2));

        List<PlayerDTO> result = playerService.getAllPlayers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test
    void testGetPlayersByTeam() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("John Doe");
        player1.setTeamId("team1");

        when(playerRepository.findByTeamId("team1")).thenReturn(Arrays.asList(player1));

        List<PlayerDTO> result = playerService.getPlayersByTeam("team1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testCreatePlayerAndUpdateTeam() {
        PlayerDTO playerDTO = new PlayerDTO(null, "John Doe", "team1");
        Player savedPlayer = new Player();
        savedPlayer.setId(1L);
        savedPlayer.setName("John Doe");
        savedPlayer.setTeamId("team1");

        TeamDTO teamDTO = new TeamDTO("team1", "Team A", Arrays.asList(1L));

        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);
        when(restTemplate.getForObject(anyString(), eq(TeamDTO.class))).thenReturn(teamDTO);
        doNothing().when(restTemplate).put(anyString(), any(TeamDTO.class));

        PlayerDTO result = playerService.createPlayerAndUpdateTeam(playerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("team1", result.getTeamId());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(TeamDTO.class));
        verify(restTemplate, times(1)).put(anyString(), any(TeamDTO.class));
    }

    @Test
    void testCreatePlayerAndUpdateTeamTeamNotFound() {
        PlayerDTO playerDTO = new PlayerDTO(null, "John Doe", "team1");
        Player savedPlayer = new Player();
        savedPlayer.setId(1L);
        savedPlayer.setName("John Doe");
        savedPlayer.setTeamId("team1");

        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);
        when(restTemplate.getForObject(anyString(), eq(TeamDTO.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> playerService.createPlayerAndUpdateTeam(playerDTO));

        verify(restTemplate, times(1)).getForObject(anyString(), eq(TeamDTO.class));
        verify(restTemplate, never()).put(anyString(), any(TeamDTO.class));
    }
}
