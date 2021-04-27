package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.*;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BasicScheduledTransaction implements ScheduledTransaction {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Category> categories= new ArrayList<Category>();
    private List<Movement> movements=new ArrayList<Movement>();
    private Movement movement_toRepeat;
    private boolean complete;
    private TransactionFrequency frequency;

    /**
     * Constructor method
     *
     * @param name                                  the name of the new transaction
     * @param description                           the description of the new transaction
     * @param start                                 the date of the new transaction
     * @param end                                   the end date of the new transaction
     * @param frequency                             the frequency of the new transaction
     * @param movement_toRepeat                     the movement to repeate of the new transaction
     * @return                                      the scheduled transaction added
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws WrongDateRangeException              exception if the range date is negative or not valid
     */
    public BasicScheduledTransaction(String name, String description, LocalDate start, LocalDate end, TransactionFrequency frequency, Movement movement_toRepeat) throws MissingInformationException, WrongDateRangeException {
        if(name.trim().length() == 0 || description.trim().length() == 0 || start==null || end==null || frequency==null
                || movement_toRepeat==null) throw new MissingInformationException();
        if(end.isBefore(start) || end.equals(start)) throw new WrongDateRangeException();
        this.name = name;
        this.description = description;
        this.startDate =start;
        this.endDate =end;
        this.frequency=frequency;
        this.complete=false;
        this.movement_toRepeat=movement_toRepeat;
    }

    /**
     * Constructor method
     *
     * @param name                                  the name of the new transaction
     * @param description                           the description of the new transaction
     * @param start                                 the date of the new transaction
     * @param end                                   the end date of the new transaction
     * @param category                              the category of the new transaction
     * @param frequency                             the frequency of the new transaction
     * @return                                      the scheduled transaction added
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws WrongDateRangeException              exception if the range date is negative or not valid
     */
    public BasicScheduledTransaction(String name, String description, LocalDate start, LocalDate end, Category category, TransactionFrequency frequency) throws MissingInformationException, WrongDateRangeException {
        if(name.trim().length() == 0 || description.trim().length() == 0 || category==null || start==null || end==null || frequency==null
                ) throw new MissingInformationException();
        if(end.isBefore(start) || end.equals(start)) throw new WrongDateRangeException();
        this.name = name;
        this.description = description;
        this.startDate =start;
        this.endDate =end;
        this.categories.add(category);
        this.frequency=frequency;
        this.complete=false;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public LocalDate getDate(){
        return this.startDate;
    }

    public LocalDate getEndDate(){
        return this.endDate;
    }

    /**
     * This method repeates the movement to repeat k times.
     *
     * @param k                                 times to repeate the movement
     */
    @Override
    public void repeat_movement(int k) {
        for(int i=0; i<k; i++) this.movements.add(movement_toRepeat);
    }

    @Override
    public void complete() {
        this.complete=true;
    }

    public List<Movement> getMovements(){
        return this.movements;
    }

    public List<Category> getCategories(){
        return this.categories;
    }

    public void addCategory(Category category){
        this.categories.add(category);
    }

    public void removeCategory(Category category){
        this.categories.remove(category);
    }

    public void addMovement(Movement movement){
        this.movement_toRepeat=movement;
    }

    public void removeMovement(Movement movement){
        this.movements.remove(movement);
    }

    public boolean getCompleted(){
        return this.complete;
    }

    public TransactionFrequency getFrequency(){
        return this.frequency;
    }

    @Override
    public Movement getMovement_toRepeat() {
        return this.movement_toRepeat;
    }

    /**
     * This method calculates the total amount of this transaction. EXPENSE movements will decrement this value
     *      and INCOME movements will increment this value.
     *      If the total is positive represents a profit (IF the account is an ASSET)
     *      If the total is negative represents a loss (IF the account is an ASSET)
     *      Viceversa if the account is a LIABILITY
     *
     * @return                                  the total amount of this transaction
     */
    @Override
    public double getTotal(){
        double total=0;
        for(Movement m : movements){
            if(m.getMovementType().equals(MovementType.INCOME))
                total+=m.getValue();
            else if(m.getMovementType().equals(MovementType.EXPENSE))
                total-=m.getValue();
        }
        return total;
    }
}
