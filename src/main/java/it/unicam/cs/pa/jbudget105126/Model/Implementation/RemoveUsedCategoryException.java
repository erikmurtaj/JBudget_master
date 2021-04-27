package it.unicam.cs.pa.jbudget105126.Model.Implementation;

public class RemoveUsedCategoryException extends Exception {

    public static final String MESSAGE = "This category or its subcategories are used";

    public RemoveUsedCategoryException() {
        super(MESSAGE);
    }
}
