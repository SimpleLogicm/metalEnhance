package com.msimplelogic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.CatalogueSchemeModel;

import java.util.List;

public class AdapterCatalogueScheme extends RecyclerView.Adapter<AdapterCatalogueScheme.MyViewHolder> {

    private List<CatalogueSchemeModel> catalogueSchemeModel;
    private Context mContext;
    private UserAdapterListener listener;
    private UserAdapterListenernew userAdapterListenernew;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrderDate,tvOrderAmount,tvOutstanding;
        //public ImageView ann_icon;
        public LinearLayout ann_container;

        public MyViewHolder(View view) {
            super(view);
            tvOrderDate = view.findViewById(R.id.tv_orderdate);
            tvOrderAmount = view.findViewById(R.id.tv_orderamount);
            tvOutstanding = view.findViewById(R.id.tv_outstanding);
            //ann_container = view.findViewById(R.id.ann_container);
        }
    }

    public AdapterCatalogueScheme(Context context, List<CatalogueSchemeModel> catalogueSchemeModel) {
        this.mContext = context;
        this.catalogueSchemeModel = catalogueSchemeModel;
//        this.listener = listener;
//        this.userAdapterListenernew = userAdapterListenernew;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_catalogue_scheme, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CatalogueSchemeModel outstandingOverdueM = catalogueSchemeModel.get(position);

        holder.tvOrderDate.setText(outstandingOverdueM.getOrderDate());
        holder.tvOrderAmount.setText(outstandingOverdueM.getOrderAmount());
        holder.tvOutstanding.setText(outstandingOverdueM.getOutstanding());

        //annId=Ann_Model.getAnn_id();

//        Glide.with(mContext)
//                .load(R.drawable.search_icon)
//                //.thumbnail(0.5f)
//                //.centerCrop()
//                .placeholder(R.drawable.img_not_found)
//                .error(R.drawable.img_not_found)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .listener(new RequestListener<Drawable>() {
//                              @Override
//                              public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                                  return false;
//                              }
//
//                              @Override
//                              public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                  return false;
//                              }
//                          }
//                )
//                .into(holder.ann_icon);


//        holder.ann_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent m = new Intent(mContext, AnnouncementInfo.class);
//                m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                m.putExtra("ANN_ID", Ann_Model.getAnn_id());
////                m.putExtra("REGION_ID", list.getDescription());
//                mContext.startActivity(m);
//                // Toast.makeText(mContext, "dsf"+Ann_Model.getAnn_id(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (userAdapterListenernew != null) {
//                    //userAdapterListenernew.onUserClickedImag(Ann_Models.get(position),position);
//                }
//
//
//            }
//        });
//
//        holder.b_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (listener != null) {
//                    // listener.onUserClicked(Ann_Models.get(position),position);
//                }
//
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return catalogueSchemeModel.size();
    }

    public interface UserAdapterListener {
        // void onUserClicked(Ann_Model Ann_Model, int position);
    }

    public interface UserAdapterListenernew {
        //void onUserClickedImag(Ann_Model Ann_Model, int position);
    }

}

