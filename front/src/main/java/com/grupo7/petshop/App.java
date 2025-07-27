package com.grupo7.petshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("MainView"), 640, 480);
        stage.setTitle("Pet Shop - Sistema de Gestão");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        String fxmlName = fxml.endsWith(".fxml") ? fxml : fxml + ".fxml";
        System.out.println("Tentando carregar FXML: " + fxmlName);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/grupo7/petshop/" + fxmlName));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        com.grupo7.petshop.model.DatabaseManager.close();
    }
}