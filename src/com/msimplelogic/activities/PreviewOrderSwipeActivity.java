package com.msimplelogic.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.App.AppController;
import com.msimplelogic.activities.kotlinFiles.Favourite;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.activities.kotlinFiles.Smart_Order;
import com.msimplelogic.adapter.OnlineSchemeAdapter;
import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.model.OnlineSchemeModel;
import com.msimplelogic.model.Product;
import com.msimplelogic.swipelistview.BaseSwipeListViewListener;
import com.msimplelogic.swipelistview.SwipeListView;
import com.msimplelogic.swipelistview.sample.adapters.PackageAdapter;
import com.msimplelogic.swipelistview.sample.utils.SettingsManager;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpm.simplelogic.helper.Catalogue_slider_caller;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


public class PreviewOrderSwipeActivity extends BaseActivity implements Catalogue_slider_caller {
    String str;
    Double pp = 0.0;
    String ttl_price;
    private OnlineSchemeAdapter mAdapter;
    private ArrayList<OnlineSchemeModel> catalogue_m1;
    double expectAmt,totalAmt;
    Double sum = 0.0;
    private RecyclerView moreDealRecyclerView;
    //String orderListStatus;
    private ProgressDialog pDialog;
    private ArrayList<Catalogue_model> catalogue_m;
    HashMap<String, String> map;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ArrayList<HashMap<String, String>> SwipeList;
    ArrayList<String> Amount_tp = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private static final int REQUEST_CODE_SETTINGS = 0;
    private ArrayList<String> Distributer_list = new ArrayList<String>();
    private PackageAdapter adapter;
    private ArrayList<Product> dataOrder;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private SwipeListView swipeListView;
    LinearLayout expectedLout;
    TextView textView1, tabletextview1, tabletextview2, tabletextview3;
    static TextView txttotalPreview,txttotalPreview1;
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    static final String TAG_PRODUCT_IMAGE = "product_image";
    static final String TAG_MIN_QTY = "product_min_qty";
    static final String TAG_PKG_QTY = "product_pkg_qty";
    static final String TAG_ITEM_SCHEME = "product_scheme";
    static final String TAG_ITEM_DETAIL1 = "detail1";
    static final String TAG_ITEM_DETAIL2 = "detail2";
    static final String TAG_ITEM_DETAIL3 = "detail3";
    static final String TAG_ITEM_DETAIL4 = "detail4";
    static final String TAG_ITEM_DETAIL5 = "detail5";
    static final String TAG_ITEM_DETAIL6 = "detail6";
    //Button buttonShowScheme;
    String expectedAmt="";
    ImageView imgView,buttonPreviewAddMOre1,buttonPreviewCheckout;
    TextView buttonPreviewAddMOre,buttonPreviewHome;
    static String price_str;
    static float totalPrice;
    String statusOrderActivity = "";
    Button buttonPreviewCheckout1, buttonPreviewHome1, btn_subapprove;
    ArrayAdapter<String> dataDistrubutorsAdapter;
    boolean firstLaunch = false;
    static String c_Total = "";
    EditText expectedAmount;
    Toolbar toolbar;
    TextView viewMoreDealBtn,checkoutBtn,order_toolbar_title;
    ImageView hedder_theame;

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpreview_swipelistview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order_toolbar_title=findViewById(R.id.order_toolbar_title);
        if (Global_Data.intentvalue=="smart"&& Global_Data.intentvalue!=""){
            order_toolbar_title.setText(getResources().getString(R.string.SPreview_Order));
            //txttotalPreview.setText("" + Global_Data.totalsumsmartorder);
            //  txttotalPreview.setText(Global_Data.amount);
        }
        else {
            order_toolbar_title.setText(getResources().getString(R.string.Preview_Order));
        }
        cd = new ConnectionDetector(getApplicationContext());
        txttotalPreview = (TextView) findViewById(R.id.txttotalPreview);
        txttotalPreview1 = (TextView) findViewById(R.id.txttotalPreview1);
        swipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        swipeListView.setNestedScrollingEnabled(true);
        viewMoreDealBtn = (TextView) findViewById(R.id.viewmoredeal_btn);
        checkoutBtn = (TextView) findViewById(R.id.txt_checkout);
        hedder_theame = (ImageView) findViewById(R.id.hedder_theame);

