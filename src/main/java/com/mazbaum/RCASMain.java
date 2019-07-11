package com.mazbaum;

import java.util.ResourceBundle;

import com.mazbaum.controller.ViewHelper;
import com.mazbaum.util.RaceCarInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

public class RCASMain extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		ViewHelper.createRaceCarView(stage);
	}

	public static void main(String[] args) {
		RaceCarInitializer.initRaceCar();
		Application.launch(RCASMain.class, args);
	}
}
