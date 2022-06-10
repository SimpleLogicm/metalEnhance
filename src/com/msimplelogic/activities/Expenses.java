package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.itextpdf.text.pdf.fonts.otf.GlyphPositioningTableReader;
import com.jsibbold.zoomage.ZoomageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.webservice.ConnectionDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Expenses extends Activity implements OnItemSelectedListener {
	private DatePickerDialog fromDatePickerDialog,fromDatePickerDialog1;
	private SimpleDateFormat dateFormatter;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	String encr_iamgecode = "";
	byte b5[];
	ImageView img_show;
	ZoomageView imgdialog;
	ImageView crossimg;
	private String mCurrentPhotoPath = "";
	Boolean B_flag;
	String image_check = "";
	DataBaseHelper dbvoc = new DataBaseHelper(this);
	private String pictureImagePath = "";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
	public static final int MY_PERMISSIONS_REQUEST_STORAGE = 200;
	String popUpContents[];
	PopupWindow popupWindowDogs;
	ProgressDialog dialog;
	int spine_flag = 0;
	Spinner type_spinner;
	EditText exp_date,exp_cost,exp_discr,exp_from,exp_to,exp_mot;
	Button exp_submit,exp_uploadpdf;
	LoginDataBaseAdapter loginDataBaseAdapter;
	private String Current_Date = "";
	Date date2;
	ImageView expence_cam;
    private String Expenses_text;
    private String[] city_state;
	//Pdf request code
	private int PICK_PDF_REQUEST = 1;
	private Uri filePath1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expenses);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
		type_spinner=(Spinner)findViewById(R.id.expenses);
		exp_date=(EditText)findViewById(R.id.exp_date);
		exp_cost=(EditText)findViewById(R.id.exp_cost);
		exp_discr=(EditText)findViewById(R.id.exp_discr);
		exp_from=(EditText)findViewById(R.id.exp_from);
		exp_to=(EditText)findViewById(R.id.exp_to);
		exp_mot=(EditText)findViewById(R.id.exp_mot);
		exp_submit=(Button)findViewById(R.id.exp_submit);
		//exp_uploadpdf=(Button)findViewById(R.id.exp_uploadpdf);
		expence_cam = (ImageView) findViewById(R.id.expence_cam);

		exp_discr.setFilters(new InputFilter[]{filter});
		exp_to.setFilters(new InputFilter[]{filter});
		exp_from.setFilters(new InputFilter[]{filter});
		exp_mot.setFilters(new InputFilter[]{filter});
		
		dateFormatter = new SimpleDateFormat("MMMM-yyyy", Locale.US);
		Calendar newCalendar = Calendar.getInstance();
		cd = new ConnectionDetector(Expenses.this);

        city_state = new String[]{getResources().getString(R.string.Select_Type), getResources().getString(R.string.Travel), getResources().getString(R.string.Miscellaneous)};
        Expenses_text = getResources().getString(R.string.Select);

		img_show=findViewById(R.id.img_show);
		crossimg = findViewById(R.id.crossimg);
		crossimg.setVisibility(View.INVISIBLE);

		crossimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				deletedialog();
			}
		});

		img_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mCurrentPhotoPath!="") {
					AlertDialog.Builder builder = new AlertDialog.Builder(Expenses.this);
					LayoutInflater inflater = getLayoutInflater();
					View dialogLayout = inflater.inflate(R.layout.dialogeimage, null);
					imgdialog = dialogLayout.findViewById(R.id.imageView);
					Glide.with(Expenses.this).load(mCurrentPhotoPath).into(imgdialog);
					builder.setPositiveButton("OK", null);
					builder.setView(dialogLayout);
					builder.show();
				}
			}
		});

		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            Calendar newDate = Calendar.getInstance();
	            newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg=Integer.toString(year);
	            String mnth_reg=Integer.toString(monthOfYear+1);
	            String date_reg=Integer.toString(dayOfMonth);
	          
	            exp_date.setText(date_reg+"-"+(dateFormatter.format(newDate.getTime())));
	        }
	        
	    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		exp_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fromDatePickerDialog.getWindow().getAttributes().verticalMargin = 0.5F;
				fromDatePickerDialog.show();
			}
		});

