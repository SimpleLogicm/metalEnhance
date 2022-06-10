package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.activities.kotlinFiles.Order_CustomerList;
import com.msimplelogic.activities.kotlinFiles.UpdateStockScreen2;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Sales_Dash extends BaseActivity implements OnItemSelectedListener{
	//Button retail_sales, institute_sales;
	ImageView retail_sales, institute_sales,customer_services,quote_status,schedule_listn,C_profile,expenses,marketing_data;
	ConnectionDetector cd;
	Boolean isInternetPresent = false;
	TextView schedule_txt,textView1sf;
	Toolbar toolbar;
	String schedulestr;
	LoginDataBaseAdapter loginDataBaseAdapter;
	DataBaseHelper dbvoc = new DataBaseHelper(this);
	ProgressDialog dialog;
	CardView bankcardId7;
	SharedPreferences spref;
	ImageView hedder_theame;
	TextView order_toolbar_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Global_Data.CUSTOMER_SERVICE_FLAG = "";
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		assert getSupportActionBar() != null;   //null check
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		 spref = getSharedPreferences("SimpleLogic", 0);
		String shopname = spref.getString("shopname", "");
		order_toolbar_title= findViewById(R.id.order_toolbar_title);
		order_toolbar_title.setText(shopname);


		expenses=findViewById(R.id.expenses);
		retail_sales = (ImageView) findViewById(R.id.retail_sales);
		institute_sales = (ImageView) findViewById(R.id.institute_sales);
		customer_services = (ImageView) findViewById(R.id.customer_services);
	//	quote_status = (ImageView) findViewById(R.id.new_survey);
		schedule_listn = (ImageView) findViewById(R.id.schedule_listn);
		C_profile = (ImageView) findViewById(R.id.C_profile);
		schedule_txt = (TextView) findViewById(R.id.schedule_txt);
		textView1sf = (TextView) findViewById(R.id.textView1sf);
		bankcardId7 = findViewById(R.id.bankcardId7);
		marketing_data = findViewById(R.id.marketing_data);
		hedder_theame=findViewById(R.id.hedder_theame);


		Global_Data.G_BEAT_IDC = "";
		Global_Data.G_BEAT_VALUEC = "";
		Global_Data.G_BEAT_service_flag = "";
		Global_Data.G_RadioG_valueC = "";
		Global_Data.q="";
		Global_Data.p_code.clear();
		Global_Data.p_mrp.clear();
		Global_Data.p_amount.clear();
		Global_Data.p_qty.clear();


		cd = new ConnectionDetector(getApplicationContext());


		sp = getSharedPreferences("SimpleLogic", 0);
		int current_theme = sp.getInt("CurrentTheme",0);

		if (current_theme== 1){
			retail_sales.setImageResource(R.drawable.order_home_dark);
			marketing_data.setImageResource(R.drawable.update_stock_dark);
			institute_sales.setImageResource(R.drawable.quote_home_dark);
			customer_services.setImageResource(R.drawable.custservices_home_dark);
			schedule_listn.setImageResource(R.drawable.deliveryschedule_home_dark);
			C_profile.setImageResource(R.drawable.custprofile_home_dark);
			hedder_theame.setImageResource(R.drawable.dark_hedder);

		}
		// for label change
		SharedPreferences spf1=this.getSharedPreferences("SimpleLogic",0);
		String schedulestr=spf1.getString("var_schedule", "");

		if(schedulestr.length()>0)
		{
			Log.d("App Language", "App Language " + Locale.getDefault().getDisplayLanguage());
			String locale = Locale.getDefault().getDisplayLanguage();
			if (locale.equalsIgnoreCase("English")) {
				schedule_txt.setText(schedulestr.toUpperCase());
			} else {
				schedule_txt.setText(getResources().getString(R.string.SCHEDULE));
			}

		}else{
			schedule_txt.setText(getResources().getString(R.string.SCHEDULE));
		}

		bankcardId7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Kot_Gloval.Companion.setStock_status("");
				Kot_Gloval.Companion.setStock_details("");
				Kot_Gloval.Companion.setStock_pick1("");
				Kot_Gloval.Companion.setStock__pick2("");
				Kot_Gloval.Companion.setStock__pick3("");
				Kot_Gloval.Companion.setStock_local_image_flag("");
				Global_Data.Number.clear();
				Global_Data.image_counter = 0;
				Global_Data.Order_hashmap.clear();
				Global_Data.CUSTOMER_SERVICE_FLAG = "" ;
				Global_Data.sales_btnstring="Secondary Sales / Retail Sales";
				Global_Data.updateStockStatus="yes";
				Intent intent = new Intent(getApplicationContext(), UpdateStockScreen2.class);
				startActivity(intent);
			}
		});

		retail_sales.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global_Data.CUSTOMER_SERVICE_FLAG = "" ;
				Global_Data.sales_btnstring="Secondary Sales / Retail Sales";
				Intent intent = new Intent(getApplicationContext(), Neworderoptions.class);
				startActivity(intent);

			}
		});

		C_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Customer_info_main.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		expenses.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Global_Data.CUSTOMER_SERVICE_FLAG = "ADD_RETAILER" ;
				Intent intent = new Intent(getApplicationContext(), Neworderoptions.class);
				startActivity(intent);
			}
		});

		institute_sales.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global_Data.sales_btnstring="Institutional Sales";
				Global_Data.CUSTOMER_SERVICE_FLAG = "QUOTE" ;
