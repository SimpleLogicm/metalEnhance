package com.msimplelogic.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;

import com.msimplelogic.model.Catalogue_model;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineCatalogue_Slides extends DialogFragment {
    int final_posion;
    Catalogue_model image;
    private String TAG = OnlineCatalogue_Slides.class.getSimpleName();
    private ArrayList<Catalogue_model> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView dialogText, dialogMrp, dialogRp, dialogPid, dialogPrdname;
    private TextView vdialogText, vdialogMrp, vdialogRp, vdialogPid, vdialogPrdname, vpp_mrp, vpp_rp, price_dialog;
    private EditText dialogEdit, vdialogEdit;
    private ImageView dialogImage, dialogClose, dialogPlus, dialogMinus;
    private ImageView vdialogImage, vdialogClose, vdialogPlus;
    private Button dialogSave, vdialogSave;
    private int selectedPosition = 0;

    static OnlineCatalogue_Slides newInstance() {
        OnlineCatalogue_Slides f = new OnlineCatalogue_Slides();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.onlinecatalogue_slids, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
//        lblCount = (TextView) v.findViewById(R.id.lbl_count);
//        lblTitle = (TextView) v.findViewById(R.id.title);
//        lblDate = (TextView) v.findViewById(R.id.date);


        dialogText = (TextView) v.findViewById(R.id.dialog_text);
        dialogImage = (ImageView) v.findViewById(R.id.vdialog_img);
        dialogClose = (ImageView) v.findViewById(R.id.close_btn);
        dialogPlus = (ImageView) v.findViewById(R.id.plus_dialog);
        dialogMinus = (ImageView) v.findViewById(R.id.minus_dialog);
        dialogEdit = (EditText) v.findViewById(R.id.editval_dialog);
        dialogMrp = (TextView) v.findViewById(R.id.mrp_dialog);
        dialogRp = (TextView) v.findViewById(R.id.rp_dialog);
        dialogPid = (TextView) v.findViewById(R.id.cat_pid_dialog);
        dialogPrdname = (TextView) v.findViewById(R.id.prodname_doalog);
        dialogSave = (Button) v.findViewById(R.id.online_catalog_dialog_save);

//        dialogMinus.setVisibility(View.GONE);
//          dialogPlus.setVisibility(View.GONE);
//         dialogEdit.setVisibility(View.GONE);


        images = (ArrayList<Catalogue_model>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // setCurrentItem(selectedPosition);

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                OnlineCatalogue_Slides.this.dismiss();
            }
        });

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        //  displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
//            Toast.makeText(getActivity(),
//                    "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(getActivity(),
                    "Selected page position: " + position,"");

            vdialogPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                    if (vdialogEdit.getText().toString().equalsIgnoreCase("")) {
                        vdialogEdit.setText(String.valueOf(1));
                    } else {
                        int s = Integer.parseInt(vdialogEdit.getText().toString()) + 1;
                        if (s <= 9999) {
                            vdialogEdit.setText(String.valueOf(Integer.parseInt(vdialogEdit.getText().toString()) + 1));
                        }
                    }
                }
            });
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

//            dialogMinus.setVisibility(View.GONE);
//            dialogPlus.setVisibility(View.GONE);
//            dialogEdit.setVisibility(View.GONE);
//            dialogMrp.setVisibility(View.GONE);
//            dialogRp.setVisibility(View.GONE);
//            dialogPrdname.setVisibility(View.GONE);
//            dialogSave.setVisibility(View.GONE);
            //dialogClose.setVisibility(View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
//        lblCount.setText((position + 1) + " of " + images.size());

        Catalogue_model image = images.get(position);

//        dialogRp.setText("RP : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));
//        dialogPid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_number()));
//        dialogEdit.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_quantity()));
//        dialogPrdname.setText("PRODUCT : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_name()));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.onlinecatalogue_slids, container, false);

            final_posion = position;

            // dialogMinus.setVisibility(View.GONE);
            // dialogPlus.setVisibility(View.GONE);
            // dialogEdit.setVisibility(View.GONE);
            // dialogMrp.setVisibility(View.GONE);
            // dialogRp.setVisibility(View.GONE);
            // dialogPrdname.setVisibility(View.GONE);
            // dialogSave.setVisibility(View.GONE);

