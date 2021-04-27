package it.unicam.cs.pa.jbudget105126.Cotroller.Implementation;

import it.unicam.cs.pa.jbudget105126.Cotroller.ExpenseManager;
import it.unicam.cs.pa.jbudget105126.Model.*;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.*;
import it.unicam.cs.pa.jbudget105126.Model.Persistence.DataManager;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.function.Predicate;

public class BasicExpenseManager<T extends ExpenseManagerState, G extends DataManager> implements ExpenseManager {

    private final T state;
    private final G data_manager;

    public BasicExpenseManager(T state, G data_manager) {
        this.state=state;
        this.data_manager= data_manager;
    }

    /**
     * This method calculates the current balance using all accounts in the state
     *
     * @return                  the current balance
     */
    @Override
    public double calculateCurrentBalance() {
        List<Account> accounts=state.getAccounts();
        return accounts.parallelStream().mapToDouble(Account::getBalance).sum();
    }

    /**
     * This method returns the transaction that are before a date passed as parameter
     *
     * @param date              the date to compare
     * @return                  the transactions that respect that predicate
     */
    @Override
    public List<Transaction> getTransactionsBefore(LocalDate date) {
        Predicate<Transaction> predicate = (transaction) -> transaction.getDate().isBefore(date);
        return state.getTransactions(predicate);
    }

    /**
     * This method returns the transaction that are after a date passed as parameter
     * @param date              the date to compare
     * @return                  the transactions that respect that predicate
     */
    @Override
    public List<Transaction> getTransactionsAfter(LocalDate date) {
        Predicate<Transaction> predicate = (transaction) -> transaction.getDate().isAfter(date);
        return state.getTransactions(predicate);
    }

    /**
     * This method returns the transaction that are in a date passed as parameter
     * @param date              the date to compare
     * @return                  the transactions that respect that predicate
     */
    @Override
    public List<Transaction> getTransactionsInDate(LocalDate date) {
        Predicate<Transaction> predicate = (transaction) -> transaction.getDate().isEqual(date);
        return state.getTransactions(predicate);
    }

    /**
     * This method returns the transactions between two dates. It decreases the start date and increases the end date
     *      by one day to include them on the predicate.
     *
     * @param start             the starting date of the period
     * @param end               the ending date of the period
     * @return                  the list of transactions between those two dates
     */
    @Override
    public List<Transaction> getTransactionsBetween(LocalDate start, LocalDate end) {
        List<Transaction> to_return= new ArrayList<>();

        Predicate<Transaction> before = (transaction) -> transaction.getDate().isBefore(end.plusDays(1));
        Predicate<Transaction> after = (transaction) -> transaction.getDate().isAfter(start.minusDays(1));

        for(Transaction transaction : (List<Transaction>) state.getTransactions(after)){
            if(state.getTransactions(before).contains(transaction)) to_return.add(transaction);
        }
        return to_return;
    }

    /**
     * This method returns the transaction of a category passed as parameter
     * @param category          the category to compare
     * @return                  the transactions that respect that predicate
     */
    @Override
    public List<Transaction> getTransactionsByCategory(Category category) {
        Predicate<Transaction> predicate = (transaction) -> transaction.getCategories().contains(category);
        return state.getTransactions(predicate);
    }

    /**
     * This method returns all accounts
     *
     * @return                  the list of all accounts
     */
    @Override
    public List<Account> getAccounts() {
        return state.getAccounts();
    }

    /**
     * This method returns all transactions
     *
     * @return                  the list of all transactions
     */
    @Override
    public List<Transaction> getTransactions() {
        return state.getTransactions();
    }

    /**
     * This method returns all scheduled transactions
     *
     * @return                  the list of all scheduled transactions
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions() {
        return state.getScheduledTransactions();
    }

    /**
     * This method returns all movements of the transaction passed as parameter
     *
     * @param transaction       the transaction from which get the movements
     * @return                  the list of all movements
     */
    @Override
    public List<Movement> getMovements(Transaction transaction) {
        return transaction.getMovements();
    }

    /**
     * This method returns all budgets
     *
     * @return                  the list of all budgets
     */
    @Override
    public List<Budget> getBudgets() {
        return state.getBudgets();
    }

