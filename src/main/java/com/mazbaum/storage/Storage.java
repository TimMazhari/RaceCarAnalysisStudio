package com.mazbaum.storage;

import com.mazbaum.model.MFTModel;
import com.mazbaum.model.RaceCar;
import com.mazbaum.model.TireModel;

import java.util.ArrayList;

public class Storage {

    private static final ArrayList<RaceCar> raceCarArrayList = new ArrayList<>();

    private static RaceCar selectedRaceCar;
    private static boolean selectedRaceCarSet = false;

    private static final TireModel defaultTireModel = new MFTModel();

    private static final TireModel customTireModel = new MFTModel(1.3, 15.2, -1.6, 1.8, 0.000075);

    public static void addRaceCar(RaceCar raceCar){
        raceCarArrayList.add(raceCar);
    }


    public static ArrayList<RaceCar> getAllRaceCars() {
        return raceCarArrayList;
    }

    public static TireModel getDefaultTireModel(){
        return defaultTireModel;
    }

    public static TireModel getCustomTireModel(){
        return customTireModel;
    }

    public static RaceCar findRaceCar(String name){
        for(RaceCar raceCar : raceCarArrayList){
            if(raceCar.getName().equals(name)){
                return raceCar;
            }
        }
        return null;
    }

    public static RaceCar getSelectedRaceCar(){
        if (selectedRaceCarSet){
            return selectedRaceCar;
        }
        return null;
    }

    public static void setSelectedRaceCar(RaceCar raceCar){
        selectedRaceCar = raceCar;
        selectedRaceCarSet = true;
    }

    public static boolean selectedRaceCarSet(){
        return selectedRaceCarSet();
    }

}
