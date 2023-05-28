package com.home.bnbchecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TradePage {
    long total;//	long	total number of trades
    List<Trade> trade = new ArrayList<>();
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Trade> getTrade() {
        return trade;
    }

    public void setTrade(ArrayList<Trade> trade) {
        this.trade = trade;
    }


    public static ArrayList<RowClass> PrepareTrades(ArrayList<Trade> trades)
    {
        List<RowClass> rows = new ArrayList<>();
        HashMap<String,Integer> counts = new HashMap<>();
        for (int i = 0;i<trades.size();i++)
        {

            Trade trade = trades.get(i);
            if (!trade.quoteAsset.equals("BNB"))
            {
                continue;
            }
            RowClass row = new RowClass();
            row.bidPrice = trade.price;
            row.bidVol = trade.quantity;
            row.time = Variable.Settime(trade.time);
            trade.count = 0;
            if(counts.containsKey(trade.symbol))
            {
                trade.count = counts.get(trade.symbol);
            }
            else {
                for (int j = 0; j < trades.size(); j++) {
                    if (trades.get(j).symbol.equals(trade.symbol)) {
                        trade.count = trade.count + 1;
                    }
                }
                counts.put(trade.symbol, trade.count);
            }
            row.count = trade.count;
            row.coin = trade.symbol + "("+trade.count+")";
            rows.add(row);
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            rows.sort(new Comparator<RowClass>() {
                @Override
                public int compare(RowClass rowClass, RowClass t1) {
                    if(rowClass.count == t1.count)
                        return 0;
                    if (rowClass.count < t1.count)
                            return 1;
                    if (rowClass.count > t1.count)
                        return -1;
                    return 0;
                }
            });
        }*/
        Collections.sort(rows, new Comparator<RowClass>() {
            @Override
            public int compare(RowClass rowClass, RowClass t1) {
                if(rowClass.count == t1.count)
                    return 0;
                if (rowClass.count < t1.count)
                    return 1;
                if (rowClass.count > t1.count)
                    return -1;
                return 0;
            }
        });

        return new ArrayList<>(rows);

    }
}



