package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105126.Model.Scheduler;
import org.joda.time.*;

import java.util.List;

public class TransactionScheduler implements Scheduler {

    /**
     *  This function schedules the scheduled transactions. Given in input a list of scheduled transactions it
     *  calculates the movements to be created using their period frequency.
     *  It determinates the period of time passed and if the transaction might be completed.
     *
     *  @param scheduled_transactions           the list of scheduled transaction to schedule
     *
     *   EXAMPLE: a period of time of 14 days with a weekly frequency and one movement already done
     *
     *  Start                        done                         end    current_date
     *    |---|---|---|---|---|---|---|---|---|---|---|---|---|---|---...|
     *    0   1   2   3   4   5   6  (7)  8   9  10  11  12  13  14
     *
     *    Period = 14 days
     *    already_done = 1 (day: 7)
     *    Movements to do = period/7 - already_done
     *
     *   EXAMPLE: a period of time of 21 days with a weekly frequency, one movement already done and
     *          current date on the 15° day of that period
     *
     *  Start                        done         current_date    end
     *    |---|---|---|---|---|---|---|---|---|...|---|---|---|...|
     *    0   1   2   3   4   5   6  (7)  8   9  14 {15}  16  17  21
     *
     *    Period = 21 days
     *    already_done = 1 (day: 7)
     *    current_date = 15
     *    Movements to do = period/7 - already_done - difference(current_date, already_done*7)/7
     *                    =  (21/7)  -      1       -            (15-1*7)/7
     *                    =    3     -      1       -               1
     *                    =    1 ==> one movement to do (day: 14), waiting for day 21 for the third movement
     */
    @Override
    public void schedule(List<ScheduledTransaction> scheduled_transactions) {

        for (ScheduledTransaction scheduled_transaction : scheduled_transactions) {
            int already_done= scheduled_transaction.getMovements().size();
            LocalDate start_date = scheduled_transaction.getDate();
            LocalDate end_date = scheduled_transaction.getEndDate();
            LocalDate current_date = LocalDate.now();
            int todo=0;
            if(calculateIfPassed(scheduled_transaction)<=0) {
                scheduled_transaction.complete();
            }
            else end_date=current_date;
            switch (scheduled_transaction.getFrequency()){
                case NONE:
                    if(calculateIfPassed(scheduled_transaction)<=0)
                        scheduled_transaction.addMovement(scheduled_transaction.getMovement_toRepeat());
                    break;
                case DAYLY:
                    todo=Days.daysBetween(start_date, end_date).getDays();
                    break;
                case WEEKLY:
                    todo= Weeks.weeksBetween(start_date, end_date).getWeeks();
                case BIWEEKLY:
                    todo=Weeks.weeksBetween(start_date, end_date).getWeeks()/2;
                case MONTHLY:
                    todo= Months.monthsBetween(start_date, end_date).getMonths();
                    break;
                case HALF_YEARLY:
                    todo= Months.monthsBetween(start_date, end_date).getMonths()/6;
                case YEARLY:
                    todo= Years.yearsBetween(start_date, end_date).getYears();
                default:
                    return;
            }
            scheduled_transaction.repeat_movement(todo-already_done);
        }
    }

    private int calculateIfPassed(ScheduledTransaction scheduled_transaction){
        LocalDate end_date = scheduled_transaction.getEndDate();
        LocalDate current_date = LocalDate.now();
        return Days.daysBetween(current_date, end_date).getDays();
    }
}