        map = new HashMap<String, String>();

        SwipeList = new ArrayList<HashMap<String, String>>();
        //btn_subapprove = (Button) findViewById(R.id.btn_subapprove);

        //buttonShowScheme = (Button) findViewById(R.id.buttonShowScheme);
        //buttonPreviewAddMOre = (ImageView) findViewById(R.id.buttonPreviewAddMOre);
        buttonPreviewAddMOre = (TextView) findViewById(R.id.buttonPreviewAddMOre);
        //buttonPreviewHome = (Button) findViewById(R.id.buttonPreviewHome);
        buttonPreviewHome = (TextView) findViewById(R.id.buttonPreviewHome);
        //expectedAmount = (EditText) findViewById(R.id.expected_amount);
        //expectedLout = (LinearLayout) findViewById(R.id.expected_lout);
        moreDealRecyclerView = (RecyclerView) findViewById(R.id.knowmoredeal_listview);

        SharedPreferences spf2 = this.getSharedPreferences("SimpleLogic", 0);
        ttl_price = spf2.getString("var_total_price", "");

        catalogue_m = new ArrayList<>();
        pDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        price_str = spf1.getString("var_total_price", "");


        c_Total = getResources().getString(R.string.CTotal);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);
        }

        viewMoreDealBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

              isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    startActivity(new Intent(PreviewOrderSwipeActivity.this, OnlineSchemeActivity.class));
                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"yes");
                }
            }
        });

