package com.home.bnbchecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {

    ArrayList<RowClass> rows = new ArrayList<>();

    private Context mContext;

    public RowAdapter(ArrayList<RowClass> rows, Context ctx) {
        this.rows = rows;
        this.mContext = ctx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycler_general_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RowClass row = rows.get(position);
        holder.tvCoin.setText(row.coin);
        holder.tvVol.setText(row.vol);
        holder.tvBidPrice.setText(row.bidPrice);
        holder.tvBidVol.setText(row.bidVol);
        holder.tvAskPrice.setText(row.askPrice);
        holder.tvAskVol.setText(row.askVol);
        holder.tvTime.setText(row.time);
        holder.tvType.setText(row.type);


    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCoin;
        public TextView tvVol;
        public TextView tvBidPrice;
        public TextView tvBidVol;
        public TextView tvAskPrice;
        public TextView tvAskVol;
        public TextView tvTime;
        public TextView tvType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoin = itemView.findViewById(R.id.tvCoin);
            tvVol = itemView.findViewById(R.id.tvVol);
            tvBidPrice = itemView.findViewById(R.id.tvBid);
            tvBidVol = itemView.findViewById(R.id.tvBidVol);
            tvAskPrice = itemView.findViewById(R.id.tvAsk);
            tvAskVol = itemView.findViewById(R.id.tvAskVol);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }

}
