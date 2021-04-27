package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.*;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.WrongDateRangeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXNewTransactionController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXNewTransactionController");

    private StageManager stageManager = new StageManager();

    @FXML Button addNewTransaction_button;

    @FXML TextField nameTransaction_value;
    @FXML TextField descriptionTransaction_value;
    @FXML DatePicker dateTransaction_value;
    @FXML DatePicker endDateTransaction_value;
    @FXML ChoiceBox<Category> categoryTransaction_value;
    @FXML ChoiceBox<Account> accountTransaction_value;
    @FXML ChoiceBox<TransactionFrequency> frequencyTransaction_value;
    @FXML Text endDate_text;
    @FXML Text frequency_text;
    @FXML ToggleButton scheduled_transaction_toggle;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setCategory_choiceBox();
        this.setAccount_choiceBox();
        this.setFrequency_choiceBox();
        this.setScheduledTransaction_properties();
        java.time.LocalDate maxDate = java.time.LocalDate.now();
        stageManager.restrictDatePicker(dateTransaction_value, java.time.LocalDate.MIN, maxDate);
    }

    public void addNewCategory_clicked(ActionEvent actionEvent) throws IOException {
        logger.finest("Opening a new stage for adding a category...");
        stageManager.changeStage("newCategory.fxml");
    }

    public void setCategory_choiceBox(){
        stageManager.setCategories(categoryTransaction_value);
    }

    public void setAccount_choiceBox(){
        stageManager.setAccounts(accountTransaction_value);
    }

    public void setFrequency_choiceBox(){
        stageManager.setTransactionFrequencies(frequencyTransaction_value);
    }

    public void setScheduledTransaction_properties(){
        this.endDate_text.visibleProperty().setValue(scheduled_transaction_toggle.isSelected());
        this.frequency_text.visibleProperty().setValue(scheduled_transaction_toggle.isSelected());
        this.endDateTransaction_value.visibleProperty().setValue(scheduled_transaction_toggle.isSelected());
        this.frequencyTransaction_value.visibleProperty().setValue(scheduled_transaction_toggle.isSelected());
    }

    public void addNewTransaction_button(ActionEvent actionEvent){
        LocalDate date = null;
        if(dateTransaction_value.getValue()!=null) date=new LocalDate(dateTransaction_value.getValue().getYear(),
                dateTransaction_value.getValue().getMonthValue(), dateTransaction_value.getValue().getDayOfMonth());

        try {
            if (!scheduled_transaction_toggle.isSelected()) {
                logger.finest("Adding a new Transaction...");
                JavaFXExpenseManager.controller.addTransaction(nameTransaction_value.getText(), descriptionTransaction_value.getText(),
                        date, categoryTransaction_value.getValue(), accountTransaction_value.getValue());
            } else {
                logger.finest("Adding a new Scheduled Transaction...");
                LocalDate end_date = null;
                if(endDateTransaction_value.getValue()!=null) end_date = new LocalDate(endDateTransaction_value.getValue().getYear(),
                        endDateTransaction_value.getValue().getMonthValue(), endDateTransaction_value.getValue().getDayOfMonth());
                logger.finest("Opening a new stage to add the movement to repeat...");
                this.addMovement_to_repeat(JavaFXExpenseManager.controller.addScheduledTransaction(nameTransaction_value.getText(),
                        descriptionTransaction_value.getText(), date, end_date, categoryTransaction_value.getValue(),
                        frequencyTransaction_value.getValue(), accountTransaction_value.getValue()));
            }
            Stage stage = (Stage) addNewTransaction_button.getScene().getWindow();
            stage.close();
        } catch (MissingInformationException | WrongDateRangeException e){
            stageManager.exceptionStage(e);
            logger.severe("Error adding a new Transaction: "+ e.getMessage());
        }
    }

    /**
     * This method enables the field of a scheduled transaction when the related toogle is pressed
     * @param actionEvent
     */
    public void scheduled_transaction_toggle(ActionEvent actionEvent) {
        this.setScheduledTransaction_properties();
    }

    public void addMovement_to_repeat(ScheduledTransaction transaction){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/newMovement.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            logger.severe("Error opening the new movement stage: "+e.getMessage());
        }

        JavaFXNewMovementController movementController = loader.getController();

        movementController.transferTransaction(transaction);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
