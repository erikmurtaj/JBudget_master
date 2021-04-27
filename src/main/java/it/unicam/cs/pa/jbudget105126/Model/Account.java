package it.unicam.cs.pa.jbudget105126.Model;

import java.util.List;
import java.util.function.Predicate;
import org.joda.time.LocalDate;

public interface Account {

    String getName();

    String getDescription();

    double getBalance();

    double getBalanceInDate(LocalDate date);

    double getBalanceInDate(LocalDate date, Category category);

    double getInitial();

    AccountType getAccountType();

    List<Transaction> getTransactions();

    List<ScheduledTransaction> getScheduledTransactions();

    List<Transaction> getTransactions(Predicate<Transaction> p);

    void addTransaction(Transaction transaction);

    void removeTransaction(Transaction transaction);

    void addScheduledTransaction(ScheduledTransaction transaction);

    void removeScheduledTransaction(ScheduledTransaction transaction);

}
