package com.msimplelogic.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msimplelogic.activities.AppLocationManager;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.LoginDataBaseAdapter;
import com.msimplelogic.activities.NewOrderActivity;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.PreviewOrderSwipeActivity;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.SchemeDialog;
import com.msimplelogic.activities.SwipCatelauge;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.model.OnlineSchemeModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.Catalogue_slider_caller;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OnlineSchemeAdapter extends RecyclerView.Adapter<OnlineSchemeAdapter.MyViewHolder> {
    TextView edit_value;
    DataBaseHelper dbvoc;
    //Button vdialogSave;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private Catalogue_slider_caller listener;
    private Activity _activity;
    String image_url = "";
    Dialog openDialog;
    Button dialogSave;
    int p_position = 0;
    OnlineSchemeModel image;
    private List<OnlineSchemeModel> catalogue_m;
    private Context mContext;
    customButtonListener customListner;
    String q_check = "";
    String str;
    //DataBaseHelper dbvoc;
    OnlineSchemeModel catalogue_mm;
    String schemeName, schemeType,schemeAmount,schemeDesc,schemeBuyQty,schemeGetQty;
    String price_str;
    private ArrayList<String> p_id = new ArrayList<String>();
    private ArrayList<String> p_name = new ArrayList<String>();
    private ArrayList<String> p_mrp = new ArrayList<String>();
    private ArrayList<String> p_rp = new ArrayList<String>();
    private ArrayList<String> p_q = new ArrayList<String>();
    private ArrayList<String> p_price = new ArrayList<String>();
    private ArrayList<String> p_remarks = new ArrayList<String>();
    private ArrayList<String> scheme_id = new ArrayList<String>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, plus_btn, minus_btn;
        public TextView t_title, mrpv, rpv, cat_pid, text_rp, totalprice, vimg_url, rp_catalogue, mrp_catalogue, discount_price,topSellingSchemeIcon;
        public EditText edit_value, edit_comment, edit_detail1, edit_detail2, edit_detail3, edit_detail4, edit_detail5;
        public ImageView schemeBtn1;
        public TextView schemeBtn;
        String schemeId, schemeName, schemeType,schemeAmount,schemeDesc,schemeBuyQty,schemeGetQty,schemeMinQty;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            t_title = (TextView) view.findViewById(R.id.t_title);
            mrpv = (TextView) view.findViewById(R.id.mrpv);
            rpv = (TextView) view.findViewById(R.id.rpv);
            // text_rp = (TextView) view.findViewById(R.id.text_rp);
            cat_pid = (TextView) view.findViewById(R.id.cat_pid);
            plus_btn = (ImageView) view.findViewById(R.id.plus_btn);
            minus_btn = (ImageView) view.findViewById(R.id.minus_btn);
            edit_value = (EditText) view.findViewById(R.id.edit_value);
            edit_comment = (EditText) view.findViewById(R.id.edit_comment);
            edit_detail1 = (EditText) view.findViewById(R.id.edit_detail1);
            edit_detail2 = (EditText) view.findViewById(R.id.edit_detail2);
            edit_detail3 = (EditText) view.findViewById(R.id.edit_detail3);
            edit_detail4 = (EditText) view.findViewById(R.id.edit_detail4);
            edit_detail5 = (EditText) view.findViewById(R.id.edit_detail5);
            totalprice = (TextView) view.findViewById(R.id.totalprice);
            discount_price = (TextView) view.findViewById(R.id.discount_price);
            vimg_url = (TextView) view.findViewById(R.id.vimg_url);
            rp_catalogue = (TextView) view.findViewById(R.id.rp_catalogue);
            mrp_catalogue = (TextView) view.findViewById(R.id.mrp_catalogue);
            schemeBtn1 = (ImageView) view.findViewById(R.id.scheme_btn1);
            schemeBtn = (TextView) view.findViewById(R.id.scheme_btn);
            topSellingSchemeIcon = (TextView) view.findViewById(R.id.top_sellingscheme_icon);
        }
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public OnlineSchemeAdapter(Activity activity, Context context, List<OnlineSchemeModel> catalogue_m, Catalogue_slider_caller listener) {
        this._activity = activity;
        mContext = context;
        this.catalogue_m = catalogue_m;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_viewdeal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        catalogue_mm = catalogue_m.get(position);

        holder.schemeId = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_id());
        holder.schemeName =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_name());
        holder.schemeType =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_type());
        holder.schemeAmount =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_amount());
        holder.schemeDesc =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_description());
        holder.schemeBuyQty =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_buy_qty());
        holder.schemeGetQty =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_get_qty());
        holder.schemeMinQty =Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_min_qty());

        holder.t_title.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_name()));
        holder.mrpv.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck("₹"+catalogue_mm.getItem_mrp()));
        holder.rpv.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck("₹"+catalogue_mm.getItem_rp()));
        holder.cat_pid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_number()));
        holder.edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity()));
        holder.edit_comment.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_remarks()));
        holder.edit_detail1.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail1()));
        holder.edit_detail2.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail2()));
        holder.edit_detail3.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail3()));
        holder.edit_detail4.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail4()));
        holder.edit_detail5.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail5()));
        holder.totalprice.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck("₹"+catalogue_mm.getItem_amount()));

        // for label RP change
        SharedPreferences spf1 = mContext.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        String order_product_detail2 = spf1.getString("order_product_detail2", "");
        String order_product_detail3 = spf1.getString("order_product_detail3", "");
        String order_product_detail4 = spf1.getString("order_product_detail4", "");
        String order_product_detail5 = spf1.getString("order_product_detail5", "");
        String order_product_detail6 = spf1.getString("order_product_detail6", "");

