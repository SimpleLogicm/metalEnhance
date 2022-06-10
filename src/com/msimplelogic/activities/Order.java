package com.msimplelogic.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.msimplelogic.activities.R;

import com.msimplelogic.webservice.ConnectionDetector;
import com.squareup.seismic.ShakeDetector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class Order extends Activity implements OnItemSelectedListener, ShakeDetector.Listener {
	private static final String TAG = "Debug";
	SensorManager sensorManager;
	ShakeDetector sd;
	Camera camera;
	int m = 1000;
	String oncreate_flag = "";
	String service_flag = "";
	//private ShakeEventManager sd;
	String state_name = "";
	String city_name = "";
	TextView schedule_txt;
	TextView mTitleTextView;
	String beat_name = "";
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	Button feedback_btn, comp_btn, Claims_btn, stock_btn, imag_btn, video_btn,
			buttonSchedule, buttoninvoice;
	Button buttonNewOrder, buttonPreviousOrder, buttonNoOrder,
			buttonReturnOrder, adr_button, order_delivery_click;
	Spinner city_spinner, state_spinner, beat_spinner;
	TextView selVersion,ocredit_limit,oamount_utstanding,oamount_overdue,customer_MObile,customer_landline;
	HttpGet httppst;
	String s[];
	int state_flag = 0;
	ProgressDialog dialog;
	ArrayAdapter<String> adapter_state1;
	AutoCompleteTextView autoCompleteTextView1;
	ArrayAdapter<String> adapter_state2;
	ArrayAdapter<String> adapter_beat;
	ArrayAdapter<String> adapter_state3;
	LinearLayout order_view, custserve_view, schedule_view,credit_profile_layout;
	RelativeLayout rlout_order, rlout_custserve, rlout_schedule;
	HttpClient httpclint;
	List<NameValuePair> nameValuePars;
	HttpPost httppost;
	HttpResponse response;
	LoginDataBaseAdapter adapter_ob;
	DataBaseHelper helper_ob;
	SQLiteDatabase db_ob;
	Cursor cursor;
	String[] from;
	ListView lv;
	SimpleCursorAdapter cursorAdapter;
	DataBaseHelper dbvoc = new DataBaseHelper(this);
	String C_ID = "";
	String S_ID = "";
	String B_ID = "";
	String CUS_ID = "";
	String CUS_NAME = "";
	SharedPreferences sp;
	ArrayList<String> list_cities = new ArrayList<String>();
	String[] customer_array;
	Button customer_submit;
	LoginDataBaseAdapter loginDataBaseAdapter;
	TextView customer_address;
	String schedulestr;
	private RequestQueue requestQueue;
	private String Re_Text = "";
	private int drop_value = 0;
	private String c_mobile_number = "";
	private String c_landline_number = "";
	private ArrayList<String> results = new ArrayList<String>();
	private ArrayList<String> results1 = new ArrayList<String>();
	private ArrayList<String> results2 = new ArrayList<String>();
	private ArrayList<String> results_beat = new ArrayList<String>();
	private LocationManager locationMangaer = null;
	private LocationListener locationListener = null;
	private Button btnGetLocation = null;
	private EditText editLocation = null;
	private ProgressBar pb = null;
	private Boolean flag = false;
	ImageView outstandingOverdueBtn;

	@SuppressLint("DefaultLocale")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.order_new);
		// lv=(ListView)findViewById(R.id.listView1);
		// customer_submit = (Button) findViewById(R.id.customer_submit);
		Global_Data.GLObalOrder_id = "";
		Global_Data.Schedule_FLAG = "";
		Global_Data.Glovel_BEAT_ID = "";
		Global_Data.AmountOutstanding = "";
		Global_Data.AmountOverdue = "";
        Global_Data.Order_Delivery_hashmap.clear();
		locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//adr_button = (Button) findViewById(R.id.but_address);
		city_spinner = (Spinner) findViewById(R.id.cust_city);
		state_spinner = (Spinner) findViewById(R.id.cust_state);
		beat_spinner = (Spinner) findViewById(R.id.cust_beat);
		autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		rlout_order = (RelativeLayout) findViewById(R.id.rlout_order);
		rlout_custserve = (RelativeLayout) findViewById(R.id.rlout_customer);
		rlout_schedule = (RelativeLayout) findViewById(R.id.rlout_schedule);

		order_view = (LinearLayout) findViewById(R.id.order_view);
		custserve_view = (LinearLayout) findViewById(R.id.customer_view);
		schedule_view = (LinearLayout) findViewById(R.id.schedule_view);
		credit_profile_layout = (LinearLayout) findViewById(R.id.credit_profile_layout);

		order_view.setVisibility(View.VISIBLE);
		cd  = new ConnectionDetector(getApplicationContext());
		rlout_custserve.setBackgroundResource(R.drawable.single_wtab);
		rlout_schedule.setBackgroundResource(R.drawable.single_wtab);

		customer_address = (TextView) findViewById(R.id.customer_address);

		ocredit_limit = (TextView) findViewById(R.id.ocredit_limit);
		oamount_utstanding = (TextView) findViewById(R.id.oamount_utstanding);
		oamount_overdue = (TextView) findViewById(R.id.oamount_overdue);
		customer_MObile = (TextView) findViewById(R.id.customer_MObile);
		customer_landline = (TextView) findViewById(R.id.customer_landline);
		schedule_txt = (TextView) findViewById(R.id.textView1sf);
		outstandingOverdueBtn = (ImageView) findViewById(R.id.outstandingoverdue_btn);

		isInternetPresent = cd.isConnectingToInternet();

		// for label change
		SharedPreferences spf6=this.getSharedPreferences("SimpleLogic",0);
		schedulestr=spf6.getString("var_schedule", "");

		if(schedulestr.length()>0)
		{
			String output = schedulestr.substring(0, 1).toUpperCase() + schedulestr.substring(1).toLowerCase();
			schedule_txt.setText(output);
		} else {
			schedule_txt.setText(getResources().getString(R.string.Scheduleo));
		}

