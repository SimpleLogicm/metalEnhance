

package com.msimplelogic.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.SchemeDialog;
import com.msimplelogic.activities.SwipCatelauge;
import com.msimplelogic.model.Catalogue_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.Catalogue_slider_caller;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.MyViewHolder> {
    private Catalogue_slider_caller listener;
    String image_url = "";
    SharedPreferences spv;
    SharedPreferences sp;

    private List<Catalogue_model> catalogue_m;
    private Context mContext;
    customButtonListener customListner;
    String q_check = "";
    String str;
    DataBaseHelper dbvoc;
    String Scheme_name;
    String Scheme_type;
    String Scheme_amount;
    String Scheme_description;
    String Scheme_buyquan;
    String Scheme_getquan;
    String Scheme_minquqn;
    Catalogue_model catalogue_mm;

    String price_str;
    private ArrayList<String> p_id = new ArrayList<String>();
    private ArrayList<String> p_name = new ArrayList<String>();
    private ArrayList<String> p_mrp = new ArrayList<String>();
    private ArrayList<String> p_rp = new ArrayList<String>();
    private ArrayList<String> p_q = new ArrayList<String>();
    private ArrayList<String> p_price = new ArrayList<String>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, plus_btn, minus_btn;
        public TextView t_title, mrpv, rpv, cat_pid, text_rp, totalprice, vimg_url, rp_catalogue, mrp_catalogue, discount_price, topSellingIcon;
        public EditText edit_value, edit_comment, edit_detail1, edit_detail2, edit_detail3, edit_detail4, edit_detail5;
        public ImageView schemeBtn,showUpdateStock,scheme_btn;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            scheme_btn = (ImageView) view.findViewById(R.id.scheme_btn);
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
            schemeBtn = (ImageView) view.findViewById(R.id.scheme_btn);
            topSellingIcon = (TextView) view.findViewById(R.id.top_selling_icon);
            showUpdateStock = (ImageView) view.findViewById(R.id.show_update_stock);
            spv = mContext.getSharedPreferences("valuesset", 0);
        }
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public CatalogueAdapter(Context context, List<Catalogue_model> catalogue_m, Catalogue_slider_caller listener) {
        mContext = context;
        this.catalogue_m = catalogue_m;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catalogue_thumbnail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        sp = mContext.getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
         holder.showUpdateStock.setImageResource(R.drawable.showupdatestock_logo_dark);
       //  holder.scheme_btn.setImageResource(R.drawable.showupdatestock_logo_dark);

        }


        catalogue_mm = catalogue_m.get(position);
        holder.t_title.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_name()));
        holder.mrpv.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_mrp()));
        holder.rpv.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_rp()));
        holder.cat_pid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_number()));

        String itemQty = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity());
        holder.edit_value.setText(itemQty);

//        String itam = spv.getString("value", "");
//        String str = holder.edit_value.getText().toString();
//        if (str.contains(itemQty)) {
//            holder.edit_value.setText(itemQty);
//        } else {
//            holder.edit_value.setText(itam);
//        }

        SharedPreferences.Editor editor = spv.edit();
        editor.putString("SCHEME_NAME", catalogue_m.get(position).getScheme_name());
        editor.putString("SCHEME_TYPE", catalogue_m.get(position).getScheme_type());
        editor.putString("SCHEME_AMOUNT", catalogue_m.get(position).getScheme_amount());
        editor.putString("SCHEME_DESCRIPTION", catalogue_m.get(position).getScheme_description());
        editor.putString("SCHEME_BUYQTY", catalogue_m.get(position).getScheme_buy_qty());
        editor.putString("SCHEME_GETQTY", catalogue_m.get(position).getScheme_get_qty());
        editor.putString("SCHEME_MINQTY", catalogue_m.get(position).getScheme_min_qty());
        editor.commit();

        Scheme_name=catalogue_m.get(position).getScheme_name();
        Scheme_amount=catalogue_m.get(position).getScheme_amount();
        Scheme_description=catalogue_m.get(position).getScheme_description();
        Scheme_type=catalogue_m.get(position).getScheme_type();
        Scheme_buyquan=catalogue_m.get(position).getScheme_buy_qty();
        Scheme_getquan=catalogue_m.get(position).getScheme_get_qty();
        Scheme_minquqn=catalogue_m.get(position).getScheme_min_qty();


