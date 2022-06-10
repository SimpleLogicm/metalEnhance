package com.msimplelogic.adapter;

/**
 * Created by sujit on 11/17/2017.
 */

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.CollectionItem;

import java.util.ArrayList;

public class CollectionStatementAdapter extends RecyclerView.Adapter<CollectionStatementAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<CollectionItem> itemList;

    // Constructor of the class
    public CollectionStatementAdapter(int layoutId, ArrayList<CollectionItem> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
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
            holder.Text1.setText(itemList.get(listPosition).getText1());
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