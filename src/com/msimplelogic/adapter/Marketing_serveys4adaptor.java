package com.msimplelogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.Marketing_serveys4;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Marketing_serveys4model;

import java.util.List;

public class Marketing_serveys4adaptor extends RecyclerView.Adapter<Marketing_serveys4adaptor.MyViewHolder> {
    private Context context;
    List<Marketing_serveys4model> list;

    public Marketing_serveys4adaptor(@NonNull Context context, List<Marketing_serveys4model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override



    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_marketing_servays4, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (list.get(0).titalforcard.equalsIgnoreCase("First")){
            holder.card1_2input.setVisibility(View.VISIBLE);
            holder.card2_1input.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         CardView card1_2input,card2_1input;
        TextView no_of_products2,tv_no_products1,tv_on_floor,tv_on_shell;
        EditText et_onfloor,et_on_shell,et_description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card2_1input=itemView.findViewById(R.id.card2_1input);
            card1_2input=itemView.findViewById(R.id.card1_2input);
            no_of_products2=itemView.findViewById(R.id.no_of_products2);
            tv_no_products1=itemView.findViewById(R.id.tv_no_products1);
            tv_on_floor=itemView.findViewById(R.id.tv_on_floor);
            tv_on_shell=itemView.findViewById(R.id.tv_on_shell);
            et_onfloor=itemView.findViewById(R.id.et_onfloor);
            et_on_shell=itemView.findViewById(R.id.et_on_shell);
            et_description=itemView.findViewById(R.id.et_description);

        }
    }
}
