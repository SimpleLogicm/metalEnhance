package com.msimplelogic.slidingmenu;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.AppLocationManager;
import com.msimplelogic.activities.BaseActivity;
import com.msimplelogic.activities.BuildConfig;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.Customer_info_main;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.LoginDataBaseAdapter;
import com.msimplelogic.activities.MainActivity;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.Sales_Dash;
import com.msimplelogic.activities.kotlinFiles.AddCustomer;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.activities.kotlinFiles.Order_CustomerList;
import com.msimplelogic.helper.OnSwipeTouchListener;
import com.msimplelogic.model.Beat;
import com.msimplelogic.model.City;
import com.msimplelogic.model.New_Model;
import com.msimplelogic.model.Retailer;
import com.msimplelogic.model.State;
import com.msimplelogic.services.getServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cpm.simplelogic.helper.ConnectionDetector;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class AddRetailerFragment extends BaseActivity implements OnItemSelectedListener {
	//ProgressDialog dialog1;
	Boolean B_flag;
	int choice=0;
	String inte="";
	LoginDataBaseAdapter loginDataBaseAdapter;
	private String Current_Date = "";
	LinearLayout buttonAddRetailerSave;
	ScrollView newswipeAddretailer;
	ImageView buttonAddRetailerCancel,btnVisingCard,btnInshopDisplay,btnSignboard;
	Spinner spinner1,spinner2,spinner3;
	String button_click_flag = "";
	String retailerPicFlag = "";
	private String pic1PhotoPath = "";
	private String pic2PhotoPath = "";
	private String pic3PhotoPath = "";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private int GALLERY = 0;
	private String CAT_ID;
	ArrayList<State> dataStates = new ArrayList<State>();
	ArrayList<City> dataCities = new ArrayList<City>();
    ArrayList<Beat> dataBeats = new ArrayList<Beat>();
    ArrayList<Retailer> dataRetaiers = new ArrayList<Retailer>();
    ImageView picVisitingCard,picInshopDisplay,picSignboard,swipe;
     String F_CITY_ID = "";
     String F_BEAT_ID = "";
     String F_STATE_ID = "";
	 Toolbar toolbar;
	Spinner customerType,customerCategory;
    CardView cardAddretailerDetails,cardAddretailerPlace,cardAddretailerContact,cardAddretailerDone;
	 List<String> listState,listCity,listBeat;
	 static int cityID,beatID,retailerID;
	 int data_stateid,data_cityID,data_beatID,userID;
     TextView txtDone;
	 ArrayAdapter<String> state_Adapter,city_Adapter,beat_Adapter;
	 EditText editTextRetailerName,editTextStoreName,editTextAddress,editTextStreet,editTextContatNo1,editTextContatMail,editTextContatVatINId,editTextPin,editTextLandMark;
	 Retailer r;
	 ImageView nextimg1,nextimg2,nextimg3,nextimg4,hedder_theame;
	 ArrayList<Retailer> newRetailer = new ArrayList<Retailer>();
	 public AddRetailerFragment(){}
	List<String> CustomerType = new ArrayList<String>();
	List<String> CustomerCategory = new ArrayList<String>();
	HashMap<String,String> CustomerType_map = new HashMap<>();
	HashMap<String,String> CustomerCategory_map = new HashMap<>();
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	SharedPreferences sp;
    public static final String GSTINFORMAT_REGEX = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}";
    public static final String GSTN_CODEPOINT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addretailer_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		assert getSupportActionBar() != null;   //null check
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

		cd = new ConnectionDetector(AddRetailerFragment.this);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		DataBaseHelper dbvoc = new DataBaseHelper(AddRetailerFragment.this);

		inte = getIntent().getStringExtra("intent");

		loginDataBaseAdapter = new LoginDataBaseAdapter(AddRetailerFragment.this);
	     loginDataBaseAdapter=loginDataBaseAdapter.open();
	     
	     Calendar c = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
         String strDate = sdf.format(c.getTime());
         Current_Date = sdf.format(c.getTime());
		r = new Retailer();
		// TextView welcomeUser=(TextView)findViewById(R.id.txtWelcomeUser);
		editTextRetailerName = (EditText) findViewById(R.id.editTextRetailerName);
		hedder_theame =  findViewById(R.id.hedder_theame);
		nextimg1 = findViewById(R.id.nextimg1);
		nextimg2 =  findViewById(R.id.nextimg2new);
		nextimg3 =  findViewById(R.id.nextimg3);
		nextimg4 =  findViewById(R.id.nextimg4);

		editTextRetailerName = (EditText) findViewById(R.id.editTextRetailerName);
		editTextStoreName = (EditText) findViewById(R.id.editTextStoreName);
		editTextAddress = (EditText) findViewById(R.id.editTextAddress);
		editTextLandMark = (EditText) findViewById(R.id.editTextLandMark);
		editTextPin = (EditText) findViewById(R.id.editTextPin);
		editTextStreet = (EditText) findViewById(R.id.editTextStreet);
		editTextContatVatINId = (EditText) findViewById(R.id.editTextContatVatINId);
		editTextContatNo1 = (EditText) findViewById(R.id.editTextContatNo1);
		editTextContatMail = (EditText) findViewById(R.id.editTextContatMail);
		spinner3 = (Spinner) findViewById(R.id.spnState);
		spinner1 = (Spinner) findViewById(R.id.spnBeat);
		spinner2 = (Spinner) findViewById(R.id.spnCity);

		customerType = findViewById(R.id.customer_type);
		customerCategory = findViewById(R.id.customer_category);

		btnVisingCard = findViewById(R.id.visiting_card_btn);
		btnInshopDisplay = findViewById(R.id.inshop_display_btn);
		btnSignboard = findViewById(R.id.signboard_btn);

		picVisitingCard = findViewById(R.id.visiting_card_pic);
		picInshopDisplay = findViewById(R.id.inshop_display_pic);
		picSignboard = findViewById(R.id.signboard_pic);

		newswipeAddretailer = findViewById(R.id.newswipe_addretailer);
		cardAddretailerDetails = findViewById(R.id.card_addretailerdetails);
		cardAddretailerPlace = findViewById(R.id.card_addretailerplace);
		cardAddretailerContact = findViewById(R.id.card_addretailercontact);
		cardAddretailerDone = findViewById(R.id.card_addretailerdone);
		swipe =  findViewById(R.id.swipe);
        txtDone =  findViewById(R.id.txt_done);

		// Spinner DropDown CustomerType
		sp = getSharedPreferences("SimpleLogic", 0);
		int current_theme = sp.getInt("CurrentTheme",0);

		if (current_theme== 1){
			hedder_theame.setImageResource(R.drawable.dark_hedder);
			nextimg1.setImageResource(R.drawable.viewordermenu_dark);
			nextimg2.setImageResource(R.drawable.viewordermenu_dark);
			nextimg3.setImageResource(R.drawable.viewordermenu_dark);
			nextimg4.setImageResource(R.drawable.viewordermenu_dark);

		}
		CustomerType.add("Customer Type");

		List<New_Model> cont1 = dbvoc.getcustomer_types();
		if (cont1.size() > 0) {

			for (New_Model cnt1 : cont1) {

				CustomerType.add(cnt1.getName());
				CustomerType_map.put(cnt1.getName(),cnt1.getCode());
			}
		}
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,R.layout.spinner_item, CustomerType);
		// Drop down layout style - list view with radio button
		dataAdapter1.setDropDownViewResource(R.layout.spinner_item);
	    customerType.setAdapter(dataAdapter1);

		// Spinner DropDown CustomerCategory

		CustomerCategory.add("Customer Category");
		List<New_Model> cont2 = dbvoc.getCustomerCategories();
		if (cont2.size() > 0) {

			for (New_Model cnt2 : cont2) {

				CustomerCategory.add(cnt2.getName());
				CustomerCategory_map.put(cnt2.getName(),cnt2.getCode());
			}
		}
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, CustomerCategory);
		// Drop down layout style - list view with radio button
		dataAdapter2.setDropDownViewResource(R.layout.spinner_item);
		customerCategory.setAdapter(dataAdapter2);

		newswipeAddretailer.setOnTouchListener(new OnSwipeTouchListener(AddRetailerFragment.this) {
			public void onSwipeRight() {
				switch(choice)
				{
					case 3:
						swipe.setImageResource(R.drawable.fourtimeline3);
						cardAddretailerDetails.setVisibility(View.GONE);
						cardAddretailerPlace.setVisibility(View.GONE);
						cardAddretailerContact.setVisibility(View.VISIBLE);
						cardAddretailerDone.setVisibility(View.GONE);
						txtDone.setText("Swipe to Next");
						choice--;
						break;
					case 2:
						swipe.setImageResource(R.drawable.fourtimeline2);
						cardAddretailerDetails.setVisibility(View.GONE);
						cardAddretailerPlace.setVisibility(View.VISIBLE);
						cardAddretailerContact.setVisibility(View.GONE);
						cardAddretailerDone.setVisibility(View.GONE);
						txtDone.setText("Swipe to Next");
						choice--;
						break;
					case 1:
						swipe.setImageResource(R.drawable.fourtimeline1);
						cardAddretailerDetails.setVisibility(View.VISIBLE);
						cardAddretailerPlace.setVisibility(View.GONE);
						cardAddretailerContact.setVisibility(View.GONE);
						cardAddretailerDone.setVisibility(View.GONE);
						txtDone.setText("Swipe to Next");
						choice--;
						break;
					default:
				    	break;
				}
			}
			public void onSwipeLeft() {
				switch(choice)
				{
					case 0:
						swipe.setImageResource(R.drawable.fourtimeline2);
						cardAddretailerDetails.setVisibility(View.GONE);
						cardAddretailerPlace.setVisibility(View.VISIBLE);
						cardAddretailerContact.setVisibility(View.GONE);
						cardAddretailerDone.setVisibility(View.GONE);
						txtDone.setText("Swipe to Next");
						choice++;
						break;
					case 1:
						swipe.setImageResource(R.drawable.fourtimeline3);
						cardAddretailerDetails.setVisibility(View.GONE);
						cardAddretailerPlace.setVisibility(View.GONE);
						cardAddretailerContact.setVisibility(View.VISIBLE);
						cardAddretailerDone.setVisibility(View.GONE);
						txtDone.setText("Swipe to Next");
						choice++;
						break;
					case 2:
						swipe.setImageResource(R.drawable.fourtimeline4);
						cardAddretailerDetails.setVisibility(View.GONE);
						cardAddretailerPlace.setVisibility(View.GONE);
						cardAddretailerContact.setVisibility(View.GONE);
						cardAddretailerDone.setVisibility(View.VISIBLE);
                        txtDone.setText("Done");
						choice++;
						break;
					default:
					    break;
				}
			}
		});

		if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Glovel_Contact_name)) {
			editTextRetailerName.setText(Global_Data.Glovel_Contact_name);
		}

		if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Glovel_Contact_Mobile_Number)) {
			editTextContatNo1.setText(Global_Data.Glovel_Contact_Mobile_Number);
		}

		SharedPreferences sp = getSharedPreferences("SimpleLogic", 0);

        listState = new ArrayList<String>();
		listState.add(getResources().getString(R.string.Select_State));


		state_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listState);
        state_Adapter.setDropDownViewResource(R.layout.spinner_item);
		spinner3 = (Spinner) findViewById(R.id.spnState);
    	spinner3.setAdapter(state_Adapter);
    	spinner3.setOnItemSelectedListener(this);



        listCity = new ArrayList<String>();
		listCity.add(getResources().getString(R.string.Select_City));


		city_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listCity);
	    city_Adapter.setDropDownViewResource(R.layout.spinner_item);
		spinner2 = (Spinner) findViewById(R.id.spnCity);
    	spinner2.setAdapter(city_Adapter);
    	spinner2.setOnItemSelectedListener(this);
    	
    	
    	listBeat = new ArrayList<String>();
		listBeat.add(getResources().getString(R.string.Select_Beats));


		List<Local_Data> contacts2 = dbvoc.getAllBeats();
    	//results.add("Select Beat");
         for (Local_Data cn : contacts2) 
         {
        	 String str_beat = ""+cn.getStateName();
        	 //Global_Data.local_pwd = ""+cn.getPwd();
       	
        	 listBeat.add(str_beat);
       
       	 }

          
         dbvoc.close();
        
    	
	   /* for (Iterator iterator = dataBeats.iterator(); iterator.hasNext();) {
			DatabaseModel databaseModel = (DatabaseModel) iterator.next();
			//Log.e("DATA", ""+databaseModel);
			listBeat.add(databaseModel.getName());
		}*/

		beat_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listBeat);
	    beat_Adapter.setDropDownViewResource(R.layout.spinner_item);
		spinner1 = (Spinner) findViewById(R.id.spnBeat);
    	spinner1.setAdapter(beat_Adapter);
    	spinner1.setOnItemSelectedListener(this);

		//dialog1 = new android.app.ProgressDialog(AddRetailerFragment.this);
		//dialog = new ProgressDialog(RaiseNewIdeaActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		btnVisingCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				button_click_flag = "pic1";
				retailerPicFlag = "vcpic";
				//selectImage();
				requestStoragePermission(button_click_flag);
			}
		});

		picVisitingCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!pic1PhotoPath.equals(""))
				{
					image_zoom_dialog(pic1PhotoPath,"pic1");
				}
			}
		});

		picInshopDisplay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!pic1PhotoPath.equals(""))
				{
					image_zoom_dialog(pic2PhotoPath,"pic2");
				}
			}
		});

		picSignboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!pic1PhotoPath.equals(""))
				{
					image_zoom_dialog(pic3PhotoPath,"pic3");
				}
			}
		});

		btnInshopDisplay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                button_click_flag = "pic2";
				retailerPicFlag = "isdpic";
                requestStoragePermission(button_click_flag);
            }
        });

        btnSignboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_click_flag = "pic3";
				retailerPicFlag = "sbpic";
                requestStoragePermission(button_click_flag);
            }
        });

      // welcomeUser.setText(sp.getString("FirstName", "")+" "+ sp.getString("LastName", ""));
		buttonAddRetailerSave = (LinearLayout) findViewById(R.id.buttonAddRetailerSave);
        
        buttonAddRetailerSave.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View b, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_UP)
			    {
			        //up event
			        //b.setBackgroundColor(Color.parseColor("#414042"));
			        return true;
			    }
			    if(event.getAction() == MotionEvent.ACTION_DOWN)
			    {

			    	try{
						if (txtDone.getText().toString().equalsIgnoreCase("Done")){
							boolean validEmail=true;
							String PhoneNo = editTextContatNo1.getText().toString();
							String pin = editTextPin.getText().toString();
							String vtn = editTextContatVatINId.getText().toString();
							String Regex = "[^\\d]";
							String PhoneDigits = PhoneNo.replaceAll(Regex, "");

							if (editTextRetailerName.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Retailer_Name), "yes");
							}

							else if (editTextStoreName.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Store_Name),"yes");
							}

							else if (editTextAddress.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Address),"yes");
							}

							else if (editTextStreet.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Street),"yes");
							}

							else if (editTextContatMail.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Valid_Email_Address),"yes");
							}

							else if (editTextLandMark.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_LandMark), "yes");
							} else if (spinner1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Beats))) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.select_beat),"yes");
							} else if (spinner2.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_City))) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.select_city),"yes");
							} else if (spinner3.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_State))) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.select_state),"yes");
							}


							else if (editTextPin.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Pin),"yes");
							}
							else if (pin.length()!=6) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Valid_Pin),"ye");
							}

							else if (editTextContatNo1.getText().length()==0) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Contact_Number),"yes");
							}

