package com.msimplelogic.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.Geo_Data;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.MainActivity;
import com.msimplelogic.activities.R;
import com.msimplelogic.helper.ThemeView;
import com.msimplelogic.model.Theme;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private List<Theme> themeList;
    Context context;
    SharedPreferences sp;

    // private RecyclerViewClickListener mRecyclerViewClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ThemeView themeView;
        // private RecyclerViewClickListener mListener;

        public MyViewHolder(View view) {
            super(view);
            // mListener = listener;
            themeView = (ThemeView) view.findViewById(R.id.themeView);
            //view.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        sp = context.getSharedPreferences("SimpleLogic", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("CurrentTheme", Global_Data.mThemeList.get(getAdapterPosition()).getId());
                        editor.commit();
                        Global_Data.selectedTheme = getAdapterPosition();
                        themeView.setActivated(true);
                        ThemeAdapter.this.notifyDataSetChanged();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Intent i = new Intent(context, MainActivity.class);
                  //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(i);

                }
            });
        }

//        @Override
//        public void onClick(View view) {
//            mListener.onClick(view, getAdapterPosition());
//            ScrollingActivity.selectedTheme = getAdapterPosition();
//            ScrollingActivity.mTheme = ScrollingActivity.mThemeList.get(getAdapterPosition()).getId();
//            themeView.setActivated(true);
//            ThemeAdapter.this.notifyDataSetChanged();
//        }
    }


    public ThemeAdapter(List<Theme> themeList, Context mcontext) {
        this.themeList = themeList;
        context = mcontext;
        //mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_theme, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Theme theme = themeList.get(position);
        holder.themeView.setTheme(theme);

        if (Global_Data.selectedTheme == position) {
            holder.themeView.setActivated(true);
        }else {
            holder.themeView.setActivated(false);
        }


    }

    @Override
    public int getItemCount() {
        return themeList.size();
    }
}