//		try {
//			ActionBar mActionBar = getActionBar();
//			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
//					.parseColor("#910505")));
//			// mActionBar.setDisplayShowHomeEnabled(false);
//			// mActionBar.setDisplayShowTitleEnabled(false);
//			LayoutInflater mInflater = LayoutInflater.from(this);
//			Intent i = getIntent();
//			String name = i.getStringExtra("retialer");
//			View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//			mCustomView.setBackgroundDrawable(new ColorDrawable(Color
//					.parseColor("#910505")));
//			mTitleTextView = (TextView) mCustomView
//					.findViewById(R.id.screenname);
//			mTitleTextView.setText(getResources().getString(R.string.Ordero));
//
//			TextView todaysTarget = (TextView) mCustomView
//					.findViewById(R.id.todaysTarget);
//			SharedPreferences sp = Order.this
//					.getSharedPreferences("SimpleLogic", 0);
//			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//			sd = new ShakeDetector(this);
//			sd.start(sensorManager);
//
//			dialog = new ProgressDialog(Order.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//			try {
//				int target = (int) Math.round(sp.getFloat("Target", 0));
//				int achieved = (int) Math.round(sp.getFloat("Achived", 0));
//				Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
//				if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
//					int age = (int) Math.round(age_float);
//
//					if (Global_Data.rsstr.length() > 0) {
//						todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//					} else {
//						todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//					}
//
//				} else {
//					int age = (int) Math.round(age_float);
//
//					if (Global_Data.rsstr.length() > 0) {
//						todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//					} else {
//						todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//					}
//
//				}
//
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//				// todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target",
//				// 0.00f)-sp.getFloat("Target", 0.00f))+"");
//				todaysTarget.setText("Today's Target Acheived");
//			}
//
//			mActionBar.setCustomView(mCustomView);
//			mActionBar.setDisplayShowCustomEnabled(true);
//			mActionBar.setHomeButtonEnabled(true);
//			mActionBar.setDisplayHomeAsUpEnabled(true);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}

		Global_Data.GLOVEL_PREVIOUS_ORDER_FLAG = "";

		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		Global_Data.GLOvel_GORDER_ID = "";
		Global_Data.GLOVEL_LONG_DESC = "";
		Global_Data.GLOVEL_CATEGORY_SELECTION = "";
		Global_Data.GLOVEL_ITEM_MRP = "";
		//Global_Data.productList.clear();

		results.clear();
		results.add(getResources().getString(R.string.Select_City));

		list_cities.clear();
		List<Local_Data> contacts3 = dbvoc.getAllCustomer();
		for (Local_Data cn : contacts3) {
			list_cities.add(cn.get_stocks_product_name());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				list_cities);
		autoCompleteTextView1.setThreshold(1);// will start working from
		// first character
		autoCompleteTextView1.setAdapter(adapter);// setting the adapter
		// data into the
		// AutoCompleteTextView
		autoCompleteTextView1.setTextColor(Color.BLACK);

		outstandingOverdueBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Global_Data.SpeedometerStatus="yes";
				Intent m = new Intent(Order.this, OutstandingOverdueActivity.class);
				m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// m.putExtra("ANN_ID", Ann_Model.getAnn_id());
//                m.putExtra("REGION_ID", list.getDescription());
				Order.this.startActivity(m);
			}
		});

		customer_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInternetPresent) {
					//if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(latlong) && !(latlong.equalsIgnoreCase("0.0,0.0")) && !(latlong.equalsIgnoreCase(","))) {

//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse("http://maps.google.com/maps?saddr=" + Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE + "&daddr=" + latlong + ""));
//                        ((Activity) view.getContext()).startActivity(intent);

						try {
							String dsf=customer_address.getText().toString().trim();
							if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(dsf)) {
								Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
										Uri.parse("http://maps.google.com/maps?saddr=" + Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE + "&daddr=" + customer_address.getText().toString().trim() + ""));
								startActivity(intent);
							} else {
								//Toast.makeText(Order.this, "Address not found", Toast.LENGTH_SHORT).show();
								Global_Data.Custom_Toast(Order.this, "Address not found","");
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}

//					} else {
//						//Toast.makeText(mcontext, "Wait..", Toast.LENGTH_SHORT).show();
////                        GeocodingLocation locationAddress = new GeocodingLocation();
////                        locationAddress.getAddressFromLocation(address,
////                                (Activity) view.getContext(), new GeocoderHandler());
//
//						try {
//							if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(customer_address.getText().toString().trim())) {
//								Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//										Uri.parse("http://maps.google.com/maps?saddr=" + Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE + "&daddr=" + customer_address.getText().toString().trim() + ""));
//								startActivity(intent);
//							} else {
//								Toast.makeText(Order.this, "Address not found", Toast.LENGTH_SHORT).show();
//							}
//
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//
//						// Toast.makeText((Activity)view.getContext(), "Customer location not found.", Toast.LENGTH_SHORT).show();
//					}
				} else {
//					Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error), "yes");
				}
				//Toast.makeText(Order.this, "yti", Toast.LENGTH_SHORT).show();
			}
		});

		autoCompleteTextView1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if(event.getAction() == MotionEvent.ACTION_UP) {
					if(event.getRawX() >= (autoCompleteTextView1.getRight() - autoCompleteTextView1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

						View view = Order.this.getCurrentFocus();
						if (view != null) {
							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
						}
						//autoCompleteTextView1.setText("");
						autoCompleteTextView1.showDropDown();
						return true;
					}
				}
				return false;
			}
		});

		autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
									long id) {
				//Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

				Global_Data.hideSoftKeyboard(Order.this);

				String customer_name = "";
				String address_type = "";
				if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0)
				{
					s = autoCompleteTextView1.getText().toString().trim().split(":");
					customer_name = s[0].trim();
					address_type = s[1].trim();
				}
				else
				{
					customer_name = autoCompleteTextView1.getText().toString().trim();
				}

