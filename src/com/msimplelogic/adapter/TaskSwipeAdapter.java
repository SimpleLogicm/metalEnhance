package com.msimplelogic.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.ActivityTask;
import com.msimplelogic.activities.kotlinFiles.ActivityTaskDetails;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.swipelistview.SwipeListView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifAnimationMetaData;
import pl.droidsonroids.gif.GifImageView;

public class TaskSwipeAdapter extends ArrayAdapter<HashMap<String, String>> {
    private List<Catalogue_model> catalogue_m;
    customButtonListener customListner;
    ViewHolder viewHolder;
    ArrayList<String> Amount_tpp = new ArrayList<String>();
    static final String TAG_Code = "code";
    static final String TAG_Activity_name = "activity_name";
    static final String TAG_From = "from";
    static final String TAG_To = "to";
    static final String TAG_Description = "description";
    static final String TAG_Location = "location";
    static final String TAG_Reminder = "reminder";
    static final String TAG_Type = "type";
    static final String TAG_Created_at = "created_at";

    ProgressDialog dialog;
    String final_response = "";
    String response_result = "";


    HashMap<String, String> getData = new HashMap<String, String>();
    private ArrayList<HashMap<String, String>> dataAray;
    HashMap<String, String> dataIthjem;
    DataBaseHelper dbvoc;
    Catalogue_model catalogue_mm = new Catalogue_model();

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, View v);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;

    //public PackageAdapter(Context context, ArrayList<HashMap<String, String>> dataItem1, List<Catalogue_model> catalogue_m) {
    public TaskSwipeAdapter(Context context, ArrayList<HashMap<String, String>> dataItem1) {
        super(context, R.layout.swipe_dumy, dataItem1);
        this.dataAray = dataItem1;
        this.context = context;
        //this.catalogue_m = catalogue_m;
    }


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
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.taskswipe_adapter, parent, false);
            holder = new ViewHolder();

            holder.task_idn = (TextView) convertView.findViewById(R.id.task_idn);
            holder.newll = (LinearLayout) convertView.findViewById(R.id.newll);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.example_row_tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.example_row_tv_description);
//            holder.tvPriece = (TextView) convertView.findViewById(R.id.example_row_tv_price);
//            holder.order_idn = (TextView) convertView.findViewById(R.id.order_idn);alarm_img
            holder.bAction1 = (ImageView) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (ImageView) convertView.findViewById(R.id.example_row_b_action_2);
            holder.alarmImage = (ImageView) convertView.findViewById(R.id.alarm_img);

            holder.gifview = convertView.findViewById(R.id.gifview);


            if (Global_Data.PlannerName.equalsIgnoreCase("Task")) {
                holder.newll.setVisibility(View.VISIBLE);
            } else {
                holder.newll.setVisibility(View.GONE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView) parent).recycle(convertView, position);

        getData = dataAray.get(position);

      //  holder.gifview.setVisibility(View.GONE);

//        if (getData.equals(0)) {
//            holder.gifview.setVisibility(View.VISIBLE);
//        } else {
//            holder.gifview.setVisibility(View.GONE);
//        }
        holder.tvTitle.setText(getData.get(TAG_Activity_name));
        holder.task_idn.setText(getData.get(TAG_Code));

        if (Global_Data.PlannerName.equals("Note")) {
            holder.tvDescription.setText("Last Updated At " + dataAray.get(position).get(TAG_Created_at));
        } else {

            holder.tvDescription.setText(dataAray.get(position).get(TAG_Description));
        }

