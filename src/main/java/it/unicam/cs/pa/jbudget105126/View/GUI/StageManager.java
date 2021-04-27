package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class StageManager {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.StageManager");

    private static final String ERROR_MESSAGE="Something went wrong!";

    public void changeStage(String fxml_name){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/"+fxml_name));
        Parent root = null;
        logger.info("Opening a new stage: "+ fxml_name);
        try {
            root = loader.load();
        } catch (IOException e) {
            logger.severe("Error opening a new stage: "+ e.getMessage());
        }
        Scene scene = new Scene(root);

        Stage newWindow = new Stage();

        newWindow.setScene(scene);
        newWindow.show();
    }

    public <T extends Exception> void exceptionStage(T exception){
        logger.warning("Incorrect user input/interaction: "+ exception.getMessage());
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(ERROR_MESSAGE);
        errorAlert.setContentText(exception.getMessage());
        errorAlert.showAndWait();
    }

    public void infoStage(String message){
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }

    public void setCategories(ChoiceBox<Category> categories_choiceBox){
        List<Category> categories=JavaFXExpenseManager.controller.getCategories();
        categories.forEach(category -> categories_choiceBox.getItems().add(category));
    }

    public void setAccounts(ChoiceBox<Account> accounts_choiceBox){
        List<Account> accounts=JavaFXExpenseManager.controller.getAccounts();
        accounts.forEach(account -> accounts_choiceBox.getItems().add(account));
    }

    public void setTransactionFrequencies(ChoiceBox<TransactionFrequency> transactionFrequencies_choiceBox){
        transactionFrequencies_choiceBox.getItems().addAll(JavaFXExpenseManager.controller.getTransactionFrequency());
    }

    public void setAccountTypes(ChoiceBox<AccountType> accountTypes_choiceBox){
        accountTypes_choiceBox.getItems().addAll(JavaFXExpenseManager.controller.getAccountTypes());
    }

    public void setAccountsTable(TableView<Account> accountsTable) {
        TableColumn<Account, String> nameColumn =new TableColumn<>("Name");
        TableColumn<Account, String> descriptionColumn =new TableColumn<>("Description");
        TableColumn<Account, Double> balanceColumn =new TableColumn<>("Balance");
        TableColumn<Account, AccountType> typeColumn =new TableColumn<>("Type");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("description"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Account, AccountType>("accountType"));

        accountsTable.getColumns().addAll(nameColumn, descriptionColumn, balanceColumn, typeColumn);
    }

    public void setTransactionsTable(TableView transactionsTable){
        TableColumn<Transaction, String> nameColumn =new TableColumn<>("Name");
        TableColumn<Transaction, String> descriptionColumn =new TableColumn<>("Description");
        TableColumn<Transaction, LocalDate> dateColumn =new TableColumn<>("Date");
        TableColumn<Transaction, Double> amountColumn =new TableColumn<>("Amount");
        TableColumn<Transaction, Category> categoryColumn =new TableColumn<>("Category");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDate>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("total"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Category>("category"));

        transactionsTable.getColumns().addAll(nameColumn, descriptionColumn, dateColumn, amountColumn, categoryColumn);
    }

    public void setScheduledTransactionsTable(TableView transactionsTable){
        TableColumn<ScheduledTransaction, String> nameColumn =new TableColumn<>("Name");
        TableColumn<ScheduledTransaction, String> descriptionColumn =new TableColumn<>("Description");
        TableColumn<ScheduledTransaction, LocalDate> dateColumn =new TableColumn<>("Date");
        TableColumn<ScheduledTransaction, LocalDate> end_dateColumn =new TableColumn<>("End date");
        TableColumn<ScheduledTransaction, Double> amountColumn =new TableColumn<>("Amount");
        TableColumn<ScheduledTransaction, TransactionFrequency> frequencyTableColumn =new TableColumn<>("Frequency");
        TableColumn<ScheduledTransaction, Boolean> completedColumn =new TableColumn<>("Completed");

        nameColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, LocalDate>("date"));
        end_dateColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, LocalDate>("endDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, Double>("total"));
        frequencyTableColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, TransactionFrequency>("frequency"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, Boolean>("completed"));

        transactionsTable.getColumns().addAll(nameColumn, descriptionColumn, dateColumn, end_dateColumn,
                amountColumn, frequencyTableColumn, completedColumn);
    }

    public void setMovementsTable(TableView movementsTable){
        TableColumn<Movement, String> nameColumn =new TableColumn<>("Name");
        TableColumn<Movement, Double> valueColumn =new TableColumn<>("Value");
        TableColumn<Movement, MovementType> typeColumn =new TableColumn<>("Type");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Movement, String>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<Movement, Double>("value"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Movement, MovementType>("movementType"));

        movementsTable.getColumns().addAll(nameColumn, valueColumn, typeColumn);
    }

    public void setBudgetsTable(TableView budgetsTable){
        TableColumn<Budget, String> nameColumn =new TableColumn<>("Name");
        TableColumn<Budget, Category> categoryColumn =new TableColumn<>("Category");
        TableColumn<Budget, Double> expectedColumn =new TableColumn<>("Expected");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Budget, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Budget, Category>("category"));
        expectedColumn.setCellValueFactory(new PropertyValueFactory<Budget, Double>("expected"));

        budgetsTable.getColumns().addAll(nameColumn, categoryColumn, expectedColumn);
    }

    public void setCategoriesTable(TreeTableView categoriesTable){
        categoriesTable.getColumns().clear();
        TreeTableColumn<Category, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        categoriesTable.getColumns().add(nameColumn);
        categoriesTable.setRoot(new TreeItem(null));
        categoriesTable.setShowRoot(false);
    }

    /**
     * This method is used to restrict the dates on the date picker.
     *  Reference: https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/date-picker.htm#CCHEBIFF
     *
     * @param datePicker                the date picker to work to
     * @param minDate                   the minimun date can be selected
     * @param maxDate                   the maximum date can be selected
     */
    public void restrictDatePicker(DatePicker datePicker, java.time.LocalDate minDate, java.time.LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(java.time.LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }
}
