package chapter33;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class Exercise33_01Client extends Application {
    // Text field for receiving loan details
    private TextField tfAnnualInterestRate = new TextField();
    private TextField tfNumOfYears = new TextField();
    private TextField tfLoanAmount = new TextField();
    private Button btSubmit = new Button("Submit");

    // Text area to display results
    private TextArea ta = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        ta.setWrapText(true);

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Annual Interest Rate"), 0, 0);
        gridPane.add(new Label("Number Of Years"), 0, 1);
        gridPane.add(new Label("Loan Amount"), 0, 2);
        gridPane.add(tfAnnualInterestRate, 1, 0);
        gridPane.add(tfNumOfYears, 1, 1);
        gridPane.add(tfLoanAmount, 1, 2);
        gridPane.add(btSubmit, 2, 1);

        tfAnnualInterestRate.setAlignment(Pos.BASELINE_RIGHT);
        tfNumOfYears.setAlignment(Pos.BASELINE_RIGHT);
        tfLoanAmount.setAlignment(Pos.BASELINE_RIGHT);

        tfLoanAmount.setPrefColumnCount(5);
        tfNumOfYears.setPrefColumnCount(5);
        tfLoanAmount.setPrefColumnCount(5);

        BorderPane pane = new BorderPane();
        pane.setCenter(new ScrollPane(ta));
        pane.setTop(gridPane);

        // Button action to send loan info to server
        btSubmit.setOnAction(e -> sendLoanInfoToServer());

        // Scene setup
        Scene scene = new Scene(pane, 400, 250);
        primaryStage.setTitle("Exercise33_01Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    // Method to send loan info to server
    private void sendLoanInfoToServer() {
        try {
            // Get loan details from text fields
            double annualInterestRate = Double.parseDouble(tfAnnualInterestRate.getText());
            int numberOfYears = Integer.parseInt(tfNumOfYears.getText());
            double loanAmount = Double.parseDouble(tfLoanAmount.getText());

            // Connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Send loan details to the server
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeDouble(annualInterestRate);
            output.writeInt(numberOfYears);
            output.writeDouble(loanAmount);

            // Receive results from the server
            DataInputStream input = new DataInputStream(socket.getInputStream());
            double monthlyPayment = input.readDouble();
            double totalPayment = input.readDouble();

            // Display results in the text area
            ta.appendText("Monthly Payment: " + monthlyPayment + "\n");
            ta.appendText("Total Payment: " + totalPayment + "\n");

            // Close connections
            output.close();
            input.close();
            socket.close();
        } catch (IOException ex) {
            ta.appendText("Error: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}