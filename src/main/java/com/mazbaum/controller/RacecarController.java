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

    //Initialize

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
        setFieldsEmpty();
        setButtonsDisabled(true, true, true);
        setAllFieldsAndSliderDisabled(true);

    }

    //Click

    /**
     * Handles the edit button click
     */
    public void editButtonClicked() {
        setButtonsDisabled(false, true, true);
        setAllFieldsAndSliderDisabled(false);
    }

    /**
     * Handles the save button click
     */
    public void saveButtonClicked() {
        clearInputFieldStyle();
        boolean minOneError = false;
        HashMap<JFXTextField, Boolean> errors = validateFields();

        for(Map.Entry<JFXTextField, Boolean> entry : errors.entrySet()) {
            JFXTextField textField = entry.getKey();
            Boolean value = entry.getValue();

            if(value){
                textField.styleProperty().setValue("-fx-text-fill: #e15f50;");
                minOneError = true;
            }
        }

        if(!minOneError){
            saveRaceCar();
            updateRaceCarList();
            selectRaceCarInListView();
            setButtonsDisabled(true, false, false);
            setAllFieldsAndSliderDisabled(true);
        }
    }

    /**
     * Handles the create button click
     */
    public void createButtonClicked() {
        clearInputFieldStyle();
        setAllFieldsAndSliderDisabled(false);
        setButtonsDisabled(false, true, true);
        setDefaultValues();
        Storage.setSelectedRaceCar(null);
    }

    //Events

    /**
     * Sets the list clicked event
     */
    private void setListClickedEvent() {
        raceCarList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String raceCarName = raceCarList.getSelectionModel().getSelectedItem();
                RaceCar raceCar = Storage.findRaceCar(raceCarName);
                Storage.setSelectedRaceCar(raceCar);
                clearInputFieldStyle();
                setFields(raceCar);
                setAllFieldsAndSliderDisabled(true);
                setButtonsDisabled(true, false, false);
            }
        });
    }

    //Listener

    /**
     * Sets the listener for the text fields without slider
     * @param textField JFXTextField
     */
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

    /**
     * Sets the listener for the textfields with slider
     * @param slider JFXSlider
     * @param textField JFXTextField
     */
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

    /**
     * Sets the lister for sliders to write the slidervalue into the text fields
     * @param cogSlider JFXSlider
     * @param cogInput JFXTextField
     */
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

    //ValueFactory

    /**
     * Sets the listener for the baseslider and the input field of the baseslider
     */
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

    //Refactor Methods

    /**
     * Sets all fields emty only for start
     */
    private void clearInputFieldStyle(){
        cogInput.styleProperty().setValue("");
        wheelBaseInput.styleProperty().setValue("");
        frontRollDistInput.styleProperty().setValue("");
        cornerWeightFLInput.styleProperty().setValue("");
        cornerWeightRLInput.styleProperty().setValue("");
        cornerWeightRRInput.styleProperty().setValue("");
        cornerWeightFRInput.styleProperty().setValue("");
    }

    /**
     * Sets all fields and slider disabled
     * @param disabled boolean
     */
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

    /**
     * Method to disable or enable buttons
     * @param save boolean
     * @param edit boolean
     * @param diagram boolean
     */
    private void setButtonsDisabled(boolean save, boolean edit, boolean diagram) {
        saveButton.setDisable(save);
        editButton.setDisable(edit);
        diagramButton.setDisable(diagram);
    }

    /**
     * Sets the fields with the value of the racecar
     * @param raceCar RaceCar
     */
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

    /**
     * Sets all fields to empty
     */
    private void setFieldsEmpty(){
        nameInput.setText("");
        frontTrackInput.setText("");
        cornerWeightFLInput.setText("");
        cornerWeightRLInput.setText("");
        rearTrackInput.setText("");
        cornerWeightRRInput.setText("");
        cornerWeightFRInput.setText("");
        cogInput.setText("");
        frontRollDistInput.setText("");
        wheelBaseInput.setText("");
    }

    /**
     * Updates the racecarlist
     */
    private void updateRaceCarList() {
        raceCarList.getItems().clear();
        for(RaceCar raceCar : Storage.getAllRaceCars()) {
            raceCarList.getItems().add(raceCar.getName());
        }
    }

    /**
     * Saves the racecar if there is one selected else does create a new race car
     */
    private void saveRaceCar() {
        if(Storage.getSelectedRaceCar() == null){
            RaceCar raceCar = new RaceCar(
                    Storage.getNewId(),
                    Double.valueOf(cornerWeightFLInput.getText()),
                    Double.valueOf(cornerWeightFRInput.getText()),
                    Double.valueOf(cornerWeightRLInput.getText()),
                    Double.valueOf(cornerWeightRRInput.getText())
            );
            setRaceCarDefaultsToValues(raceCar);
            Storage.addRaceCar(raceCar);
            Storage.setSelectedRaceCar(raceCar);
        }else{
            RaceCar raceCar = Storage.getSelectedRaceCar();
            raceCar.setCornerWeightFL(Double.valueOf(cornerWeightFLInput.getText()));
            raceCar.setCornerWeightFR(Double.valueOf(cornerWeightFRInput.getText()));
            raceCar.setCornerWeightRL(Double.valueOf(cornerWeightRLInput.getText()));
            raceCar.setCornerWeightRR(Double.valueOf(cornerWeightRRInput.getText()));
            setRaceCarDefaultsToValues(raceCar);
            Storage.replaceRaceCar(raceCar);
            Storage.setSelectedRaceCar(raceCar);
        }
    }

    /**
     * Sets the defaults of a racecar to the imputs
     * @param raceCar RaceCar
     */
    private void setRaceCarDefaultsToValues(RaceCar raceCar) {
        raceCar.setName(nameInput.getText());
        raceCar.setFrontTrack(Double.valueOf(frontTrackInput.getText()));
        raceCar.setRearTrack(Double.valueOf(rearTrackInput.getText()));
        raceCar.setCogHeight(Double.valueOf(cogInput.getText()));
        raceCar.setFrontRollDist(Double.valueOf(frontRollDistInput.getText()));
        raceCar.setWheelbase(Double.valueOf(wheelBaseInput.getText()));
    }

    /**
     * Validates all fields
     * @return HashMap &lt;JFXTextField, Boolean&gt;
     */
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

    /**
     * Validates the carnamefiled
     * @param carName String
     * @return boolean
     */
    private boolean validateField(String carName) {
        if (carName == null || carName.isEmpty() || carName.length() > 200)
            return true;
        else {
            if(Storage.getSelectedRaceCar() == null){
                for(RaceCar raceCar : Storage.getAllRaceCars()){
                    if(raceCar.getName().equals(carName)){
                        return true;
                    }
                }
            }else{
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
        }
        return false;
    }

    /**
     * Validates all fields
     * @param text String
     * @param min double
     * @param max double
     * @return boolean
     */
    private boolean validateField(String text, double min, double max) {
        if(text != null && !text.isEmpty()) {
            try {
                Double input = Double.valueOf(text);
                if (input < min || input > max)
                    return true;
                return false;
            } catch (Exception e) {
                return true;
            }
        }
        return true;
    }

    /**
     * Sets the default values of a racecar to the fields. If there is no default value it sets empty
     */
    private void setDefaultValues() {
        nameInput.setText("");
        frontTrackInput.setText(String.valueOf(RaceCar.getDefaultTrack()));
        cornerWeightFLInput.setText("");
        cornerWeightRLInput.setText("");
        rearTrackInput.setText(String.valueOf(RaceCar.getDefaultTrack()));
        cornerWeightRRInput.setText("");
        cornerWeightFRInput.setText("");
        cogInput.setText(String.valueOf(RaceCar.getDefaultCogheight()));
        frontRollDistInput.setText(String.valueOf(RaceCar.getDefaultFrontrolldist()));
        wheelBaseInput.setText(String.valueOf(RaceCar.getDefaultWheelbase()));
    }

    private void selectRaceCarInListView(){
        raceCarList.getSelectionModel().select(Storage.getSelectedRaceCar().getName());
    }
}