//        price_str=spf1.getString("var_total_price", "");

        if(catalogue_mm.getScheme_topsellingproduct().equalsIgnoreCase("true"))
        {
            holder.topSellingSchemeIcon.setVisibility(View.VISIBLE);
        }else{
            holder.topSellingSchemeIcon.setVisibility(View.GONE);
        }

        if (rpstr.length() > 0) {
            holder.rp_catalogue.setText(rpstr + " : ");
        } else {
            holder.rp_catalogue.setText(mContext.getResources().getString(R.string.RP));
        }

        if (mrpstr.length() > 0) {
            holder.mrp_catalogue.setText(mrpstr + " : ");
        } else {
            holder.mrp_catalogue.setText(mContext.getResources().getString(R.string.MRP));
        }

        if (order_product_detail2.length() > 0) {
            holder.edit_detail1.setHint(order_product_detail2);
        }

        if (order_product_detail3.length() > 0) {
            holder.edit_detail2.setHint(order_product_detail3);
        }

        if (order_product_detail4.length() > 0) {
            holder.edit_detail3.setHint(order_product_detail4);
        }

        if (order_product_detail5.length() > 0) {
            holder.edit_detail4.setHint(order_product_detail5);
        }

        if (order_product_detail6.length() > 0) {
            holder.edit_detail5.setHint(order_product_detail6);
        }

        String aa= catalogue_mm.getScheme_type();

        if(aa.equalsIgnoreCase("") || aa.equalsIgnoreCase(null) || aa.equalsIgnoreCase("null"))
        {
            holder.schemeBtn.setVisibility(View.GONE);
        }else{
            holder.schemeBtn.setVisibility(View.VISIBLE);
        }

//        if(str.length()>0)
//        {
//            holder.edit_value.setText(str);
//        }else{
//                   }

        // holder.t_title.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_name()));
        try {
            String s = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_image_url());
            if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {
                Glide.with(mContext).load(R.drawable.img_not_found)
                        //.crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.thumbnail);
                image_url = "not found";
                holder.vimg_url.setText("not found");
            } else {
                Glide.with(mContext).load(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_image_url()))
                        .thumbnail(Glide.with(mContext).load("file:///android_asset/loading.gif"))
                        //.crossFade()
                        .error(R.drawable.img_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.thumbnail);
                image_url = catalogue_mm.getItem_image_url();
                holder.vimg_url.setText(catalogue_mm.getItem_image_url().trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Glide.with(mContext).load(R.drawable.img_not_found)
                    //.crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.thumbnail);

            image_url = "not found";
            holder.vimg_url.setText("not found");

        }

        holder.plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                if (holder.edit_value.getText().toString().equalsIgnoreCase("")) {
                    holder.edit_value.setText(String.valueOf(1));
                } else {
                    int s = Integer.parseInt(holder.edit_value.getText().toString()) + 1;
                    if (s <= 9999) {
                        holder.edit_value.setText(String.valueOf(Integer.parseInt(holder.edit_value.getText().toString()) + 1));
                    }
                }
            }
        });

        holder.minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                if (!(holder.edit_value.getText().toString().equalsIgnoreCase("")) && !(Integer.parseInt(holder.edit_value.getText().toString()) <= 0)) {
                    holder.edit_value.setText(String.valueOf(Integer.parseInt(holder.edit_value.getText().toString()) - 1));
                }
            }
        });

        //String scheme_type = extras.getString("SCHEME_TYPE", null);
        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(holder.schemeType)) {
            if(holder.schemeType.equalsIgnoreCase("quantity_scheme"))
            {
                holder.schemeBtn.setText("Buy "+holder.schemeBuyQty+" Get "+holder.schemeGetQty);
               // schemeHeader.setText("Buy "+extras.getString("SCHEME_BUYQTY", null) +" Get "+extras.getString("SCHEME_GETQTY", null)+" Free");
            }else if(holder.schemeType.equalsIgnoreCase("value_scheme")){
                holder.schemeBtn.setText("₹"+holder.schemeAmount+" \n off ");
                //schemeHeader.setText("Value Scheme");
            }else if(holder.schemeType.equalsIgnoreCase("discount_scheme")){
                holder.schemeBtn.setText(holder.schemeAmount+"% \n off ");
                //schemeHeader.setText("Discount Scheme");
            }
        } else {
            holder.schemeBtn.setVisibility(View.INVISIBLE);
        }

        holder.schemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.schemeName.equalsIgnoreCase("") || !holder.schemeName.equalsIgnoreCase("null"))
                {
                    Intent m = new Intent(mContext, SchemeDialog.class);
                    m.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    m.putExtra("SCHEME_NAME", holder.schemeName);
                    m.putExtra("SCHEME_TYPE", holder.schemeType);
                    m.putExtra("SCHEME_AMOUNT", holder.schemeAmount);
                    m.putExtra("SCHEME_DESCRIPTION", holder.schemeDesc);
                    m.putExtra("SCHEME_BUYQTY", holder.schemeBuyQty);
                    m.putExtra("SCHEME_GETQTY", holder.schemeGetQty);
                    m.putExtra("SCHEME_MINQTY", holder.schemeMinQty);
                    mContext.startActivity(m);
                }else {

                    Global_Data.Custom_Toast(mContext, "There is no scheme available","yes");
                }
            }
        });

        holder.edit_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                HashMap<String, String> edit = new HashMap<>();

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString()) && Integer.parseInt(String.valueOf(s)) > 0) {
                    // edit.put("string", s.toString());
                    try {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                            Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                            Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                            Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                            Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                            Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                            Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());
                        }

//                        if(price_str.length()>0)
//                        {
//                            holder.totalprice.setText(price_str+" : " + String.valueOf(value));
//                        }else{
//                                 holder.totalprice.setText("PRICE : " + String.valueOf(value));
//                             }

                        Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());


                        holder.totalprice.setText(mContext.getResources().getString(R.string.PRICE) + String.valueOf(value));