//				Global_Data.credit_limit_amount = "";
//				Global_Data.outstandings_amount = "";

				List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);
				if(contacts.size() <= 0)
				{
//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Customer_Not_Found),"yes");
				}
				else {
					for (Local_Data cn : contacts) {

						if(!Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales"))
						{
							outstandingOverdueBtn.setVisibility(View.VISIBLE);
						}

						Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
						Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
						Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
						Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
						Global_Data.CustLandlineNo = cn.getLANDLINE_NO();

						List<Local_Data> statei = dbvoc.getstateby_id(cn.getSTATE_ID());
						if(statei.size() > 0)
						{
							for (Local_Data cnn : statei) {
								state_name = cnn.getStateName();
							}
						}

						List<Local_Data> cityi = dbvoc.getcityByState_idn(cn.getCITY_ID());
						if(cityi.size() > 0)
						{
							for (Local_Data cnnn : cityi) {
								city_name = cnnn.getCityName();
							}
						}

						List<Local_Data> beati = dbvoc.getbeatByCityIDn(cn.getBEAT_ID());
						if(beati.size() > 0)
						{
							for (Local_Data cnnnn : beati) {
								beat_name = cnnnn.getCityName();
							}
						}

						adapter_state1 = new ArrayAdapter<String>(Order.this,
								android.R.layout.simple_spinner_item, results1);

						// ArrayAdapter<String> adapter_state3 = new
						// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
						// results2);

						adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						state_spinner.setAdapter(adapter_state1);
						state_spinner.setOnItemSelectedListener(Order.this);

						if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(state_name)){
							int spinnerPosition = adapter_state1.getPosition(state_name);
							state_spinner.setSelection(spinnerPosition);
						}
//
						String habitnumber = "<b>" + getResources().getString(R.string.Address) + "</b> " + cn.getAddress();
						String mobile_number = "<b>" + getResources().getString(R.string.Mobile_Number_H) + "</b> " + "<u>" + cn.getMOBILE_NO() + "</u>";
						String landline_number = "<b>" + getResources().getString(R.string.Landline_Number_H) + "</b> " + "<u>" + cn.getLANDLINE_NO() + "</u>";
						c_mobile_number = cn.getMOBILE_NO();
						c_landline_number = cn.getLANDLINE_NO();
						customer_address.setText(Html.fromHtml(habitnumber ));
						customer_MObile.setText(Html.fromHtml(mobile_number));
						customer_landline.setText(Html.fromHtml(landline_number));

						//customer_address.setText("Address: "+cn.getAddress());
					}
				}

				List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Global_Data.GLOvel_CUSTOMER_ID);
				if(contactlimit.size() > 0) {
					credit_profile_layout.setVisibility(View.VISIBLE);
					for (Local_Data cn : contactlimit) {

						if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_credit_limit())) {
							Double credit_limit =  ((Double.valueOf(cn.get_credit_limit())));
							ocredit_limit.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
						} else {
							ocredit_limit.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
						}

						if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_shedule_outstanding_amount())) {
							Double amt_outstanding =  ((Double.valueOf(cn.get_shedule_outstanding_amount())));
							oamount_utstanding.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", amt_outstanding)));
							Global_Data.AmountOutstanding = cn.get_shedule_outstanding_amount();
						} else {
							oamount_utstanding.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
							Global_Data.AmountOutstanding = "0.0";
						}

						if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getAmmount_overdue())) {
							Double amt_overdue =  ((Double.valueOf(cn.getAmmount_overdue())));
							oamount_overdue.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));

							Global_Data.AmountOverdue = cn.getAmmount_overdue();
						} else {
							oamount_overdue.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
							Global_Data.AmountOverdue = "0.0";
						}
					}
				}
				else
				{
					Global_Data.AmountOutstanding = "0.0";
					Global_Data.AmountOverdue = "0.0";
					credit_profile_layout.setVisibility(View.GONE);
					ocredit_limit.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
					oamount_utstanding.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
					oamount_overdue.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
				}
			}
		});

		customer_MObile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				requestPhoneCallPermission(c_mobile_number.trim());
			}
		});

		customer_landline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				requestPhoneCallPermission(c_landline_number.trim());
			}
		});


		/* for cutomer navigation vinod */

		buttonSchedule = (Button) findViewById(R.id.buttonSchedule);
		buttoninvoice = (Button) findViewById(R.id.buttoninvoice);

		buttonNewOrder = (Button) findViewById(R.id.but_neworder);
		buttonNoOrder = (Button) findViewById(R.id.but_noorder);
		buttonPreviousOrder = (Button) findViewById(R.id.but_preorder);
		buttonReturnOrder = (Button) findViewById(R.id.but_returnorder);
		order_delivery_click = (Button) findViewById(R.id.order_delivery_click);

		// for label change
		SharedPreferences spf1=this.getSharedPreferences("SimpleLogic",0);
		String noorderstr=spf1.getString("var_norder", "");

		if(noorderstr.length()>0)
		{
			buttonNoOrder.setText(noorderstr);
		}else{
			buttonNoOrder.setText(getResources().getString(R.string.No_Order));
		}

		SharedPreferences spf2=this.getSharedPreferences("SimpleLogic",0);
		String retorderstr=spf2.getString("var_retorder", "");

		if(retorderstr.length()>0)
		{
			buttonReturnOrder.setText(retorderstr);
		}else{
			buttonReturnOrder.setText(getResources().getString(R.string.Return_Order));
		}

		if(Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales"))
		{
			buttonNewOrder.setText(getResources().getString(R.string.New_Quote));

			buttonPreviousOrder.setText(getResources().getString(R.string.Previous_Quote));
			buttonPreviousOrder.setVisibility(View.GONE);
			buttonNoOrder.setText(getResources().getString(R.string.No_Quote));
			buttonNoOrder.setVisibility(View.GONE);
			buttonReturnOrder.setText(getResources().getString(R.string.Return_Quote));
			buttonReturnOrder.setVisibility(View.GONE);
			order_delivery_click.setVisibility(View.GONE);

		}

		stock_btn = (Button) findViewById(R.id.stock_btn);
		feedback_btn = (Button) findViewById(R.id.feedback_btn);
		comp_btn = (Button) findViewById(R.id.comp_btn);
		Claims_btn = (Button) findViewById(R.id.Claims_btn);
		video_btn = (Button) findViewById(R.id.video_btn);
		imag_btn = (Button) findViewById(R.id.imag_btn);

		buttonSchedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state), "yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {
//
//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city), "yes");

				}

				else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				}

				else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer), "yes");

				} else {

					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0)
					{
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					}
					else
					{
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}

					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0)
					{
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found), "yes");
					}
					else
					{

						for (Local_Data cn : contacts)
						{

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_Address = cn.getAddress();

						}


						//Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

//						Intent intent = new Intent(getApplicationContext(),
//								Schedule.class);

						isInternetPresent = cd.isConnectingToInternet();

						AlertDialog alertDialog = new AlertDialog.Builder(Order.this).create(); //Read Update

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
//									Toast toast = Toast.makeText(Order.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
									Global_Data.Custom_Toast(Order.this, getResources().getString(R.string.internet_connection_error), "yes");
								}
							}
						});

						alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Offline), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								List<Local_Data> contacts3 = dbvoc.getSchedule_List(Global_Data.GLOvel_CUSTOMER_ID);

								if (contacts3.size() <= 0) {
									//Toast.makeText(Order.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

//									Toast toast = Toast.makeText(Order.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
									Global_Data.Custom_Toast(Order.this, getResources().getString(R.string.Sorry_No_Record_Found),"yes");
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

					}
				}
			}
		});

		buttoninvoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {
//
//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					Re_Text = autoCompleteTextView1.getText().toString();
					Intent intent = new Intent(getApplicationContext(),
							Schedule2.class);
					// intent.putExtra("CP_NAME", "Image");
					intent.putExtra("RE_TEXT", Re_Text);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}
		});

		buttonNewOrder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city), "yes");
				}

				else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				}

				else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {

					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0)
					{
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					}
					else
					{
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);
					if(contacts.size() <= 0)
					{
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					}
					else
					{
						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();

						}


						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.GLObalOrder_id = "";
						Global_Data.GLOvel_ITEM_NUMBER = "";
						Global_Data.GLOvel_GORDER_ID_RETURN = "";
						Global_Data.GLObalOrder_id_return = "";
						Global_Data.GLOvel_GORDER_ID = "";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG = "";

						Global_Data.GLOVEL_PREVIOUS_ORDER_FLAG = "";
						Intent intent = new Intent(getApplicationContext(),
								NewOrderActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);

					}
				}
			}
		});

		buttonPreviousOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				} else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {

					String customer_name = "";
					String address_type = "";
					if (autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if (contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found), "yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();

						}


						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.GLOVEL_PREVIOUS_ORDER_FLAG = "TRUE";


						Global_Data.GLObalOrder_id = "";
						Global_Data.Previous_Order_UpdateOrder_ID = "";
						Global_Data.GLObalOrder_id = "";
						Global_Data.Previous_Order_ServiceOrder_ID = "";

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent) {
                            getPrevious_OrderData();
                        } else {
                            //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(Order.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Order.this, getResources().getString(R.string.internet_connection_error), "yes");
                        }



//							 Intent intent = new Intent(getApplicationContext(),
//							 Previous_orderNew_S2.class);
//							 startActivity(intent);
//							  finish();
//							 overridePendingTransition(R.anim.slide_in_right,
//							 R.anim.slide_out_left);

					}
				}
			}
		});

		order_delivery_click.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				} else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat), "yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {

					String customer_name = "";
					String address_type = "";
					if (autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if (contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}


						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.GLOVEL_PREVIOUS_ORDER_FLAG = "TRUE";


						Global_Data.GLObalOrder_id = "";
						Global_Data.Previous_Order_UpdateOrder_ID = "";
						Global_Data.GLObalOrder_id = "";
						Global_Data.Previous_Order_ServiceOrder_ID = "";
                        Global_Data.Order_Delivery_hashmap.clear();

                        Intent intent = new Intent(getApplicationContext(),
								Order_Delivery.class);
						startActivity(intent);
						finish();


					}
				}
			}
		});

		buttonNoOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city), "yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							// Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}

						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;
						Intent intent = new Intent(getApplicationContext(),
								NoOrderActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				}
			}
		});

		buttonReturnOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer), "yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							//Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}

						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.GLObalOrder_id = "";
						Global_Data.GLOvel_ITEM_NUMBER = "";
						Global_Data.GLOvel_GORDER_ID_RETURN = "";
						Global_Data.GLObalOrder_id_return = "";
						Global_Data.GLOvel_GORDER_ID= "";

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								ReturnOrder1.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				}
			}
		});

		stock_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}

					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}


						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								Customer_Stock.class);
						intent.putExtra("CP_NAME", "Stock");
						intent.putExtra("RE_TEXT", Re_Text);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}

				}
			}
		});

		imag_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {
//
//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes" );

				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}

						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								Customer_Feed.class);
						intent.putExtra("CP_NAME", "Image");
						intent.putExtra("RE_TEXT", Re_Text);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				}
			}
		});

		feedback_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
