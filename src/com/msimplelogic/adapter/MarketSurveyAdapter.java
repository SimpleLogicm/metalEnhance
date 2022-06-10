package com.msimplelogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.FeedbackDynamicModel;
import java.util.ArrayList;
import java.util.List;

public class MarketSurveyAdapter extends BaseAdapter {
    Context context;
    List<FeedbackDynamicModel> questionsList;
    LayoutInflater inflter;
    public static ArrayList<String> selectedAnswers;

    public MarketSurveyAdapter(Context applicationContext, List<FeedbackDynamicModel> packageListIn) {
        this.context = applicationContext;
        this.questionsList = packageListIn;
        // initialize arraylist and add static string for all the questions
        selectedAnswers = new ArrayList<>();

        for (int i = 0; i < questionsList.size(); i++) {
            selectedAnswers.add("Not Attempted");
        }
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return questionsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.adapter_marketsurvey, null);

        // get the reference of TextView and Button's
        TextView question = (TextView) view.findViewById(R.id.question);
        TextView typeFouremojiQue = (TextView) view.findViewById(R.id.typefouremoji_que);
        RadioButton yes = (RadioButton) view.findViewById(R.id.yes);
        RadioButton no = (RadioButton) view.findViewById(R.id.no);
        LinearLayout radioLout= (LinearLayout) view.findViewById(R.id.radio_lout);
        LinearLayout optionFouremogiLayout= (LinearLayout) view.findViewById(R.id.optionfouremogi_layout);
        TextView typefour_option1_txt = (TextView) view.findViewById(R.id.typefour_option1_txt);
        TextView typefour_option2_txt = (TextView) view.findViewById(R.id.typefour_option2_txt);
        TextView typefour_option3_txt = (TextView) view.findViewById(R.id.typefour_option3_txt);
        TextView typefour_option4_txt = (TextView) view.findViewById(R.id.typefour_option4_txt);

        LinearLayout typefour_option1 = view.findViewById(R.id.typefour_option1);
        LinearLayout typefour_option2 = view.findViewById(R.id.typefour_option2);
        LinearLayout typefour_option3 = view.findViewById(R.id.typefour_option3);
        LinearLayout typefour_option4 = view.findViewById(R.id.typefour_option4);

        FeedbackDynamicModel packageModel = questionsList.get(i);

        if(packageModel.getQuestion_type().equalsIgnoreCase("radio_button"))
        {
            radioLout.setVisibility(View.VISIBLE);
            question.setText(packageModel.getAll_english_tv_channels());
            // perform setOnCheckedChangeListener event on yes button
            yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set Yes values in ArrayList if RadioButton is checked
                    if (isChecked)
                        selectedAnswers.set(i, "Yes");
                }
            });
            // perform setOnCheckedChangeListener event on no button
            no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set No values in ArrayList if RadioButton is checked
                    if (isChecked)
                        selectedAnswers.set(i, "No");
                }
            });
        }else if(packageModel.getQuestion_type().equalsIgnoreCase("emoji")){
            optionFouremogiLayout.setVisibility(View.VISIBLE);
            typeFouremojiQue.setText(packageModel.all_english_tv_channels);

            typefour_option1_txt.setText(packageModel.getPriceList().get(0));
            typefour_option2_txt.setText(packageModel.getPriceList().get(1));
            typefour_option3_txt.setText(packageModel.getPriceList().get(2));
            typefour_option4_txt.setText(packageModel.getPriceList().get(3));

            typefour_option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   typefour_option1.setBackgroundResource(R.drawable.card_border_black);
                    typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                    Global_Data.Custom_Toast(context,typefour_option1_txt.getText().toString(),"Yes");
                    // Global_Data.feedbackValues.add(holder.typefour_option1_txt.getText().toString());

                    //String dsfdsf=packageModel.getPriceList().get(0);

                    //selectedAnswers.set(1, dsfdsf);
                }
            });

            typefour_option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option2.setBackgroundResource(R.drawable.card_border_black);
                    typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                    Global_Data.Custom_Toast(context,typefour_option2_txt.getText().toString(),"Yes");
                    selectedAnswers.set(i,packageModel.getPriceList().get(1));
                }
            });


           typefour_option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                   typefour_option3.setBackgroundResource(R.drawable.card_border_black);
                    typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                    Global_Data.Custom_Toast(context,typefour_option3_txt.getText().toString(),"Yes");
                    selectedAnswers.set(i, packageModel.getPriceList().get(2));
                }
            });

            typefour_option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                    typefour_option4.setBackgroundResource(R.drawable.card_border_black);
                    Global_Data.Custom_Toast(context,typefour_option4_txt.getText().toString(),"Yes");
                    selectedAnswers.set(i, packageModel.getPriceList().get(3));
                }
            });

            //holder.packageName.setText(packageModel.all_english_tv_channels);

        }
        //question.setText(questionsList.get(i));
        // set the value in TextView
        //question.setText(com.msimplelogic.model.FeedbackDynamicModel.get(i));
        return view;
    }
}