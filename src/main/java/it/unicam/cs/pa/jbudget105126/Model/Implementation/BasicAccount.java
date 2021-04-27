package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.*;

import org.joda.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BasicAccount implements Account{
    private String name;
    private String description;
    private List<Transaction> transactions;
    private List<ScheduledTransaction> scheduled_transactions;
    private double initial;
    private AccountType type;

    /**
     *  Constructor method
     *
     * @param name                                  the name of the new account
     * @param description                           the description of the new account
     * @param type                                  the type of the new account
     * @param initial                               the starting amount of the new account
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws InstanceAlreadyExistsException       exception if this account already exists
     */
    public BasicAccount(String name, String description, AccountType type, double initial) throws MissingInformationException {
        if(name.trim().length() == 0 || description.trim().length() == 0 || type==null) throw new MissingInformationException();
        this.name = name;
        this.initial=initial;
        this.description = description;
        this.transactions = new ArrayList<>();
        this.scheduled_transactions=new ArrayList<>();
        this.type=type;
    }

    /**
     *  Constructor method
     *
     * @param name                                  the name of the new account
     * @param description                           the description of the new account
     * @param type                                  the type of the new account
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws InstanceAlreadyExistsException       exception if this account already exists
     */
    public BasicAccount(String name, String description, AccountType type) throws MissingInformationException {
        if(name.trim().length() == 0 || description.trim().length() == 0 || type==null) throw new MissingInformationException();
        this.name = name;
        this.initial=0;
        this.description = description;
        this.transactions = new ArrayList<Transaction>();
        this.scheduled_transactions=new ArrayList<>();
        this.type=type;
    }

    /**
     * This method returns the name
     *
     * @return                                      the name of the account
     */
    @Override
    public String getName(){
        return this.name;
    }

    /**
     * This method returns the description
     *
     * @return                                      the description of the account
     */
    @Override
    public String getDescription(){
        return this.description;
    }

    /**
     * This method returns the account ype
     *
     * @return                                     the account type
     */
    @Override
    public AccountType getAccountType() {
        return this.type;
    }

    /**
     * This method returns the starting amount
     *
     * @return                                     the starting amount of the account
     */
    @Override
    public double getInitial() {
        return this.initial;
    }

    /**
     * This method returns all the transactions of this account
     *
     * @return                                     the transactions of this account
     */
    @Override
    public List<Transaction> getTransactions(){
        return this.transactions;
    }

    /**
     * This method returns the list of the scheduled transactions
     *
     * @return List<ScheduledTransaction>
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions() {
        return this.scheduled_transactions;
    }

    /**
     * This function returns a list of transactions that respect a predicate
     *
     * @param predicate                 the predicate to work with
     * @return List<Transaction>        the list of transactions that respect that predicate
     */
    public List<Transaction> getTransactions(Predicate<Transaction> predicate){
        List<Transaction> to_return = transactions.parallelStream().filter((Predicate<Transaction>) predicate).collect(Collectors.toList());
        to_return.addAll(scheduled_transactions.parallelStream().filter(predicate).collect(Collectors.toList()));
        return to_return;
    }

    /**
     * This method return the balance of this account. If the account is a LIABILITY the balance will be
     *    multiplicated by -1 because it represents a debit
     *
     * @return balance                  the balance of this account
     */
    @Override
    public double getBalance(){
        double balance=initial;
        if(this.transactions==null){ return balance;}
        balance+=transactions.parallelStream().mapToDouble(Transaction::getTotal).sum();
        balance+=scheduled_transactions.parallelStream().mapToDouble(Transaction::getTotal).sum();
        return ((this.type.equals(AccountType.ASSET)) ? (balance) : (balance*-1));
    }

    /**
     * This method return the balance in a specific day. For that it filters the transactions by a predicate
     *  getting all of them which are before that date (+ 1 to consider the date as well)
     * @param date          the date to get the balance from
     * @return balance      the balance in that date
     */
    @Override
    public double getBalanceInDate(LocalDate date) {
        double balance=initial;
        if(this.transactions==null){ return balance;}
        Predicate<Transaction> before_predicate = (transaction) -> transaction.getDate().isBefore(date.plusDays(1));
        balance+=this.getTransactions(before_predicate).parallelStream().mapToDouble(Transaction::getTotal).sum();
        return ((this.type.equals(AccountType.ASSET)) ? (balance) : (balance*-1));
    }

    /**
     * This method return the balance in a specific day. For that it filters the transactions by a predicate
     *  getting all of them which are before that date (+ 1 to consider the date as well). It also filters the transaction
     *  by a passed category
     *
     * @param date              the date to get the balance from
     * @param category          the category to use as filter
     * @return balance          the balance in that date
     */
    @Override
    public double getBalanceInDate(LocalDate date, Category category){
        double balance=initial;
        if(this.transactions==null){ return balance;}
        Predicate<Transaction> before_predicate = (transaction) -> transaction.getDate().isBefore(date.plusDays(1));
        Predicate<Transaction> category_predicate = (transaction) -> transaction.getCategories().contains(category);
        balance+=this.getTransactions(before_predicate).parallelStream().filter(category_predicate).mapToDouble(Transaction::getTotal).sum();
        return ((this.type.equals(AccountType.ASSET)) ? (balance) : (balance*-1));
    }

    /**
     * This method adds a transaction into the list of transactions
     *
     * @param transaction               the transaction to add
     */
    @Override
    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }

    /**
     * This method removes a transaction into the list of transactions
     *
     * @param transaction               the transaction to remove
     */
    @Override
    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
    }

    /**
     * This method adds a scheduled transaction into the list of scheduled transactions
     *
     * @param scheduled_transaction      the scheduled transaction to add
     */
    @Override
    public void addScheduledTransaction(ScheduledTransaction scheduled_transaction){
        this.scheduled_transactions.add(scheduled_transaction);
    }

    /**
     * This method removes a scheduled transaction into the list of scheduled transactions
     *
     * @param scheduled_transaction      the scheduled transaction to remove
     */
    @Override
    public void removeScheduledTransaction(ScheduledTransaction scheduled_transaction) {
        this.scheduled_transactions.remove(scheduled_transaction);
    }

    /**
     * Overrided euqals method. Two accounts are equals if the name, the description and the type are the same
     *
     * @param o                         the object to compare
     * @return                          true if the accounts are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicAccount that = (BasicAccount) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                type == that.type;
    }

    @Override
    public String toString() {
        return "" + name;
    }
}
