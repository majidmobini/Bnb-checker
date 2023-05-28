package com.home.bnbchecker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.home.bnbchecker.Variable.BaseUrl;

public class MainActivity extends AppCompatActivity {

    Account account = new Account();
    MarketDepth dept = new MarketDepth();
    OrderList closed = new OrderList();
    OrderList opened = new OrderList();
    List<TickerStatistics> tickers = new ArrayList<>();
    TradePage trades = new TradePage();
    ArrayList<RowClass> rows = new ArrayList<>();
    ArrayList<Market> globalMarkets = new ArrayList<>();
    EditText tvTime;
    List<Statistic> activeMarkets = new ArrayList<>();
    StatisticsAdapter adapter;
    RowAdapter rowAdapter;
    RecyclerView recycler;
    private ProgressDialog dialog;
    TextView tvCoin;
    TextView tvLow;
    TextView tvHigh;
    TextView tvBnb;
    EditText etAdress;
    double totalBnb = 0;
    double totalHigh = 0;
    double totalLow = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = findViewById(R.id.tvTime);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        etAdress = findViewById(R.id.etAddress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        tvHigh = findViewById(R.id.tvHighBnb);
        tvLow = findViewById(R.id.tvLowBnb);
        tvBnb = findViewById(R.id.tvBnb);
        SharedPreferences shared = getSharedPreferences("main", Context.MODE_PRIVATE);
        etAdress.setText(shared.getString("add",""));
        Variable.BNBAdress = etAdress.getText().toString().trim().split(";")[0];
        Variable.SetBnbAddress(Variable.BNBAdress);
        tvCoin = findViewById(R.id.tvCoin);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dadaa = GetClass.GetData("https://us-central1-glo3d-c338b.cloudfunctions.net/createCheckoutSession?user_id=cujMZGmSWwPHCdFOnm43Ogl95713&sub_price_id=price_1KNAhMIKOPdwmPxlrtiQwLBA&credit_price_id=price_1KNAyqIKOPdwmPxlW9AYGKUH");
                Log.d("Test",dadaa);

            }
        }).start();


        tvCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int q, int i1, int i2) {
                if (activeMarkets.size() > 0)
                {
                    List<Statistic> activs = new ArrayList<>();
                    for(int i = 0 ;i < activeMarkets.size();i++)
                    {
                        if(activeMarkets.get(i).getSymbol().startsWith(charSequence.toString().toUpperCase()))
                        {
                            activs.add(activeMarkets.get(i));
                        }
                    }
                    StatisticsAdapter adapter = new StatisticsAdapter(activs,MainActivity.this);
                    recycler.setAdapter(adapter);
                }
                else if (rows.size() > 0)
                {
                    ArrayList<RowClass> row = new ArrayList<>();
                    for(int i = 0 ;i < rows.size();i++)
                    {
                        if(rows.get(i).coin.startsWith(charSequence.toString().toUpperCase()))
                        {
                            row.add(rows.get(i));
                        }
                    }
                    RowAdapter rowAdapter = new RowAdapter(row,MainActivity.this);
                    recycler.setAdapter(rowAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        /*RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        final RowAdapter adapter = new RowAdapter(rows,MainActivity.this);
        recycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        */
        rows.clear();
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Updating , please wait.");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = GetClass.GetData(Variable.GetAccount);
                if (data.length() > 10) {
                    Gson gson = new Gson();
                    account =  gson.fromJson(data, Account.class);
                    Log.d("Majid C",account.balances.size()+"");
                }

                data = GetClass.GetData(Variable.GetTicker);
                if (data.length() > 10) {
                    Gson gson = new Gson();
                    Log.d("Majid ticker",data);
                    try {
                        JSONArray jsa = new JSONArray(data);
                        tickers.clear();
                        for (int i = 0;i<jsa.length();i++)
                        {
                            JSONObject js = jsa.getJSONObject(i);
                            tickers.add(gson.fromJson(js.toString(),TickerStatistics.class));

                        }
                    }
                    catch (Exception ex)
                    {

                    }

                }
                CalculateBalances();
                runOnUiThread(() -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });
            }
        }).start();

        findViewById(R.id.getCalcelled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
                editor.putString("add",etAdress.getText().toString().trim());
                editor.apply();
                */
                Variable.BNBAdress = etAdress.getText().toString().trim().split(";")[0];
                Variable.SetBnbAddress(Variable.BNBAdress);
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Updating , please wait.");
                dialog.show();
                rowAdapter = new RowAdapter(rows,MainActivity.this);
                recycler.setAdapter(rowAdapter);
                rows.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rows.clear();
                        activeMarkets.clear();
                        String data = GetClass.GetData(Variable.GetClosed);
                        Log.d("Majid Closed",data);
                        if (data.length() > 10) {
                            Gson gson = new Gson();
                            closed =  gson.fromJson(data, OrderList.class);
                        }

                        for (int i =0;i < closed.order.size();i++)
                        {
                            Order order = closed.order.get(i);
                            if (order.status.equals("PartialFill"))
                            {
                                RowClass row = new RowClass();
                                row.coin = order.symbol;
                                if (order.side == 1)
                                {
                                    row.bidPrice = order.price;
                                    row.bidVol = order.quantity;
                                }
                                else
                                {
                                    row.askPrice = order.price;
                                    row.askVol = order.quantity;
                                }
                                row.time = order.transactionTime;
                                row.vol = order.cumulateQuantity;
                                rows.add(row);

                            }
                        }

                        runOnUiThread(() -> {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                rowAdapter.notifyDataSetChanged();
                                //  rows.addAll(TradePage.PrepareTrades(new ArrayList<>(trades.trade)));
                                //   adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }).start();

            }
        });
        findViewById(R.id.button).setOnClickListener(view -> {
            /*SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
            editor.putString("add",etAdress.getText().toString().trim());
            editor.apply();*/
            Variable.BNBAdress = etAdress.getText().toString().trim().split(";")[0];
            Variable.SetBnbAddress(Variable.BNBAdress);
            rows.clear();
            activeMarkets.clear();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Updating , please wait.");
            dialog.show();
            adapter = new StatisticsAdapter(activeMarkets,MainActivity.this);
            recycler.setAdapter(adapter);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String data = GetClass.GetData(Variable.GetAccount);
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        account =  gson.fromJson(data, Account.class);
                        Log.d("Majid C",account.balances.size()+"");
                    }
                    Log.d("Majid Account",data);


                    data = GetClass.GetData(Variable.GetTicker);
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        Log.d("Majid ticker",data);
                        try {
                            JSONArray jsa = new JSONArray(data);
                            tickers.clear();
                            for (int i = 0;i<jsa.length();i++)
                            {
                                JSONObject js = jsa.getJSONObject(i);
                                tickers.add(gson.fromJson(js.toString(),TickerStatistics.class));

                            }
                        }
                        catch (Exception ex)
                        {

                        }

                    }

                    CalculateBalances();
                    data = GetClass.GetData(Variable.GetMarkets);
                    ArrayList<Market> markets = new ArrayList<>();
                    if (data.length() > 10) {
                        try {

                            JSONArray jsa = new JSONArray(data);
                            for (int i = 0 ; i < jsa.length();i++) {
                                Gson gson = new Gson();
                                JSONObject js = jsa.getJSONObject(i);
                                markets.add(gson.fromJson(js.toString(),Market.class));
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }

                    }
                    globalMarkets.clear();
                    globalMarkets.addAll(markets);
                    AnalyzeMarket(markets);

                  /*  data = GetClass.GetData(Variable.GetDepth);
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        dept =  gson.fromJson(data, MarketDepth.class);
                    }
                    Log.d("Majid Dept",data);
                    data = GetClass.GetData(Variable.GetClosed);
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        closed =  gson.fromJson(data, OrderList.class);
                    }
                    Log.d("Majid closed",data);

                    data = GetClass.GetData(Variable.GetOpened);
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        opened =  gson.fromJson(data, OrderList.class);
                    }
                    Variable.GetTimeSec(opened.order.get(2).transactionTime);
                    //2021-03-20T18:56:25.313Z
                    Log.d("Majid opened",data);


                    data = GetClass.GetData(Variable.GetTicker);
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        Log.d("Majid ticker",data);
                        try {
                                JSONArray jsa = new JSONArray(data);
                                for (int i = 0;i<jsa.length();i++)
                                {
                                   /// Log.d("Majid tickers","Adding "+i + " - "+tickers.size());
                                    JSONObject js = jsa.getJSONObject(i);
                                    tickers.add(gson.fromJson(js.toString(),TickerStatistics.class));

                                }
                        }
                        catch (Exception ex)
                        {

                        }

                    }

                    Date date = new Date();

                    data = GetClass.GetData(Variable.GetTrades+"&start="+(date.getTime()-24*60*60*1000)+"&end="+date.getTime()+"&limit=500");
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        trades =  gson.fromJson(data, TradePage.class);
                    }
                    Log.d("Majid trades",data);
*/
                    runOnUiThread(() -> {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                          //  rows.addAll(TradePage.PrepareTrades(new ArrayList<>(trades.trade)));
                         //   adapter.notifyDataSetChanged();

                        }
                    });


                }
            }).start();
        });

        findViewById(R.id.btGetFiled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartListning();
                SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
                editor.putString("add",etAdress.getText().toString().trim().split(";")[0]);
                editor.apply();
                Variable.BNBAdress = etAdress.getText().toString().trim().split(";")[0];
                Variable.SetBnbAddress(Variable.BNBAdress);
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Updating , please wait.");
                dialog.show();
                rowAdapter = new RowAdapter(rows,MainActivity.this);
                recycler.setAdapter(rowAdapter);
                rows.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rows.clear();
                        activeMarkets.clear();
                        String data = GetClass.GetData(Variable.GetClosedFilled);
                        Log.d("Majid Closed",data);
                        if (data.length() > 10) {
                            Gson gson = new Gson();
                            closed =  gson.fromJson(data, OrderList.class);
                        }

                        for (int i =0;i < closed.order.size();i++)
                        {
                            Order order = closed.order.get(i);
                           // if (order.status.equals("PartialFill"))
                            {
                                RowClass row = new RowClass();
                                row.coin = order.symbol;
                                if (order.side == 1)
                                {
                                    row.bidPrice = order.price;
                                    row.bidVol = order.quantity;
                                }
                                else
                                {
                                    row.askPrice = order.price;
                                    row.askVol = order.quantity;
                                }
                                row.time = order.transactionTime;
                                row.vol = order.cumulateQuantity;
                                rows.add(row);

                            }
                        }

                        runOnUiThread(() -> {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                rowAdapter.notifyDataSetChanged();
                                //  rows.addAll(TradePage.PrepareTrades(new ArrayList<>(trades.trade)));
                                //   adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }).start();

            }
        });
    /*    findViewById(R.id.btStarListen).setOnClickListener(view -> {
                    SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
                    editor.putString("add",etAdress.getText().toString().trim());
                    editor.apply();
                    Variable.BNBAdress = etAdress.getText().toString().trim();
                  Variable.SetBnbAddress(Variable.BNBAdress);
                    Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
                    // Create a PendingIntent to be triggered when the alarm goes off
                    final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // Setup periodic alarm every every half hour from this point onwards
                    long firstMillis = System.currentTimeMillis(); // alarm is set right away
                    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
                    // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
                    alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,10*60*1000,pIntent);
                  //  alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                         //   AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
                    Toast.makeText(MainActivity.this, "LISTNING STARTED .....", Toast.LENGTH_LONG).show();

        });
        */
        findViewById(R.id.myCoins).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
                editor.putString("add",etAdress.getText().toString().trim().split(";")[0]);
                editor.apply();
                Variable.BNBAdress = etAdress.getText().toString().trim().split(";")[0];
                Variable.SetBnbAddress(Variable.BNBAdress);
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Updating , please wait.");
                dialog.show();
                rowAdapter = new RowAdapter(rows,MainActivity.this);
                recycler.setAdapter(rowAdapter);
                rows.clear();
                String chk = tvCoin.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OrderList openOrders = new OrderList();
                        TradePage myTrades = new TradePage();
                        String data = GetClass.GetData(Variable.GetAccount);
                        String dataOpened = GetClass.GetData(Variable.GetOpened);



                        if (dataOpened.length() > 10) {
                            Gson gson = new Gson();
                            openOrders =  gson.fromJson(dataOpened, OrderList.class);
                        }
                        if (globalMarkets.size() == 0) {
                            dataOpened = GetClass.GetData(Variable.GetMarkets);
                            ArrayList<Market> markets = new ArrayList<>();
                            if (dataOpened.length() > 10) {
                                try {

                                    JSONArray jsa = new JSONArray(dataOpened);
                                    for (int i = 0; i < jsa.length(); i++) {
                                        Gson gson = new Gson();
                                        JSONObject js = jsa.getJSONObject(i);
                                        markets.add(gson.fromJson(js.toString(), Market.class));
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                globalMarkets.clear();
                                globalMarkets.addAll(markets);

                            }
                        }

                        dataOpened = GetClass.GetData(Variable.GetClosedFilled);
                        Log.d("Majid Closed",dataOpened);
                        if (dataOpened.length() > 10) {
                            Gson gson = new Gson();
                            closed =  gson.fromJson(dataOpened, OrderList.class);
                        }
                        String dataTrades = GetClass.GetData(Variable.GetMyTrades);
                        if (dataTrades.length() > 10) {
                            Gson gson = new Gson();
                            myTrades =  gson.fromJson(dataTrades, TradePage.class);
                        }
                        if (data.length() > 10) {
                            Gson gson = new Gson();
                            account =  gson.fromJson(data, Account.class);
                            Log.d("Majid C",account.balances.size()+"");
                            for (Balance bla : account.balances)
                            {
                                double lockedB = new Double(bla.locked);
                                double freeB = new Double(bla.free);

                                RowClass row = new RowClass();
                                row.coin = bla.symbol;
                                // if (order.side == 1)
                                {
                                    row.bidPrice = "---";
                                    row.bidVol = bla.free;
                                }
                                //      else
                                {
                                    row.askPrice = "---";
                                    row.askVol = bla.locked;
                                }
                                row.time ="---";

                                for (int i = 0; i < closed.order.size(); i++)
                                {
                                    Order ord = closed.order.get(i);
                                    if (ord.symbol.equals(bla.symbol+"_BNB") && ord.side == 1)
                                    {
                                        row.bidPrice = ord.price;
                                        break;

                                    }

                                }
                                if (row.bidPrice.equals("---"))
                                {
                                    for (int k = 0 ;k < myTrades.trade.size();k++)
                                    {
                                        Trade tr = myTrades.trade.get(k);
                                        if (tr.buyerId.equals(Variable.BNBAdress) && tr.symbol.equals(bla.symbol))
                                        {
                                            row.bidPrice = tr.price;
                                            break;
                                        }
                                    }

                                }
                                if (!row.bidPrice.equals("---")) {
                                    for (Order opOrd : openOrders.order) {
                                        if (opOrd.symbol.equals(bla.symbol + "_BNB")) {
                                            row.askPrice = opOrd.price;
                                            double percent = (Double.parseDouble(opOrd.price) - Double.parseDouble(row.bidPrice)) * 100 / Double.parseDouble(row.bidPrice);
                                            row.time = String.format("%3.2f%%", percent);
                                            break;
                                        }
                                    }
                                }

                                row.vol = (Double.valueOf(bla.free) + Double.valueOf(bla.locked))+"";

                                for (int j = 0 ; j < globalMarkets.size();j++)
                                {
                                    Market mrk = globalMarkets.get(j);
                                    if (mrk.quote_asset_symbol.equals("BNB") && mrk.base_asset_symbol.equals(bla.symbol))
                                    {
                                        if ( (Double.valueOf(bla.free) + Double.valueOf(bla.locked))< Double.valueOf(mrk.lot_size))
                                        {

                                            row.coin = bla.symbol + "(Low lot_size)";

                                        }
                                        break;
                                    }
                                }
                                if (!row.coin.contains("lot_size"))
                                {
                                    rows.add(row);
                                }
                                else if (chk.contains("low"))
                                {
                                    rows.add(row);
                                }


                            }

                        }

                        runOnUiThread(() -> {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                rowAdapter.notifyDataSetChanged();
                                //  rows.addAll(TradePage.PrepareTrades(new ArrayList<>(trades.trade)));
                                //   adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }).start();

            }
        });
        /*findViewById(R.id.btStopListen).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pIntent);
        });*/
        RequestSmsPermission();

    }


    private void RequestSmsPermission() {

        String permission2 = Manifest.permission.WRITE_EXTERNAL_STORAGE;


        int grant = ContextCompat.checkSelfPermission(this, permission2);

        if ( grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];

            permission_list[0] = permission2;
;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }

    }



    private void StartListning()
    {
        SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
        editor.putString("add",etAdress.getText().toString().trim().split(";")[0]);
        editor.apply();
        Variable.BNBAdress = etAdress.getText().toString().trim().split(";")[0];
        Variable.SetBnbAddress(Variable.BNBAdress);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(1,
                new ComponentName(this, CheckerJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(30*60*1000)
                .setRequiresBatteryNotLow(false)
                .build());
        /*Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,INTERVAL_FIFTEEN_MINUTES,pIntent);
        //  alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
        //   AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);

        */
        Toast.makeText(MainActivity.this, "LISTNING STARTED .....", Toast.LENGTH_LONG).show();
    }

    double allBnb = 0;
    private void CalculateBalances()
    {

        List<Account> accs = new ArrayList<>();
        String[] addreses = etAdress.getText().toString().trim().split(";");
        for (String addr : addreses)
        {
            String getAccInfo = BaseUrl+"account/"+addr;
            String data = GetClass.GetData(getAccInfo);
            if (data.length() > 10) {
                Gson gson = new Gson();
                accs.add(gson.fromJson(data, Account.class));
            }
        }

        totalHigh = 0;
        totalLow = 0;
        allBnb = 0;
        for (Account account : accs) {
            for (Balance bla : account.balances) {

                try {
                    if (bla.symbol.equals("BNB")) {
                        double lockedB = new Double(bla.locked);
                        double freeB = new Double(bla.free);
                        totalHigh = totalHigh + lockedB + freeB;
                        totalLow = totalLow + lockedB + freeB;
                        totalBnb = lockedB + freeB;
                        allBnb = allBnb + totalBnb;
                        continue;
                    }
                    double locked = new Double(bla.locked) + new Double(bla.free);
                    if (locked != 0) {
                        Log.d("Majid", bla.symbol + " " + locked);
                        TickerStatistics selectedTc = null;
                        for (TickerStatistics tc : tickers) {
                            if (tc.symbol.equals(bla.symbol + "_BNB")) {
                                selectedTc = tc;
                                break;
                            }
                        }
                        if (selectedTc != null) {
                            Log.d("Majid", selectedTc.bidPrice + " " + selectedTc.askPrice);
                            totalHigh = totalHigh + locked * new Double(selectedTc.askPrice);

                            totalLow = totalLow + locked * new Double(selectedTc.bidPrice);
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

        ckekBotStatus();
        runOnUiThread(() -> {

                tvBnb.setText(String.format("BNB : %.4f",allBnb));
                tvHigh.setText(String.format("High BNB : %.4f-%d",totalHigh,accs.size()));
                tvLow.setText(String.format("Low BNB :%.4f",totalLow));

        });
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



    private void AnalyzeMarket(ArrayList<Market> markets)
    {
        String base = "BNB";
        int time = new Integer(tvTime.getText().toString());
        String data = GetClass.GetData(Variable.GetTrades+"start="+(System.currentTimeMillis() - time * 60 * 1000)+"&quoteAsset="+base+"&limit=1000");
        if (data.length() > 10) {
            Gson gson = new Gson();
            trades =  gson.fromJson(data, TradePage.class);
        }

        for (Market market : markets) {
            try {
                if (!market.getQuoteAssetSymbol().equals(base)) {
                    continue;
                }
               // log.info("### {} ###", market.getBaseAssetSymbol());
               // log.info("quoteAssetSymbol: {} baseAssetSymbol: {} price: {} tick: {} lot: {}", market.getQuoteAssetSymbol(), market.getBaseAssetSymbol(),
                 //       market.getPrice(), market.getTickSize(), market.getLotSize());
                String symbol = market.getBaseAssetSymbol() + "_"+base;
               // OrderBook orderBook = client.getOrderBook(symbol, 5);
                //// OrderBook orderBook = client.getOrderBook("PND-943_BNB", 5);
               // Thread.sleep(10);

                TradesRequest tradesRequest = new TradesRequest();
                // tradesRequest.setQuoteAsset("BNB");
                //1616588028 000
//               /1616588073 350

                tradesRequest.setStart(System.currentTimeMillis() - time * 60 * 1000); // minutes
                tradesRequest.setSymbol(symbol);
               /// TradePage trades = client.getTrades(tradesRequest);

              /*
                String data = GetClass.GetData(Variable.GetTrades+"start="+(tradesRequest.getStart())+"&symbol="+tradesRequest.getSymbol()+"&limit=500");
                if (data.length() > 10) {
                    Gson gson = new Gson();
                    trades =  gson.fromJson(data, TradePage.class);
                }

                */
                TradePage newTrades = new TradePage();
                for (Trade tr : trades.trade)
                {
                    if (tr.symbol.equals(symbol))
                    {
                        newTrades.trade.add(tr);
                    }
                }

                if (!newTrades.getTrade().isEmpty()) {

                    TickerStatistics selectedTc = null;
                    for (TickerStatistics tc : tickers)
                    {
                        if (tc.symbol.equals(symbol))
                        {
                            selectedTc = tc;
                            break;
                        }
                    }
                   // OrderBook orderBook = client.getOrderBook(symbol, 5);
                    /*data = GetClass.GetData(Variable.GetDepth+symbol+"&limit=2");
                    if (data.length() > 10) {
                        Gson gson = new Gson();
                        dept =  gson.fromJson(data, MarketDepth.class);
                    }*/

                    Optional<Trade> max = newTrades.getTrade().stream().max(Comparator.comparing(
                            Trade::getPrice, (p1, p2) -> {
                                return Double.compare(Double.parseDouble(p1), Double.parseDouble(p2));
                            }));
                    Optional<Trade> min = newTrades.getTrade().stream().min(Comparator.comparing(
                            Trade::getPrice, (p1, p2) -> {
                                return Double.compare(Double.parseDouble(p1), Double.parseDouble(p2));
                            }));
                    double delta = Double.parseDouble(max.get().getPrice()) - Double.parseDouble(min.get().getPrice());
                    //log.info("{}", orderBook);
                    //log.info("trades.trade.size: {} max: {} min: {} delta: {} ", trades.getTrade().size(), max.get().getPrice(), min.get().getPrice(), delta);
                    if (Double.parseDouble(min.get().getPrice()) <= 15) {
                        Log.d("Adding",symbol+ " " +newTrades.getTrade().size());
                        Statistic stat = new Statistic(symbol, newTrades.getTrade().size(), max.get().getPrice(), min.get().getPrice(), delta);
                        double percent = 0 ;
                        if (selectedTc != null)
                        {
                            stat.count = selectedTc.count;
                            stat.quoteVolume = selectedTc.quoteVolume;
                            stat.weightedAvgPrice = selectedTc.weightedAvgPrice;
                            stat.askPrice = selectedTc.askPrice;
                            stat.bidPrice = selectedTc.bidPrice;
                            try{
                                double bid = new Double(selectedTc.bidPrice);
                                double ask = new Double(selectedTc.askPrice);
                                percent = (ask - bid) * 100 /bid ;
                                stat.profitPercent = String.format("%.2f%%",percent);
                            }
                            catch (Exception ex)
                            {

                            }

                        }
                        if ( new Double(stat.quoteVolume) > 1.0 && percent > 3 )
                             activeMarkets.add(stat);

                    }
                } else {
                    Log.d("trades.trade.size: {}", trades.getTrade().size()+"");
                }
                /*
                 * for (Trade trade : trades.getTrade()) {
                 * log.info("trade quoteAsset: {} symbol: {} price: {} quantity: {} time: {}",
                 * trade.getQuoteAsset(), trade.getSymbol(), trade.getPrice(),
                 * trade.getQuantity(), trade.getTime());
                 * }
                 */
            } catch (RuntimeException ex) {
                Log.e("RuntimeException ex: {}", ex.getMessage());
            }
            //Thread.sleep(10);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.sort(activeMarkets, new Comparator<Statistic>() {
                    @Override
                    public int compare(Statistic rowClass, Statistic t1) {
                        if(rowClass.getTradeSize() == t1.getTradeSize())
                            return 0;
                        if (rowClass.getTradeSize() < t1.getTradeSize())
                            return 1;
                        if (rowClass.getTradeSize() > t1.getTradeSize())
                            return -1;
                        return 0;
                    }
                });
                  adapter.notifyDataSetChanged();
            }
        });

        /*StringBuilder str = new StringBuilder();
        for (Statistic a : activeMarkets)
        {
            str.append("\n"+a.getSymbol()+"--->"+a.getTradeSize());
        }*/
     //  log.info("\n{}\n",str.toString());

    }

}