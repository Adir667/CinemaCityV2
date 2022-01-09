package nl.inholland.javafx.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.inholland.javafx.model.Movie;
import nl.inholland.javafx.model.Room;
import nl.inholland.javafx.model.Showing;
import nl.inholland.javafx.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {

    //lists
    private List<User> users;
    private List<Room> rooms;
    private ObservableList<Movie> movies;
    private ObservableList<Showing> showings = FXCollections.observableArrayList();

    //users
    private User admin = new User("Admin", "Admin", "Peter", User.Position.Admin);
    private User customer = new User("Cashier", "Cashier", "Jan", User.Position.Cashier);

    //movies
    private Movie noTimeToLie = new Movie("No time to lie", 125, 12.5);
    private Movie theAddamsFamily19 = new Movie("The Addams Family 19", 92, 9);

    //rooms
    private Room room1 = new Room("Room 1", 1,200);
    private Room room2 = new Room("Room 2", 2,100);

    //showings
    private Showing showing1 = new Showing(noTimeToLie, room1,
            LocalDateTime.of(2021, 10, 9, 20, 0));

    private Showing showing2 = new Showing(theAddamsFamily19, room1,
            LocalDateTime.of(2021, 10, 9, 22, 30));

    private Showing showing3 = new Showing(theAddamsFamily19, room2,
            LocalDateTime.of(2021, 10, 9, 20, 0));

    private Showing showing4 = new Showing(noTimeToLie, room2,
            LocalDateTime.of(2021, 10, 9, 22, 0));


    public Database () {
        initiate();
    }

    public void initiate () {
        this.users = new ArrayList<>();
        users.add(admin);
        users.add(customer);

        this.rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        this.movies = FXCollections.observableArrayList();
        movies.add(noTimeToLie);
        movies.add(theAddamsFamily19);

        this.showings = FXCollections.observableArrayList();
        showings.add(showing1);
        showings.add(showing2);
        showings.add(showing3);
        showings.add(showing4);
    }

    //functions
    public List<User> getUsers() {
        return users;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public ObservableList<Showing> getShowingsByRoom(Room room) {

        ObservableList<Showing> showingsInRoom = FXCollections.observableArrayList();

        for (Showing s: showings
        ) { if(s.getRoom().getName().equals(room.getName())) {
            showingsInRoom.add(s);
        }
        }
        return showingsInRoom;
    }

    public void addShowing (Showing showing) {
        showings.add(showing);
    }

    public void addMovie (Movie movie) { movies.add(movie); }
}
