package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Cotroller.ExpenseManager;
import it.unicam.cs.pa.jbudget105126.Model.*;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.RemoveUsedCategoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXController<T extends ExpenseManager> implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXController");

    private final StageManager stageManager =new StageManager();

    @FXML private Text current_balance_text;
    @FXML private Text reportBudget_text;
    @FXML private Text report_text;

    @FXML private TableView<Account> accountsTable;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableView<ScheduledTransaction> scheduled_transactionsTable;
    @FXML private TableView<Movement> movementsTable;
    @FXML private TableView<Budget> budgetsTable;
    @FXML private TreeTableView<Category> categoriesTable;

    @FXML private Button removeAccount_button;
    @FXML private Button removeTransaction_button;
    @FXML private Button removeScheduledTransaction_button;
    @FXML private Button removeBudget_button;
    @FXML private Button addMovement_button;
    @FXML private Button removeCategory_button;

    @FXML private VBox accounts_vbox;
    @FXML private VBox transactions_vbox;
    @FXML private VBox budgets_vbox;
    @FXML private VBox categeories_vbox;

    /**
     * This method initializes the main stage. It initializes the different tables and the current balance text
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing all data and tables...");
        JavaFXExpenseManager.controller.balanceTrend(new LocalDate(2020,06,12),
                new LocalDate(2020,06,14));
        this.accountsTable_initialize();
        this.transactionsTable_initialize();
        this.categoriesTable_initialize();
        this.scheduled_transactionsTable_initialize();
        this.movementsTable_initialize();
        this.budgetsTable_initialize();
        this.current_balance_text.setText(String.valueOf(JavaFXExpenseManager.controller.calculateCurrentBalance())+" "+
                JavaFXExpenseManager.controller.getCurrency().getCurrencyCode());

        this.accounts_vbox.visibleProperty().setValue(true);
        this.budgets_vbox.visibleProperty().setValue(false);
        this.transactions_vbox.visibleProperty().setValue(false);
        this.movementsTable.visibleProperty().setValue(false);
        this.reportBudget_text.visibleProperty().setValue(false);
        this.addMovement_button.visibleProperty().setValue(false);
        this.report_text.visibleProperty().setValue(false);
        this.categeories_vbox.visibleProperty().setValue(false);
    }

    /**
     * This method refreshes the different tables and the current balance text
     */
    @FXML
    private void refresh(){
        logger.info("Refreshing data and tables...");
        JavaFXExpenseManager.controller.schedule();
        this.accountsTable.refresh();
        this.accountsTable.setItems(this.getAccounts());
        this.transactionsTable.refresh();
        this.transactionsTable.setItems(this.getTransactions());
        this.transactionsTable.refresh();
        this.scheduled_transactionsTable.setItems(this.getScheduledTransaction());
        this.budgetsTable.refresh();
        this.budgetsTable.setItems(this.getBudgets());
        this.categoriesTable_initialize();
        this.current_balance_text.setText(String.valueOf(JavaFXExpenseManager.controller.calculateCurrentBalance()));
    }

    public void about(ActionEvent actionEvent) {
        stageManager.infoStage("JBUDGET: This is a project for the exam of Advanced Programming at the Bacheleror Degree in Computer Science at University of Camerino.");
    }

    @FXML
    public void addAccount_clicked(){
        logger.info("New Account button clicked");
        stageManager.changeStage("newAccount.fxml");
    }

    /**
     *  This function initializes the accounts table
     */
    public void accountsTable_initialize(){
        stageManager.setAccountsTable(accountsTable);
        this.accountsTable.setItems(this.getAccounts());
    }

    /**
     *  This function initializes the transactions table
     */
    public void transactionsTable_initialize(){
        stageManager.setTransactionsTable(transactionsTable);
        this.transactionsTable.setItems(this.getTransactions());
    }

    /**
     *  This function initializes the scheduled transactions table
     */
    public void scheduled_transactionsTable_initialize(){
        stageManager.setScheduledTransactionsTable(scheduled_transactionsTable);
        this.scheduled_transactionsTable.setItems(this.getScheduledTransaction());
    }

    /**
     *  This function initializes the movements table
     */
    public void movementsTable_initialize(){
        stageManager.setMovementsTable(movementsTable);
    }

    /**
     *  This function initializes the budgets table
     */
    public void budgetsTable_initialize(){
        stageManager.setBudgetsTable(budgetsTable);
        this.budgetsTable.setItems(this.getBudgets());
    }

    /**
     *  This function initializes the categories table
     */
    public void categoriesTable_initialize(){
        stageManager.setCategoriesTable(categoriesTable);

        List<Category> categories_parent = new ArrayList<>();
        List<Category> categories_children = new ArrayList<>();
        for(Category category : (List<Category>) JavaFXExpenseManager.controller.getCategories()){
            if(category.getParent()==null)
                categories_parent.add(category);
            else
                categories_children.add(category);
        }

        for(Category parent : categories_parent){
            TreeItem category_parent = new TreeItem(parent);
            for(Category children : categories_children){
                if(children.getParent().equals(parent)) {
                    TreeItem category_children = new TreeItem(children);
                    category_parent.getChildren().add(category_children);
                }
            }
            categoriesTable.getRoot().getChildren().add(category_parent);
        }
    }

    /**
     * This method will return an ObservableList of Account objects
     * @return ObservableList<Account>
     */
    public ObservableList<Account> getAccounts()
    {
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        accounts.addAll(JavaFXExpenseManager.controller.getAccounts());
        return accounts;
    }

    /**
     * This method will return an ObservableList of Transaction objects
     * @return ObservableList<Transaction>
     */
    private ObservableList<Transaction> getTransactions()
    {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        transactions.addAll(JavaFXExpenseManager.controller.getTransactions());
        return transactions;
    }

    /**
     * This method will return an ObservableList of ScheduledTransaction objects
     * @return ObservableList<ScheduledTransaction>
     */
    private ObservableList<ScheduledTransaction> getScheduledTransaction()
    {
        ObservableList<ScheduledTransaction> scheduled_transactions = FXCollections.observableArrayList();
        scheduled_transactions.addAll(JavaFXExpenseManager.controller.getScheduledTransactions());
        return scheduled_transactions;
    }

    /**
     * This method will return an ObservableList of Movement objects related to the
     *  transaction passed as parameter
     * @return ObservableList<Movement>
     */
    private ObservableList<Movement> getMovements(Transaction transaction)
    {
        ObservableList<Movement> movements = FXCollections.observableArrayList();
        movements.addAll(JavaFXExpenseManager.controller.getMovements(transaction));
        return movements;
    }

    /**
     * This method will return an ObservableList of Budget objects
     * @return
     */
    private ObservableList<Budget> getBudgets()
    {
        ObservableList<Budget> budgets = FXCollections.observableArrayList();
        budgets.addAll(JavaFXExpenseManager.controller.getBudgets());
        return budgets;
    }

    /**
     * This method will enable the remove Account button once a row in the table is selected
     */
    public void userClickedOnAccountsTable()
    {
        this.removeAccount_button.setDisable(false);
    }

    /**
     * This method will remove the selected row(s) from the table
     * @param actionEvent
     */
    public void removeAccount_clicked(ActionEvent actionEvent) {
        logger.info("Remove Account button clicked");
        Account selectedItem = accountsTable.getSelectionModel().getSelectedItem();
        JavaFXExpenseManager.controller.removeAccount(selectedItem);
        accountsTable.getItems().remove(selectedItem);
        this.refresh();
    }

    private void setVisibilitiesToAll(boolean value){
        this.accounts_vbox.visibleProperty().setValue(value);
        this.budgets_vbox.visibleProperty().setValue(value);
        this.transactions_vbox.visibleProperty().setValue(value);
        this.categeories_vbox.visibleProperty().setValue(value);
        this.movementsTable.visibleProperty().setValue(value);
        this.addMovement_button.visibleProperty().setValue(value);
        this.reportBudget_text.visibleProperty().setValue(value);
        this.report_text.visibleProperty().setValue(value);
    }

    public void accounts_button_menu(ActionEvent actionEvent) {
        this.setVisibilitiesToAll(false);
        this.accounts_vbox.visibleProperty().setValue(true);
    }

    public void transaction_button_menu(ActionEvent actionEvent) {
        this.setVisibilitiesToAll(false);
        this.transactions_vbox.visibleProperty().setValue(true);
        this.movementsTable.visibleProperty().setValue(true);
        this.addMovement_button.visibleProperty().setValue(true);
    }

    public void categories_button_menu(ActionEvent actionEvent) {
        this.setVisibilitiesToAll(false);
        this.categeories_vbox.visibleProperty().setValue(true);
    }

    public void budgets_button_menu(ActionEvent actionEvent) {
        this.setVisibilitiesToAll(false);
        this.budgets_vbox.visibleProperty().setValue(true);
        this.reportBudget_text.visibleProperty().setValue(true);
        this.report_text.visibleProperty().setValue(true);
    }

    public void stats_button_menu(ActionEvent actionEvent) {
        stageManager.changeStage("statsController.fxml");
    }

    /**
     * This method opens a new stage for adding a new transaction
     */
    public void addTransaction_clicked(ActionEvent actionEvent) throws IOException {
        logger.info("New Transaction button clicked");
        stageManager.changeStage("newTransaction.fxml");
    }

    /**
     * This method will enable the table view of the movements related to a selected
     *  transaction on the table
     * @param mouseEvent
     */
    public void userClickedOnTransactionTable(MouseEvent mouseEvent) {
        if(transactionsTable.getSelectionModel().getSelectedItem() == null) return;
        Transaction selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
        this.movementsTable_fill(selectedItem);
        this.removeTransaction_button.setDisable(false);
    }

    /**
     * This method will enable the table view of the movements related to a selected
     *  scheduled transaction on the table
     * @param mouseEvent
     */
    public void userClickedOnScheduledTransactionTable(MouseEvent mouseEvent) {
        if(scheduled_transactionsTable.getSelectionModel().getSelectedItem() == null) return;
        Transaction selectedItem = scheduled_transactionsTable.getSelectionModel().getSelectedItem();
        this.movementsTable_fill(selectedItem);
        this.removeScheduledTransaction_button.setDisable(false);
    }

    /**
     * This method will enable the view of a report related to a selected budget on the table
     * @param mouseEvent
     */
    public void userClickedOnBudgetsTable(MouseEvent mouseEvent) {
        if(budgetsTable.getSelectionModel().getSelectedItem() == null) return;
        Budget selectedItem = budgetsTable.getSelectionModel().getSelectedItem();
        this.reportBudget_text.setText(String.valueOf(JavaFXExpenseManager.controller.generate_report(selectedItem)));
        this.report_text.visibleProperty().setValue(true);
        this.removeBudget_button.setDisable(false);
    }

    /**
     * This method will enable the view of a report related to a selected category on the table
     * @param mouseEvent
     */
    public void userClickedOnCategoriesTable(MouseEvent mouseEvent){
        this.removeCategory_button.setDisable(false);
    }

    /**
     * This method fills the movements table with the information about a transaction passed as input
     * @param selectedItem
     */
    private void movementsTable_fill(Transaction selectedItem) {
        this.movementsTable.setItems(this.getMovements(selectedItem));
    }

    /**
     * This method opens a new stage for adding a new movement related a selected transaction on the transaction table
     *
     * @param actionEvent
     */
    public void addMovement_clicked(ActionEvent actionEvent){
        logger.info("New Movement button clicked");
        Transaction transactionSelected =  transactionsTable.getSelectionModel().getSelectedItem();
        if(transactionSelected==null) return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/newMovement.fxml"));
        Parent root = null;
        logger.finest("Opening the new movement stage...");
        try {
            root = loader.load();
        } catch (IOException e) {
            logger.severe("Opening the new movement stage error: "+e.getMessage());
        }
        JavaFXNewMovementController movementController = loader.getController();

        movementController.transferTransaction(transactionSelected);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This method opens a new stage for adding a new account
     */
    public void addBudget_clicked(ActionEvent actionEvent) throws IOException {
        logger.info("New Budget button clicked");
        stageManager.changeStage("newBudget.fxml");
    }

    public void removeBudget_clicked(ActionEvent actionEvent) {
        logger.info("Remove Budget button clicked");
        Budget selectedItem = budgetsTable.getSelectionModel().getSelectedItem();
        JavaFXExpenseManager.controller.removeBudget(selectedItem);
        this.refresh();
    }

    public void transfer_button_clicked(ActionEvent actionEvent) {
        logger.info("Transfer button clicked");
        stageManager.changeStage("newTransfer.fxml");
    }

    public void removeTransaction_clicked(ActionEvent actionEvent) {
        logger.info("Remove Transaction button clicked");
        if(transactionsTable.getSelectionModel().getSelectedItem() == null) return;
        Transaction selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
        JavaFXExpenseManager.controller.removeTransaction(selectedItem);
        this.refresh();
    }

    public void removeScheduledTransaction_clicked(ActionEvent actionEvent) {
        logger.info("Remove Scheduled Transaction button clicked");
        if(scheduled_transactionsTable.getSelectionModel().getSelectedItem() == null) return;
        ScheduledTransaction selectedItem = scheduled_transactionsTable.getSelectionModel().getSelectedItem();
        JavaFXExpenseManager.controller.removeScheduledTransaction(selectedItem);
        this.refresh();
    }

    public void addCategory_clicked(ActionEvent actionEvent) {
        logger.info("New Category button clicked");
        stageManager.changeStage("newCategory.fxml");
    }

    public void removeCategory_clicked(ActionEvent actionEvent){
        logger.info("Remove Category button clicked");
        if(categoriesTable.getSelectionModel().getSelectedItem() == null) return;
        Category selectedItem = categoriesTable.getSelectionModel().getSelectedItem().getValue();
        try {
            JavaFXExpenseManager.controller.removeCategory(selectedItem);
        } catch (RemoveUsedCategoryException e) {
            stageManager.exceptionStage(e);
        }
        this.refresh();
    }
}