//				Intent intent = new Intent(getApplicationContext(), Order.class);
//				startActivity(intent);
				Intent intent = new Intent(getApplicationContext(), Neworderoptions.class);
				startActivity(intent);
			}
		});

		customer_services.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global_Data.sales_btnstring="Secondary Sales / Retail Sales";
				Global_Data.CUSTOMER_SERVICE_FLAG = "CUSTOMER_SERVICE" ;
				//Intent intent = new Intent(getApplicationContext(), Order.class);
				Intent intent = new Intent(getApplicationContext(), Neworderoptions.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

//		quote_status.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Global_Data.CUSTOMER_SERVICE_FLAG = "QUOTE_STATUS" ;
//				isInternetPresent = cd.isConnectingToInternet();
//				if (isInternetPresent)
//				{
//					Intent a = new Intent(Sales_Dash.this,Status_Act.class);
//					startActivity(a);
//					finish();
//				}
//				else
//				{
//					//Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//					Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//				}
//			}
//		});

		schedule_listn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global_Data.sales_btnstring="Secondary Sales / Retail Sales";
				Global_Data.CUSTOMER_SERVICE_FLAG = "SCHEDULE" ;

				isInternetPresent = cd.isConnectingToInternet();

				AlertDialog alertDialog = new AlertDialog.Builder(Sales_Dash.this).create(); //Read Update

				if(schedulestr.length()>0) {
					alertDialog.setTitle(schedulestr);
					alertDialog.setMessage(getResources().getString(R.string.view_dialog) + schedulestr + getResources().getString(R.string.offline_dialog));
				}else{
					alertDialog.setTitle(getResources().getString(R.string.Scheduleo));
					alertDialog.setMessage(getResources().getString(R.string.view_dialog) + schedulestr + getResources().getString(R.string.offline_dialog));
				}
				alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Online), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						if (isInternetPresent)
						{
							getScheduleData();
						}
						else
						{
							//Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//							Toast toast = Toast.makeText(Sales_Dash.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
							Global_Data.Custom_Toast(Sales_Dash.this, getResources().getString(R.string.internet_connection_error),"yes");
						}
					}
				});

				alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Offline), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						List<Local_Data> contacts3 = dbvoc.getSchedule_List(Global_Data.GLOvel_CUSTOMER_ID);

						if (contacts3.size() <= 0) {
							//Toast.makeText(Order.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

//							Toast toast = Toast.makeText(Sales_Dash.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
							Global_Data.Custom_Toast(Sales_Dash.this, getResources().getString(R.string.Sorry_No_Record_Found),"yes");
						}
						else
						{
							Global_Data.Schedule_FLAG = "CUSTOMER";
							Intent intent = new Intent(getApplicationContext(),
									Schedule_List.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							startActivity(intent);
							finish();

						}
						dialog.cancel();
					}
				});
				alertDialog.show();

