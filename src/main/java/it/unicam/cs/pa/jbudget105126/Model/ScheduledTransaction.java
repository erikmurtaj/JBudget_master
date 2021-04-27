package it.unicam.cs.pa.jbudget105126.Model;

import org.joda.time.LocalDate;

public interface ScheduledTransaction extends Transaction {

    boolean getCompleted();

    TransactionFrequency getFrequency();

    Movement getMovement_toRepeat();

    LocalDate getEndDate();

    void repeat_movement(int k);

    void complete();
}
