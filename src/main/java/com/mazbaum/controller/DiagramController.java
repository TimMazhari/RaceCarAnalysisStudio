package com.mazbaum.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mazbaum.model.RaceCar;
import com.mazbaum.util.CorneringAnalyserUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

import static java.lang.Math.sqrt;

public class DiagramController {

    @FXML
    private JFXTextField curveRadiusInput;

    @FXML
    private JFXTextField frictionOnRoadInput;

    @FXML
    private JFXTextField maxSpeedOutput;

    @FXML
    private JFXComboBox speedUnit;

    @FXML
    private JFXTextField nameInput;

    @FXML
    private JFXTextField frontTrackInput;

    @FXML
    private JFXTextField rearTrackInput;

    @FXML
    private JFXTextField cornerWeightFLInput;

    @FXML
    private JFXTextField cornerWeightRLInput;

    @FXML
    private JFXTextField cornerWeightRRInput;

    @FXML
    private JFXTextField cornerWeightFRInput;

    @FXML
    private JFXTextField cogInput;

    @FXML
    private JFXTextField frontRollDistanceInput;

    @FXML
    private JFXTextField wheelbaseInput;

    @FXML
    private LineChart<Number, Number> mainChart;

    @FXML
    private Text toHighCog;

    @FXML
    public void initialize(){

        speedUnit.getItems().addAll(SpeedUnits.values());
        speedUnit.getSelectionModel().selectFirst();
        disableInputFields();
        setOnInput(curveRadiusInput);
        setOnInput(frictionOnRoadInput);
    }

    private void disableInputFields() {
        nameInput.setDisable(true);
        frontTrackInput.setDisable(true);
        rearTrackInput.setDisable(true);
        cornerWeightFLInput.setDisable(true);
        cornerWeightRLInput.setDisable(true);
        cornerWeightRRInput.setDisable(true);
        cornerWeightFRInput.setDisable(true);
        cogInput.setDisable(true);
        frontRollDistanceInput.setDisable(true);
        wheelbaseInput.setDisable(true);
        maxSpeedOutput.setDisable(true);
    }

    public void updateRaceCarData(RaceCar raceCar){
        nameInput.setText(raceCar.getName());
        frontTrackInput.setText(String.valueOf(raceCar.getFrontTrack()));
        rearTrackInput.setText(String.valueOf(raceCar.getRearTrack()));
        cornerWeightFLInput.setText(String.valueOf(raceCar.getCornerWeightFL()));
        cornerWeightRLInput.setText(String.valueOf(raceCar.getCornerWeightRL()));
        cornerWeightRRInput.setText(String.valueOf(raceCar.getCornerWeightRR()));
        cornerWeightFRInput.setText(String.valueOf(raceCar.getCornerWeightFR()));
        cogInput.setText(String.valueOf(raceCar.getCogHeight()));
        frontRollDistanceInput.setText(String.valueOf(raceCar.getFrontRollDist()));
        wheelbaseInput.setText(String.valueOf(raceCar.getWheelbase()));
        updateDiagram(raceCar);
    }

    private void setOnInput(JFXTextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!curveRadiusInput.getText().equals("") && !frictionOnRoadInput.getText().equals("")){
                    calculateMaxSpeed(curveRadiusInput.getText(), frictionOnRoadInput.getText());
                }else{
                    maxSpeedOutput.setText("");
                }
            }
        });
    }

    public void calculateSpeedClicked(){
        calculateMaxSpeed(curveRadiusInput.getText(), frictionOnRoadInput.getText());
    }

    /**
     * The original formula for the max speed is "(friction * normalforce * radius) / mass".
     * Since normalforce is equal to "mass * gravity", we can rewrite the formula like so:
     * "(friction * mass * gravity * radius) / mass". Now the two masses cancel each other out, so we end up with the
     * formula "(friction * gravity * radius) = maxSpeed".
     * @param curveRadius String
     * @param frictionOnRoad String
     */
    public void calculateMaxSpeed(String curveRadius, String frictionOnRoad){
        double friction = Double.parseDouble(frictionOnRoad);
        double gravity = 9.81;
        double radius = Double.parseDouble(curveRadius);
        double maxSpeed = sqrt(friction * gravity * radius);

        String unit = String.valueOf(speedUnit.getValue());

        if (SpeedUnits.valueOf(unit) == SpeedUnits.KMH){
            maxSpeed = maxSpeed * 3.6;
        }else if (SpeedUnits.valueOf(unit) == SpeedUnits.MPH) {
            maxSpeed = maxSpeed * 2.2369;
        }

        maxSpeedOutput.setText(String.valueOf(maxSpeed));
    }

    private void updateDiagram(RaceCar raceCar){
        mainChart.getData().clear();
        if(raceCar.getCogHeight() < 70) {
            mainChart.setVisible(true);
            toHighCog.setVisible(false);
            CorneringAnalyserUtil corneringUtil = new CorneringAnalyserUtil();
            ObservableList<XYChart.Series<Number, Number>> dataList = corneringUtil.generateMMMChartData(raceCar);
            mainChart.getData().addAll(dataList);
            this.setSeriesStyle(dataList, ".chart-series-line", "-fx-stroke: blue; -fx-stroke-width: 1px;");
        }else{
            mainChart.setVisible(false);
            toHighCog.setVisible(true);
        }
    }

    private void setSeriesStyle(ObservableList<XYChart.Series<Number, Number>> dataList_1, String styleSelector, String lineStyle) {
        for (XYChart.Series<Number, Number> curve : dataList_1) {
            curve.getNode().lookup(styleSelector).setStyle(lineStyle);
        }
    }

}
