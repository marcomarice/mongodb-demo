package it.bitrock.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Critic {
    private Double rating;
    private Integer numReviews;
    private Integer meter;

    @Override
    public String toString() {
        return "\nCritic: " + rating + ", " + numReviews + ", " + meter;
    }
}
