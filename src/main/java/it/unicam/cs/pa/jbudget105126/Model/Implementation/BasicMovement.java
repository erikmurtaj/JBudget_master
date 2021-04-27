package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.Movement;
import it.unicam.cs.pa.jbudget105126.Model.MovementType;

public class BasicMovement implements Movement {
    private double value;
    private String name;
    private MovementType type;

    /**
     * Constructor method
     *
     * @param name                                  the name of the new movement
     * @param value                                 the amount of the new movement
     * @param type                                  the type of the new movement
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    public BasicMovement(String name, MovementType type, double value) throws MissingInformationException {
        if(name.trim().length() == 0|| type==null) throw new MissingInformationException();
        this.name = name;
        this.type=type;
        this.value=value;
    }

    /**
     * This method returns the name
     *
     * @return                                      the name of the movement
     */
    public String getName(){
        return this.name;
    }

    /**
     * This method returns the amount value
     *
     * @return                                      the amount value of the movement
     */
    @Override
    public double getValue() {
        return this.value;
    }

    /**
     * This method returns the movement type
     *
     * @return                                      the movement type
     */
    public MovementType getMovementType(){
        return this.type;
    }
}
