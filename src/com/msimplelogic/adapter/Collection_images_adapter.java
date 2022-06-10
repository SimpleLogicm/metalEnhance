package com.msimplelogic.adapter;

/**
 * Created by admin on 15-09-2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;

import java.io.File;
import java.util.List;


public class Collection_images_adapter extends RecyclerView.Adapter<Collection_images_adapter.MyView> {

    private List<String> list;
    Context _context;
    Dialog dialogcustom;

    public class MyView extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView c_image_view;
        public CardView ccardview;
        TextView hidden_m_url;

        public MyView(View view) {
            super(view);

            c_image_view = (ImageView) view.findViewById(R.id.c_image_view);

            hidden_m_url = (TextView) view.findViewById(R.id.hidden_m_url);

            ccardview = (CardView) view.findViewById(R.id.ccardview);

            ccardview.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            image_zoom_dialog(hidden_m_url.getText().toString());
        }
    }


    public Collection_images_adapter(Context con, List<String> horizontalList) {
        this.list = horizontalList;
        _context = con;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_images_adapter, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        //holder.c_image_view.setText(list.get(position));
        // holder.c_image_view.setImageResource(R.drawable.logo);

        // Glide.with(_context).load(list.get(position)).into(holder.c_image_view);

        Glide.with(_context).load(list.get(position))
                .thumbnail(Glide.with(_context).load("file:///android_asset/loading.gif"))
                .fitCenter()
                // .crossFade()
                .into(holder.c_image_view);

        holder.hidden_m_url.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void image_zoom_dialog(final String hm_url) {
        dialogcustom = new Dialog(_context);
        dialogcustom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcustom.setContentView(R.layout.collection_image_dialog);


        ImageView Collection_zoom_image = (ImageView) dialogcustom.findViewById(R.id.Collection_zoom_image);

//        Glide.with(_context).load(hm_url)
//                .placeholder(R.drawable.loa)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(Collection_zoom_image);

        Glide.with(_context).load(hm_url)
                .thumbnail(Glide.with(_context).load("file:///android_asset/loading.gif"))
                .fitCenter()
                // .crossFade()
                .into(Collection_zoom_image);


        Button collection_zoom_delete = (Button) dialogcustom.findViewById(R.id.collection_zoom_delete);

        Button collection_zoom_ok = (Button) dialogcustom.findViewById(R.id.collection_zoom_ok);

        collection_zoom_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(hm_url);
                if (file.exists()) {
                    file.delete();
                }
                list.remove(hm_url);
                Global_Data.image_counter = list.size();
                notifyDataSetChanged();
                dialogcustom.dismiss();

            }

        });

        collection_zoom_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogcustom.dismiss();

            }

        });

        dialogcustom.setCancelable(false);
        dialogcustom.show();
    }

}
