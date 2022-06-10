package com.msimplelogic.adapter;

/**
 * Created by vinod on 04-10-2016.
 */

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msimplelogic.activities.Customer_info_main;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.webservice.ConnectionDetector;

import java.util.List;

import cpm.simplelogic.helper.Customer_Info;

public class Customer_Outstanding_Adapter extends RecyclerView.Adapter<Customer_Outstanding_Adapter.ContactViewHolder> {

    static ConnectionDetector cd;
    static Boolean isInternetPresent = false;
    private List<Customer_Info> contactList;
    private List<Customer_Info> contactListfilter;

    static Context mcontext;

    public Customer_Outstanding_Adapter(List<Customer_Info> contactList, Context context) {
        this.contactList = contactList;
        this.mcontext = context;
        //this.contactListfilter.addAll(this.contactList);
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(Customer_Outstanding_Adapter.ContactViewHolder contactViewHolder, int i) {
        Customer_Info ci = contactList.get(i);
        contactViewHolder.c_name.setText(ci.name);
        contactViewHolder.C_code.setText(ci.cus_code);
        // contactViewHolder.c_name2.setText(ci.name);
        contactViewHolder.c_ammount1.setText(ci.amount1);
        contactViewHolder.c_ammount2.setText(ci.amount2);

    }

    @Override
    public Customer_Outstanding_Adapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.customer_outstanding_adapter, viewGroup, false);

        return new Customer_Outstanding_Adapter.ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView c_name;
        protected TextView c_name2;
        protected TextView C_code;

        // protected TextView c_credit_profile;
        protected TextView c_ammount1;
        protected TextView c_ammount2;
        protected RelativeLayout outlayout;

        public ContactViewHolder(View v) {
            super(v);

            c_name = (TextView) v.findViewById(R.id.c_name);
            c_name2 = (TextView) v.findViewById(R.id.c_name2);
            C_code = (TextView) v.findViewById(R.id.C_code);
            c_ammount1 = (TextView) v.findViewById(R.id.c_ammount1);
            c_ammount2 = (TextView) v.findViewById(R.id.c_ammount2);
            outlayout = (RelativeLayout) v.findViewById(R.id.outlayout);

            outlayout.setOnClickListener(this);


        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position

            String c_code = C_code.getText().toString().trim();
            // Toast.makeText(mcontext, ""+c_code, Toast.LENGTH_SHORT).show();
            Global_Data.Globelo_OU_CUSTID = c_code;
            Intent callIntent = new Intent(mcontext, Customer_info_main.class);
            Global_Data.customeroutstanding="yes";
            view.getContext().startActivity(callIntent);
            //((Activity) mcontext).finish();


        }
    }


}