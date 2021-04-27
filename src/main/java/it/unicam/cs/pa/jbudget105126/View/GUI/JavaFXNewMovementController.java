package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import it.unicam.cs.pa.jbudget105126.Model.MovementType;
import it.unicam.cs.pa.jbudget105126.Model.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXNewMovementController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXNewMovementController");

    private Transaction transaction;
    private StageManager scene_builder = new StageManager();

    @FXML Button addNewMovement_button;

    @FXML TextField nameMovement_value;
    @FXML TextField valueMovement_value;
    @FXML ChoiceBox<MovementType> typeMovement_choice;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0d);
        valueMovement_value.setTextFormatter(formatter);
        this.setMovementType_choiceBox();
    }

    /**
     * This method is used to recive the transaction from JavaFXNewTransaction
     * @param transaction
     */
    public void transferTransaction(Transaction transaction) {
        this.transaction=transaction;
    }

    /**
     * This method sets the movement type choicebox
     */
    private void setMovementType_choiceBox() {
        typeMovement_choice.getItems().addAll(JavaFXExpenseManager.controller.getMovementTypes());
    }

    /**
     * This method adds a new movement related to a transaction and the closes the stage
     * @param actionEvent
     */
    public void addNewMovement_button(ActionEvent actionEvent){
        logger.finest("Adding a new movement...");
        if(valueMovement_value.getText().isEmpty()) valueMovement_value.setText("0");
        try {
            JavaFXExpenseManager.controller.addMovement(nameMovement_value.getText(), Double.parseDouble(valueMovement_value.getText()), typeMovement_choice.getValue(), transaction);
            Stage stage = (Stage) addNewMovement_button.getScene().getWindow();
            stage.close();
        } catch (MissingInformationException e) {
            scene_builder.exceptionStage(e);
            logger.severe("Error adding a new movement: "+ e.getMessage());
        }
    }
}
