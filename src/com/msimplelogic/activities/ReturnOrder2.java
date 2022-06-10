package com.msimplelogic.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.msimplelogic.activities.R;

import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.model.Product;
import com.msimplelogic.services.getServices;
import com.msimplelogic.swipelistview.BaseSwipeListViewListener;
import com.msimplelogic.swipelistview.SwipeListView;
import com.msimplelogic.swipelistview.sample.utils.SettingsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


public class ReturnOrder2 extends BaseActivity {
    String str;
    static String price_str;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> SwipeList;
    ArrayList<String> Amount_tp = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private static final int REQUEST_CODE_SETTINGS = 0;
    private ArrayList<String> Distributer_list = new ArrayList<String>();
    private PackageAdapterReturnOrder adapter;
    private ArrayList<Product> dataOrder;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private SwipeListView swipeListView;
    static TextView txttotalPreview;
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    ImageView imgView;
    static float totalPrice;
    String statusOrderActivity = "";
    Toolbar toolbar;
    Button buttonPreviewCheckout, buttonPreviewCheckout1, btn_subapprove;
    ArrayAdapter<String> dataDistrubutorsAdapter;
    TextView buttonPreviewAddMOre,buttonPreviewHome;
    static String c_Total = "";
    boolean firstLaunch = false;

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.return_order_2_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getResources().getString(R.string.Sales_Return));

        txttotalPreview = (TextView) findViewById(R.id.txttotalPreview);
        swipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        map = new HashMap<String, String>();

        SwipeList = new ArrayList<HashMap<String, String>>();
        btn_subapprove = (Button) findViewById(R.id.btn_subapprove);

        btn_subapprove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                getServices.SYNCORDER_BYCustomerINSTI(ReturnOrder2.this, "");

            }
        });

        buttonPreviewCheckout = (Button) findViewById(R.id.buttonPreviewCheckout);
        buttonPreviewCheckout1 = (Button) findViewById(R.id.buttonPreviewCheckout1);

        if (Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales")) {
            btn_subapprove.setVisibility(View.VISIBLE);
            buttonPreviewCheckout.setVisibility(View.GONE);
            buttonPreviewCheckout1.setVisibility(View.VISIBLE);
            //buttonPreviewCheckout.setEnabled(false);
        } else {

        }


        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        price_str = spf1.getString("var_total_price", "");

        if (price_str.length() > 0) {
            txttotalPreview.setText("" + price_str);
        } else {
            txttotalPreview.setText(getResources().getString(R.string.Total_Price));
        }
        c_Total = getResources().getString(R.string.CTotal);

        Intent i = this.getIntent();
        if (i.hasExtra("return")) {
            statusOrderActivity = "return";
        } else if (i.hasExtra("new")) {
            statusOrderActivity = "new";
        } else if (i.hasExtra("previous")) {
            statusOrderActivity = "previous";
        }

        List<Local_Data> cont1 = dbvoc.getItemName_return(Global_Data.GLObalOrder_id_return);
        for (Local_Data cnt1 : cont1) {
            HashMap<String, String> mapp = new HashMap<String, String>();
            mapp.put(TAG_ITEMNAME, cnt1.getProduct_nm());
            mapp.put(TAG_QTY, cnt1.getQty());
            mapp.put(TAG_PRICE, cnt1.getPrice());
            mapp.put(TAG_ITEM_NUMBER, cnt1.get_category_ids());
            Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.get_category_ids());
            str += cnt1.getAmount();
            Amount_tp.add(cnt1.getAmount());
            SwipeList.add(mapp);
        }
        Double sum = 0.0;
        for (int m = 0; m < Amount_tp.size(); m++) {
            sum += Double.valueOf(Amount_tp.get(m));
        }
        updateSum(sum);

        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.GLOVEL_ITEM_MRP = "";
        //Global_Data.productList.clear();
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(Global_Data.order_retailer);
            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");
            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);

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


