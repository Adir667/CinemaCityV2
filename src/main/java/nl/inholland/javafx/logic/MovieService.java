package nl.inholland.javafx.logic;

import javafx.scene.chart.PieChart;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.Movie;

import java.util.List;

public class MovieService {

    private Database db;

    public MovieService (Database db) {
        this.db = db;
    }

    public List<Movie> getAllMovies () {
        return db.getMovies();
    }

    public void addMovie (Movie movie) {
        db.addMovie(movie);
    }
}
