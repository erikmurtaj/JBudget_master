package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.Category;

import java.util.Objects;

public class BasicCategory implements Category{
    private static int IDCounter=1;
    private int ID;
    private String name;
    private Category parent;

    /**
     * Constructor method
     *
     * @param name                                  the name of the new category
     * @param parent                                the parent of the new category
     * @throws MissingInformationException          exception if any information is missing or not valid
     * @throws WrongCategoryParameterException      exception if the parent is not valid
     */
    public BasicCategory(String name, Category parent) throws MissingInformationException, WrongCategoryParameterException {
        if(name.trim().length() == 0) throw new MissingInformationException();
        if(parent.getParent()!=null) throw new WrongCategoryParameterException();
        this.ID = IDCounter;
        IDCounter++;
        this.name = name;
        this.parent = parent;
    }

    /**
     * Constructor method
     *
     * @param name                                  the name of the new category
     * @throws MissingInformationException          exception if any information is missing or not valid
     */
    public BasicCategory(String name) throws MissingInformationException {
        if(name.trim().length() == 0) throw new MissingInformationException();
        this.ID = IDCounter;
        IDCounter++;
        this.name = name;
        this.parent = null;
    }

    /**
     * This method returns the ID
     *
     * @return                                      the ID of the category
     */
    public int getID(){
        return this.ID;
    }

    /**
     * This method returns the name
     *
     * @return                                      the name of the category
     */
    public String getName(){
        return this.name;
    }

    /**
     * This method returns the parent
     *
     * @return                                      the category parent of this category
     */
    public Category getParent(){
        return parent;
    }

    /**
     * Overrided euqals method. Two categories are equals if have the same name
     *
     * @param o                         the object to compare
     * @return                          true if the budgets are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicCategory that = (BasicCategory) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return ""+name;
    }

}
