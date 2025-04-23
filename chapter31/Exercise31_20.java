package chapter31;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Exercise31_20 extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create TabPane and tabs with shapes
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Line", new StackPane(new Line(10, 10, 80, 80)));
        Tab tab2 = new Tab("Rectangle", new Rectangle(10, 10, 200, 200));
        Tab tab3 = new Tab("Circle", new Circle(50, 50, 20));
        Tab tab4 = new Tab("Ellipse", new Ellipse(10, 10, 100, 80));

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Radio buttons for tab position
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton topBtn = new RadioButton("Top");
        RadioButton bottomBtn = new RadioButton("Bottom");
        RadioButton leftBtn = new RadioButton("Left");
        RadioButton rightBtn = new RadioButton("Right");

        topBtn.setToggleGroup(toggleGroup);
        bottomBtn.setToggleGroup(toggleGroup);
        leftBtn.setToggleGroup(toggleGroup);
        rightBtn.setToggleGroup(toggleGroup);
        topBtn.setSelected(true); // default position

        HBox radioBox = new HBox(10, new Label("Tab Position:"), topBtn, bottomBtn, leftBtn, rightBtn);
        radioBox.setPadding(new Insets(10));
        radioBox.setStyle("-fx-background-color: #f0f0f0;");

     // Listener to change tab position using if statements
        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                RadioButton selected = (RadioButton) newVal;
                String text = selected.getText();
                if (text.equals("Top")) {
                    tabPane.setSide(Side.TOP);
                } else if (text.equals("Bottom")) {
                    tabPane.setSide(Side.BOTTOM);
                } else if (text.equals("Left")) {
                    tabPane.setSide(Side.LEFT);
                } else if (text.equals("Right")) {
                    tabPane.setSide(Side.RIGHT);
                }
            }
        });
        
        // Program Layout
        BorderPane root = new BorderPane();
        root.setBottom(radioBox);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("DisplayFigure");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}