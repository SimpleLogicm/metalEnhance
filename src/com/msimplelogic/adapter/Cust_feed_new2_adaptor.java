package com.msimplelogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Cust_feed_new2_Model;
import java.util.List;

public class Cust_feed_new2_adaptor extends RecyclerView.Adapter<Cust_feed_new2_adaptor.MyViewHolder> {
    public Context context;
    public List<Cust_feed_new2_Model> array;
    private int lastSelectedPosition = -1;
    private int lastSelectedPosition1 = -1;

    public Cust_feed_new2_adaptor(Context context, List<Cust_feed_new2_Model> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cust_feed_new2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.radio_typetwo_yes.setChecked(lastSelectedPosition == position);
        holder.radio_typetwo_no.setChecked(lastSelectedPosition1 == position);

        if(array.get(position).type_que.equalsIgnoreCase("radio_button")) {
            holder.optiontwo_layout.setVisibility(View.VISIBLE);
            holder.optionfouremogi_layout.setVisibility(View.GONE);
            holder.typeone_layout.setVisibility(View.GONE);

            holder.typetwo_que.setText(array.get(position).title_que);
            holder.radio_typetwo_yes.setText(array.get(position).option1);
            holder.radio_typetwo_no.setText(array.get(position).option2);

//            holder.plusbtn_minus_btn_layout.setVisibility(View.GONE);
//            holder.noraml_edtext_layout.setVisibility(View.GONE);
        }else if(array.get(position).type_que.equalsIgnoreCase("emoji")) {
            holder.optiontwo_layout.setVisibility(View.GONE);
            holder.optionfouremogi_layout.setVisibility(View.VISIBLE);
            holder.typeone_layout.setVisibility(View.GONE);

            holder.typefouremoji_que.setText(array.get(position).title_que);
            holder.typefour_option1_txt.setText(array.get(position).option1);
            holder.typefour_option2_txt.setText(array.get(position).option2);
            holder.typefour_option3_txt.setText(array.get(position).option3);
            holder.typefour_option4_txt.setText(array.get(position).option4);

//            holder.plusbtn_minus_btn_layout.setVisibility(View.GONE);
//            holder.noraml_edtext_layout.setVisibility(View.GONE);
        }else if(array.get(position).type_que.equalsIgnoreCase("option1")) {
            holder.optiontwo_layout.setVisibility(View.GONE);
            holder.optionfouremogi_layout.setVisibility(View.GONE);
            holder.typeone_layout.setVisibility(View.VISIBLE);

            holder.typeone_que.setText(array.get(position).title_que);
//            holder.typefour_option1_txt.setText(array.get(position).option1);
//            holder.typefour_option2_txt.setText(array.get(position).option2);
//            holder.typefour_option3_txt.setText(array.get(position).option3);
//            holder.typefour_option4_txt.setText(array.get(position).option4);

//            holder.plusbtn_minus_btn_layout.setVisibility(View.GONE);
//            holder.noraml_edtext_layout.setVisibility(View.GONE);
        }

        holder.typefour_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.card_border_black);
                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                Global_Data.Custom_Toast(context,holder.typefour_option1_txt.getText().toString(),"Yes");
            }
        });

        holder.typefour_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option2.setBackgroundResource(R.drawable.card_border_black);
                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                Global_Data.Custom_Toast(context,holder.typefour_option2_txt.getText().toString(),"Yes");
            }
        });


        holder.typefour_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option3.setBackgroundResource(R.drawable.card_border_black);
                holder.typefour_option4.setBackgroundResource(R.drawable.background_transparent);
                Global_Data.Custom_Toast(context,holder.typefour_option3_txt.getText().toString(),"Yes");
            }
        });

        holder.typefour_option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.typefour_option1.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option2.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option3.setBackgroundResource(R.drawable.background_transparent);
                holder.typefour_option4.setBackgroundResource(R.drawable.card_border_black);
                Global_Data.Custom_Toast(context,holder.typefour_option4_txt.getText().toString(),"Yes");
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout optiontwo_layout;
        TextView typetwo_que,typefour_option1_txt,typefour_option2_txt,typefour_option3_txt,typefour_option4_txt;
        RadioGroup card1radiogroup;
        RadioButton radio_typetwo_yes;
        RadioButton radio_typetwo_no;
        LinearLayout optionfouremogi_layout;
        TextView typefouremoji_que;
        LinearLayout typefour_option1;
        LinearLayout typefour_option2;
        LinearLayout typefour_option3;
        LinearLayout typefour_option4;
        LinearLayout typeone_layout;
        TextView typeone_que;
        ImageView minus_btn;
        EditText typeone_editvalue;
        ImageView plus_btn;
        LinearLayout noraml_edtext_layout;
        TextView noraml_edtext_question_tv;
        EditText noraml_edtext_et;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            typefour_option1_txt = itemView.findViewById(R.id.typefour_option1_txt);
            typefour_option2_txt = itemView.findViewById(R.id.typefour_option2_txt);
            typefour_option3_txt = itemView.findViewById(R.id.typefour_option3_txt);
            typefour_option4_txt = itemView.findViewById(R.id.typefour_option4_txt);

            optiontwo_layout = itemView.findViewById(R.id.optiontwo_layout);
            typetwo_que = itemView.findViewById(R.id.typetwo_que);
            card1radiogroup = itemView.findViewById(R.id.card1radiogroup);
            radio_typetwo_yes = itemView.findViewById(R.id.radio_typetwo_yes);
            radio_typetwo_no = itemView.findViewById(R.id.radio_typetwo_no);
            optionfouremogi_layout = itemView.findViewById(R.id.optionfouremogi_layout);
            typefouremoji_que = itemView.findViewById(R.id.typefouremoji_que);
            typefour_option1 = itemView.findViewById(R.id.typefour_option1);
            typefour_option2 = itemView.findViewById(R.id.typefour_option2);
            typefour_option3 = itemView.findViewById(R.id.typefour_option3);
            typefour_option4 = itemView.findViewById(R.id.typefour_option4);
            typeone_layout = itemView.findViewById(R.id.typeone_layout);
            typeone_que = itemView.findViewById(R.id.typeone_que);
            minus_btn = itemView.findViewById(R.id.minus_btn);
            typeone_editvalue = itemView.findViewById(R.id.typeone_editvalue);
            plus_btn = itemView.findViewById(R.id.plus_btn);
            noraml_edtext_layout = itemView.findViewById(R.id.noraml_edtext_layout);
            noraml_edtext_question_tv = itemView.findViewById(R.id.noraml_edtext_question_tv);
            noraml_edtext_et = itemView.findViewById(R.id.noraml_edtext_et);

            radio_typetwo_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();


                    Global_Data.Custom_Toast(Cust_feed_new2_adaptor.this.context,
                            "selected offer is " + "yes","yes");
                }
            });

            radio_typetwo_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition1 = getAdapterPosition();
                    notifyDataSetChanged();

                    Global_Data.Custom_Toast(Cust_feed_new2_adaptor.this.context,
                            "selected offer is " + "no","yes");
                }
            });


        }

//        public void onRadioButtonClicked1(View v)
//        {
//            //is the current radio button now checked?
//            boolean  checked = ((RadioButton) v).isChecked();
//
//            //android switch statement
//            switch(v.getId()){
//                case R.id.radio_typetwo_yes:
//                    if(checked)
//                        Global_Data.copyTimesheetRadioValue="Yes";
//                    break;
//
//                case R.id.radio_typetwo_no:
//                    if(checked)
//                        Global_Data.copyTimesheetRadioValue="No";
//                    break;
//            }
//        }
    }

}