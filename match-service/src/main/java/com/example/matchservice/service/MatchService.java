package com.example.matchservice.service;

import com.example.matchservice.model.Match;
import com.example.matchservice.dto.MatchDTO;
import com.example.matchservice.repository.MatchRepository;
import com.example.matchservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public MatchDTO createMatch(MatchDTO matchDTO) {
        Match match = new Match();
        match.setTeamAId(matchDTO.getTeamAId());
        match.setTeamBId(matchDTO.getTeamBId());
        match = matchRepository.save(match);
        matchDTO.setId(match.getId());
        return matchDTO;
    }

    public MatchDTO updateMatch(String id, MatchDTO matchDTO) {
        Match match = matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        match.setTeamAId(matchDTO.getTeamAId());
        match.setTeamBId(matchDTO.getTeamBId());
        match = matchRepository.save(match);
        return matchDTO;
    }

    public MatchDTO getMatch(String id) {
        Match match = matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setId(match.getId());
        matchDTO.setTeamAId(match.getTeamAId());
        matchDTO.setTeamBId(match.getTeamBId());
        return matchDTO;
    }

    public void deleteMatch(String id) {
        matchRepository.deleteById(id);
    }

    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(match -> new MatchDTO(match.getId(), match.getTeamAId(), match.getTeamBId()))
                .collect(Collectors.toList());
    }
}
