package nl.inholland.javafx.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.logic.UserService;
import nl.inholland.javafx.model.User;

public class Login {

    private Database db;
    private UserService userService;

    public Login(Database db) {

        this.db = db;
        userService = new UserService(db);
        Stage stage = new Stage();
        stage.setWidth(120);
        stage.setWidth(340);
        stage.setTitle("Cinema City v2.0 - Login");

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));

        GridPane gridPane = new GridPane();
        gridPane.setVgap(12);
        gridPane.setHgap(16);

        //Name
        Label lblUsername = new Label("Username: ");
        GridPane.setConstraints(lblUsername, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        // Password
        Label lblPassword = new Label("Password: ");
        GridPane.setConstraints(lblPassword, 0, 1);
        PasswordField passwordField = new PasswordField();
        GridPane.setConstraints(passwordField, 1, 1);

        //Display
        Label lblDisplay = new Label();
        lblDisplay.setId("attention");

        // Button
        Button btnLogin = new Button("Log in");
        GridPane.setConstraints(btnLogin, 0, 3);

        gridPane.getChildren().addAll(lblUsername, lblPassword, usernameInput, passwordField, btnLogin);

        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (usernameInput.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    lblDisplay.setText("Please fill in both username and password fields");
                }
                else {
                    User u = userService.validateCredentials(usernameInput.getText(), passwordField.getText());
                    if (u != null) {
                        stage.close();
                        Main main = new Main(db, u);
                    } else {
                        lblDisplay.setText("Bad credentials");
                    }
                }
            }
        });

        // Layout
        layout.getChildren().addAll(gridPane, lblDisplay);
        layout.setStyle("-fx-background-color: #00325a;");

        Scene loginScene = new Scene(layout);
        loginScene.getStylesheets().add("style.css");
        stage.setScene(loginScene);
        stage.show();
    }
}
