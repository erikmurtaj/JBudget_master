package it.unicam.cs.pa.jbudget105126.Model.Persistence;

import it.unicam.cs.pa.jbudget105126.Model.ExpenseManagerState;

public interface DataManager<T extends ExpenseManagerState> {

    void load();

    void save();
}
