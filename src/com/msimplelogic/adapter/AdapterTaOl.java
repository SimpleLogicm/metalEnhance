package com.msimplelogic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.TargetOrderListModel;
import java.util.List;

public class AdapterTaOl extends RecyclerView.Adapter<AdapterTaOl.PlayerViewHolder> {
    public List<TargetOrderListModel> torderlist;
    Context context;
    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, customer, club, rating, age;

        public PlayerViewHolder(View view) {
            super(view);
            orderId = (TextView) view.findViewById(R.id.txt1_prev);
            customer = (TextView) view.findViewById(R.id.txt2_prev);
//            club = (TextView) view.findViewById(R.id.club);
//            rating = (TextView) view.findViewById(R.id.rating);
//            age = (TextView) view.findViewById(R.id.age);
        }
    }

    public AdapterTaOl(List<TargetOrderListModel> torderlist, Context context) {
        this.torderlist = torderlist;
        this.context=context;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ta_ol_items, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global_Data.Custom_Toast(context, "Details","");
            }
        });
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        TargetOrderListModel targetOrderListModel = torderlist.get(position);
        holder.orderId.setText(targetOrderListModel.getOrderId());
        holder.customer.setText(targetOrderListModel.getProductName());
//        holder.club.setText(player.getClub());
//        holder.rating.setText(player.getRating().toString());
//        holder.age.setText(player.getAge().toString());
    }

    @Override
    public int getItemCount() {
        return torderlist.size();
    }
}

