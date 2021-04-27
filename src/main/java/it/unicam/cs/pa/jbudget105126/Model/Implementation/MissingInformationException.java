package it.unicam.cs.pa.jbudget105126.Model.Implementation;

public class MissingInformationException extends Exception {

    public static final String MESSAGE = "One or more information are missing.";

    public MissingInformationException() {
        super(MESSAGE);
    }
}
