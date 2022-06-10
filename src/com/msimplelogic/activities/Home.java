package com.msimplelogic.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.AllOrders_Sync;
import com.msimplelogic.activities.kotlinFiles.Marketing;
import com.msimplelogic.slidingmenu.AddRetailerFragment;
import com.msimplelogic.slidingmenu.CalendarAct;
import com.msimplelogic.webservice.ConnectionDetector;

import java.io.BufferedReader;
import java.util.List;

public class Home extends Fragment {
	BufferedReader in = null;
	ProgressDialog dialog;
	String line;
	Cursor cursor;
	private DataBaseHelper dbHelper;
	public  SQLiteDatabase db;
	ConnectionDetector cd;
	Boolean isInternetPresent = false;
	ImageView plus_toggle;
	TextView txtWelcomeUser;
    LoginDataBaseAdapter loginDataBaseAdapter;
    DataBaseHelper dbvoc;
	View rootView;
	 ImageView order,calendar,custom_serve,expenses,target,schedule,logout,pricing,add_retailernew,marketing_data,syncmdata;
	 public Home(){}
	 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		Global_Data.Stock_warehouse_flag = "";
		Global_Data.Stock_product_flag = "";
		Global_Data.Stock_product_flag_value_check = "";
		Global_Data.Stock_warehouse_flag_value_check = "";
        rootView = inflater.inflate(R.layout.fragment_logout, container, false);
               
        order=(ImageView)rootView.findViewById(R.id.order);
        calendar=(ImageView)rootView.findViewById(R.id.calendar);
        //custom_serve=(ImageView)rootView.findViewById(R.id.custom_serve);
        expenses=(ImageView)rootView.findViewById(R.id.expenses);
        target=(ImageView)rootView.findViewById(R.id.target);
		pricing=(ImageView)rootView.findViewById(R.id.pricing);
		add_retailernew=(ImageView)rootView.findViewById(R.id.add_retailernew);
		marketing_data=(ImageView)rootView.findViewById(R.id.marketing_data);
		syncmdata=(ImageView)rootView.findViewById(R.id.syncmdata);
		txtWelcomeUser=(TextView) rootView.findViewById(R.id.txtWelcomeUser);
		plus_toggle = (ImageView) rootView.findViewById(R.id.plus_toggle);

		Global_Data.GLObalOrder_id = "";
		Global_Data.GLOvel_ITEM_NUMBER = "";
		Global_Data.GLOvel_GORDER_ID_RETURN = "";
		Global_Data.GLObalOrder_id_return = "";
		Global_Data.GLOvel_GORDER_ID= "";
		Global_Data.target_amount= "";
		Global_Data.target_grpby= "";

		String user_name = "";
		if(!Global_Data.USER_FIRST_NAME.equalsIgnoreCase("null"))
		{
			user_name = Global_Data.USER_FIRST_NAME.trim();
			if(!Global_Data.USER_LAST_NAME.equalsIgnoreCase("null"))
			{
				user_name +=  " " + Global_Data.USER_LAST_NAME.trim();
			}
		}

		txtWelcomeUser.setText(user_name);

		loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		Global_Data.CUSTOMER_SERVICE_FLAG = "";

        dbvoc = new DataBaseHelper(getActivity());
        cd = new ConnectionDetector(getActivity());

		plus_toggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MenuOptimization.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

        order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				List<Local_Data> contacts = dbvoc.checkCustomer();
				if(contacts.size() <= 0)
		        {

//					Toast toast = Toast.makeText(getActivity(),
//                            getResources().getString(R.string.customer_notfound_message), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					Global_Data.Custom_Toast(getActivity(),
							getResources().getString(R.string.customer_notfound_message),"yes");

		        }
				else
				{
					Global_Data.CUSTOMER_SERVICE_FLAG = "";
					Intent intent = new Intent(getActivity(), Sales_Dash.class);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					//getActivity().finish();
				}
    	}
		});
         
         calendar.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
				Intent intent1 = new Intent(getActivity(), CalendarAct.class);
                ///Intent intent1 = new Intent(getActivity(), TargetAnalysisActivity.class);
 			    startActivity(intent1);
 				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				//getActivity().finish();
 	    	}
 		});
         
         expenses.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), ExpensesNewActivity.class);
//				startActivity(intent);
 					Intent intent = new Intent(getActivity(), Expenses.class);
	 				startActivity(intent);
	 				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				//getActivity().finish();

 	    	}
 		});

		pricing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Pricing_Main.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				//getActivity().finish();
  		}
		});

		add_retailernew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddRetailerFragment.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

				//requestStoragePermissionLog();
			}
		});


		marketing_data.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), Marketing.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				//getActivity().finish();


			}
		});


		syncmdata.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), AllOrders_Sync.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				//getActivity().finish();

			}
		});
         
         target.setOnClickListener(new OnClickListener() {
  			@Override
  			public void onClick(View v) {

				isInternetPresent = cd.isConnectingToInternet();

				Intent i = new Intent(getActivity(), Target_REYC_Main.class);
				startActivity(i);
				getActivity().finish();

			}
  		});

        return rootView;
    }
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


    }

	private void requestStoragePermissionLog() {

		Dexter.withActivity(getActivity())
				.withPermissions(
						Manifest.permission.READ_CONTACTS,
						Manifest.permission.READ_CALL_LOG)
				.withListener(new MultiplePermissionsListener() {
					@Override
					public void onPermissionsChecked(MultiplePermissionsReport report) {
						// check if all permissions are granted
						if (report.areAllPermissionsGranted()) {

//							Intent intent = new Intent(getActivity(), CallLogActivity.class);
//							startActivity(intent);
//							getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                     //   Toast.makeText(getActivity(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getActivity(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
		Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
		intent.setData(uri);
		startActivityForResult(intent, 101);
	}
}
