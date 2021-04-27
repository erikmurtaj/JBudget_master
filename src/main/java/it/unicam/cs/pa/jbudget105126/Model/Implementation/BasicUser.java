package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.User;

public class BasicUser implements User {

    private String name;

    /**
     * Constructor method
     *
     * @param name                                      the name of the new user
     * @throws MissingInformationException              exception if any information is missing or not valid
     */
    public BasicUser(String name) throws MissingInformationException {
        if(name.trim().length() == 0) throw new MissingInformationException();
        this.name = name;
    }

    /**
     * This method returns the name
     *
     * @return                                      the name of the user
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * This method sets up the name of the user
     *
     * @param name                                  the name to sets up
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