//                        Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

                        if(holder.schemeType.equalsIgnoreCase("value_scheme"))
                        {
//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(holder.schemeAmount)*Double.valueOf(holder.edit_value.getText().toString()))));
//                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(holder.schemeAmount)*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());

                            String minSchemeQty=(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(holder.schemeMinQty));

                            if(minSchemeQty.length()>0)
                            {
                                if(Double.valueOf(holder.edit_value.getText().toString())>=Double.valueOf(minSchemeQty))
                                {
                                    holder.discount_price.setVisibility(View.VISIBLE);
                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(holder.schemeAmount))));
                                    //Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(holder.schemeAmount))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                }else{
                                    holder.discount_price.setVisibility(View.GONE);
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());
                                }

                            }else{
                                holder.discount_price.setVisibility(View.VISIBLE);
                                holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(holder.schemeAmount)*Double.valueOf(holder.edit_value.getText().toString()))));
                                Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(holder.schemeAmount)*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());
                            }


                        }else if(holder.schemeType.equalsIgnoreCase("discount_scheme"))
                        {
//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
//                            double res = (value-((value / 100.0f) * (Double.valueOf(holder.schemeAmount))));
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
//                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());

                            String minSchemeQty=(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(holder.schemeMinQty));

                            if(minSchemeQty.length()>0)
                            {
                                if(Double.valueOf(holder.edit_value.getText().toString())>=Double.valueOf(minSchemeQty))
                                {
                                    holder.discount_price.setVisibility(View.VISIBLE);
                                    //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                    double res = (value-((value / 100.0f) * (Double.valueOf(holder.schemeAmount))));
                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                }else{
                                    holder.discount_price.setVisibility(View.GONE);
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());
                                }

                            }else{
                                holder.discount_price.setVisibility(View.VISIBLE);
                                //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                double res = (value-((value / 100.0f) * (Double.valueOf(holder.schemeAmount))));
                                holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());
                            }

                        }else if(holder.schemeType.equalsIgnoreCase("quantity_scheme"))
                        {

//                         Toast.makeText(mContext, ""+(((Double.valueOf(catalogue_mm.getScheme_buy_qty()))+(Double.valueOf(catalogue_mm.getScheme_get_qty())))*Double.valueOf(holder.edit_value.getText().toString())), Toast.LENGTH_SHORT).show();
                            double aaa =(Double.valueOf(holder.edit_value.getText().toString()) / Double.valueOf(holder.schemeBuyQty)) + Double.valueOf(holder.edit_value.getText().toString());
                            int y=(int)aaa;
                            // Toast.makeText(mContext, ""+y, Toast.LENGTH_SHORT).show();

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), y + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());

//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
                        }else{
                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + holder.schemeId+ "url" + holder.vimg_url.getText().toString());

                        }

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } else {
                    holder.totalprice.setText("");
                    holder.discount_price.setVisibility(View.GONE);
                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

        holder.edit_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                    Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            // holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

        holder.edit_detail1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            // holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

        holder.edit_detail2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                    Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            // holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

        holder.edit_detail3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                    Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            // holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

        holder.edit_detail4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                    Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            // holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

        holder.edit_detail5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                    Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail5.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            // holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail5.put(position + "&" + holder.cat_pid.getText().toString(), "");
                }
            }
        });

//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog openDialog = new Dialog(mContext);
//                openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                openDialog.setCancelable(false);
//                openDialog.setContentView(R.layout.catalogue_dialog);
//
//                //                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
////                ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
//                ImageView dialogClose = (ImageView)openDialog.findViewById(R.id.close_btn);
//
//                dialogClose.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        openDialog.dismiss();
//                    }
//                });
//
//                openDialog.show();
//            }
//        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // listener.slideCall(position);
//                dbvoc = new DataBaseHelper(mContext);
//
//                List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id,holder.cat_pid.getText().toString());
//                if (contactsn.size() > 0) {
//                    for (Local_Data cn : contactsn) {
//                        catalogue_mm.setItem_quantity(cn.get_delivery_product_order_quantity());
//                        catalogue_mm.setItem_amount("PRICE : " + cn.getAmount());
//
//                    }
//                } else {
//                    catalogue_mm.setItem_quantity("");
//                    catalogue_mm.setItem_amount("");
//                }
//
//                catalogue_m.set(position,catalogue_mm);

//                Intent goToNewOrderActivity = new Intent(mContext, SwipCatelauge.class);
//                goToNewOrderActivity.putExtra("position", position);
////                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
//                goToNewOrderActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(goToNewOrderActivity);

                openDialog = new Dialog(v.getContext());
                openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                openDialog.setCancelable(false);
                openDialog.setContentView(R.layout.catalogue_dialog);

                final TextView dialogText = (TextView) openDialog.findViewById(R.id.dialog_text);
                // ImageView dialogImage = (ImageView) openDialog.findViewById(R.id.dialog_img);
                ImageView dialogClose = (ImageView) openDialog.findViewById(R.id.close_btn);
                ImageView dialogPlus = (ImageView) openDialog.findViewById(R.id.plus_dialog);
                ImageView dialogMinus = (ImageView) openDialog.findViewById(R.id.minus_dialog);
                final EditText dialogEdit = (EditText) openDialog.findViewById(R.id.editval_dialog);
                final TextView dialogMrp = (TextView) openDialog.findViewById(R.id.mrp_dialog);
                final TextView dialogRp = (TextView) openDialog.findViewById(R.id.rp_dialog);
                final TextView dialogPid = (TextView) openDialog.findViewById(R.id.cat_pid_dialog);
                //final TextView dialogPrdname = (TextView) openDialog.findViewById(R.id.prodname_doalog);
                final TextView dialogPrdDesc = (TextView) openDialog.findViewById(R.id.proddesc_doalog);

                dialogSave = (Button) openDialog.findViewById(R.id.online_catalog_dialog_save);

                final TextView ediscountprice_dialog = (TextView) openDialog.findViewById(R.id.discount_price_dialog);
                final TextView eprice_dialog = (TextView) openDialog.findViewById(R.id.price_dialog);
                final TextView erp_dialog = (TextView) openDialog.findViewById(R.id.erp_dialog);
                final TextView emrp_dialog = (TextView) openDialog.findViewById(R.id.emrp_dialog);
                final TextView minqty_dialog = (TextView) openDialog.findViewById(R.id.minqty_dialog);
                final TextView pkgqty_dialog = (TextView) openDialog.findViewById(R.id.pkgqty_dialog);
                final TextView scheme_dialog = (TextView) openDialog.findViewById(R.id.scheme_dialog);
                final EditText remark_dialog = (EditText) openDialog.findViewById(R.id.remark_dialog);

                final EditText edit_detail1 = (EditText) openDialog.findViewById(R.id.dialog_detail1);
                final EditText edit_detail2 = (EditText) openDialog.findViewById(R.id.dialog_detail2);
                final EditText edit_detail3 = (EditText) openDialog.findViewById(R.id.dialog_detail3);
                final EditText edit_detail4 = (EditText) openDialog.findViewById(R.id.dialog_detail4);
                final EditText edit_detail5 = (EditText) openDialog.findViewById(R.id.dialog_detail5);

                dialogPrdDesc.setMovementMethod(new ScrollingMovementMethod());

                image = catalogue_m.get(position);

                p_position = position;

                dialogText.setText(image.getItem_name());


                // eprice_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_amount()));

                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getScheme_name()).length()>0 && !image.getScheme_type().equalsIgnoreCase("quantity_scheme"))
                {
                    if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_quantity()).length()>0)
                    {
                        ediscountprice_dialog.setVisibility(View.VISIBLE);
                        ediscountprice_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(_activity.getResources().getString(R.string.DISCOUNT_PRICE)+image.getItem_amount()));

                        Double value1 = (Double.valueOf(image.getItem_quantity()) * Double.valueOf(image.getItem_mrp()));
                        eprice_dialog.setText(_activity.getResources().getString(R.string.PRICE) + String.valueOf(value1));
                    }
                }else{
                    eprice_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_amount()));
                }

                remark_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_remarks()));

                edit_detail1.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail1()));
                edit_detail2.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail2()));
                edit_detail3.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail3()));
                edit_detail4.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail4()));
                edit_detail5.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail5()));

