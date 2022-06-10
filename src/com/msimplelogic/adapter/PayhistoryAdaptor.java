package com.msimplelogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.R;
import com.msimplelogic.model.PayhistoryModel;

import java.util.List;

public class PayhistoryAdaptor extends RecyclerView.Adapter<PayhistoryAdaptor.MyViewHolder> {
    Context context;
    private List<PayhistoryModel> array;

    public PayhistoryAdaptor(Context context, List<PayhistoryModel> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public PayhistoryAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_payhistory, parent, false);
        return new PayhistoryAdaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PayhistoryAdaptor.MyViewHolder holder, int position) {
        holder.date.setText(array.get(position).getDate());
        holder.amount.setText("₹ "+array.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}
