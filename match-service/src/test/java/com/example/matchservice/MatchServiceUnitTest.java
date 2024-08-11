package com.example.matchservice;

import com.example.matchservice.dto.MatchDTO;
import com.example.matchservice.model.Match;
import com.example.matchservice.repository.MatchRepository;
import com.example.matchservice.service.MatchService;
import com.example.matchservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceUnitTest {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @Test
    public void testCreateMatch() {
        // Arrange
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setTeamAId("teamA");
        matchDTO.setTeamBId("teamB");

        Match match = new Match();
        match.setId("1");
        match.setTeamAId(matchDTO.getTeamAId());
        match.setTeamBId(matchDTO.getTeamBId());

        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // Act
        MatchDTO createdMatch = matchService.createMatch(matchDTO);

        // Assert
        assertNotNull(createdMatch.getId());
        assertEquals("teamA", createdMatch.getTeamAId());
        assertEquals("teamB", createdMatch.getTeamBId());
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    public void testUpdateMatch() {
        // Arrange
        String matchId = "1";
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setTeamAId("teamA");
        matchDTO.setTeamBId("teamB");

        Match match = new Match();
        match.setId(matchId);
        match.setTeamAId("oldTeamA");
        match.setTeamBId("oldTeamB");

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // Act
        MatchDTO updatedMatch = matchService.updateMatch(matchId, matchDTO);

        // Assert
        assertEquals("teamA", updatedMatch.getTeamAId());
        assertEquals("teamB", updatedMatch.getTeamBId());
        verify(matchRepository, times(1)).findById(matchId);
        verify(matchRepository, times(1)).save(match);
    }

    @Test
    public void testGetMatch() {
        // Arrange
        String matchId = "1";
        Match match = new Match();
        match.setId(matchId);
        match.setTeamAId("teamA");
        match.setTeamBId("teamB");

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        MatchDTO retrievedMatch = matchService.getMatch(matchId);

        // Assert
        assertNotNull(retrievedMatch);
        assertEquals(matchId, retrievedMatch.getId());
        assertEquals("teamA", retrievedMatch.getTeamAId());
        assertEquals("teamB", retrievedMatch.getTeamBId());
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    public void testGetMatch_NotFound() {
        // Arrange
        String matchId = "1";
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> matchService.getMatch(matchId));
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    public void testDeleteMatch() {
        // Arrange
        String matchId = "1";
        doNothing().when(matchRepository).deleteById(matchId);

        // Act
        matchService.deleteMatch(matchId);

        // Assert
        verify(matchRepository, times(1)).deleteById(matchId);
    }

    @Test
    public void testGetAllMatches() {
        // Arrange
        Match match = new Match();
        match.setId("1");
        match.setTeamAId("teamA");
        match.setTeamBId("teamB");

        when(matchRepository.findAll()).thenReturn(Arrays.asList(match));

        // Act
        List<MatchDTO> matches = matchService.getAllMatches();

        // Assert
        assertEquals(1, matches.size());
        assertEquals("1", matches.get(0).getId());
        assertEquals("teamA", matches.get(0).getTeamAId());
        assertEquals("teamB", matches.get(0).getTeamBId());
        verify(matchRepository, times(1)).findAll();
    }
}