//                minqty_dialog.setText(_activity.getResources().getString(R.string.OMIN_QTY) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_min_qty()));
//
//                pkgqty_dialog.setText(_activity.getResources().getString(R.string.OPkg_Qty) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_pkg_qty1()));

                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getScheme_name()).length()>0)
                {
                    scheme_dialog.setText(_activity.getResources().getString(R.string.OScheme) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getScheme_name()));
                    scheme_dialog.setVisibility(View.VISIBLE);
                }


                // for label RP change
                SharedPreferences spf1 = _activity.getSharedPreferences("SimpleLogic", 0);
                String rpstr = spf1.getString("var_rp", "");
                String mrpstr = spf1.getString("var_mrp", "");
                String order_product_detail2 = spf1.getString("order_product_detail2", "");
                String order_product_detail3 = spf1.getString("order_product_detail3", "");
                String order_product_detail4 = spf1.getString("order_product_detail4", "");
                String order_product_detail5 = spf1.getString("order_product_detail5", "");
                String order_product_detail6 = spf1.getString("order_product_detail6", "");

                if (order_product_detail2.length() > 0) {
                    edit_detail1.setHint(order_product_detail2);
                }

                if (order_product_detail3.length() > 0) {
                    edit_detail2.setHint(order_product_detail3);
                }

                if (order_product_detail4.length() > 0) {
                    edit_detail3.setHint(order_product_detail4);
                }

                if (order_product_detail5.length() > 0) {
                    edit_detail4.setHint(order_product_detail5);
                }

                if (order_product_detail6.length() > 0) {
                    edit_detail5.setHint(order_product_detail6);
                }
//        price_str=spf1.getString("var_total_price", "");

                emrp_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_mrp()));
                erp_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));
                if (rpstr.length() > 0) {
                    dialogRp.setText(rpstr + " : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));
                } else {
                    dialogRp.setText(_activity.getResources().getString(R.string.SRP) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));
                }

                if (mrpstr.length() > 0) {
                    dialogMrp.setText(mrpstr + " : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_mrp()));
                } else {
                    dialogMrp.setText(_activity.getResources().getString(R.string.SMRP) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_mrp()));
                }

                dialogPid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_number()));
                dialogEdit.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_quantity()));
                //dialogPrdname.setText(_activity.getResources().getString(R.string.SPRODUCT) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_name()));

//                String itemDesc=Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_description());
//                if(!itemDesc.equalsIgnoreCase("") && !itemDesc.equalsIgnoreCase(null))
//                {
//                    dialogPrdDesc.setVisibility(View.VISIBLE);
//                    dialogPrdDesc.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_description()));
//
//                }else{
//                    dialogPrdDesc.setVisibility(View.GONE);
//                }

                if (rpstr.length() > 0) {
                    erp_dialog.setText(rpstr + " : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));
                } else {
                    erp_dialog.setText(_activity.getResources().getString(R.string.SRP) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));

                }

                if (mrpstr.length() > 0) {
                    dialogMrp.setText(mrpstr + " : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_mrp()));
                } else {
                    dialogMrp.setText(_activity.getResources().getString(R.string.SMRP) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_mrp()));
                }

//                try {
//                    String s = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_image_url());
//                    if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {
//
//                        vimg_url.setText("not found");
//                    } else {
//
//                        vimg_url.setText(image.getItem_image_url().trim());
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//
//                    vimg_url.setText("not found");
//
//                }