//        btn_subapprove.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent) {
//
//                    if(TextUtils.isEmpty(expectedAmount.getText().toString()))
//                    {
//                        getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
//                    }else{
//                        expectAmt = new Double(expectedAmount.getText().toString());
//                        totalAmt = new Double(txttotalPreview1.getText().toString());
//
//                        if(expectAmt>totalAmt)
//                        {
//                            Toast.makeText(getApplicationContext(),"Expected Amount Should be less than Total amount",Toast.LENGTH_LONG).show();
//                        }else{
//                            Global_Data.expectedAmountAtCreate=expectedAmount.getText().toString().trim();
//                            getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
//                        }
//                    }
//
////                    if(expectedAmount.getText().toString().length()>0)
////                    {
////                        w = new Double(expectedAmount.getText().toString());
////                        if(w>sum)
////                        {
////                            getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
////                        }else{
////                            Toast.makeText(getApplicationContext(),"Expected Amount Should greater than Total amount",Toast.LENGTH_LONG).show();
////                        }
////
////                    }else{
////                        getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
////                    }
//
//                } else {
//                    // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//            }
//        });

        buttonPreviewCheckout = (ImageView) findViewById(R.id.buttonPreviewCheckout);
       // buttonPreviewCheckout1 = (Button) findViewById(R.id.buttonPreviewCheckout1);

        if (Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales")) {
            buttonPreviewHome.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
            //btn_subapprove.setVisibility(View.VISIBLE);
            buttonPreviewCheckout.setVisibility(View.GONE);
            //buttonPreviewCheckout1.setVisibility(View.GONE);
            //buttonShowScheme.setVisibility(View.GONE);
           // expectedLout.setVisibility(View.VISIBLE);
            //buttonPreviewCheckout.setEnabled(false);

//            expectedAmount.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                    //adapter.getFilter().filter(cs);
//
////                    try {
////
////                    if((Double.parseDouble(String.valueOf(cs)))>sum)
////                    {
////                        w = new Double(expectedAmount.getText().toString());
////                    }
////                    else{
////                        Toast.makeText(getApplicationContext(),"Expected Amount Should greater than Total amount",Toast.LENGTH_LONG).show();
////                    }
////
////                    } catch (NumberFormatException e) {
////                        w = 0; // your default value
////                    }
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                    //Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void afterTextChanged(Editable arg0) {
//                    //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
//                }
//            });

        } else {

        }

        Intent i = this.getIntent();
        Global_Data.orderListStatus = i.getStringExtra("TA_ORDERLIST_STATUS");
        String name = i.getStringExtra("retialer");

        if (i.hasExtra("return")) {
            statusOrderActivity = "return";
        } else if (i.hasExtra("new")) {
            statusOrderActivity = "new";
        } else if (i.hasExtra("previous")) {
            statusOrderActivity = "previous";
        }

  if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status"))
  {
      Global_Data.PREVIOUS_ORDER_BACK_FLAG=Global_Data.orderListStatus;
      buttonPreviewAddMOre.setVisibility(View.INVISIBLE);
      buttonPreviewCheckout.setVisibility(View.GONE);
      //buttonShowScheme.setVisibility(View.INVISIBLE);
      buttonPreviewHome.setVisibility(View.INVISIBLE);

      HashMap<String, String> mapp = new HashMap<String, String>();
      mapp.put(TAG_ITEMNAME, i.getStringExtra("PRODUCT_NAME"));
      mapp.put(TAG_QTY, i.getStringExtra("TOTAL_QTY"));
      mapp.put(TAG_PRICE, i.getStringExtra("MRP"));
      //mapp.put(TAG_ITEM_NUMBER, i.getStringExtra("TA_ORDERLIST_STATUS"));

      //Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.get_category_ids());
      //str += i.getStringExtra("MRP");
      Amount_tp.add(i.getStringExtra("AMOUNT"));
      //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
      //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
      SwipeList.add(mapp);


      for (int m = 0; m < Amount_tp.size(); m++) {
          sum += Double.valueOf(Amount_tp.get(m));
      }
      updateSum(sum);
  }else if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status"))
  {

  } else {
      try {
          List<Local_Data> cont1 = dbvoc.getItemName(Global_Data.GLObalOrder_id);
          for (Local_Data cnt1 : cont1) {
              HashMap<String, String> mapp = new HashMap<String, String>();
              mapp.put(TAG_ITEMNAME, cnt1.getProduct_nm());
              mapp.put(TAG_QTY, cnt1.getQty());
              mapp.put(TAG_PRICE, cnt1.getPrice());
              mapp.put(TAG_ITEM_NUMBER, cnt1.get_category_ids());

              if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.get_mediaUrl()) && !cnt1.get_mediaUrl().equalsIgnoreCase("not found")) {
                  mapp.put(TAG_PRODUCT_IMAGE, cnt1.get_mediaUrl());
              } else {
                  mapp.put(TAG_PRODUCT_IMAGE, "");
              }

              try {
                  List<Local_Data> contactsn1 = dbvoc.GetSchemeByCode(cnt1.get_category_ids());
                  if (contactsn1.size() > 0) {
                      for (Local_Data cn : contactsn1) {
                          mapp.put(TAG_ITEM_SCHEME, cn.getDisplayName());
                          // list3.add("SCHEME : " + cn.getDisplayName());
                      }
                  } else {
                      mapp.put(TAG_ITEM_SCHEME, "");
                  }

                  List<Local_Data> contactsn2 = dbvoc.getProductBymin_cat(cnt1.get_category_ids());
                  if (contactsn2.size() > 0) {
                      for (Local_Data cn : contactsn1) {
                          mapp.put(TAG_MIN_QTY, (cn.getMinqty()));
                          mapp.put(TAG_PKG_QTY, (cn.getPkgqty()));
                          // list3.add("SCHEME : " + cn.getDisplayName());
                      }
                  } else {
                      mapp.put(TAG_MIN_QTY, (""));
                      mapp.put(TAG_PKG_QTY, (""));
                  }

              } catch (Exception e) {
                  e.printStackTrace();
              }

              Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.get_category_ids());
              str += cnt1.getAmount();
              Amount_tp.add(cnt1.getAmount());
              //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
              //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
              SwipeList.add(mapp);
          }

          // }
          Double sum = 0.0;
          for (int m = 0; m < Amount_tp.size(); m++) {
              sum += Double.valueOf(Amount_tp.get(m));
          }
          updateSum(sum);
      } catch (Exception ex) {
          ex.printStackTrace();
      }
  }


