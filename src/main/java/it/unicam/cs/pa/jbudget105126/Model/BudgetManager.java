package it.unicam.cs.pa.jbudget105126.Model;

import java.util.List;

public interface BudgetManager {

    double generate_report(List<Transaction> transactions, Budget budget);
}