//                try {
//                    if (image.getItem_image_url().equalsIgnoreCase("") || image.getItem_image_url().equalsIgnoreCase("null")) {
//                        Glide.with(_activity).load(R.drawable.img_not_found).crossFade()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(dialogImage);
//                    } else {
//                        Glide.with(mContext).load(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_image_url()))
//                                .thumbnail(Glide.with(mContext).load("file:///android_asset/loading.gif"))
//                                .error(R.drawable.img_not_found)
//                                .crossFade()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(dialogImage);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    Glide.with(mContext).load(R.drawable.img_not_found).crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(dialogImage);
//                }


                dialogPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                        if (dialogEdit.getText().toString().equalsIgnoreCase("")) {
                            dialogEdit.setText(String.valueOf(1));
                        } else {
                            int s = Integer.parseInt(dialogEdit.getText().toString()) + 1;
                            if (s <= 9999) {
                                dialogEdit.setText(String.valueOf(Integer.parseInt(dialogEdit.getText().toString()) + 1));
                            }
                        }
                    }
                });

                dialogMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                        if (!(dialogEdit.getText().toString().equalsIgnoreCase("")) && !(Integer.parseInt(dialogEdit.getText().toString()) <= 0)) {
                            dialogEdit.setText(String.valueOf(Integer.parseInt(dialogEdit.getText().toString()) - 1));
                        }
                    }
                });


                dialogEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        HashMap<String, String> edit = new HashMap<>();

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {

                            try {

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString())) {
                                    Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString());
                                }
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString().toString())) {
                                    Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString().toString());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                                    Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                                    Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                                    Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                                    Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());
                                }

                                Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                eprice_dialog.setText(_activity.getResources().getString(R.string.PRICE) + String.valueOf(value));
                                //ediscountprice_dialog.setText(_activity.getResources().getString(R.string.PRICE) + String.valueOf(value));
                                //Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());

                                if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getScheme_type())).equalsIgnoreCase("value_scheme"))
                                {
                                    String minSchemeQty=(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_min_qty()));

                                    if(minSchemeQty.length()>0)
                                    {
                                        if(Double.valueOf(dialogEdit.getText().toString())>=Double.valueOf(minSchemeQty))
                                        {
                                            ediscountprice_dialog.setVisibility(View.VISIBLE);
                                            ediscountprice_dialog.setText(_activity.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(image.getScheme_amount()))));
                                            Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                        }else{
                                            ediscountprice_dialog.setVisibility(View.GONE);
                                            Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");
                                        }

                                    }else{
                                        ediscountprice_dialog.setVisibility(View.VISIBLE);
                                        ediscountprice_dialog.setText(_activity.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(image.getScheme_amount())*Double.valueOf(dialogEdit.getText().toString()))));
                                        Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(dialogEdit.getText().toString()))) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");
                                    }

                                }else if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getScheme_type())).equalsIgnoreCase("discount_scheme"))
                                {
//                                    ediscountprice_dialog.setVisibility(View.VISIBLE);
//                                    //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
//                                    double res = (value-((value / 100.0f) * (Double.valueOf(image.getScheme_amount()))));
//                                    ediscountprice_dialog.setText(_activity.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
//                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + vimg_url.getText().toString());




                                    String minSchemeQty=(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getScheme_min_qty()));

                                    if(minSchemeQty.length()>0)
                                    {
                                        if(Double.valueOf(dialogEdit.getText().toString())>=Double.valueOf(minSchemeQty))
                                        {
                                            ediscountprice_dialog.setVisibility(View.VISIBLE);
                                            //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                            double res = (value-((value / 100.0f) * (Double.valueOf(image.getScheme_amount()))));
                                            ediscountprice_dialog.setText(_activity.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                            Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                        }else{
                                            ediscountprice_dialog.setVisibility(View.GONE);
                                            Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");
                                        }

                                    }else{
                                        ediscountprice_dialog.setVisibility(View.VISIBLE);
                                        //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                        double res = (value-((value / 100.0f) * (Double.valueOf(image.getScheme_amount()))));
                                        ediscountprice_dialog.setText(_activity.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                        Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");
                                    }

                                }else if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getScheme_type())).equalsIgnoreCase("quantity_scheme"))
                                {

//                         Toast.makeText(mContext, ""+(((Double.valueOf(catalogue_mm.getScheme_buy_qty()))+(Double.valueOf(catalogue_mm.getScheme_get_qty())))*Double.valueOf(holder.edit_value.getText().toString())), Toast.LENGTH_SHORT).show();
                                    double aaa =(Double.valueOf(dialogEdit.getText().toString()) / Double.valueOf(image.getScheme_buy_qty())) + Double.valueOf(dialogEdit.getText().toString());
                                    int y=(int)aaa;
                                    //Toast.makeText(_activity, ""+y, Toast.LENGTH_SHORT).show();

                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), y + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");

//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
                                }else{
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + "");

                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                        } else {
                            eprice_dialog.setText("");
                            ediscountprice_dialog.setText("");
                            ediscountprice_dialog.setVisibility(View.GONE);
                            Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), "");
                        }
                    }
                });

                remark_dialog.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString())) {
                            Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString());

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString().toString())) {
                                Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                                Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                                Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                                Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                                Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {
                                // edit.put("string", s.toString());

                                try {
                                    Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                    // eprice_dialog.setText("PRICE : " + String.valueOf(value));
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + "");


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        } else {
                            Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), "");
                        }

                    }
                });

                edit_detail1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                            Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString().toString())) {
                                Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                                Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                                Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                                Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString())) {
                                Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {
                                // edit.put("string", s.toString());

                                try {
                                    Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                    // eprice_dialog.setText("PRICE : " + String.valueOf(value));
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + "");


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            }
                        } else {
                            Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), "");
                        }
                    }
                });

                edit_detail2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                            Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString().toString())) {
                                Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                                Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                                Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                                Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString())) {
                                Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {
                                // edit.put("string", s.toString());

                                try {
                                    Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                    // eprice_dialog.setText("PRICE : " + String.valueOf(value));
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + "");


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            }
                        } else {
                            Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), "");
                        }
                    }
                });

                edit_detail3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                            Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString().toString())) {
                                Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                                Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                                Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                                Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString())) {
                                Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {
                                // edit.put("string", s.toString());

                                try {
                                    Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                    // eprice_dialog.setText("PRICE : " + String.valueOf(value));
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + "");


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            }
                        } else {
                            Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), "");
                        }
                    }
                });

                edit_detail4.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                            Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString().toString())) {
                                Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                                Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                                Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                                Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString())) {
                                Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {
                                // edit.put("string", s.toString());

                                try {
                                    Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                    // eprice_dialog.setText("PRICE : " + String.valueOf(value));
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + "");


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            }
                        } else {
                            Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), "");
                        }
                    }
                });

                edit_detail5.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail5.getText().toString())) {
                            Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), edit_detail5.getText().toString());

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(remark_dialog.getText().toString().toString())) {
                                Global_Data.Orderproduct_remarks.put(position + "&" + dialogPid.getText().toString(), remark_dialog.getText().toString().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail1.getText().toString())) {
                                Global_Data.Orderproduct_detail1.put(position + "&" + dialogPid.getText().toString(), edit_detail1.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail3.getText().toString())) {
                                Global_Data.Orderproduct_detail3.put(position + "&" + dialogPid.getText().toString(), edit_detail3.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail4.getText().toString())) {
                                Global_Data.Orderproduct_detail4.put(position + "&" + dialogPid.getText().toString(), edit_detail4.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(edit_detail2.getText().toString())) {
                                Global_Data.Orderproduct_detail2.put(position + "&" + dialogPid.getText().toString(), edit_detail2.getText().toString());
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(dialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(emrp_dialog.getText().toString())) {
                                // edit.put("string", s.toString());

                                try {
                                    Double value = Double.valueOf(dialogEdit.getText().toString()) * Double.valueOf(emrp_dialog.getText().toString());
                                    // eprice_dialog.setText("PRICE : " + String.valueOf(value));
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + "");


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            }
                        } else {
                            Global_Data.Orderproduct_detail5.put(position + "&" + dialogPid.getText().toString(), "");
                        }
                    }
                });

                dialogSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        p_id.clear();
                        //p_url.clear();
                        p_name.clear();
                        scheme_id.clear();
                        p_mrp.clear();
                        p_q.clear();
                        p_price.clear();
                        p_remarks.clear();