//		List<Local_Data> swipedata = dbvoc.getAportunityBy_opid(cn.geopportunity_id());
//        for (Local_Data swipe : swipedata) {
//            map.put(TAG_OPPORTUNITY, swipe.getdescription());
//            map.put(TAG_PRODUCTID, swipe.getPRODUCT_ID());
//            map.put(TAG_VALUE, swipe.getexpected_value());
//           // total =total+Integer.parseInt(swipe.getexpected_value());
//        }
		
		/*ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#8A0808")));
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(name);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		*/
        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.GLOVEL_ITEM_MRP = "";
        //Global_Data.productList.clear();
//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            mTitleTextView.setText(Global_Data.order_retailer);
//            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);
//            //Log.e("DATA", "PreviewOrderSwipeActivity-"+sp.getString("order", ""));
//
//
////        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////		}
//            try {
//                int target = (int) Math.round(sp.getFloat("Target", 0));
//                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
//                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
//                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
//                } else {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
////        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
//                todaysTarget.setText("Today's Target Acheived");
//            }
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        dataOrder = i.getParcelableArrayListExtra("productsList");
        adapter = new PackageAdapter(PreviewOrderSwipeActivity.this, SwipeList);
        totalPrice = 0.00f;

//        if (Build.VERSION.SDK_INT >= 11) {
//            swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            swipeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    mode.setTitle("Selected (" + swipeListView.getCountSelected() + ")");
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.menu_delete) {
                        swipeListView.dismissSelected();
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_choice_items, menu);
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    swipeListView.unselectedChoiceStates();
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
        }

        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {

            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    dataOrder.remove(position);
                }
                adapter.notifyDataSetChanged();
            }

        });

        swipeListView.setAdapter(adapter);

        reload();

        catalogue_m1= new ArrayList<>();
        mAdapter = new OnlineSchemeAdapter(PreviewOrderSwipeActivity.this,getApplicationContext(), catalogue_m1, PreviewOrderSwipeActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        moreDealRecyclerView.setLayoutManager(mLayoutManager);
        moreDealRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moreDealRecyclerView.setAdapter(mAdapter);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            OnlineSchemeCatalogue();
        } else {
            Global_Data.Custom_Toast(this, "" + getResources().getString(R.string.internet_connection_error), "Yes");
        }

