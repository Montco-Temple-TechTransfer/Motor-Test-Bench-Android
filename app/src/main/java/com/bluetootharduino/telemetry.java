package com.bluetootharduino;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aaron on 5/1/18.
 */
@SuppressWarnings("unchecked")

public class telemetry{
    private ArrayList<ArrayList<Double>> rawData= new ArrayList<>();
    private ArrayList<ArrayList<Double>> computedData = new ArrayList<>();
    private String csv;
    Context myContext;



    public telemetry(HashMap<Integer, HashMap<String, String>> dataToBeExported, Context c) {
        myContext = c;
        hashmapToArrayList(dataToBeExported);
        //rawData = (ArrayList<ArrayList<Double>>) dataToBeExported.clone();
        computeData();
        generateCSV();
    }

    private void hashmapToArrayList(HashMap<Integer, HashMap<String, String>> dataToConverted){
        try {
            for(int i =0; i < dataToConverted.size(); i++ ){
                HashMap<String,String> tempMap = (HashMap) dataToConverted.get(i).clone();
                ArrayList<Double> tempList = new ArrayList<>();
                tempList.add(Double.parseDouble(tempMap.get("Pulse")));
                tempList.add(Double.parseDouble(tempMap.get("Voltage")));
                tempList.add(Double.parseDouble(tempMap.get("Current")));
                tempList.add(Double.parseDouble(tempMap.get("Lift")));
                tempList.add(Double.parseDouble(tempMap.get("MotorTemp")));
                tempList.add(Double.parseDouble(tempMap.get("AmbientTemp")));
                rawData.add(tempList);
            }
        }
        catch (Exception ex) {
            throw new Error(String.format("Please visit http://stackoverflow.com/search?q=%%5Bjava%%5D%%20%s", URLEncoder.encode(ex.getMessage())));
        }

    }

    private void computeData() {
        for (int i = 0; i < rawData.size(); i++) {
            ArrayList<Double> temp = (ArrayList<Double>) rawData.get(i).clone();
            double volts = temp.get(1);
            double amps = temp.get(2);
            double lift = temp.get(3);
            double watts = (Math.floor((volts * amps) * 1000) / 1000);

            temp.add(6, watts);
            temp.add(7, (Math.floor((lift/watts) * 1000) / 1000));
            computedData.add((ArrayList<Double>) temp.clone());
        }

    }

    private void generateCSV() {
        for (ArrayList<Double> row : computedData) {
            for (Double column : row) {
                csv += String.valueOf(Math.floor((column) * 1000)) + ", ";
            }
            csv = csv + "\n";
        }
    }

    public void toClipboard(){
        ClipboardManager clipboard = (ClipboardManager) myContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Clippy",csv);
        clipboard.setPrimaryClip(clip);
    }
    public String getCsv() {
        return csv;
    }

    @Override
    public String toString() {
        return "telemetry{" +
                "csv='" + csv + '\'' +
                '}';
    }
}