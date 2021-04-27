package it.unicam.cs.pa.jbudget105126;

import it.unicam.cs.pa.jbudget105126.Model.*;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.*;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class DataCreator {

    public static List<Account> getListAccounts(int size, AccountType type, double amount) {
        List<Account> to_return = new ArrayList<>();
        for(int i=0; i<size; i++) {
            try {
                to_return.add(new BasicAccount("Account"+i, "description", type, amount));
            } catch (MissingInformationException e) {
                e.printStackTrace();
            }
        }
        return to_return;
    }

    public static List<Movement> getListMovements(int size, MovementType type, double amount){
        List<Movement> to_return = new ArrayList<>();
        for(int i=0; i<size; i++) {
            try {
                to_return.add(new BasicMovement("Movement"+i, type, amount));
            } catch (MissingInformationException e) {
                e.printStackTrace();
            }
        }
        return to_return;
    }

    public static List<Category> getListCategories(int size){
        List<Category> to_return = new ArrayList<>();
        for(int i=0; i<size; i++) {
            try {
                to_return.add(new BasicCategory("Category"+i));
            } catch (MissingInformationException e) {
                e.printStackTrace();
            }
        }
        return to_return;
    }

    public static List<Budget> getListBudgets(int size) throws MissingInformationException {
        List<Budget> to_return = new ArrayList<>();
        for(int i=0; i<size; i++) {
            try {
                to_return.add(new BasicBudget(new BasicCategory("category test"), "Budget"+i,100));
            } catch (MissingInformationException e) {
                e.printStackTrace();
            }
        }
        return to_return;
    }

    public static List<Transaction> getListTransactions(int size, Category category){
        List<Transaction> to_return = new ArrayList<>();
        for(int i=0; i<size; i++) {
            try {
                to_return.add(new BasicTransaction("Transaction"+i, "description", LocalDate.now(), category));
            } catch (MissingInformationException e) {
                e.printStackTrace();
            }
        }
        return to_return;
    }

}
