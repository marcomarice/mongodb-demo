package it.bitrock.demo.service;

import com.mongodb.client.MongoCursor;
import it.bitrock.demo.model.Movie;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MovieServiceTest {

    @Test
    void getOneMovieByOneDirectorAndNewDocumentPositiveTest() {
        Movie movie = MovieService.getOneMovieByOneDirectorAndNewDocument("William K.L. Dickson");
        Assertions.assertEquals(1, movie.getAwards().getWins());
        Assertions.assertEquals(6.2, movie.getImdb().getRating());
    }

    @Test
    void getOneMovieByOneDirectorAndNewDocumentNegativeTest() {
        Movie movie = MovieService.getOneMovieByOneDirectorAndNewDocument("William K.L. Dickson");
        Assertions.assertNotEquals(2, movie.getAwards().getWins());
        Assertions.assertNotEquals(8.8, movie.getImdb().getRating());
    }

    @Test
    void getOneMovieByOneDirectorAndFilterPositiveTest() {
        Movie movie = MovieService.getOneMovieByOneDirectorAndFilter("William K.L. Dickson");
        Assertions.assertEquals(1, movie.getAwards().getWins());
        Assertions.assertEquals(6.2, movie.getImdb().getRating());
    }

    @Test
    void getMoviesByYearAndCursorPositiveTest() {
        MongoCursor<Movie> cursor = MovieService.getMoviesByYearAndCursor(2000);
        Assertions.assertEquals(101, cursor.available());
    }

    @Test
    void getMoviesByRatingAndComplexFindPositiveTest() {
        List<Movie> movies = MovieService.getMoviesByRatingAndComplexFind(1.0, 10.0);
        Assertions.assertEquals(2, movies.size());
    }

    @Test
    void getMoviesByYearPositiveTests() {
        List<Movie> movies = MovieService.getMoviesByYear(2000);
        Assertions.assertEquals(618, movies.size());
    }

    @Test
    void getMovieByDirectorsAndGroupMoviesByRatingPositiveTest() {
        List<Document> documentsMovieAndDirectors = MovieService.getMovieByDirectorsAndGroupMoviesByRating();
        Assertions.assertEquals(1, documentsMovieAndDirectors.size());
    }

    @Test
    void getMovieByDirectorsAndGroupMoviesByRatingByDocumentPositiveTest() {
        List<Document> documentsMovieAndDirectors = MovieService.getMovieByDirectorsAndGroupMoviesByRatingByDocument();
        Assertions.assertEquals(1, documentsMovieAndDirectors.size());
    }
}
