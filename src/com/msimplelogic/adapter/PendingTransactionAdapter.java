package com.msimplelogic.adapter;

/**
 * Created by sujit on 11/20/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msimplelogic.activities.Cash_Submit;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.CollectionItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PendingTransactionAdapter extends RecyclerView.Adapter<PendingTransactionAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<CollectionItem> itemList;
    Context context;

    // Constructor of the class
    public PendingTransactionAdapter(Context con, int layoutId, ArrayList<CollectionItem> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = con;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_collect_state, parent, false);
        // View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(itemList.get(listPosition).getText1())) {
            if (holder.timer != null) {
                holder.timer.cancel();
            }
            holder.Text1.setText(itemList.get(listPosition).getText1());

            try {

                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                // Date date = simpleDateFormat.parse((itemList.get(listPosition).getText1()));
                //  System.out.println("date : " + simpleDateFormat.format(date));
                //  long service_date_time = date.getTime();

                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String today1 = format.format(new Date());
                Date today = new Date(today1);
                final long currentTime = today.getTime();

                // String today1 = format.format(System.currentTimeMillis()+15*60*1000);
                Date service_plusf = format.parse((itemList.get(listPosition).getText1()));

                Calendar cal = Calendar.getInstance();
                cal.setTime(service_plusf);
                cal.add(Calendar.MINUTE, 15);
                Date new_date = cal.getTime();

                final long s_time = new_date.getTime();
                long expiryTime = s_time - currentTime;

                holder.timer = new CountDownTimer(expiryTime, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //holder.Text1.setText("" + millisUntilFinished/1000 + " Sec");

                        holder.Text1.setText("" + String.format("%d:%d mins",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        holder.Text1.setText("00:00:00");
                        Intent intent = new Intent(context, Cash_Submit.class);
                        Global_Data.GlobeloPname = itemList.get(listPosition).getText2();
                        Global_Data.GlobeloPAmount = itemList.get(listPosition).getText3();
                        context.startActivity(intent);
                    }
                }.start();

            } catch (ParseException e) {
                e.printStackTrace();
            }









//            CountDownTimer countDownTimer = new CountDownTimer(Integer.valueOf(itemList.get(listPosition).getText1()), 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    holder.Text1.setText(""+String.format("%d:%d mins",
//                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
//                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//                }
//
//                public void onFinish() {
//                    holder.Text1.setText("done!");
//                }
//            }.start();
        } else {
            holder.Text1.setText("");
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(itemList.get(listPosition).getText2())) {
            holder.Text2.setText(itemList.get(listPosition).getText2());
        } else {
            holder.Text2.setText("");
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(itemList.get(listPosition).getText3())) {
            holder.Text3.setText(itemList.get(listPosition).getText3());
        } else {
            holder.Text3.setText("");
        }
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Text1, Text2, Text3;
        CountDownTimer timer;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Text1 = (TextView) itemView.findViewById(R.id.text1);
            Text2 = (TextView) itemView.findViewById(R.id.text2);
            Text3 = (TextView) itemView.findViewById(R.id.text3);
        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + Text1.getText());
        }
    }
}