package it.bitrock.mongodbdemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Viewer {
    private Double rating;
    private Integer numReviews;
    private Integer meter;

    @Override
    public String toString() {
        return "\nViewer: " + rating + ", " + numReviews + ", " + meter;
    }
}
