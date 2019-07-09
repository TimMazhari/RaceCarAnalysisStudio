package com.mazbaum.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.mazbaum.model.RaceCar;
import com.mazbaum.model.dto.RaceCarDto;
import com.mazbaum.storage.Storage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class RacecarController {

    @FXML
    private Pane mainPane;


    @FXML
    private Button createButton;

    @FXML
    private JFXButton saveButton;


    @FXML
    private JFXTextField nameInput;

    @FXML
    private JFXTextField frontTrackInput;

    @FXML
    private JFXTextField cornerWeightFRInput;

    @FXML
    private JFXTextField cornerWeightRLInput;

    @FXML
    private JFXTextField cornerWeightFLInput;

    @FXML
    private JFXTextField rearTrackInput;

    @FXML
    private JFXTextField cornerWeightRRInput;

    @FXML
    private JFXTextField cogInput;

    @FXML
    private JFXTextField frontRollDistInput;

    @FXML
    private JFXTextField wheelBaseInput;


    @FXML
    private JFXSlider cornerWeightFRSlider;

    @FXML
    private JFXSlider cornerWeightRRSlider;

    @FXML
    private JFXSlider cornerWeightRLSlider;

    @FXML
    private JFXSlider cornerWeightFLSlider;

    @FXML
    private JFXSlider frontRollDistSlider;

    @FXML
    private JFXSlider cogSlider;

    @FXML
    private JFXSlider wheelBaseSlider;

    @FXML
    private JFXListView<String> raceCarList;


    @FXML
    public void initialize() {
        setWheelBaseSliderSettings();

        onSlide(cogSlider, cogInput);
        onSlide(wheelBaseSlider, wheelBaseInput);
        onSlide(frontRollDistSlider, frontRollDistInput);
        onSlide(cornerWeightFLSlider, cornerWeightFLInput);
        onSlide(cornerWeightRLSlider, cornerWeightRLInput);
        onSlide(cornerWeightRRSlider, cornerWeightRRInput);
        onSlide(cornerWeightFRSlider, cornerWeightFRInput);

        onInput(cogSlider, cogInput);
        onInput(wheelBaseSlider, wheelBaseInput);
        onInput(frontRollDistSlider, frontRollDistInput);
        onInput(cornerWeightFLSlider, cornerWeightFLInput);
        onInput(cornerWeightRLSlider, cornerWeightRLInput);
        onInput(cornerWeightRRSlider, cornerWeightRRInput);
        onInput(cornerWeightFRSlider, cornerWeightFRInput);

        onInput(frontTrackInput);
        onInput(rearTrackInput);

        updateRaceCarList();
    }

    private void onInput(final JFXTextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(!newValue.isEmpty()){
                        Double.parseDouble(newValue);
                    }
                }catch (NumberFormatException nfe){
                    textField.setText(oldValue);
                }
            }
        });
    }

    private void onInput(final JFXSlider slider, final JFXTextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    //TODO Bugs because slider
                    //TODO Somewhere a "Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: The start must be <= the end"
                    if(!newValue.isEmpty()){
                        if(newValue.substring(newValue.length() - 1).equals(".")){
                            newValue = newValue + "0";
                        }
                        double input = Double.parseDouble(newValue);
                        if(input > slider.getMax()){
                            slider.setValue(slider.getMax());
                        }else if (input < slider.getMin()){
                            slider.setValue(slider.getMin());
                        }else{
                            slider.setValue(input);
                        }
                    }
                }catch (NumberFormatException nfe){
                    textField.setText(oldValue);
                }
            }
        });
    }

    private void updateRaceCarList() {
        raceCarList.getItems().clear();
        for(RaceCar raceCar : Storage.getAllRaceCars()) {
            raceCarList.getItems().add(raceCar.getName());
        }
    }

    private void onSlide(JFXSlider cogSlider, final JFXTextField cogInput) {
        cogSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //TODO Round on 1 or more?
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.HALF_UP);
                cogInput.setText(df.format(newValue));
            }
        });
    }

    private void setWheelBaseSliderSettings() {
        wheelBaseSlider.setValueFactory(new Callback<JFXSlider, StringBinding>() {
            @Override
            public StringBinding call(JFXSlider arg0) {
                return Bindings.createStringBinding(new java.util.concurrent.Callable<String>(){
                    @Override
                    public String call() {
                        DecimalFormat df = new DecimalFormat("#.0");
                        return df.format(wheelBaseSlider.getValue());
                    }
                }, wheelBaseSlider.valueProperty());
            }
        });
    }

}
