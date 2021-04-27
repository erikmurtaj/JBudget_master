package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.Category;
import it.unicam.cs.pa.jbudget105126.Model.Movement;
import it.unicam.cs.pa.jbudget105126.Model.MovementType;
import it.unicam.cs.pa.jbudget105126.Model.Transaction;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicTransactionTest {

    private Transaction transaction;

    @BeforeEach
    void init() throws MissingInformationException {
        transaction=new BasicTransaction("Transaction test", "description", LocalDate.now(), new BasicCategory("category test"));
        DataCreator.getListMovements(5, MovementType.INCOME, 10).stream().forEach(movement -> transaction.addMovement(movement));
    }

    @Test
    void getName() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicTransaction("    ", "   ", LocalDate.now()));
        assertThrows(MissingInformationException.class, () -> new BasicTransaction("", "", LocalDate.now()));
        assertEquals("Transaction test", transaction.getName());
    }

    @Test
    void getDescription() throws MissingInformationException {
        assertThrows(MissingInformationException.class, () -> new BasicTransaction("    ", "   ", LocalDate.now()));
        assertThrows(MissingInformationException.class, () -> new BasicTransaction("", "", LocalDate.now()));
        assertEquals("description", transaction.getDescription());
    }

    @Test
    void getDate() {
        assertTrue(LocalDate.now().equals(transaction.getDate()));
    }

    @Test
    void getMovements() throws MissingInformationException {
        assertFalse(transaction.getMovements().isEmpty());
        assertEquals(5, transaction.getMovements().size());
    }

    @Test
    void getCategories() throws MissingInformationException, WrongCategoryParameterException {
        List<Category> categories=new ArrayList<>();
        Category category_test=new BasicCategory("category test");
        Category category_test2=new BasicCategory("category test 2");
        categories.add(category_test);
        categories.add(category_test2);

        transaction.addCategory(category_test2);
        assertEquals(categories, transaction.getCategories());
    }

    @Test
    void getTotal() throws MissingInformationException {
        assertEquals(50, transaction.getTotal());
        DataCreator.getListMovements(5, MovementType.EXPENSE, 10).stream().forEach(movement -> transaction.addMovement(movement));
        assertEquals(0, transaction.getTotal());
        DataCreator.getListMovements(5, MovementType.EXPENSE, 10).stream().forEach(movement -> transaction.addMovement(movement));
        assertEquals(-50, transaction.getTotal());
    }

}