//        SharedPreferences spf1 = _activity.getSharedPreferences("SimpleLogic", 0);
//        String rpstr = spf1.getString("var_rp", "");
//        String mrpstr = spf1.getString("var_mrp", "");


//        if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity()).equalsIgnoreCase(Global_Data.SchemeItemQty))
//        {
//            //Global_Data.SchemeItemQty= Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity());
//            //String itemQty= Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity());
//            //Global_Data.SchemeItemQty=holder.edit_value.getText().toString();
//            holder.edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity()));
//            //Global_Data.SchemeItemQty="";
//        }else{
//            //String itemQty= Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity());
//            holder.edit_value.setText(Global_Data.SchemeItemQty);
//
//            //Global_Data.SchemeItemQty=holder.edit_value.getText().toString().trim();
//        }

        // holder.edit_value.setText(itemQty);

//        if(itemQty.length()>0)
//        {
//            holder.edit_value.setText("");
//        }else{
//            holder.edit_value.setText(itemQty);
//        }

        holder.edit_comment.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_remarks()));
        holder.edit_detail1.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail1()));
        holder.edit_detail2.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail2()));
        holder.edit_detail3.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail3()));
        holder.edit_detail4.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail4()));
        holder.edit_detail5.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail5()));
        holder.totalprice.setPaintFlags(0);
        holder.totalprice.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_amount()));

        // for label RP change
        final SharedPreferences spf1 = mContext.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        String order_product_detail2 = spf1.getString("order_product_detail2", "");
        String order_product_detail3 = spf1.getString("order_product_detail3", "");
        String order_product_detail4 = spf1.getString("order_product_detail4", "");
        String order_product_detail5 = spf1.getString("order_product_detail5", "");
        String order_product_detail6 = spf1.getString("order_product_detail6", "");

//        price_str=spf1.getString("var_total_price", "");

        if (catalogue_mm.getScheme_topsellingproduct().equalsIgnoreCase("true")) {
            holder.topSellingIcon.setVisibility(View.VISIBLE);
        } else {
            holder.topSellingIcon.setVisibility(View.GONE);
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

        String aa = catalogue_mm.getScheme_type();

        if (aa.equalsIgnoreCase("") || aa.equalsIgnoreCase(null) || aa.equalsIgnoreCase("null")) {
            holder.schemeBtn.setVisibility(View.GONE);
        } else {
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


        holder.showUpdateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View yourView = v.findViewById(R.id.show_update_stock);
                new SimpleTooltip.Builder(mContext)
                        .anchorView(yourView)
                        .text("Customer Stock:0 \n Partner Stock:0")
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });

        holder.schemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!catalogue_mm.getScheme_name().equalsIgnoreCase("") || !catalogue_mm.getScheme_name().equalsIgnoreCase("null")) {
                    Intent m = new Intent(mContext, SchemeDialog.class);
                    m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    m.putExtra("SCHEME_NAME", catalogue_m.get(position).getScheme_name());
                    m.putExtra("SCHEME_TYPE", catalogue_m.get(position).getScheme_type());
                    m.putExtra("SCHEME_AMOUNT", catalogue_m.get(position).getScheme_amount());
                    m.putExtra("SCHEME_DESCRIPTION", catalogue_m.get(position).getScheme_description());
                    m.putExtra("SCHEME_BUYQTY", catalogue_m.get(position).getScheme_buy_qty());
                    m.putExtra("SCHEME_GETQTY",catalogue_m.get(position).getScheme_get_qty());
                    m.putExtra("SCHEME_MINQTY",catalogue_m.get(position).getScheme_min_qty());
                    mContext.startActivity(m);
                } else {
                    Global_Data.Custom_Toast(mContext, "There is no scheme available","Yes");
//                    Toast.makeText(mContext, "There is no scheme available", Toast.LENGTH_SHORT).show();
                }


