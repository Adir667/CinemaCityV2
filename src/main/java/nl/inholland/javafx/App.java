package nl.inholland.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.logic.UserService;
import nl.inholland.javafx.model.User;
import nl.inholland.javafx.ui.Login;
import nl.inholland.javafx.ui.Main;

public class App extends Application {
    @Override
    public void start(Stage window) throws Exception {

        Database db = new Database();
        Login login = new Login(db);
    }
}
