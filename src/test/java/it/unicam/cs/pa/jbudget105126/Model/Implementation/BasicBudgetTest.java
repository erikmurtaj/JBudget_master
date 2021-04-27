package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.*;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicBudgetTest {

    private BasicBudget budget_test;

    @BeforeEach
    void init() throws MissingInformationException {
        budget_test=new BasicBudget(new BasicCategory("Category test"), "Budget test", 500);
    }

    @Test
    void getName() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicCategory("    "));
        assertThrows(MissingInformationException.class, () -> new BasicCategory(""));
        assertEquals("Budget test", budget_test.getName());
    }

    @Test
    void getCategory() throws MissingInformationException {
        assertEquals(new BasicCategory("Category test"), budget_test.getCategory());
    }
}