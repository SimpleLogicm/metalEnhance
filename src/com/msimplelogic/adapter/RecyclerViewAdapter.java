package com.msimplelogic.adapter;

/**
 * Created by sujit on 12/27/2017.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.MenuItems;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<MenuItems> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<MenuItems> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_items, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        holder.ItemName.setText(itemList.get(position).getName());
        //  holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());

        Glide.with(context).load(itemList.get(position).getPhoto()).into(holder.ItemImage);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
