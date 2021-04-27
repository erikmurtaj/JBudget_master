package it.unicam.cs.pa.jbudget105126.Model.Implementation;

public class WrongDateRangeException extends Exception{

    public static final String MESSAGE = "The end date must be after the start date";

    public WrongDateRangeException() {
        super(MESSAGE);
    }
}
