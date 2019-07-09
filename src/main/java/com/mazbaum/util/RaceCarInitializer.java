package com.mazbaum.util;

import com.mazbaum.model.MFTModel;
import com.mazbaum.model.RaceCar;
import com.mazbaum.model.TireModel;
import com.mazbaum.storage.Storage;

public class RaceCarInitializer {

    public static void initRaceCar(){
        Storage.addRaceCar(raceCarOne());
        Storage.addRaceCar(raceCarTwo());
        Storage.addRaceCar(raceCarTwo());
        Storage.addRaceCar(raceCarTwo());
        Storage.addRaceCar(raceCarTwo());
        Storage.addRaceCar(raceCarTwo());
    }

    private static RaceCar raceCarTwo() {
        RaceCar raceCar = new RaceCar(420, 420, 370, 370);
        TireModel myTireModel_2_Fr = new MFTModel(1.3, 15.2, -1.6, 1.6, 0.000075);
        TireModel myTireModel_2_Rr = new MFTModel(1.3, 15.2, -1.6, 1.8, 0.000075);
        raceCar.setFrontRollDist(0.55);
        raceCar.setFrontAxleTireModel(myTireModel_2_Fr);
        raceCar.setRearAxleTireModel(myTireModel_2_Rr);
        raceCar.setName("Car MOD (red)");
        return raceCar;
    }

    private static RaceCar raceCarOne() {
        RaceCar raceCar = new RaceCar(420, 420, 370, 370);
        TireModel myTireModel_1 = new MFTModel();
        raceCar.setFrontRollDist(0.55);
        raceCar.setFrontAxleTireModel(myTireModel_1);
        raceCar.setRearAxleTireModel(myTireModel_1);
        raceCar.setName("Car STD (blue)");
        return raceCar;
    }

}
