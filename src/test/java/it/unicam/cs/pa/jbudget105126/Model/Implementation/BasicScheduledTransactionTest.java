package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.Movement;
import it.unicam.cs.pa.jbudget105126.Model.MovementType;
import it.unicam.cs.pa.jbudget105126.Model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105126.Model.TransactionFrequency;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicScheduledTransactionTest {

    private ScheduledTransaction transaction;
    private ScheduledTransaction transaction2;

    private TransactionScheduler scheduler = new TransactionScheduler();

    @BeforeEach
    void init() throws MissingInformationException, WrongDateRangeException {
        Movement toRepeat=new BasicMovement("Internet provider", MovementType.EXPENSE, 30);
        transaction=new BasicScheduledTransaction("Scheduled transaction test", "description",
                new LocalDate(2020, 01, 1), new LocalDate(2020, 06, 1),
                TransactionFrequency.MONTHLY, toRepeat);
        transaction2=new BasicScheduledTransaction("Scheduled transaction test2", "description",
                LocalDate.now(), LocalDate.now().plusMonths(12), TransactionFrequency.YEARLY, toRepeat);

        List<ScheduledTransaction> scheduledTransactions= new ArrayList<>();
        scheduledTransactions.add(transaction);
        scheduledTransactions.add(transaction2);
        scheduler.schedule(scheduledTransactions);
    }

    @Test
    void getName() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicScheduledTransaction("    ", "   ",
                LocalDate.now(), LocalDate.now().plusDays(1), TransactionFrequency.MONTHLY, new BasicMovement("test", MovementType.INCOME, 0)));
        assertThrows(MissingInformationException.class, () -> new BasicScheduledTransaction("", "",
                LocalDate.now(), LocalDate.now().plusDays(1), TransactionFrequency.MONTHLY, new BasicMovement("test", MovementType.INCOME, 0)));
        assertEquals("Scheduled transaction test", transaction.getName());
        assertEquals("Scheduled transaction test2", transaction2.getName());
    }

    @Test
    void getDescription() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicScheduledTransaction("    ", "   ",
                LocalDate.now(), LocalDate.now().plusDays(1), TransactionFrequency.MONTHLY, new BasicMovement("test", MovementType.INCOME, 0)));
        assertThrows(MissingInformationException.class, () -> new BasicScheduledTransaction("", "",
                LocalDate.now(), LocalDate.now().plusDays(1), TransactionFrequency.MONTHLY, new BasicMovement("test", MovementType.INCOME, 0)));
        assertEquals("description", transaction.getDescription());
        assertEquals("description", transaction2.getDescription());
    }

    @Test
    void rangeDate(){
        assertThrows(WrongDateRangeException.class, () -> new BasicScheduledTransaction("test", "test",
                LocalDate.now(), LocalDate.now(), TransactionFrequency.MONTHLY,
                new BasicMovement("test", MovementType.INCOME, 0)));

        assertThrows(WrongDateRangeException.class, () -> new BasicScheduledTransaction("test", "test",
                LocalDate.now(), LocalDate.now().minusDays(1), TransactionFrequency.MONTHLY,
                new BasicMovement("test", MovementType.INCOME, 0)));
    }

    @Test
    void isCompleted() {
        assertTrue(transaction.getCompleted());
        assertFalse(transaction2.getCompleted());
    }

    @Test
    void getFrequency() {
        assertEquals(TransactionFrequency.MONTHLY, transaction.getFrequency());
        assertEquals(TransactionFrequency.YEARLY, transaction2.getFrequency());
    }

    @Test
    void getTotal() {
        assertEquals(-150, transaction.getTotal());
        assertEquals(0, transaction2.getTotal());
    }
}