package com.mazbaum.controller;

import com.mazbaum.model.MFTModel;
import com.mazbaum.model.RaceCar;
import com.mazbaum.model.TireModel;
import com.mazbaum.util.CorneringAnalyserUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;

import java.util.Iterator;
import java.util.ResourceBundle;

public class RCASMainViewController {

	@FXML
	private GridPane mainPane;
	@FXML
	private LineChart<Number, Number> mainChart;
	@FXML
	private ResourceBundle resources;

	@FXML
	public void initialize() {
		// create race cars and calculate a chart.
		RaceCar myRaceCar_2 = new RaceCar(0L,420, 420, 370, 370);
		TireModel myTireModel_2_Fr = new MFTModel(1.3, 15.2, -1.6, 1.6, 0.000075);
		TireModel myTireModel_2_Rr = new MFTModel(1.3, 15.2, -1.6, 1.8, 0.000075);
		myRaceCar_2.setFrontRollDist(0.55);
		myRaceCar_2.setFrontAxleTireModel(myTireModel_2_Fr);
		myRaceCar_2.setRearAxleTireModel(myTireModel_2_Rr);
		myRaceCar_2.setName("Car MOD (red)");

		RaceCar myRaceCar_1 = new RaceCar(1L,420, 420, 370, 370);
		TireModel myTireModel_1 = new MFTModel();
		myRaceCar_1.setFrontRollDist(0.55);
		myRaceCar_1.setFrontAxleTireModel(myTireModel_1);
		myRaceCar_1.setRearAxleTireModel(myTireModel_1);
		myRaceCar_1.setName("Car STD (blue)");


		CorneringAnalyserUtil corneringUtil = new CorneringAnalyserUtil();

		ObservableList<Series<Number, Number>> dataList_1 = corneringUtil.generateMMMChartData(myRaceCar_1);
		mainChart.getData().addAll(dataList_1);
		// Set the style of the series after adding the data to the chart.
		// Otherwise no there is no reference "Node" which leads to a
		// NullPointerException.
		this.setSeriesStyle(dataList_1, ".chart-series-line", "-fx-stroke: blue; -fx-stroke-width: 1px;");

		ObservableList<Series<Number, Number>> dataList_2 = corneringUtil.generateMMMChartData(myRaceCar_2);
		mainChart.getData().addAll(dataList_2);
		this.setSeriesStyle(dataList_2, ".chart-series-line", "-fx-stroke: red; -fx-stroke-width: 1px;");
	}

	private void setSeriesStyle(ObservableList<Series<Number, Number>> dataList_1, String styleSelector,
			String lineStyle) {
		for (Series<Number, Number> curve : dataList_1) {
			curve.getNode().lookup(styleSelector).setStyle(lineStyle);
		}
	}

}
