package it.bitrock.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Imdb {
    private Double rating;
    private Integer votes;
    private Long id;

    @Override
    public String toString() {
        return "\nIMDB: " + rating + ", " + votes + ", " + id;
    }
}
