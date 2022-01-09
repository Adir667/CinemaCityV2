package nl.inholland.javafx.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.logic.RoomService;
import nl.inholland.javafx.logic.ShowingService;
import nl.inholland.javafx.model.Room;
import nl.inholland.javafx.model.Showing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dashboard extends VBox {

    private Database db;
    private RoomService roomService;
    private ShowingService showingService;
    private GridPane gridPane;
    private Showing selectedShowing;
    private Label lblDisplayMessage = new Label();
    private Label lblRoomName = new Label();
    private Label lblMovieTitle = new Label();
    private Label lblStartTime = new Label();
    private Label lblEndTime = new Label();
    private Button purchase = new Button("Purchase");
    private ChoiceBox<Integer> cbNrOfTickets = new ChoiceBox<>();
    private TextField nameInput = new TextField("");


    public Dashboard (Database db) {

        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.db = db;
        roomService = new RoomService(db);
        showingService = new ShowingService(db);

        // headline
        Label headline = new Label("Purchase Tickets");
        headline.setId("headline");

        // main frame
        VBox tablesBox = getTableViews();

        // control panel
        HBox controlPanel = getControlPanel();
        gridPane.setVisible(false);

        // message box
        HBox displayBox = getDisplayMessage();

        this.getChildren().addAll(headline, tablesBox, controlPanel, displayBox);
    }

    private VBox getTableViews() {
        VBox frame = new VBox();
        frame.setId("topFrame");

        HBox tablesBox = new HBox();
        tablesBox.setSpacing(20);
        List<Room> rooms = roomService.getAllRooms();

        TableView tableViewForRoom1 = new TableView();
        TableView tableViewForRoom2 = new TableView();
        List<TableView> showingTables = new ArrayList<>();
        showingTables.add(tableViewForRoom1);
        showingTables.add(tableViewForRoom2);

        for (int i = 0; i < rooms.size(); i++) {
            ObservableList<Showing> showingsListInRoom = FXCollections.observableArrayList(showingService.getShowingsByRoom(rooms.get(i)));
            VBox showingPane = new VBox();
            Label tableHeader = new Label(rooms.get(i).getName());
            tableHeader.setId("tableHeader");

            //TableView<Showing> showingsTableView = new TableView<>();
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
            showingTables.get(i).setItems(showingsListInRoom);
            showingPane.getChildren().addAll(tableHeader, showingTables.get(i));
            tablesBox.getChildren().add(showingPane);

            // Tableview click events
            showingTables.get(0).setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    selectedShowing = (Showing)showingTables.get(0).getSelectionModel().getSelectedItem();
                    lblRoomName.setText(selectedShowing.getRoom().getName());
                    lblMovieTitle.setText(selectedShowing.getMovieTitle());
                    lblStartTime.setText(selectedShowing.getPrintStartTime());
                    lblEndTime.setText(selectedShowing.getPrintEndTime());
                    gridPane.setVisible(true);
                }
            });

            showingTables.get(1).setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    selectedShowing = (Showing)showingTables.get(1).getSelectionModel().getSelectedItem();
                    lblRoomName.setText(selectedShowing.getRoom().getName());
                    lblMovieTitle.setText(selectedShowing.getMovieTitle());
                    lblStartTime.setText(selectedShowing.getPrintStartTime());
                    lblEndTime.setText(selectedShowing.getPrintEndTime());
                    gridPane.setVisible(true);
                }
            });

            purchase.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (cbNrOfTickets.getValue() ==0 ) {
                        lblDisplayMessage.setText("In order to complete a purchase, at lease 1 ticket has to be selected");
                    }
                    else if (nameInput.getText().isEmpty()) {
                        lblDisplayMessage.setText("Please fill in customer name");
                    }
                    else if ((cbNrOfTickets.getValue() > selectedShowing.getAvailableTickets())) {
                        lblDisplayMessage.setText("Can not sell more tickets than tickets available");
                    }
                    else {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation window");
                        alert.setHeaderText("");
                        alert.setContentText("Are you sure you want to complete the purchase?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK){
                            selectedShowing.updateSoldTickets(cbNrOfTickets.getValue());
                            //
                            showingTables.get(0).refresh();
                            showingTables.get(1).refresh();
                            gridPane.setVisible(false);
                            nameInput.setText("");
                            lblRoomName.setText("");
                            cbNrOfTickets.setValue(0);
                            lblDisplayMessage.setText("");
                        }
                    }
                }
            });
        }

        frame.getChildren().add(tablesBox);
        return frame;
    }

    private HBox getDisplayMessage () {
        HBox hBox = new HBox();
        hBox.setId("displayMessageBox");
        lblDisplayMessage.setId("attention");
        hBox.getChildren().add(lblDisplayMessage);

        return hBox;
    }

    private HBox getControlPanel() {
        HBox hBox = new HBox();
        hBox.setId("controlPanelBox");

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(20);
        gridPane.setHgap(50);

        //nodes
        Label lblRoom = new Label("Room");
        GridPane.setConstraints(lblRoom, 0, 0);
        GridPane.setConstraints(lblRoomName, 1, 0);
        Label lblMovie = new Label("Movie Title");
        GridPane.setConstraints(lblMovie, 2, 0);
        GridPane.setConstraints(lblMovieTitle, 3, 0);

        Label lblStart = new Label("Start");
        GridPane.setConstraints(lblStart, 0, 1);
        GridPane.setConstraints(lblStartTime, 1, 1);
        Label lblNrOfSeats = new Label("No of seats");
        GridPane.setConstraints(lblNrOfSeats, 2, 1);
        cbNrOfTickets.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10);
        cbNrOfTickets.setValue(0);
        GridPane.setConstraints(cbNrOfTickets, 3, 1);
        GridPane.setConstraints(purchase, 4, 1);

        Label lblEnd = new Label("End");
        GridPane.setConstraints(lblEnd, 0, 2);
        GridPane.setConstraints(lblEndTime, 1, 2);
        Label customerName = new Label("Name");
        GridPane.setConstraints(customerName, 2, 2);
        GridPane.setConstraints(nameInput, 3, 2);
        Button btnClear = new Button("Clear");
        GridPane.setConstraints(btnClear, 4, 2);

        gridPane.getChildren().addAll(lblRoom, lblRoomName, lblMovie, lblMovieTitle,
                lblStart, lblStartTime, lblNrOfSeats, cbNrOfTickets, purchase,
                lblEnd, lblEndTime, customerName, nameInput, btnClear);

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gridPane.setVisible(false);
                nameInput.setText("");
                cbNrOfTickets.setValue(0);
                lblDisplayMessage.setText("");
            }
        });

        hBox.getChildren().add(gridPane);
        return hBox;
    }

}
