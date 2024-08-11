package com.example.teamservice.service;

import com.example.teamservice.model.Team;
import com.example.teamservice.dto.TeamDTO;
import com.example.teamservice.repository.TeamRepository;
import com.example.teamservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = new Team();
        team.setName(teamDTO.getName());
        team.setPlayerIds(teamDTO.getPlayerIds());
        team = teamRepository.save(team);
        teamDTO.setId(team.getId());
        return teamDTO;
    }

    public TeamDTO updateTeam(String id, TeamDTO teamDTO) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        team.setName(teamDTO.getName());
        team.setPlayerIds(teamDTO.getPlayerIds());
        team = teamRepository.save(team);
        return teamDTO;
    }

    public TeamDTO getTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setPlayerIds(team.getPlayerIds());
        return teamDTO;
    }

    public void deleteTeam(String id) {
        teamRepository.deleteById(id);
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamDTO(team.getId(), team.getName(), team.getPlayerIds()))
                .collect(Collectors.toList());
    }
}
