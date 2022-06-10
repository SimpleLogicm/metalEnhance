package com.msimplelogic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Order_Delivery_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import cpm.simplelogic.helper.MyOrder_totalInterface;

public class Order_Delivery_Adapter extends RecyclerView.Adapter<Order_Delivery_Adapter.MyViewHolder> {
    View itemView;
    private MyOrder_totalInterface listener;
    private List<Order_Delivery_Model> order_item_list;
    // private List<RetItem> _retData;
    Context contextm;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ditem_id1;
        public TextView product_named;
        public TextView order_name_value;
        public TextView balance_stock_value;
        public TextView order_qty_value;
        public TextView dquantity_error;
        public EditText delivered_qty_value;
        public RelativeLayout brandnamerf;
        LinearLayout brand_strip;

        //public TextView stock_addno12,radio_btn;

        public MyViewHolder(View view) {
            super(view);
            ditem_id1 = (TextView) view.findViewById(R.id.ditem_id1);
            product_named = (TextView) view.findViewById(R.id.product_named);
            order_name_value = (TextView) view.findViewById(R.id.order_name_value);
            balance_stock_value = (TextView) view.findViewById(R.id.balance_stock_value);
            order_qty_value = (TextView) view.findViewById(R.id.order_qty_value);
            dquantity_error = (TextView) view.findViewById(R.id.dquantity_error);
            delivered_qty_value = (EditText) view.findViewById(R.id.delivered_qty_value);

            brandnamerf = (RelativeLayout) view.findViewById(R.id.brandnamerf);
//            brand_strip = (LinearLayout) view.findViewById(R.id.brand_strip);


        }
    }

    public Order_Delivery_Adapter(Context context, List<Order_Delivery_Model> order_item_list, MyOrder_totalInterface listener) {
        this.order_item_list = order_item_list;
        this.contextm = context;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_order_adapter, parent, false);

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.mob_grid_txt, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Order_Delivery_Model items = order_item_list.get(position);

        holder.ditem_id1.setText(items.getDitem_id1());
        holder.product_named.setText(items.getProduct_named());
        holder.order_name_value.setText(items.getOrder_name_value());
        holder.balance_stock_value.setText(items.getBalance_stock_value());
        holder.order_qty_value.setText(items.getOrder_qty_value());

        if (position > 0 && !(order_item_list.get(position).getOrder_name_value().equalsIgnoreCase(order_item_list.get(position - 1).getOrder_name_value()))) {

            holder.brandnamerf.setVisibility(View.VISIBLE);

        } else {
            if (position != 0) {
                holder.brandnamerf.setVisibility(View.GONE);

            } else {
                holder.brandnamerf.setVisibility(View.VISIBLE);
            }


        }

        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(items.getOrder_qty_value())) {

            holder.delivered_qty_value.setEnabled(false);
            holder.delivered_qty_value.setClickable(false);
        }


//        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(items.getOrder_qty_value())) {
//
//            // holder.stock_addno1.setText(items.getStock_addno1());
//
//            Double stock_val = 0.0;
//            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(items.getOrder_qty_value()) )
//            {
//                stock_val = Double.valueOf(items.getOrder_qty_value());
//            }
//
//
//                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzero(items.getOrder_qty_value())) {
//
//                    holder.delivered_qty_value.setText(items.getOrder_qty_value());
//                   // Global_Data.order_text1.put(position,items.getOrder_qty_value());
//                }
//
//                holder.delivered_qty_value.setClickable(true);
//
//
//
//            }
//        else
//        {
//
//            holder.delivered_qty_value.setEnabled(false);
//
//        }


        holder.delivered_qty_value.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.delivered_qty_value.getText().toString())) {

                        int delivered_qty_value = Integer.parseInt(holder.delivered_qty_value.getText().toString().trim());
                        int order_qty_value = Integer.parseInt(order_item_list.get(position).getOrder_qty_value());
                        if (delivered_qty_value <= order_qty_value) {
                            int final_balance_value = order_qty_value - delivered_qty_value;
                            holder.dquantity_error.setVisibility(View.GONE);
                            Global_Data.Order_Delivery_hashmap.put(position + "&" + holder.ditem_id1.getText().toString(), s.toString() + "%" + holder.order_name_value.getText().toString());

                            holder.balance_stock_value.setText(String.valueOf(final_balance_value));
                            // order_item_list.get(position).setBalance_stock_value(String.valueOf(final_balance_value));
                        } else {
                            if (holder.delivered_qty_value.getText().toString().trim().equalsIgnoreCase("") || holder.delivered_qty_value.getText().toString().equalsIgnoreCase("0")) {
                                holder.dquantity_error.setVisibility(View.GONE);
                                holder.dquantity_error.setText("");
                            } else {
                                holder.dquantity_error.setVisibility(View.VISIBLE);
                                holder.dquantity_error.setText("Entered value should be less than or equal to order quantity.");
                            }

                            Global_Data.Order_Delivery_hashmap.put(position + "&" + holder.ditem_id1.getText().toString(), "");
                            holder.balance_stock_value.setText(order_item_list.get(position).getBalance_stock_value());
                            holder.delivered_qty_value.setText("");

                        }
                    } else {
                        // holder.dquantity_error.setVisibility(View.GONE);
                        // holder.dquantity_error.setText("");
                        Global_Data.Order_Delivery_hashmap.put(position + "&" + holder.ditem_id1.getText().toString(), "");
                        holder.balance_stock_value.setText(order_item_list.get(position).getBalance_stock_value());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

        });


    }

    public void setListContent(ArrayList<Order_Delivery_Model> order_item_list) {
        this.order_item_list = order_item_list;
        notifyItemRangeChanged(0, order_item_list.size());
    }

    @Override
    public int getItemCount() {
        return order_item_list.size();
    }

//    public List<RetItem> retrieveData()
//    {
//        return Global_Data._retData;
//    }

    static final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>();

    static {
        suffixes.put(1000L, "k");
        suffixes.put(100000L, "L");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "G");
        suffixes.put(1000000000000L, "T");
        suffixes.put(1000000000000000L, "P");
        suffixes.put(1000000000000000000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


}