Global_Data.Custom_Toast(Order.this,
		getResources().getString(R.string.Please_Select_Customer),"yes");
				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}

						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								Customer_Feed.class);
						intent.putExtra("CP_NAME", "Feedback");
						intent.putExtra("RE_TEXT", Re_Text);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}

				}
			}
		});

		Claims_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat), "yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}

					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found), "yes");
					} else {

						for (Local_Data cn : contacts) {
							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}

						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								Customer_Feed.class);
						intent.putExtra("CP_NAME", "Claim");
						intent.putExtra("RE_TEXT", Re_Text);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				}
			}
		});

		comp_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state), "yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}


						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								Customer_Feed.class);
						intent.putExtra("CP_NAME", "Complaints");
						intent.putExtra("RE_TEXT", Re_Text);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}

				}
			}
		});

		video_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (state_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_state), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_state),"yes");
				}

				else if (city_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_city), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_city),"yes");
				} else if (beat_spinner.getSelectedItem().toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.select_beat), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.select_beat),"yes");
				} else if (autoCompleteTextView1.getText().toString()
						.equalsIgnoreCase("")) {
//
//					Toast toast = Toast.makeText(Order.this,
//							getResources().getString(R.string.Please_Select_Customer), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this,
							getResources().getString(R.string.Please_Select_Customer),"yes");

				} else {
					// Re_Text = autoCompleteTextView1.getText().toString();
					String customer_name = "";
					String address_type = "";
					if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
						s = autoCompleteTextView1.getText().toString().trim().split(":");
						customer_name = s[0].trim();
						address_type = s[1].trim();
					} else {
						customer_name = autoCompleteTextView1.getText().toString().trim();
					}


					List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

					if(contacts.size() <= 0) {
//						Toast toast = Toast.makeText(Order.this,
//								getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
						Global_Data.Custom_Toast(Order.this,
								getResources().getString(R.string.Customer_Not_Found),"yes");
					} else {

						for (Local_Data cn : contacts) {

							Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
							Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
							Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
							Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
							Global_Data.CustLandlineNo = cn.getLANDLINE_NO();
						}

						Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE";
						Global_Data.order_city = city_spinner.getSelectedItem()
								.toString();
						Global_Data.order_beat = beat_spinner.getSelectedItem()
								.toString();
						Global_Data.order_state = state_spinner.getSelectedItem()
								.toString();
						Global_Data.order_retailer = customer_name;

						Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = "";
						Intent intent = new Intent(getApplicationContext(),
								Customer_Feed.class);
						intent.putExtra("CP_NAME", "video");
						intent.putExtra("RE_TEXT", Re_Text);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}

				}
			}
		});

		if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("CUSTOMER_SERVICE"))
		{
			//mTitleTextView.setText(getResources().getString(R.string.Customer_Serviceo));
			custserve_view.setVisibility(View.VISIBLE);
			// rlout_custserve.setVisibility(View.INVISIBLE);
			rlout_order.setBackgroundResource(R.drawable.single_wtab);
			rlout_schedule.setBackgroundResource(R.drawable.single_wtab);
			rlout_custserve.setBackgroundResource(R.drawable.single_btab);
			order_view.setVisibility(View.GONE);
			// rlout_order.setVisibility(View.VISIBLE);
			//
			schedule_view.setVisibility(View.GONE);
			// rlout_schedule.setVisibility(View.VISIBLE);
		}
		else
		if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("SCHEDULE"))
		{
//			if(schedulestr.length()>0)
//			{
//				mTitleTextView.setText(schedulestr);
//			}else{
//				mTitleTextView.setText(getResources().getString(R.string.Scheduleo));
//			}

			custserve_view.setVisibility(View.GONE);
			// rlout_custserve.setVisibility(View.VISIBLE);
			rlout_custserve.setBackgroundResource(R.drawable.single_wtab);
			rlout_order.setBackgroundResource(R.drawable.single_wtab);
			rlout_schedule.setBackgroundResource(R.drawable.single_btab);
			order_view.setVisibility(View.GONE);
			// rlout_order.setVisibility(View.VISIBLE);

			schedule_view.setVisibility(View.VISIBLE);
			// rlout_schedule.setVisibility(View.INVISIBLE);
		}
		else
		{
//			if(Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales"))
//			{
//				mTitleTextView.setText(getResources().getString(R.string.Quote));
//			}
//			else
//			{
//				mTitleTextView.setText(getResources().getString(R.string.Ordero));
//			}

			order_view.setVisibility(View.VISIBLE);
			// rlout_order.setVisibility(View.INVISIBLE);
			rlout_custserve.setBackgroundResource(R.drawable.single_wtab);
			rlout_schedule.setBackgroundResource(R.drawable.single_wtab);
			rlout_order.setBackgroundResource(R.drawable.single_btab);
			custserve_view.setVisibility(View.GONE);
			// rlout_custserve.setVisibility(View.VISIBLE);

			schedule_view.setVisibility(View.GONE);
			// rlout_schedule.setVisibility(View.VISIBLE);
		}
		rlout_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				if(Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales"))
//				{
//					mTitleTextView.setText(getResources().getString(R.string.Quote));
//				}
//				else
//				{
//					mTitleTextView.setText(getResources().getString(R.string.Ordero));
//				}

				Global_Data.CUSTOMER_SERVICE_FLAG = "";
				order_view.setVisibility(View.VISIBLE);
				// rlout_order.setVisibility(View.INVISIBLE);
				rlout_custserve.setBackgroundResource(R.drawable.single_wtab);
				rlout_schedule.setBackgroundResource(R.drawable.single_wtab);
				rlout_order.setBackgroundResource(R.drawable.single_btab);
				custserve_view.setVisibility(View.GONE);
				// rlout_custserve.setVisibility(View.VISIBLE);

				schedule_view.setVisibility(View.GONE);
				// rlout_schedule.setVisibility(View.VISIBLE);
			}
		});

		rlout_custserve.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//.setText(getResources().getString(R.string.Customer_Serviceo));
				Global_Data.CUSTOMER_SERVICE_FLAG = "CUSTOMER_SERVICE";
				custserve_view.setVisibility(View.VISIBLE);
				// rlout_custserve.setVisibility(View.INVISIBLE);
				rlout_order.setBackgroundResource(R.drawable.single_wtab);
				rlout_schedule.setBackgroundResource(R.drawable.single_wtab);
				rlout_custserve.setBackgroundResource(R.drawable.single_btab);
				order_view.setVisibility(View.GONE);
				// rlout_order.setVisibility(View.VISIBLE);
				//
				schedule_view.setVisibility(View.GONE);
				// rlout_schedule.setVisibility(View.VISIBLE);

			}
		});

		rlout_schedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				if(schedulestr.length()>0)
