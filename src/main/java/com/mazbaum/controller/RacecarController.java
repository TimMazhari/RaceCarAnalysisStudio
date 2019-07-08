package com.mazbaum.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.text.DecimalFormat;

public class RacecarController {

    @FXML
    private Pane mainPane;

    @FXML
    private Button createButton;

    @FXML
    private JFXTextField nameInput;

    @FXML
    private JFXTextField frontTrackInput;

    @FXML
    private JFXTextField cornerWeightFRInput;

    @FXML
    private JFXSlider cornerWeightFRSlider;

    @FXML
    private JFXSlider cornerWeightRRSlider;

    @FXML
    private JFXTextField cornerWeightRRInput;

    @FXML
    private JFXSlider cornerWeightRLSlider;

    @FXML
    private JFXSlider cornerWeightFLSlider;

    @FXML
    private JFXTextField cornerWeightRLInput;

    @FXML
    private JFXTextField cornerWeightFLInput;

    @FXML
    private JFXTextField rearTrackInput;

    @FXML
    private JFXSlider cogSlider;

    @FXML
    private JFXSlider frontRollDistSlider;

    @FXML
    private JFXTextField cogInput;

    @FXML
    private JFXTextField frontRollDistInput;

    @FXML
    private JFXTextField wheelBaseInput;

    @FXML
    private JFXSlider wheelBaseSlider;

    @FXML
    private JFXButton saveButton;

    @FXML
    public void initialize() {
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
