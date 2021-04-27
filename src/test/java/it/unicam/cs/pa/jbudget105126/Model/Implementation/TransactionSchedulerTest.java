package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.*;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionSchedulerTest {

    private List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
    private Scheduler transaction_scheduler= new TransactionScheduler();

    @BeforeEach
    void init() throws MissingInformationException, WrongDateRangeException {
        Movement toRepeat=new BasicMovement("Internet provider", MovementType.EXPENSE, 30);
        scheduledTransactions.add(new BasicScheduledTransaction("Scheduled transaction test", "description",
                new LocalDate(2020, 01, 1), new LocalDate(2020, 06, 1),
                TransactionFrequency.MONTHLY, toRepeat));
        scheduledTransactions.add(new BasicScheduledTransaction("Scheduled transaction test", "description",
                LocalDate.now(), LocalDate.now().plusMonths(12), TransactionFrequency.YEARLY, toRepeat));
    }

    @Test
    void schedule() {
        transaction_scheduler.schedule(scheduledTransactions);
        assertEquals(5, scheduledTransactions.get(0).getMovements().size());
        assertEquals(0, scheduledTransactions.get(1).getMovements().size());
        assertTrue(scheduledTransactions.get(0).getCompleted());
        assertFalse(scheduledTransactions.get(1).getCompleted());
    }
}