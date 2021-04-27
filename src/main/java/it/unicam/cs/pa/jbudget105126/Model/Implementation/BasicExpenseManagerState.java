package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.*;
import org.joda.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BasicExpenseManagerState<T extends Scheduler, G extends BudgetManager> implements ExpenseManagerState {

    private T transaction_scheduler;
    private G budget_manager;
    private Currency currency;
    private List<Account> accounts=new ArrayList<>();
    private List<Category> categories=new ArrayList<>();
    private List<Budget> budgets=new ArrayList<>();
    private User user;

    /**
     * Constructor method
     *
     * @param currency                                  the currency of the state
     * @param transaction_scheduler                     the transaction scheduler for the scheduled transactions
     * @param budget_manager                            the budget manager for budget reports
     */
    public BasicExpenseManagerState(final Currency currency, final T transaction_scheduler, final G budget_manager) {
        this.transaction_scheduler=transaction_scheduler;
        this.budget_manager=budget_manager;
        this.currency = currency;
        this.user = null;
    }

    /**
     * Constructor method
     *
     * @param currency                                  the currency of the state
     * @param user                                      the user of the state
     * @param transaction_scheduler                     the transaction scheduler for the scheduled transactions
     * @param budget_manager                            the budget manager for budget reports
     */
    public BasicExpenseManagerState(Currency currency, User user, T transaction_scheduler, G budget_manager) {
        this.transaction_scheduler=transaction_scheduler;
        this.budget_manager=budget_manager;
        this.currency = currency;
        this.user = user;
    }

    public List<Account> getAccounts(){
        return this.accounts;
    }

    public List<Transaction> getTransactions(){
        List<Transaction> to_return=new ArrayList<>();
        this.accounts.parallelStream().forEach(account -> to_return.addAll(account.getTransactions()));
        return to_return;
    }

    /**
     * This method returns a list of transactions that respect a particular predicate
     *
     * @param p                     the predicate to work with
     * @return return_list          the list of transactions that respect that predicate
     */
    @Override
    public List<Transaction> getTransactions(Predicate p) {
        List<Transaction> return_list = new ArrayList<>();
        this.accounts.parallelStream().forEach(account -> return_list.addAll(account.getTransactions(p)));
        return return_list;
    }

    /**
     * This method returns the list of the scheduled transactions
     *
     * @return                      the list of the scheduled transactions
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions() {
        List<ScheduledTransaction> to_return=new ArrayList<>();
        this.accounts.parallelStream().forEach(account -> to_return.addAll(account.getScheduledTransactions()));
        return to_return;
    }

    /**
     * This method returns the list of the categories
     *
     * @return                      the list of the categories
     */
    public List<Category> getCategories(){
        return this.categories;
    }

    /**
     * This method returns the list of the budgets
     *
     * @return                      the list of the budgets
     */
    @Override
    public List<Budget> getBudgets() {
        return this.budgets;
    }

    /**
     * This method sets up the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * This method returns the user
     *
     * @return                      the user of the state
     */
    public User getUser(){
        return this.user;
    }

    /**
     * This method returns the currency
     *
     * @return                      the currency of the state
     */
    public Currency getCurrency(){
        return currency;
    }

    /**
     * This method sets up the currency
     */
    public void setCurrency(Currency currency){
        this.currency=currency;
    }

    /**
     * This method calls the transacion scheduler method to schedule the scheduled transactions
     */
    @Override
    public void schedule() {
        this.transaction_scheduler.schedule(this.getScheduledTransactions());
    }

    /**
     * This method make the transfer from an account to another
     *
     * @param from                              the account where the transfer come
     * @param to                                the account where to transfer the money
     * @param amount                            the amount to transfer
     * @throws InsufficientBalanceException     exception if the balance of the account is less than the amount to transfer
     * @throws IllegalArgumentException         exception if the two accounts are the same
     * @throws MissingInformationException      exception of the movements created
     */
    @Override
    public void transfer(Account from, Account to, double amount) throws InsufficientBalanceException, IllegalArgumentException,
            MissingInformationException{
        if(from.getBalance()<amount) throw new InsufficientBalanceException(from.getBalance(), amount);
        if(from.equals(to) || (from.getAccountType()!=AccountType.ASSET || (to.getAccountType()!=AccountType.ASSET)))
            throw new IllegalArgumentException();
        BasicMovement movement_from=new BasicMovement("Transfer", MovementType.EXPENSE, amount);
        BasicMovement movement_to=new BasicMovement("Transfer", MovementType.INCOME, amount);
        from.addTransaction(new BasicTransaction("Transfer", "Transfer to"+to.getName(), LocalDate.now(),
                new BasicCategory("Transfer"), movement_from));
        to.addTransaction(new BasicTransaction("Transfer", "Transfer from"+to.getName(), LocalDate.now(),
                new BasicCategory("Transfer"), movement_to));
    }

    /**
     * This method calls the budget report method to get the report of a budget
     *
     * @param transactions                      the transaction of the same category of the budget
     * @param budget                            the budget to work with
     * @return                                  the difference between amount spent and amount expected
     */
    @Override
    public double generate_report(List transactions, Budget budget) {
        return budget_manager.generate_report( transactions, budget);
    }

    /**
     * This method add a new account to the list
     *
     * @param account                           the account to add
     * @throws InstanceAlreadyExistsException   exception if the account already exists
     */
    public void addAccount(Account account) throws InstanceAlreadyExistsException {
        if(this.accounts.contains(account)) throw new InstanceAlreadyExistsException("This budget already exixts");
        this.accounts.add(account);
    }

    /**
     * This method removes the passed account
     *
     * @param account                           the account to remove
     */
    @Override
    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }

    /**
     * This method add a new transaction to the list
     *
     * @param account                           the account where to add the transaction
     * @param transaction                       the transaction to add
     */
    @Override
    public void addTransaction(Account account, Transaction transaction) {
        account.addTransaction(transaction);
    }

    /**
     * This method removes the passed transaction
     *
     * @param transaction                        the transaction to remove
     */
    @Override
    public void removeTransaction(Transaction transaction) {
        this.accounts.parallelStream().forEach(account -> account.removeTransaction(transaction));
    }

    /**
     * This method add a new account to the list
     *
     * @param account                              the account where to add the transaction
     * @param scheduled_transaction                the scheduled transaction to add
     */
    @Override
    public void addScheduledTransaction(Account account, ScheduledTransaction scheduled_transaction) {
        account.addScheduledTransaction(scheduled_transaction);
    }

    /**
     * This method removes the passed scheduled transaction
     *
     * @param scheduled_transaction                the scheduled transaction to remove
     */
    @Override
    public void removeScheduledTransaction(ScheduledTransaction scheduled_transaction) {
        this.accounts.parallelStream().forEach(account -> account.removeScheduledTransaction(scheduled_transaction));
    }

    /**
     * This method add a movement to a transaction
     *
     * @param current_balance                       the current balance
     * @param movement                              the movement to add
     * @param transaction                           the transaction where to add the new movement
     * @throws InsufficientBalanceException         exception if the current balance is less than the movement value (if it is an expense)
     */
    public void addMovement(double current_balance, Movement movement, Transaction transaction) throws InsufficientBalanceException {
        if(movement.getMovementType()==MovementType.EXPENSE && movement.getValue()>current_balance)
            throw new InsufficientBalanceException(current_balance, movement.getValue());
        transaction.addMovement(movement);
    }

    /**
     * This method adds a new budget to the list
     *
     * @param budget                                the budget to add
     * @throws InstanceAlreadyExistsException       exception if the budget already exists
     */
    public void addBudget(Budget budget) throws InstanceAlreadyExistsException {
        if(this.budgets.contains(budget)) throw new InstanceAlreadyExistsException("This budget already exixts");
        budgets.add(budget);
    }

    /**
     * This method removes a budget
     *
     * @param budget                                the budget to remove
     */
    @Override
    public void removeBudget(Budget budget) {
        budgets.remove(budget);
    }

    /**
     * This method adds a new category to the list
     *
     * @param category                              the category to add
     * @throws InstanceAlreadyExistsException       exception if the category already exists
     */
    @Override
    public void addCategory(Category category) throws InstanceAlreadyExistsException {
        if(this.categories.contains(category)) throw new InstanceAlreadyExistsException("This category already exixts");
        this.categories.add(category);
    }

    /**
     * This category removes a category. If this category is a parent of other categories, they will be eliminated, only
     *  if they aren't used
     *
     * @param category_toRemove                     the category to remove
     * @throws RemoveUsedCategoryException          exception if thi category is used
     */
    @Override
    public void removeCategory(Category category_toRemove) throws RemoveUsedCategoryException {
        Predicate<Transaction> predicate_transactions = (transaction) -> transaction.getCategories().contains(category_toRemove);
        Predicate<Budget> predicate_budgets = (budget) -> budget.getCategory().equals(category_toRemove);
        if(this.getTransactions().parallelStream().anyMatch(predicate_transactions) ||
                this.getBudgets().parallelStream().anyMatch(predicate_budgets) ) throw new RemoveUsedCategoryException();

        if(category_toRemove.getParent()==null){
            Predicate<Category> predicate = (category) -> category.getParent()!=null && category.getParent().equals(category_toRemove);
            List<Category> children_categories = this.categories.parallelStream().filter(predicate).collect(Collectors.toList());
            for (Category category : children_categories) {
                this.removeCategory(category);
            }
        }
        this.categories.remove(category_toRemove);
    }

    /**
     * This method add a list of account to the list
     *
     * @param accounts                              the accounts to add
     */
    @Override
    public void addAccounts(List accounts) {
        this.accounts.addAll(accounts);
    }

    /**
     * This method add a list of categories to the list
     *
     * @param categories                            the categories to add
     */
    @Override
    public void addCategories(List categories) {
        this.categories.addAll(categories);
    }

    /**
     * This method add a list of budgets to the list
     *
     * @param budgets                               the budgets to add
     */
    @Override
    public void addBudgets(List budgets) {
        this.budgets.addAll(budgets);
    }


}