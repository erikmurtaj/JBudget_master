package it.unicam.cs.pa.jbudget105126.View;

import it.unicam.cs.pa.jbudget105126.Cotroller.ExpenseManager;

import java.io.IOException;

/**
 * The classes implementing this interface have the responsibility of implementing I/O user interface.
 */

public interface View<T extends ExpenseManager>{

    void open() throws IOException;

    void close();
}
