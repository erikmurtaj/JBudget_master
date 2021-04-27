package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.Category;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import javax.management.InstanceAlreadyExistsException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXNewBudgetController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXNewBudgetController");

    private StageManager stageManager =new StageManager();

    @FXML Button addNewBudget_button;

    @FXML ChoiceBox<Category> categoryBudget_value;

    @FXML TextField nameBudget_value;
    @FXML TextField expectedBudget_value;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0d);
        expectedBudget_value.setTextFormatter(formatter);
        this.setCategory_choiceBox();
    }

    /**
     * This method opens a new stage for adding a new category
     * @param actionEvent
     * @throws IOException
     */
    public void addNewCategory_clicked(ActionEvent actionEvent){
        logger.finest("Opening a new stage for adding a category...");
        stageManager.changeStage("newCategory.fxml");
    }

    /**
     * This metehod fills the AccountType choiceBox
     */
    public void setCategory_choiceBox(){
        List<Category> categories=JavaFXExpenseManager.controller.getCategories();
        categories.forEach(category -> categoryBudget_value.getItems().add(category));
    }

    /**
     * This method adds a new budget when addNewBudget_button is clicked
     * @param actionEvent
     */
    public void addNewBudget_button(ActionEvent actionEvent){
        if(expectedBudget_value.getText().isEmpty()) expectedBudget_value.setText("0");
        logger.finest("Adding a new budget...");
        try {
            JavaFXExpenseManager.controller.addBudget(nameBudget_value.getText(), categoryBudget_value.getValue(), Double.parseDouble(expectedBudget_value.getText()));
            Stage stage = (Stage) addNewBudget_button.getScene().getWindow();
            stage.close();
        } catch (MissingInformationException | InstanceAlreadyExistsException e) {
            stageManager.exceptionStage(e);
            logger.severe("Error adding a new budget: "+ e.getMessage());
        }
    }
}