//		exp_uploadpdf.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				https://www.simplifiedcoding.net/upload-pdf-file-server-android/
////				https://www.android-examples.com/upload-pdf-file-server-android/
//				showFileChooser();
//			}
//		});
		
		ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, city_state);
			  
		  adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  type_spinner.setAdapter(adapter_state1);
		  type_spinner.setOnItemSelectedListener(this);
		  
		  loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		  loginDataBaseAdapter=loginDataBaseAdapter.open();
		  
		  	Calendar c = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
	        String strDate = sdf.format(c.getTime());
	        Current_Date = sdf.format(c.getTime());
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
            mTitleTextView.setText(getResources().getString(R.string.EXPENSES));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = Expenses.this.getSharedPreferences("SimpleLogic", 0);

            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
            H_LOGO.setImageResource(R.drawable.rs);
            H_LOGO.setVisibility(View.INVISIBLE);
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
//      	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

		expence_cam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				requestStoragePermission();

				}
		});

		exp_cost.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {


			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(!String.valueOf(s).equalsIgnoreCase(""))
				{
					if(Integer.parseInt(String.valueOf(s))<=0)
					{
						exp_cost.setText("");
					}
				}

			}
		});
      
      exp_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences spf = Expenses.this.getSharedPreferences("SimpleLogic", 0);
				String user_email = spf.getString("USER_EMAIL",null);

				Date date1 = new Date();

				if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_date.getText().toString()))
				{
					date2 = new Date(exp_date.getText().toString());
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(date1);
					cal2.setTime(date1);
				}

                if (Expenses_text == getResources().getString(R.string.Travel))
				 {
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_date.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Select Date", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Select_Date),"yes");
					 }
					 else
					 if(date2.compareTo(date1)>0)
					 {
						 //System.out.println("Date1 is after Date2");
						// Toast.makeText(getApplicationContext(),"Selected date can not to be a future date.", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Future_date_validation), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Future_date_validation),"yes");

					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_cost.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Enter Cost", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_Cost), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_Cost),"yes");
					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_discr.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Enter Description", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_Description),"yes");

					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_from.getText().toString()))
					 {
						 //Toast.makeText(getApplicationContext(),"Please Enter Form Field Data", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_Form_Field_Data), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();

						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_Form_Field_Data), "yes");

					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_to.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Enter To Field Data", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_To_Field_Data), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_To_Field_Data),"yes");
					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_mot.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Enter Mode of Travel", Toast.LENGTH_LONG).show();
//
//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_Mode_of_Travel), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_Mode_of_Travel),"yes");
					 }
					 else
					 {
						 Long randomPIN = System.currentTimeMillis();
						 String PINString = String.valueOf(randomPIN);

			 			 loginDataBaseAdapter.insertExpenceTravels("1", "1",user_email,
		        		 "1", exp_from.getText().toString(), exp_to.getText().toString(), exp_date.getText().toString(), exp_mot.getText().toString(), exp_cost.getText().toString(), exp_discr.getText().toString(),
								 "", "", "", "", Current_Date, Current_Date, PINString, pictureImagePath,"");
//
//						 Toast toast = Toast.makeText(getApplicationContext(),
//                                 getResources().getString(R.string.Travel_Expense_Save_Successfully), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
						 Global_Data.Custom_Toast(getApplicationContext(),
								 getResources().getString(R.string.Travel_Expense_Save_Successfully), "yes");

						 Intent a = new Intent(Expenses.this,MainActivity.class);
						 startActivity(a);
						 finish();

//						 isInternetPresent = cd.isConnectingToInternet();
//
//						if (isInternetPresent)
//	                    {
//							call_service_Expenses_TRAVEL(user_email,exp_from.getText().toString(),exp_to.getText().toString(), exp_date.getText().toString(), exp_mot.getText().toString(), exp_cost.getText().toString(), exp_discr.getText().toString(),
//				 					Current_Date, Current_Date);
//	                    }
//		   	        	else
//		   	        	{
//		   	        	   //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//							Toast toast = Toast.makeText(Expenses.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
//
//		   	        	}
			 			
						 
//						 Toast.makeText(getApplicationContext(),"Your Data Submit Successfuly", Toast.LENGTH_LONG).show();
//						 
//						 Intent intent = new Intent(Expenses.this, Order.class);
//						 startActivity(intent);
//						 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//						 finish();
					 }
				 }
				 else if (Expenses_text == getResources().getString(R.string.Miscellaneous))
				 {
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_date.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Select Date", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();

						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Select_Date),"yes");

					 }
					 else
					 if(date2.compareTo(date1)>0)
					 {
						 //System.out.println("Date1 is after Date2");
						 //Toast.makeText(getApplicationContext(),"Selected date can not to be a future date.", Toast.LENGTH_LONG).show();


//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Future_date_validation), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();

						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Future_date_validation),"yes");

					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_cost.getText().toString()))
					 {
						 //Toast.makeText(getApplicationContext(),"Please Enter Cost", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_Cost), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_Cost),"yes");

					 }
					 else
					 if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(exp_discr.getText().toString()))
					 {
						// Toast.makeText(getApplicationContext(),"Please Enter Description", Toast.LENGTH_LONG).show();

//                         Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
						 Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.Enter_Description),"yes");
					 }
					 else
					 {

						 Long randomPIN = System.currentTimeMillis();
						 String PINString = String.valueOf(randomPIN);

						 loginDataBaseAdapter.insertExpencesMiscs("1", "1", user_email, "1", exp_date.getText().toString(), exp_cost.getText().toString(), exp_discr.getText().toString(), "", "", "",
								 "", Current_Date, Current_Date, PINString, pictureImagePath,"");

//						 Toast toast = Toast.makeText(getApplicationContext(),
//                                 getResources().getString(R.string.Miscellaneous_Expense_Save_Successfully), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();

						 Global_Data.Custom_Toast(getApplicationContext(),
								 getResources().getString(R.string.Miscellaneous_Expense_Save_Successfully), "yes");

						 Intent a = new Intent(Expenses.this,MainActivity.class);
						 startActivity(a);
						 finish();
						 
//						  isInternetPresent = cd.isConnectingToInternet();
//
//							if (isInternetPresent)
//		                    {
//								call_service_Expenses_MISC(user_email, exp_date.getText().toString(), exp_cost.getText().toString(), exp_discr.getText().toString());
//		                    }
//			   	        	else
//			   	        	{
//			   	        	 //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//								Toast toast = Toast.makeText(Expenses.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
//			   	        	}
						 
						 
						 }
				 }	 
					 
				 
				 
				
			}
		});
      
     
