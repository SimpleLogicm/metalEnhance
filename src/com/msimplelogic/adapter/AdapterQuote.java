package com.msimplelogic.adapter;

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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Item_Edit_Activity;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.PreviewOrderSwipeActivity;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.swipelistview.SwipeListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterQuote extends ArrayAdapter<HashMap<String, String>>{
    private List<Catalogue_model> catalogue_m;
    customButtonListener customListner;
    ViewHolder viewHolder;
    ArrayList<String> Amount_tpp = new ArrayList<String>();
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    static final String TAG_PRODUCT_IMAGE = "product_image";
    static final String TAG_MIN_QTY = "product_min_qty";
    static final String TAG_PKG_QTY = "product_pkg_qty";
    static final String TAG_ITEM_SCHEME = "product_scheme";
    String TAG_ITEM_DETAIL1 = "detail1";
    String TAG_ITEM_DETAIL2 = "detail2";
    String TAG_ITEM_DETAIL3 = "detail3";
    String TAG_ITEM_DETAIL4 = "detail4";
    String TAG_ITEM_DETAIL5 = "detail5";
    String TAG_ITEM_DETAIL6 = "detail6";
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
    public AdapterQuote(Context context, ArrayList<HashMap<String, String>> dataItem1) {
        super(context, R.layout.adapter_quote, dataItem1);
        this.dataAray = dataItem1;
        this.context = context;
        this.catalogue_m = catalogue_m;
    }

//    public PackageAdapter(Context context, ArrayList<HashMap<String, String>> dataItem1) {
//        super(context, R.layout.package_row, dataItem1);
//        this.dataAray = dataItem1;
//        this.context = context;
//      //  this.catalogue_m = catalogue_m;
//    }

//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Product getItem(int position) {
//        return data.get(position);
//    }

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
            convertView = li.inflate(R.layout.adapter_quote, parent, false);
            holder = new ViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.example_row_tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.example_row_tv_description);
            holder.tvPriece = (TextView) convertView.findViewById(R.id.example_row_tv_price);
            holder.order_idn = (TextView) convertView.findViewById(R.id.order_idn);
            holder.bAction1 = (ImageView) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (ImageView) convertView.findViewById(R.id.example_row_b_action_2);
            // holder.bAction4 = (ImageView) convertView.findViewById(R.id.example_row_b_action_4);
            holder.bAction3 = (ImageView) convertView.findViewById(R.id.example_row_b_action_3);
            holder.previewSchemeBtn = (ImageView) convertView.findViewById(R.id.preview_schemebtn);
            holder.spv = context.getSharedPreferences("valuesset", 0);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

      //  ((SwipeListView)parent).recycle(convertView, position);
        getData = dataAray.get(position);
        //catalogue_mm = catalogue_m.get(position);

        dbvoc = new DataBaseHelper(context);

        if (Global_Data.CatlogueStatus.equalsIgnoreCase("online")) {
            holder.bAction3.setVisibility(View.VISIBLE);
        } else {
            holder.bAction3.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(getData.get(TAG_ITEMNAME));
        holder.tvDescription.setText("Quantity: "+getData.get(TAG_QTY));
        holder.tvPriece.setText("Price: ₹ "+getData.get(TAG_PRICE));
        holder.order_idn.setText(getData.get(TAG_ITEM_NUMBER));

        if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status")){
            Global_Data.orderListStatus="";
            holder.bAction1.setVisibility(View.INVISIBLE);
            holder.bAction2.setVisibility(View.INVISIBLE);
            holder.bAction3.setVisibility(View.INVISIBLE);
        }

        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	getData = dataAray.get(position);
				 Log.d("ITEM_NUMBER", "ITEM_NUMBER"+getData.get(TAG_ITEM_NUMBER).toString());
				 dbvoc = new DataBaseHelper(context);
				 List<Local_Data> cont1 = dbvoc.Get_OrderProducts_BYITEM_NUMBER(getData.get(TAG_ITEM_NUMBER).toString(),Global_Data.GLObalOrder_id);
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
                    Global_Data.SCHE_CODE = cnp.getSche_code();
                      Global_Data.e_remarks = cnp.getRemarks();
                      Global_Data.e_detail1 = cnp.getDetail1();
                      Global_Data.e_detail2 = cnp.getDetail2();
                      Global_Data.e_detail3 = cnp.getDetail3();
                      Global_Data.e_detail4 = cnp.getDetail4();
                      Global_Data.e_detail5 = cnp.getDetail5();
	   	          }

	   	       Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
			  Intent goToNewOrderActivity = new Intent(context, Item_Edit_Activity.class);
			  goToNewOrderActivity.putExtra("SCHEME_ID", Global_Data.SCHE_CODE);
           	  //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
           	  context.startActivity(goToNewOrderActivity);
            }
        });

        holder.bAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog openDialog = new Dialog(v.getContext());
                openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                openDialog.setCancelable(false);
                openDialog.setContentView(R.layout.product_details);

                final TextView dialogText = (TextView) openDialog.findViewById(R.id.dialog_text);
                ImageView dialogClose = (ImageView) openDialog.findViewById(R.id.close_btn);
                ImageView dialogImage = (ImageView) openDialog.findViewById(R.id.swipe_img);
                final TextView dialogProductanme = (TextView) openDialog.findViewById(R.id.product_name_swipedialog);
                final TextView dialogPrice = (TextView) openDialog.findViewById(R.id.price_swipedialog);
                final TextView dialogMinqty = (TextView) openDialog.findViewById(R.id.minqty_swipedialog);
                final TextView dialogPackqty = (TextView) openDialog.findViewById(R.id.packqty_swipedialog);
                final TextView dialogSchemename = (TextView) openDialog.findViewById(R.id.schemename_swipedialog);
                final TextView detail1_swipedialog = (TextView) openDialog.findViewById(R.id.detail1_swipedialog);
                final TextView detail2_swipedialog = (TextView) openDialog.findViewById(R.id.detail2_swipedialog);
                final TextView detail3_swipedialog = (TextView) openDialog.findViewById(R.id.detail3_swipedialog);
                final TextView detail4_swipedialog = (TextView) openDialog.findViewById(R.id.detail4_swipedialog);
                final TextView detail5_swipedialog = (TextView) openDialog.findViewById(R.id.detail5_swipedialog);
                final TextView comments_swipedialog = (TextView) openDialog.findViewById(R.id.comments_swipedialog);
                final Button dialogOk = (Button) openDialog.findViewById(R.id.dialog_okbtn);

                getData = dataAray.get(position);

                Log.d("ITEM_NUMBER", "ITEM_NUMBER" + getData.get(TAG_ITEM_NUMBER).toString());

                try {
                    String s = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PRODUCT_IMAGE));
                    if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {
                        Glide.with(context).load(R.drawable.img_not_found)
                                //.crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(dialogImage);
                    } else {
                        Glide.with(context).load(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PRODUCT_IMAGE)))
                                .thumbnail(Glide.with(context).load("file:///android_asset/loading.gif"))
                                // .crossFade()
                                .error(R.drawable.img_not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(dialogImage);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Glide.with(context).load(R.drawable.img_not_found)
                            //.crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(dialogImage);
                }

