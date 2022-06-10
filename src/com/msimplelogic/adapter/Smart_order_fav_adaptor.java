package com.msimplelogic.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Smart_order_model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Smart_order_fav_adaptor extends RecyclerView.Adapter<Smart_order_fav_adaptor.MyViewHolder>{
    private Context context;
    Smart_order_adaptor.OnShareClickedListener mCallback;

    private List<Smart_order_model> array;
    private int lastCheckedPosition2 = -1;
    private int lastCheckedPosition = -1;
    ProgressDialog dialog;

    // dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

    //  ProgressDialog progress = new ProgressDialog(context);

    public void setOnShareClickedListener(Smart_order_adaptor.OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }
    public interface OnShareClickedListener {
        public void ShareClicked(String url);

    }

    public Smart_order_fav_adaptor(Context context, List<Smart_order_model> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public Smart_order_fav_adaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_smartorder_adaptor, parent, false);

        return new Smart_order_fav_adaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Smart_order_fav_adaptor.MyViewHolder holder, int position) {
//        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//        val date: Date = dateFormat.parse(array[position].date) //You will get date object relative to server/client timezone wherever it is parsed
//        val formatter: DateFormat = SimpleDateFormat("MMMM d, yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
//        val dateStr: String = formatter.format(date)
//
        holder.radio.setChecked(position == lastCheckedPosition);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        try {
            Date dtae = dateFormat.parse(array.get(position).getOrder_date());
            DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
            String dateStr = formatter.format(dtae);
            holder.order_date.setText(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.order_status.setText("In Cart");
        holder.amount.setText(array.get(position).getAmount());

        String s = Global_Data.image_check.get(position);
        if (s.equalsIgnoreCase("yes")) {
//            Global_Data.image_check.put(position, "no");
//            holder.order_favorite_flag.setText("no");
            holder.order_feb.setImageResource(R.drawable.fevstar);
            holder.order_favorite_flag.setText("yes");
        } else {
            holder.order_feb.setImageResource(R.drawable.unfavstar);
//            Global_Data.image_check.put(position, "yes");
//            holder.order_favorite_flag.setText("yes");
            holder.order_favorite_flag.setText("no");
        }
        // holder.radio.setChecked(position == lastCheckedPosition);


//        String s = Global_Data.image_check.get(position);
//        if (s.equalsIgnoreCase("yes")) {
//            holder.order_feb.setImageResource(R.drawable.fevstar);
//            holder.order_favorite_flag.setText("yes");
//        } else {
//            holder.order_feb.setImageResource(R.drawable.unfavstar);
//            holder.order_favorite_flag.setText("no");
//        }


        holder.order_feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // holder.order_feb.setImageResource(R.drawable.fevstar);

                String s = Global_Data.image_check.get(position);
                if (s.equalsIgnoreCase("yes")) {
                    Global_Data.image_check.put(position, "no");
                    holder.order_favorite_flag.setText("no");
                    holder.order_feb.setImageResource(R.drawable.unfavstar);
                    Global_Data.order_favorite_flag = "no";
//                    progress.setMessage(context.getResources().getString(R.string.Please_Wait));
//                    progress.setTitle(context.getResources().getString(R.string.app_name));
//                    progress.show();
//                    progress.setCancelable(false);
                    SaveDFunc(Global_Data.GLOvel_CUSTOMER_ID, array
                            .get(position).getId(), "false");
                    //  progress.dismiss();
                    notifyDataSetChanged();

                } else {
                    holder.order_feb.setImageResource(R.drawable.fevstar);
                    Global_Data.image_check.put(position, "yes");
                    holder.order_favorite_flag.setText("yes");
                    Global_Data.order_favorite_flag = "yes";
//                    progress.setMessage(context.getResources().getString(R.string.Please_Wait));
//                    progress.setTitle(context.getResources().getString(R.string.app_name));
//                    progress.show();
//                    progress.setCancelable(false);
                    SaveDFunc(Global_Data.GLOvel_CUSTOMER_ID, array
                            .get(position).getId(), "true");
                    //  progress.dismiss();
                    notifyDataSetChanged();
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastCheckedPosition2 = position;
                lastCheckedPosition = position;
                Global_Data.GLOvel_GORDER_ID = array.get(position).getId();
                //  Toast.makeText(context,""+Global_Data.GLOvel_GORDER_ID,Toast.LENGTH_SHORT);
                Global_Data.selected="yes";
                // Smart_Order.selcted();
                //   Global_Data.smart_order_adaptor= array.get(position).toString();
                Global_Data.smart_order_adaptor = (String.valueOf(holder.getAdapterPosition()));
//              )  Global_Data.Order_status = holder.order_status.getText().toString();
//                Global_Data.Order_date = holder.order_daten.getText().toString();
//                Global_Data.Order_time = holder.order_time.getText().toString();
//                Global_Data.Order_edate = holder.order_edate.getText().toString();
//                Global_Data.Order_Amount = holder.order_amounts.getText().toString();
                Global_Data.order_favorite_flag = holder.order_favorite_flag.getText().toString();
                notifyDataSetChanged();
            }
        });

        holder.radio.setOnClickListener(view -> {
            lastCheckedPosition2 = position;
            lastCheckedPosition = position;
            Global_Data.GLOvel_GORDER_ID = array.get(position).getId();
            Global_Data.smart_order_adaptor = (String.valueOf(holder.getAdapterPosition()));
            Global_Data.order_favorite_flag = holder.order_favorite_flag.getText().toString();
            notifyDataSetChanged();
        });

        if (position == lastCheckedPosition2) {
            holder.itemView.setBackgroundResource(R.drawable.rounded_corner);
            holder.radio.setChecked(true);

        } else {
            holder.itemView.setBackgroundResource(R.drawable.rounded_corner_white);
            holder.radio.setChecked(false);
        }

        if (lastCheckedPosition == -1) {
            holder.itemView.setBackgroundResource(R.drawable.rounded_corner_white);
            holder.radio.setChecked(false);
        }
    }

    public void detete(int smartOrderAdaptor) {
        array.remove(smartOrderAdaptor);

        notifyItemRemoved(smartOrderAdaptor);
        lastCheckedPosition2 = -1;
        lastCheckedPosition = -1;

        notifyDataSetChanged();


    }


    private void SaveDFunc(String GLOvel_CUSTOMER_ID, String ordernumber, String aFalse) {
        dialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        JSONObject product_valuenew = new JSONObject();


        SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = sp.getString("USER_EMAIL", null);

        try {
            product_valuenew.put("cust_code", GLOvel_CUSTOMER_ID);
            product_valuenew.put("order_id", ordernumber);
            product_valuenew.put("is_favourits", aFalse);
            product_valuenew.put("email", user_email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String domain = context.getResources().getString(R.string.service_domain);
        String url = domain + "orders/create_order_favourite";


        JsonObjectRequest jsObjRequest = null;
        try {


            Log.d("Server url", "Server url" + url);


            jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, product_valuenew, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);

                    Log.d("jV", "JV length" + response.length());
                    //JSONObject json = new JSONObject(new JSONTokener(response));
                    try {

                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }


                        if (response_result.equalsIgnoreCase("success")) {


                            //Toast.makeText(Customer_Feed.this, "Media Upload Successfully.", Toast.LENGTH_LONG).show();

                            String val = "";


//                            try {
//                                File file = new File(Global_Data.Default_video_Path);
//                                if (file.exists()) {
//
//                                    file.delete();
//                                    dbvoc.getDeleteMediaBYID(Global_Data.Default_video_Path);
//                                }
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }

                            ((Activity)context).runOnUiThread(new Runnable() {
                                public void run() {

                                    dialog.dismiss();
                                }
                            });

                            Global_Data.Custom_Toast(context, "Fav", "Yes");

//								Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

//                            Intent a = new Intent(Customer_Feed.this, Neworderoptions.class);
//                            startActivity(a);
//                            finish();


                        } else {

//                            dialog.dismiss();
//                            button1.setClickable(true);
//                            button1.setEnabled(true);
                            ((Activity)context).runOnUiThread(new Runnable() {
                                public void run() {

                                    dialog.dismiss();
                                }
                            });

                            Global_Data.Custom_Toast(context, response_result, "Yes");
//								Toast toast = Toast.makeText(Customer_Feed.this, response_result, Toast.LENGTH_SHORT);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
//                                    Intent a = new Intent(context,Order.class);
//                                    context.startActivity(a);

                            ((Activity)context).finish();
                            ((Activity)context).startActivity(((Activity) context).getIntent());



                        }

                        //  finish();
                        // }

                        // output.setText(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  dialog.dismiss();
                    }


                    // dialog.dismiss();
                    //   dialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
                    ((Activity)context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                        }
                    });
                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "Yes");
