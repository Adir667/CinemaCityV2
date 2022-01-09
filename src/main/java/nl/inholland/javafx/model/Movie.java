package nl.inholland.javafx.model;

public class Movie {

    private String title;
    private int duration;
    private double price;
    private String printPrice;
    private String printDuration;

    public Movie(String title, int duration, double price) {
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.printPrice = String.valueOf(price);
        this.printDuration = String.format("%02d", duration/60) + ":" + String.format("%02d", duration%60);
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public double getPrice() {
        return price;
    }

    public String getPrintPrice () { return printPrice; }

    public String getPrintDuration() {
        return printDuration;
    }

    @Override
    public String toString() {
        return title;
    }
}
