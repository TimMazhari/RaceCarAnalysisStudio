package com.mazbaum.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;

import java.awt.*;

import static java.lang.Math.sqrt;

public class DiagramController {

    @FXML
    private Pane mainPane;

    @FXML
    private JFXTextField curveRadiusInput;

    @FXML
    private JFXTextField frictionOnRoadInput;

    @FXML
    private JFXTextField maxSpeedOutput;

    @FXML
    private JFXComboBox speedUnit;

    @FXML
    private JFXButton calculateButton;

    @FXML
    public void initialize(){
        maxSpeedOutput.setDisable(true);

        speedUnit.getItems().addAll(SpeedUnits.values());
        speedUnit.getSelectionModel().selectFirst();

    }

    public void calculateSpeedClicked(){
        calculateMaxSpeed(curveRadiusInput.getText(), frictionOnRoadInput.getText(), maxSpeedOutput);
    }

    /**
     * The original formula for the max speed is "(friction * normalforce * radius) / mass".
     * Since normalforce is equal to "mass * gravity", we can rewrite the formula like so:
     * "(friction * mass * gravity * radius) / mass". Now the two masses cancel each other out, so we end up with the
     * formula "(friction * gravity * radius) = maxSpeed".
     * @param curveRadius
     * @param maxSpeedOutput
     */
    public void calculateMaxSpeed(String curveRadius, String frictionOnRoad, final JFXTextField maxSpeedOutput){
        double friction = Double.parseDouble(frictionOnRoad);
        double maxSpeed;
        double gravity = 9.81;
        double radius = Double.parseDouble(curveRadius);
        maxSpeed = sqrt(friction * gravity * radius);

        String unit;
        unit = String.valueOf(speedUnit.getValue());

        if (unit.equals("KMH")){
            maxSpeed = maxSpeed * 3.6;
        }else if (unit.equals("MPH")) {
            maxSpeed = maxSpeed * 2.2369;
        }
        String output = String.valueOf(maxSpeed);

        maxSpeedOutput.setText(output);
    }

}
