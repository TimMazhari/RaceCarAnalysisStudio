package com.mazbaum;

import java.util.ResourceBundle;

import com.mazbaum.util.RaceCarInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RCASMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(RCASMain.class.getResource("/fxml/Racecar.fxml"));
		ResourceBundle resourceBundle = ResourceBundle.getBundle("RCASResources");
		fxmlLoader.setResources(resourceBundle);

		Pane mainPane = fxmlLoader.load();
		Scene mainScene = new Scene(mainPane, 1400, 890);
		primaryStage.getIcons().add(new Image(RCASMain.class.getResourceAsStream("/images/Icon.png")));
		primaryStage.centerOnScreen();
		primaryStage.setTitle(resourceBundle.getString("applicationTitle"));
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		RaceCarInitializer.initRaceCar();
		Application.launch(RCASMain.class, args);
	}
}
