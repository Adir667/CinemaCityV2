package nl.inholland.javafx.logic;

import javafx.collections.ObservableList;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.Room;
import nl.inholland.javafx.model.Showing;

public class ShowingService {

    private Database db;

    public ShowingService (Database db) {
        this.db = db;
    }

    public ObservableList<Showing> getShowingsByRoom (Room room) {
        return db.getShowingsByRoom(room);
    }

    public void addShowing (Showing showing) { db.addShowing(showing);}
}
