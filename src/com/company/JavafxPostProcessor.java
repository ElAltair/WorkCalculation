package com.company;/**
 * Created by Laiserg on 25.12.2016.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;

public class JavafxPostProcessor extends Application {


    public static Map<Integer, Work> map;

    public int count = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("WorkTast Calculator");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        map.forEach((integer, work) -> {
            int i = count++;
            grid.add(new VBox(new Text(integer.toString())), 0, i);
            VBox workingBox = new VBox(new Rectangle(work.getDuration()*10, 20, Paint.valueOf(work.isOnCriticalPath() ? "RED" : "BLUE")));
            workingBox.setPadding(new Insets(0,0,0,work.getStart()*10));
            grid.add(workingBox, 1, i);
        });
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