//                if(!catalogue_mm.getScheme_name().equalsIgnoreCase("") || !catalogue_mm.getScheme_name().equalsIgnoreCase("null"))
//                {
//                    SchemeDialog(mContext);
//                }else {
//                    Toast.makeText(mContext, "There is no scheme available", Toast.LENGTH_SHORT).show();
//                }

                // SchemeDialog(mContext);

//                // custom dialog
//                final Dialog dialog = new Dialog(mContext);
//                dialog.setContentView(R.layout.scheme_dialog);
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                dialog.setTitle("Scheme");
//
//                // set the custom dialog components - text, image and button
//                TextView schemeName = dialog.findViewById(R.id.tv_schemename);
//                TextView schemeType = dialog.findViewById(R.id.tv_schemetype);
//                TextView schemeAmount = dialog.findViewById(R.id.tv_schemeamnt);
//                TextView schemeDescription = dialog.findViewById(R.id.tv_schemedscrpn);
//
//                schemeName.setText("Scheme Name : "+catalogue_mm.getScheme_name());
//                schemeType.setText("Scheme Type : "+catalogue_mm.getScheme_type());
//                schemeAmount.setText("Scheme Amount : "+catalogue_mm.getScheme_amount());
//                schemeDescription.setText("Scheme Description : "+catalogue_mm.getScheme_description());

                // Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
//                dialogButton.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                //dialog.show();
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
//                    spv=mContext.getSharedPreferences("valuesset",0);
                    SharedPreferences.Editor editor = spv.edit();
                    String str = holder.edit_value.getText().toString();
                    editor.putString("value", str);
                    editor.commit();
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

                        String editValue = holder.edit_value.getText().toString();

                        Global_Data.SchemeItemQty = holder.edit_value.getText().toString();
                        Double value = Double.valueOf(editValue) * Double.valueOf(holder.mrpv.getText().toString());

                        holder.totalprice.setPaintFlags(0);
                     //   holder.totalprice.setText(mContext.getResources().getString(R.string.PRICE) + String.valueOf(value));
                        holder.totalprice.setText("₹" + String.valueOf(value));

