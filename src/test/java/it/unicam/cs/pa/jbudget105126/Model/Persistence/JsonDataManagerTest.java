package it.unicam.cs.pa.jbudget105126.Model.Persistence;

import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.AccountType;
import it.unicam.cs.pa.jbudget105126.Model.BudgetManager;
import it.unicam.cs.pa.jbudget105126.Model.ExpenseManagerState;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.BasicBudgetManager;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.BasicExpenseManagerState;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.TransactionScheduler;
import it.unicam.cs.pa.jbudget105126.Model.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class JsonDataManagerTest {

    private static final String FILE_ACCOUNTS = "test/accounts.json";
    private static final String FILE_BUDGETS = "test/budget.json";
    private static final String FILE_CATEGORIES = "test/categories.json";

    private JsonDataManager json_manager;

    private ExpenseManagerState state;

    @BeforeEach
    void init(){
        Currency curr= Currency.getInstance(Locale.ITALY);
        Scheduler transaction_scheduler=new TransactionScheduler();
        BudgetManager budget_manager=new BasicBudgetManager();
        this.state=new BasicExpenseManagerState(curr, transaction_scheduler, budget_manager);

        json_manager = new JsonDataManager(state, FILE_ACCOUNTS, FILE_BUDGETS, FILE_CATEGORIES);
    }


    @Test
    void save() throws MissingInformationException {
        DataCreator.getListAccounts(100, AccountType.ASSET, 10).stream().forEach(account -> {
            try {
                state.addAccount(account);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            }
        });
        DataCreator.getListBudgets(50).stream().forEach(budget -> {
            try {
                state.addBudget(budget);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            }
        });
        DataCreator.getListCategories(50).stream().forEach(category -> {
            try {
                state.addCategory(category);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            }
        });

        json_manager.save();
    }

    @Test
    void load() throws MissingInformationException {
        json_manager.load();
        assertEquals(DataCreator.getListAccounts(100, AccountType.ASSET, 10), state.getAccounts());
        assertEquals(DataCreator.getListBudgets(50), state.getBudgets());
        assertEquals(DataCreator.getListCategories(50), state.getCategories());
    }
}