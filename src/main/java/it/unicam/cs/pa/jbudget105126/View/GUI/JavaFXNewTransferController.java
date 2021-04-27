package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.Account;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.InsufficientBalanceException;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXNewTransferController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXNewTransferController");

    private StageManager stageManager = new StageManager();

    @FXML ChoiceBox<Account> fromAccount_choice;
    @FXML ChoiceBox<Account> toAccount_choice;
    @FXML TextField amount_value;

    @FXML Button makeTransfer_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0d);
        amount_value.setTextFormatter(formatter);
        this.setAccount_choiceBox();
    }

    public void setAccount_choiceBox(){
        stageManager.setAccounts(fromAccount_choice);
        stageManager.setAccounts(toAccount_choice);
    }

    public void makeTransfer_button(ActionEvent actionEvent) {
        logger.finest("Make a new transfer...");
        try {
            JavaFXExpenseManager.controller.transfer(fromAccount_choice.getValue(), toAccount_choice.getValue(),
                    Double.parseDouble(amount_value.getText()));
            Stage stage = (Stage) makeTransfer_button.getScene().getWindow();
            stage.close();
        } catch (InsufficientBalanceException | IllegalArgumentException | MissingInformationException e) {
            stageManager.exceptionStage(e);
            logger.severe("Error making a new transfer: "+ e.getMessage());
        }
    }
}
