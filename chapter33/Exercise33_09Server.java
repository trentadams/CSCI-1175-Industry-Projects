package chapter33;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Exercise33_09Server extends Application {
    private TextArea taServer = new TextArea(); // Display history
    private TextArea taClient = new TextArea(); // Input message area

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void start(Stage primaryStage) {
        taServer.setWrapText(true);
        taServer.setEditable(false); // History should be non-editable
        taClient.setWrapText(true);

        // Layouts
        BorderPane pane1 = new BorderPane();
        pane1.setTop(new Label("History"));
        pane1.setCenter(new ScrollPane(taServer));
        BorderPane pane2 = new BorderPane();
        pane2.setTop(new Label("New Message"));
        pane2.setCenter(new ScrollPane(taClient));

        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(pane1, pane2);

        // Create a scene and place it in the stage
        Scene scene = new Scene(vBox, 400, 400);
        primaryStage.setTitle("Server Chat");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle Enter key press to send message
        taClient.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                sendMessage(taClient.getText().trim());
                taClient.clear(); // Clear the text area after sending
            }
        });

        // Start the server thread to listen for client connections
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            // Create server socket and start listening on port 12345
            serverSocket = new ServerSocket(8000);
            clientSocket = serverSocket.accept(); // Accept client connection
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                // Append message to the history
                taServer.appendText("Client: " + clientMessage + "\n");
            }
        } catch (IOException e) {
            taServer.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            out.println(message); // Send message to the client
            taServer.appendText("Server: " + message + "\n");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}