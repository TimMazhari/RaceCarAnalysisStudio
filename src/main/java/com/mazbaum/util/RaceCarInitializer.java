package com.mazbaum.util;

import com.mazbaum.model.RaceCar;
import com.mazbaum.storage.Storage;

public class RaceCarInitializer {

    public static void initRaceCar(){
        Storage.addRaceCar(raceCarOne());
        Storage.addRaceCar(raceCarTwo());
    }

    private static RaceCar raceCarTwo() {
        RaceCar raceCar = new RaceCar(0L,420, 420, 370, 370);
        raceCar.setFrontRollDist(0.55);
        raceCar.setFrontAxleTireModel(Storage.getCustomTireModel());
        raceCar.setRearAxleTireModel(Storage.getCustomTireModel());
        raceCar.setName("Car MOD (red)");
        return raceCar;
    }

    private static RaceCar raceCarOne() {
        RaceCar raceCar = new RaceCar(1L,420, 420, 370, 370);
        raceCar.setFrontRollDist(0.55);
        raceCar.setFrontAxleTireModel(Storage.getDefaultTireModel());
        raceCar.setRearAxleTireModel(Storage.getDefaultTireModel());
        raceCar.setName("Car STD (blue)");
        return raceCar;
    }

}
