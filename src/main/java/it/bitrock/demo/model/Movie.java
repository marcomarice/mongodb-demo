package it.bitrock.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class Movie {

    @BsonProperty("_id")
    private ObjectId id;
    private String title;
    private Integer year;
    private List<String> directors;
    private Awards awards;
    private Imdb imdb;
    private Tomatoes tomatoes;

    @Override
    public String toString() {
        return id + ", " + "\"" + title + "\" (" + year + "), " + directors + ", " + awards + ", " + imdb + ", " + tomatoes;
    }
}