//        holder.tvPriece.setText(getData.get(TAG_ITEMNAME));
//        holder.order_idn.setText(getData.get(TAG_ITEMNAME));

        if (Global_Data.PlannerName.equalsIgnoreCase("Task")) {
            holder.alarmImage.setVisibility(View.VISIBLE);
        } else {
            holder.alarmImage.setVisibility(View.GONE);
        }

        holder.bAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
                alertDialog.setTitle(context.getResources().getString(R.string.Warning));
                alertDialog.setMessage(context.getResources().getString(R.string.Product_Delete_dialog_message));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        getData = dataAray.get(position);
                        Log.d("DELETE ORDER ID", "DELETE ORDER ID" + getData.get(TAG_Code).toString());

                        // dbvoc = new DataBaseHelper(context);
                        //  dbvoc.getDeleteTableorder_byORDER_ID_return(getData.get(TAG_Code).toString());
                        deteleTask(getData.get(TAG_Code).toString(), position);


                    }
                });

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                alertDialog.show();
            }
        });

        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ActivityTaskDetails.class);
                if (Global_Data.PlannerName.equals("Task")) {
                    Global_Data.PlannerUpdate = "TaskUpdate";
                    intent.putExtra("code", dataAray.get(position).get(TAG_Code));
                    intent.putExtra("activity_name", dataAray.get(position).get(TAG_Activity_name));
                    intent.putExtra("from", dataAray.get(position).get(TAG_From));
                    intent.putExtra("to", dataAray.get(position).get(TAG_To));
                    intent.putExtra("description", dataAray.get(position).get(TAG_Description));
                    intent.putExtra("location", dataAray.get(position).get(TAG_Location));
                    intent.putExtra("reminder", dataAray.get(position).get(TAG_Reminder));
                    intent.putExtra("type", dataAray.get(position).get(TAG_Type));
                } else {
                    Global_Data.PlannerUpdate = "NotesUpdate";
                    intent.putExtra("code", dataAray.get(position).get(TAG_Code));
                    intent.putExtra("title", dataAray.get(position).get(TAG_Activity_name));
                    intent.putExtra("description", dataAray.get(position).get(TAG_Description));

                }
                context.startActivity(intent);
            }
        });


//        holder.bAction1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle, task_idn;
        TextView tvDescription;
        TextView tvPriece;
        LinearLayout newll;
        GifImageView gifview;
        ImageView bAction1, bAction2, alarmImage;


    }

    public void deteleTask(String code, int position) {

        dialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait....");
        dialog.setTitle("Dealer App");
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";


        try {
            if (Global_Data.PlannerName.equals("Task")) {
                service_url = service_url + "activity_planners/destroy_activity?email=" +
                        URLEncoder.encode(user_email, "UTF-8") + "&code=" + URLEncoder.encode(code, "UTF-8");
            } else {
                service_url = service_url + "notes/destroy_notes?email=" +
                        URLEncoder.encode(user_email, "UTF-8") + "&id=" + URLEncoder.encode(code, "UTF-8");
            }


            Log.i("service_url", "service_url: " + service_url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {

            StringRequest jsObjRequest = null;
            jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    try {
                        JSONObject responses = new JSONObject(final_response);
                        if (responses.has("result")) {
                            response_result = responses.getString("result");

                            if (!response_result.equalsIgnoreCase("User Not Found")) {
                                dataAray.remove(position);

                                if (dataAray.size() <= 0) {
                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Task_delete), "Yes");
                                    Intent ActivityTask = new Intent(context, ActivityTask.class);
                                    context.startActivity(ActivityTask);
                                } else {
                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Task_delete), "Yes");
                                    Intent ActivityTask = new Intent(context, ActivityTask.class);
                                    context.startActivity(ActivityTask);

                                }

//                                ArrayList<HashMap<String, String>>  objects =  new  ArrayList <HashMap<String, String>>();
//                                objects.addAll(dataAray);
//                                // update data in our adapter
//                                dataAray.clear();
//                                dataAray.addAll(objects);
                                // notifyDataSetChanged();

                            }

                            dialog.dismiss();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // dialog.dismiss();

                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    dialog.dismiss();
                                }
                            });

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                Global_Data.Custom_Toast(context,
                                        "Network Error", "");
                            } else if (error instanceof AuthFailureError) {

                                Global_Data.Custom_Toast(context,
                                        "Server AuthFailureError  Error", "");
                            } else if (error instanceof ServerError) {

                                Global_Data.Custom_Toast(context,
                                        "Server   Error", "");
                            } else if (error instanceof NetworkError) {

                                Global_Data.Custom_Toast(context,
                                        "Network   Error", "");
                            } else if (error instanceof ParseError) {

                                Global_Data.Custom_Toast(context,
                                        "ParseError   Error", "");
                            } else {

                                Global_Data.Custom_Toast(context, error.getMessage(), "");
                            }
                            //dialog.dismiss();
                            // finish();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            int socketTimeout = 300000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            // requestQueue.se
            //requestQueue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            requestQueue.getCache().clear();
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
