package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.*;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicAccountTest {

    private Account test_account;
    private Account test_account2;
    private Transaction transaction;
    private ScheduledTransaction scheduled_transaction;

    private TransactionScheduler scheduler = new TransactionScheduler();

    @BeforeEach
    void init() throws MissingInformationException, WrongDateRangeException {
        test_account = new BasicAccount("Bank Account", "Family bank account", AccountType.ASSET, 500);
        test_account2 = new BasicAccount("Credid card", "Family credid card", AccountType.LIABILITY, 500);

        transaction=new BasicTransaction("Transaction test", "description", LocalDate.now(), new BasicCategory("category test"));
        DataCreator.getListMovements(5, MovementType.INCOME, 10).stream().forEach(movement -> transaction.addMovement(movement));

        Movement toRepeat=new BasicMovement("Internet provider", MovementType.EXPENSE, 30);
        scheduled_transaction=new BasicScheduledTransaction("Scheduled transaction test", "description",
                LocalDate.now().minusDays(10), LocalDate.now(), new BasicCategory("category test"), TransactionFrequency.DAYLY);
        scheduled_transaction.addMovement(toRepeat);
        List<ScheduledTransaction> scheduledTransactions= new ArrayList<>();
        scheduledTransactions.add(scheduled_transaction);

        test_account.addTransaction(transaction);
        test_account2.addTransaction(transaction);
        test_account.addScheduledTransaction(scheduled_transaction);
        test_account2.addScheduledTransaction(scheduled_transaction);

        scheduler.schedule(scheduledTransactions);
    }

    @Test
    void getName() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicAccount("    ", "   ", AccountType.ASSET));
        assertThrows(MissingInformationException.class, () -> new BasicAccount("", "", AccountType.LIABILITY));
        assertEquals("Bank Account", test_account.getName());
    }

    @Test
    void getDescription() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicAccount("    ", "   ", AccountType.ASSET));
        assertThrows(MissingInformationException.class, () -> new BasicAccount("", "", AccountType.LIABILITY));
        assertEquals("Family bank account", test_account.getDescription());
    }

    @Test
    void getAccountType() {
        assertEquals(AccountType.ASSET, test_account.getAccountType());
        assertEquals(AccountType.LIABILITY, test_account2.getAccountType());
    }

    @Test
    void getBalance() {
        assertEquals(250, test_account.getBalance());
        assertEquals(-250, test_account2.getBalance());
    }

    @Test
    void getBalanceInDate() throws MissingInformationException {
        assertEquals(250, test_account.getBalanceInDate(LocalDate.now()));
        Transaction transactionTest=new BasicTransaction("Transaction test", "description", LocalDate.now().minusDays(10), new BasicCategory("category test 2"));
        DataCreator.getListMovements(5, MovementType.EXPENSE, 10).stream().forEach(movement -> transactionTest.addMovement(movement));
        test_account.addTransaction(transactionTest);
        assertEquals(200, test_account.getBalanceInDate(LocalDate.now()));
        assertEquals(150, test_account.getBalanceInDate(LocalDate.now().minusDays(10)));
        assertEquals(500, test_account.getBalanceInDate(LocalDate.now().minusDays(11)));
        assertEquals(500, test_account.getBalanceInDate(LocalDate.now().minusDays(11), new BasicCategory("category test")));
        assertEquals(200, test_account.getBalanceInDate(LocalDate.now().minusDays(9), new BasicCategory("category test")));
        assertEquals(450, test_account.getBalanceInDate(LocalDate.now(), new BasicCategory("category test 2")));

    }

    @Test
    void addTransaction() throws MissingInformationException {
        Transaction transactionTest=new BasicTransaction("test", "test", LocalDate.now(), new BasicCategory("test"));
        test_account.addTransaction(transactionTest);
        assertEquals(2, test_account.getTransactions().size());
    }

    @Test
    void getTransactions() throws MissingInformationException {

        Movement movementTest1=new BasicMovement("Payment", MovementType.INCOME, 265);
        Movement movementTest2=new BasicMovement("Tax", MovementType.EXPENSE, 5);
        Transaction transactionTest=new BasicTransaction("Transaction1", "test", LocalDate.now(), new BasicCategory("test"), movementTest1);
        Transaction transactionTest2=new BasicTransaction("Transaction2", "test", LocalDate.now().plusDays(1), new BasicCategory("test"), movementTest1);
        transactionTest.addMovement(movementTest2);
        test_account.addTransaction(transactionTest);
        test_account.addTransaction(transactionTest2);

        List<Transaction> test_list= new ArrayList<>();
        test_list.add(transaction);
        test_list.add(transactionTest);
        test_list.add(scheduled_transaction);

        Predicate<Transaction> predicate = (transaction) -> transaction.getDate().isBefore(LocalDate.now().plusDays(1));
        assertEquals(test_list, test_account.getTransactions(predicate));

        Predicate<Transaction> predicate2 = (transaction) -> transaction.getDate().isEqual(LocalDate.now().plusDays(1));
        test_list= new ArrayList<>();
        test_list.add(transactionTest2);
        assertEquals(test_list, test_account.getTransactions(predicate2));
    }

}