//        buttonShowScheme.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//              isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent) {
//                    startActivity(new Intent(PreviewOrderSwipeActivity.this, OnlineSchemeActivity.class));
//                } else {
//                    // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//            }
//        });

        buttonPreviewCheckout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    firstLaunch = true;

                    Intent intent = new Intent(getApplicationContext(), CaptureSignature.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });


        checkoutBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    firstLaunch = true;

                    Intent intent = new Intent(getApplicationContext(), CaptureSignature.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

//                    final Dialog dialog = new Dialog(PreviewOrderSwipeActivity.this);
//                    dialog.setContentView(R.layout.distributor_dilogue);
//                    dialog.setTitle(getResources().getString(R.string.PDistributors));
//
//                    Distributer_list.clear();
//                    Distributer_list.add(getResources().getString(R.string.Select_Distributor));
//                    String beat_id = "";
//                    String dis_id = "";
//
//                    List<Local_Data> getbeatid = dbvoc.GetOrders_beatID(Global_Data.GLOvel_GORDER_ID);
//
//                    for (Local_Data cn : getbeatid) {
//                        beat_id = cn.getBEAT_ID();
//                    }
//
//                    List<Local_Data> getdistri_code = dbvoc.getDistributors_code(beat_id);
//                    for (Local_Data cn : getdistri_code) {
//                        dis_id = cn.getDISTRIBUTER_ID();
//                        List<Local_Data> contacts1 = dbvoc.getDistributors_BYID(dis_id);
//                        for (Local_Data cnn : contacts1) {
//                            if (!cnn.getStateName().equalsIgnoreCase("") && !cnn.getStateName().equalsIgnoreCase(" ")) {
//                                String str_categ = "" + cnn.getStateName();
//                                Distributer_list.add(str_categ);
//                            }
//                        }
//                    }
//
//                    final Spinner spnDistributor = (Spinner) dialog.findViewById(R.id.spnDistributor);
//                    dataDistrubutorsAdapter = new ArrayAdapter<String>(PreviewOrderSwipeActivity.this, android.R.layout.simple_spinner_item, Distributer_list);
//                    dataDistrubutorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spnDistributor.setAdapter(dataDistrubutorsAdapter);
//
//                    spnDistributor.setOnItemSelectedListener(new OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View arg1,
//                                                   int pos, long arg3) {
//
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> arg0) {
//
//                        }
//                    });
//
//                    Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOk);
//                    dialogButtonOk.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if (spnDistributor.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Distributor))) {
//                                Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this, getResources().getString(R.string.Please_Select_distributor), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                            } else {
//
//                                String id = "";
//                                List<Local_Data> cont1 = dbvoc.Get_distributer_id(spnDistributor.getSelectedItem().toString().trim());
//                                for (Local_Data cnt1 : cont1) {
//                                    id = cnt1.getDISTRIBUTER_ID();
//                                }
//
//                                dbvoc.updateORDER_DISTRIBUTER(id, Global_Data.GLObalOrder_id);
//                                dialog.dismiss();
//
//
//                                Intent intent = new Intent(getApplicationContext(), CaptureSignature.class);
//                                intent.putParcelableArrayListExtra("productsList", dataOrder);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(intent);
//
//                            }
//                        }
//                    });
//
//                    Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
//                    dialogButtonCancel.setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
                    return true;
                }
                return false;
            }
        });


        buttonPreviewHome.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event

                   // b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    //b.setBackgroundColor(Color.parseColor("#910505"));
                    AlertDialog alertDialog = new AlertDialog.Builder(PreviewOrderSwipeActivity.this).create(); //Read Update
                    alertDialog.setTitle(getResources().getString(R.string.Warning));
                    alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // create a instance of SQLite Database
                            dbvoc = new DataBaseHelper(PreviewOrderSwipeActivity.this);
                            dbvoc.getDeleteTableorder_byOID(Global_Data.GLObalOrder_id);
                            dbvoc.getDeleteTableorderproduct_byOID(Global_Data.GLObalOrder_id);
                            Global_Data.GLOvel_GORDER_ID = "";
                            //Intent order_home = new Intent(getApplicationContext(), Order.class);
                            Intent order_home = new Intent(getApplicationContext(), Neworderoptions.class);
                            startActivity(order_home);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    });

                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();

                        }
                    });


                    alertDialog.show();

                    return true;
                }
                return false;
            }
        });

        //buttonPreviewCheckout.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewHome.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewAddMOre.setBackgroundColor(Color.parseColor("#414042"));
        buttonPreviewAddMOre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(Color.parseColor("#910505"));
//				if(statusOrderActivity.equalsIgnoreCase("previous")){
                final Intent i = new Intent(PreviewOrderSwipeActivity.this, NewOrderActivity.class);
                i.putExtra("data", "data");
                i.putParcelableArrayListExtra("productsList", dataOrder);

                // Log.e("DATA", "Starting out NewOrderFragment...");
                SharedPreferences sp = PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);

                i.putExtra("retialer", "" + sp.getString("RetailerName", ""));
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //finish();
                //this.startActivity(i);

                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished() {
                        //Log.e("DATA", "Starting in NewOrderFragment...");
                        startActivity(i);
                        finish();
                    }
                });
