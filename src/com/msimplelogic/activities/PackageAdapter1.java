package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackageAdapter1 extends ArrayAdapter<HashMap<String, String>> {

    customButtonListener customListner;
    static final String TAG_ORDERID = "order_id";
    static final String TAG_CUSTOMER_NAME = "customer_name";
    static final String TAG_ADDRESS = "address";

    HashMap<String, String> getData = new HashMap<String, String>();
    private ArrayList<HashMap<String, String>> dataAray;
    HashMap<String, String> dataIthjem;
    DataBaseHelper dbvoc;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, View v);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;

    public PackageAdapter1(Context context, ArrayList<HashMap<String, String>> dataItem1) {
        super(context, R.layout.orderlist_adapter, dataItem1);
        this.dataAray = dataItem1;
        this.context = context;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ShowToast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final Product item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.orderlist_adapter, parent, false);
            holder = new ViewHolder();
            holder.o_customer_name = convertView.findViewById(R.id.o_customer_name);
            holder.o_oder_id = (TextView) convertView.findViewById(R.id.o_oder_id);
            holder.o_address = (TextView) convertView.findViewById(R.id.o_address);
            holder.o_view = convertView.findViewById(R.id.o_view);
            holder.o_delete =  convertView.findViewById(R.id.o_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       // ((ListView) parent).recycle(convertView, position);

        getData = dataAray.get(position);

        holder.o_customer_name.setText(getData.get(TAG_CUSTOMER_NAME));
        holder.o_oder_id.setText(getData.get(TAG_ORDERID));
        holder.o_address.setText(getData.get(TAG_ADDRESS));


        holder.o_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData = dataAray.get(position);

                Log.d("EDIT ORDER ID", "EDIT ORDER ID" + getData.get(TAG_ORDERID).toString());


                dbvoc = new DataBaseHelper(context);
                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
                Global_Data.GLObalOrder_id = getData.get(TAG_ORDERID).toString();

                Global_Data.GLOvel_GORDER_ID = getData.get(TAG_ORDERID).toString();

                List<Local_Data> contacts = dbvoc.GetOrdersBYORDER_ID(getData.get(TAG_ORDERID).toString());
                //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");

                for (Local_Data cn : contacts) {
                    Global_Data.GLOvel_CUSTOMER_ID = cn.getLEGACY_CUSTOMER_CODE();
                }

                List<Local_Data> contactnew = dbvoc.getCustomername(Global_Data.GLOvel_CUSTOMER_ID);
                for (Local_Data cn : contactnew) {
                    Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
                    Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
                    Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
                    Global_Data.order_retailer = cn.getCUSTOMER_SHOPNAME();
                }

                try {

                    List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Global_Data.GLOvel_CUSTOMER_ID);
                    if (contactlimit.size() > 0) {

                        for (Local_Data cn : contactlimit) {

//
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_shedule_outstanding_amount())) {


                                Global_Data.AmountOutstanding = cn.get_shedule_outstanding_amount();
                            } else {


                                Global_Data.AmountOutstanding = "0.0";
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getAmmount_overdue())) {


                                Global_Data.AmountOverdue = cn.getAmmount_overdue();
                            } else {

                                Global_Data.AmountOverdue = "0.0";
                            }


                        }
                    } else {
                        Global_Data.AmountOutstanding = "0.0";
                        Global_Data.AmountOverdue = "0.0";

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
                Global_Data.PREVIOUS_ORDER_BACK_FLAG = "TRUE";

                Intent goToNewOrderActivity = new Intent(context, PreviewOrderSwipeActivity.class);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
                context.startActivity(goToNewOrderActivity);

            }
        });

        holder.o_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
                alertDialog.setTitle(context.getResources().getString(R.string.Warning));
                alertDialog.setMessage(context.getResources().getString(R.string.Product_Delete_dialog_message));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        getData = dataAray.get(position);
                        Log.d("DELETE ORDER ID", "DELETE ORDER ID" + getData.get(TAG_ORDERID).toString());

                        dbvoc = new DataBaseHelper(context);
                        dbvoc.getDeleteTableorder_byORDER_ID(getData.get(TAG_ORDERID).toString());
                        dataAray.remove(position);
                        dialog.cancel();
                        Global_Data.Custom_Toast(context,context.getResources().getString(R.string.Order_Deleted_suc),"Yes");

                        notifyDataSetChanged();

                        if (dataAray.size() <= 0) {
                            Intent goToNewOrderActivity = new Intent(context, MainActivity.class);
                            context.startActivity(goToNewOrderActivity);
                        } else {

                            Global_Data.Custom_Toast(context,context.getResources().getString(R.string.Order_Deleted),"Yes");
                            dialog.cancel();
                        }


                    }
                });

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });


                alertDialog.show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView o_customer_name, o_oder_id;
        TextView o_address;
        ImageView o_view;
        ImageView o_delete;

    }

}