//                        p_detail1.clear();
//                        p_detail2.clear();
//                        p_detail3.clear();
//                        p_detail4.clear();
//                        p_detail5.clear();
                        p_rp.clear();

                        q_check = "";

//                    dialog = new ProgressDialog(OnlineCatalogue.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    dialog.setMessage("Please wait Customer Loading....");
//                    dialog.setTitle("Metal App");
//                    dialog.setCancelable(false);
//                    dialog.show();

                        new OnlineSchemeAdapter.Varientsave().execute();
                    }
                });

                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Global_Data.Order_hashmap.size() > 0) {

                            AlertDialog alertDialog = new AlertDialog.Builder(_activity).create(); //Read Update
                            alertDialog.setTitle(_activity.getResources().getString(R.string.Warning));
                            alertDialog.setMessage(_activity.getResources().getString(R.string.ITEM_DISCART_DIALOG_MESSAGE));
                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, _activity.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    p_id.clear();
                                    //p_url.clear();
                                    p_name.clear();
                                    scheme_id.clear();
                                    p_mrp.clear();
                                    p_q.clear();
                                    p_price.clear();
                                    p_remarks.clear();
//                                    p_detail1.clear();
//                                    p_detail2.clear();
//                                    p_detail3.clear();
//                                    p_detail4.clear();
//                                    p_detail5.clear();
                                    p_rp.clear();
                                    Global_Data.Order_hashmap.clear();
                                    Global_Data.Orderproduct_remarks.clear();
                                    Global_Data.Orderproduct_detail1.clear();
                                    Global_Data.Orderproduct_detail2.clear();
                                    Global_Data.Orderproduct_detail3.clear();
                                    Global_Data.Orderproduct_detail4.clear();
                                    Global_Data.Orderproduct_detail5.clear();
                                    openDialog.dismiss();
                                }
                            });

                            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, _activity.getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });


                            alertDialog.show();


                        } else {

                            p_id.clear();
                            //p_url.clear();
                            p_name.clear();
                            scheme_id.clear();
                            p_mrp.clear();
                            p_q.clear();
                            p_price.clear();
                            p_remarks.clear();
//                            p_detail1.clear();
//                            p_detail2.clear();
//                            p_detail3.clear();
//                            p_detail4.clear();
//                            p_detail5.clear();
                            p_rp.clear();
                            Global_Data.Order_hashmap.clear();
                            Global_Data.Orderproduct_remarks.clear();
                            Global_Data.Orderproduct_detail1.clear();
                            Global_Data.Orderproduct_detail2.clear();
                            Global_Data.Orderproduct_detail3.clear();
                            Global_Data.Orderproduct_detail4.clear();
                            Global_Data.Orderproduct_detail5.clear();
                            openDialog.dismiss();
                        }
                    }
                });

                openDialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return catalogue_m.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


