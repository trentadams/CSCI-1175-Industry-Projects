package chapter33;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Exercise33_01Server extends Application {
    private TextArea ta = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        ta.setWrapText(true);

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 400, 200);
        primaryStage.setTitle("Exercise31_01Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread(() -> {
            try {
                // Server setup
                ServerSocket serverSocket = new ServerSocket(8000);
                ta.appendText("Server started at " + new Date() + "\n");

                while (true) {
                    // Accept client connection
                    Socket socket = serverSocket.accept();
                    ta.appendText("Client connected: " + socket.getInetAddress().getHostName() + "\n");

                    // Handle client in a new thread
                    new Thread(new ClientHandler(socket)).start();
                }
            } catch (IOException ex) {
                ta.appendText("Error: " + ex.getMessage() + "\n");
            }
        }).start();
    }

    // Handler for each client
    class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Set up I/O streams
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // Read loan details from client
                double annualInterestRate = input.readDouble();
                int numberOfYears = input.readInt();
                double loanAmount = input.readDouble();

                // Create Loan object and calculate payment details
                Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
                double monthlyPayment = loan.getMonthlyPayment();
                double totalPayment = loan.getTotalPayment();

                // Send results back to client
                output.writeDouble(monthlyPayment);
                output.writeDouble(totalPayment);

                // Close streams and socket
                input.close();
                output.close();
                socket.close();

                // Update server UI
                Platform.runLater(() -> {
                    ta.appendText("Loan processed for client: \n");
                    ta.appendText("Monthly Payment: " + monthlyPayment + "\n");
                    ta.appendText("Total Payment: " + totalPayment + "\n");
                });

            } catch (IOException ex) {
                Platform.runLater(() -> ta.appendText("Error: " + ex.getMessage() + "\n"));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}