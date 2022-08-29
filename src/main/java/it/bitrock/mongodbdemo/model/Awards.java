package it.bitrock.mongodbdemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Awards {

    private Integer wins;
    private Integer nominations;
    private String text;

    @Override
    public String toString() {
        return "\nAwards: " + wins + ", " + nominations + ", " + text;
    }
}