    /**
     * This method returns all categories
     *
     * @return                  the list of all categories
     */
    @Override
    public List<Category> getCategories() {
        return state.getCategories();
    }

    /**
     * This method returns the account types
     *
     * @return                  the array of account types
     */
    @Override
    public AccountType[] getAccountTypes() {
        return AccountType.values();
    }

    /**
     * This method returns the movement types
     *
     * @return                  the array of movement types
     */
    @Override
    public MovementType[] getMovementTypes() {
        return MovementType.values();
    }

    /**
     * This method returns the transaction frequencies
     *
     * @return                  the array of transaction frequencies
     */
    @Override
    public TransactionFrequency[] getTransactionFrequency() {
        return TransactionFrequency.values();
    }

    /**
     * This method adds a new account
     *
     * @param name                                  the name of the new account
     * @param description                           the description of the new account
     * @param type                                  the type of the new account
     * @param initial                               the starting amount of the new account
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws InstanceAlreadyExistsException       exception if this account already exists
     */
    @Override
    public void addAccount(String name, String description, AccountType type, double initial) throws MissingInformationException, InstanceAlreadyExistsException {
        state.addAccount(new BasicAccount(name, description, type, initial));
    }

    /**
     * This method removes the passed account
     *
     * @param account                               the account to remove
     */
    @Override
    public void removeAccount(Account account) {
        state.removeAccount(account);
    }

    /**
     * This method adds a new transaction
     *
     * @param name                                  the name of the new transaction
     * @param description                           the description of the new transaction
     * @param date                                  the date of the new transaction
     * @param category                              the category of the new transaction
     * @param account                               the account where to create the new transaction
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    @Override
    public void addTransaction(String name, String description, LocalDate date, Category category, Account account)
            throws MissingInformationException {
        state.addTransaction(account, new BasicTransaction(name, description, date, category));
    }

    /**
     * This method removes the passed transaction
     *
     * @param transaction                            the transaction to remove
     */
    @Override
    public void removeTransaction(Transaction transaction) {
        state.removeTransaction(transaction);
    }

    /**
     * This method adds a new scheduled transaction
     *
     * @param name                                  the name of the new transaction
     * @param description                           the description of the new transaction
     * @param date                                  the date of the new transaction
     * @param end_date                              the end date of the new transaction
     * @param category                              the category of the new transaction
     * @param frequency                             the frequency of the new transaction
     * @param account                               the account where to create the new transaction
     * @return                                      the scheduled transaction added
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws WrongDateRangeException              exception if the range date is negative or not valid
     */
    @Override
    public ScheduledTransaction addScheduledTransaction(String name, String description, LocalDate date, LocalDate end_date,
            Category category, TransactionFrequency frequency, Account account) throws MissingInformationException, WrongDateRangeException {
        ScheduledTransaction scheduled_transaction=new BasicScheduledTransaction(name, description, date, end_date, category, frequency);
        account.addScheduledTransaction(scheduled_transaction);
        return scheduled_transaction;
    }

    /**
     * This method removes the passed scheduled transaction
     *
     * @param scheduled_transaction                 the scheduled transaction to remove
     */
    @Override
    public void removeScheduledTransaction(ScheduledTransaction scheduled_transaction) {
        state.removeScheduledTransaction(scheduled_transaction);
    }

    /**
     * This method adds a new category
     *
     * @param name                                  the name of the new category
     * @param parent                                the parent of the new category
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws WrongCategoryParameterException      exception if the parent is not valid
     * @throws InstanceAlreadyExistsException       exception if this category already exists
     */
    @Override
    public void addCategory(String name, Category parent) throws MissingInformationException, WrongCategoryParameterException, InstanceAlreadyExistsException {
        if(parent==null)
            state.addCategory(new BasicCategory(name));
        else
            state.addCategory(new BasicCategory(name, parent));
    }

    /**
     * This method removes the passed category
     *
     * @param category                              the category to remove
     * @throws RemoveUsedCategoryException          exception if this category is used
     */
    @Override
    public void removeCategory(Category category) throws RemoveUsedCategoryException {
        state.removeCategory(category);
    }

