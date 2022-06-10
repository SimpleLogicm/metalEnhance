package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.toolbox.JsonObjectRequest;
import com.msimplelogic.activities.R;
import com.msimplelogic.slidingmenu.CalendarAct;
import com.msimplelogic.webservice.ConnectionDetector;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class Calendar_Event extends BaseActivity{
	EditText details;
	Date date1;
	Date date2;
	private String Current_Date = "";
	Button submit,submit_details_save,submit_details_delete;
	ProgressDialog dialog;
	LoginDataBaseAdapter loginDataBaseAdapter;
	TextView from,to;
	DataBaseHelper dbvoc = new DataBaseHelper(this);
	private DatePickerDialog fromDatePickerDialog,fromDatePickerDialog1;
	private SimpleDateFormat dateFormatter;
	private static final String tag = "Calendar_Event";
	String popUpContents[];
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	TextView textView3,tvTime;
	String c_user_id = "";
	String c_id = "";
	String update_flag = "";
	TimePickerDialog picker;
	private Calendar calendar;
	private String format = "";

	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.calendar_event);
	    from=(TextView)findViewById(R.id.from_details);
		to=(TextView)findViewById(R.id.to_details);
		textView3=(TextView)findViewById(R.id.textView3);
		tvTime=(TextView)findViewById(R.id.tv_time);
		details=(EditText)findViewById(R.id.details);
		submit=(Button)findViewById(R.id.submit_details);

		//submit_details_save = (Button)findViewById(R.id.submit_details_save);
		submit_details_delete = (Button)findViewById(R.id.submit_details_delete);

		details.setFilters(new InputFilter[]{filter});

		cd = new ConnectionDetector(Calendar_Event.this);
		dateFormatter = new SimpleDateFormat("MMMM-yyyy", Locale.US);

		calendar = Calendar.getInstance();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		//showTime(hour, min);

		if (Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase(getResources().getString(R.string.Travel_Planner1)))
		{
            textView3.setText(getResources().getString(R.string.Travel_Details));
		}
		else if (Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase(getResources().getString(R.string.Leave_Management1)))
		{
            textView3.setText(getResources().getString(R.string.Leave_Details));
		}
		else if (Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase(getResources().getString(R.string.Task11)))
		{
            textView3.setText(getResources().getString(R.string.Task_Details));
		}


		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		String strDate = sdf.format(c.getTime());
		Current_Date = sdf.format(c.getTime());
		 
		Calendar newCalendar = Calendar.getInstance();

		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		//int monthold = calendar.get(Calendar.MONTH)-1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		Calendar calendarnew = Calendar.getInstance();
		calendarnew.add(Calendar.MONTH ,-1);
		int monthn = calendarnew.get(Calendar.MONTH);

		Formatter fmtt = new Formatter();
		// fmt.format("%tB %tb %tm", calendar, calendar, calendar);

		String mmm = fmtt.format("%tB", calendarnew).toString();

		//Calendar cal = Calendar.getInstance();
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
		Formatter fmt = new Formatter();
		// fmt.format("%tB %tb %tm", calendar, calendar, calendar);

		String mm = fmt.format("%tB", calendar).toString();

		Log.d("C Month","C Month"+mm);

		Log.d("C Month old","C Month old"+mmm);
		Log.d("C MAXDAY","C MAXDAY"+days);
		Log.d("C year","C year"+year);
		Log.d("C year","C year"+year);

		String e_name = "";
		if (Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase(getResources().getString(R.string.Travel_Planner1)))
		{
			e_name = "Travel";
		}
		else if (Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase(getResources().getString(R.string.Leave_Management1)))
		{
			e_name = "Leave";
		}
		else if (Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase(getResources().getString(R.string.Task11)))
		{
			e_name = "Task";
		}

		List<Local_Data> contacts2 = dbvoc.getCalender_EventValue(Global_Data.select_date,e_name,"NO");

		if(contacts2.size() <= 0 )
		{
			from.setText(Global_Data.select_date);
			update_flag = "";
			submit_details_delete.setVisibility(View.INVISIBLE);
		}
		else
		{
			for (Local_Data cn : contacts2) {

				from.setText(Global_Data.select_date);
				to.setText(cn.getto_date());
				details.setText(cn.getcalender_details());

				c_user_id = cn.getuser_email();
				c_id = cn.getcalender_id();
			//	c_user_id = cn.getuser_email();


			}
            submit.setText(getResources().getString(R.string.Update));
			submit_details_delete.setVisibility(View.VISIBLE);
			update_flag = "TRUE";
		}

		tvTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Calendar cldr = Calendar.getInstance();
				int hour = cldr.get(Calendar.HOUR_OF_DAY);
				int minutes = cldr.get(Calendar.MINUTE);
				// time picker dialog
				picker = new TimePickerDialog(Calendar_Event.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

								//showTime(sHour, sMinute);
								tvTime.setText(sHour + ":" + sMinute);
							}
						}, hour, minutes, true);
				picker.show();
			}
		});

		submit_details_delete.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  AlertDialog alertDialog = new AlertDialog.Builder(Calendar_Event.this).create(); //Read Update
                  alertDialog.setTitle(getResources().getString(R.string.Warning));
                  alertDialog.setMessage(getResources().getString(R.string.event_dialog_warning_message));
                  alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

					  @Override
					  public void onClick(DialogInterface dialog, int which) {


						  dbvoc.updateCalendervalue("YES",c_id);
						  dbvoc.updateORDER_SIGNATUREnew(Current_Date,c_id);

						  Global_Data.Custom_Toast(Calendar_Event.this, getResources().getString(R.string.event_dialog_delete_success),"Yes");
//
//                          Toast toast = Toast.makeText(Calendar_Event.this, getResources().getString(R.string.event_dialog_delete_success),
//								  Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();

						  Intent intentn = new Intent(getApplicationContext(), CalendarAct.class);
						  startActivity(intentn);
						  finish();
						  //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

					  }
				  });

                  alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

					  @Override
					  public void onClick(DialogInterface dialog, int which) {

						  dialog.cancel();

					  }
				  });
				  alertDialog.show();

			  }

		  });


		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            Calendar newDate = Calendar.getInstance();
	            newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg=Integer.toString(year);
	            String mnth_reg=Integer.toString(monthOfYear+1);
	            String date_reg=Integer.toString(dayOfMonth);
	          
	            from.setText(date_reg+"-"+(dateFormatter.format(newDate.getTime())));
	        }
	        
	    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		fromDatePickerDialog1 = new DatePickerDialog(this, new OnDateSetListener() {

	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            Calendar newDate = Calendar.getInstance();
	            newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg=Integer.toString(year);
	            String mnth_reg=Integer.toString(monthOfYear+1);
	            String date_reg=Integer.toString(dayOfMonth);
	          
	            to.setText(date_reg+"-"+(dateFormatter.format(newDate.getTime())));
	        }
	        
	    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		
		from.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fromDatePickerDialog.getWindow().getAttributes().verticalMargin = 0.5F;
				fromDatePickerDialog.show();
				//fromDatePickerDialog.setlo
				Locale locale = getResources().getConfiguration().locale;
				Locale.setDefault(locale);
			}
		});
		
		to.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fromDatePickerDialog1.getWindow().getAttributes().verticalMargin = 0.5F;
				fromDatePickerDialog1.show();
			}
		});
		
		
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(from.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(to.getText().toString()))
				{
					 date1 = new Date(from.getText().toString());
					 date2 = new Date(to.getText().toString());
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(date1);
					cal2.setTime(date1);
				}


				if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(from.getText().toString()))
					 {

					 	Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Select_From_Date),"Yes");
