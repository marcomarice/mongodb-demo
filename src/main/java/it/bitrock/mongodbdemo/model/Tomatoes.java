package it.bitrock.mongodbdemo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Tomatoes {
    private Viewer viewer;
    private Integer fresh;
    private Critic critic;
    private Integer rotten;
    private LocalDateTime lastUpdated;

    @Override
    public String toString() {
        return "\nTomatoes: " + viewer + ", " + fresh + ", " + critic + ", " + rotten + ", " + lastUpdated;
    }
}
