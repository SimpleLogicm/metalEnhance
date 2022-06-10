package com.msimplelogic.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.FeedbackDynamicModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterFeedbackDynamic extends
        RecyclerView.Adapter<AdapterFeedbackDynamic.ViewHolder> {
    ArrayList radio_arr = new ArrayList();
    private List<FeedbackDynamicModel> packageList;
    private Context context;
    private RadioGroup lastCheckedRadioGroup = null;

    public AdapterFeedbackDynamic(List<FeedbackDynamicModel> packageListIn, Context ctx) {
        packageList = packageListIn;
        context = ctx;
    }

    @Override
    public AdapterFeedbackDynamic.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_feddbackdynamic, parent, false);

        AdapterFeedbackDynamic.ViewHolder viewHolder =
                new AdapterFeedbackDynamic.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterFeedbackDynamic.ViewHolder holder,
                                 final int position) {
        FeedbackDynamicModel packageModel = packageList.get(position);

        if (packageModel.getQuestion_type().equalsIgnoreCase("radio_button"))
        {
            holder.radioLout.setVisibility(View.VISIBLE);
            holder.packageName.setText(packageModel.all_english_tv_channels);
            int id = (position + 1) * 100;
            for (String price : packageModel.getPriceList()) {
                RadioButton rb = new RadioButton(AdapterFeedbackDynamic.this.context);
                rb.setId(id++);
                if (Build.VERSION.SDK_INT >= 21) {
                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{
                                    new int[]{-android.R.attr.state_enabled}, //disabled
                                    new int[]{android.R.attr.state_enabled} //enabled
                            },
                            new int[]{
                                    Color.BLACK //disabled
                                    , Color.parseColor("#075b97") //enabled
                            }
                    );
                    rb.setButtonTintList(colorStateList);//set the color tint list
                    rb.invalidate(); //could not be necessary
                }
                // rb.setHighlightColor(Color.parseColor("#075b97"));
                rb.setText(price);
                holder.priceGroup.addView(rb);
            }
        } else if (packageModel.getQuestion_type().equalsIgnoreCase("emoji")) {
            holder.optionFouremogiLayout.setVisibility(View.VISIBLE);
            holder.typeFouremojiQue.setText(packageModel.all_english_tv_channels);

            holder.typefour_option1_txt.setText(packageModel.getPriceList().get(0));
            holder.typefour_option2_txt.setText(packageModel.getPriceList().get(1));
            holder.typefour_option3_txt.setText(packageModel.getPriceList().get(2));
            holder.typefour_option4_txt.setText(packageModel.getPriceList().get(3));
            //holder.packageName.setText(packageModel.all_english_tv_channels);

        } else if (packageModel.getQuestion_type().equalsIgnoreCase("text_area")) {
            holder.editextLayout.setVisibility(View.VISIBLE);
            holder.typeEditQue.setText(packageModel.all_english_tv_channels);
        } else if (packageModel.getQuestion_type().equalsIgnoreCase("number")) {
            holder.editextNoLayout.setVisibility(View.VISIBLE);
            holder.typeEditNoQue.setText(packageModel.all_english_tv_channels);
        } else if (packageModel.getQuestion_type().equalsIgnoreCase("check_box")) {
            holder.checkboxLayout.setVisibility(View.VISIBLE);
            holder.typeCheckboxQue.setText(packageModel.all_english_tv_channels);
        }

        //for check_box
//          holder.typefour_option1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.typefour_option1.setBackgroundResource(R.drawable.card_border_black);
//                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
//                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
//                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);
//                //Global_Data.Custom_Toast(context,holder.typefour_option1_txt.getText().toString(),"Yes");
//               // Global_Data.feedbackValues.add(holder.typefour_option1_txt.getText().toString());
//                //selectedAnswers.set(position, holder.typefour_option1_txt.getText().toString());
//
//                Global_Data.multicheck_arr.add(holder.typefour_option1_txt.getText().toString());
//                Global_Data.multicheck_arr.add(holder.typefour_option2_txt.getText().toString());
//                Global_Data.multicheck_arr.add(holder.typefour_option3_txt.getText().toString());
//
//                Global_Data.quastionFeedback.put(packageModel.getQuestion_id()+":"+packageModel.getAll_english_tv_channels(),Global_Data.multicheck_arr.toString());
//            }
//        });


        holder.typefour_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.card_border_black);
                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);

                Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), holder.typefour_option1_txt.getText().toString());
            }
        });


        holder.typefour_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option2.setBackgroundResource(R.drawable.card_border_black);
                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), holder.typefour_option2_txt.getText().toString());