//				Intent intent = new Intent(getApplicationContext(), Order.class);
//				startActivity(intent);
			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
							   long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
//		return super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
			case R.id.add:
				String targetNew="";
				SharedPreferences sp = Sales_Dash.this.getSharedPreferences("SimpleLogic", 0);
				try {
					int target = (int) Math.round(sp.getFloat("Target", 0));
					int achieved = (int) Math.round(sp.getFloat("Achived", 0));
					Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
					if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
						int age = (int) Math.round(age_float);
						if (Global_Data.rsstr.length() > 0) {
							targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
							//todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
						} else {
							targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
							//todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
						}
						//todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
					} else {
						int age = (int) Math.round(age_float);
						if (Global_Data.rsstr.length() > 0) {
							targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
							// todaysTarget.setText();
						} else {
							targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
							//todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
						}
						//todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				View yourView = findViewById(R.id.add);
				new SimpleTooltip.Builder(this)
						.anchorView(yourView)
						.text(targetNew)
						.gravity(Gravity.START)
						.animated(true)
						.transparentOverlay(false)
						.build()
						.show();

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();

		Intent i = new Intent(Sales_Dash.this, Order_CustomerList.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		startActivity(i);
		finish();
	}


	public void getScheduleData()
	{

		//calendarn = Calendar.getInstance();
		//year = calendarn.get(Calendar.YEAR);
		loginDataBaseAdapter=new LoginDataBaseAdapter(Sales_Dash.this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		dialog = new ProgressDialog(Sales_Dash.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        // for label change
        SharedPreferences spf1=this.getSharedPreferences("SimpleLogic",0);
        String schedulestr=spf1.getString("var_schedule", "");

		if(schedulestr.length()>0)
		{
			dialog.setMessage(getResources().getString(R.string.Please_Wait) + schedulestr + getResources().getString(R.string.Synco));
		}else{
			dialog.setMessage(getResources().getString(R.string.Schedule_sync_dialog_message));
		}

		dialog.setTitle(getResources().getString(R.string.app_name));
		dialog.setCancelable(false);
		dialog.show();

		try
		{

			//TODO USER EMAIL

			SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
			String Cust_domain = sp.getString("Cust_Service_Url", "");
			String service_url = Cust_domain + "metal/api/v1/";
			String device_id = sp.getString("devid", "");
			String domain = service_url;

			SharedPreferences spf = Sales_Dash.this.getSharedPreferences("SimpleLogic", 0);
			String user_email = spf.getString("USER_EMAIL",null);

			Log.i("volley", "domain: " + domain);
			Log.i("volley", "email: " + user_email);
			Log.i("target url", "target url " + domain+"delivery_schedules?customer_code="+Global_Data.GLOvel_CUSTOMER_ID+"&email="+user_email);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain+"delivery_schedules?customer_code="+Global_Data.GLOvel_CUSTOMER_ID+"&email="+user_email,null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.i("volley", "response: " + response);
					//  Log.i("volley", "response reg Length: " + response.length());


					try{


						//   for (int a = 0; a < response.length(); a++) {

//	                        JSONObject person = (JSONObject) response.getJSONArray(response);
						//
						//   String name = response.getString("result44");

						String response_result = "";
						if(response.has("result"))
						{
							response_result = response.getString("result");
						}
						else
						{
							response_result = "data";
						}


						if(response_result.equalsIgnoreCase("Schedule doesn't exist")) {


							//Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

//							Toast toast = Toast.makeText(Sales_Dash.this, response_result, Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
							Global_Data.Custom_Toast(Sales_Dash.this, response_result,"yes");

						}
						else {

							//dbvoc.getDeleteTable("delivery_products");
							//dbvoc.getDeleteTable("delivery_schedules");
							//dbvoc.getDeleteTable("credit_profile");
							dbvoc.getDeletedelivery_schedules(Global_Data.GLOvel_CUSTOMER_ID);
							dbvoc.getDeletedelivery_products(Global_Data.GLOvel_CUSTOMER_ID);
							dbvoc.getDeletecredit_limits(Global_Data.GLOvel_CUSTOMER_ID);

							JSONArray delivery_products = response.getJSONArray("delivery_products");
							JSONArray delivery_schedules = response.getJSONArray("delivery_schedules");
							JSONArray credit_profile = response.getJSONArray("credit_profiles");


							Log.i("volley", "response reg delivery_products Length: " + delivery_products.length());
							Log.i("volley", "response reg delivery_schedules Length: " + delivery_schedules.length());
							Log.i("volley", "response reg credit_profile Length: " + credit_profile.length());

							Log.d("States", "delivery_products" + delivery_products.toString());
							Log.d("States", "delivery_schedules" + delivery_schedules.toString());
							Log.d("States", "credit_profile" + credit_profile.toString());

							//
							if(delivery_schedules.length() <= 0)
							{
								dialog.dismiss();
								//Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//								Toast toast = Toast.makeText(Sales_Dash.this, getResources().getString(R.string.Delivery_Schedule_Not_Found), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(Sales_Dash.this, getResources().getString(R.string.Delivery_Schedule_Not_Found),"yes");
							}
							else
							{
								for (int i = 0; i < delivery_products.length(); i++) {

									JSONObject jsonObject = delivery_products.getJSONObject(i);

									loginDataBaseAdapter.insertDeliveryProducts("",jsonObject.getString("customer_code"),jsonObject.getString("order_number"),"", "","","","", jsonObject.getString("order_quantity"), jsonObject.getString("delivered_quantity"),jsonObject.getString("truck_number"), jsonObject.getString("transporter_details"),"","",jsonObject.getString("product_name") +"" +"" +"");

								}

								for (int i = 0; i < delivery_schedules.length(); i++) {

									JSONObject jsonObject = delivery_schedules.getJSONObject(i);

									loginDataBaseAdapter.insertDeliverySchedule("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), jsonObject.getString("order_number"),"",jsonObject.getString("user_email"),jsonObject.getString("dispatch_date"), jsonObject.getString("delivery_date"), jsonObject.getString("order_amount"), jsonObject.getString("accepted_payment_mode"),"", jsonObject.getString("collected_amount"), jsonObject.getString("outstanding_amount"),"","","");


								}


								for (int i = 0; i < credit_profile.length(); i++) {

									JSONObject jsonObject = credit_profile.getJSONObject(i);

									loginDataBaseAdapter.insert_credit_profile("",jsonObject.getString("customer_code"),jsonObject.getString("customer_code"),"","","","",jsonObject.getString("credit_limit"),jsonObject.getString("amount_outstanding"),jsonObject.getString("amount_overdue"));


								}

								Global_Data.Schedule_FLAG = "CUSTOMER";
								Intent intent = new Intent(Sales_Dash.this, Schedule_List.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
								startActivity(intent);
								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
								finish();
								dialog.dismiss();
							}
							dialog.dismiss();

							//finish();

						}


						// }

						// output.setText(data);
					}catch(JSONException e){e.printStackTrace(); dialog.dismiss(); }


					dialog.dismiss();
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.i("volley", "error: " + error);
					//Toast.makeText(Order.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

//					Toast toast = Toast.makeText(Sales_Dash.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Sales_Dash.this, getResources().getString(R.string.Server_Error),"yes");

					dialog.dismiss();

				}
			});

			RequestQueue requestQueue = Volley.newRequestQueue(Sales_Dash.this);
			// queue.add(jsObjRequest);
			jsObjRequest.setShouldCache(false);
			int socketTimeout = 200000;//30 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			jsObjRequest.setRetryPolicy(policy);
			requestQueue.add(jsObjRequest);

		} catch (Exception e) {
			e.printStackTrace();
			dialog.dismiss();



		}
	}
}
