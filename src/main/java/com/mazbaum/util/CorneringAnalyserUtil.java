package com.mazbaum.util;

import com.mazbaum.model.RaceCar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

/**
 * Utility Class for the analysis of the cornering capabilities of a RaceCar
 * Object. Calculates various values and chart data for a specific race car with
 * a specific front and rear tire model.
 * 
 * @author suy
 *
 */
public class CorneringAnalyserUtil {

	private static double G = 9.81;
	private static double DEFAULT_SPEED = 100 / 3.6;
	private static double FROM_DELTA = -20.0;
	private static double TO_DELTA = 20.0;
	private static double DELTA_INCREMENT = 0.5;
	private static double FROM_BETA = -10.0;
	private static double TO_BETA = 10.0;
	private static double BETA_INCREMENT = 0.5;
	private static double APPROXIMATION_COEFFICIENT = 0.001;

	public CorneringAnalyserUtil() {
	}


	/**
	 *
	 * @param raceCar
	 * @return - front static axle weight in kg.
	 */
	public Double getStaticFrontAxleWeight(RaceCar raceCar) {
		return raceCar.getCornerWeightFL() + raceCar.getCornerWeightFR();
	}

	/**
	 * 
	 * @param raceCar
	 * @return - rear static axle weight in kg.
	 */
	public Double getStaticRearAxleWeight(RaceCar raceCar) {
		return raceCar.getCornerWeightRL() + raceCar.getCornerWeightRR();
	}



	/**
	 * 
	 * @param raceCar
	 * @return - total race car weight in kg.
	 */
	public Double getTotalCarWeight(RaceCar raceCar) {
		return this.getStaticFrontAxleWeight(raceCar) + this.getStaticRearAxleWeight(raceCar);
	}

	/**
	 * 
	 * @param raceCar
	 * @return - distance lf from front axle to cog in m.
	 */
	public Double getCogDistanceFromFrontAxle(RaceCar raceCar) {
		return (this.getStaticRearAxleWeight(raceCar) * raceCar.getWheelbase()) / this.getTotalCarWeight(raceCar);
	}

	/**
	 * 
	 * @param raceCar
	 * @return - distance lr from rear axle to cog in m.
	 */
	public Double getCogDistanceFromRearAxle(RaceCar raceCar) {
		return raceCar.getWheelbase() - this.getCogDistanceFromFrontAxle(raceCar);
	}

	/**
	 * Helper inner class to be used as a return value for the caluclation of a
	 * single Point in the yaw moment diagram.
	 * 
	 * @author suy
	 *
	 */
	private class MMMSinglePoint {
		private Double beta;
		private Double delta;
		private Double lateralAccel;
		private Double yawMoment;

		public MMMSinglePoint(Double beta, Double delta, Double lateralAccel, Double yawMoment) {
			this.beta = beta;
			this.delta = delta;
			this.lateralAccel = lateralAccel;
			this.yawMoment = yawMoment;
		}

		/**
		 * 
		 * @return body slip angle beta in degrees (�).
		 */
		public Double getBeta() {
			return this.beta;
		}

		/**
		 * 
		 * @param beta
		 *            - the body slip angle beta in degrees (�) to be set.
		 */
		public void setBeta(Double beta) {
			this.beta = beta;
		}

		/**
		 * 
		 * @return steering angle delta in degrees (�).
		 */
		public Double getDelta() {
			return this.delta;
		}

		/**
		 *
		 */
		public void setDelta(Double delta) {
			this.delta = delta;
		}

		/**
		 * @return the lateralAccel in m/s^2.
		 */
		public Double getLateralAccel() {
			return lateralAccel;
		}

		/**
		 * @param lateralAccel
		 *            - the lateralAccel to set in m/s^2.
		 */
		public void setLateralAccel(Double lateralAccel) {
			this.lateralAccel = lateralAccel;
		}

