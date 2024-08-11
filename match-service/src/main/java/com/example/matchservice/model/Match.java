package com.example.matchservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "matches")
@Data
public class Match {
    @Id
    private String id;
    private String teamAId;
    private String teamBId;
}
