package com.home.bnbchecker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Variable {
    static String BNBAdress = "";//"bnb1wwy2ntxdpr2ezrav752c8mah90qyyzg7prnvav";// "";//"bnb1wwy2ntxdpr2ezrav752c8mah90qyyzg7prnvav";
    static String BaseUrl = "https://dex.binance.org/api/v1/";
    static String GetAccount = BaseUrl+"account/"+BNBAdress;
    static String GetDepth = BaseUrl+"depth?symbol=";
    static String GetClosed = BaseUrl + "orders/closed?address="+BNBAdress+"&limit=1000&status=Canceled";//address limit symbol
    static String GetClosedFilled = BaseUrl + "orders/closed?address="+BNBAdress+"&limit=1000&status=FullyFill";//address limit symbol
    static String GetClosed2 = BaseUrl + "orders/closed?address="+BNBAdress+"&limit=1000";
    static String GetOpened = BaseUrl + "orders/open?address="+BNBAdress+"&limit=1000";//address limit symbol
    static String GetTicker = BaseUrl + "ticker/24hr";
    static String GetTrades = BaseUrl + "trades?";
    static String GetMarkets = BaseUrl + "markets?limit=1000";
    static String GetMyTrades = BaseUrl + "trades?address="+BNBAdress;

    static void SetBnbAddress(String address)
    {
        BNBAdress = address;
        Date date = new Date();
        GetMyTrades = BaseUrl + "trades?address="+BNBAdress+"&start="+(date.getTime()-30*24*60*60*1000)+"&end="+date.getTime()+"&limit=1000";
        GetAccount = BaseUrl+"account/"+BNBAdress;
        GetClosed = BaseUrl + "orders/closed?address="+BNBAdress+"&limit=1000&status=Canceled";//address limit symbol
        GetClosed2 = BaseUrl + "orders/closed?address="+BNBAdress+"&limit=1000";
        GetOpened = BaseUrl + "orders/open?address="+BNBAdress+"&limit=1000";//address limit symbol
        GetClosedFilled = BaseUrl + "orders/closed?address="+BNBAdress+"&limit=1000&status=FullyFill";//address limit symbol
    }
    static String Settime(long time)
    {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(time);
        return simpleDate.format(date);
    }
    static long GetTimeSec(String time)
    {
        //2021-03-20T18:56:25.313Z
        try {

            String newTime = time.split("\\.")[0];
            SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = simpleDate.parse(newTime);
            return  date.getTime();
        }
        catch (Exception ex)
        {

        }
        return  0;

    }
    ///api/v1/trades
}