    /**
     * This method adds a new movement
     *
     * @param name                                  the name of the new movement
     * @param value                                 the amount of the new movement
     * @param type                                  the type of the new movement
     * @param transaction                           the transaction where to add the new movement
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    @Override
    public void addMovement(String name, double value, MovementType type, Transaction transaction) throws MissingInformationException {
        transaction.addMovement(new BasicMovement(name, type, value));
    }

    /**
     * This method adds a new budget
     *
     * @param name                                  the name of the new budget
     * @param category                              the category of the new budget
     * @param expected                              the amount excpeted of the new budget
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws InstanceAlreadyExistsException       exception if this budget already exists
     */
    @Override
    public void addBudget(String name, Category category, double expected) throws MissingInformationException, InstanceAlreadyExistsException {
        state.addBudget(new BasicBudget(category, name, expected));
    }

    /**
     * This method removes the passed budget
     *
     * @param budget                                the budget to remove
     */
    @Override
    public void removeBudget(Budget budget) {
        state.removeBudget(budget);
    }

    /**
     * This method generate the report of a budget
     *
     * @param budget                                the budget to work with
     * @return                                      the difference between amount spent and amount expected
     */
    @Override
    public double generate_report(Budget budget) {
        return state.generate_report(this.getTransactionsByCategory(budget.getCategory()), budget);
    }

    /**
     * This method calls the state method for scheduled transaction scheduling
     */
    @Override
    public void schedule() {
        state.schedule();
    }

    /**
     * This method calls the data manager method to save the state of the application
     */
    @Override
    public void save() {
        data_manager.save();
    }

    /**
     * This method calls the data manager method to load the state of the application
     */
    @Override
    public void load() {
        data_manager.load();
    }

    /**
     * This method calls the state method to make a transfer between two accounts
     *
     * @param from                              the account where the transfer come
     * @param to                                the account where to transfer the money
     * @param amount                            the amount to transfer
     * @throws InsufficientBalanceException     exception if the balance of the account is less than the amount to transfer
     * @throws IllegalArgumentException         exception if the two accounts are the same
     * @throws MissingInformationException      exception of the movements created
     */
    @Override
    public void transfer(Account from, Account to, double amount) throws InsufficientBalanceException,
            IllegalArgumentException, MissingInformationException {
        state.transfer(from, to, amount);
    }

    /**
     * This method returns the trending of the total balance between two dates
     *
     * @param start                                 the starting date to make the trend
     * @param end                                   the starting date to make the trend
     * @return                                      the array representing the daily change of the total balance
     */
    @Override
    public Double[] balanceTrend(LocalDate start, LocalDate end) {
        return this.trendCreator(start, end, null);
    }

    /**
     * This method returns the trending of the total balance between two dates filtered by a category
     *
     * @param start                                 the starting date to make the trend
     * @param end                                   the starting date to make the trend
     * @param category                              the category to filter the transactions
     * @return                                      the array representing the daily change of the total balance
     */
    @Override
    public Double[] balanceTrend(LocalDate start, LocalDate end, Category category) {
        return this.trendCreator(start, end, category);
    }

    /**
     * This methods returns an array representing the daily change of the total balance
     *
     * @param start                                 the starting date to make the trend
     * @param end                                   the starting date to make the trend
     * @param category                              the category to filter the transactions
     * @return                                      the array representing the daily change of the total balance
     */
    private Double[] trendCreator(LocalDate start, LocalDate end, Category category){
        int days=Days.daysBetween(start, end).getDays()+1;
        double balance=0;
        Double[] trend = new Double[days];

        for(int i=0; i<days; i++) {
            balance=0;
            for (Account account : (List<Account>) state.getAccounts()) {
                if(category!=null)
                    balance += account.getBalanceInDate(start.plusDays(i), category);
                else
                    balance += account.getBalanceInDate(start.plusDays(i));
            }
            trend[i]=balance;
        }
        return trend;
    }

    /**
     * This method returns the currency of the state
     *
     * @return                                  the currency of the state
     */
    @Override
    public Currency getCurrency() {
        return state.getCurrency();
    }

}