		/**
		 * @return the yaw moment around the CoG in Nm.
		 */
		public Double getYawMoment() {
			return yawMoment;
		}

		/**
		 * @param yawMoment
		 *            - the yaw moment around the CoG in Nm.
		 * 
		 */
		public void setYawMoment(Double yawMoment) {
			this.yawMoment = yawMoment;
		}
	}

	/**
	 * Helper private function to approximate the lateral force of the race car
	 * and to calculate the corresponding lateral force ay in G and yaw moment
	 * Mz in Nm. It returns an MMMSinglePointHelper which represents a single
	 * Point in the MMM diagramm with x=ay and y=Mz. This method is created to
	 * prevent code duplication in the calculation of the MMM charts.
	 * 
	 * 
	 * @param raceCar
	 *            - race car object
	 * @param carSpeed
	 *            - speed of the car in m/s.
	 * @param beta
	 *            - vehicle slip angle "beta" in degrees (�).
	 * @param delta
	 *            - steering angle "delta" in degrees (�).
	 */
	private MMMSinglePoint calculateMMMSinglePoint(RaceCar raceCar, Double carSpeed, Double beta, Double delta) {
		Double yawRate = 0.1;
		MMMSinglePoint singlePoint = new MMMSinglePoint(0.0, 0.0, (0.8 * G), 0.0);
		singlePoint.setBeta(beta);
		singlePoint.setDelta(delta);

		double approxDiff = 100.0;

		Double slipAngleFL;
		Double slipAngleFR;
		Double slipAngleRL;
		Double slipAngleRR;

		Double tireLoadFL;
		Double tireLoadFR;
		Double tireLoadRL;
		Double tireLoadRR;

		Double tireForceFL = 0.0;
		Double tireForceFR = 0.0;
		Double tireForceRL = 0.0;
		Double tireForceRR = 0.0;

		while (Math.abs(approxDiff) >= APPROXIMATION_COEFFICIENT) {
			Double oldLateralAccel = singlePoint.getLateralAccel();

			double vY = carSpeed * Math.sin(Math.toRadians(singlePoint.getBeta()));
			double vX = carSpeed * Math.cos(Math.toRadians(singlePoint.getBeta()));
			Double lf = this.getCogDistanceFromFrontAxle(raceCar);
			Double lr = this.getCogDistanceFromRearAxle(raceCar);
			Double l = raceCar.getWheelbase();
			Double bf = raceCar.getFrontTrack();
			Double br = raceCar.getRearTrack();
			Double hbo = raceCar.getCogHeightProcent();

			slipAngleFL = singlePoint.getDelta()
					- Math.toDegrees(Math.atan((vY + (yawRate * lf)) / (vX - (yawRate * (bf / 2)))));
			slipAngleFR = singlePoint.getDelta()
					- Math.toDegrees(Math.atan((vY + (yawRate * lf)) / (vX + (yawRate * (bf / 2)))));
			slipAngleRL = -1.0 * Math.toDegrees(Math.atan((vY - (yawRate * lr)) / (vX - (yawRate * (br / 2)))));
			slipAngleRR = -1.0 * Math.toDegrees(Math.atan((vY - (yawRate * lr)) / (vX + (yawRate * (br / 2)))));

			double ax = 0.0;
			double aeroLoadFront = 0.0;
			double aeroLoadRear = 0.0;
			Double frontRollDist = raceCar.getFrontRollDistProcent();
			double rearRollDist = 1.0 - raceCar.getFrontRollDistProcent();

			tireLoadFL = this.getTotalCarWeight(raceCar) * ((frontRollDist * G) - ((hbo / l) * ax))
					* (0.5 - ((hbo / bf) * (singlePoint.getLateralAccel() / G))) + aeroLoadFront;
			tireLoadFR = this.getTotalCarWeight(raceCar) * ((frontRollDist * G) - ((hbo / l) * ax))
					* (0.5 + ((hbo / bf) * (singlePoint.getLateralAccel() / G))) + aeroLoadFront;
			tireLoadRL = this.getTotalCarWeight(raceCar) * ((rearRollDist * G) + ((hbo / l) * ax))
					* (0.5 - ((hbo / br) * (singlePoint.getLateralAccel() / G))) + aeroLoadRear;
			tireLoadRR = this.getTotalCarWeight(raceCar) * ((rearRollDist * G) + ((hbo / l) * ax))
					* (0.5 + ((hbo / br) * (singlePoint.getLateralAccel() / G))) + aeroLoadRear;

			tireForceFL = raceCar.getFrontAxleTireModel().getLateralTireForce(slipAngleFL, tireLoadFL);
			tireForceFR = raceCar.getFrontAxleTireModel().getLateralTireForce(slipAngleFR, tireLoadFR);
			tireForceRL = raceCar.getRearAxleTireModel().getLateralTireForce(slipAngleRL, tireLoadRL);
			tireForceRR = raceCar.getRearAxleTireModel().getLateralTireForce(slipAngleRR, tireLoadRR);

			singlePoint.setLateralAccel(
					(tireForceFL + tireForceFR + tireForceRL + tireForceRR) / this.getTotalCarWeight(raceCar));

			yawRate = singlePoint.getLateralAccel() / carSpeed;
			approxDiff = oldLateralAccel - singlePoint.getLateralAccel();
		}
		Double yawMoment = ((tireForceFL + tireForceFR) * this.getCogDistanceFromFrontAxle(raceCar))
				- ((tireForceRL + tireForceRR) * this.getCogDistanceFromRearAxle(raceCar));
		singlePoint.setYawMoment(yawMoment);
		return singlePoint;
	}