//
            //ImageView imageViewPreview = (ImageView) view.findViewById(R.id.vdialog_img);
            image = images.get(position);

            vdialogText = (TextView) view.findViewById(R.id.dialog_text);
            //ImageView dialogImage = (ImageView) view.findViewById(R.id.dialog_img);
            vdialogClose = (ImageView) view.findViewById(R.id.close_btn);
            vdialogPlus = (ImageView) view.findViewById(R.id.plus_dialog);
            ImageView vdialogMinus = (ImageView) view.findViewById(R.id.minus_dialog);
            vdialogEdit = (EditText) view.findViewById(R.id.editval_dialog);
            vdialogMrp = (TextView) view.findViewById(R.id.mrp_dialog);
            vdialogRp = (TextView) view.findViewById(R.id.rp_dialog);
            price_dialog = (TextView) view.findViewById(R.id.price_dialog);
            vpp_mrp = (TextView) view.findViewById(R.id.pp_mrp);
            vpp_rp = (TextView) view.findViewById(R.id.pp_rp);

            vdialogPid = (TextView) view.findViewById(R.id.cat_pid_dialog);
            vdialogPrdname = (TextView) view.findViewById(R.id.prodname_doalog);
            vdialogSave = (Button) view.findViewById(R.id.online_catalog_dialog_save);

            vdialogMrp.setText("MRP : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_mrp()));
            vdialogRp.setText("RP : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_rp()));
            vdialogPid.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_number()));
            vdialogEdit.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_quantity()));
            vdialogPrdname.setText("PRODUCT : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_name()));
            price_dialog.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(image.getItem_amount()));

//            try {
//                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(image.getItem_image_url())) {
//                    Glide.with(getActivity()).load(R.drawable.img_not_found).crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imageViewPreview);
//                } else {
//
//                    Glide.with(getActivity()).load(image.getItem_image_url())
//                            .thumbnail(Glide.with(getActivity()).load("file:///android_asset/loading.gif"))
//                            .crossFade()
//                            .error(R.drawable.img_not_found)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imageViewPreview);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Glide.with(getActivity()).load(R.drawable.img_not_found).crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(imageViewPreview);
//            }

            vdialogPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                    if (vdialogEdit.getText().toString().equalsIgnoreCase("")) {
                        vdialogEdit.setText(String.valueOf(1));
                    } else {
                        int s = Integer.parseInt(vdialogEdit.getText().toString()) + 1;
                        if (s <= 9999) {
                            vdialogEdit.setText(String.valueOf(Integer.parseInt(vdialogEdit.getText().toString()) + 1));
                        }
                    }
                }
            });

            vdialogMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(v.getContext(), "You Clicked"+holder.stock_addno1.getText().toString(), Toast.LENGTH_SHORT).show();

                    if (!(vdialogEdit.getText().toString().equalsIgnoreCase("")) && !(Integer.parseInt(vdialogEdit.getText().toString()) <= 0)) {
                        vdialogEdit.setText(String.valueOf(Integer.parseInt(vdialogEdit.getText().toString()) - 1));
                    }
                }
            });

            vdialogEdit.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View p_v, MotionEvent p_event) {
                    // this will disallow the touch request for parent scroll on touch of child view
                    p_v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });


            vdialogEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    HashMap<String, String> edit = new HashMap<>();

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(vdialogEdit.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image.getItem_mrp())) {
                        // edit.put("string", s.toString());

                        try {
                            Double value = Double.valueOf(vdialogEdit.getText().toString()) * Double.valueOf(image.getItem_mrp());
                            price_dialog.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(final_posion + "&" + vdialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + vdialogText.getText().toString() + "pmrp" + image.getItem_mrp() + "prp" + image.getItem_rp());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    } else {
                        price_dialog.setText("");
                        Global_Data.Order_hashmap.put(final_posion + "&" + vdialogPid.getText().toString(), "");
                    }
                }
            });



//
//
//
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
