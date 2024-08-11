package com.example.matchservice.dto;

public class MatchDTO {
    private String id;
    private String teamAId;
    private String teamBId;

    // Default constructor
    public MatchDTO() {}

    // Parameterized constructor
    public MatchDTO(String id, String teamAId, String teamBId) {
        this.id = id;
        this.teamAId = teamAId;
        this.teamBId = teamBId;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(String teamAId) {
        this.teamAId = teamAId;
    }

    public String getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(String teamBId) {
        this.teamBId = teamBId;
    }
}
