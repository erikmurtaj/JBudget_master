package it.unicam.cs.pa.jbudget105126.Cotroller;

import it.unicam.cs.pa.jbudget105126.Model.*;

import it.unicam.cs.pa.jbudget105126.Model.Implementation.*;
import it.unicam.cs.pa.jbudget105126.Model.Persistence.DataManager;
import org.joda.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;
import java.util.Currency;
import java.util.List;

public interface ExpenseManager<T extends ExpenseManagerState, G extends DataManager> {

    double calculateCurrentBalance();

    List<Transaction> getTransactionsBefore(LocalDate date);

    List<Transaction> getTransactionsAfter(LocalDate date);

    List<Transaction> getTransactionsInDate(LocalDate date);

    List<Transaction> getTransactionsBetween(LocalDate start, LocalDate end);

    List<Transaction> getTransactionsByCategory(Category category);

    List<Account> getAccounts();

    List<Transaction> getTransactions();

    List<ScheduledTransaction> getScheduledTransactions();

    List<Movement> getMovements(Transaction transaction);

    List<Budget> getBudgets();

    AccountType[] getAccountTypes();

    MovementType[] getMovementTypes();

    TransactionFrequency[] getTransactionFrequency();

    void addAccount(String name, String description, AccountType type, double initial) throws MissingInformationException, InstanceAlreadyExistsException;

    void removeAccount(Account account);

    void addTransaction(String name, String description, LocalDate date, Category category, Account account) throws MissingInformationException;

    void removeTransaction(Transaction transaction);

    ScheduledTransaction addScheduledTransaction(String name, String description, LocalDate date, LocalDate end_date, Category category, TransactionFrequency frequency, Account account) throws MissingInformationException, WrongDateRangeException;

    void removeScheduledTransaction(ScheduledTransaction transaction);

    void addCategory(String name, Category parent) throws MissingInformationException, WrongCategoryParameterException, InstanceAlreadyExistsException;

    void removeCategory(Category category) throws RemoveUsedCategoryException;

    void addMovement(String name, double value, MovementType type, Transaction transaction) throws MissingInformationException;

    void addBudget(String name, Category category, double expected) throws MissingInformationException, InstanceAlreadyExistsException;

    void removeBudget(Budget budget);

    double generate_report(Budget budget);

    List<Category> getCategories();

    void schedule();

    void save();

    void load();

    void transfer(Account from, Account to, double amount) throws InsufficientBalanceException, MissingInformationException;

    Double[] balanceTrend(LocalDate start, LocalDate end);

    Double[] balanceTrend(LocalDate start, LocalDate end, Category category);

    Currency getCurrency();
}
