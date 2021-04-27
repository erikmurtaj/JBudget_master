package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.DataCreator;
import it.unicam.cs.pa.jbudget105126.Model.Movement;
import it.unicam.cs.pa.jbudget105126.Model.MovementType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicMovementTest {

    @Test
    void getName() throws MissingInformationException {
        Movement movement= new BasicMovement("Movement test", MovementType.INCOME, 50);
        assertThrows(MissingInformationException.class, () -> new BasicCategory("    "));
        assertThrows(MissingInformationException.class, () -> new BasicCategory(""));
        assertEquals("Movement test", movement.getName());
    }

    @Test
    void getValue() {
        DataCreator.getListMovements(5, MovementType.INCOME, 5).forEach(
                movement -> assertEquals(5, movement.getValue()));
    }
}