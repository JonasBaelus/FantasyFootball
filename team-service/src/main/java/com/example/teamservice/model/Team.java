package com.example.teamservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Document(collection = "teams")
@Data
public class Team {
    @Id
    private String id;
    private String name;
    private List<Long> playerIds;
}
