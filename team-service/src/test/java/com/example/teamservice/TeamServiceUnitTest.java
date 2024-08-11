package com.example.teamservice;

import com.example.teamservice.dto.TeamDTO;
import com.example.teamservice.model.Team;
import com.example.teamservice.repository.TeamRepository;
import com.example.teamservice.service.TeamService;
import com.example.teamservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceUnitTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Test
    public void testCreateTeam() {
        // Arrange
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Test Team");
        teamDTO.setPlayerIds(Arrays.asList(1L, 2L, 3L));

        Team team = new Team();
        team.setId("1");
        team.setName(teamDTO.getName());
        team.setPlayerIds(teamDTO.getPlayerIds());

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // Act
        TeamDTO createdTeam = teamService.createTeam(teamDTO);

        // Assert
        assertEquals("1", createdTeam.getId());
        assertEquals("Test Team", createdTeam.getName());
        assertEquals(Arrays.asList(1L, 2L, 3L), createdTeam.getPlayerIds());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void testUpdateTeam() {
        // Arrange
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId("1"); // Ensure ID is set if needed
        teamDTO.setName("Updated Team");
        teamDTO.setPlayerIds(Arrays.asList(4L, 5L, 6L));

        Team existingTeam = new Team();
        existingTeam.setId("1");
        existingTeam.setName("Old Team");
        existingTeam.setPlayerIds(Arrays.asList(1L, 2L, 3L));

        // Simulate the updated team saved to the repository
        Team updatedTeam = new Team();
        updatedTeam.setId("1");
        updatedTeam.setName("Updated Team");
        updatedTeam.setPlayerIds(Arrays.asList(4L, 5L, 6L));

        when(teamRepository.findById("1")).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(updatedTeam);

        // Act
        TeamDTO returnedTeamDTO = teamService.updateTeam("1", teamDTO);

        // Debugging output
        System.out.println("Returned TeamDTO ID: " + returnedTeamDTO.getId());
        System.out.println("Expected TeamDTO ID: " + teamDTO.getId());

        // Assert
        assertEquals("1", returnedTeamDTO.getId(), "The team ID should be '1'");
        assertEquals("Updated Team", returnedTeamDTO.getName(), "The team name should be 'Updated Team'");
        assertEquals(Arrays.asList(4L, 5L, 6L), returnedTeamDTO.getPlayerIds(), "The player IDs should match");

        verify(teamRepository, times(1)).findById("1");
        verify(teamRepository, times(1)).save(any(Team.class));
    }


    @Test
    public void testGetTeam() {
        // Arrange
        Team team = new Team();
        team.setId("1");
        team.setName("Test Team");
        team.setPlayerIds(Arrays.asList(1L, 2L, 3L));

        when(teamRepository.findById("1")).thenReturn(Optional.of(team));

        // Act
        TeamDTO teamDTO = teamService.getTeam("1");

        // Assert
        assertEquals("1", teamDTO.getId());
        assertEquals("Test Team", teamDTO.getName());
        assertEquals(Arrays.asList(1L, 2L, 3L), teamDTO.getPlayerIds());
        verify(teamRepository, times(1)).findById("1");
    }

    @Test
    public void testGetTeam_NotFound() {
        // Arrange
        when(teamRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.getTeam("1"));
        verify(teamRepository, times(1)).findById("1");
    }

    @Test
    public void testDeleteTeam() {
        // Arrange
        doNothing().when(teamRepository).deleteById("1");

        // Act
        teamService.deleteTeam("1");

        // Assert
        verify(teamRepository, times(1)).deleteById("1");
    }

    @Test
    public void testGetAllTeams() {
        // Arrange
        Team team = new Team();
        team.setId("1");
        team.setName("Test Team");
        team.setPlayerIds(Arrays.asList(1L, 2L, 3L));

        when(teamRepository.findAll()).thenReturn(Arrays.asList(team));

        // Act
        List<TeamDTO> teams = teamService.getAllTeams();

        // Assert
        assertEquals(1, teams.size());
        assertEquals("1", teams.get(0).getId());
        assertEquals("Test Team", teams.get(0).getName());
        assertEquals(Arrays.asList(1L, 2L, 3L), teams.get(0).getPlayerIds());
        verify(teamRepository, times(1)).findAll();
    }
}
