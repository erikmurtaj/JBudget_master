package it.unicam.cs.pa.jbudget105126.Model.Implementation;

public class WrongCategoryParameterException extends Exception{

    public static final String MESSAGE = "A subcategory cannot be a parent for a new category.";

    public WrongCategoryParameterException() {
        super(MESSAGE);
    }
}
