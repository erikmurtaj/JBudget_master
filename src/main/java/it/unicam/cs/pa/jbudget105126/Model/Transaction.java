package it.unicam.cs.pa.jbudget105126.Model;

//import java.time.LocalDate;
import org.joda.time.LocalDate;
import java.util.List;

public interface Transaction {

    String getName();

    String getDescription();

    List<Movement> getMovements();

    LocalDate getDate();

    List<Category> getCategories();

    void addCategory(Category category);

    void addMovement(Movement movement);

    double getTotal();

    void removeCategory(Category category);

}
