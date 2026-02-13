package it.unicam.cs.mpgc.jtime126294;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    
    private it.unicam.cs.mpgc.jtime126294.controller.MainController controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        primaryStage.setTitle("JTime Management System - 126294");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null) {
            controller.saveData();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
