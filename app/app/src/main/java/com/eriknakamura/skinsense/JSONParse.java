package com.eriknakamura.skinsense;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParse{
    public static String dis;
    public static double conf;
    public static HashMap<String, Double> finalMap;


    public static void mainletsgo(String a, String b, String c, String d) throws JSONException {

        String jObject = a;
        String jObjectUber1 = b;
        String jObjectUber2 = c;
        String jObjectUber3 = d;
        //Array and hash map for normal
        JSONObject jsonObj = new JSONObject(jObject);
        JSONArray arr = jsonObj.getJSONArray("Predictions");
        HashMap<String, Double> valueMap = new HashMap<String, Double>();
        for(int i=0;i<arr.length();i++){
            String diseaseType = arr.getJSONObject(i).getString("Tag");
            double diseaseProb = arr.getJSONObject(i).getDouble("Probability");
            valueMap.put(diseaseType, diseaseProb);
        }

        //Array and hash map for uber1
        JSONObject jsonObjUber1 = new JSONObject(jObjectUber1);
        JSONArray arrUber1 = jsonObjUber1.getJSONArray("Predictions");
        HashMap<String, Double> valueMapUber1 = new HashMap<String, Double>();
        for(int i=0;i<arrUber1.length();i++){
            String diseaseTypeMain = arrUber1.getJSONObject(i).getString("Tag");
            Double diseaseProbMain = arrUber1.getJSONObject(i).getDouble("Probability");
            valueMapUber1.put(diseaseTypeMain, diseaseProbMain);
        }


        //Array and hash map for uber2
        JSONObject jsonObjUber2 = new JSONObject(jObjectUber2);
        JSONArray arrUber2 = jsonObjUber2.getJSONArray("Predictions");
        HashMap<String, Double> valueMapUber2 = new HashMap<String, Double>();
        for(int i=0;i<arrUber2.length();i++){
            String diseaseType = arrUber2.getJSONObject(i).getString("Tag");
            double diseaseProb = arrUber2.getJSONObject(i).getDouble("Probability");
            valueMapUber2.put(diseaseType, diseaseProb);
        }

        //Array and hash map for uber3
        JSONObject jsonObjUber3 = new JSONObject(jObjectUber3);
        JSONArray arrUber3 = jsonObjUber3.getJSONArray("Predictions");
        HashMap<String, Double> valueMapUber3 = new HashMap<String, Double>();
        for(int i=0;i<arrUber3.length();i++){
            String diseaseType = arrUber3.getJSONObject(i).getString("Tag");
            double diseaseProb = arrUber3.getJSONObject(i).getDouble("Probability");
            valueMapUber3.put(diseaseType, diseaseProb);
        }

        System.out.println("main" + valueMap);
        System.out.println("uber 1" + valueMapUber1);
        System.out.println("uber 2" + valueMapUber2);
        System.out.println("uber 3" + valueMapUber3);

        finalMap = new HashMap<String, Double>();
        finalMap = applyUberLayers(valueMap, valueMapUber1, valueMapUber2, valueMapUber3);
        List<String> finalMapKeys = new ArrayList(finalMap.keySet());
        double largest = 0;
        String mapkey = "hello";
        for(int i=0;i<finalMapKeys.size();i++){
            double hashSize = finalMap.get(finalMapKeys.get(i));
            if(hashSize>largest){
                largest = hashSize;
                mapkey = finalMapKeys.get(i);
            }
        }
        conf = largest;
        dis = mapkey;

        System.out.println("final" + finalMap);


    }


    public static HashMap<String, Double> applyUberLayers(HashMap<String,Double> main, HashMap<String,Double> uber1, HashMap<String,Double> uber2, HashMap<String,Double> uber3){
        List<String> mainKeys = new ArrayList(main.keySet());
        List<String> uber1Keys = new ArrayList(uber1.keySet());
        List<String> uber2Keys = new ArrayList(uber2.keySet());
        List<String> uber3Keys = new ArrayList(uber3.keySet());
        System.out.println(mainKeys);
        System.out.println(uber1Keys);
        System.out.println(uber2Keys);
        System.out.println(uber3Keys);
        List<Double> vals = new ArrayList<Double>();
        List<String> keys = new ArrayList<String>();
        HashMap<String,Double> uberApplyedMap = new HashMap<String,Double>();
        for(int i = 0;i<=1;i++){

            int matchIndex = mainKeys.indexOf(uber1Keys.get(i));
            if(matchIndex != -1){
                String valMain = mainKeys.get(matchIndex);
                double newVal = main.get(valMain) * uber1.get(valMain);
                main.put(valMain, newVal);
            }


            int matchIndex1 = mainKeys.indexOf(uber2Keys.get(i));
            if(matchIndex1!=-1){
                String valMain = mainKeys.get(matchIndex1);
                double newVal = main.get(valMain) * uber2.get(valMain);
                main.put(valMain, newVal);
            }

            int matchIndex2 = mainKeys.indexOf(uber3Keys.get(i));
            if(matchIndex2!=-1){
                String valMain = mainKeys.get(matchIndex2);
                double newVal = main.get(valMain) * uber3.get(valMain);
                main.put(valMain, newVal);
            }
        }

        return main;
    }

    public static void accountWeighted(double[] weightedSymptoms){
        List<String> hashKeys = new ArrayList<String>(finalMap.keySet());
        for(int i=0;i<hashKeys.size();i++){
            String keyVal = hashKeys.get(i);
            finalMap.put(keyVal,finalMap.get(hashKeys.get(i))*.60 + weightedSymptoms[i]);
        }

        List<String> finalMapKeys = new ArrayList(finalMap.keySet());
        double largest = 0;
        String mapkey = "hello";
        for(int i=0;i<finalMapKeys.size();i++){
            double hashSize = finalMap.get(finalMapKeys.get(i));
            if(hashSize>largest){
                largest = hashSize;
                mapkey = finalMapKeys.get(i);
            }
        }
        conf = largest;
        dis = mapkey;

    }
}
