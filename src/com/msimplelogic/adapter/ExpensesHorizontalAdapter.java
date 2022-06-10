package com.msimplelogic.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msimplelogic.activities.ExpenseDetailsActivity;
import com.msimplelogic.activities.ExpensesNewActivity;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.ExpensesModel;
import java.util.List;

public class ExpensesHorizontalAdapter extends RecyclerView.Adapter<ExpensesHorizontalAdapter.GroceryViewHolder>{
    private List<ExpensesModel> horizontalGrocderyList;
    Context context;

    public ExpensesHorizontalAdapter(Context context,List<ExpensesModel> horizontalGrocderyList){
        this.context = context;
        this.horizontalGrocderyList= horizontalGrocderyList;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_horizontal_adapter, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {

        final ExpensesModel employee_model = horizontalGrocderyList.get(position);

        holder.txtAmount.setText(employee_model.getExpenseAmount());
        if(Global_Data.ExpenseName.equalsIgnoreCase("Conveyance"))
        {
            holder.txtName.setText("Mode of Travel : "+employee_model.getExpenseName());
        }else if(Global_Data.ExpenseName.equalsIgnoreCase("Food"))
        {
            holder.txtName.setText("Hotel Name : "+employee_model.getExpenseName());
        }else if(Global_Data.ExpenseName.equalsIgnoreCase("Hotel"))
        {
            holder.txtName.setText("Hotel Name : "+employee_model.getExpenseName());
        }else if(Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous"))
        {
            holder.txtName.setText("Description : "+employee_model.getExpenseName());
        }

        holder.txtStatus.setText(employee_model.getExpensestatus());
        holder.txtDate.setText("Applied On : "+employee_model.getExpenseDate());

        holder.horRlout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.ExpenseId = employee_model.getExpenseId();
                Global_Data.ExpenseStatus = employee_model.getExpensestatus();
                Intent m = new Intent(context, ExpenseDetailsActivity.class);
                m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //m.putExtra("SUBCAT_ID", horizontalGrocderyList.get(position).getProductId());
                context.startActivity(m);
                //Toast.makeText(context, Global_Data.ExpenseId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount,txtName,txtStatus,txtDate;
        RelativeLayout horRlout;
        public GroceryViewHolder(View view) {
            super(view);
            txtAmount=view.findViewById(R.id.example_row_tv_title);
            txtName=view.findViewById(R.id.order_idn);
            txtStatus=view.findViewById(R.id.example_row_tv_description);
            txtDate=view.findViewById(R.id.example_row_tv_price);


            horRlout=view.findViewById(R.id.hor_rlout);
        }
    }
}