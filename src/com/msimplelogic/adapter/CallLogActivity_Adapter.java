package com.msimplelogic.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.slidingmenu.AddRetailerFragment;
import com.msimplelogic.webservice.ConnectionDetector;

import java.util.List;

import cpm.simplelogic.helper.Customer_Info;

public class CallLogActivity_Adapter extends RecyclerView.Adapter<CallLogActivity_Adapter.ContactViewHolder> {

    static ConnectionDetector cd;
    static Boolean isInternetPresent = false;
    private List<Customer_Info> contactList;
    private List<Customer_Info> contactListfilter;
    static DataBaseHelper dbvoc;

    static Context mcontext;

    public CallLogActivity_Adapter(List<Customer_Info> contactList, Context context) {
        this.contactList = contactList;
        this.mcontext = context;
        //this.contactListfilter.addAll(this.contactList);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(CallLogActivity_Adapter.ContactViewHolder contactViewHolder, int i) {
        Customer_Info ci = contactList.get(i);
        contactViewHolder.contact_name.setText(ci.getContact_name());
        contactViewHolder.contact_mobile.setText(ci.getContact_mobile());
        contactViewHolder.contact_mobilehidden.setText(ci.getContact_mobileh());
        contactViewHolder.contact_call_type.setText(ci.getContact_call_type());
        contactViewHolder.contact_call_duration.setText(ci.getContact_call_duration());
        contactViewHolder.contact_timestamp.setText(ci.getContact_timestamp());

    }

    @Override
    public CallLogActivity_Adapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.calllogactivity_adapter, viewGroup, false);
        return new CallLogActivity_Adapter.ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView contact_name;
        protected TextView contact_mobile;
        protected TextView contact_call_type;
        protected TextView contact_call_duration;
        protected TextView contact_timestamp;
        protected TextView contact_mobilehidden;
        protected Button contact_add;


        public ContactViewHolder(View v) {
            super(v);

            contact_name = (TextView) v.findViewById(R.id.contact_name);
            contact_mobile = (TextView) v.findViewById(R.id.contact_mobile);
            contact_call_type = (TextView) v.findViewById(R.id.contact_call_type);
            contact_call_duration = (TextView) v.findViewById(R.id.contact_call_duration);
            contact_timestamp = (TextView) v.findViewById(R.id.contact_timestamp);
            contact_mobilehidden = (TextView) v.findViewById(R.id.contact_mobilehidden);
            contact_add = (Button) v.findViewById(R.id.contact_add);


            contact_add.setOnClickListener(this);

        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            cd = new ConnectionDetector(view.getContext());
            isInternetPresent = cd.isConnectingToInternet();

            if (view.getId() == contact_add.getId()) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(contact_name.getText().toString()) && !contact_name.getText().toString().equalsIgnoreCase("Unknown")) {
                    Global_Data.Glovel_Contact_name = contact_name.getText().toString();
                } else {
                    Global_Data.Glovel_Contact_name = "";
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(contact_mobilehidden.getText().toString())) {
                    Global_Data.Glovel_Contact_Mobile_Number = contact_mobilehidden.getText().toString();
                } else {
                    Global_Data.Glovel_Contact_Mobile_Number = "";
                }

                try {
                    dbvoc = new DataBaseHelper(view.getContext());
                    List<Local_Data> cont1 = dbvoc.getmob_byshop_mobile(contact_mobilehidden.getText().toString());

                    if (cont1.size() > 0) {
                        Global_Data.Custom_Toast(view.getContext(), "Retailer Already Exist.","");
                    } else {
                        Intent intent = new Intent(view.getContext(), AddRetailerFragment.class);
                        view.getContext().startActivity(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    //Toast.makeText(view.getContext(), "Retailer Already Exist.", Toast.LENGTH_SHORT).show();
                }


                //getContext().finish();
            }

        }
    }


}