//    public void SchemeDialog(Context context)
//    {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
////                LayoutInflater inflater = mContext.getActivity().getLayoutInflater();
////                LayoutInflater inflater = mContext.getApplicationContext().getLayoutInflater();
//        //LayoutInflater inflater = LayoutInflater.from(mContext);
//
//
//        WindowManager wm = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
//        //LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        //View dialogView = inflater.inflate(R.layout.scheme_dialog, null);
//
//        View dialogView = inflater.inflate(R.layout.scheme_dialog, null);
//
//        dialogBuilder.setView(dialogView);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_TOAST,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        wm.addView(dialogView, params);
//
////        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//////                LayoutInflater inflater = mContext.getActivity().getLayoutInflater();
//////                LayoutInflater inflater = mContext.getApplicationContext().getLayoutInflater();
////        LayoutInflater inflater = LayoutInflater.from(mContext);
////        View dialogView = inflater.inflate(R.layout.scheme_dialog, null);
////        dialogBuilder.setView(dialogView);
//
//        TextView schemeName = dialogView.findViewById(R.id.tv_schemename);
//        TextView schemeType = dialogView.findViewById(R.id.tv_schemetype);
//        TextView schemeAmount = dialogView.findViewById(R.id.tv_schemeamnt);
//        TextView schemeDescription = dialogView.findViewById(R.id.tv_schemedscrpn);
//
//        schemeName.setText("Scheme Name : "+catalogue_mm.getScheme_name());
//        schemeType.setText("Scheme Type : "+catalogue_mm.getScheme_type());
//        schemeAmount.setText("Scheme Amount : "+catalogue_mm.getScheme_amount());
//        schemeDescription.setText("Scheme Description : "+catalogue_mm.getScheme_description());
//
//        AlertDialog alertDialog = dialogBuilder.create();
////        alertDialog.getWindow().setType(WindowManager.LayoutParams.
////                TYPE_SYSTEM_ALERT);
//        alertDialog.show();
//    }

    private class Varientsave extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            try {

                dbvoc = new DataBaseHelper(_activity);
                loginDataBaseAdapter = new LoginDataBaseAdapter(_activity);
                loginDataBaseAdapter = loginDataBaseAdapter.open();
                _activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //View parentView = null;

                        if (!(Global_Data.Order_hashmap.isEmpty())) {

                            try {
                                for (Object name : Global_Data.Order_hashmap.keySet()) {

                                    Object key = name.toString();
                                    Object value = Global_Data.Order_hashmap.get(name);
                                    Object value_remarks = Global_Data.Orderproduct_remarks.get(name);
                                    Object value_detail1 = Global_Data.Orderproduct_detail1.get(name);
                                    Object value_detail2 = Global_Data.Orderproduct_detail2.get(name);
                                    Object value_detail3 = Global_Data.Orderproduct_detail3.get(name);
                                    Object value_detail4 = Global_Data.Orderproduct_detail4.get(name);
                                    Object value_detail5 = Global_Data.Orderproduct_detail5.get(name);
                                    //System.out.println(key + " " + value);
                                    Log.d("KEY", "Key: " + key + " Value: " + value);
                                    JSONObject item = new JSONObject();

                                    String key_array[] = String.valueOf(key).split("&");
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value))) {

                                        String key_value_array[] = String.valueOf(value).split("pq");
                                        String key_value_price_array[] = key_value_array[1].split("pprice");
                                        String key_value_pname_array[] = key_value_price_array[1].split("pmrp");
                                        String key_value_pmrp_array[] = key_value_pname_array[1].split("prp");
                                        String key_value_schemeid_array[] = key_value_pmrp_array[1].split("sid");
                                        String key_value_url_array[] = key_value_schemeid_array[1].split("url");


                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_price_array[0])) {
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_remarks))) {
                                                p_remarks.add(String.valueOf(value_remarks));
                                            } else {
                                                p_remarks.add("");
                                            }

