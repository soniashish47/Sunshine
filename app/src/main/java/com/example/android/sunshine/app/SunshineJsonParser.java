package com.example.android.sunshine.app;

/**
 * Created by ab219tx on 14-10-2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SunshineJsonParser {
    String mJsonString;
    JSONObject mRootObject;

    public void SunshineJsonParser(String jSonString) {
        try {
            mRootObject = new JSONObject(jSonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getMaxTemp(int dayIndex) {

        double maxTemp = 0;
        JSONArray tempArray = null;
        try {
            tempArray = mRootObject.getJSONArray("list");
            JSONObject day = tempArray.getJSONObject(dayIndex);
            JSONObject tempInfo = day.getJSONObject("temp");
            maxTemp = tempInfo.getDouble("max");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maxTemp;
    }
}



