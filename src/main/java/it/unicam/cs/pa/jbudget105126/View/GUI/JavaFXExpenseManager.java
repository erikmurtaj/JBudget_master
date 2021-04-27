package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Cotroller.Implementation.BasicExpenseManager;
import it.unicam.cs.pa.jbudget105126.Model.BudgetManager;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.BasicBudgetManager;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.BasicExpenseManagerState;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.TransactionScheduler;
import it.unicam.cs.pa.jbudget105126.Model.Persistence.JsonDataManager;
import it.unicam.cs.pa.jbudget105126.View.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class JavaFXExpenseManager extends Application {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXExpenseManager");

    private static final String FILE_ACCOUNTS = "accounts.json";
    private static final String FILE_BUDGETS = "budget.json";
    private static final String FILE_CATEGORIES = "categories.json";

    private final static TransactionScheduler scheduler=new TransactionScheduler();
    private final static BudgetManager budget_manager=new BasicBudgetManager();
    private static BasicExpenseManagerState state = new BasicExpenseManagerState(null, scheduler, budget_manager);
    private static final JsonDataManager json_manager = new JsonDataManager(state, FILE_ACCOUNTS, FILE_BUDGETS, FILE_CATEGORIES);
    protected static BasicExpenseManager controller = new BasicExpenseManager(state, json_manager);

    /**
     * The start method for the JavaFx Application. This method loads the saved data
     *
     * @param primaryStage           the primary stage to load by the application
     */
    @Override
    public void start(Stage primaryStage){
        logger.finest("Starting the application...");
        controller.load();
        controller.schedule();
        View gui_view= new JavaFXView(primaryStage);
        try {
            gui_view.open();
        } catch (IOException e) {
            logger.severe("Application start error: "+ e.getMessage());
        }
    }

    /**
     * Stops the application and saves the state
     */
    @Override
    public void stop(){
        controller.save();
    }
}
