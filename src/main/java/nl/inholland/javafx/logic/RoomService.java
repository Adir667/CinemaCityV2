package nl.inholland.javafx.logic;

import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.Room;

import java.util.List;

public class RoomService {

    private Database db;

    public RoomService (Database db) {
        this.db = db;
    }

    public List<Room> getAllRooms () {
        return db.getAllRooms();
    }
}
