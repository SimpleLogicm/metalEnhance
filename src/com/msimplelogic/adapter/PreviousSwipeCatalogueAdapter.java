package com.msimplelogic.adapter;

/**
 * Created by VV on 10/02/18.
 */


import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.msimplelogic.activities.PreviousOnlineCatalogue;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Catalogue_model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.TouchImageView;

public class PreviousSwipeCatalogueAdapter extends PagerAdapter {
    private Context mContext;
    String price_str;
    String image_url = "";
    String q_check = "";
    Dialog openDialog;
    int p_position = 0;
    Catalogue_model image;
    private ArrayList<String> p_id = new ArrayList<String>();
    private ArrayList<String> p_name = new ArrayList<String>();
    private ArrayList<String> p_mrp = new ArrayList<String>();
    private ArrayList<String> p_rp = new ArrayList<String>();
    private ArrayList<String> p_q = new ArrayList<String>();
    private ArrayList<String> p_price = new ArrayList<String>();
    private ArrayList<String> p_remarks = new ArrayList<String>();
    private ArrayList<String> p_url = new ArrayList<String>();
    private ArrayList<String> p_detail1 = new ArrayList<String>();
    private ArrayList<String> p_detail2 = new ArrayList<String>();
    private ArrayList<String> p_detail3 = new ArrayList<String>();
    private ArrayList<String> p_detail4 = new ArrayList<String>();
    private ArrayList<String> p_detail5 = new ArrayList<String>();
    Button dialogSave;
    DataBaseHelper dbvoc;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private List<Catalogue_model> catalogue_m;
    private Activity _activity;

    private LayoutInflater inflater;
    TextView vdialogText, vdialogMrp, vdialogRp, vdialogPid, vdialogPrdname, vpp_mrp, vpp_rp, vimg_url, flip_img, eprice_dialog;
    ImageView vdialogImage, vdialogPlus, vdialogMinus, vdialogClose;
    TextView edit_value;
    Button vdialogSave;



    // constructor
    public PreviousSwipeCatalogueAdapter(Activity activity,
                                         List<Catalogue_model> catalogue_m) {
        this._activity = activity;
        this.catalogue_m = catalogue_m;
    }

    @Override
    public int getCount() {
        return this.catalogue_m.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final TouchImageView thumbnail;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.onlinecatalogue_slids, container,
                false);

        thumbnail = (TouchImageView) viewLayout.findViewById(R.id.vdialog_img);

        vdialogText = (TextView) viewLayout.findViewById(R.id.dialog_text);

        //ImageView dialogImage = (ImageView) view.findViewById(R.id.dialog_img);
        vdialogClose = (ImageView) viewLayout.findViewById(R.id.close_btn);
        vdialogPlus = (ImageView) viewLayout.findViewById(R.id.plus_dialog);
        vdialogMinus = (ImageView) viewLayout.findViewById(R.id.minus_dialog);
        edit_value = (TextView) viewLayout.findViewById(R.id.editval_dialog);

        vdialogMrp = (TextView) viewLayout.findViewById(R.id.mrp_dialog);
        vdialogRp = (TextView) viewLayout.findViewById(R.id.rp_dialog);
        flip_img = (TextView) viewLayout.findViewById(R.id.flip_img);
        //sli_price = (TextView) viewLayout.findViewById(R.id.sli_price);

        vpp_mrp = (TextView) viewLayout.findViewById(R.id.pp_mrp);
        vpp_rp = (TextView) viewLayout.findViewById(R.id.pp_rp);

        vdialogPid = (TextView) viewLayout.findViewById(R.id.cat_pid_dialog);
        vdialogPrdname = (TextView) viewLayout.findViewById(R.id.prodname_doalog);
        vdialogSave = (Button) viewLayout.findViewById(R.id.online_catalog_dialog_save);

        vimg_url = (TextView) viewLayout.findViewById(R.id.vimg_url);

        final Catalogue_model catalogue_mm = catalogue_m.get(position);


        // for label RP change
        SharedPreferences spf1 = _activity.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
//        price_str=spf1.getString("var_total_price", "");

        if (rpstr.length() > 0) {
            vdialogRp.setText(rpstr + " : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_rp()));

        } else {
            vdialogRp.setText(_activity.getResources().getString(R.string.SRP) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_rp()));
        }

        if (mrpstr.length() > 0) {
            vdialogMrp.setText(mrpstr + " : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_mrp()));
        } else {
            vdialogMrp.setText(_activity.getResources().getString(R.string.SMRP) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_mrp()));

        }


        vdialogPid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_number()));
        edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_quantity()));
        vdialogPrdname.setText(_activity.getResources().getString(R.string.SPRODUCT) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_name()));
        //  sli_price.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_amount()));

