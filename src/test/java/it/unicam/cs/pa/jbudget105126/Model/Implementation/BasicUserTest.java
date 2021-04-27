package it.unicam.cs.pa.jbudget105126.Model.Implementation;

import it.unicam.cs.pa.jbudget105126.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserTest {

    private User user;

    @BeforeEach
    void init() throws MissingInformationException {
        user=new BasicUser("test");
    }

    @Test
    void getName() {
        assertThrows(MissingInformationException.class, () -> new BasicUser("    "));
        assertThrows(MissingInformationException.class, () -> new BasicUser(""));
        user.setName("Erik");
        assertEquals("Erik", user.getName());
    }
}