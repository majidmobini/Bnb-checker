package com.home.bnbchecker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class CheckerService extends IntentService {
    public CheckerService() {
        super("CheckerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("MyTestService", "Service running");
        Context ctx = this;


        SharedPreferences sharedpreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
        Variable.BNBAdress = sharedpreferences.getString("add","");
        Variable.SetBnbAddress(Variable.BNBAdress);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderList closed = new OrderList();
                Log.i("MyTestService", "Service 1");
                String data = GetClass.GetData(Variable.GetClosed2+"&start="+(System.currentTimeMillis() - 12 * 60 * 60 * 1000) + "&end="+System.currentTimeMillis());
                Log.i("MyTestService", "Service "+data);
                if (data.length() > 10) {
                    Gson gson = new Gson();
                    closed =  gson.fromJson(data, OrderList.class);
                }
                OrderList oldClosed = new OrderList();
                SharedPreferences sharedpreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
                String jsData = sharedpreferences.getString("closed","");
                ArrayList<Order> newOrders = new ArrayList<>();
                if (jsData.length() > 2)
                {
                    Gson gson = new Gson();
                    oldClosed =  gson.fromJson(jsData, OrderList.class);
                }
                if(oldClosed.order.size() > 0)
                {
                    for (Order ord : closed.order)
                    {
                        if(Double.valueOf(ord.cumulateQuantity)  == 0)
                        {
                            continue;
                        }
                        boolean exist = false;
                        for (Order oldOrd : oldClosed.order) {
                            if (oldOrd.orderId == ord.orderId) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            newOrders.add(ord);
                        }
                    }
                }
                SharedPreferences.Editor editor  = sharedpreferences.edit();
                editor.putString("closed",data);
                editor.apply();
                Log.i("MyTestService", "Sending Note ");
            //    if (newOrders.size() > 0)
              ////  {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    StringBuilder stb = new StringBuilder();
                    if (newOrders.size() > 0) {
                        for (Order ord : newOrders) {
                            stb.append(ord.symbol + " " + ord.price + "\n");
                        }
                    }
                    else
                    {
                        stb.append("Nothing happened :(");
                    }
                    Notification.Builder n = new Notification.Builder(ctx)
                            .setContentTitle(newOrders.size() + " order field ")

                            .setSmallIcon(R.drawable.bnb)
                            .setStyle(new Notification.BigTextStyle().bigText(stb.toString()))
                            .setAutoCancel(true);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = "bnbId";
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                "bnbCheckerIdNote",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
                        n.setChannelId(channelId);
                    }
                    notificationManager.notify(0, n.build());
             ///   }


            }
        }).start();
    }
}
