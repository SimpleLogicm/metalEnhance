package com.msimplelogic.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.model.OnlineSchemeModel;
import com.msimplelogic.model.SpinerListModel;
import com.msimplelogic.model.SwipeImage;
import com.msimplelogic.model.Theme;
import com.msimplelogic.model.TimeSheetShowModel;

import org.json.JSONArray;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Global_Data {
    public static String DistributorStatus="";
    public static String Voice_value="";
    public static String customernaaaame="";
    public static String q="";
    public static String cam="";
    public static String quickorder_maponback="";
    public static String reject="";
    public static String newll="";
    public static String quat="";
    public static String emailcustomer="";
    public static String sync="";
    public static String dayscheduleback="";
    public static String Code="";
    public static String Instatus="";

    public static String fromQuotation="";
    public static String quickedit="";
    public static String intentvalue="";
    public static String ExpenseId="";
    public static String rpquickorder="";
    public static String mrpquickorder="";
    public static String smart_order_adaptor="";
    public static String custname="";
    public static String btnquick="";
    public static String quickremark="";
    public static String quickdate="";
    public static Bitmap quicksign;
    public static Bitmap emptyBitmap;
    public static String quickorder_detail2="";
    public static String quickorder_detail2nft="";
    public static String quikeditdel="";
    public static String addcustomerintent="";
    public static String customeroutstanding="";
    public static String updateStockStatus="";
    public static Double totalsumsmartorder=0.0;
    public static String ExpenseStatus="";
    public static HashMap<Integer,String> image_check = new HashMap<>();
    public static String order_favorite_flag = "";
    public static HashMap<Integer,String> Order_Select_check = new HashMap<>();
    public static String LeaveMHistory="";
    public static String Selected_USER_EMAIL="";
    public static String ExpenseName="";
    public static String GLOVEL_CUSTOMER_State="";
    public static String GLOVEL_CUSTOMER_City="";
    public static String GLOVEL_CUSTOMER_Beat="";
    public static String PlannerName = "";
    public static String PlannerUpdate = "";
    public static String GLOVEL_CUSTOMER_SERCH_State="";
    public static String GLOVEL_CUSTOMER_SERCH_City="";
    public static String GLOVEL_CUSTOMER_SERCH_Beat="";
    public static String GLOVEL_CUSTOMER_SERCH_VALUE="";
    public static String GLOVEL_CUSTOMER_SERCH_VALUE_NEW="";
    public static List<Catalogue_model> catalogue_m = new ArrayList<>();
    public static List<OnlineSchemeModel> catalogue_mr = new ArrayList<>();
    public static List<SwipeImage> imageUrlArr = new ArrayList<>();
    public static List<String> imageUrlArr1 = new ArrayList<>();
    public static List<String> multiSelectVariant = new ArrayList<>();
    public static String[] tempArray;
    public static JSONArray productCatalogueImages;
    public static String forgetPwdStatus = "";
    public static int Globel_Month = 0;
    public static String SpeedometerStatus = "";
    public static String Globelo_OU_CUSTID = "";
    public static String Globelo_OU_CUST_name = "";
    public static JSONArray products;
    public static String NewCategory = "";
    public static String NewProduct = "";
    public static String BarGraphStatus = "";
    public static String GLOVEL_PRODUCT_SERCH_VALUE = "";
    public static String Act_Performance = "";
    public static String PieStatus = "";
    public static String Globel_fromDate = "";
    public static String Globel_toDate = "";
    public static String F_PRODUCT_NAME = "";
    public static String GLOVEL_PRODUCT_SERCH_Category = "";
    public static String GLOVEL_PRODUCT_SERCH_Sub_Category = "";
    public static String GLOVEL_PRODUCT_SERCH_Variant = "";
    public static ArrayList<String> FINAL_PI_X = new ArrayList<String>();
    public static  ArrayList <String> FINAL_PI_Y = new ArrayList<String>();
    public static String NewVariant = "";
    public static String CatlogueStatus = "";
    public static String CustLandlineNo = "";
    public static String ProductVariant = "";
    public static String GlobeloPAmount = "";
    public static String GlobeloPname = "";
    public static String marker_color = "";
    public static String SchemeCodeEdit = "";
    public static String SchemeItemQty = "";
    public static Double GrandTotal = 0.0;
    public static String G_BEAT_IDC = "";
    public static String G_BEAT_service_flag = "";
    public static String G_BEAT_VALUEC = "";
    public static String G_CBUSINESS_TYPE = "";
    public static String G_RadioG_valueC = "";
    public static ArrayList<String> CustAnaXaxisPI = new ArrayList<String>();
    public static  ArrayList <String> CustAnaYaxisPI = new ArrayList<String>();
    private int mMessageSentParts;
    private int mMessageSentTotalParts;
    private int mMessageSentCount;
    public static List<String> array_of_pVarient =new ArrayList<>();
    public static List<SpinerListModel> spiner_list_modelList =new ArrayList<SpinerListModel>();
    public static String custServiceType = "";
    public static String QuotebeatId = "";
    public static String QuoteDisId = "";
    public static String QuoteSignaturePath = "";
    public static String orderDetail1 = "";
    public static String orderDetail2 = "";
    public static String orderDetailName = "";
    public static String QuoteOrderImage = "";
    public static String quotationId = "";
    public static String quoteOrderAmount = "";
    public static String quoteOrderType = "";
    public static String quoteItemNo = "";
    public static String expectedAmountAtCreate = "";
    public static String maplat = "";
    public static String maplon = "";
    public static Boolean app_sound = false;
    public static String New_Label="";
    public static String Var_Label="";
    public static String target_amount="";
    public static String pre_schecode="";
    public static String editable="";
    public static String mandatory="";
    //public static String multiSelectVariant="";
    public static String allow="";
    public static String PrevDate="";
    public static String QuoteStatus="";
    public static String TaOrderListStatus="";
    public static ArrayList<Local_Data> list_cities = new ArrayList<>();
    public static ArrayList<Local_Data> list_mapdata = new ArrayList<>();

    public static ArrayList<String> p_code = new ArrayList<>();
    public static ArrayList<String> p_mrp = new ArrayList<>();
    public static ArrayList<String> p_amount = new ArrayList<>();
    public static ArrayList<String> p_qty = new ArrayList<>();

    public static String Varient_value_add_flag = "";
    public static int image_counter = 0;
    public static String Stock_warehouse_flag="";
    public static String Stock_product_flag="";
    public static String Stock_product_flag_value_check="";
    public static String Stock_warehouse_flag_value_check="";

    public static String sound_file = "";
    public static String Web_view_back="";
    public static String Web_view_url="";
    public static String Web_view_Title="";
    public static String Previous_Order_ServiceOrder_ID="";
    public static String Previous_Order_UpdateOrder_ID="";
    public static HashMap<String, String> Order_hashmap = new HashMap<String, String>();
    public static HashMap<String, String> Orderproduct_remarks = new HashMap<String, String>();
    public static HashMap<String, String> Orderproduct_detail1 = new HashMap<String, String>();
    public static HashMap<String, String> Orderproduct_detail2 = new HashMap<String, String>();
    public static HashMap<String, String> Orderproduct_detail3 = new HashMap<String, String>();
    public static HashMap<String, String> Orderproduct_detail4 = new HashMap<String, String>();
    public static HashMap<String, String> Orderproduct_detail5 = new HashMap<String, String>();
    public static HashMap<String, String> Order_Delivery_hashmap = new HashMap<String, String>();
    public static HashMap<String, String> return_oredr = new HashMap<String, String>();
    public static HashMap<String, String> quantity_returnorder = new HashMap<String, String>();
    public static HashMap<String, String> batchno_returnorder = new HashMap<String, String>();
    public static HashMap<String, String> refundamount_returnorder = new HashMap<String, String>();
    public static HashMap<String, String> remark_returnorder = new HashMap<String, String>();

    public static ArrayList<String> Number = new ArrayList<>();
    public static String Target_From_Month="";
    public static String Target_To_Month="";
    public static String Target_Product_Category="";
    public static String Target_Year="";
    public static String target_quarter="";
    public static String target_grpby="";
    public static String SCHE_CODE="";
    public static String e_remarks = "";
    public static String e_detail1 = "";
    public static String e_detail2 = "";
    public static String e_detail3 = "";
    public static String e_detail4 = "";
    public static String e_detail5 = "";

    public static String Search_Category_name = "";
    public static String Search_Product_name = "";

    public static Double lat;
    public static Double lon;
    public static Double amt_outstanding;

    public static String AmountOutstanding="";
    public static String AmountOverdue="";
    public static String customer_code="";
    public static String LOCATION_SERVICE_HIT="";
    public static String customer_MobileNumber="";
    public static String USER_FIRST_NAME="";
    public static String USER_MANAGER_NAME="";
    public static String USER_LAST_NAME="";
    public static String CUSTOMER_NAME_NEW="";
    public static String CUSTOMER_ADDRESS_NEW="";
    public static String customer_reportingTo="";
    public static String cus_MAnager_mobile="";
    public static String customer_Address="";
    public static String credit_limit_amount="";
    public static String outstandings_amount="";
    public static String item_code="";
    public static String SYNC_ORDER_COUNT="";
    public static String item_code_return="";
    public static String CUSTOMER_SERVICE_FLAG="";
    public static String pname="";
    public static String amnt="";
    public static String amnt1="";
    public static String rr_price="";
    public static String cust_str="";
    public static String retailer="";
    public static String txt_label="";
    public static String calendar_resp="";
    public static String select_date="";
    public static String calspinner="";
    public static String imei_no="";
    public static String local_user="";
    public static String local_pwd="";
    public static String local_imeino="";
    public static String login_resp="";

    public static String user_name="";

    public static String user_email="";

    public static String variant_rr="";
    public static String variant_mrp="";
    public static String order_city="";
    public static String order_state="";
    public static String order_beat="";
    public static String order_retailer="";
    public static String order_category="";
    public static String order_product="";
    public static String order_variant="";
    public static String order_qty="";
    public static float order_amount;
    public static ArrayList<String> results3;
    public static String GLOVEL_CATEGORY_NAME="";
    public static String GLOVEL_SUBCATEGORY_NAME="";
    public static String GLOVEL_LONG_DESC="";
    public static String GLOVEL_CATEGORY_SELECTION="";
    public static String GLOVEL_SubCategory_Button="";
    public static String GLOVEL_INVOICE_VALUE="";

    public static String GLOVEL_RETURN_FLAG="";
    public static String GLOVEL_BY_FILTER_RETURN="";
    public static String GLOVEL_ITEM_MRP="";
    public static String GLOVEL_FILTER_FLAG="";
    public static String GLOvel_STATE="";
    public static String GLOvel_CITY="";
    public static String GLOvel_ADDRESS="";

    public static String GLOvel_LATITUDE="";
    public static String GLOvel_LONGITUDE="";
    public static String GLOvel_Bearing = "";
    public static String GLOvel_Accuracy = "";
    public static String GLOvel_Speed = "";
    public static String GLOvel_Altitude = "";
    public static String GLOvel_Provider = "";
    public static String address="";
    public static String lat_val="";
    public static String device_id="";
    public static String GLObalOrder_id="";
    public static String GLObalOrder_id_return="";
    public static String long_val="";
    public static String sales_btnstring="";
    public static String GLOvel_STATE_n="";
    public static String GLOvel_CITY_n="";
    public static String GLOvel_CUSTOMER_ID="";
    public static String GLOvel_ITEM_NUMBER="";
    public static String GLOvel_ITEM_NUMBER_RETURN="";
    public static String GLOvel_GORDER_ID="";
    public static String selected="";
    public static String orderid="";
    public static String GLOvel_GORDER_ID_RETURN="";
    public static String GLOvel_REMARK="";
    public static String GLOvel_USER_EMAIL="";
    public static  ArrayList productList = new ArrayList();
    public static  ArrayList feedbackValues = new ArrayList();
    public static String getsign_str="";
    public static String total_qty = "";
    public static String Schedule_FLAG = "";
    public static String Default_Image_Path = "";
    public static String Default_video_Path = "";
    public static String MRP = "";
    public static String RP = "";
    public static String timesheetStatus = "";
    public static String amount = "";
    public static String scheme_amount = "";
    public static String actual_discount = "";
    public static String product_dec = "";
    public static String item_no = "";
    public static String GLOVEL_ORDER_REJECT_FLAG="";
    public static String GLOVEL_ORDER_LIST_FLAG="";
    public static  ArrayList date_arr = new ArrayList();
    public static  ArrayList multicheck_arr = new ArrayList();
    public static List<TimeSheetShowModel> ShowTimesheetArr = new ArrayList<>();
    public static String GLOVEL_CATEGORY_ID="";
    public static String GLOVEL_PRODUCT_ID="";
    public static String GLOVEL_PRODUCT_NAME="";
    public static String GLOVEL_USER_ID="";
    public static String GLOVEL_RETURNOrder_ID="";
    public static String PREVIOUS_ORDER_BACK_FLAG="";
    public static String PREVIOUS_ORDER_BACK_FLAG_REURN="";
    public static String CALENDER_EVENT_TYPE="";
    public static String SYNC_SERVICE_FLAG="";
    public static String orderListStatus="";
    public static String CALENDER_LIST="";
    public static String rsstr = "";
    public static String CALENDER_READONLY_Address="";
    public static String CALENDER_READONLY_Date="";
    public static String GLOVEL_PREVIOUS_ORDER_FLAG="";
    public static String copyTimesheetRadioValue="";
    public static String Glovel_BEAT_ID="";
    public static String Glovel_Contact_name = "";
    public static String Glovel_Contact_Mobile_Number = "";
    public static String push_activity_flag = "";
    public static String order_pushflag = "";
    public static String quickorderback="";

    public static int selectedPosition=-1;
    public static List<Marker> mMarkers = new ArrayList<Marker>();

    public static List<String> Qorder_item_list = new ArrayList<>();

    public static List<Theme> mThemeList = new ArrayList<>();
    public static int selectedTheme;

    //public static String GLOVEL_LONG_DESC="";

    // create map to store
    public static  Map<String, List<String>> map = new HashMap<String, List<String>>();
    public static LinkedHashMap<String, String> quastionmap = new LinkedHashMap<>();

    public static LinkedHashMap<String, String> quastionFeedback = new LinkedHashMap<>();

    Map<String, List<String>> itemmap = new HashMap<String, List<String>>();



    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void Custom_Toast(Context context, String message, String center)
    {

        SharedPreferences sp =context.getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);
        //Toast toast = new Toast(context);
        //View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
       //toast.getView().setBackgroundColor(Color.parseColor("#ffffff"));
       // toast.show();

       // Toast toast = Toast.makeText(context, Html.fromHtml("<font color='#ffffff' >" + message + "</font>"), Toast.LENGTH_LONG);
        //Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        //View view = toast.getView();
        //toast.getView().setBackgroundColor(Color.parseColor("#075b97"));
       // toast.setGravity(Gravity.CENTER, 0, 0);
        if(!center.equalsIgnoreCase("")) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        //toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

//        TextView textView = (TextView) view.findViewById(R.id.message);
//        textView.setText(message);
//        toast.setView(view);
//        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();

        //Toast toast = Toast.makeText(MainActivity.this, R.string.toastMessage, Toast.LENGTH_LONG);
//        toast.getView().setBackgroundColor(Color.parseColor("#F6AE2D"));
//        toast.show();


//        if(!center.equalsIgnoreCase("")) {
//            toast.setGravity(Gravity.CENTER, 0, 0);
//        }
        //LayoutInflater inflater = context.getLayoutInflater();
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.custom_toast,
//                (ViewGroup) ((Activity) context).findViewById(R.id.customToast));

//        if (current_theme== 1){
//            //view.getBackground().setColorFilter(context.getResources().getColor(R.color.primaryColorDarkTheme), PorterDuff.Mode.SRC_IN);
//            //view.setBackgroundColor(context.getResources().getColor(R.color.primaryColorDarkTheme));
//            view.setBackgroundColor(Color.parseColor("#0C9855"));
//        }
//        else
//        {
//            //view.getBackground().setColorFilter(context.getResources().getColor(R.color.primarycolor), PorterDuff.Mode.SRC_IN);
//            //view.setBackgroundColor(context.getResources().getColor(R.color.primarycolor));
//            view.setBackgroundColor(Color.parseColor("#075b97"));
//        }
//        TextView text = view.findViewById(android.R.id.message);
//        //text.setTextColor(context.getResources().getColor(R.color.white));
//
//        toast.show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        String latlong = "";

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            latlong = location.getLatitude() + ","+location.getLongitude();

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }

        return latlong;
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }


    public static long getdiff(String date11,String date22) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputString1 = "23 01 1997";
        String inputString2 = "27 04 1997";
        long diff = 0;
        try {
            Date date1 = myFormat.parse(date11);
            Date date2 = myFormat.parse(date22);
             diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  diff;
    }

    public static class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress = "";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    //locationAddress = " ";
            }
            //  LOCATION.setText(locationAddress);


            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(locationAddress)) {
                Global_Data.address = locationAddress;
                Log.d("GLOBEL ADDRESS G", "V" + locationAddress);

            } else {
                Global_Data.address = "";
                Log.d("GLOBEL ADDRESS G", "address not found.");
            }


        }
    }
}
