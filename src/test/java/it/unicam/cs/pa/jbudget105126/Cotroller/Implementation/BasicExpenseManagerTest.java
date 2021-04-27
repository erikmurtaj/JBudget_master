package it.unicam.cs.pa.jbudget105126.Cotroller.Implementation;

import it.unicam.cs.pa.jbudget105126.Cotroller.ExpenseManager;
import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.*;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.*;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicExpenseManagerTest {

    private ExpenseManager controller;
    private TransactionScheduler scheduler = new TransactionScheduler();

    @BeforeEach
    void init() throws MissingInformationException, InstanceAlreadyExistsException, WrongDateRangeException {
        //state
        Currency curr= Currency.getInstance(Locale.ITALY);
        Scheduler transaction_scheduler=new TransactionScheduler();
        BudgetManager budget_manager=new BasicBudgetManager();
        ExpenseManagerState state=new BasicExpenseManagerState(curr, transaction_scheduler, budget_manager);

        Account account_test = new BasicAccount("test account", "description", AccountType.ASSET, 0);
        Account account_test2= new BasicAccount("test account 2", "description", AccountType.ASSET, 200);
        Transaction transaction_test=new BasicTransaction("Transaction test", "description", LocalDate.now(), new BasicCategory("category test"));
        //  +50
        DataCreator.getListMovements(5, MovementType.INCOME, 10).stream().forEach(movement -> transaction_test.addMovement(movement));
        //  -5
        DataCreator.getListMovements(5, MovementType.EXPENSE, 1).stream().forEach(movement -> transaction_test.addMovement(movement));

        Movement toRepeat=new BasicMovement("Internet provider", MovementType.EXPENSE, 30);
        ScheduledTransaction scheduled_transaction=new BasicScheduledTransaction("Scheduled transaction test", "description",
                new LocalDate(2020, 01, 1), new LocalDate(2020, 06, 1),
                TransactionFrequency.MONTHLY, toRepeat);

        List<ScheduledTransaction> scheduledTransactions= new ArrayList<>();
        scheduledTransactions.add(scheduled_transaction);
        scheduler.schedule(scheduledTransactions);

        account_test.addTransaction(transaction_test);
        account_test2.addScheduledTransaction(scheduled_transaction);

        state.addAccount(account_test);
        state.addAccount(account_test2);
        state.addAccount(new BasicAccount("test account 3", "description", AccountType.LIABILITY, 200));

        controller=new BasicExpenseManager(state, null);
    }

    @Test
    void calculateCurrentBalance() {
        assertEquals(-105, controller.calculateCurrentBalance());
    }

    @Test
    void getTransactionsBefore() {
    }

    @Test
    void getTransactionsAfter() {
    }

    @Test
    void getTransactionsInDate() {
    }

    @Test
    void getTransactionsByCategory() {
    }

    @Test
    void budget_report() {
    }

}