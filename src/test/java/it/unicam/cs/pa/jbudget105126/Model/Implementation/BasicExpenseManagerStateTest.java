package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.joda.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;
import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicExpenseManagerStateTest {

    private ExpenseManagerState state;
    private Account account_test;
    private Account account_test2;
    private Account account_test3;

    @BeforeEach
    void init() throws MissingInformationException, InstanceAlreadyExistsException {
        Currency curr= Currency.getInstance(Locale.ITALY);
        Scheduler transaction_scheduler=new TransactionScheduler();
        BudgetManager budget_manager=new BasicBudgetManager();
        this.state=new BasicExpenseManagerState(curr, transaction_scheduler, budget_manager);

        DataCreator.getListAccounts(2, AccountType.ASSET, 100).stream().forEach(account -> {
            try {
                state.addAccount(account);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            }
        });

        DataCreator.getListCategories(2).stream().forEach(category -> {
            try {
                state.addCategory(category);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            }
        });

        account_test = new BasicAccount("test account", "description", AccountType.ASSET, 0);
        account_test2 = new BasicAccount("test account 2", "description", AccountType.LIABILITY, 200);
        account_test3 = new BasicAccount("test account 3", "description", AccountType.ASSET, 100);
        Transaction transaction_test=new BasicTransaction("Transaction test", "description", LocalDate.now(), new BasicCategory("category test"));
        //  +50
        DataCreator.getListMovements(5, MovementType.INCOME, 10).stream().forEach(movement -> transaction_test.addMovement(movement));
        //  -5
        DataCreator.getListMovements(5, MovementType.EXPENSE, 1).stream().forEach(movement -> transaction_test.addMovement(movement));

        account_test.addTransaction(transaction_test);
        this.state.addAccount(account_test);
        this.state.addAccount(account_test2);
        this.state.addAccount(account_test3);
    }

    @Test
    void getAccounts() throws MissingInformationException, InstanceAlreadyExistsException {
        List<Account> accounts = DataCreator.getListAccounts(2, AccountType.ASSET, 100);
        accounts.add(account_test);
        accounts.add(account_test2);
        accounts.add(account_test3);
        assertEquals(accounts, state.getAccounts());
        this.state.addAccount(new BasicAccount("test", "test", AccountType.ASSET));
        assertThrows(InstanceAlreadyExistsException.class, () -> this.state.addAccount(new BasicAccount("test", "test", AccountType.ASSET)));
    }

    @Test
    void getCategories() throws MissingInformationException, InstanceAlreadyExistsException, WrongCategoryParameterException {
        assertEquals(DataCreator.getListCategories(2), state.getCategories());
        this.state.addCategory(new BasicCategory("test"));
        assertThrows(InstanceAlreadyExistsException.class, () -> this.state.addCategory(new BasicCategory("test")));
    }

    @Test
    void getBudgets() throws MissingInformationException, InstanceAlreadyExistsException {
        Budget budget_test = new BasicBudget(new BasicCategory("category test"), "budget test", 100);
        state.addBudget(budget_test);
        assertThrows(InstanceAlreadyExistsException.class, () -> this.state.addBudget(
                new BasicBudget(new BasicCategory("category test"), "budget test", 600)));
    }

    @Test
    void transfer() throws InsufficientBalanceException, MissingInformationException {
        assertThrows(IllegalArgumentException.class, () -> state.transfer(account_test, account_test2, 5));
        assertThrows(InsufficientBalanceException.class, () -> state.transfer(account_test3, account_test, 110));
        this.state.transfer(account_test3, account_test, 100);
        assertEquals(145, account_test.getBalance());
        assertEquals(0, account_test3.getBalance());
    }
}