//                         Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Select_From_Date), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(to.getText().toString()))
					 {
						 //Toast.makeText(getApplicationContext(),"Please Select To Date", Toast.LENGTH_LONG).show();
						 Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Select_To_Date),"Yes");
//                         Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Select_To_Date), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
					 }
				     else
					 if(date1.compareTo(date2)>0)
					 {
						//System.out.println("Date1 is after Date2");
						//Toast.makeText(getApplicationContext(),"To Date not a valid date.", Toast.LENGTH_LONG).show();
						 Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.To_date_validation_message),"Yes");
//                         Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.To_date_validation_message), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(details.getText().toString()))
					 {
					 	Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Travel_Details_vali_message),"Yes");

//                         Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Travel_Details_vali_message), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
					 }
//					 else
//					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(tvTime.getText().toString()))
//					 {
//
//						 Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enter_time), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
//					 }
					 else
					 {

//						 SecureRandom random = new SecureRandom();
//						loginDataBaseAdapter.insertCalenderEntries("", "", Global_Data.GLOvel_USER_EMAIL, new BigInteger(130,random).toString(32),Global_Data.CALENDER_EVENT_TYPE, from.getText().toString().trim(), to.getText().toString().trim(), details.getText().toString().trim(),  Global_Data.lat_val+","+Global_Data.long_val, Current_Date, Current_Date);

//						 isInternetPresent = cd.isConnectingToInternet();
//
//						if (isInternetPresent)
//	                    {
							call_service_Calender_Event();
//	                    }
//		   	        	else
//		   	        	{
//		   	        	 //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//							Toast toast = Toast.makeText(getApplicationContext(),"You don't have internet connection.", Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
//		   	        	}
			 			
						 
//						 Toast.makeText(getApplicationContext(),"Your Data Submit Successfuly", Toast.LENGTH_LONG).show();
//						 
//						 Intent intent = new Intent(Expenses.this, Order.class);
//						 startActivity(intent);
//						 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//						 finish();
					 }
				 
			
				
			}
		});

