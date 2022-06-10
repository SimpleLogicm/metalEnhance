package com.msimplelogic.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.msimplelogic.activities.PreviousSwipCatelauge;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Catalogue_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.Catalogue_slider_caller;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PreviousCatalogueAdapter extends RecyclerView.Adapter<PreviousCatalogueAdapter.MyViewHolder> {
    private Catalogue_slider_caller listener;
    String image_url = "";
    private List<Catalogue_model> catalogue_m;
    private Context mContext;
    customButtonListener customListner;
    String q_check = "";
    String str;
    DataBaseHelper dbvoc;
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
        public TextView t_title, mrpv, rpv, cat_pid, text_rp, totalprice, vimg_url, rp_catalogue, mrp_catalogue;
        public EditText edit_value, edit_comment, edit_detail1, edit_detail2, edit_detail3, edit_detail4, edit_detail5;

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
            vimg_url = (TextView) view.findViewById(R.id.vimg_url);
            rp_catalogue = (TextView) view.findViewById(R.id.rp_catalogue);
            mrp_catalogue = (TextView) view.findViewById(R.id.mrp_catalogue);
        }
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public PreviousCatalogueAdapter(Context context, List<Catalogue_model> catalogue_m, Catalogue_slider_caller listener) {
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

        catalogue_mm = catalogue_m.get(position);
        holder.t_title.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_name()));
        holder.mrpv.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_mrp()));
        holder.rpv.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_rp()));
        holder.cat_pid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_number()));
        holder.edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity()));
        holder.edit_comment.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_remarks()));
        holder.edit_detail1.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail1()));
        holder.edit_detail2.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail2()));
        holder.edit_detail3.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail3()));
        holder.edit_detail4.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail4()));
        holder.edit_detail5.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getDetail5()));
        holder.totalprice.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_amount()));

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
                        // .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.thumbnail);
                image_url = "not found";
                holder.vimg_url.setText("not found");
            } else {
                Glide.with(mContext).load(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_image_url()))
                        .thumbnail(Glide.with(mContext).load("file:///android_asset/loading.gif"))
                        //  .crossFade()
                        .error(R.drawable.img_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.thumbnail);
                image_url = catalogue_mm.getItem_image_url();
                holder.vimg_url.setText(catalogue_mm.getItem_image_url().trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Glide.with(mContext).load(R.drawable.img_not_found)
                    // .crossFade()
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

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
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

                        Double value = Double.valueOf(holder.edit_value.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
//                        if(price_str.length()>0)
//                        {
//                            holder.totalprice.setText(price_str+" : " + String.valueOf(value));
//                        }else{
//                                 holder.totalprice.setText("PRICE : " + String.valueOf(value));
//                             }
                        holder.totalprice.setText(mContext.getResources().getString(R.string.PRICE) + String.valueOf(value));

                        Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } else {
                    holder.totalprice.setText("");
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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

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

                            Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), holder.edit_value.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "url" + holder.vimg_url.getText().toString());

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

                Intent goToNewOrderActivity = new Intent(mContext, PreviousSwipCatelauge.class);
                goToNewOrderActivity.putExtra("position", position);
//                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
                goToNewOrderActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(goToNewOrderActivity);

//
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


}