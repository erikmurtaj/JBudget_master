package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.Budget;
import it.unicam.cs.pa.jbudget105126.Model.Category;

import javax.management.InstanceAlreadyExistsException;
import java.util.Objects;

public class BasicBudget implements Budget {

    private Category category;
    private String name;
    private double expected;

    /**
     *  Constructor method
     *
     * @param name                                  the name of the new budget
     * @param category                              the category of the new budget
     * @param expected                              the amount excpeted of the new budget
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws InstanceAlreadyExistsException       exception if this budget already exists
     */
    public BasicBudget(Category category, String name, double expected) throws MissingInformationException {
        if(name.trim().length() == 0 || category==null) throw new MissingInformationException();
        this.category = category;
        this.name = name;
        this.expected = expected;
    }

    /**
     * This method returns the name
     *
     * @return                                      the name of the budget
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * This method returns the category
     *
     * @return                                      the category of the budget
     */
    @Override
    public Category getCategory() {
        return this.category;
    }

    /**
     * This method returns the expected amount
     *
     * @return                                      the expected amount of the budget
     */
    @Override
    public double getExpected() {
        return this.expected;
    }

    /**
     * Overrided euqals method. Two budgets are equals if the name and the category are the same
     *
     * @param o                         the object to compare
     * @return                          true if the budgets are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicBudget that = (BasicBudget) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(name, that.name);
    }
}