//				}
//				else {
//					onBackPressed();
//				}
            }
        });
    }

    private void reload() {
        SettingsManager settings = SettingsManager.getInstance();
        swipeListView.setSwipeMode(settings.getSwipeMode());
        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }


    public boolean onOptionsItemSelected (int featureId, MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            //finish();
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTINGS:
                reload();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstLaunch) {

        }
        SharedPreferences spf = PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);
        if (spf.getInt("Capture", 0) == 1) {
            SharedPreferences.Editor editor = spf.edit();
            editor.putInt("Capture", 2);
            editor.commit();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //this.finish();
//                onBackPressed();
//                Global_Data.CatlogueStatus = "";
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Global_Data.CatlogueStatus = "";
                return true;
        }
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        //super.onBackPressed();

        if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("TRUE")) {
            Global_Data.CatlogueStatus = "";
            final Intent i = new Intent(getApplicationContext(), Previous_Order.class);
            i.putExtra("data", "data");
            // i.putParcelableArrayListExtra("productsList", dataOrder);

            //SharedPreferences  sp=PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);

            //i.putExtra("retialer", ""+sp.getString("RetailerName", ""));
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //finish();
            //this.startActivity(i);

            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    startActivity(i);
                    finish();
                }
            });
        }else if (Global_Data.intentvalue=="smart"&& Global_Data.intentvalue!=""){
            Global_Data.intentvalue="";
            Intent order_home = new Intent(getApplicationContext(), Smart_Order.class);
            startActivity(order_home);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }else if(Global_Data.intentvalue=="favourite"&& Global_Data.intentvalue!=""){
            Global_Data.intentvalue="";
            Intent order_home = new Intent(getApplicationContext(), Favourite.class);
            startActivity(order_home);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }

        else if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("")) {
            Global_Data.CatlogueStatus = "";
            Global_Data.intentvalue="";

            final Intent i = new Intent(PreviewOrderSwipeActivity.this, NewOrderActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    //Log.e("DATA", "Starting in NewOrderFragment...");
                    startActivity(i);
                    finish();
                }
            });
        } else if (statusOrderActivity.equalsIgnoreCase("previous")) {
            AlertDialog alertDialog = new AlertDialog.Builder(PreviewOrderSwipeActivity.this).create(); //Read Update
            alertDialog.setTitle(getResources().getString(R.string.Warning));
            alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Global_Data.CatlogueStatus = "";
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });
            alertDialog.show();
        }else if(Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("orderlist_status")){
            Global_Data.PREVIOUS_ORDER_BACK_FLAG = "";
            final Intent i = new Intent(getApplicationContext(), TargetAnalysisActivity.class);
            i.putExtra("data", "data");

            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    public static void updateSum(Double sum) {
        if (price_str.length() > 0) {
            txttotalPreview.setText(price_str + " : "+" ₹" + sum);
            txttotalPreview1.setText(""+" ₹" +sum);
        } else {
            txttotalPreview.setText(c_Total +" ₹"+  sum);
            txttotalPreview1.setText(""+" ₹"+sum);
        }
        //txttotalPreview.setText("Total		:		"+sum);
    }

    public void OnlineSchemeCatalogue() {

        String domain = "";
        String domain1 = "";

        //dialog = new ProgressDialog(Video_Main_List.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setMessage(getResources().getString(R.string.Please_Wait));
        pDialog.setTitle(getResources().getString(R.string.app_name));
        pDialog.setCancelable(false);
        pDialog.show();

        String service_url = "";
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url1 = Cust_domain + "metal/api/v1/";
        //String device_id = sp.getString("devid", "");
        domain = service_url1;

        domain1 = Cust_domain;

        SharedPreferences spf = PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

//        Global_Data.Search_Category_name = Global_Data.Search_Category_name.replaceAll("&", "%26");
//        Global_Data.Search_Product_name = Global_Data.Search_Product_name.replaceAll("&", "%26");
//        Global_Data.ProductVariant = Global_Data.ProductVariant.replaceAll("&", "%26");

        if (Global_Data.ProductVariant.trim().equalsIgnoreCase("Select All")) {

            try {
                service_url = domain + "products/get_products_of_active_schemes?email=" +
                        URLEncoder.encode(user_email, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //service_url = service_url.replaceAll(" ", "%20");

        } else {
            try {
                service_url = domain + "products/get_products_of_active_schemes?email=" + URLEncoder.encode(user_email, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //service_url = service_url.replaceAll(" ", "%20");
        }
        Log.d("Server_url", "Server url" + service_url);

        StringRequest stringRequest = null;
        final String finalDomain = domain1;
        stringRequest = new StringRequest(service_url,
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
                                if (json.has("result")) {
                                    response_result = json.getString("result");

//                                    Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                    Global_Data.Custom_Toast(getApplicationContext(), response_result,"yes");
//
//                                    Intent launch = new Intent(PreviewOrderSwipeActivity.this, NewOrderActivity.class);
//                                    startActivity(launch);
//                                    finish();
                                } else {

                                    JSONArray products = json.getJSONArray("products");
                                    Global_Data.products = json.getJSONArray("products");
//
                                    Log.i("volley", "response reg products Length: " + products.length());
//
                                    Log.d("users", "products" + products.toString());
//
                                    catalogue_m.clear();
                                    pp = 0.0;

                                    if (products.length() > 0) {
                                        for (int i = 0; i < products.length(); i++) {
                                            JSONObject object = products.getJSONObject(i);
                                            OnlineSchemeModel catalogue_model = new OnlineSchemeModel();
                                            catalogue_model.setItem_name(object.getString("name"));
                                            catalogue_model.setItem_number(object.getString("code"));
                                            catalogue_model.setItem_rp(object.getString("retail_price"));
                                            catalogue_model.setItem_mrp(object.getString("mrp"));
                                            catalogue_model.setItem_min_qty(object.getString("min_order_qty"));
                                            catalogue_model.setItem_max_qty(object.getString("max_order_qty"));
                                            catalogue_model.setItem_pkg_qty1(object.getString("pkg_qty1"));
                                            catalogue_model.setItem_pkg_qty2(object.getString("pkg_qty2"));

                                            catalogue_model.setScheme_id(object.getString("scheme_id"));
                                            catalogue_model.setScheme_name(object.getString("scheme_name"));
                                            catalogue_model.setScheme_type(object.getString("scheme_type"));
                                            catalogue_model.setScheme_amount(object.getString("scheme_amount"));
                                            catalogue_model.setScheme_description(object.getString("scheme_description"));
                                            catalogue_model.setScheme_buy_qty(object.getString("scheme_buy_qty"));
                                            catalogue_model.setScheme_get_qty(object.getString("scheme_get_qty"));
                                            catalogue_model.setScheme_topsellingproduct(object.getString("is_top_selling_product"));
                                            catalogue_model.setScheme_min_qty(object.getString("scheme_min_qty"));

                                            JSONArray product_catalogues = object.getJSONArray("product_catalogues");
                                            // Global_Data.products = object.getJSONArray("product_catalogues");

                                            List<Local_Data> contactsn1 = dbvoc.GetSchemeByCode(object.getString("code"));
                                            if (contactsn1.size() > 0) {
                                                for (Local_Data cn : contactsn1) {
                                                    catalogue_model.setItem_scheme(cn.getDisplayName());
                                                    // list3.add("SCHEME : " + cn.getDisplayName());
                                                }
                                            }

                                            for (int j = 0; j < product_catalogues.length(); j++) {
                                                JSONObject image_object = product_catalogues.getJSONObject(j);
                                                catalogue_model.setItem_image_url(finalDomain + image_object.getString("thumb_url"));
                                                //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
                                                //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
                                            }

                                            List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, object.getString("code"));
                                            if (contactsn.size() > 0) {
                                                for (Local_Data cn : contactsn) {
                                                    catalogue_model.setItem_quantity(cn.get_delivery_product_order_quantity());
                                                    catalogue_model.setItem_amount(getResources().getString(R.string.PRICE) + cn.getAmount());


                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getRemarks())) {
                                                        catalogue_model.setItem_remarks(cn.getRemarks());
                                                    } else {
                                                        catalogue_model.setItem_remarks("");
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail1())) {
                                                        catalogue_model.setDetail1(cn.getDetail1());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail2())) {
                                                        catalogue_model.setDetail2(cn.getDetail2());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail3())) {
                                                        catalogue_model.setDetail3(cn.getDetail3());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail4())) {
                                                        catalogue_model.setDetail4(cn.getDetail4());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail5())) {
                                                        catalogue_model.setDetail5(cn.getDetail5());
                                                    }


                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getAmount())) {
                                                        pp += Double.valueOf(cn.getAmount());
                                                    }
                                                }
                                            } else {
                                                catalogue_model.setItem_quantity("");
                                                catalogue_model.setItem_amount("");
                                                catalogue_model.setItem_remarks("");
                                                catalogue_model.setDetail1("");
                                                catalogue_model.setDetail2("");
                                                catalogue_model.setDetail3("");
                                                catalogue_model.setDetail4("");
                                                catalogue_model.setDetail5("");
                                            }

                                            catalogue_m1.add(catalogue_model);

                                        }

                                        //Intent launch = new Intent(context,Youtube_Player_Activity.class);
                                        //startActivity(launch);

                                        //Global_Data.catalogue_m = catalogue_m;
                                        pDialog.hide();
                                        mAdapter.notifyDataSetChanged();

                                        if (ttl_price.length() > 0) {
                                            txttotalPreview.setText(ttl_price + ":"+" ₹" + pp);
                                        } else {

                                            txttotalPreview.setText(getResources().getString(R.string.Total)+" ₹" + pp);
                                        }


                                    } else {
                                        pDialog.hide();
                                        // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();
//
//                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.product_not_found_message), Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
                                        Global_Data.Custom_Toast(getApplicationContext(),  getResources().getString(R.string.product_not_found_message),"yes");

                                        Intent launch = new Intent(PreviewOrderSwipeActivity.this, NewOrderActivity.class);
                                        startActivity(launch);
                                        finish();
                                    }

                                    //finish();

                                }

                                //  finish();
                                // }

                                // output.setText(data);
                            } catch (JSONException e) {
                                e.printStackTrace();


//                                Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this,
//                                        "Service Error",
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(getApplicationContext(), "Service Error","yes");


                                Intent launch = new Intent(PreviewOrderSwipeActivity.this, MainActivity.class);
                                startActivity(launch);
                                finish();

                                pDialog.hide();
                            }
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();
                            pDialog.dismiss();
                        }
                        pDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(Image_Gellary.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(PreviewOrderSwipeActivity.this,
                                    "Network Error","");
                        } else if (error instanceof AuthFailureError) {

//                            Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this,
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(PreviewOrderSwipeActivity.this,
                                    "Server AuthFailureError  Error","");
                        } else if (error instanceof ServerError) {
//
//                            Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(PreviewOrderSwipeActivity.this,
                                    getResources().getString(R.string.Server_Errors),"");
                        } else if (error instanceof NetworkError) {

//                            Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this,
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(PreviewOrderSwipeActivity.this,
                                    "Network   Error","");
                        } else if (error instanceof ParseError) {


//                            Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(PreviewOrderSwipeActivity.this,
                                    "ParseError   Error","");
                        } else {
                            // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(PreviewOrderSwipeActivity.this, error.getMessage(), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(PreviewOrderSwipeActivity.this, error.getMessage(),"yes");
                        }
                        Intent launch = new Intent(PreviewOrderSwipeActivity.this, MainActivity.class);
                        startActivity(launch);
                        finish();
                        pDialog.dismiss();
                        // finish();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PreviewOrderSwipeActivity.this);

        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void slideCall(int po) {

    }
}
