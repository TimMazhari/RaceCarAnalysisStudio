package com.mazbaum.storage;

import com.mazbaum.model.RaceCar;

import java.util.ArrayList;

public class Storage {

    private static final ArrayList<RaceCar> raceCarArrayList = new ArrayList<>();

    public static void addRaceCar(RaceCar raceCar){
        raceCarArrayList.add(raceCar);
    }


    public static ArrayList<RaceCar> getAllRaceCars() {
        return raceCarArrayList;
    }

}
