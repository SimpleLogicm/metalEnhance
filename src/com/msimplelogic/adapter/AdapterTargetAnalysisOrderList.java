package com.msimplelogic.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.MainActivity;
import com.msimplelogic.activities.PreviewOrderSwipeActivity;
import com.msimplelogic.activities.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterTargetAnalysisOrderList extends ArrayAdapter<HashMap<String, String>> {

    AdapterTargetAnalysisOrderList customListner;
    static final String TAG_ORDERID = "order_id";
    static final String TAG_PRODUCTNM = "product_name";
//    static final String TAG_ITEMNAME = "product_name";
//    static final String TAG_QTY = "total_qty";
//    static final String TAG_PRICE = "MRP";
//    static final String TAG_AMOUNT = "amount";
//    static final String TAG_ITEM_NUMBER = "item_number";

    static final String TAG_TOTALQTY = "total_qty";
    static final String TAG_RETAILPRICE = "retail_price";
    static final String TAG_MRP = "mrp";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_CUSTSHOPNAME = "shop_name";

    HashMap<String, String> getData = new HashMap<String, String>();
    private ArrayList<HashMap<String, String>> dataAray;
    HashMap<String, String> dataIthjem;
    DataBaseHelper dbvoc;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, View v);
    }

    public void setCustomButtonListner(AdapterTargetAnalysisOrderList listener) {
        this.customListner = listener;
    }

    private Context context;

    public AdapterTargetAnalysisOrderList(Context context, ArrayList<HashMap<String, String>> dataItem1) {
        super(context, R.layout.adapter_ta_orderlist, dataItem1);
        this.dataAray = dataItem1;
        this.context = context;
    }

//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Product getItem(int position) {
//        return data.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (position == 2) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    @SuppressLint("ShowToast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final Product item = getItem(position);
        //final com.simplelogic.adapter.AdapterTargetAnalysisOrderList.ViewHolder holder;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.adapter_ta_orderlist, parent, false);
            //holder = new com.simplelogic.adapter.AdapterTargetAnalysisOrderList.ViewHolder();
            holder = new ViewHolder();

            holder.tvtext1 = (TextView) convertView.findViewById(R.id.txt1_prev);
            holder.tvtext2 = (TextView) convertView.findViewById(R.id.txt2_prev);
//            holder.tvPriece = (TextView) convertView.findViewById(R.id.example_row_tv_price);
//            holder.order_idn = (TextView) convertView.findViewById(R.id.order_idn);
            holder.bAction1 = (Button) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (Button) convertView.findViewById(R.id.example_row_b_action_2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
           // holder = (com.simplelogic.adapter.AdapterTargetAnalysisOrderList.ViewHolder) convertView.getTag();
        }

        //((SwipeListView) parent).recycle(convertView, position);

        getData = dataAray.get(position);

//        String aws=getData.get(TAG_ORDERID);
//        String awss=getData.get(TAG_PRODUCTNM);
//
//        System.out.println(aws+""+awss);

        holder.tvtext1.setText(getData.get(TAG_ORDERID));
        holder.tvtext2.setText(getData.get(TAG_PRODUCTNM));

        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData = dataAray.get(position);

                Log.d("EDIT ORDER ID", "EDIT ORDER ID" + getData.get(TAG_ORDERID).toString());

                Intent goToNewOrderActivity = new Intent(context, PreviewOrderSwipeActivity.class);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);

                goToNewOrderActivity.putExtra("ORDER_ID", getData.get(TAG_ORDERID));
                goToNewOrderActivity.putExtra("PRODUCT_NAME", getData.get(TAG_PRODUCTNM));
                goToNewOrderActivity.putExtra("TOTAL_QTY", getData.get(TAG_TOTALQTY));
                goToNewOrderActivity.putExtra("RETAIL_PRICE", getData.get(TAG_RETAILPRICE));
                goToNewOrderActivity.putExtra("MRP", getData.get(TAG_MRP));
                goToNewOrderActivity.putExtra("AMOUNT", getData.get(TAG_AMOUNT));
                Global_Data.order_retailer = getData.get(TAG_CUSTSHOPNAME);
                goToNewOrderActivity.putExtra("TA_ORDERLIST_STATUS", "orderlist_status");
                context.startActivity(goToNewOrderActivity);

            }
        });

        holder.bAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (isPlayStoreInstalled()) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + item.getPackageName())));
                } */

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
                        notifyDataSetChanged();

                        if (dataAray.size() <= 0) {
                            Intent goToNewOrderActivity = new Intent(context, MainActivity.class);
                            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
                            context.startActivity(goToNewOrderActivity);
                        } else {


                            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Item_Deleted), "yes");
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
        TextView tvtext1, tvtext2;
        TextView tvDescription;
        TextView tvPriece;
        Button bAction1;
        Button bAction2;
    }
}
