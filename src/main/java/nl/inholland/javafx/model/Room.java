package nl.inholland.javafx.model;

public class Room {

    private String name;
    private int roomID;
    private int capacity;

    public Room(String name, int roomID, int capacity) {
        this.name = name;
        this.roomID = roomID;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