//                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail1))) {
//                                                p_detail1.add(String.valueOf(value_detail1));
//                                            } else {
//                                                p_detail1.add("");
//                                            }
//
//                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail2))) {
//                                                p_detail2.add(String.valueOf(value_detail2));
//                                            } else {
//                                                p_detail2.add("");
//                                            }
//
//                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail3))) {
//                                                p_detail3.add(String.valueOf(value_detail3));
//                                            } else {
//                                                p_detail3.add("");
//                                            }
//
//                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail4))) {
//                                                p_detail4.add(String.valueOf(value_detail4));
//                                            } else {
//                                                p_detail4.add("");
//                                            }
//
//                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail5))) {
//                                                p_detail5.add(String.valueOf(value_detail5));
//                                            } else {
//                                                p_detail5.add("");
//                                            }

                                            q_check = "yes";
                                            p_id.add(key_array[1]);
                                            p_q.add(key_value_array[0]);
                                            p_price.add(key_value_price_array[0]);
                                            p_name.add(key_value_pname_array[0]);
                                            scheme_id.add(key_value_url_array[0]);
                                            p_mrp.add(key_value_pmrp_array[0]);
                                            p_rp.add(key_value_url_array[0]);
                                            //p_url.add(key_value_url_array[1]);


                                            Log.d("quantity", "quantity" + key_value_array[0]);
                                        }


                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                    }
                });


            } catch (Exception ex) {
                ex.printStackTrace();
                // dialog.dismiss();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            SharedPreferences spf = _activity.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();

            if (!p_id.isEmpty() && q_check.equalsIgnoreCase("yes")) {

                Long randomPIN = System.currentTimeMillis();
                String PINString = String.valueOf(randomPIN);
                if (Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
                    if (Global_Data.sales_btnstring.equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                        Global_Data.GLObalOrder_id = "Ord" + PINString;
                        Global_Data.GLOvel_GORDER_ID = "Ord" + PINString;
                    } else {
                        Global_Data.GLObalOrder_id = "QNO" + PINString;
                        Global_Data.GLOvel_GORDER_ID = "QNO" + PINString;
                    }

                    try {
                        AppLocationManager appLocationManager = new AppLocationManager(_activity);
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                        PlayService_Location PlayServiceManager = new PlayService_Location(_activity);

                        if (PlayServiceManager.checkPlayServices(_activity)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    List<Local_Data> checkq = dbvoc.checkOrderExist(Global_Data.GLOvel_CUSTOMER_ID, Global_Data.GLObalOrder_id);

                    if (checkq.size() <= 0) {

                        Calendar c1 = Calendar.getInstance();
                        System.out.println("Current time =&gt; " + c1.getTime());

                        SimpleDateFormat fdff = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        final String formattedDaten = fdff.format(c1.getTime());

                        loginDataBaseAdapter.insertOrders("", Global_Data.GLOvel_GORDER_ID, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, "", "", "", "", "", "", "");

                    }

                }


                Double pp = 0.0;
                try {
                    for (int k = 0; k < p_id.size(); k++) {


                        List<Local_Data> contactsn = dbvoc.GetOrders_BY_ORDER_ID(Global_Data.GLObalOrder_id, p_id.get(k));

                        if (contactsn.size() > 0) {

                            for (Local_Data cn : contactsn) {

//                                if(Global_Data.Varient_value_add_flag.equalsIgnoreCase("yes"))
//                                {
                                //Global_Data.GrandTotal=Double.valueOf(p_price.get(k));
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                    dbvoc.update_itemamountandquantity_withremarks(String.valueOf(p_q.get(k)), String.valueOf(p_price.get(k)), p_id.get(k), Global_Data.GLObalOrder_id, p_remarks.get(k), "", "", "", "", "", "");
                                } else if (!(p_q.get(k).equalsIgnoreCase(cn.get_delivery_product_order_quantity())) && !(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k)))) {
                                    dbvoc.getDeleteTableorderproduct_byITEM_NUMBER(p_id.get(k), Global_Data.GLObalOrder_id);
                                }

//                                }
//                                else
//                                {
//                                    int quantity = Integer.parseInt(cn.get_delivery_product_order_quantity()) + Integer.parseInt(p_q.get(k));
//                                    Double amount = Double.valueOf(cn.getAmount()) + Double.valueOf(p_price.get(k));
//                                    dbvoc.update_itemamountandquantity(String.valueOf(quantity), String.valueOf(amount), p_id.get(k), Global_Data.GLObalOrder_id);
//                                }

                            }
                        } else {
                            String schemeid= scheme_id.get(k);
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", Global_Data.Search_Category_name, Global_Data.Search_Product_name, p_name.get(k), " ", schemeid, " ", "", p_q.get(k), p_rp.get(k), p_mrp.get(k), p_price.get(k), "", "", Global_Data.order_retailer, " ", p_id.get(k), " ", p_name.get(k), p_remarks.get(k), "", "", "", "", "", "");//Reading all

                                // Log.d("pPRIZE","Pprize"+ p_price.get(k));
                            }


                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                            pp += Double.valueOf(p_price.get(k));
                        }

                        edit_value.setText(p_q.get(k));
                        image.setItem_quantity(p_q.get(k));
                        image.setItem_remarks(p_remarks.get(k));
//                        image.setDetail1(p_detail1.get(k));
//                        image.setDetail2(p_detail2.get(k));
//                        image.setDetail3(p_detail3.get(k));
//                        image.setDetail4(p_detail4.get(k));
//                        image.setDetail5(p_detail5.get(k));

                        swipCatalauge_Adapter adapter = new swipCatalauge_Adapter(_activity,
                                Global_Data.catalogue_m);
                        ViewPager viewPager = (ViewPager) _activity.findViewById(R.id.pager);

                        viewPager.setAdapter(adapter);

                        // displaying selected image first
                        viewPager.setCurrentItem(p_position);


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

//                vdialogSave.setEnabled(true);
//                vdialogSave.setText(_activity.getResources().getString(R.string.Save));
                // txttotalPreview.setText("Total : " + pp);
                Global_Data.Varient_value_add_flag = "yes";

                List<Local_Data> checkq = dbvoc.getItemName(Global_Data.GLOvel_GORDER_ID);

                if (checkq.size() <= 0) {
                    q_check = "";
                    Global_Data.Order_hashmap.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_id.clear();
                    //p_url.clear();
                    p_q.clear();
                    p_price.clear();
                    p_remarks.clear();
//                    p_detail1.clear();
//                    p_detail2.clear();
//                    p_detail3.clear();
//                    p_detail4.clear();
//                    p_detail5.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_rp.clear();
                    // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    _activity.runOnUiThread(new Runnable() {
                        public void run() {

                            Global_Data.Custom_Toast(_activity, _activity.getResources().getString(R.string.All_item_delete),"yes");
                            openDialog.dismiss();
                            // Product_Variant.setText("");

                            Intent i = new Intent(_activity, NewOrderActivity.class);
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            _activity.startActivity(i);
                            _activity.finish();
                        }
                    });

                } else {
                    pp = 0.0;
                    for (Local_Data qtr : checkq) {
                        pp += Double.valueOf(qtr.getAmount());
                    }
                    Global_Data.GrandTotal=pp;
                    //txttotalPreview.setText("Total : " + pp);
                    q_check = "";
                    Global_Data.Order_hashmap.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
//                    if(!Product_Variant.getText().toString().equalsIgnoreCase(""))
//                    {
//                        Product_Variant.setText("");
//                    }

                    p_id.clear();
                    //p_url.clear();
                    p_q.clear();
                    p_price.clear();
                    p_remarks.clear();
//                    p_detail1.clear();
//                    p_detail2.clear();
//                    p_detail3.clear();
//                    p_detail4.clear();
//                    p_detail5.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_rp.clear();

                    Global_Data.Custom_Toast(_activity, _activity.getResources().getString(R.string.Item_added_successfully),"yes");
                    openDialog.dismiss();
                    Intent i = new Intent(_activity, PreviewOrderSwipeActivity.class);
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    _activity.startActivity(i);
                    //_activity.finish();
                }

            } else {
                Global_Data.Orderproduct_detail1.clear();
                Global_Data.Orderproduct_detail2.clear();
                Global_Data.Orderproduct_detail3.clear();
                Global_Data.Orderproduct_detail4.clear();
                Global_Data.Orderproduct_detail5.clear();
                q_check = "";
                Global_Data.Order_hashmap.clear();
                Global_Data.Orderproduct_remarks.clear();
                p_id.clear();
                //p_url.clear();
                p_q.clear();
                p_price.clear();
                p_remarks.clear();
//                p_detail1.clear();
//                p_detail2.clear();
//                p_detail3.clear();
//                p_detail4.clear();
//                p_detail5.clear();
                p_name.clear();
                scheme_id.clear();
                p_mrp.clear();
                p_rp.clear();
//                vdialogSave.setEnabled(true);
//                vdialogSave.setText(_activity.getResources().getString(R.string.Save));

                Global_Data.Custom_Toast(_activity, _activity.getResources().getString(R.string.Please_enter_quantity),"yes");
            }

            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });

        }

        @Override
        protected void onPreExecute() {

            _activity.runOnUiThread(new Runnable() {
                public void run() {


//          dialog.setMessage("Please wait....");
//          dialog.setTitle("Siyaram App");
//          dialog.setCancelable(false);
//          dialog.show();

//                    vdialogSave.setEnabled(false);
//                    vdialogSave.setText(_activity.getResources().getString(R.string.Please_Wait));
                    //int pic = R.drawable.round_btngray;
                    // buttonPreviewAddMOre.setBackgroundResource(pic);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

}