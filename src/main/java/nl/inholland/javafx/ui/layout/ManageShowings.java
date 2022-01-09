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
import nl.inholland.javafx.logic.RoomService;
import nl.inholland.javafx.logic.ShowingService;
import nl.inholland.javafx.model.Movie;
import nl.inholland.javafx.model.Room;
import nl.inholland.javafx.model.Showing;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class ManageShowings extends VBox {

    private Database db;
    private ShowingService showingService;
    private RoomService roomService;
    private MovieService movieService;
    private Label lblDisplayMessage = new Label();
    private Label lblEndTime = new Label();
    private ChoiceBox<Movie> cbMovies = new ChoiceBox<>();
    private ChoiceBox<Room> cbRooms = new ChoiceBox<>();
    private Button btnAddShowing = new Button("Add Showing");

    private LocalDateTime showingStartTime;
    private List<Room> rooms;


    public ManageShowings (Database db) {

        this.db = db;
        showingService = new ShowingService(db);
        roomService = new RoomService(db);
        movieService = new MovieService(db);
        rooms = roomService.getAllRooms();

        this.setPadding(new Insets(20));
        this.setSpacing(20);

        Label headline = new Label("Manage Showings");
        headline.setId("headline");

        //create tables
        VBox tablesBox = getTableViews();

        // create grid
        HBox controlPanel = getControlPanelGrid();

        // message panel
        HBox displayBox = getDisplayMessage();

        // layout.add.headline.tables.grid.display
        this.getChildren().addAll(headline, tablesBox, controlPanel, displayBox);
    }

    private VBox getTableViews() {
        VBox frame = new VBox();
        frame.setId("topFrame");

        HBox tablesBox = new HBox();
        tablesBox.setSpacing(20);
        List<Room> rooms = roomService.getAllRooms();

        List<TableView> showingTables = new ArrayList<>();
        TableView tableViewForRoom1 = new TableView();
        TableView tableViewForRoom2 = new TableView();
        showingTables.add(tableViewForRoom1);
        showingTables.add(tableViewForRoom2);

        List<ObservableList> showingLists = new ArrayList<>();
        ObservableList<Showing> showingsListInRoom1 = FXCollections.observableArrayList(showingService.getShowingsByRoom(rooms.get(0)));
        ObservableList<Showing> showingsListInRoom2 = FXCollections.observableArrayList(showingService.getShowingsByRoom(rooms.get(1)));
        showingLists.add(showingsListInRoom1);
        showingLists.add(showingsListInRoom2);

        for (int i = 0; i < rooms.size(); i++) {
            VBox showingPane = new VBox();
            Label tableHeader = new Label(rooms.get(i).getName());
            tableHeader.setId("tableHeader");

            showingTables.get(i).setMinWidth(460);
            showingTables.get(i).getSelectionModel().setCellSelectionEnabled(false);
            showingTables.get(i).getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            TableColumn startDateCol = new TableColumn("Start");
            startDateCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("printStartTime"));
            TableColumn endDateCol = new TableColumn("End");
            endDateCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("printEndTime"));
            TableColumn titleCol = new TableColumn("Title");
            titleCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("movieTitle"));
            TableColumn openSeatsCol = new TableColumn("Seats");
            openSeatsCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("availableTickets"));
            TableColumn priceCol = new TableColumn("Price");
            priceCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("ticketPrice"));
            showingTables.get(i).getColumns().addAll(startDateCol, endDateCol, titleCol, openSeatsCol, priceCol);
            showingTables.get(i).setItems(showingLists.get(i));

            showingPane.getChildren().addAll(tableHeader, showingTables.get(i));
            tablesBox.getChildren().add(showingPane);
        }

            btnAddShowing.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (cbMovies.getValue() != null) {
                        if (cbRooms.getValue() != null) {
                            if (showingStartTime != null) {
                                if (checkOverlaps(showingStartTime, cbMovies.getValue().getDuration(), cbRooms.getValue())) {
                                    Showing newShowing = new Showing(cbMovies.getValue(), cbRooms.getValue(), showingStartTime);
                                    showingService.addShowing(newShowing);

                                    ObservableList<Showing> showingsListInRoom1 = FXCollections.observableArrayList(showingService.getShowingsByRoom(rooms.get(0)));
                                    ObservableList<Showing> showingsListInRoom2 = FXCollections.observableArrayList(showingService.getShowingsByRoom(rooms.get(1)));
                                    showingTables.get(0).setItems(showingsListInRoom1);
                                    showingTables.get(1).setItems(showingsListInRoom2);
                                }
                                else {
                                    lblDisplayMessage.setText("Your showing overlaps an existing showing");
                                }
                            }
                            else {
                                lblDisplayMessage.setText("Please select a valid start time");
                            }
                        }
                        else {
                            lblDisplayMessage.setText("Please select a room");
                        }
                    }
                    else {
                        lblDisplayMessage.setText("Please select a movie");
                    }
                }
            });


        frame.getChildren().add(tablesBox);
        return frame;
    }

    private HBox getControlPanelGrid() {

        HBox hBox = new HBox();
        hBox.setId("controlPanelBox");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(20);
        gridPane.setHgap(20);

        List<Room> rooms = roomService.getAllRooms();

        //first row
        Label lblMovieTitle = new Label("Movie Title");
        GridPane.setConstraints(lblMovieTitle, 0, 0);
        for (Movie m :movieService.getAllMovies()
        ) {
            cbMovies.getItems().add(m);
        }
        GridPane.setConstraints(cbMovies, 1, 0);
        Label lblStart = new Label("Start");
        GridPane.setConstraints(lblStart, 2, 0);
        DatePicker datePicker = new DatePicker();
        GridPane.setConstraints(datePicker, 3, 0);
        TextField txtStartTime = new TextField();
        txtStartTime.setPromptText("HH:mm");
        GridPane.setConstraints(txtStartTime, 4, 0);

        //second row
        Label lblRoom = new Label("Room");
        GridPane.setConstraints(lblRoom, 0, 1);
        for (Room r : rooms
        ) {
            cbRooms.getItems().add(r);
        }
        GridPane.setConstraints(cbRooms, 1, 1);
        Label lblEnd = new Label("End");
        GridPane.setConstraints(lblEnd, 2, 1);

        //third row
        GridPane.setConstraints(lblEndTime, 3, 1);
        GridPane.setConstraints(btnAddShowing, 5, 1);

        Label lblNrOfSeats = new Label("Nr of seats");
        GridPane.setConstraints(lblNrOfSeats, 0, 2);

        Label lblCapacity = new Label();
        cbRooms.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            lblCapacity.setText(String.valueOf(newValue.getCapacity()));
        });
        GridPane.setConstraints(lblCapacity, 1, 2);
        Label lblPrice = new Label("Price");
        GridPane.setConstraints(lblPrice, 2, 2);
        Label lblMoviePrice = new Label();
        cbMovies.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            lblMoviePrice.setText(String.valueOf(newValue.getPrice()));
        });
        GridPane.setConstraints(lblMoviePrice, 3, 2);
        Button btnClear = new Button("Clear");
        GridPane.setConstraints(btnClear, 5, 2);

        gridPane.getChildren().addAll(lblMovieTitle, cbMovies, lblStart, datePicker, txtStartTime,
                lblRoom, cbRooms, lblEnd, lblEndTime, btnAddShowing,
                lblNrOfSeats, lblCapacity, lblPrice, lblMoviePrice, btnClear);


        // try to determine end time
        txtStartTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (txtStartTime.getText().length() == 5 && datePicker.getValue() != null && cbMovies.getValue() != null) {
                    try {
                        showingStartTime = LocalDateTime.parse((datePicker.getValue().toString() + "T" + txtStartTime.getText()));
                        lblEndTime.setText(showingStartTime.plusMinutes(cbMovies.getValue().getDuration()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                        lblDisplayMessage.setText("");
                    } catch (Exception e) {
                        lblEndTime.setText("");
                        lblDisplayMessage.setText("Invalid date and time");
                    }
                } else {
                    lblEndTime.setText("");
                    lblDisplayMessage.setText("");
                }
            }
        });

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cbMovies.getSelectionModel().clearSelection();
                cbRooms.getSelectionModel().clearSelection();
                datePicker.setValue(null);
                txtStartTime.setText("");
                lblCapacity.setText("");
                lblEndTime.setText("");
                lblMoviePrice.setText("");
                lblDisplayMessage.setText("");
            }
        });

        hBox.getChildren().add(gridPane);
        return hBox;
    }

    private HBox getDisplayMessage () {
        HBox hBox = new HBox();
        hBox.setId("displayMessageBox");
        lblDisplayMessage.setId("attention");
        hBox.getChildren().add(lblDisplayMessage);

        return hBox;
    }

    private boolean checkOverlaps (LocalDateTime showingStartTime, int duration, Room room) {
        //set showing duration with break gaps
        LocalDateTime newShowingStartTime = showingStartTime.minusMinutes(15);
        LocalDateTime newShowingEndTime = showingStartTime.plusMinutes((duration + 15));

        int count = 0;
        List<Showing> showingsByRoom = showingService.getShowingsByRoom(room);

        for (Showing s : showingsByRoom
        ) {
            if (s.getStartTime().isBefore(newShowingStartTime)) { // if existing show is before new show
                if (s.getEndTime().isBefore(newShowingStartTime)) { // new show has to start after existing ends
                    count++;
                }
            }
            if (s.getStartTime().isAfter(newShowingStartTime)) { // if existing show is after new show
                if (newShowingEndTime.isBefore(s.getStartTime())) { // new show has to end before existing show begins
                    count++;
                }
            }
        }
        return count == showingsByRoom.size(); //make sure new show doesn't collide with any showing already existing
    }

}
