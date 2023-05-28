package com.home.bnbchecker;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Candlestick {
    String close = "";//	number	closing price
    long closeTime = 0;//	long	time of closing trade
    String high	= "0";//number	the highest price
    String low = "0";//	number	the lowest price
    int numberOfTrades = 0 ;//	integer	total trades
    String open = "0";//	number	open price
    long openTime = 0;//	long	time of open trade
    String quoteAssetVolume = "0";//	number	the total trading volume in quote asset
    String volume = "0";//	number	the total trading volume

    public static ArrayList<Candlestick> FillCandels(JSONArray jsAll)
    {
        ArrayList<Candlestick> candels = new ArrayList<>();
        for(int i = 0 ; i < jsAll.length();i++)
        {
            try {
                JSONArray jsa = jsAll.getJSONArray(i);
                Candlestick cdk = new Candlestick();
                cdk.openTime = jsa.getLong(0);
                cdk.open = jsa.getString(1);
                cdk.high = jsa.getString(2);
                cdk.low = jsa.getString(3);
                cdk.close = jsa.getString(4);
                cdk.volume = jsa.getString(5);
                cdk.closeTime = jsa.getLong(6);
                cdk.quoteAssetVolume = jsa.getString(7);
                cdk.numberOfTrades = jsa.getInt(8);
                candels.add(cdk);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return candels;
    }
}


/*[
        1499040000000,      // Open time
        "0.01634790",       // Open
        "0.80000000",       // High
        "0.01575800",       // Low
        "0.01577100",       // Close
        "148976.11427815",  // Volume
        1499644799999,      // Close time
        "2434.19055334",    // Quote asset volume
        308                // Number of trades
        ]*/