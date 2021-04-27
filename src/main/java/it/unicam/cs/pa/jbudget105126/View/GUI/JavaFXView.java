package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Cotroller.ExpenseManager;
import it.unicam.cs.pa.jbudget105126.View.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class JavaFXView<T extends ExpenseManager> implements View<T> {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXView");

    private final Stage primaryStage;

    public JavaFXView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void open() {
        Parent root= null;
        logger.info("Primary stage opening...");
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/expenseManager.fxml"));
        } catch (IOException e) {
            logger.severe("Opening the primary stage error: "+ e.getMessage());
        }
        primaryStage.setTitle("Expense Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void close() {
        logger.info("Primary stage closing...");
    }
}
