package chapter31;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Exercise31_17 extends Application {

    // Method to calculate future value
    private double calculateFutureValue(double investmentAmount, double interestRate, int years) {
        double monthlyInterestRate = interestRate / 100 / 12;
        return investmentAmount * Math.pow(1 + monthlyInterestRate, years * 12);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the user interface
        primaryStage.setTitle("Investment Calculator");

        // Create the GridPane layout for the form inputs
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Investment Amount field
        Label investmentAmountLabel = new Label("Investment Amount:");
        TextField investmentAmountField = new TextField();
        grid.add(investmentAmountLabel, 0, 0);
        grid.add(investmentAmountField, 1, 0);

        // Interest Rate field
        Label interestRateLabel = new Label("Interest Rate (%):");
        TextField interestRateField = new TextField();
        grid.add(interestRateLabel, 0, 1);
        grid.add(interestRateField, 1, 1);

        // Years field
        Label yearsLabel = new Label("Number of Years:");
        TextField yearsField = new TextField();
        grid.add(yearsLabel, 0, 2);
        grid.add(yearsField, 1, 2);

        // Future Value field
        Label futureValueLabel = new Label("Future Value:");
        TextField futureValueField = new TextField();
        futureValueField.setEditable(false);  // This field should not be edited by the user
        grid.add(futureValueLabel, 0, 3);
        grid.add(futureValueField, 1, 3);

        // Calculate button
        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> {
            try {
                double investmentAmount = Double.parseDouble(investmentAmountField.getText());
                double interestRate = Double.parseDouble(interestRateField.getText());
                int years = Integer.parseInt(yearsField.getText());

                // Calculate the future value
                double futureValue = calculateFutureValue(investmentAmount, interestRate, years);

                // Display the result in the future value text field
                futureValueField.setText(String.format("%.2f", futureValue));
            } catch (NumberFormatException ex) {
                futureValueField.setText("Invalid input!");
            }
        });
        grid.add(calculateButton, 1, 4);

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        // Create the "File" menu
        Menu fileMenu = new Menu("Operation");
        MenuItem calculateMenuItem = new MenuItem("Calculate");
        calculateMenuItem.setOnAction(e -> calculateButton.fire());
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> primaryStage.close());
        fileMenu.getItems().addAll(calculateMenuItem, exitMenuItem);

        menuBar.getMenus().add(fileMenu);

        // Create a BorderPane layout for the entire UI
        BorderPane root = new BorderPane();
        root.setTop(menuBar);  // Place the MenuBar at the top
        root.setCenter(grid);  // Place the GridPane in the center

        // Set up the Scene and Stage
        Scene scene = new Scene(root, 300, 210);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}