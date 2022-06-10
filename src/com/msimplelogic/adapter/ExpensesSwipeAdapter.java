package com.msimplelogic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.swipelistview.SwipeListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpensesSwipeAdapter extends ArrayAdapter<HashMap<String, String>>{
    private List<Catalogue_model> catalogue_m;
    customButtonListener customListner;
    ViewHolder viewHolder;
    ArrayList<String> Amount_tpp = new ArrayList<String>();
    static final String TAG_ITEMNAME = "amount";
    static final String TAG_QTY = "status";
    static final String TAG_PRICE = "date";

    HashMap<String, String> getData = new HashMap<String, String>();
    private ArrayList<HashMap<String, String>> dataAray;
    HashMap<String, String> dataIthjem;
    DataBaseHelper dbvoc;
    Catalogue_model catalogue_mm = new Catalogue_model();

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, View v);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;

    //public PackageAdapter(Context context, ArrayList<HashMap<String, String>> dataItem1, List<Catalogue_model> catalogue_m) {
    public ExpensesSwipeAdapter(Context context, ArrayList<HashMap<String, String>> dataItem1) {
        super(context, R.layout.swipe_dumy, dataItem1);
        this.dataAray = dataItem1;
        this.context = context;
        //this.catalogue_m = catalogue_m;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (position == 2) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    @SuppressLint("ShowToast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final Product item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.swipe_dumy, parent, false);
            holder = new ViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.example_row_tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.example_row_tv_description);
            holder.tvPriece = (TextView) convertView.findViewById(R.id.example_row_tv_price);
            holder.order_idn = (TextView) convertView.findViewById(R.id.order_idn);
            holder.bAction1 = (ImageView) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (ImageView) convertView.findViewById(R.id.example_row_b_action_2);
            holder.selectImage = (ImageView) convertView.findViewById(R.id.select_image);
//            // holder.bAction4 = (ImageView) convertView.findViewById(R.id.example_row_b_action_4);
//            holder.bAction3 = (Button) convertView.findViewById(R.id.example_row_b_action_3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView)parent).recycle(convertView, position);

        getData = dataAray.get(position);

        holder.tvTitle.setText(getData.get(TAG_ITEMNAME));
        holder.tvDescription.setText(getData.get(TAG_QTY));
        holder.tvPriece.setText(getData.get(TAG_PRICE));
        holder.order_idn.setText(getData.get(TAG_ITEMNAME));

//        holder.bAction1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.bAction2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
//            }
//        });

//        if(Global_Data.ExpenseName.equalsIgnoreCase("Conveyance"))
//        {
//            Glide.with(context)
//                    //.load(horizontalGrocderyList.get(position).getProductImage())
//                    .load(R.drawable.conveyance)
//                    .thumbnail(0.5f)
//                    //.centerCrop()
//                    .placeholder(R.drawable.not_found)
//                    .error(R.drawable.not_found)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.selectImage);
//        }else if(Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous"))
//        {
//            Glide.with(context)
//                    //.load(horizontalGrocderyList.get(position).getProductImage())
//                    .load(R.drawable.miscllaneous)
//                    .thumbnail(0.5f)
//                    //.centerCrop()
//                    .placeholder(R.drawable.not_found)
//                    .error(R.drawable.not_found)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.selectImage);
//        }else if(Global_Data.ExpenseName.equalsIgnoreCase("Food"))
//        {
//            Glide.with(context)
//                    //.load(horizontalGrocderyList.get(position).getProductImage())
//                    .load(R.drawable.food)
//                    .thumbnail(0.5f)
//                    //.centerCrop()
//                    .placeholder(R.drawable.not_found)
//                    .error(R.drawable.not_found)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.selectImage);
//        }else if(Global_Data.ExpenseName.equalsIgnoreCase("Hotel"))
//        {
//            Glide.with(context)
//                    //.load(horizontalGrocderyList.get(position).getProductImage())
//                    .load(R.drawable.hotel)
//                    .thumbnail(0.5f)
//                    //.centerCrop()
//                    .placeholder(R.drawable.not_found)
//                    .error(R.drawable.not_found)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.selectImage);
//        }

//        if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status")){
//            Global_Data.orderListStatus="";
//            holder.bAction1.setVisibility(View.INVISIBLE);
//            holder.bAction2.setVisibility(View.INVISIBLE);
//            holder.bAction3.setVisibility(View.INVISIBLE);
//        }

//        holder.bAction1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getData = dataAray.get(position);
//                Log.d("ITEM_NUMBER", "ITEM_NUMBER"+getData.get(TAG_ITEM_NUMBER).toString());
//
//                dbvoc = new DataBaseHelper(context);
//                List<Local_Data> cont1 = dbvoc.Get_OrderProducts_BYITEM_NUMBER(getData.get(TAG_ITEM_NUMBER).toString(),Global_Data.GLObalOrder_id);
//                for (Local_Data cnp : cont1)
//                {
//                    // tem.put("order_number", cnp.get_category_code());
//                    //item.put("item_number", cnp.get_custadr());
//                    Global_Data.item_no = cnp.get_delivery_product_id();
//                    Global_Data.total_qty = cnp.get_stocks_product_quantity();
//                    Global_Data.MRP = cnp.getMRP();
//                    Global_Data.RP = cnp.getRP();
//                    Global_Data.amount = cnp.get_Claims_amount();
//                    Global_Data.scheme_amount = cnp.get_Target_Text();
//                    Global_Data.actual_discount = cnp.get_stocks_product_text();
//                    Global_Data.product_dec = cnp.get_product_status();
//                    Global_Data.SCHE_CODE = cnp.getSche_code();
//                    Global_Data.e_remarks = cnp.getRemarks();
//                    Global_Data.e_detail1 = cnp.getDetail1();
//                    Global_Data.e_detail2 = cnp.getDetail2();
//                    Global_Data.e_detail3 = cnp.getDetail3();
//                    Global_Data.e_detail4 = cnp.getDetail4();
//                    Global_Data.e_detail5 = cnp.getDetail5();
//
//                }
//
//                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
//                Intent goToNewOrderActivity = new Intent(context,Item_Edit_Activity.class);
//                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                goToNewOrderActivity.putExtra("SCHEME_ID", Global_Data.SCHE_CODE);
//                //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
//                context.startActivity(goToNewOrderActivity);
//
//            }
//        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle,order_idn;
        TextView tvDescription;
        TextView tvPriece;
        ImageView bAction1,bAction2,selectImage;


    }



}