//        if(str.length()>0)
//        {
//            holder.edit_value.setText(str);
//        }else{
//                   }

        // holder.t_title.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_name()));
        try {
            String s = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_image_url());
            if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {
                Glide.with(_activity).load(R.drawable.img_not_found)
                        // .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(thumbnail);
                image_url = "not found";
                flip_img.setVisibility(View.GONE);
                // vimg_url.setText("not found");
            } else {
                Glide.with(_activity).load(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(catalogue_mm.getItem_image_url()))
                        .thumbnail(Glide.with(_activity).load("file:///android_asset/loading.gif"))
                        // .crossFade()
                        .error(R.drawable.img_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(thumbnail);

                image_url = catalogue_mm.getItem_image_url();
                flip_img.setVisibility(View.VISIBLE);


                // vimg_url.setText(catalogue_mm.getItem_image_url().trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Glide.with(_activity).load(R.drawable.img_not_found)
                    // .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnail);
            image_url = "not found";
            flip_img.setVisibility(View.GONE);
            // vimg_url.setText("not found");

        }

        flip_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(_activity, R.animator.flipping);
                    anim.setTarget(thumbnail);
                    anim.setDuration(3000);
                    anim.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        vdialogPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                if (edit_value.getText().toString().equalsIgnoreCase("")) {
                    edit_value.setText(String.valueOf(1));
                } else {
                    int s = Integer.parseInt(edit_value.getText().toString()) + 1;
                    if (s <= 9999) {
                        edit_value.setText(String.valueOf(Integer.parseInt(edit_value.getText().toString()) + 1));
                    }
                }
            }
        });

        vdialogMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                if (!(edit_value.getText().toString().equalsIgnoreCase("")) && !(Integer.parseInt(edit_value.getText().toString()) <= 0)) {
                    edit_value.setText(String.valueOf(Integer.parseInt(edit_value.getText().toString()) - 1));
                }
            }
        });

        edit_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(_activity, "You Clicked", Toast.LENGTH_SHORT).show();
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
                final TextView dialogPrdname = (TextView) openDialog.findViewById(R.id.prodname_doalog);

                dialogSave = (Button) openDialog.findViewById(R.id.online_catalog_dialog_save);

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


                image = catalogue_m.get(position);

                p_position = position;

                dialogText.setText(image.getItem_name());

                eprice_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_amount()));

                remark_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_remarks()));

                edit_detail1.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail1()));
                edit_detail2.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail2()));
                edit_detail3.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail3()));
                edit_detail4.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail4()));
                edit_detail5.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getDetail5()));

                minqty_dialog.setText(_activity.getResources().getString(R.string.OMIN_QTY) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_min_qty()));

                pkgqty_dialog.setText(_activity.getResources().getString(R.string.OPkg_Qty) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_pkg_qty1()));

                scheme_dialog.setText(_activity.getResources().getString(R.string.OScheme) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_scheme()));

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
                dialogPrdname.setText(_activity.getResources().getString(R.string.SPRODUCT) + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_name()));

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

                try {
                    String s = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_image_url());
                    if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {

                        vimg_url.setText("not found");
                    } else {

                        vimg_url.setText(image.getItem_image_url().trim());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    vimg_url.setText("not found");

                }


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


                                Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                        } else {
                            eprice_dialog.setText("");
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
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


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
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


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
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


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
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


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
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


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
                                    Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), dialogEdit.getText().toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "url" + vimg_url.getText().toString());


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
                        p_url.clear();
                        p_name.clear();
                        p_mrp.clear();
                        p_q.clear();
                        p_price.clear();
                        p_remarks.clear();
                        p_detail1.clear();
                        p_detail2.clear();
                        p_detail3.clear();
                        p_detail4.clear();
                        p_detail5.clear();
                        p_rp.clear();
                        q_check = "";

//                    dialog = new ProgressDialog(OnlineCatalogue.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    dialog.setMessage("Please wait Customer Loading....");
//                    dialog.setTitle("Metal App");
//                    dialog.setCancelable(false);
//                    dialog.show();

                        new Varientsave().execute();
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
                                    p_url.clear();
                                    p_name.clear();
                                    p_mrp.clear();
                                    p_q.clear();
                                    p_price.clear();
                                    p_remarks.clear();
                                    p_detail1.clear();
                                    p_detail2.clear();
                                    p_detail3.clear();
                                    p_detail4.clear();
                                    p_detail5.clear();
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
                            p_url.clear();
                            p_name.clear();
                            p_mrp.clear();
                            p_q.clear();
                            p_price.clear();
                            p_remarks.clear();
                            p_detail1.clear();
                            p_detail2.clear();
                            p_detail3.clear();
                            p_detail4.clear();
                            p_detail5.clear();
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

