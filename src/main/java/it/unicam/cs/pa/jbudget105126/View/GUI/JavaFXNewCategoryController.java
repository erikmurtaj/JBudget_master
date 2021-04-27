package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.Category;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.WrongCategoryParameterException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.management.InstanceAlreadyExistsException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXNewCategoryController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXNewCategoryController");

    private StageManager stageManager =new StageManager();

    @FXML Button addNewCategory_button;
    @FXML TextField nameCategory_value;
    @FXML ChoiceBox<Category> category_choiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setCategory_choiceBox();
    }

    /**
     * This metehod fills the AccountType choiceBox
     */
    public void setCategory_choiceBox(){
        stageManager.setCategories(category_choiceBox);
    }

    /**
     * This methos adds the new category and closes the stage when the addNewCategory_button is clicked
     * @param actionEvent
     */
    public void addNewCategory_button(javafx.event.ActionEvent actionEvent) {
        logger.finest("Adding a new category...");
        try {
            JavaFXExpenseManager.controller.addCategory(nameCategory_value.getText(), category_choiceBox.getValue());
            Stage stage = (Stage) addNewCategory_button.getScene().getWindow();
            stage.close();
        } catch (MissingInformationException | WrongCategoryParameterException | InstanceAlreadyExistsException e) {
            stageManager.exceptionStage(e);
            logger.severe("Error adding a new category: "+ e.getMessage());
        }
    }
}
