package it.unicam.cs.pa.jbudget105126.Model.Implementation;

public class InsufficientBalanceException extends Exception{

    public InsufficientBalanceException(double current_balance, double value_to_insert) {
        super("Insufficient Balance ("+current_balance+") in front of "+value_to_insert);
    }
}