//		submit_details_save.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(from.getText().toString()))
//				{
//					//Toast.makeText(getApplicationContext(),"Please Select From Date", Toast.LENGTH_LONG).show();
//					Toast toast = Toast.makeText(getApplicationContext(),"Please Select From Date", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//				}
//				else
//				if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(to.getText().toString()))
//				{
//					//Toast.makeText(getApplicationContext(),"Please Select To Date", Toast.LENGTH_LONG).show();
//					Toast toast = Toast.makeText(getApplicationContext(),"Please Select To Date", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//				}
//				else
//				if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(details.getText().toString()))
//				{
//					//Toast.makeText(getApplicationContext(),"Please Enter Travel Details", Toast.LENGTH_LONG).show();
//					Toast toast = Toast.makeText(getApplicationContext(),"Please Enter Travel Details", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//				}
//				else
//				{
//
//
//					if(update_flag.equalsIgnoreCase("TRUE"))
//					{
//						dbvoc.getDeleteTablecalender_event(c_id);
//						loginDataBaseAdapter.insertCalenderEntries("", "", c_user_id,c_id,Global_Data.CALENDER_EVENT_TYPE, from.getText().toString().trim(), to.getText().toString().trim(), details.getText().toString().trim(),  Global_Data.lat_val+","+Global_Data.long_val, Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,"");
//
//						//Toast.makeText(getApplicationContext(),"Update Successfully.",Toast.LENGTH_LONG).show();
//						Toast toast = Toast.makeText(getApplicationContext(),"Update Successfully.", Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}
//					else
//					{
//						SecureRandom random = new SecureRandom();
//						loginDataBaseAdapter.insertCalenderEntries("", "", Global_Data.GLOvel_USER_EMAIL, new BigInteger(130,random).toString(32),Global_Data.CALENDER_EVENT_TYPE, from.getText().toString().trim(), to.getText().toString().trim(), details.getText().toString().trim(),  Global_Data.lat_val+","+Global_Data.long_val, Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,"");
//
//						//Toast.makeText(getApplicationContext(),"Save Successfully.",Toast.LENGTH_LONG).show();
//
//						Toast toast = Toast.makeText(getApplicationContext(),"Save Successfully.", Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}
//
//
//					Intent a = new Intent(Calendar_Event.this,MainActivity.class);
//					startActivity(a);
//					finish();
//
//
//
////					isInternetPresent = cd.isConnectingToInternet();
////
////					if (isInternetPresent)
////					{
////						call_service_Calender_Event();
////					}
////					else
////					{
////						Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
////					}
//
//
////						 Toast.makeText(getApplicationContext(),"Your Data Submit Successfuly", Toast.LENGTH_LONG).show();
////
////						 Intent intent = new Intent(Expenses.this, Order.class);
////						 startActivity(intent);
////						 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////						 finish();
//				}
//
//
//
//			}
//		});
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);
            Intent i = getIntent();
            String name = i.getStringExtra("retialer");
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(Global_Data.calspinner);

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);

            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
            H_LOGO.setImageResource(R.drawable.cal);
            H_LOGO.setVisibility(View.INVISIBLE);

            SharedPreferences sp = Calendar_Event.this.getSharedPreferences("SimpleLogic", 0);

