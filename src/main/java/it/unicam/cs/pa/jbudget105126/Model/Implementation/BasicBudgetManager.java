package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.Budget;
import it.unicam.cs.pa.jbudget105126.Model.BudgetManager;
import it.unicam.cs.pa.jbudget105126.Model.Transaction;

import java.util.List;

public class BasicBudgetManager implements BudgetManager{

    /**
     * This method returns the difference between the amount expected and the amount spent in the
     *  category related to this budget
     *
     * @param transactions                  the transaction of the same category of the budget
     * @param budget                        the budget to work with
     * @return                              the difference between amount spent and amount expected
     */
    @Override
    public double generate_report(List<Transaction> transactions, Budget budget){
        return transactions.parallelStream().mapToDouble(Transaction::getTotal).sum()+budget.getExpected();
    }
}
