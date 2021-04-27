package it.unicam.cs.pa.jbudget105126.Model.Implementation;


import it.unicam.cs.pa.jbudget105126.Model.Category;
import it.unicam.cs.pa.jbudget105126.Model.Movement;
import it.unicam.cs.pa.jbudget105126.Model.MovementType;
import it.unicam.cs.pa.jbudget105126.Model.Transaction;

import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BasicTransaction implements Transaction {

    private String name;
    private String description;
    private LocalDate date;
    private List<Category> categories=new ArrayList<Category>();
    private List<Movement> movements=new ArrayList<Movement>();

    /**
     * Constructor method
     *
     * @param name                                  the name of the new transaction
     * @param description                           the descriptio of the new transaction
     * @param date                                  the date of the new transaction
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    public BasicTransaction(String name, String description, LocalDate date) throws MissingInformationException {
        if(name.trim().length() == 0 || description.trim().length() == 0 || date==null) throw new MissingInformationException();
        this.name = name;
        this.description = description;
        this.date = date;
    }

    /**
     * Constructor method

     * @param name                                  the name of the new transaction
     * @param description                           the description of the new transaction
     * @param date                                  the date of the new transaction
     * @param category                              the category of the new transaction
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    public BasicTransaction(String name, String description, LocalDate date, Category category) throws MissingInformationException {
        if(name.trim().length() == 0 || description.trim().length() == 0|| date==null || category==null) throw new MissingInformationException();
        this.name = name;
        this.description = description;
        this.date = date;
        this.categories.add(category);
    }

    /**
     * Constructor method

     * @param name                                  the name of the new transaction
     * @param description                           the description of the new transaction
     * @param date                                  the date of the new transaction
     * @param category                              the category of the new transaction
     * @param movement                              the movement to add to the new transaction
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    public BasicTransaction(String name, String description, LocalDate date, Category category, Movement movement) throws MissingInformationException {
        if(name.trim().length() == 0 || description.trim().length() == 0 || date==null || category==null
                || movement==null) throw new MissingInformationException();
        this.name = name;
        this.description = description;
        this.date = date;
        this.categories.add(category);
        this.movements.add(movement);
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public List<Movement> getMovements(){
        return this.movements;
    }

    public List<Category> getCategories(){
        return this.categories;
    }

    public Category getCategory(){
        return this.categories.get(0);
    }

    public void addCategory(Category category){
        this.categories.add(category);
    }

    public void removeCategory(Category category){
        this.categories.remove(category);
    }

    public void addMovement(Movement movement){
        this.movements.add(movement);
    }

    public void removeMovement(Movement movement){
        this.movements.remove(movement);
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