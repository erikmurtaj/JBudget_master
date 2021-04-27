package it.unicam.cs.pa.jbudget105126;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXExpenseManager;
import javafx.application.Application;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;

public class App {

    private static void launchGui() {
        Application.launch(JavaFXExpenseManager.class);
    }

    public static void main(String[] args) throws IOException {
        launchGui();
    }
}
