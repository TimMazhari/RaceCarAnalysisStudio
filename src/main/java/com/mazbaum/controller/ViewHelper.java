package com.mazbaum.controller;

import com.mazbaum.RCASMain;
import com.mazbaum.storage.Storage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class ViewHelper {

    private static boolean diagramViewOpen = false;
    private static DiagramController diagramController;

    public static void createRaceCarView(Stage stage){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(RCASMain.class.getResource("/fxml/RacecarView.fxml"));
            ResourceBundle resourceBundle = ResourceBundle.getBundle("RCASResources");
            fxmlLoader.setResources(resourceBundle);

            Scene scene = new Scene((Parent) fxmlLoader.load(), 1400, 890);

            stage.getIcons().add(new Image(RCASMain.class.getResourceAsStream("/images/Icon.png")));
            stage.centerOnScreen();
            stage.setTitle(resourceBundle.getString("applicationTitle"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void createDiagramView(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(RacecarController.class.getResource("/fxml/DiagramView.fxml"));
            ResourceBundle resourceBundle = ResourceBundle.getBundle("RCASResources");
            fxmlLoader.setResources(resourceBundle);

            Scene scene = new Scene((Parent) fxmlLoader.load(), 933, 890);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(RCASMain.class.getResourceAsStream("/images/Icon.png")));
            stage.setTitle(resourceBundle.getString("diagramTitle"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            diagramViewOpen = true;
            diagramController = fxmlLoader.getController();
            updateCarData();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateCarData(){
        if(diagramViewOpen){
            diagramController.updateRaceCarData(Storage.getSelectedRaceCar());
        }
    }

}
