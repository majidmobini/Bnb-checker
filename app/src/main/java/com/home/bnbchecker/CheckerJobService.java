package com.home.bnbchecker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import static com.home.bnbchecker.Variable.BaseUrl;

public class CheckerJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        RunTheTask(jobParameters);
        return true;
    }


    private void RunTheTask(JobParameters jobParameters) {
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
                String data = GetClass.GetData(Variable.GetClosedFilled+"&start="+(System.currentTimeMillis() - 12 * 60 * 60 * 1000) + "&end="+System.currentTimeMillis());
                Log.i("MyTestService", "Service "+data);
                if (data.length() > 10) {
                    Gson gson = new Gson();
                    closed =  gson.fromJson(data, OrderList.class);
                }
                OrderList oldClosed = new OrderList();
                SharedPreferences sharedpreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
                String jsData = readLog(ctx);//sharedpreferences.getString("closed","");
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
                            if (oldOrd.orderId.equals(ord.orderId)) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            newOrders.add(ord);
                        }
                    }
                }
               /* SharedPreferences.Editor editor  = sharedpreferences.edit();
                editor.putString("closed",data);
                editor.apply();
                */
               if ( oldClosed.order.size() == 0 && data.length() > 2)
               {
                   writeLog(data,ctx);
               }
                Log.i("MyTestService", "Sending Note ");
                if (newOrders.size() > 0)
                  {
                      writeLog(data,ctx);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    StringBuilder stb = new StringBuilder();
                    if (newOrders.size() > 0) {
                        for (Order ord : newOrders) {
                            stb.append(ord.symbol + " " + ord.price + " "+ (ord.side == 1 ? "BUY" : "SELL") + "\n");
                        }
                    }
                    else
                    {
                        stb.append("Nothing happened :(");
                    }
                    Notification.Builder n = new Notification.Builder(ctx)
                            .setContentTitle(newOrders.size() + " order field " + oldClosed.order.size() + " "+ closed.order.size())

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
                      Random r = new Random();
                      int i1 = r.nextInt(80 - 20) + 20;
                    notificationManager.notify(i1, n.build());
                  }
                ckekBotStatus();
                jobFinished(jobParameters,false);


            }
        }).start();
    }

    private void ckekBotStatus()
    {
        //https://dex.binance.org/api/v1/orders/open?address=bnb1wwy2ntxdpr2ezrav752c8mah90qyyzg7prnvav
        String GetOpened = BaseUrl + "orders/open?address=bnb16l42z0ms00a7az2dy5rc43pu56c7928z373n07";
        Context ctx = this;
        OrderList opens = new OrderList();
        Log.i("MyTestService", "Service 1");
        String data = GetClass.GetData(GetOpened);
        Log.i("MyTestService", "Service "+data);
        if (data.length() > 10) {
            Gson gson = new Gson();
            opens =  gson.fromJson(data, OrderList.class);
        }
        boolean hasLowerThanTen = false;
        for (Order ord : opens.order) {
            //2021-05-21T18:47:54.193Z

            String dtStart = ord.orderCreateTime;//  "2010-10-15T09:27:37Z";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = format.parse(dtStart);

                if ((new Date().getTime()) - date.getTime() < 10 * 60 * 1000) {
                    hasLowerThanTen = true;
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!hasLowerThanTen)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Notification.Builder n = new Notification.Builder(ctx)
                    .setContentTitle("LOOk May be turned off !!!!!!!!")

                    .setSmallIcon(R.drawable.bnb)
                    .setStyle(new Notification.BigTextStyle().bigText("LOOk May be turned off !!!!!!!!"))
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
            Random r = new Random();
            int i1 = r.nextInt(80 - 20) + 20;
            notificationManager.notify(i1, n.build());
        }



    }


    private String readLog(Context ctx)
    {
        File directory = ctx.getFilesDir();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File f = new File(directory,"bnbChecker.log");
        if (f.exists())
        {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(f));
                while ((line = in.readLine()) != null) stringBuilder.append(line);
            } catch (FileNotFoundException e) {
                return "";
            } catch (IOException e) {
                return "";
            }
            return stringBuilder.toString();

        }
        return "";
    }

    private void writeLog(String data,Context ctx)
    {
        File directory = ctx.getFilesDir();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File f = new File(directory,"bnbChecker.log");
        if (f.exists())
        {
            f.delete();
            f = new File(directory,"bnbChecker.log");
        }
        try {
            FileWriter fr = new FileWriter(f, true);
            fr.write(data);
            fr.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



   /* public static void writeStringAsFile(final String fileContents, String fileName) {
        Context context = App.instance.getApplicationContext();
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {

        }
    }
    public static String readFileAsString(String fileName) {
        Context context = App.instance.getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return stringBuilder.toString();
    }
    */

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
