package it.unicam.cs.pa.jbudget105126.Model;

import it.unicam.cs.pa.jbudget105126.Model.Implementation.InsufficientBalanceException;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.MissingInformationException;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.RemoveUsedCategoryException;

import javax.management.InstanceAlreadyExistsException;
import java.util.Currency;
import java.util.List;
import java.util.function.Predicate;

public interface ExpenseManagerState<T extends Scheduler, G extends BudgetManager> {

    List<Account> getAccounts();

    //void addTransaction(Transaction transaction);

    List<Transaction> getTransactions();

    List<Transaction> getScheduledTransactions();

    List<Transaction> getTransactions(Predicate<Transaction> p);

    List<Category> getCategories();

    List<Budget> getBudgets();

    void addAccount(Account account) throws InstanceAlreadyExistsException;

    void removeAccount(Account account);

    void addTransaction(Account account, Transaction transaction);

    void removeTransaction(Transaction transaction);

    void addScheduledTransaction(Account account, ScheduledTransaction scheduled_transaction);

    void removeScheduledTransaction(ScheduledTransaction scheduled_transaction);

    void addAccounts(List<Account> accounts);

    void addBudget(Budget budget) throws InstanceAlreadyExistsException;

    void removeBudget(Budget budget);

    void addBudgets(List<Budget> budgets);

    void addCategory(Category category) throws InstanceAlreadyExistsException;

    void removeCategory(Category category) throws RemoveUsedCategoryException;

    void addCategories(List<Category> categories);

    void addMovement(double current_balance, Movement movement, Transaction transaction) throws InsufficientBalanceException;

    User getUser();

    Currency getCurrency();

    void setCurrency(Currency c);

    double generate_report(List<Transaction> transactionsByCategory, Budget budget);

    void schedule();

    void transfer(Account from, Account to, double amount) throws InsufficientBalanceException, MissingInformationException;
}