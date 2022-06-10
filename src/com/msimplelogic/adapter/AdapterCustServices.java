package com.msimplelogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.CustServicesModel;
import com.msimplelogic.model.Cust_feed_new2_Model;

import java.util.ArrayList;
import java.util.List;

public class AdapterCustServices extends RecyclerView.Adapter<AdapterCustServices.MyViewHolder> {
    public Context context;
    ArrayList<CustServicesModel> users;

    public AdapterCustServices(Context context, ArrayList<CustServicesModel> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_customerservices, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(users.get(position).description);
        holder.tvHome.setText(users.get(position).date);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvHome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.cust_desc);
            tvHome = (TextView) itemView.findViewById(R.id.cust_date);
        }
    }
}