	/**
	 * Calculates and returns Chart Data for an MMM (Milliken Moment Method)
	 * Yaw-Moment (Nm) vs. Lateral Acceleration (G) Diagram for the given race
	 * car at the given speed. For now, no aerodynamic effects are being
	 * considered.
	 * 
	 * @param raceCar
	 *            - the race car object
	 * @return - an observable list containing all the series for a complete MMM
	 *         diagram.
	 */
	public ObservableList<Series<Number, Number>> generateMMMChartData(RaceCar raceCar) {
		ObservableList<Series<Number, Number>> chartDataList = FXCollections.observableArrayList();
		Double carSpeed = DEFAULT_SPEED;

		for (Double delta = FROM_DELTA; delta <= TO_DELTA; delta += DELTA_INCREMENT) {

			Series<Number, Number> betaCurve = new Series<>();
			chartDataList.add(betaCurve);
			betaCurve.setName(String.format("D:%.2f", delta));

			for (Double beta = FROM_BETA; beta <= TO_BETA; beta += BETA_INCREMENT) {
				MMMSinglePoint singlePoint = this.calculateMMMSinglePoint(raceCar, carSpeed, beta, delta);

				Data<Number, Number> betaData = new Data<Number, Number>((singlePoint.getLateralAccel() / G),
						singlePoint.getYawMoment());
				betaData.setExtraValue(singlePoint);
				betaCurve.getData().add(betaData);
			}
		}

		for (Double beta = FROM_BETA; beta <= TO_BETA; beta += BETA_INCREMENT) {

			Series<Number, Number> deltaCurve = new Series<>();
			chartDataList.add(deltaCurve);
			deltaCurve.setName(String.format("B:%.2f", beta));

			for (double delta = FROM_DELTA; delta <= TO_DELTA; delta += DELTA_INCREMENT) {
				MMMSinglePoint singlePoint = this.calculateMMMSinglePoint(raceCar, carSpeed, beta, delta);

				Data<Number, Number> deltaData = new Data<Number, Number>((singlePoint.getLateralAccel() / G),
						singlePoint.getYawMoment());
				deltaData.setExtraValue(singlePoint);
				deltaCurve.getData().add(deltaData);
			}
		}
		return chartDataList;
	}
}
