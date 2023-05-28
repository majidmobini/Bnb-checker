package com.home.bnbchecker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    List<Statistic> rows = new ArrayList<>();

    private Context mContext;

    public StatisticsAdapter(List<Statistic> rows, Context ctx) {
        this.rows = rows;
        this.mContext = ctx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycler_active_market, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Statistic row = rows.get(position);
        holder.tvMarket.setText(row.getSymbol());
        holder.tvSize.setText(row.getTradeSize()+"");

        holder.tvAllSize.setText(row.count+"");
        holder.tvVol.setText(row.quoteVolume);
        holder.tvWeight.setText(row.weightedAvgPrice);
        holder.tvPer.setText(row.profitPercent);
        holder.tvAskPrice.setText(row.askPrice);
        holder.tvBidPrice.setText(row.bidPrice);
       // holder.llMainBack.setBackgroundColor(Color.argb(255,98,68,56));
        holder.llMainBack.setBackgroundColor(Color.argb(255,251,175,143));

        try
        {
            double ask = Double.parseDouble(row.askPrice);
            double bid = Double.parseDouble(row.bidPrice);
            double weight = Double.parseDouble(row.weightedAvgPrice);
            double per = Double.parseDouble(row.profitPercent.replace("%",""));
            if (weight< ask && weight > bid && per > 10 )
            {
                if ( ((ask - weight)*100/weight) < 20 || ((weight-bid)*100/bid) > 10 )
                {//FBAF8F
                    holder.llMainBack.setBackgroundColor(Color.argb(255,4,255,30));

                }
            }

        }
        catch (Exception ex)
        {



        }



    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMarket;
        public TextView tvSize;
        public TextView tvAllSize;
        public TextView tvVol;
        public TextView tvPer;
        public TextView tvWeight;
        public TextView tvAskPrice;
        public TextView tvBidPrice;
        public View llMainBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llMainBack = itemView.findViewById(R.id.mainBack);
            tvMarket = itemView.findViewById(R.id.tvMarket);
            tvSize = itemView.findViewById(R.id.tvCount);
            tvAllSize = itemView.findViewById(R.id.tvAllCount);
            tvVol = itemView.findViewById(R.id.tvVol);
            tvPer = itemView.findViewById(R.id.tvPercent);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvAskPrice = itemView.findViewById(R.id.tvAskPrice);
            tvBidPrice = itemView.findViewById(R.id.tvBidPrice);
        }
    }

}
