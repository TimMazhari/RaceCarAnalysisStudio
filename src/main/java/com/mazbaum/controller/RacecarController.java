package com.mazbaum.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.mazbaum.model.RaceCar;
import com.mazbaum.storage.Storage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class RacecarController {

    @FXML
    private Pane mainPane;


    @FXML
    private Button createButton;

    @FXML
    private JFXButton diagramButton;

    @FXML
    private JFXButton editButton;

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
        //Slider settings
        setWheelBaseSliderSettings();
        setOnSlide(cogSlider, cogInput);
        setOnSlide(wheelBaseSlider, wheelBaseInput);
        setOnSlide(frontRollDistSlider, frontRollDistInput);
        setOnSlide(cornerWeightFLSlider, cornerWeightFLInput);
        setOnSlide(cornerWeightRLSlider, cornerWeightRLInput);
        setOnSlide(cornerWeightRRSlider, cornerWeightRRInput);
        setOnSlide(cornerWeightFRSlider, cornerWeightFRInput);

        //Input fields settings
        setOnInput(cogSlider, cogInput);
        setOnInput(wheelBaseSlider, wheelBaseInput);
        setOnInput(frontRollDistSlider, frontRollDistInput);
        setOnInput(cornerWeightFLSlider, cornerWeightFLInput);
        setOnInput(cornerWeightRLSlider, cornerWeightRLInput);
        setOnInput(cornerWeightRRSlider, cornerWeightRRInput);
        setOnInput(cornerWeightFRSlider, cornerWeightFRInput);
        setOnInput(frontTrackInput);
        setOnInput(rearTrackInput);

        //List clicked Event
        setListClickedEvent();

        //Reloads RaceCarList
        updateRaceCarList();

        //Set default disabled
        setChangeAndSaveDisabled(true);
        setAllFieldsAndSliderDisabled(true);

    }

    private void setAllFieldsAndSliderDisabled(boolean disabled) {
        nameInput.setDisable(disabled);
        frontTrackInput.setDisable(disabled);
        cornerWeightFLInput.setDisable(disabled);
        cornerWeightRLInput.setDisable(disabled);
        rearTrackInput.setDisable(disabled);
        cornerWeightRRInput.setDisable(disabled);
        cornerWeightFRInput.setDisable(disabled);
        cogInput.setDisable(disabled);
        frontRollDistInput.setDisable(disabled);
        wheelBaseInput.setDisable(disabled);

        cogSlider.setDisable(disabled);
        wheelBaseSlider.setDisable(disabled);
        frontRollDistSlider.setDisable(disabled);
        cornerWeightFLSlider.setDisable(disabled);
        cornerWeightRLSlider.setDisable(disabled);
        cornerWeightRRSlider.setDisable(disabled);
        cornerWeightFRSlider.setDisable(disabled);
    }

    private void setChangeAndSaveDisabled(boolean disabled) {
        editButton.setDisable(disabled);
        saveButton.setDisable(disabled);
    }

    private void setListClickedEvent() {
        raceCarList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String raceCarName = raceCarList.getSelectionModel().getSelectedItem();
                RaceCar raceCar = Storage.findRaceCar(raceCarName);
                Storage.setSelectedRaceCar(raceCar);
                setFields(raceCar);
                setAllFieldsAndSliderDisabled(true);
                saveButton.setDisable(true);
                editButton.setDisable(false);
            }
        });
    }

    private void setFields(RaceCar raceCar){
        nameInput.setText(raceCar.getName());
        frontTrackInput.setText(String.valueOf(raceCar.getFrontTrack()));
        cornerWeightFLInput.setText(String.valueOf(raceCar.getCornerWeightFL()));
        cornerWeightRLInput.setText(String.valueOf(raceCar.getCornerWeightRL()));
        rearTrackInput.setText(String.valueOf(raceCar.getRearTrack()));
        cornerWeightRRInput.setText(String.valueOf(raceCar.getCornerWeightRR()));
        cornerWeightFRInput.setText(String.valueOf(raceCar.getCornerWeightFR()));
        cogInput.setText(String.valueOf(raceCar.getCogHeight()));
        frontRollDistInput.setText(String.valueOf(raceCar.getFrontRollDist()));
        wheelBaseInput.setText(String.valueOf(raceCar.getWheelbase()));
    }

    private void setOnInput(final JFXTextField textField) {
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

    private void setOnInput(final JFXSlider slider, final JFXTextField textField) {
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

    private void setOnSlide(JFXSlider cogSlider, final JFXTextField cogInput) {
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

    public void editButtonClicked() {
        setChangeAndSaveDisabled(false);
        setAllFieldsAndSliderDisabled(false);
    }

    public void saveButtonClicked() {
        boolean minOneError = false;
        HashMap<JFXTextField, Boolean> errors = validateFields();

        for(Map.Entry<JFXTextField, Boolean> entry : errors.entrySet()) {
            JFXTextField textField = entry.getKey();
            Boolean value = entry.getValue();

            if(value){
                // TODO Style error field
                //textField.setStyle();
                minOneError = true;
            }
        }

        if(!minOneError){

        }
    }

    private HashMap<JFXTextField, Boolean> validateFields() {
        HashMap<JFXTextField, Boolean> errors = new HashMap<>();
        errors.put(nameInput, validateField(nameInput.getText()));
        errors.put(frontTrackInput, validateField(frontTrackInput.getText(), 0D, 1000D));
        errors.put(cornerWeightFLInput, validateField(cornerWeightFLInput.getText(), 50D, 1000D));
        errors.put(cornerWeightRLInput, validateField(cornerWeightRLInput.getText(), 50D, 1000D));
        errors.put(rearTrackInput, validateField(rearTrackInput.getText(), 0D, 1000D));
        errors.put(cornerWeightRRInput, validateField(cornerWeightRRInput.getText(), 50D, 1000D));
        errors.put(cornerWeightFRInput, validateField(cornerWeightFRInput.getText(), 50D, 1000D));
        errors.put(cogInput, validateField(cogInput.getText(), 10D, 200D));
        errors.put(frontRollDistInput, validateField(frontRollDistInput.getText(), 20D, 80D));
        errors.put(wheelBaseInput, validateField(wheelBaseInput.getText(), 0.5D, 6D));
        return errors;
    }

    private Boolean validateField(String carName) {
        if (carName == null || carName.isEmpty() || carName.length() > 200)
            return true;
        else {
            if(Storage.getSelectedRaceCar().getName().equals(carName)){
                return false;
            }else{
                for(RaceCar raceCar : Storage.getAllRaceCars()){
                    if(raceCar.getName().equals(carName)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean validateField(String text, double min, double max) {
        try {
            Double input = Double.valueOf(text);
            if (input < min || input > max)
                return true;
            return false;
        }catch (Exception e){
            return true;
        }
    }
}
