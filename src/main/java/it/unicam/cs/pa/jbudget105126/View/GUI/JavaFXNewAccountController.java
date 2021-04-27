package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.AccountType;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import javax.management.InstanceAlreadyExistsException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXNewAccountController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXNewAccountController");

    private StageManager stageManager =new StageManager();

    @FXML ChoiceBox<AccountType> typeAccount_choice;

    @FXML Button addNewAccount_button;

    @FXML TextField nameAccount_value;
    @FXML TextField descriptionAccount_value;
    @FXML TextField initialAccount_value;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0d);
        initialAccount_value.setTextFormatter(formatter);
        this.setAccountType_choiceBox();
    }

    /**
     * This methos adds the new account and closes the stage when the addNewAccount_button is clicked
     * @param actionEvent
     */
    public void addNewAccount_button(ActionEvent actionEvent) {
        if(initialAccount_value.getText().isEmpty()) initialAccount_value.setText("0");
        logger.finest("Adding a new account...");
        try{
            JavaFXExpenseManager.controller.addAccount(nameAccount_value.getText(), descriptionAccount_value.getText(), typeAccount_choice.getValue(), Double.parseDouble(initialAccount_value.getText()));
            Stage stage = (Stage) addNewAccount_button.getScene().getWindow();
            stage.close();
        }
        catch (MissingInformationException | InstanceAlreadyExistsException e){
            stageManager.exceptionStage(e);
            logger.severe("Error adding a new account");
        }
    }

    /**
     * This metehod fills the AccountType choiceBox
     */
    public void setAccountType_choiceBox(){
        stageManager.setAccountTypes(typeAccount_choice);
    }

}
