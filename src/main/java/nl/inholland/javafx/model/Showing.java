package nl.inholland.javafx.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Showing {

    private Movie movie;
    private Room room;
    private int availableTickets;
    private LocalDateTime startTime;
    private String printStartTime;
    private LocalDateTime endTime;
    private String printEndTime;
    private String movieTitle;
    private double ticketPrice;

    public Showing(Movie movie, Room room, LocalDateTime startTime) {

        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(movie.getDuration());
        this.printStartTime = startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        this.printEndTime = endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        this.availableTickets = room.getCapacity();
        this.movieTitle = movie.getTitle();
        this.ticketPrice = movie.getPrice();
    }

    public void updateSoldTickets (int soldTickets) {
        this.availableTickets = room.getCapacity() - soldTickets;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getStartTime() {return startTime; }

    public String getPrintStartTime () { return printStartTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public String getPrintEndTime () { return printEndTime; }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public String getMovieTitle () { return movieTitle; }

    public Double getTicketPrice () { return ticketPrice;}

    @Override
    public String toString() {
        return "Showing{" +
                "printStartTime='" + printStartTime + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }
}