//                        Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();


                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                    } catch (JSONException e) {
                        //Handle a malformed json response
                    } catch (UnsupportedEncodingException errorn) {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // dialog.dismiss();
//                    button1.setClickable(true);
//                    button1.setEnabled(true);
                }
            });


            RequestQueue requestQueue = Volley.newRequestQueue(context);

            int socketTimeout = 2000000;//90 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            // requestQueue.se
            //requestQueue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            requestQueue.getCache().clear();
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            //  dialog.dismiss();
        }


    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView order_feb;
        private TextView order_status, order_date, amount, order_favorite_flag;
        private LinearLayout llmain, card_ll;
        private CardView card;
        private RadioButton radio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            order_feb = itemView.findViewById(R.id.order_feb);
            order_status = itemView.findViewById(R.id.order_status);
            order_date = itemView.findViewById(R.id.order_date);
            amount = itemView.findViewById(R.id.amount);
            order_favorite_flag = itemView.findViewById(R.id.order_favorite_flag);
            llmain = itemView.findViewById(R.id.llmain);
            card = itemView.findViewById(R.id.card);
            card_ll = itemView.findViewById(R.id.card_ll);
            radio = itemView.findViewById(R.id.radio);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });

        }
    }


    public void onClick(View v) {
        mCallback.ShareClicked("Share this text.");
    }

}