//				  else if (PhoneNo.length()!=10) {
//						Toast toast = Toast.makeText(AddRetailerFragment.this, "Please Enter Valid Contact Number", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

							else if (editTextContatMail.length() > 0 && !validate(editTextContatMail.getText().toString().trim())) {

								validEmail=false;

								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Valid_Email_Address),"yes");
							}

//					else if (editTextContatVatINId.getText().length() == 0) {
//						Toast toast = Toast.makeText(AddRetailerFragment.this, getResources().getString(R.string.Enter_GSTIN_Number), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}
							else if (editTextContatVatINId.getText().length() > 0 && vtn.length() != 15 ) {

								Global_Data.Custom_Toast(AddRetailerFragment.this, "GST Number should be 15 digit","yes");
							}else if(!isValidGSTNo(editTextContatVatINId.getText().toString())){


								Global_Data.Custom_Toast(AddRetailerFragment.this, getResources().getString(R.string.Enter_Valid_GSTIN_Number),"yes");
							}




							else {
								if (validEmail) {

									AlertDialog alertDialog = new AlertDialog.Builder(AddRetailerFragment.this).create(); //Read Update
									alertDialog.setTitle(getResources().getString(R.string.Confirmation));
									alertDialog.setMessage(getResources().getString(R.string.Do_you_wish_to_continue));
									alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											InsertAddRetailerAsyncTask insertAddRetailerAsyncTask = new InsertAddRetailerAsyncTask(AddRetailerFragment.this);
											insertAddRetailerAsyncTask.execute();

										}
									});

									alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.cancel();
										}
									});


									alertDialog.show();
								}


							}

						}
						//down event
						//b.setBackgroundColor(Color.parseColor("#910505"));

						// TODO Auto-generated method stub
					} catch (Exception e) {
						e.printStackTrace();
					}



			    }
				return false;
			}
		});
       /* buttonAddRetailerSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {}
		});*/

		buttonAddRetailerCancel = (ImageView) findViewById(R.id.buttonAddRetailerCancel);
        buttonAddRetailerCancel.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View b, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_UP)
			    {
			        //up event
			        b.setBackgroundColor(Color.parseColor("#414042"));
			        return true;
			    }
			    if(event.getAction() == MotionEvent.ACTION_DOWN)
			    {
			        //down event
			        b.setBackgroundColor(Color.parseColor("#910505"));

					// TODO Auto-generated method stub


					AlertDialog alertDialog = new AlertDialog.Builder(AddRetailerFragment.this).create(); //Read Update
					alertDialog.setTitle(getResources().getString(R.string.Warning));
					alertDialog.setMessage(getResources().getString(R.string.retailer_discard_warning_message));
					alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub


							Intent i = new Intent(AddRetailerFragment.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
							AddRetailerFragment.this.finish();
                        }
					});

					alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							  dialog.cancel();
						}
					});
				  

				    alertDialog.show(); 
					

				
			    }
				return false;
			}
		});
       /* buttonAddRetailerCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {}
		});*/
        try {
            ActionBar mActionBar = AddRetailerFragment.this.getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(AddRetailerFragment.this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
			mTitleTextView.setText(getResources().getString(R.string.ADD_RETAILER));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            //SharedPreferences sp = Expenses.this.getSharedPreferences("SimpleLogic", 0);

//		ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
//		H_LOGO.setImageResource(R.drawable.rs);
//		H_LOGO.setVisibility(View.VISIBLE);
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
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
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

         

    }

	@Override
	public void onResume() {

		super.onResume();

//		setFocusableInTouchMode(true);
//		getView().requestFocus();
//		getView().setOnKeyListener(new View.OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//
//					AlertDialog alertDialog = new AlertDialog.Builder(AddRetailerFragment.this).create(); //Read Update
//					alertDialog.setTitle("Warning");
//					alertDialog.setMessage("This operation will discard all the values entered, Are you sure you want to continue?");
//					alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes",new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//
//							Intent i = new Intent(AddRetailerFragment.this, MainActivity.class);
//                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(i);
//                            AddRetailerFragment.this.finish();
//                        }
//					});
//
//					alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.cancel();
//						}
//					});
//
//
//					alertDialog.show();
//
//					return true;
//
//				}
//
//				return false;
//			}
//		});
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		DataBaseHelper dbvoc = new DataBaseHelper(AddRetailerFragment.this);

		loginDataBaseAdapter = new LoginDataBaseAdapter(AddRetailerFragment.this);
		     loginDataBaseAdapter=loginDataBaseAdapter.open();
	     
			 Spinner spinner = (Spinner) parent;
		     if(spinner.getId() == R.id.spnBeat)
		     {
				 if (spinner1.getSelectedItem().toString() == getResources().getString(R.string.Select_Beats))
		    	   {	
		    		   listCity.clear();
					   listCity.add(getResources().getString(R.string.Select_City));
					   city_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listCity);
		    			 city_Adapter.setDropDownViewResource(R.layout.spinner_item);
		    			 spinner2.setAdapter(city_Adapter);
		    			 
		    		     listState.clear();
					     listState.add(getResources().getString(R.string.Select_State));
					     state_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listState);
		    			 state_Adapter.setDropDownViewResource(R.layout.spinner_item);
		    			 spinner3.setAdapter(state_Adapter);
      	    	   }
		    	   else
		    	   {	   
			    	    String items = spinner1.getSelectedItem().toString();
		                String C_ID = "";
		                Log.i("Selected item : ", items);
		                
		                List<Local_Data> contacts = dbvoc.getBeats_CITYID(spinner1.getSelectedItem().toString());      
		                for (Local_Data cn : contacts) 
		                {
		                   	C_ID = cn.getStateName();
		                	CAT_ID = cn.getStateName();
		                	F_CITY_ID =  cn.getStateName();
		                	F_BEAT_ID =  cn.get_category_id();
		      	        }

					   //listCity.clear();
							if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(C_ID))
		                {
		                	List<Local_Data> contacts2 = dbvoc.getCity(C_ID);
		                	//add
							listCity.clear();
		                    for (Local_Data cn : contacts2) 
		                    {
		                  	   listCity.add(cn.getStateName());
		                    }
							    city_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listCity);
		                    	city_Adapter.setDropDownViewResource(R.layout.spinner_item);
		                    	spinner2.setAdapter(city_Adapter);
		                }
		    	   } 
		    	   dbvoc.close();
		      }
		     else
	    	 if(spinner.getId() == R.id.spnCity)
		     {

				 if (spinner2.getSelectedItem().toString() == getResources().getString(R.string.Select_City))
		    	   {	
		    		   listState.clear();
					   listState.add(getResources().getString(R.string.Select_State));
					   state_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listState);
		    			 state_Adapter.setDropDownViewResource(R.layout.spinner_item);
		    			 spinner3.setAdapter(state_Adapter);
		     	   }
		    	   else
		    	   {	   
			    	    String items = spinner1.getSelectedItem().toString();
		                String C_ID = "";
		                Log.i("Selected item : ", items);
		                
		                List<Local_Data> contacts = dbvoc.getCityByStateID(spinner2.getSelectedItem().toString());      
		                for (Local_Data cn : contacts) 
		                {
		              	     
		                	C_ID = cn.getcity_id();
		                	CAT_ID = cn.getcity_id();
		                	F_STATE_ID = cn.getcity_id();
		      	        }

					    //listState.clear();
		                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(C_ID))
		                {
		                	List<Local_Data> contacts2 = dbvoc.getstate(C_ID);
							//add
							listState.clear();
		                    for (Local_Data cn : contacts2) 
		                    {
		                  	     
		                    	listState.add(cn.getStateName());
		                    }
							state_Adapter = new ArrayAdapter<String>(AddRetailerFragment.this, R.layout.spinner_item, listState);
			    			 state_Adapter.setDropDownViewResource(R.layout.spinner_item);
			    			 spinner3.setAdapter(state_Adapter);
		                }
		    	   } 
		     }
		   
		     dbvoc.close();	 
	       
	}

	public class InsertAddRetailerAsyncTask extends AsyncTask<Void, Void, Void> {

		/** progress dialog to show user that the backup is processing. */
		//private ProgressDialog dialog;
		/** application context. */
		private Activity activity;
		
		private Context context;
		
		private boolean sucess;
		float f=0.00f;

		public InsertAddRetailerAsyncTask(Activity activity) {
			this.activity = activity;
			context=activity;
			//dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			this.dialog.setMessage(getResources().getString(R.string.Retailer_dialog_message));
//			this.dialog.setCancelable(false);
//			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				//dataProducts=(ArrayList<DatabaseProductModel>) myDbHelper.ger(spnCategory.getSelectedItem().toString());
					if (data_stateid==0||data_cityID==0||data_beatID==0) {
						
						try
						{
							AddRetailerFragment.this.runOnUiThread(new Runnable() {
								public void run() {
									AppLocationManager appLocationManager = new AppLocationManager(AddRetailerFragment.this);
									Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
									Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

									PlayService_Location PlayServiceManager = new PlayService_Location(AddRetailerFragment.this);

									if (PlayServiceManager.checkPlayServices(AddRetailerFragment.this))
									{
										Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

									}
									else
									if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
									{
										Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
										Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
									}
								}
							});

						}catch(Exception ex){ex.printStackTrace();}
						
						final SecureRandom random = new SecureRandom();

						AddRetailerFragment.this.runOnUiThread(new Runnable() {
							public void run() {

								SharedPreferences spf = AddRetailerFragment.this.getSharedPreferences("SimpleLogic", 0);
								String user_email = spf.getString("USER_EMAIL",null);

								String customerType_id = "";
								String customerCategory_id = "";

								try {
									if(!customerType.getSelectedItem().toString().equalsIgnoreCase("Customer Type"))
									{
										customerType_id = CustomerType_map.get(customerType.getSelectedItem().toString().trim());
									}
								}catch(Exception ex)
								{
									customerType_id = "";
									ex.printStackTrace();
								}

								try {
									if(!customerCategory.getSelectedItem().toString().equalsIgnoreCase("Customer Category"))
									{
										customerCategory_id = CustomerCategory_map.get(customerCategory.getSelectedItem().toString().trim());
									}
								}catch(Exception ex)
								{
									customerCategory_id = "";
									ex.printStackTrace();
								}


								Long randomPIN = System.currentTimeMillis();
								String PINString = "R"+String.valueOf(randomPIN);
								cd = new ConnectionDetector(AddRetailerFragment.this);
								isInternetPresent = cd.isConnectingToInternet();
								if (isInternetPresent) {

									loginDataBaseAdapter.insertRetailerMaster(PINString,editTextRetailerName.getText().toString(), editTextStoreName.getText().toString(),editTextAddress.getText().toString(),
											editTextStreet.getText().toString(),editTextLandMark.getText().toString(), editTextPin.getText().toString(),"", editTextContatNo1.getText().toString()
											, editTextContatMail.getText().toString(), "active", F_STATE_ID,F_CITY_ID,F_BEAT_ID,editTextContatVatINId.getText().toString(),user_email,Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,pic1PhotoPath,pic2PhotoPath,pic3PhotoPath,"","","","","","","","","","retailer",customerType_id,customerCategory_id,"");

									getServices.SYNCustomer(AddRetailerFragment.this,"retailer",PINString);
								}
								else
								{

									loginDataBaseAdapter.insertRetailerMaster(PINString,editTextRetailerName.getText().toString(), editTextStoreName.getText().toString(),editTextAddress.getText().toString(),
											editTextStreet.getText().toString(),editTextLandMark.getText().toString(), editTextPin.getText().toString(),"", editTextContatNo1.getText().toString()
											, editTextContatMail.getText().toString(), "active", F_STATE_ID,F_CITY_ID,F_BEAT_ID,editTextContatVatINId.getText().toString(),user_email,Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,pic1PhotoPath,pic2PhotoPath,pic3PhotoPath,"","","","","","","","","","retailer",customerType_id,customerCategory_id,"");


									if (Global_Data.addcustomerintent.equalsIgnoreCase("add_cust")&& Global_Data.addcustomerintent!="") {
										Intent i = new Intent(AddRetailerFragment.this, AddCustomer.class);
										Global_Data.addcustomerintent = "";
										Global_Data.Custom_Toast(context,getResources().getString(R.string.Retailer_Added), "Yes");
										overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
										i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(i);
										finish();
									}else
									{
										Global_Data.Custom_Toast(context,getResources().getString(R.string.Retailer_Added), "Yes");

										Intent a = new Intent(AddRetailerFragment.this, Sales_Dash.class);
										startActivity(a);
										AddRetailerFragment.this.finish();
									}



								}




							}
						});

						Calendar c = Calendar.getInstance();
						System.out.println("Current time =&gt; "+c.getTime());

						SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						final String formattedDate = df.format(c.getTime());

						String gaddress = "";
						try {
							if (Global_Data.address.equalsIgnoreCase("null")) {
								gaddress = "";
							} else {
								gaddress = Global_Data.address;
							}
						}catch(Exception ex){ex.printStackTrace();}

					}

					sucess= true;
					
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}
	}

	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
}
	
	public boolean validate(final String hex) {
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*public class InsertRetailerMasterAsyncTask extends AsyncTask<Void, Void, Void> {

		*//** progress dialog to show user that the backup is processing. *//*
		private ProgressDialog dialog;
		*//** application context. *//*
		private Activity activity;
		
		private Context context;
		
		private boolean webServiceResponse;
		float f=0.00f;

		public InsertRetailerMasterAsyncTask(Activity activity) {
			this.activity = activity;
			context=activity;
			dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			this.dialog.setMessage("Adding to Retailer Master...Please wait");
			this.dialog.show();
			

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				//dataProducts=(ArrayList<DatabaseProductModel>) myDbHelper.ger(spnCategory.getSelectedItem().toString());
					
				 
				
					
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
				
			}
			 
		   
			
			
			
		}
	}
	*/
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
				SharedPreferences sp = AddRetailerFragment.this.getSharedPreferences("SimpleLogic", 0);
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
		if (Global_Data.addcustomerintent.equalsIgnoreCase("add_cust")&& Global_Data.addcustomerintent!="") {
			Intent i = new Intent(AddRetailerFragment.this, MainActivity.class);
			Global_Data.addcustomerintent="";
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
		else {
			Global_Data.CUSTOMER_SERVICE_FLAG.equals("ADD_RETAILER");
			Intent i = new Intent(AddRetailerFragment.this, Order_CustomerList.class);
			Global_Data.addcustomerintent="";
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

	}
	private void requestStoragePermission(String button_Flag) {

		Dexter.withActivity(this)
				.withPermissions(
						Manifest.permission.CAMERA,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.ACCESS_FINE_LOCATION)
				.withListener(new MultiplePermissionsListener() {
					@Override
					public void onPermissionsChecked(MultiplePermissionsReport report) {
						// check if all permissions are granted
						if (report.areAllPermissionsGranted()) {

							selectImage();
							// Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
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
						Global_Data.Custom_Toast(getApplicationContext(), "Error occurred! " + error.toString(),"");
					}
				})
				.onSameThread()
				.check();
	}

	private void showSettingsDialog() {
		androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddRetailerFragment.this);
		builder.setTitle("Need Permissions");
		builder.setCancelable(false);
		builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
		builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				openSettings();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

	private void selectImage() {
		//final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
		final CharSequence[] items = { "Take Photo", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(AddRetailerFragment.this);
		//android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (items[item].equals("Take Photo")) {
					cameraIntent();
				}
//				else if (items[item].equals("Choose from Gallery")) {
//					choosePhotoFromGallary();
//				}
				else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void cameraIntent()
	{
		B_flag = isDeviceSupportCamera();
		if(B_flag == true)
		{
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (cameraIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile(button_click_flag, retailerPicFlag);
				} catch (IOException ex) {
					// Error occurred while creating the File
					ex.printStackTrace();
					Log.i("Image TAG", "IOException");
					if(button_click_flag.equalsIgnoreCase("pic1"))
					{
						pic1PhotoPath = "";
					}
					else
					if(button_click_flag.equalsIgnoreCase("pic2"))
					{
						pic2PhotoPath = "";
					}
					else
					if(button_click_flag.equalsIgnoreCase("pic3"))
					{
						pic3PhotoPath = "";
					}
				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					//cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
					Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
							BuildConfig.APPLICATION_ID + ".provider",
							photoFile);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
					cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
				}
			}
		}
		else
		{
			Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device","");
		}
	}

	public void choosePhotoFromGallary() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(galleryIntent, GALLERY);
	}

	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private File createImageFile(String flag, String picname) throws IOException {
		// Create an image file name
		String imageFileName = picname;
		//  File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File storageDir = new File(path,"MetalAppRetailer");

		if(!storageDir.exists())
		{
			storageDir.mkdir();
		}

		File image = File.createTempFile(
				imageFileName,  // prefix
				".png",         // suffix
				storageDir      // directory
		);

		// Save a file: path for use with ACTION_VIEW intents
		if(button_click_flag.equalsIgnoreCase("pic1"))
		{
			try {
				if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pic1PhotoPath)) {
					Uri uri = Uri.parse(pic1PhotoPath);
					File fdelete = new File(uri.getPath());
					if (fdelete.exists()) {
						if (fdelete.delete()) {
							System.out.println("file Deleted :" + pic1PhotoPath);
						} else {
							System.out.println("file not Deleted :" + pic1PhotoPath);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			pic1PhotoPath = "file:" + image.getAbsolutePath();

		}
		else
		if(button_click_flag.equalsIgnoreCase("pic2"))
		{
			try {
				if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pic2PhotoPath)) {


					Uri uri = Uri.parse(pic2PhotoPath);
					File fdelete = new File(uri.getPath());
					if (fdelete.exists()) {
						if (fdelete.delete()) {
							System.out.println("file Deleted :" + pic2PhotoPath);
						} else {
							System.out.println("file not Deleted :" + pic2PhotoPath);
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			pic2PhotoPath = "file:" + image.getAbsolutePath();
		}
		else
		if(button_click_flag.equalsIgnoreCase("pic3"))
		{
			try {
				if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pic3PhotoPath)) {


					Uri uri = Uri.parse(pic3PhotoPath);
					File fdelete = new File(uri.getPath());
					if (fdelete.exists()) {
						if (fdelete.delete()) {
							System.out.println("file Deleted :" + pic3PhotoPath);
						} else {
							System.out.println("file not Deleted :" + pic3PhotoPath);
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			pic3PhotoPath = "file:" + image.getAbsolutePath();
		}


		// mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

//			if (dialog1.isShowing()) {
//				dialog1.dismiss();
//
//			}
//			dialog1 = new ProgressDialog(AddRetailerFragment.this);
//			dialog1.setMessage("Please wait....");
//			dialog1.setTitle("Metal App");
//			dialog1.setCancelable(false);
//			dialog1.show();

			if (button_click_flag.equalsIgnoreCase("pic1")) {
				if (!pic1PhotoPath.equalsIgnoreCase("")) {
					Glide.with(AddRetailerFragment.this).load(pic1PhotoPath).into(picVisitingCard);
				}
			} else if (button_click_flag.equalsIgnoreCase("pic2")) {
				if (!pic2PhotoPath.equalsIgnoreCase("")) {
					Glide.with(AddRetailerFragment.this).load(pic2PhotoPath).into(picInshopDisplay);
				}
			} else if (button_click_flag.equalsIgnoreCase("pic3")) {
				if (!pic3PhotoPath.equalsIgnoreCase("")) {
					Glide.with(AddRetailerFragment.this).load(pic3PhotoPath).into(picSignboard);
				}
			}

			new ActivityResultTask().execute();

		} else if (requestCode == GALLERY && resultCode == RESULT_OK) {

			Uri contentURI = data.getData();
			try {
				String filePath = getPath(contentURI);
				File currentFilenGal = new File(filePath);
				long length = currentFilenGal.length();
				length = length / 1024;
				if (length > 10000) {

					Global_Data.Custom_Toast(this, "Unable to upload more than 10mb Image","");
				} else {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
					Glide.with(AddRetailerFragment.this).load(filePath).into(picVisitingCard);
					//picVisitingCard.setImageBitmap(bitmap);
				}
			} catch (IOException e) {
				e.printStackTrace();
				// Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
			}


		}
	}

	private String getPath(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String result = cursor.getString(column_index);
		cursor.close();
		return result;
	}


	private class ActivityResultTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... response) {

			try {
				if(button_click_flag.equalsIgnoreCase("pic1"))
				{
					if(!pic1PhotoPath.equalsIgnoreCase(""))
					{
						// reduce_img_Qaulity(Uri.parse(outletsignboard_mCurrentPhotoPath));
						Bitmap b= BitmapFactory.decodeFile(Uri.parse(pic1PhotoPath).getPath());
						Bitmap out = Bitmap.createScaledBitmap(b, 500, 500, false);
						File file = new File(Uri.parse(pic1PhotoPath).getPath());

						FileOutputStream fOut;
						try {
							fOut = new FileOutputStream(file);
							out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
							fOut.flush();
							fOut.close();
							b.recycle();
							out.recycle();
						} catch (Exception e) {}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(AddRetailerFragment.this.getContentResolver(), Uri.parse(pic1PhotoPath));
									//Glide.with(AddRetailerFragment.this).load(pic1PhotoPath).into(picVisitingCard);
									//picVisitingCard.setImageBitmap(mImageBitmap);
								} catch (IOException e) {
									e.printStackTrace();
								}

							}
						});

					}

				}
				else
				if(button_click_flag.equalsIgnoreCase("pic2"))
				{
					if(!pic2PhotoPath.equalsIgnoreCase(""))
					{
						Bitmap b= BitmapFactory.decodeFile(Uri.parse(pic2PhotoPath).getPath());
						Bitmap out = Bitmap.createScaledBitmap(b, 500, 500, false);

						File file = new File(Uri.parse(pic2PhotoPath).getPath());
						FileOutputStream fOut;
						try {
							fOut = new FileOutputStream(file);
							out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
							fOut.flush();
							fOut.close();
							b.recycle();
							out.recycle();
						} catch (Exception e) {}
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								try {
									Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(AddRetailerFragment.this.getContentResolver(), Uri.parse(pic2PhotoPath));
									//Glide.with(AddRetailerFragment.this).load(pic2PhotoPath).into(picInshopDisplay);
									//picInshopDisplay.setImageBitmap(mImageBitmap);
									//   outlet_signboard_pick.setRotation(90);
								} catch (IOException e) {
									e.printStackTrace();
								}

							}
						});

					}
				}
				else
				if(button_click_flag.equalsIgnoreCase("pic3"))
				{
					if(!pic3PhotoPath.equalsIgnoreCase(""))
					{
						Bitmap b= BitmapFactory.decodeFile(Uri.parse(pic3PhotoPath).getPath());
						Bitmap out = Bitmap.createScaledBitmap(b, 500, 500, false);

						File file = new File(Uri.parse(pic3PhotoPath).getPath());
						FileOutputStream fOut;
						try {
							fOut = new FileOutputStream(file);
							out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
							fOut.flush();
							fOut.close();
							b.recycle();
							out.recycle();
						} catch (Exception e) {}
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								try {
									Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(AddRetailerFragment.this.getContentResolver(), Uri.parse(pic3PhotoPath));
									//Glide.with(AddRetailerFragment.this).load(pic3PhotoPath).into(picSignboard);
									//picSignboard.setImageBitmap(mImageBitmap);
									//  outlet_signboard_pick.setRotation(90);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			//dialog1.dismiss();


//                }
//            });

			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {

			// might want to change "executed" for the returned string passed
			// into onPostExecute() but that is upto you
			//dialog.dismiss();
			AddRetailerFragment.this.runOnUiThread(new Runnable() {
				public void run() {
//					if (dialog1.isShowing()) {
//						dialog1.dismiss();
//					}
				}
			});
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public void image_zoom_dialog(String hm_url,String image_flag) {
		Dialog dialogcustom = new Dialog(AddRetailerFragment.this);
		dialogcustom.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogcustom.setContentView(R.layout.collection_image_dialog);


		ImageView Collection_zoom_image = (ImageView) dialogcustom.findViewById(R.id.Collection_zoom_image);

//        Glide.with(_context).load(hm_url)
//                .placeholder(R.drawable.loa)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(Collection_zoom_image);

		Glide.with(AddRetailerFragment.this).load(hm_url)
				.thumbnail(Glide.with(AddRetailerFragment.this).load("file:///android_asset/loading.gif"))
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
				if(image_flag.equals("pic1"))
				{
					pic1PhotoPath = "";
					Glide.with(AddRetailerFragment.this).load(R.drawable.img_not_found).into(picVisitingCard);
				}
				else
				if(image_flag.equals("pic2"))
				{
					pic2PhotoPath = "";
					Glide.with(AddRetailerFragment.this).load(R.drawable.img_not_found).into(picInshopDisplay);
				}
				else
				if(image_flag.equals("pic3"))
				{
					pic3PhotoPath = "";
					Glide.with(AddRetailerFragment.this).load(R.drawable.img_not_found).into(picSignboard);
				}

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


	public static boolean isValidGSTNo(String str)
	{
		// Regex to check valid
		// GST (Goods and Services Tax) number
		String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
				+ "[A-Z]{1}[1-9A-Z]{1}"
				+ "Z[0-9A-Z]{1}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);

				return m.matches();
	}


	private static boolean validGSTIN(String gstin) throws Exception {
        boolean isValidFormat = false;
        if (checkPattern(gstin, GSTINFORMAT_REGEX)) {
            isValidFormat = verifyCheckDigit(gstin);
        }
        return isValidFormat;

    }



    private static boolean verifyCheckDigit(String gstinWCheckDigit) throws Exception {
        Boolean isCDValid = false;
        String newGstninWCheckDigit = getGSTINWithCheckDigit(
                gstinWCheckDigit.substring(0, gstinWCheckDigit.length() - 1));

        if (gstinWCheckDigit.trim().equals(newGstninWCheckDigit)) {
            isCDValid = true;
        }
        return isCDValid;
    }

    /**
     * Method to check if an input string matches the regex pattern passed
     *
     * @param inputval
     * @param regxpatrn
     * @return boolean
     */
    public static boolean checkPattern(String inputval, String regxpatrn) {
        boolean result = false;
        if ((inputval.trim()).matches(regxpatrn)) {
            result = true;
        }
        return result;
    }

    public static String getGSTINWithCheckDigit(String gstinWOCheckDigit) throws Exception {
        int factor = 2;
        int sum = 0;
        int checkCodePoint = 0;
        char[] cpChars;
        char[] inputChars;

        try {
            if (gstinWOCheckDigit == null) {
                throw new Exception("GSTIN supplied for checkdigit calculation is null");
            }
            cpChars = GSTN_CODEPOINT_CHARS.toCharArray();
            inputChars = gstinWOCheckDigit.trim().toUpperCase().toCharArray();

            int mod = cpChars.length;
            for (int i = inputChars.length - 1; i >= 0; i--) {
                int codePoint = -1;
                for (int j = 0; j < cpChars.length; j++) {
                    if (cpChars[j] == inputChars[i]) {
                        codePoint = j;
                    }
                }
                int digit = factor * codePoint;
                factor = (factor == 2) ? 1 : 2;
                digit = (digit / mod) + (digit % mod);
                sum += digit;
            }
            checkCodePoint = (mod - (sum % mod)) % mod;
            return gstinWOCheckDigit + cpChars[checkCodePoint];
        } finally {
            inputChars = null;
            cpChars = null;
        }
    }

}
