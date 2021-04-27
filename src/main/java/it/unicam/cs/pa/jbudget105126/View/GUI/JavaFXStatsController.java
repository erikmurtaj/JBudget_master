package it.unicam.cs.pa.jbudget105126.View.GUI;

import it.unicam.cs.pa.jbudget105126.Model.Category;
import it.unicam.cs.pa.jbudget105126.Model.Implementation.WrongDateRangeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaFXStatsController implements Initializable {

    private Logger logger = Logger.getLogger("it.unicam.cs.pa.jbudget105126.View.GUI.JavaFXStatsController");

    private final StageManager stageManager = new StageManager();

    @FXML DatePicker startDate_choice;
    @FXML DatePicker endDate_choice;
    @FXML RadioButton filterByCategory_radio;
    @FXML ChoiceBox<Category> category_choiceBox;
    @FXML Button addFilter_button;

    @FXML LineChart<String, Double> balance_lineChart;

    final CategoryAxis xAxis = new CategoryAxis();
    final ValueAxis yAxis = new NumberAxis();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing the stats stage...");
        final CategoryAxis xAxis = new CategoryAxis();
        final ValueAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");
        yAxis.setLabel("Amount");
        balance_lineChart.setTitle("Balance Trend");
        filterByCategory_radio.setDisable(true);
        category_choiceBox.setDisable(true);
        addFilter_button.setDisable(true);
        this.setCategory_choiceBox();
        java.time.LocalDate maxDate = java.time.LocalDate.now();
        stageManager.restrictDatePicker(startDate_choice, java.time.LocalDate.MIN, maxDate);
        stageManager.restrictDatePicker(endDate_choice, java.time.LocalDate.MIN, maxDate);
    }

    public void generateChart_button(ActionEvent actionEvent) {
        XYChart.Series balance = null;
        logger.finest("Generating the chart...");
        try {
            balance=this.series_generator("Total Balance", null);
            balance_lineChart.getData().clear();
            balance_lineChart.getData().add(balance);
            filterByCategory_radio.setDisable(false);
        } catch (WrongDateRangeException e) {
            stageManager.exceptionStage(e);
            logger.severe("Error generating the chart: "+ e.getMessage());
        }
    }

    public void addFilter_button(ActionEvent actionEvent) {
        XYChart.Series balance_category=null;
        logger.finest("Adding a filter in the chart...");
        try {
            balance_category=this.series_generator(category_choiceBox.getValue().getName(), category_choiceBox.getValue());
            balance_lineChart.getData().add(balance_category);
            filterByCategory_radio.setDisable(false);
        } catch (WrongDateRangeException e) {
            stageManager.exceptionStage(e);
            logger.severe("Error adding a filter in the chart: "+ e.getMessage());
        }
    }

    public void filterByCategory_radio(ActionEvent actionEvent) {
        if(filterByCategory_radio.isArmed()) {
            category_choiceBox.setDisable(false);
            addFilter_button.setDisable(false);
        }
    }

    private void setCategory_choiceBox(){
        List<Category> categories=JavaFXExpenseManager.controller.getCategories();
        categories.forEach(category -> category_choiceBox.getItems().add(category));
    }

    public void startDate_choice(ActionEvent actionEvent) {
        java.time.LocalDate start_date= java.time.LocalDate.of(startDate_choice.getValue().getYear(), startDate_choice.getValue().getMonthValue(), startDate_choice.getValue().getDayOfMonth());
        java.time.LocalDate maxDate = java.time.LocalDate.now();
        stageManager.restrictDatePicker(endDate_choice, start_date, maxDate);
    }

    /**
     * This method make a series for the chart using an array of the daily trend of the balance during the period of time
     *  between the two dates.
     *
     *  If the period of time is more than one month, the series will save the trend in period of 30 days, for a better view.
     *
     *  If category is not null this method will generate the series filtering the transaction by that category.
     *
     * @param name                                          the name of the series
     * @param category                                      the category to use as a filter
     * @return                                              a series to add in a line chart
     * @throws WrongDateRangeException                      exception if the range date is not valid
     */
    private XYChart.Series series_generator(String name, Category category) throws WrongDateRangeException {
        if(startDate_choice.getValue()==null || endDate_choice.getValue()==null) throw new WrongDateRangeException();

        LocalDate start_date=new LocalDate(startDate_choice.getValue().getYear(), startDate_choice.getValue().getMonthValue(),
                startDate_choice.getValue().getDayOfMonth());
        LocalDate end_date=new LocalDate(endDate_choice.getValue().getYear(), endDate_choice.getValue().getMonthValue(),
                endDate_choice.getValue().getDayOfMonth());

        if(Days.daysBetween(start_date, end_date).getDays()<=0) throw new WrongDateRangeException();

        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        Double[] values = JavaFXExpenseManager.controller.balanceTrend(start_date, end_date, category);

        if(Months.monthsBetween(start_date, end_date).getMonths() > 1){
            int count=0;
            for(double x : values){
                if(count%30==0)
                    series.getData().add(new XYChart.Data(""+start_date, x));
                start_date=start_date.plusDays(1);
                count++;
            }
        }
        else {
            for(double x : values){
                series.getData().add(new XYChart.Data(""+start_date, x));
                start_date=start_date.plusDays(1);
            }
        }
        return series;
    }
}
