package it.unicam.cs.pa.jbudget105126.Model.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import it.unicam.cs.pa.jbudget105126.Model.*;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.*;
import org.joda.time.LocalDate;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.logging.Logger;

import static it.unicam.cs.pa.jbudget105126.Model.Persistence.InterfaceSerializer.interfaceSerializer;

public final class JsonDataManager<T extends ExpenseManagerState> implements DataManager{

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.Model.Persistence.JsonDataManager");

    private static final String FILE_ACCOUNTS = "accounts.json";
    private static final String FILE_BUDGETS = "budget.json";
    private static final String FILE_CATEGORIES = "categories.json";

    private T state;
    private File file_accounts;
    private File file_budgets;
    private File file_categories;

    private Gson gson = new GsonBuilder()
                .registerTypeAdapter(Account.class, interfaceSerializer(BasicAccount.class))
                .registerTypeAdapter(Transaction.class, interfaceSerializer(BasicTransaction.class))
                .registerTypeAdapter(ScheduledTransaction.class, interfaceSerializer(BasicScheduledTransaction.class))
                .registerTypeAdapter(Movement.class, interfaceSerializer(BasicMovement.class))
                .registerTypeAdapter(Category.class, interfaceSerializer(BasicCategory.class))
                .registerTypeAdapter(User.class, interfaceSerializer(BasicUser.class))
                .registerTypeAdapter(Budget.class, interfaceSerializer(BasicBudget.class))
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .setPrettyPrinting()
                .create();

    /**
     * Constructor method
     *
     * @param state                                 the state of the application
     * @param filename_accounts                     the filename to save the accounts
     * @param filename_budgets                      the filename to save the budgets
     * @param filename_categories                   the filename to save the categories
     */
    public JsonDataManager(T state, String filename_accounts, String filename_budgets, String filename_categories) {
        this.state = state;
        file_accounts = new File(filename_accounts);
        file_budgets = new File(filename_budgets);
        file_categories = new File(filename_categories);
    }

    /**
     * This method saves the state calling the save_to_file() method for each file
     */
    public void save(){
        logger.info("Saving data to files...");
        save_to_file(state.getAccounts(), file_accounts);
        save_to_file(state.getBudgets(), file_budgets);
        save_to_file(state.getCategories(), file_categories);
    }

    /**
     * This method loads the state calling the load() method for each file. It also get the config info of the
     *  application
     */
    public void load() {
        // Load config values from the default location application.conf
        logger.info("Loading config data...");
        Config conf = ConfigFactory.load();
        Currency c  = Currency.getInstance(conf.getString("expense-manager.currency"));
        state.setCurrency(c);
        state.addAccounts(load(file_accounts, new TypeToken<List<Account>>() {}.getType()));
        state.addBudgets(load(file_budgets, new TypeToken<List<Budget>>() {}.getType()));
        state.addCategories(load(file_categories, new TypeToken<List<Category>>() {}.getType()));
    }

    /**
     * This method saves to the file a list of object to save using gson. The file will be a json file
     *
     * @param to_save                           a list of object to save
     * @param file                              the file to save into
     */
    public void save_to_file(List to_save, File file){
        Writer writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            logger.severe("Error saving data to files: "+e.getMessage());
        }
        gson.toJson(to_save, writer);
        try {
            writer.close();
        } catch (IOException e) {
            logger.severe("Error saving data to files: "+e.getMessage());
        }
    }

    /**
     * This method loads a list of object from a json file using a type token
     *
     * @param file                              the file from where load the data
     * @param type_token                        the type token of the objects
     * @return                                  a list of a objects defined by a type token
     */
    private List<?> load(File file, Type type_token) {
        logger.info("Loading data from files...");
        List<Account> to_return=new ArrayList<>();
        try (JsonReader file_reader = new JsonReader(new FileReader(file)))
        {
            to_return = gson.fromJson(file_reader, type_token);

        } catch (FileNotFoundException e) {
            logger.severe(e.getMessage()+". It will be created...");
        } catch (IOException e) {
            logger.severe(e.getMessage()+" ("+ file.getName()+" )");
        }
        return to_return;
    }
}
