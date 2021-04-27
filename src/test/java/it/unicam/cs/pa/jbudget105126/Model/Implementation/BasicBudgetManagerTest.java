package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.*;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicBudgetManagerTest {

    private BudgetManager budgetManager = new BasicBudgetManager();
    private TransactionScheduler scheduler = new TransactionScheduler();
    private List<Transaction> transactions = new ArrayList<>();
    private Budget budget_test;

    @BeforeEach
    void init() throws MissingInformationException, WrongDateRangeException {
        Transaction transaction_test=new BasicTransaction("Transaction test", "description", LocalDate.now(), new BasicCategory("category test"));
        //  +50
        DataCreator.getListMovements(5, MovementType.INCOME, 10).stream().forEach(movement -> transaction_test.addMovement(movement));
        //  -5
        DataCreator.getListMovements(5, MovementType.EXPENSE, 1).stream().forEach(movement -> transaction_test.addMovement(movement));

        Movement toRepeat=new BasicMovement("Internet provider", MovementType.EXPENSE, 30);
        ScheduledTransaction scheduledTransaction=new BasicScheduledTransaction("Scheduled transaction test", "description",
                new LocalDate(2020, 01, 1), new LocalDate(2020, 06, 1),
                TransactionFrequency.MONTHLY, toRepeat);
        List<ScheduledTransaction> scheduledTransactions= new ArrayList<>();
        scheduledTransactions.add(scheduledTransaction);
        scheduler.schedule(scheduledTransactions);

        transactions.add(transaction_test);
        transactions.add(scheduledTransaction);

        budget_test=new BasicBudget(new BasicCategory("Category test"), "Budget test", 500);
    }

    @Test
    void generate_report() {
        assertEquals(395, budgetManager.generate_report(transactions, budget_test));
    }
}