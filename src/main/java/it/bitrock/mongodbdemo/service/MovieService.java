package it.bitrock.mongodbdemo.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BucketOptions;
import com.mongodb.client.model.Facet;
import it.bitrock.mongodbdemo.controller.ConnectionController;
import it.bitrock.mongodbdemo.model.Movie;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.push;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static it.bitrock.mongodbdemo.controller.ConnectionController.closeConnection;
import static it.bitrock.mongodbdemo.controller.ConnectionController.openConnection;

public class MovieService {

    private static final String FIELD_DIRECTORS = "directors";
    private static final String FIELD_DIRECTORS_VALUE = "$directors";
    private static final String FIELD_TITLE_VALUE = "$title";

    private MovieService() {
    }

    public static Movie getOneMovieByOneDirectorAndNewDocument(String director) {
        Movie movie = getMoviesCollection(openConnection())
                .find(new Document(FIELD_DIRECTORS, director)).first();
        closeConnection(openConnection());
        return movie;
    }

    public static Movie getOneMovieByOneDirectorAndFilter(String director) {
        Movie movie = getMoviesCollection(openConnection())
                .find(eq(FIELD_DIRECTORS, director)).first();
        closeConnection(openConnection());
        return movie;
    }

    public static MongoCursor<Movie> getMoviesByYearAndCursor(Integer year) {
        MongoCursor<Movie> movies = getMoviesCollection(openConnection())
                .find(eq("year", year)).iterator();
        closeConnection(openConnection());
        return movies;
    }

    public static List<Movie> getMoviesByRatingAndOtherConditions(Double viewerRating, Double criticRating) {
        List<Movie> movies = getMoviesCollection(openConnection())
                .find(and(gte("tomatoes.viewer.rating", viewerRating),
                        lte("tomatoes.critic.rating", criticRating)))
                .projection(fields(excludeId(), include("title", FIELD_DIRECTORS)))
                .sort(descending("year"))
                .skip(2)
                .limit(100)
                .into(new ArrayList<>());
        closeConnection(openConnection());
        return movies;
    }

    public static List<Movie> getMoviesByYear(Integer year) {
        List<Movie> movies = getMoviesCollection(openConnection())
                .find(eq("year", year)).into(new ArrayList<>());
        closeConnection(openConnection());
        return movies;
    }

    public static List<Document> getMovieByDirectorsAndGroupMoviesByRating() {
        List<Bson> pipeline = new ArrayList<>();
        Bson unwindDirectors = Aggregates.unwind(FIELD_DIRECTORS_VALUE);
        Bson groupDirectors = Aggregates.group(FIELD_DIRECTORS_VALUE,
                push("movie_title", FIELD_TITLE_VALUE));
        Facet directorMovies = new Facet("director_movies", unwindDirectors, groupDirectors);

        Bson bucketMoviesRating = Aggregates.bucket(
                "$imdb.Rating",
                Arrays.asList(0.0d, 6.0d, 8.0d, 10.0d),
                new BucketOptions()
                        .defaultBucket("Other")
                        .output(sum("count", 1),
                                push("movie", FIELD_TITLE_VALUE),
                                push("year", "$year")));
        Facet groupMoviesByRating = new Facet("movies_rating", bucketMoviesRating);

        Bson facetsStage = Aggregates.facet(directorMovies, groupMoviesByRating);
        pipeline.add(facetsStage);
        return ConnectionController.getDocumentsCollection(openConnection()).aggregate(pipeline).into(new ArrayList<>());
    }

    public static List<Document> getMovieByDirectorsAndGroupMoviesByRatingByDocument() {
        return ConnectionController.getDocumentsCollection(openConnection())
                .aggregate(Arrays.asList(new Document("$facet",
                        new Document("director_movies", Arrays.asList(new Document("$limit", 50L),
                                new Document("$unwind",
                                        new Document("path", FIELD_DIRECTORS_VALUE)
                                                .append("preserveNullAndEmptyArrays", false)),
                                new Document("$group",
                                        new Document("_id", FIELD_DIRECTORS_VALUE)
                                                .append("movie_titles",
                                                        new Document("$push", FIELD_TITLE_VALUE)))))
                                .append("movies_rating", Arrays.asList(new Document("$limit", 100L),
                                        new Document("$bucket",
                                                new Document("groupBy", "$imdb.rating")
                                                        .append("boundaries", Arrays.asList(0.0d, 6.0d, 8.0d, 10.0d))
                                                        .append("output",
                                                                new Document("movies",
                                                                        new Document("$push",
                                                                                new Document("name", FIELD_TITLE_VALUE)
                                                                                        .append("year", "$year"))))))))))
                .into(new ArrayList<>());
    }

    private static MongoCollection<Movie> getMoviesCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase(ConnectionController.getDatabase())
                .getCollection(ConnectionController.getCollection(), Movie.class);
    }
}
