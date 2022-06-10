package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;
import com.msimplelogic.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackageAdapterReturnOrder extends ArrayAdapter<HashMap<String, String>>{

	customButtonListener customListner;
    ViewHolder viewHolder;
    ArrayList<String> Amount_tpp = new ArrayList<String>();
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    HashMap<String, String> getData = new HashMap<String, String>();
    private ArrayList<HashMap<String, String>> dataAray;
    DataBaseHelper dbvoc;
    
    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, View v);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;

    public PackageAdapterReturnOrder(Context context, ArrayList<HashMap<String, String>> dataItem1) {
        super(context, R.layout.pac_return_order, dataItem1);
        this.dataAray = dataItem1;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ShowToast")
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final Product item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.package_row, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.example_row_tv_title); 
            holder.tvDescription = (TextView) convertView.findViewById(R.id.example_row_tv_description);
            holder.tvPriece = (TextView) convertView.findViewById(R.id.example_row_tv_price);
            holder.order_idn = (TextView) convertView.findViewById(R.id.order_idn);
            holder.bAction1 = (Button) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (Button) convertView.findViewById(R.id.example_row_b_action_2);
           
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView)parent).recycle(convertView, position);
        
        getData = dataAray.get(position);

        holder.tvTitle.setText(getData.get(TAG_ITEMNAME));
        holder.tvDescription.setText(getData.get(TAG_QTY));
        holder.tvPriece.setText(getData.get(TAG_PRICE));
        holder.order_idn.setText(getData.get(TAG_ITEM_NUMBER));
        

        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	getData = dataAray.get(position);
				 Log.d("ITEM_NUMBER", "ITEM_NUMBER"+getData.get(TAG_ITEM_NUMBER).toString());
				 
				 dbvoc = new DataBaseHelper(context);
				 List<Local_Data> cont1 = dbvoc.Get_OrderProducts_BYITEM_NUMBER_RETURN(getData.get(TAG_ITEM_NUMBER).toString(),Global_Data.GLObalOrder_id_return);      
	   	          for (Local_Data cnp : cont1) 
	   	          {
	   	        	 
	   	        	// tem.put("order_number", cnp.get_category_code());
	                 //item.put("item_number", cnp.get_custadr());
	   	        	Global_Data.item_no = cnp.get_delivery_product_id();
	   	        	Global_Data.total_qty = cnp.get_stocks_product_quantity();
	   	        	Global_Data.MRP = cnp.getMRP();
	   	        	Global_Data.RP = cnp.getRP();
	   	        	Global_Data.amount = cnp.get_Claims_amount();
	   	        	Global_Data.scheme_amount = cnp.get_Target_Text();
	   	        	Global_Data.actual_discount = cnp.get_stocks_product_text();
	   	        	Global_Data.product_dec = cnp.get_product_status();
	                 
	   	          }
			 
	   	       Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";   
			   Intent goToNewOrderActivity = new Intent(context,ReturnEditItem.class);
			   context.startActivity(goToNewOrderActivity);
           	
            }
        });

        holder.bAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (isPlayStoreInstalled()) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + item.getPackageName())));
                } */
            	
            	AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
                alertDialog.setTitle(context.getResources().getString(R.string.Warning));
                alertDialog.setMessage(context.getResources().getString(R.string.Product_Delete_dialog_message));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						 getData = dataAray.get(position);
						 Log.d("ITEM_NUMBER", "ITEM_NUMBER"+getData.get(TAG_ITEM_NUMBER).toString());
						
						 dbvoc = new DataBaseHelper(context);
						 dbvoc.getDeleteTableorderproduct_byITEM_NUMBER_return(getData.get(TAG_ITEM_NUMBER).toString(),Global_Data.GLObalOrder_id_return);
						 dataAray.remove(position);
	                      notifyDataSetChanged();
						 
	                      if(dataAray.size() <= 0)
	                      {
	                    	  Intent goToNewOrderActivity = new Intent(context,MainActivity.class);
	                    	  ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	                    	  //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
	                    	   context.startActivity(goToNewOrderActivity);  
	                      }
	                      else
	                      {
                              Double sum = 0.0;
	                    	  //dbvoc = new DataBaseHelper(context);
	                    	  List<Local_Data> cont1 = dbvoc.getItemName_return(Global_Data.GLObalOrder_id_return);      
	            	          for (Local_Data cnt1 : cont1) {
	            	        	 
	            	        	  Amount_tpp.add(cnt1.getAmount());
	            	        	 //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
	            	        	//Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
	            	        	 //SwipeList.add(mapp);
	            	          }
	            	          
	            	          for(int m=0; m<Amount_tpp.size(); m++)
	            	          {
	            	        	  sum += Double.valueOf(Amount_tpp.get(m));
	            	          }

	                    	  ReturnOrder2.updateSum(sum);
	            	          Intent goToNewOrderActivity = new Intent(context,ReturnOrder2.class);
	            	          context.startActivity(goToNewOrderActivity);
	                      }

//                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.Item_Deleted), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();

							Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Item_Deleted),"yes");
                        SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);

                        dialog.cancel();

						
					}
				});

               alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						  dialog.cancel();
					}
				});
			  
			    alertDialog.show(); 
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle,order_idn;
        TextView tvDescription;
        TextView tvPriece;
        Button bAction1;
        Button bAction2;
      
    }

   

}