//				{
//					mTitleTextView.setText(schedulestr);
//				}else{
//					mTitleTextView.setText(getResources().getString(R.string.Scheduleo));
//				}

				//mTitleTextView.setText(schedulestr);
				Global_Data.CUSTOMER_SERVICE_FLAG = "SCHEDULE";
				custserve_view.setVisibility(View.GONE);
				// rlout_custserve.setVisibility(View.VISIBLE);
				rlout_custserve.setBackgroundResource(R.drawable.single_wtab);
				rlout_order.setBackgroundResource(R.drawable.single_wtab);
				rlout_schedule.setBackgroundResource(R.drawable.single_btab);
				order_view.setVisibility(View.GONE);
				// rlout_order.setVisibility(View.VISIBLE);

				schedule_view.setVisibility(View.VISIBLE);
				// rlout_schedule.setVisibility(View.INVISIBLE);
			}
		});



		// Reading all
		List<Local_Data> contacts1 = dbvoc.getAllState();
		results1.add(getResources().getString(R.string.Select_State));
		for (Local_Data cn : contacts1) {
			String str_state = "" + cn.getStateName();
			results1.add(str_state);
		}

		adapter_state1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, results1);

		// ArrayAdapter<String> adapter_state3 = new
		// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
		// results2);

		adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		state_spinner.setAdapter(adapter_state1);
		state_spinner.setOnItemSelectedListener(this);

		//results.clear();
		//results.add(getResources().getString(R.string.Select_City));

		adapter_state2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, results);
		adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		city_spinner.setAdapter(adapter_state2);
		//	city_spinner.setOnItemSelectedListener(this);

		//results_beat.clear();
		results_beat.add(getResources().getString(R.string.Select_Beats));
		adapter_beat = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, results_beat);
		adapter_beat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		beat_spinner.setAdapter(adapter_beat);
		//beat_spinner.setOnItemSelectedListener(this);


	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		Spinner spinner = (Spinner) arg0;
		drop_value = drop_value+1;

		if(drop_value > 1) {
			if (spinner.getId() == R.id.cust_state) {
				if (arg0.getItemAtPosition(arg2).toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

					customer_address.setText("");
					customer_MObile.setText("");
					customer_landline.setText("");
					ocredit_limit.setText("");
					oamount_utstanding.setText("");
					oamount_overdue.setText("");

					state_name = "";
					city_name = "";
					beat_name = "";

					results.clear();
					results.add(getResources().getString(R.string.Select_City));
					results_beat.clear();
					results_beat.add(getResources().getString(R.string.Select_Beats));

					adapter_state2 = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_item, results);
					adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					city_spinner.setAdapter(adapter_state2);
					city_spinner.setOnItemSelectedListener(this);

					//state_spinner.setOnItemSelectedListener(this);

					autoCompleteTextView1.setText("");
					// String []customer_array = {"Apple", "Banana", "Cherry",
					// "Date", "Grape", "Kiwi", "Mango", "Pear"};
					list_cities.clear();
					List<Local_Data> contacts2 = dbvoc.getAllCustomer();
					for (Local_Data cn : contacts2) {
						list_cities.add(cn.get_stocks_product_name());
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_dropdown_item,
							list_cities);
					autoCompleteTextView1.setThreshold(1);// will start working from
					// first character
					autoCompleteTextView1.setAdapter(adapter);// setting the adapter
					// data into the
					// AutoCompleteTextView
					autoCompleteTextView1.setTextColor(Color.BLACK);

				} else {

					String items = state_spinner.getSelectedItem().toString().trim();
					//String C_ID = "";
					Log.i("Selected item : ", items);

					Log.i("Selected item : ", items);

					List<Local_Data> contacts = dbvoc.getState_id(items);
					for (Local_Data cn : contacts) {

						S_ID = cn.getSTATE_ID();

					}

					if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(S_ID)) {
						results.clear();
						results.add(getResources().getString(R.string.Select_City));

						results_beat.clear();
						results_beat.add(getResources().getString(R.string.Select_Beats));
						List<Local_Data> contacts2 = dbvoc.getcityByState_id(S_ID);
						for (Local_Data cn : contacts2) {

							results.add(cn.getCityName());
						}
						adapter_state2 = new ArrayAdapter<String>(Order.this,
								android.R.layout.simple_spinner_item, results);
						adapter_state2
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						city_spinner.setAdapter(adapter_state2);
						city_spinner.setOnItemSelectedListener(this);

						adapter_state1 = new ArrayAdapter<String>(Order.this,
								android.R.layout.simple_spinner_item, results_beat);
						adapter_state1
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						beat_spinner.setAdapter(adapter_state1);
						beat_spinner.setOnItemSelectedListener(this);

						if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(state_name)){
							int spinnerPosition = adapter_state1.getPosition(state_name);
							state_spinner.setSelection(spinnerPosition);
						}

						if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(city_name)){
							int spinnerPosition = adapter_state2.getPosition(city_name);
							city_spinner.setSelection(spinnerPosition);
						}

					}

					if (!Global_Data.GLOvel_CITY_n.equalsIgnoreCase("")) {
						city_spinner.setSelection(adapter_state2
								.getPosition(Global_Data.GLOvel_CITY_n
										.toUpperCase()));
					} else if (!Global_Data.GLOvel_CITY.equalsIgnoreCase("")) {
						city_spinner
								.setSelection(adapter_state2
										.getPosition(Global_Data.GLOvel_CITY
												.toUpperCase()));
					}
				}
			}

			if (spinner.getId() == R.id.cust_city) {
				if (arg0.getItemAtPosition(arg2).toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_City))) {
					results_beat.clear();
					results_beat.add(getResources().getString(R.string.Select_Beats));
					customer_address.setText("");
					customer_MObile.setText("");
					customer_landline.setText("");
					ocredit_limit.setText("");
					oamount_utstanding.setText("");
					oamount_overdue.setText("");
					state_name = "";
					city_name = "";
					beat_name = "";
					// /results2.clear();
					// results2.add("Select Customer");
					adapter_beat = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_item, results_beat);
					adapter_beat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					beat_spinner.setAdapter(adapter_beat);
					beat_spinner.setOnItemSelectedListener(this);

					autoCompleteTextView1.setText("");
					// String []customer_array = {"Apple", "Banana", "Cherry",
					// "Date", "Grape", "Kiwi", "Mango", "Pear"};
					list_cities.clear();
					List<Local_Data> contacts2 = dbvoc.getAllCustomer();
					for (Local_Data cn : contacts2) {
						list_cities.add(cn.get_stocks_product_name());
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_dropdown_item,
							list_cities);
					autoCompleteTextView1.setThreshold(1);// will start working from
					// first character
					autoCompleteTextView1.setAdapter(adapter);// setting the adapter
					// data into the
					// AutoCompleteTextView
					autoCompleteTextView1.setTextColor(Color.BLACK);

				} else {

					String items = city_spinner.getSelectedItem().toString().trim();
					//String C_ID = "";
					Log.i("Selected item : ", items);

					results_beat.clear();
					results_beat.add(getResources().getString(R.string.Select_Beats));

					List<Local_Data> contacts = dbvoc.getCity_id(items);
					for (Local_Data cn : contacts) {

						C_ID = cn.getCITY_ID();

					}

					if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(C_ID)) {
//						results_beat.clear();
//						results_beat.add(getResources().getString(R.string.Select_Beats));
						List<Local_Data> contacts2 = dbvoc.getbeatByCityID((C_ID));
						for (Local_Data cn : contacts2) {

							results_beat.add(cn.getCityName());
						}
						adapter_beat = new ArrayAdapter<String>(Order.this,
								android.R.layout.simple_spinner_item, results_beat);
						adapter_beat
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						beat_spinner.setAdapter(adapter_beat);
						beat_spinner.setOnItemSelectedListener(this);

						if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(beat_name)){
							int spinnerPosition = adapter_beat.getPosition(beat_name);
							beat_spinner.setSelection(spinnerPosition);
						}

					}

					if (!Global_Data.GLOvel_CITY_n.equalsIgnoreCase("")) {
						beat_spinner.setSelection(adapter_beat
								.getPosition(Global_Data.GLOvel_CITY_n
										.toUpperCase()));
					} else if (!Global_Data.GLOvel_CITY.equalsIgnoreCase("")) {
						beat_spinner
								.setSelection(adapter_beat
										.getPosition(Global_Data.GLOvel_CITY
												.toUpperCase()));
					}
				}
			}

			if (spinner.getId() == R.id.cust_beat) {
				if (arg0.getItemAtPosition(arg2).toString()
						.equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

					list_cities.add("");
					list_cities.clear();
					customer_address.setText("");
					customer_MObile.setText("");
					customer_landline.setText("");
					ocredit_limit.setText("");
					oamount_utstanding.setText("");
					oamount_overdue.setText("");
					state_name = "";
					city_name = "";
					beat_name = "";

					autoCompleteTextView1.setText("");
					// String []customer_array = {"Apple", "Banana", "Cherry",
					// "Date", "Grape", "Kiwi", "Mango", "Pear"};
					list_cities.clear();
					List<Local_Data> contacts2 = dbvoc.getAllCustomer();
					for (Local_Data cn : contacts2) {
						list_cities.add(cn.get_stocks_product_name());
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_dropdown_item,
							list_cities);
					autoCompleteTextView1.setThreshold(1);// will start working from
					// first character
					autoCompleteTextView1.setAdapter(adapter);// setting the adapter
					// data into the
					// AutoCompleteTextView
					autoCompleteTextView1.setTextColor(Color.BLACK);

				} else {

					String items = beat_spinner.getSelectedItem().toString();
					//String C_ID = "";
					Log.i("Selected item : ", items);

					List<Local_Data> contacts = dbvoc.getBeat_id(items);
					for (Local_Data cn : contacts) {

						B_ID = cn.getBEAT_ID();
						Global_Data.Glovel_BEAT_ID = B_ID;
					}


					if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(B_ID)) {
						list_cities.clear();
						List<Local_Data> contacts2 = dbvoc.getcustomerByCityName(B_ID);
						for (Local_Data cn : contacts2) {

//					if(!cn.getPURPOSE_ADDRESS().equalsIgnoreCase(null) && !cn.getPURPOSE_ADDRESS().equalsIgnoreCase("null") && !cn.getPURPOSE_ADDRESS().equalsIgnoreCase(""))
//					{
							list_cities.add(cn.get_stocks_product_name()); //+ ":" +cn.getPURPOSE_ADDRESS());
//					}
//					else
//					{
//						list_cities.add(cn.get_stocks_product_name());
//					}

						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
								android.R.layout.simple_spinner_dropdown_item,
								list_cities);
						autoCompleteTextView1.setThreshold(1);// will start working from
						// first character
						autoCompleteTextView1.setAdapter(adapter);// setting the adapter
						// data into the
						// AutoCompleteTextView
						autoCompleteTextView1.setTextColor(Color.BLACK);
					}
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		dialog.dismiss();
		Intent i = new Intent(Order.this, Sales_Dash.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		startActivity(i);
		finish();
	}


	public void showstate(String state_name, String cityname) {
		if (state_flag == 0) {

			++state_flag;
			int sp_position = adapter_state1.getPosition(state_name
					.toUpperCase());
			state_spinner.setSelection(sp_position);
			Global_Data.GLOvel_CITY = cityname.toUpperCase();

			Global_Data.GLOvel_STATE = state_name.toUpperCase();
			dialog.dismiss();
		}
	}

	public void getScheduleData()
	{

		//calendarn = Calendar.getInstance();
		//year = calendarn.get(Calendar.YEAR);
		loginDataBaseAdapter=new LoginDataBaseAdapter(Order.this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		dialog = new ProgressDialog(Order.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

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

			SharedPreferences spf = Order.this.getSharedPreferences("SimpleLogic", 0);
			String user_email = spf.getString("USER_EMAIL",null);

			Log.i("volley", "domain: " + domain);
			Log.i("volley", "email: " + user_email);
			Log.i("target url", "target url " + domain+"delivery_schedules?imei_no="+device_id+"&customer_code="+Global_Data.GLOvel_CUSTOMER_ID+"&email="+user_email);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain+"delivery_schedules?imei_no="+device_id+"&customer_code="+Global_Data.GLOvel_CUSTOMER_ID+"&email="+user_email,null, new Response.Listener<JSONObject>() {

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
//
//							Toast toast = Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
							Global_Data.Custom_Toast(Order.this, response_result,"yes");

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

//								Toast toast = Toast.makeText(Order.this, getResources().getString(R.string.Delivery_Schedule_Not_Found), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

								Global_Data.Custom_Toast(Order.this, getResources().getString(R.string.Delivery_Schedule_Not_Found),"yes");
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
								Intent intent = new Intent(Order.this, Schedule_List.class);
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

//					Toast toast = Toast.makeText(Order.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(Order.this, getResources().getString(R.string.Server_Error), "yes");

					dialog.dismiss();

				}
			});

			RequestQueue requestQueue = Volley.newRequestQueue(Order.this);
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

	public void getPrevious_OrderData()
	{
		//calendarn = Calendar.getInstance();
		//year = calendarn.get(Calendar.YEAR);
		loginDataBaseAdapter=new LoginDataBaseAdapter(Order.this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		dialog = new ProgressDialog(Order.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setMessage(getResources().getString(R.string.Please_Wait_Previous_Sync));
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

			SharedPreferences spf = Order.this.getSharedPreferences("SimpleLogic", 0);
			String user_email = spf.getString("USER_EMAIL",null);

			Log.i("volley", "domain: " + domain);
			Log.i("volley", "email: " + user_email);
            Log.i("previous_order url", "previous_order url " + domain + "customers/previous_order?imei_no=" + device_id + "&customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain+"customers/previous_order?imei_no="+device_id+"&customer_code="+Global_Data.GLOvel_CUSTOMER_ID+"&email="+user_email,null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.i("volley", "response: " + response);
					//  Log.i("volley", "response reg Length: " + response.length());
					try{

						String response_result = "";
						if(response.has("message"))
						{
							response_result = response.getString("message");

//							Toast toast = Toast.makeText(getApplicationContext(),response_result,Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
							Global_Data.Custom_Toast(getApplicationContext(),response_result,"yes");
						}
						else {

							dbvoc.getDeleteTable("previous_order_products");
							dbvoc.getDeleteTable("previous_orders");

							//JSONArray previous_orders = response.getJSONArray("previous_orders");
							JSONArray previous_order_products = response.getJSONArray("order_products");

							//Log.i("volley", "response reg previous_orders Length: " + previous_orders.length());
							Log.i("volley", "response reg previous_order_products Length: " + previous_order_products.length());


							//	Log.d("States", "previous_orders" + previous_orders.toString());
							Log.d("States", "previous_order_products" + previous_order_products.toString());


							//
							if(previous_order_products.length() <= 0)
							{
								dialog.dismiss();
								//Toast.makeText(Order.this, "Previous order not found.", Toast.LENGTH_LONG).show();

//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Previous_order_not_found), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Previous_order_not_found),"yes");
							}
							else
							{
//								for (int i = 0; i < previous_orders.length(); i++) {
//
//									JSONObject jsonObject = previous_orders.getJSONObject(i);
//
//								}

								for (int i = 0; i < previous_order_products.length(); i++) {

									JSONObject jsonObject = previous_order_products.getJSONObject(i);

									Global_Data.Previous_Order_UpdateOrder_ID = jsonObject.getString("order_number").trim();
									Global_Data.Previous_Order_ServiceOrder_ID = jsonObject.getString("order_number").trim();

									loginDataBaseAdapter.insertPreviousOrderProducts("", "", jsonObject.getString("order_number"), "","","","", "",jsonObject.getString("scheme_code"), " ", "",jsonObject.getString("total_qty"),jsonObject.getString("retail_price"),jsonObject.getString("mrp"),jsonObject.getString("amount"), "", "",""," ",jsonObject.getString("product_code")," ",jsonObject.getString("product_name"));


								}

								Intent intent = new Intent(Order.this, Previous_orderNew_S2.class);
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

//					Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Server_Error),"yes");
					dialog.dismiss();

				}
			});

			RequestQueue requestQueue = Volley.newRequestQueue(Order.this);
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

	public void hearShake() {
		//Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();

		try {
			camera = Camera.open();
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			camera.stopPreview();
			camera.release();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		isInternetPresent = cd.isConnectingToInternet();
		if (service_flag.equalsIgnoreCase("") && isInternetPresent) {
			service_flag = "true";
			//sd.register();

			View_NearestCustomer_order("", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE);
		} else {
			if (!isInternetPresent) {
//				Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nearest_internet), Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
				Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.nearest_internet),"yes");
			}

		}

	}



	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		//sd.deregister();
	}

	public void View_NearestCustomer_order(String address, String latitude, String longitude) {


		loginDataBaseAdapter = new LoginDataBaseAdapter(Order.this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();


		// dbvoc = new DataBaseHelper(context);

		//PreferencesHelper Prefs = new PreferencesHelper(context);
		//String URL = Prefs.GetPreferences("URL");



		Order.this.runOnUiThread(new Runnable() {
			public void run() {

				if (!((Activity) Order.this).isFinishing()) {
					dialog.setMessage(getResources().getString(R.string.Nearest_customer_searching));
					dialog.setTitle("Metal");
					dialog.setCancelable(false);
					dialog.show();
				}

			}
		});


        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
		String device_id = sp.getString("devid", "");
        String domain = service_url;

		SharedPreferences spf = Order.this.getSharedPreferences("SimpleLogic", 0);
		String user_email = spf.getString("USER_EMAIL",null);

		// Global_Val global_Val = new Global_Val();
//        if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//            domain = context.getResources().getString(R.string.service_domain);
//        }
//        else
//        {
//            domain = URL.toString();
//        }


		try {
			Log.d("Server url", "Server url" + domain + "customer_list?imei_no=" + device_id + "&latitude="
					+ URLEncoder.encode(latitude, "UTF-8") + "&longitude="
					+ URLEncoder.encode(longitude, "UTF-8") + "&email="
					+ user_email);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringRequest stringRequest = null;
		try {
			stringRequest = new StringRequest(Request.Method.GET, domain + "customer_list?imei_no=" + device_id + "&latitude="
					+ URLEncoder.encode(latitude, "UTF-8") + "&longitude="
					+ URLEncoder.encode(longitude, "UTF-8") + "&email="
					+ user_email,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							//showJSON(response);
							// Log.d("jV", "JV" + response);
							Log.d("jV", "JV length" + response.length());
							// JSONObject person = (JSONObject) (response);
							try {
								JSONObject json = new JSONObject(new JSONTokener(response));
								try {


									String response_result = "";
									if (json.has("message")) {
										response_result = json.getString("message");
									} else {
										response_result = "data";
									}


									if (response_result.equalsIgnoreCase("User not found")) {

										//Toast.makeText(Nearest_Customer.this, response_result, Toast.LENGTH_LONG).show();

//										Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//										toast.setGravity(Gravity.CENTER, 0, 0);
//										toast.show();
										Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

									}
									if (response_result.equalsIgnoreCase("Customers not found")) {

										// Toast.makeText(Nearest_Customer.this, response_result, Toast.LENGTH_LONG).show();

//										Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//										toast.setGravity(Gravity.CENTER, 0, 0);
//										toast.show();
Global_Data.Custom_Toast(getApplicationContext(), response_result,"yes");

									} else if (json.getJSONArray("data").length() <= 0) {

										// Toast.makeText(Nearest_Customer.this, "Customers not found", Toast.LENGTH_LONG).show();

//										Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Customers_not_found), Toast.LENGTH_LONG);
//										toast.setGravity(Gravity.CENTER, 0, 0);
//										toast.show();
										Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Customers_not_found),"yes");


									} else {

										JSONArray data = json.getJSONArray("data");
										Log.d("data", "data" + data.toString());

										Log.d("data", "data length" + data.length());
										// Log.d("customers", "customers" + customers.toString());
										// Log.d("devices", "devices" + devices.toString());

										try {
											Double min = 101.0;
											String cus_name = "";
											String cus_address = "";
											String str = "";


											for (int i = 0; i < data.length(); i++) {

												JSONObject jsonObject = data.getJSONObject(i);

												if (i == 0) {
													cus_name = jsonObject.getString("customer_name");
													cus_address = jsonObject.getString("customer_address");
													str = jsonObject.getString("distance");
													Log.d("cus_address", "cus_address" + cus_address);
												}


//												String[] splited = str.split("\\s+");
//
//												double km = Double.valueOf(splited[0]);
//												double kmtometer = km * m;
//
//												if (kmtometer <= 100.0) {
//													if (kmtometer < min) {
//														min = kmtometer;
//														cus_name = jsonObject.getString("customer_name");
//														cus_address = jsonObject.getString("customer_address");
//
//													}
//													Log.d("small", "mall" + kmtometer);
//												}

											}

//


											if (!cus_name.equalsIgnoreCase("")) {
												List<Local_Data> contacts = dbvoc.getCustomerCode(cus_name);
												if (contacts.size() <= 0) {
//													Toast toast = Toast.makeText(Order.this,
//															getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//													toast.setGravity(Gravity.CENTER, 0, 0);
//													toast.show();
													Global_Data.Custom_Toast(Order.this,
															getResources().getString(R.string.Customer_Not_Found), "yes");
												} else {
													for (Local_Data cn : contacts) {

														Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
														Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
														Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
														Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
														Global_Data.CustLandlineNo = cn.getLANDLINE_NO();

														List<Local_Data> statei = dbvoc.getstateby_id(cn.getSTATE_ID());
														if (statei.size() > 0) {
															for (Local_Data cnn : statei) {
																state_name = cnn.getStateName();
															}
														}

														List<Local_Data> cityi = dbvoc.getcityByState_idn(cn.getCITY_ID());
														if (cityi.size() > 0) {
															for (Local_Data cnnn : cityi) {
																city_name = cnnn.getCityName();
															}
														}

														List<Local_Data> beati = dbvoc.getbeatByCityIDn(cn.getBEAT_ID());
														if (beati.size() > 0) {
															for (Local_Data cnnnn : beati) {
																beat_name = cnnnn.getCityName();
															}
														}

														adapter_state1 = new ArrayAdapter<String>(Order.this,
																android.R.layout.simple_spinner_item, results1);

														// ArrayAdapter<String> adapter_state3 = new
														// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
														// results2);

														adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
														state_spinner.setAdapter(adapter_state1);
														state_spinner.setOnItemSelectedListener(Order.this);

														if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(state_name)) {
															int spinnerPosition = adapter_state1.getPosition(state_name);
															state_spinner.setSelection(spinnerPosition);
														}

														String habitnumber = "<b>" + getResources().getString(R.string.Address) + "</b> " + cn.getAddress();
														String mobile_number = "<b>" + getResources().getString(R.string.Mobile_Number_H) + "</b> " + "<u>" + cn.getMOBILE_NO() + "</u>";
														String landline_number = "<b>" + getResources().getString(R.string.Landline_Number_H) + "</b> " + "<u>" + cn.getLANDLINE_NO() + "</u>";
														c_mobile_number = cn.getMOBILE_NO();
														c_landline_number = cn.getLANDLINE_NO();
														customer_address.setText(Html.fromHtml(habitnumber));
														customer_MObile.setText(Html.fromHtml(mobile_number));
														customer_landline.setText(Html.fromHtml(landline_number));
														autoCompleteTextView1.setText(cus_name);
														//customer_address.setText("Address: "+cn.getAddress());
													}
												}

												List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Global_Data.GLOvel_CUSTOMER_ID);
												if (contactlimit.size() > 0) {
													credit_profile_layout.setVisibility(View.VISIBLE);
													for (Local_Data cn : contactlimit) {

														if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_credit_limit())) {
															Double credit_limit = ((Double.valueOf(cn.get_credit_limit())));
															ocredit_limit.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
														} else {
															ocredit_limit.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
														}

														if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_shedule_outstanding_amount())) {
															Double amt_outstanding = ((Double.valueOf(cn.get_shedule_outstanding_amount())));
															oamount_utstanding.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", amt_outstanding)));
															Global_Data.AmountOutstanding = cn.get_shedule_outstanding_amount();
														} else {
															oamount_utstanding.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
															Global_Data.AmountOutstanding = "0.0";
														}

														if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getAmmount_overdue())) {
															Double amt_overdue = ((Double.valueOf(cn.getAmmount_overdue())));
															oamount_overdue.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));

															Global_Data.AmountOverdue = cn.getAmmount_overdue();
														} else {
															oamount_overdue.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
															Global_Data.AmountOverdue = "0.0";
														}
													}
												} else {
													Global_Data.AmountOutstanding = "0.0";
													Global_Data.AmountOverdue = "0.0";
													credit_profile_layout.setVisibility(View.GONE);
													ocredit_limit.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
													oamount_utstanding.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
													oamount_overdue.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
												}
												autoCompleteTextView1.dismissDropDown();
											} else {
//												Toast toast = Toast.makeText(Order.this,
//														getResources().getString(R.string.Nearest_Customer_Not_Found), Toast.LENGTH_SHORT);
//												toast.setGravity(Gravity.CENTER, 0, 0);
//												toast.show();
												Global_Data.Custom_Toast(Order.this,
														getResources().getString(R.string.Nearest_Customer_Not_Found), "yes");
											}

//											Toast toastn = Toast.makeText(Order.this,
//													getResources().getString(R.string.Distance_of) + cus_name + getResources().getString(R.string.iso) + str, Toast.LENGTH_SHORT);
//											toastn.setGravity(Gravity.CENTER, 0, 0);
//											toastn.show();
											Global_Data.Custom_Toast(Order.this,
													getResources().getString(R.string.Distance_of) + cus_name + getResources().getString(R.string.iso) + str,"yes");
											dialog.dismiss();
										} catch (Exception ex) {
											ex.printStackTrace();
										}


										// Global_Val.STOCK_SERVICE_FLAG = "";
										dialog.dismiss();
										service_flag = "";
										//finish();

									}

									//  finish();
									// }

									// output.setText(data);
								} catch (JSONException e) {

									service_flag = "";
									e.printStackTrace();
									dialog.dismiss();


								}

								dialog.dismiss();
							} catch (JSONException e) {
								e.printStackTrace();
								service_flag = "";
								//  finish();
								dialog.dismiss();

							}
							dialog.dismiss();

						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							//Toast.makeText(Nearest_Customer.this, error.getMessage(), Toast.LENGTH_LONG).show();

							service_flag = "";
							if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//								Toast toast = Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), "Network Error","yes");
							} else if (error instanceof AuthFailureError) {
//                              Toast.makeText(Nearest_Customer.this,
//                                      "Server AuthFailureError  Error",
//                                      Toast.LENGTH_LONG).show();
//								Toast toast = Toast.makeText(getApplicationContext(), "Server AuthFailureError  Error", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), "Server AuthFailureError  Error","yes");
							} else if (error instanceof ServerError) {

//								Toast toast = Toast.makeText(getApplicationContext(), "Customers not found", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), "Customers not found","yes");
							} else if (error instanceof NetworkError) {
//
//								Toast toast = Toast.makeText(getApplicationContext(), "Network   Error", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), "Network   Error","yes");
							} else if (error instanceof ParseError) {
//								Toast toast = Toast.makeText(getApplicationContext(), "ParseError   Error", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), "ParseError   Error","yes");
							} else {
								// Toast.makeText(Nearest_Customer.this, error.getMessage(), Toast.LENGTH_LONG).show();

//								Toast toast = Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
								Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"yes");
							}
							dialog.dismiss();

							// finish();
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}

		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(Order.this);
			Log.d("new error", "Setting a new request queue");
		}

		int socketTimeout = 300000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		stringRequest.setRetryPolicy(policy);
		// requestQueue.se
		//requestQueue.add(jsObjRequest);
		stringRequest.setShouldCache(false);
		requestQueue.getCache().clear();
		requestQueue.add(stringRequest);
	}

	@Override
	protected void onDestroy() {
		try {

			sd.stop();

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}



	/**
	 * Requesting GPS permission
	 * This uses single permission model from dexter
	 * Once the permission granted, opens the camera
	 * On permanent denial opens settings dialog
	 */
	private void requestPhoneCallPermission(final String mobile_number) {
		Dexter.withActivity(this)
				.withPermission(Manifest.permission.CALL_PHONE)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse response) {

						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:" + mobile_number));
						startActivity(callIntent);

						return;

					}

					@Override
					public void onPermissionDenied(PermissionDeniedResponse response) {
						// check for permanent denial of permission
						if (response.isPermanentlyDenied()) {
							showSettingsDialog();
						}
					}

					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
						token.continuePermissionRequest();
					}
				}).check();
	}

	/**
	 * Showing Alert Dialog with Settings option
	 * Navigates user to app settings
	 * NOTE: Keep proper title and message depending on your app
	 */
	private void showSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Order.this);
		builder.setTitle(getResources().getString(R.string.need_permission_message));
		builder.setCancelable(false);
		builder.setMessage(getResources().getString(R.string.need_permission_setting_message));
		builder.setPositiveButton(getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				openSettings();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();

	}

	// navigating user to app settings
	private void openSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", getPackageName(), null);
		intent.setData(uri);
		startActivityForResult(intent, 101);
	}

}