//				 String total_qty = "";
//				 String MRP = "";
//				 String amount = "";
//				 String scheme_amount = "";
//				 String actual_discount = "";
//				 String product_dec = "";
                dbvoc = new DataBaseHelper(context);
                List<Local_Data> cont1 = dbvoc.Get_OrderProducts_BYITEM_NUMBER(getData.get(TAG_ITEM_NUMBER).toString(), Global_Data.GLObalOrder_id);
                for (Local_Data cnp : cont1) {
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
                    Global_Data.SCHE_CODE = cnp.getSche_code();
                    TAG_ITEM_DETAIL1 = cnp.getRemarks();
                    TAG_ITEM_DETAIL2 = cnp.getDetail1();
                    TAG_ITEM_DETAIL3 = cnp.getDetail2();
                    TAG_ITEM_DETAIL4 = cnp.getDetail3();
                    TAG_ITEM_DETAIL5 = cnp.getDetail4();
                    TAG_ITEM_DETAIL6 = cnp.getDetail5();
                }

                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";

                dialogProductanme.setText(context.getResources().getString(R.string.Product_Name) + Global_Data.product_dec);
                dialogPrice.setText(context.getResources().getString(R.string.VPrice) + Global_Data.amount);

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(getData.get(TAG_MIN_QTY))) {
                    dialogMinqty.setText(getData.get(TAG_MIN_QTY));
                } else {
                    dialogMinqty.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(getData.get(TAG_PKG_QTY))) {
                    dialogPackqty.setText(getData.get(TAG_PKG_QTY));
                } else {
                    dialogPackqty.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(getData.get(TAG_ITEM_SCHEME))) {
                    dialogSchemename.setText(getData.get(TAG_ITEM_SCHEME));
                } else {
                    dialogSchemename.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(TAG_ITEM_DETAIL1)) {
                    comments_swipedialog.setText(context.getResources().getString(R.string.Product_Remarks) + TAG_ITEM_DETAIL1);
                } else {
                    comments_swipedialog.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(TAG_ITEM_DETAIL2)) {
                    detail1_swipedialog.setText(context.getResources().getString(R.string.Product_Detail1) + TAG_ITEM_DETAIL2);
                } else {
                    detail1_swipedialog.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(TAG_ITEM_DETAIL3)) {
                    detail2_swipedialog.setText(context.getResources().getString(R.string.Product_Detail2) + TAG_ITEM_DETAIL3);
                } else {
                    detail2_swipedialog.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(TAG_ITEM_DETAIL4)) {
                    detail3_swipedialog.setText(context.getResources().getString(R.string.Product_Detail3) + TAG_ITEM_DETAIL4);
                } else {
                    detail3_swipedialog.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(TAG_ITEM_DETAIL5)) {
                    detail4_swipedialog.setText(context.getResources().getString(R.string.Product_Detail4) + TAG_ITEM_DETAIL5);
                } else {
                    detail4_swipedialog.setVisibility(View.GONE);
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(TAG_ITEM_DETAIL6)) {
                    detail5_swipedialog.setText(context.getResources().getString(R.string.Product_Detail5) + TAG_ITEM_DETAIL6);
                } else {
                    detail5_swipedialog.setVisibility(View.GONE);
                }

                dialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        openDialog.dismiss();
                    }
                });

                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        openDialog.dismiss();
                    }
                });

                openDialog.show();
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
						 dbvoc.getDeleteTableorderproduct_byITEM_NUMBER(getData.get(TAG_ITEM_NUMBER).toString(),Global_Data.GLObalOrder_id);
						 dataAray.remove(position);
	                     notifyDataSetChanged();

	                      if(dataAray.size() <= 0)
	                      {
	                    	  Intent goToNewOrderActivity = new Intent(context, Neworderoptions.class);
	                    	  ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	                    	  //Intent order_home = new Intent(context,PreviewOrderSwipeActivity.class);
	                    	   context.startActivity(goToNewOrderActivity);
	                      }
	                      else
	                      {
	                    	  Double sum = 0.0;
	                    	  //dbvoc = new DataBaseHelper(context);
	                    	  List<Local_Data> cont1 = dbvoc.getItemName(Global_Data.GLObalOrder_id);
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

	                    	  PreviewOrderSwipeActivity.updateSum(sum);
                              Intent goToNewOrderActivity = new Intent(context,PreviewOrderSwipeActivity.class);
                              context.startActivity(goToNewOrderActivity);
                          }


							Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Item_Deleted),"yes");

						String order="";
						SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);

					     order=sp.getString("order", "");
					     dialog.cancel();
                    }
				});

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

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
        ImageView bAction1;
        ImageView bAction2;
        ImageView bAction3;
        ImageView previewSchemeBtn;
        String schemeName,schemeType,schemeAmount,schemeDesc,schemeBuyQty,schemeGetQty,schemeMinQty;
        SharedPreferences spv;
    }

}
