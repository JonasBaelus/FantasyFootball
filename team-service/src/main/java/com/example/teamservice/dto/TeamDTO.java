package com.example.teamservice.dto;

import java.util.List;

public class TeamDTO {
    private String id;
    private String name;
    private List<Long> playerIds;

    // Default constructor
    public TeamDTO() {}

    // Parameterized constructor
    public TeamDTO(String id, String name, List<Long> playerIds) {
        this.id = id;
        this.name = name;
        this.playerIds = playerIds;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Long> playerIds) {
        this.playerIds = playerIds;
    }
}