//                try {
//                    if (customListner != null) {
//                        customListner.onButtonClickListner(position);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }

            }
        });


        // close button click event
        vdialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewOrderActivity = new Intent(_activity, PreviousOnlineCatalogue.class);
                _activity.startActivity(goToNewOrderActivity);
                _activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }


    private class Varientsave extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            try {

                dbvoc = new DataBaseHelper(_activity);
                loginDataBaseAdapter = new LoginDataBaseAdapter(_activity);
                loginDataBaseAdapter = loginDataBaseAdapter.open();
                _activity.runOnUiThread(new Runnable() {
                    public void run() {
                        View parentView = null;

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
                                        String key_value_url_array[] = key_value_pmrp_array[1].split("url");


                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_price_array[0])) {
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_remarks))) {
                                                p_remarks.add(String.valueOf(value_remarks));
                                            } else {
                                                p_remarks.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail1))) {
                                                p_detail1.add(String.valueOf(value_detail1));
                                            } else {
                                                p_detail1.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail2))) {
                                                p_detail2.add(String.valueOf(value_detail2));
                                            } else {
                                                p_detail2.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail3))) {
                                                p_detail3.add(String.valueOf(value_detail3));
                                            } else {
                                                p_detail3.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail4))) {
                                                p_detail4.add(String.valueOf(value_detail4));
                                            } else {
                                                p_detail4.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail5))) {
                                                p_detail5.add(String.valueOf(value_detail5));
                                            } else {
                                                p_detail5.add("");
                                            }

                                            q_check = "yes";
                                            p_id.add(key_array[1]);
                                            p_q.add(key_value_array[0]);
                                            p_price.add(key_value_price_array[0]);
                                            p_name.add(key_value_pname_array[0]);
                                            p_mrp.add(key_value_pmrp_array[0]);
                                            p_rp.add(key_value_url_array[0]);
                                            p_url.add(key_value_url_array[1]);


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

            SharedPreferences spf = mContext.getSharedPreferences("SimpleLogic", 0);
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

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                    dbvoc.update_itemamountandquantity_withremarks(String.valueOf(p_q.get(k)), String.valueOf(p_price.get(k)), p_id.get(k), Global_Data.GLObalOrder_id, p_remarks.get(k), p_detail1.get(k), p_url.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k));
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

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", Global_Data.Search_Category_name, Global_Data.Search_Product_name, p_name.get(k), " ", "", " ", "", p_q.get(k), p_rp.get(k), p_mrp.get(k), p_price.get(k), "", "", Global_Data.order_retailer, " ", p_id.get(k), " ", p_name.get(k), p_remarks.get(k), p_detail1.get(k), p_url.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k));//Reading all

                                // Log.d("pPRIZE","Pprize"+ p_price.get(k));
                            }


                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                            pp += Double.valueOf(p_price.get(k));
                        }

                        edit_value.setText(p_q.get(k));
                        image.setItem_quantity(p_q.get(k));
                        image.setItem_remarks(p_remarks.get(k));
                        image.setDetail1(p_detail1.get(k));
                        image.setDetail2(p_detail2.get(k));
                        image.setDetail3(p_detail3.get(k));
                        image.setDetail4(p_detail4.get(k));
                        image.setDetail5(p_detail5.get(k));

                        PreviousSwipeCatalogueAdapter adapter = new PreviousSwipeCatalogueAdapter(_activity,
                                Global_Data.catalogue_m);
                        ViewPager viewPager = (ViewPager) _activity.findViewById(R.id.pager);

                        viewPager.setAdapter(adapter);

                        // displaying selected image first
                        viewPager.setCurrentItem(p_position);


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                dialogSave.setEnabled(true);
                dialogSave.setText(_activity.getResources().getString(R.string.Save));
                // txttotalPreview.setText("Total : " + pp);
                Global_Data.Varient_value_add_flag = "yes";

                List<Local_Data> checkq = dbvoc.getItemName(Global_Data.GLOvel_GORDER_ID);

                if (checkq.size() <= 0) {
                    q_check = "";
                    Global_Data.Order_hashmap.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_id.clear();
                    p_url.clear();
                    p_q.clear();
                    p_price.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
                    p_name.clear();
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
                    p_url.clear();
                    p_q.clear();
                    p_price.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
                    p_name.clear();
                    p_mrp.clear();
                    p_rp.clear();

                    Global_Data.Custom_Toast(_activity, _activity.getResources().getString(R.string.Item_added_successfully),"yes");
                    openDialog.dismiss();
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
                p_url.clear();
                p_q.clear();
                p_price.clear();
                p_remarks.clear();
                p_detail1.clear();
                p_detail2.clear();
                p_detail3.clear();
                p_detail4.clear();
                p_detail5.clear();
                p_name.clear();
                p_mrp.clear();
                p_rp.clear();
                dialogSave.setEnabled(true);
                dialogSave.setText(_activity.getResources().getString(R.string.Save));

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

                    dialogSave.setEnabled(false);
                    dialogSave.setText(_activity.getResources().getString(R.string.Please_Wait));
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