//                Global_Data.Custom_Toast(context,holder.typefour_option2_txt.getText().toString(),"Yes");
            }
        });

        holder.typefour_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option3.setBackgroundResource(R.drawable.card_border_black);
                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), holder.typefour_option3_txt.getText().toString());

            }
        });

        holder.typefour_option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option4.setBackgroundResource(R.drawable.card_border_black);
                Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), holder.typefour_option4_txt.getText().toString());

                //selectedAnswers.set(position, holder.typefour_option4_txt.getText().toString());
            }
        });

        holder.editextAnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), holder.editextAnset.getText().toString());

            }
        });

        holder.editextNoAnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), holder.editextNoAnset.getText().toString());

            }
        });


        holder.priceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) rg.getChildAt(i);
                    if (btn.getId() == checkedId) {
                        String text = btn.getText().toString();

                        //  btn.setHighlightColor(Color.RED);
                        //  btn.setHighlightColor(Color.parseColor("#000000"));
//                        Toast.makeText(AdapterFeedbackDynamic.this.context,
//                                "Radio button clicked " + text+":"+packageModel.getQuestion_id(),
//                                Toast.LENGTH_SHORT).show();
//                        radio_arr.clear();
//                        radio_arr.add(text);
                        Global_Data.quastionFeedback.put(packageModel.getQuestion_id() + ":" + packageModel.getAll_english_tv_channels(), text);

                        // do something with text
                        return;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView packageName, typeFouremojiQue, typefour_option1_txt, typefour_option2_txt, typefour_option3_txt, typefour_option4_txt, typeEditQue, typeEditNoQue, typeCheckboxQue;
        public RadioGroup priceGroup;
        private LinearLayout radioLout, optionFouremogiLayout;
        LinearLayout typefour_option1;
        LinearLayout typefour_option2;
        LinearLayout typefour_option3;
        LinearLayout typefour_option4;
        RelativeLayout checkboxLayout;
        LinearLayout editextLayout, editextNoLayout;
        EditText editextAnset, editextNoAnset;
        CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;

        public ViewHolder(View view) {
            super(view);
            packageName = (TextView) view.findViewById(R.id.package_name);
            typeFouremojiQue = (TextView) view.findViewById(R.id.typefouremoji_que);
            typeEditQue = (TextView) view.findViewById(R.id.editext_quetv);
            typeEditNoQue = (TextView) view.findViewById(R.id.editextnumber_que);
            typeCheckboxQue = (TextView) view.findViewById(R.id.checkbox_question);
            priceGroup = (RadioGroup) view.findViewById(R.id.price_grp);
            radioLout = (LinearLayout) view.findViewById(R.id.radio_lout);
            editextLayout = (LinearLayout) view.findViewById(R.id.editext_layout);
            optionFouremogiLayout = (LinearLayout) view.findViewById(R.id.optionfouremogi_layout);
            editextNoLayout = (LinearLayout) view.findViewById(R.id.editextnumber_layout);
            checkboxLayout = view.findViewById(R.id.checkbox_layout);
            typefour_option1_txt = (TextView) view.findViewById(R.id.typefour_option1_txt);
            typefour_option2_txt = (TextView) view.findViewById(R.id.typefour_option2_txt);
            typefour_option3_txt = (TextView) view.findViewById(R.id.typefour_option3_txt);
            typefour_option4_txt = (TextView) view.findViewById(R.id.typefour_option4_txt);
            typefour_option1 = view.findViewById(R.id.typefour_option1);
            typefour_option2 = view.findViewById(R.id.typefour_option2);
            typefour_option3 = view.findViewById(R.id.typefour_option3);
            typefour_option4 = view.findViewById(R.id.typefour_option4);
            editextAnset = view.findViewById(R.id.editext_anset);
            editextNoAnset = view.findViewById(R.id.editextnumber_anset);
            checkBox1 = view.findViewById(R.id.checkbox1);
            checkBox2 = view.findViewById(R.id.checkbox2);
            checkBox3 = view.findViewById(R.id.checkbox3);
            checkBox4 = view.findViewById(R.id.checkbox4);
            checkBox5 = view.findViewById(R.id.checkbox5);

        }
    }
}