//      if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//      	//todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		  todaysTarget.setText("Target/Acheived : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
//		}

	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

        if (type_spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Travel))) {
				expence_cam.setVisibility(View.VISIBLE);
            Expenses_text = getResources().getString(R.string.Travel);
				exp_date.setVisibility(View.VISIBLE);
				exp_cost.setVisibility(View.VISIBLE);
				exp_discr.setVisibility(View.VISIBLE);
				exp_from.setVisibility(View.VISIBLE);
				exp_to.setVisibility(View.VISIBLE);
				exp_mot.setVisibility(View.VISIBLE);
				exp_submit.setVisibility(View.VISIBLE);
			    //exp_uploadpdf.setVisibility(View.INVISIBLE);

				exp_cost.setText("");
				exp_discr.setText("");
				exp_mot.setText("");
				exp_from.setText("");
				exp_to.setText("");
				exp_date.setText("");
				//exp_discr.setText("");
			} else if (type_spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Miscellaneous))) {
				expence_cam.setVisibility(View.VISIBLE);
            Expenses_text = getResources().getString(R.string.Miscellaneous);
		    	exp_date.setVisibility(View.VISIBLE);
				exp_cost.setVisibility(View.VISIBLE);
				exp_discr.setVisibility(View.VISIBLE);
				exp_from.setVisibility(View.GONE);
				exp_to.setVisibility(View.GONE);
				exp_mot.setVisibility(View.GONE);
				exp_submit.setVisibility(View.VISIBLE);
			    //exp_uploadpdf.setVisibility(View.INVISIBLE);

				exp_cost.setText("");
				exp_discr.setText("");
				exp_mot.setText("");
				exp_from.setText("");
				exp_to.setText("");
				exp_date.setText("");
        } else if (type_spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Type))) {
				expence_cam.setVisibility(View.GONE);
                Expenses_text = getResources().getString(R.string.Select);
				
				exp_date.setVisibility(View.GONE);
				exp_cost.setVisibility(View.GONE);
				exp_discr.setVisibility(View.GONE);
				exp_from.setVisibility(View.GONE);
				exp_to.setVisibility(View.GONE);
				exp_mot.setVisibility(View.GONE);
				exp_submit.setVisibility(View.GONE);
			    //exp_uploadpdf.setVisibility(View.GONE);
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
		//super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();
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



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == RESULT_OK) {

				//if (requestCode == 1 && resultCode == RESULT_OK) {


				File imgFils = new File(mCurrentPhotoPath);
				if (imgFils.exists()) {

					img_show = findViewById(R.id.img_show);
					crossimg.setVisibility(View.VISIBLE);
					img_show.setVisibility(View.VISIBLE);
					//img_show.setRotation((float) 90.0);
					Glide.with(Expenses.this).load(mCurrentPhotoPath).into(img_show);
					//img_show.setImageURI(Uri.fromFile(imgFils));
				}

			//new Expenses.LongOperation().execute();

//			try {
//
//				//dbvoc.updateORDER_order_image(pictureImagePath, Global_Data.GLObalOrder_id);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

		} else if (requestCode == 2 && resultCode == RESULT_OK) {

			try {
				Uri selectedImage = data.getData();

				String[] filePath = {MediaStore.Images.Media.DATA};

				Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

				c.moveToFirst();

				int columnIndex = c.getColumnIndex(filePath[0]);

				pictureImagePath = "file:" + c.getString(columnIndex);
                mCurrentPhotoPath=c.getString(columnIndex);
				File imgFils = new File(mCurrentPhotoPath);
				if (imgFils.exists()) {

					img_show = findViewById(R.id.img_show);
					crossimg.setVisibility(View.VISIBLE);
					img_show.setVisibility(View.VISIBLE);
					Glide.with(Expenses.this).load(mCurrentPhotoPath).into(img_show);
					//img_show.setImageURI(Uri.fromFile(imgFils));
				}

				c.close();

				//new Expenses.LongOperation().execute();
			} catch (Exception ex) {
				ex.printStackTrace();
			}


		}else if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			filePath1 = data.getData();
		}
	}

	private boolean isDeviceSupportCamera() {
		// this device has a camera
// no camera on this device
		return getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA);
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String imageFileName = "exp";
		File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_Expenses");

		if (!storageDir.exists()) {
			storageDir.mkdir();
		}

		File image = File.createTempFile(
				imageFileName,  // prefix
				".jpg",         // suffix
				storageDir      // directory
		);

		// Save a file: path for use with ACTION_VIEW intents
		pictureImagePath = "file:" + image.getAbsolutePath();

		mCurrentPhotoPath = image.getAbsolutePath();

		// mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	private void requestStoragePermission() {

		Dexter.withActivity(this)
				.withPermissions(
						Manifest.permission.CAMERA,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.withListener(new MultiplePermissionsListener() {
					@Override
					public void onPermissionsChecked(MultiplePermissionsReport report) {
						// check if all permissions are granted
						if (report.areAllPermissionsGranted()) {

							B_flag = isDeviceSupportCamera();

							if (B_flag == true) {


                                final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};


								AlertDialog.Builder builder = new AlertDialog.Builder(Expenses.this);

                                builder.setTitle(getResources().getString(R.string.Add_Photo));

								builder.setItems(options, new DialogInterface.OnClickListener() {

									@Override

									public void onClick(DialogInterface dialog, int item) {

                                        if (options[item].equals(getResources().getString(R.string.Take_Photo)))

										{
											image_check = "photo";

											File photoFile = null;
											try {
												photoFile = createImageFile();
											} catch (IOException ex) {
												// Error occurred while creating the File
												Log.i("Image TAG", "IOException");
												pictureImagePath = "";
											}
											Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
											//cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
											Uri photoURI = FileProvider.getUriForFile(Expenses.this,
													BuildConfig.APPLICATION_ID + ".provider",
													photoFile);
											cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
											cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

											startActivityForResult(cameraIntent, 1);

                                        } else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))

										{

											image_check = "gallery";
											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

											startActivityForResult(intent, 2);


                                        } else if (options[item].equals(getResources().getString(R.string.Cancel))) {

											dialog.dismiss();

										}

									}

								});

								builder.show();


							} else {
                               // Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera),"");
							}


						}

						// check for permanent denial of any permission
						if (report.isAnyPermissionPermanentlyDenied()) {
							// show alert dialog navigating to Settings
							showSettingsDialog();
						}
					}

					@Override
					public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
						token.continuePermissionRequest();
					}
				}).
				withErrorListener(new PermissionRequestErrorListener() {
					@Override
					public void onError(DexterError error) {
                     //   Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
					}
				})
				.onSameThread()
				.check();
	}

	/**
	 * Showing Alert Dialog with Settings option
	 * Navigates user to app settings
	 * NOTE: Keep proper title and message depending on your app
	 */
	private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Expenses.this);
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

	//method to show file chooser
	private void showFileChooser() {
		Intent intent = new Intent();
		intent.setType("application/pdf");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
	}

	private void deletedialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(Expenses.this).create(); //Read Update
		alertDialog.setTitle(getResources().getString(R.string.Warning));
		alertDialog.setMessage(getResources().getString(R.string.image_dialog_warning_message));
		alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {



				File file = new File(mCurrentPhotoPath);
				boolean delete= file.delete();

				File file1 = new File(pictureImagePath);
				boolean delete1= file1.delete();

				img_show.setVisibility(View.INVISIBLE);
				crossimg.setVisibility(View.INVISIBLE);

//				Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.image_dialog_delete_success),
//						Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();

				Global_Data.Custom_Toast(Expenses.this, getResources().getString(R.string.image_dialog_delete_success),"yes");

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

}