//                        Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

                        if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_m.get(position).getScheme_type())).equalsIgnoreCase("value_scheme")) {
                            String minSchemeQty = (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_m.get(position).getScheme_min_qty()));

                            if (minSchemeQty.length() > 0) {
                                if (Double.valueOf(holder.edit_value.getText().toString()) >= Double.valueOf(minSchemeQty)) {
                                    holder.discount_price.setVisibility(View.VISIBLE);
                                    holder.totalprice.setPaintFlags(holder.totalprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                   // holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value - (Double.valueOf(catalogue_m.get(position).getScheme_amount()))));
                                    holder.discount_price.setText("₹" + String.valueOf(value - (Double.valueOf(catalogue_m.get(position).getScheme_amount()))));
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value - (Double.valueOf(catalogue_m.get(position).getScheme_amount()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                } else {
                                    holder.discount_price.setVisibility(View.GONE);
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());
                                }

                            } else {
                                holder.discount_price.setVisibility(View.VISIBLE);
                              //  holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value - (Double.valueOf(catalogue_m.get(position).getScheme_amount()) * Double.valueOf(holder.edit_value.getText().toString()))));
                                holder.discount_price.setText("₹" + String.valueOf(value - (Double.valueOf(catalogue_m.get(position).getScheme_amount()) * Double.valueOf(holder.edit_value.getText().toString()))));
                                holder.totalprice.setPaintFlags(holder.totalprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value - (Double.valueOf(catalogue_m.get(position).getScheme_amount()) * Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());
                            }


                        } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_m.get(position).getScheme_type())).equalsIgnoreCase("discount_scheme")) {
//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
//                            double res = (value-((value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount()))));
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
//                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                            String minSchemeQty = (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_m.get(position).getScheme_min_qty()));

                            if (minSchemeQty.length() > 0) {
                                if (Double.valueOf(holder.edit_value.getText().toString()) >= Double.valueOf(minSchemeQty)) {
                                    holder.discount_price.setVisibility(View.VISIBLE);
                                    holder.totalprice.setPaintFlags(holder.totalprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                    double res = (value - ((value / 100.0f) * (Double.valueOf(catalogue_m.get(position).getScheme_amount()))));
                                //    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                    holder.discount_price.setText("₹" + String.valueOf(res));
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                } else {
                                    holder.discount_price.setVisibility(View.GONE);
                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());
                                }

                            } else {
                                holder.discount_price.setVisibility(View.VISIBLE);
                                holder.totalprice.setPaintFlags(holder.totalprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                double res = (value - ((value / 100.0f) * (Double.valueOf(catalogue_m.get(position).getScheme_amount()))));
                                holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                holder.discount_price.setText("₹" + String.valueOf(res));
                                Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());
                            }


                        } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_m.get(position).getScheme_type())).equalsIgnoreCase("quantity_scheme")) {

//                         Toast.makeText(mContext, ""+(((Double.valueOf(catalogue_mm.getScheme_buy_qty()))+(Double.valueOf(catalogue_mm.getScheme_get_qty())))*Double.valueOf(holder.edit_value.getText().toString())), Toast.LENGTH_SHORT).show();
                            double aaa = (Double.valueOf(holder.edit_value.getText().toString()) / Double.valueOf(catalogue_m.get(position).getScheme_buy_qty())) + Double.valueOf(holder.edit_value.getText().toString());
                            int y = (int) aaa;
                            // Toast.makeText(mContext, ""+y, Toast.LENGTH_SHORT).show();

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), y + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
                        } else {
                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

                        }

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } else {
                    holder.totalprice.setPaintFlags(0);
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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_m.get(position).getScheme_id() + "url" + holder.vimg_url.getText().toString());

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


//        for (int j = 0; j < Global_Data.products.length(); j++) {
//            //JSONObject image_object = Global_Data.productCatalogueImages.getJSONObject(j);
//            //catalogue_model.setItem_image_url(finalDomain + image_object.getString("thumb_url"));
//
//            //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
//            //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
//        }




        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String domain1 = "";
                SharedPreferences sp = mContext.getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                domain1 = Cust_domain;
                Global_Data.imageUrlArr1.clear();
                    JSONObject jObject = null;
                    try {
                        jObject = Global_Data.products.getJSONObject(position);
                        JSONArray product_catalogues = jObject.getJSONArray("product_catalogues");
                        //product_catalogues.get(position);

                        for (int j = 0; j < product_catalogues.length(); j++) {
                            JSONObject image_object = product_catalogues.getJSONObject(j);
                            //swipeImageModel.setImage(finalDomain + image_object.getString("thumb_url"));
                            //swipeImageModel.setImage(finalDomain + image_object.getString("thumb_url"));
                            Global_Data.imageUrlArr1.add((domain1 + image_object.getString("thumb_url")));
                            //tempArray[i] = myArray [i];
                            // swipeImageModel.setImage("https://demonuts.com/Demonuts/SampleImages/W-03.JPG");
                            //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
                            //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                //}


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

                Intent goToNewOrderActivity = new Intent(mContext, SwipCatelauge.class);
                goToNewOrderActivity.putExtra("position", position);
//                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
                goToNewOrderActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(goToNewOrderActivity);

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

}