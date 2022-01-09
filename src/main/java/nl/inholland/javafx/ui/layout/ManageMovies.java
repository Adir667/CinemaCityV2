package nl.inholland.javafx.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.logic.MovieService;
import nl.inholland.javafx.model.Movie;

public class ManageMovies extends VBox {

    private Database db;
    private MovieService movieService;
    private Label lblDisplayMessage = new Label();
    private ObservableList<Movie> movies;
    private TableView<Movie> moviesTableView;

    public ManageMovies (Database db) {

        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.db = db;
        movieService = new MovieService(db);
        this.movies = FXCollections.observableArrayList(movieService.getAllMovies());

        //create headline
        Label headline = new Label("Manage Movies");
        headline.setId("headline");

        //create tables
        VBox tablesBox = getTableView();

        // create grid
        HBox controlPanel = getControlPanelGrid();

        // message panel
        HBox displayBox = getDisplayMessage();

        // layout.add.headline.tables.grid.display
        this.getChildren().addAll(headline, tablesBox, controlPanel, displayBox);

    }

    private VBox getTableView() {
        VBox frame = new VBox();
        frame.setId("topFrame");

        moviesTableView = new TableView<>();
        moviesTableView.setMinWidth(250);
        moviesTableView.getSelectionModel().setCellSelectionEnabled(false);
        moviesTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        TableColumn durationCol = new TableColumn("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("printDuration"));
        TableColumn priceCol = new TableColumn("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("printPrice"));

        moviesTableView.getColumns().addAll(titleCol, durationCol, priceCol);
        moviesTableView.setItems(movies);

        frame.getChildren().add(moviesTableView);
        return frame;
    }

    private HBox getControlPanelGrid() {

        HBox hBox = new HBox();
        hBox.setId("controlPanelBox");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(20);
        gridPane.setHgap(20);

        //nodes
        //first row
        Label lblTitle = new Label("Title");
        GridPane.setConstraints(lblTitle, 0, 0);
        TextField txtTitle = new TextField();
        GridPane.setConstraints(txtTitle, 1, 0);

        //second row
        Label lblPrice = new Label("Ticket price");
        GridPane.setConstraints(lblPrice, 0, 1);
        TextField txtPrice = new TextField();
        GridPane.setConstraints(txtPrice, 1, 1);

        //third row
        Label lblDuration = new Label("Running time");
        GridPane.setConstraints(lblDuration, 0, 2);
        TextField txtDuration = new TextField();
        txtDuration.setPromptText("HH:mm");
        GridPane.setConstraints(txtDuration, 1, 2);

        Button btnAddMovie = new Button("Add movie");
        GridPane.setConstraints(btnAddMovie, 0, 3);

        gridPane.getChildren().addAll(lblTitle, txtTitle, lblPrice, txtPrice, lblDuration, txtDuration, btnAddMovie);

        btnAddMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (txtTitle.getText().isEmpty() || txtPrice.getText().isEmpty() || txtDuration.getText().isEmpty()) {
                    lblDisplayMessage.setText("Please fill in all fields in order to add a movie");
                } else {
                    int duration = movieDurationToMin(txtDuration.getText());
                    try {
                        if (duration > 0) {
                            double price = Double.parseDouble(txtPrice.getText());
                            Movie movie = new Movie(txtTitle.getText(), duration, price);
                            movieService.addMovie(movie);
                            ObservableList<Movie> newMovies = FXCollections.observableArrayList(movieService.getAllMovies());
                            moviesTableView.setItems(newMovies);

                            txtDuration.setText("");
                            txtPrice.setText("");
                            txtTitle.setText("");
                            lblDisplayMessage.setText("");
                        } else {
                            lblDisplayMessage.setText("Invalid duration");
                        }
                    } catch (Exception e) {
                        lblDisplayMessage.setText("Wrong formatting of price");
                    }
                }
            }
        });

        hBox.getChildren().add(gridPane);
        return hBox;
    }

    private int movieDurationToMin (String HHmm) {

        String[] parts = HHmm.split(":");
        String hours = parts[0];
        String minutes = parts[1];

        int convertHours;
        int convertMinutes;
        try {
            convertHours = Integer.parseInt(hours);
            convertMinutes = Integer.parseInt(minutes);
            if (convertHours < 0 || convertMinutes < 0 || convertMinutes > 59) {
                lblDisplayMessage.setText("Invalid duration");
                return 0;
            }
            else {
                return (convertHours*60 + convertMinutes%60);
            }
        }
        catch (Exception e) {
            lblDisplayMessage.setText("Wrong formatting of duration, please use HH:mm");
        }
        return 0;
    }

    private HBox getDisplayMessage() {
        HBox hBox = new HBox();
        hBox.setId("displayMessageBox");
        lblDisplayMessage.setId("attention");
        hBox.getChildren().add(lblDisplayMessage);

        return hBox;
    }

}