//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	//todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//			todaysTarget.setText("Target/Acheived : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
//		}
            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
	
	public void call_service_Calender_Event()
	{
		System.gc();
		String reason_code = "";	
		try {
			
//			DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//			DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
//			Date date1 = originalFormat.parse(getDateTime());
//			String formattedDate = targetFormat.format(date1);
			
			
			 
			 
//		    dialog = new ProgressDialog(Calendar_Event.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//	        dialog.setMessage("Please wait....");
//	        dialog.setTitle("Metal");
//	        dialog.setCancelable(false);
//	        dialog.show();


            String device_id = "";
			 
			 


            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            String domain = service_url;

		       // Global_Val global_Val = new Global_Val();
//		        if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//		            domain = context.getResources().getString(R.string.service_domain);
//		        }
//		        else
//		        {
//		            domain = URL.toString();
//		        }
			// StringRequest stringRequest = null;
			 
			 JsonObjectRequest jsObjRequest = null;
			 try
			 {



				// SecureRandom random = new SecureRandom();

				 String code = "";

				 if(update_flag.equalsIgnoreCase("TRUE"))
				 {
					 dbvoc.getDeleteTablecalender_event(c_id);

					 String event_nname = "";

					 if(Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase("Travel Planner"))
					 {
						 event_nname = "Travel";
					 }
					 else
					 if(Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase("Leave Management"))
					 {
						 event_nname = "Leave";
					 }
					 else
					 if(Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase("Task"))
					 {
						 event_nname = "Task";
					 }

					 try
					 {
						 AppLocationManager appLocationManager = new AppLocationManager(Calendar_Event.this);
						 Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
						 Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

						 PlayService_Location PlayServiceManager = new PlayService_Location(Calendar_Event.this);

						 if(PlayServiceManager.checkPlayServices(Calendar_Event.this))
						 {
							 Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);


						 }
						 else
						 if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
						 {
							 Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
							 Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
						 }

					 }catch(Exception ex){ex.printStackTrace();}

					 String dsd=tvTime.getText().toString().trim();
					 loginDataBaseAdapter.insertCalenderEntries("", "", c_user_id,c_id,event_nname, from.getText().toString().trim(), to.getText().toString().trim(), details.getText().toString().trim(),  Global_Data.lat_val+","+Global_Data.long_val, Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,"NO",dsd);

					 code =  c_id;

					 String gaddress = "";

					 try {
						 if (Global_Data.address.equalsIgnoreCase("null")) {
							 gaddress = "";
						 } else {
							 gaddress = Global_Data.address;
						 }
					 }catch(Exception ex){ex.printStackTrace();}

					 if(!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null")  && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("")  && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" "))
					 {
						// Global_Data.sendSMS(Global_Data.cus_MAnager_mobile,sms_body, Calendar_Event.this);
						 // mobile_numbers.add(Global_Data.cus_MAnager_mobile);
					 }

					 Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Calender_Entry_Update_Message), "");
                    // Toast.makeText(getApplicationContext(), getResources().getString(R.string.Calender_Entry_Update_Message), Toast.LENGTH_LONG).show();
                     Intent a = new Intent(Calendar_Event.this,CalendarAct.class);
					 startActivity(a);
					 finish();
				 }
				 else
				 {
					 String gaddress = "";

					 try {
						 if (Global_Data.address.equalsIgnoreCase("null")) {
							 gaddress = "";
						 } else {
							 gaddress = Global_Data.address;
						 }
					 }catch(Exception ex){ex.printStackTrace();}


					 String event_nname = "";

					 if(Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase("Travel Planner"))
					 {
						 event_nname = "Travel";
					 }
					 else
					 if(Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase("Leave Management"))
					 {
						 event_nname = "Leave";
					 }
					 else
					 if(Global_Data.CALENDER_EVENT_TYPE.equalsIgnoreCase("Task"))
					 {
						 event_nname = "Task";
					 }

					 try
					 {
						 AppLocationManager appLocationManager = new AppLocationManager(Calendar_Event.this);
						 Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
						 Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

						 PlayService_Location PlayServiceManager = new PlayService_Location(Calendar_Event.this);

						 if(PlayServiceManager.checkPlayServices(Calendar_Event.this))
						 {
							 Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

						 }
						 else
						 if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
						 {
							 Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
							 Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
						 }

					 }catch(Exception ex){ex.printStackTrace();}

					 SecureRandom random = new SecureRandom();
					 String dsd=tvTime.getText().toString().trim();

					 SharedPreferences spf = Calendar_Event.this.getSharedPreferences("SimpleLogic", 0);
					 String user_email = spf.getString("USER_EMAIL",null);

					 loginDataBaseAdapter.insertCalenderEntries("", "", user_email, new BigInteger(130,random).toString(32),event_nname, from.getText().toString().trim(), to.getText().toString().trim(), details.getText().toString().trim(),  Global_Data.lat_val+","+Global_Data.long_val, Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,"NO",dsd);

					 code =  new BigInteger(130,random).toString(32);

					 Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Calender_Entry_Created_Message),"");
                 //    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Calender_Entry_Created_Message), Toast.LENGTH_LONG).show();

					 Intent a = new Intent(Calendar_Event.this,CalendarAct.class);
					 startActivity(a);
					 finish();
				 }
			
				 Log.d("Server url","Server url"+domain+"calendars/create_calender_entry");
                   
				 
			 }catch(Exception e)
			 {
				 e.printStackTrace();
				 //dialog.dismiss();
			 }


        } catch (Exception e) {
			// TODO: handle exception
			Log.e("DATA", e.getMessage());
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
	}

	public static String getlength(int len){
		int number = len;
		int length = (int) Math.log10(number) + 1;
		if(length == 1)
		{
			return "0"+len;
		}
		else
		{
			return ""+len;
		}

	}

	InputFilter filter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				int type = Character.getType(source.charAt(i));
				//System.out.println("Type : " + type);
				if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
					return "";
				}
			}
			return null;
		}
	};

//	public void setTime(View view) {
//		int hour = timePicker1.getCurrentHour();
//		int min = timePicker1.getCurrentMinute();
//		showTime(hour, min);
//	}

	public void showTime(int hour, int min) {
		if (hour == 0) {
			hour += 12;
			format = "AM";
		} else if (hour == 12) {
			format = "PM";
		} else if (hour > 12) {
			hour -= 12;
			format = "PM";
		} else {
			format = "AM";
		}

		tvTime.setText(new StringBuilder().append(hour).append(" : ").append(min)
				.append(" ").append(format));
	}


}
