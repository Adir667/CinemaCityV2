package nl.inholland.javafx.ui;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.User;
import nl.inholland.javafx.ui.layout.Dashboard;
import nl.inholland.javafx.ui.layout.ManageMovies;
import nl.inholland.javafx.ui.layout.ManageShowings;


public class Main {

    private Database db;
    private User user;
    private Stage stage;

    public Main(Database db, User user) {

        this.db = db;
        this.user = user;

        stage = new Stage();
        stage.setHeight(740);
        stage.setWidth(1120);

        showDashboard();
    }

    public HBox navBar() {

        HBox hbox = new HBox();
        hbox.setId("navbar");
        javafx.scene.control.MenuBar menuBar = new javafx.scene.control.MenuBar();
        Menu adminMenu = new Menu("Admin");
        Menu helpMenu = new Menu("Help");
        Menu logoutMenu = new Menu("Logout");

        MenuItem dashboard = new MenuItem ("Dashboard");
        MenuItem manageShowings = new MenuItem ("Manage Showings");
        MenuItem manageMovies = new MenuItem ("Manage Movies");
        MenuItem aboutItem = new MenuItem ("About");
        MenuItem logoutItem = new MenuItem ("Logout");
        adminMenu.getItems().addAll(dashboard, manageShowings, manageMovies);
        helpMenu.getItems().add(aboutItem);
        logoutMenu.getItems().add(logoutItem);

        if (user.getPosition() == User.Position.Admin) {
            menuBar.getMenus().add(adminMenu);
        }

        menuBar.getMenus().addAll(helpMenu, logoutMenu);
        hbox.getChildren().addAll(menuBar);

        logoutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Log out");
                alert.setHeaderText("");
                alert.setContentText("You are about to log out, continue?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    stage.close();
                    Login login = new Login(db);
                }
            }
        });

        dashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showDashboard();
            }
        });

        manageShowings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                manageShowings();
            }
        });

        manageMovies.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                manageMovies();
            }
        });

        return hbox;
    }

    public void showDashboard () {
        stage.setTitle("Cinema City v2.0 [Dashboard] - Logged in as: " + user.toString());

        Dashboard dashboard = new Dashboard(db);
        VBox layout = new VBox();
        layout.getChildren().addAll(navBar(), dashboard);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void manageShowings () {
        VBox layout = new VBox();
        stage.setTitle("Cinema City v2.0 [Manage Showings] - Logged in as: " + user.toString());

        VBox contentBox = new ManageShowings(db);
        layout.getChildren().addAll(navBar(), contentBox);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void manageMovies () {
        VBox layout = new VBox();
        stage.setTitle("Cinema City v2.0 [Manage Movies] - Logged in as: " + user.toString());

        VBox contentBox = new ManageMovies(db);
        layout.getChildren().addAll(navBar(), contentBox);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }
}