//        TextView welcomeUser=(TextView)findViewById(R.id.txtWelcomeUser);
//        //question_value.setTypeface(null,Typeface.BOLD);
//         welcomeUser.setText(sp.getString("FirstName", "")+" "+ sp.getString("LastName", ""));

        dataOrder = i.getParcelableArrayListExtra("productsList");
        adapter = new PackageAdapterReturnOrder(ReturnOrder2.this, SwipeList);

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


        buttonPreviewCheckout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    firstLaunch = true;

                    SharedPreferences sp = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);

                    @SuppressWarnings("unused")
                    int data_stateid = sp.getInt("StateID", 0);
                    @SuppressWarnings("unused")
                    int data_cityID = sp.getInt("cityID", 0);
                    @SuppressWarnings("unused")
                    int data_beatID = sp.getInt("BeatID", 0);

                    // create a Dialog component
                    final Dialog dialog = new Dialog(ReturnOrder2.this);
                    dialog.setContentView(R.layout.distributor_dilogue);
                    dialog.setTitle(getResources().getString(R.string.PDistributors));
                    Distributer_list.clear();
                    Distributer_list.add(getResources().getString(R.string.Select_Distributor));

                    String beat_id = "";
                    String dis_id = "";

                    List<Local_Data> getbeatid = dbvoc.GetreturnOrders_beatID(Global_Data.GLOvel_GORDER_ID_RETURN);
                    for (Local_Data cn : getbeatid) {
                        beat_id = cn.getBEAT_ID();
                    }

                    List<Local_Data> getdistri_code = dbvoc.getDistributors_code(beat_id);
                    for (Local_Data cn : getdistri_code) {
                        dis_id = cn.getDISTRIBUTER_ID();
                        List<Local_Data> contacts1 = dbvoc.getDistributors_BYID(dis_id);
                        for (Local_Data cnn : contacts1) {
                            if (!cnn.getStateName().equalsIgnoreCase("") && !cnn.getStateName().equalsIgnoreCase(" ")) {
                                String str_categ = "" + cnn.getStateName();
                                Distributer_list.add(str_categ);
                            }
                        }
                    }

                    final Spinner spnDistributor = (Spinner) dialog.findViewById(R.id.spnDistributor);
                    //dataDistrubutorsAdapter = new ArrayAdapter<String>(PreviewOrderSwipeActivity.this,android.R.layout.simple_spinner_item, Global_Data.results3);
                    dataDistrubutorsAdapter = new ArrayAdapter<String>(ReturnOrder2.this, android.R.layout.simple_spinner_item, Distributer_list);
                    dataDistrubutorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnDistributor.setAdapter(dataDistrubutorsAdapter);

                    spnDistributor.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int pos, long arg3) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });

                    Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOk);
                    dialogButtonOk.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (spnDistributor.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Distributor))) {
//                                Toast toast = Toast.makeText(ReturnOrder2.this, getResources().getString(R.string.Please_Select_distributor), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(ReturnOrder2.this, getResources().getString(R.string.Please_Select_distributor), "yes");
                            } else {

                                dialog.dismiss();
                                String id = "";
                                List<Local_Data> cont1 = dbvoc.Get_distributer_id(spnDistributor.getSelectedItem().toString().trim());
                                for (Local_Data cnt1 : cont1) {
                                    id = cnt1.getDISTRIBUTER_ID();
                                }

                                dbvoc.updateORDER_DISTRIBUTER_return(id, Global_Data.GLObalOrder_id_return);

                                SharedPreferences sp = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);

                                @SuppressWarnings("unused")
                                int data_cityID = sp.getInt("cityID", 0);
                                @SuppressWarnings("unused")
                                int data_beatID = sp.getInt("BeatID", 0);
                                @SuppressWarnings("unused")
                                int distID = sp.getInt("DistributorID", 0);
                                //ArrayList<String> contacts = myDbHelper.loadDistributorMobileEmailIds(data_cityID, data_beatID, distID);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("DistributorMobile", "1234567890");
                                editor.putString("DistributorEmailId", "abc@gmail.com");
                                editor.commit();


                                Intent intent = new Intent(getApplicationContext(), ReturnOrder3.class);
                                intent.putParcelableArrayListExtra("productsList", dataOrder);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                //startActivityForResult(intent,SIGNATURE_ACTIVITY);
                                ReturnOrder2.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(intent);
                            }
                        }
                    });

                    Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                    dialogButtonCancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setCancelable(false);
                    dialog.show();

                    return true;
                }
                return false;
            }
        });

        buttonPreviewHome = (Button) findViewById(R.id.buttonPreviewHome);
        buttonPreviewHome.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event

                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    b.setBackgroundColor(Color.parseColor("#910505"));
                    AlertDialog alertDialog = new AlertDialog.Builder(ReturnOrder2.this).create(); //Read Update
                    alertDialog.setTitle(getResources().getString(R.string.Warning));
                    alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // create a instance of SQLite Database
                            dbvoc = new DataBaseHelper(ReturnOrder2.this);
                            dbvoc.getDeleteTableorder_byOID_return(Global_Data.GLObalOrder_id_return);
                            dbvoc.getDeleteTableorderproduct_byOID_return(Global_Data.GLObalOrder_id_return);
                            Global_Data.GLOvel_GORDER_ID_RETURN = "";
                            Intent order_home = new Intent(getApplicationContext(), Order.class);
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

        buttonPreviewAddMOre = (Button) findViewById(R.id.buttonPreviewAddMOre);
        buttonPreviewCheckout.setBackgroundColor(Color.parseColor("#414042"));
        buttonPreviewHome.setBackgroundColor(Color.parseColor("#414042"));
        buttonPreviewAddMOre.setBackgroundColor(Color.parseColor("#414042"));
        buttonPreviewAddMOre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(Color.parseColor("#910505"));
//				if(statusOrderActivity.equalsIgnoreCase("previous")){
                final Intent i = new Intent(ReturnOrder2.this, ReturnOrder1.class);
                i.putExtra("data", "data");
                i.putParcelableArrayListExtra("productsList", dataOrder);

                // Log.e("DATA", "Starting out NewOrderFragment...");
                SharedPreferences sp = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);

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
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        SharedPreferences spf = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);
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
                SharedPreferences sp = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);
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
        if (Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN.equalsIgnoreCase("TRUE")) {

            final Intent i = new Intent(getApplicationContext(), Previous_returnOrder.class);
            i.putExtra("data", "data");

            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    startActivity(i);
                    finish();
                }
            });
        } else if (Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN.equalsIgnoreCase("")) {
            final Intent i = new Intent(ReturnOrder2.this, ReturnOrder1.class);
            i.putExtra("data", "data");
            i.putParcelableArrayListExtra("productsList", dataOrder);

            // Log.e("DATA", "Starting out NewOrderFragment...");
            SharedPreferences sp = ReturnOrder2.this.getSharedPreferences("SimpleLogic", 0);

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
        } else if (statusOrderActivity.equalsIgnoreCase("previous")) {

            AlertDialog alertDialog = new AlertDialog.Builder(ReturnOrder2.this).create(); //Read Update
            alertDialog.setTitle(getResources().getString(R.string.Warning));
            alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

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
        }
    }

    public static void updateSum(Double sum) {

        if (price_str.length() > 0) {
            txttotalPreview.setText("" + sum);
        } else {
            txttotalPreview.setText("" + sum);
        }
        //txttotalPreview.setText("Total		:		"+sum);
    }
}
