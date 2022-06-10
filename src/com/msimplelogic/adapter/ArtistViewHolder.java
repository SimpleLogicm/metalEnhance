//package com.msimplelogic.adapter;
//
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.cardview.widget.CardView;
//
//import com.msimplelogic.activities.R;
//import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
//
//import io.grpc.Context;
//
//public class ArtistViewHolder extends ChildViewHolder {
//    private TextView childTextView;
//    private TextView childDate;
//    private CardView order_card;
//    Context context;
//
//    public ArtistViewHolder(View itemView) {
//        super(itemView);
//        childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
//        childDate = itemView.findViewById(R.id.date);
//        order_card=itemView.findViewById(R.id.order_card);
//
//
//        order_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int str = getAdapterPosition();
//            }
//        });
//
//
//    }
//
//    public void setArtistName(String name) {
//        childTextView.setText(name);
//    }
//
//    public void setChildDate(String date) {
//        childDate.setText(date);
//    }
//
//
//}
