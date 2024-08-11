package com.example.playerservice.dto;

public class PlayerDTO {
    private Long id;
    private String name;
    private String teamId;

    // Default constructor
    public PlayerDTO() {}

    // Parameterized constructor
    public PlayerDTO(Long id, String name, String teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
