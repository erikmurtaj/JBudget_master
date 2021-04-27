package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

class BasicCategoryTest {

    private Category category_test;

    @BeforeEach
    void init() throws MissingInformationException {
        category_test=new BasicCategory("Test category");
    }

    @Test
    void getName(){
        assertThrows(MissingInformationException.class, () -> new BasicCategory("    "));
        assertThrows(MissingInformationException.class, () -> new BasicCategory(""));
        assertEquals("Test category", category_test.getName());
    }

    @Test
    void getParent() throws MissingInformationException, WrongCategoryParameterException {
        Category category_test2=new BasicCategory("Test subcategory", category_test);
        assertTrue(category_test2.getParent().equals(category_test));
        assertThrows(WrongCategoryParameterException.class, () -> new BasicCategory("Test subcategory2", category_test2));
    }

    @Test
    void testEquals() throws MissingInformationException {
        assertTrue(category_test.equals(new BasicCategory("Test category")));
        assertFalse(category_test.equals(new BasicCategory("Test category2")));
    }
}