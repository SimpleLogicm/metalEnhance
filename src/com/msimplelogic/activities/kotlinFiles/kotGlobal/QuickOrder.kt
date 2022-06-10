package com.msimplelogic.activities.kotlinFiles.kotGlobal

import com.msimplelogic.activities.R

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.activities.Global_Data.GLOvel_CUSTOMER_ID
import com.msimplelogic.adapter.AutoCompleteAdapter
import com.msimplelogic.adapter.Quick_order_adaptor
import com.msimplelogic.adapter.SpinnerListAdapter
import com.msimplelogic.helper.ThemeUtil
import com.msimplelogic.model.Catalogue_model
import com.msimplelogic.model.SpinerListModel
import com.msimplelogic.model.SwipeImage
import cpm.simplelogic.helper.Catalogue_slider_caller
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_quick_order.*
import kotlinx.android.synthetic.main.content_quick_order.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class QuickOrder : BaseActivity(), Quick_order_adaptor.customButtonListener, Catalogue_slider_caller {
    override fun onButtonClickListner(position: Int) {
        try {
            getMethod()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun getMethod() {
        val openDialog = Dialog(this@QuickOrder)
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        openDialog.setCancelable(false)
        openDialog.setContentView(R.layout.catalogue_dialog)

        val dialogClose = openDialog.findViewById<View>(R.id.close_btn) as ImageView

        dialogClose.setOnClickListener {
            // TODO Auto-generated method stub
            openDialog.dismiss()
        }

        openDialog.show()
    }

    override fun slideCall(po: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var pDialog: ProgressDialog? = null
    var pp: Double? = 0.0
    var total_ll: LinearLayout? = null
    var totalprice: TextView? = null
    var dis_pices: TextView? = null
    var online_catalog_save: Button? = null
    var checkout_ll: LinearLayout? = null
    var btn_mapview: TextView? = null
    var ttl_price: String? = null
    var rv: RecyclerView? = null
    var Product_Variant_product: AutoCompleteTextView? = null
    var Product_Variant: AutoCompleteTextView? = null
    var searchLout: RelativeLayout? = null
    var search_spinner_recycleview: RecyclerView? = null
    var list_cancel: Button? = null
    var list_ok: Button? = null
    var mAdapter: Quick_order_adaptor? = null
    var txttotalPreview: TextView? = null
    var catalogue_m: MutableList<Catalogue_model>? = null
    var content: LinearLayout? = null
    var AutoSearchAdapter: AutoCompleteAdapter? = null
    var spinner_list_adapter: SpinnerListAdapter? = null
    var snlist: ArrayList<SpinerListModel>? = null
    var All_customers: ArrayList<String>? = null
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null

    var dbvoc = DataBaseHelper(this)
    private val scheme_id = ArrayList<String>()
    private val p_id = ArrayList<String>()
    private val p_name = ArrayList<String>()
    private val p_mrp = ArrayList<String>()
    private val p_rp = ArrayList<String>()
    private val p_q = ArrayList<String>()
    private val p_price = ArrayList<String>()
    private val p_remarks = ArrayList<String>()
    private val p_detail1 = ArrayList<String>()
    private val p_detail2 = ArrayList<String>()
    private val p_detail3 = ArrayList<String>()
    private val p_detail4 = ArrayList<String>()
    private val p_detail5 = ArrayList<String>()
    private val p_url = ArrayList<String>()

    var q_check = ""
   // var mTheme: Int = ThemeUtil.THEME_RED
    var mIsNightMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_order)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        pDialog = ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        All_customers = ArrayList<String>()
        Global_Data.quickorderback = ""
        snlist = ArrayList<SpinerListModel>()
        Product_Variant = findViewById(R.id.Product_Variant)
        txttotalPreview = findViewById(R.id.txttotalPreview)
        total_ll = findViewById(R.id.total_ll);
        list_cancel = findViewById(R.id.list_cancel);
        content = findViewById(R.id.content);
        list_ok = findViewById(R.id.list_ok);
        totalprice = findViewById(R.id.totalprice);
        dis_pices = findViewById(R.id.dis_pices);
        checkout_ll = findViewById(R.id.checkout_ll);
        btn_mapview = findViewById(R.id.btn_mapview)
        rv = findViewById(R.id.rv)
        Product_Variant_product = findViewById(R.id.Product_Variant_search)
        searchLout = findViewById(R.id.search_lout)
        search_spinner_recycleview = findViewById(R.id.search_spinner_recycleview)
        loginDataBaseAdapter = LoginDataBaseAdapter(this@QuickOrder)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()

        Global_Data.btnquick = ""
        Global_Data.quickremark = ""
        Global_Data.quickdate = ""
        Global_Data.quicksign = Global_Data.emptyBitmap
        Global_Data.quickorder_detail2 = ""
        Global_Data.quickorder_detail2nft = ""
        Global_Data.quickedit = ""
        Global_Data.quikeditdel = ""
        Global_Data.quickorder_maponback=""

        catalogue_m = ArrayList()
        mAdapter = Quick_order_adaptor(applicationContext, Global_Data.catalogue_m, this@QuickOrder)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        rv!!.setLayoutManager(mLayoutManager)
        rv!!.setItemAnimator(DefaultItemAnimator())
        rv!!.setAdapter(mAdapter)

        online_catalog_save = findViewById(R.id.online_catalog_save);

        All_customers!!.clear()

        var cont1 = dbvoc.getAllProducta()
        if (cont1.size > 0) {
            Global_Data.spiner_list_modelList.clear()
            for (cnt1 in cont1) {
                var spiner_list_model = SpinerListModel()
                //				spiner_list_model.setBusiness_category("");
                //				spiner_list_model.setBusiness_unit("");
                spiner_list_model.primary_category = ""
                spiner_list_model.product_variant = cnt1.getProduct_variant()
                spiner_list_model.sub_category = ""
                spiner_list_model.code = cnt1.getCode()
                spiner_list_model.setSelected(false)

                snlist?.add(spiner_list_model)
                Global_Data.spiner_list_modelList.add(spiner_list_model)
            }
        }


        val adapter = ArrayAdapter(this@QuickOrder,  R.layout.autocomplete, All_customers!!)
        Product_Variant!!.setThreshold(1)
        Product_Variant!!.setAdapter(adapter)

        val contacts3 = dbvoc.getcustomer_pre_data()
        if (contacts3.size <= 0) run {
            // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

            this@QuickOrder.runOnUiThread(Runnable { Global_Data.Custom_Toast(this@QuickOrder, resources.getString(R.string.Sorry_No_Record_Found), "yes") })

            //  dialog1!!.dismiss()
            val intent = Intent(applicationContext,
                    MainActivity::class.java)
            startActivity(intent)
        } else {
            for (cn in contacts3) {
                All_customers!!.add(cn.customeR_SHOPNAME.trim())
                val customer_id = cn.cust_Code.trim()

//                var s_name = cn.getCUSTOMER_SHOPNAME()
//                var s_address = cn.getAddress()
//                var c_mobile_number = cn.getMOBILE_NO()
//               var  customer_landline = cn.landlinE_NO
            }


            val adapter = ArrayAdapter(this@QuickOrder, android.R.layout.simple_spinner_dropdown_item,
                    All_customers!!)
            Product_Variant!!.setThreshold(1)
            Product_Variant!!.setAdapter(adapter)

            // dialog1!!.dismiss()
        }

        btn_mapview!!.setOnClickListener {
         //   Global_Data.quickorder_maponback="yes"
            val i = Intent(this@QuickOrder, MapsActivity::class.java)
            startActivity(i)
        }


//        val contactnew = dbvoc.getCustomername(customer_id)
//        for (cn in contactnew) {
//            Global_Data.customer_MobileNumber = cn.mobilE_NO
//            Global_Data.CUSTOMER_NAME_NEW = cn.customeR_NAME
//            Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress()
//            Global_Data.order_retailer = cn.customeR_SHOPNAME
//
//        }


        barcode_scanner.setOnClickListener {

            Log.d("CUSTOMER", "CUSTOMER " + Product_Variant!!.getText().toString().trim())
            val contacts = dbvoc.getCustomerCode(Product_Variant!!.getText().toString().trim())
            if (contacts.size <= 0) {
//                val toast = Toast.makeText(applicationContext,
//                        resources.getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT)
//                toast.setGravity(Gravity.CENTER, 0, 0)
////                toast.show()
                Global_Data.Custom_Toast(applicationContext,
                        resources.getString(R.string.Customer_Not_Found),"yyes")
            } else {
                //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                val intent = Intent(this@QuickOrder, ScanActivity::class.java)
                intent.putExtra("customer_name", Product_Variant!!.getText().toString().trim())
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                Global_Data.customernaaaame=Product_Variant!!.getText().toString().trim()
                startActivity(intent)

            }

        }

        Product_Variant!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= Product_Variant!!.getRight() - Product_Variant!!.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {

                    val view = this@QuickOrder.getCurrentFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                    }
                    //autoCompleteTextView1.setText("");
                    Product_Variant!!.showDropDown()
                    return@OnTouchListener true
                }
            }
            false
        })



        btn_mapview!!.setOnClickListener {

            val i = Intent(this@QuickOrder, MapsActivity::class.java)
            startActivity(i)
        }

        list_ok!!.setOnClickListener {
            var data = ""
            catalogue_m = ArrayList()
            catalogue_m!!.clear()
            Global_Data.catalogue_m.clear()
            Global_Data.array_of_pVarient.clear()
            // catalogue_m!!.clear();
            var service_call_flag = "";
            val contacts = dbvoc.getCustomerCode(Product_Variant!!.text.toString())
            for (i in Global_Data.spiner_list_modelList.indices) {
                val singleStudent = Global_Data.spiner_list_modelList[i]
                if (singleStudent.isSelected() == true) {
                    data = singleStudent.product_variant
                    Log.d("Values", "Values" + data + " " + singleStudent.isSelected())
                    try {
                        data = URLEncoder.encode(data, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }

                    if (!Global_Data.Qorder_item_list.contains(data)) {
                        service_call_flag = "yes"
                        Global_Data.array_of_pVarient.add(data)
                    }

                }
                Log.d("Values", "Values" + data + " " + singleStudent.isSelected())
            }

            if (contacts.size <= 0)
                run {
//                    val toast = Toast.makeText(this@QuickOrder,
//                            resources.getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT)
//                    toast.setGravity(Gravity.CENTER, 0, 0)
//                    toast.show()
                    Global_Data.Custom_Toast(this@QuickOrder,
                            resources.getString(R.string.Customer_Not_Found),"yes")
                }
            else if (Global_Data.array_of_pVarient.size > 0) {
                //Global_Data.Search_business_unit_name = "";
                Global_Data.Search_Category_name = ""
                //  Toast.makeText(applicationContext, "done", Toast.LENGTH_SHORT).show()


                btn!!.visibility = View.VISIBLE
                content!!.visibility = View.VISIBLE
                search_lout!!.visibility = View.GONE


                if (service_call_flag.equals("yes", ignoreCase = false)) {
                    onnlinedata()
                }


                //Global_Data.Search_BusinessCategory_name = "";
//                val i = Intent(applicationContext, OnlineCatalogue::class.java)
//                startActivity(i)
            } else {
               // Toast.makeText(applicationContext, "Please select product variant.", Toast.LENGTH_SHORT).show()
                Global_Data.Custom_Toast(applicationContext, "Please select product variant.","")
            }
        }


//        Requiredby.setOnClickListener(View.OnClickListener {
//            // TODO Auto-generated method stub
//            DatePickerDialog(this@QuickOrder, date, myCalendar
//                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
//        })


        search_spinner_recycleview!!.setLayoutManager(LinearLayoutManager(this@QuickOrder))
        AutoSearchAdapter = AutoCompleteAdapter(this,  R.layout.autocomplete
        )
        Product_Variant_product!!.setAdapter(AutoSearchAdapter)


        search_spinner_recycleview!!.setLayoutManager(LinearLayoutManager(this@QuickOrder))
        spinner_list_adapter = SpinnerListAdapter(this@QuickOrder, snlist)
        search_spinner_recycleview!!.setAdapter(spinner_list_adapter)

//        checkout_ll!!.setOnClickListener {
//            val i = Intent(this@QuickOrder, Checkout_quick_order::class.java)
//            startActivity(i)
//        }

        Product_Variant_product!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= Product_Variant_product!!.getRight() - Product_Variant_product!!.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {

                    val view = this@QuickOrder.getCurrentFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                    }
                    if (!Product_Variant_product!!.getText().toString().equals("", ignoreCase = true)) {
                        Product_Variant_product!!.setText("")
                        search_spinner_recycleview!!.setVisibility(View.GONE)
                        list_ok!!.setVisibility(View.GONE)
                        list_cancel!!.setVisibility(View.GONE)
                        //     show_list_fromsearch.setVisibility(View.GONE)
                        //spnBusinessDiv.setVisibility(View.VISIBLE);
//                        spnCategory.setVisibility(View.VISIBLE)
//                        spnProduct.setVisibility(View.VISIBLE)
                        //spnBu.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                        content!!.visibility = View.VISIBLE

                    } else {
                        //searchLout.setVisibility(View.VISIBLE);
                        search_spinner_recycleview!!.setVisibility(View.VISIBLE)
                        list_ok!!.setVisibility(View.VISIBLE)
                        list_cancel!!.setVisibility(View.VISIBLE)
                        btn.setVisibility(View.GONE);
                        //  show_list_fromsearch.setVisibility(View.VISIBLE)
                        //spnBusinessDiv.setVisibility(View.GONE);
//                        spnCategory.setVisibility(View.GONE)
//                        spnProduct.setVisibility(View.GONE)
                        //spnBu.setVisibility(View.GONE);
                    }
                    searchLout!!.setVisibility(View.VISIBLE)
                    //autoCompleteTextView1.setText("");
                    // Product_Variant.showDropDown();
                    return@OnTouchListener true
                }
            }
            false
        })

        Product_Variant_product!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (Product_Variant_product!!.getText().toString().trim({ it <= ' ' }).length == 0) {
                    Product_Variant_product!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0)
                    search_spinner_recycleview!!.setVisibility(View.GONE)
                    list_ok!!.setVisibility(View.GONE)
                    list_cancel!!.setVisibility(View.GONE)
                    //show_list_fromsearch.setVisibility(View.GONE)
                    //searchLout.setVisibility(View.GONE);
                    //spnBusinessDiv.setVisibility(View.VISIBLE);
//                    spnCategory.setVisibility(View.VISIBLE)
//                    spnProduct.setVisibility(View.VISIBLE)
                    //spnBu.setVisibility(View.VISIBLE);
                } else {
                    searchLout!!.setVisibility(View.VISIBLE)

                    Product_Variant_product!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_product, 0)
                }

            }
        })



        list_cancel!!.setOnClickListener(View.OnClickListener {
            //            val i = Intent(applicationContext, QuickOrder::class.java)
//            startActivity(i)
            //u 9029794271
            //Simple@123
//            finish()
            finish();
            startActivity(getIntent());
        })


        online_catalog_save!!.setOnClickListener(View.OnClickListener { arg0 ->
            val buttonText = (arg0 as Button).text.toString()

            if (buttonText.equals("Add More", ignoreCase = true)) {
                p_id.clear()
                p_url.clear()
                p_name.clear()
                scheme_id.clear()
                p_mrp.clear()
                p_q.clear()
                p_price.clear()
                p_rp.clear()
                Global_Data.Order_hashmap.clear()
                Global_Data.Orderproduct_remarks.clear()
                p_remarks.clear()
                p_detail1.clear()
                p_detail2.clear()
                p_detail3.clear()
                p_detail4.clear()
                p_detail5.clear()
                Global_Data.Orderproduct_detail1.clear()
                Global_Data.Orderproduct_detail2.clear()
                Global_Data.Orderproduct_detail3.clear()
                Global_Data.Orderproduct_detail4.clear()
                Global_Data.Orderproduct_detail5.clear()
                Global_Data.GLOVEL_LONG_DESC = ""
                Global_Data.GLOVEL_CATEGORY_SELECTION = ""
                Global_Data.GLOVEL_ITEM_MRP = ""
                // Global_Data.Search_business_unit_name = "";
                Global_Data.Search_Category_name = ""

                val i = Intent(this@QuickOrder, MainActivity::class.java)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                finish()

            } else {
                p_id.clear()
                p_url.clear()
                p_name.clear()
                scheme_id.clear()
                p_mrp.clear()
                p_q.clear()
                p_price.clear()
                p_rp.clear()
                q_check = ""

                p_remarks.clear()
                p_detail1.clear()
                p_detail2.clear()
                p_detail3.clear()
                p_detail4.clear()
                p_detail5.clear()

                val contacts = dbvoc.getCustomerCode(Product_Variant!!.getText().toString().trim())

                for (cn in contacts) {
                    // All_customers!!.add(cn.customeR_SHOPNAME.trim())
                    var customer_id = cn.cust_Code.trim()
                    Global_Data.GLOvel_CUSTOMER_ID = customer_id

                }
                //                    dialog = new ProgressDialog(OnlineCatalogue.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                //                    dialog.setMessage("Please wait Customer Loading....");
                //                    dialog.setTitle("Metal App");
                //                    dialog.setCancelable(false);
                //                    dialog.show();

                Global_Data.sales_btnstring = "Secondary Sales / Retail Sales"
                Varientsave().execute()

            }
        })

        checkout_ll!!.setOnClickListener {

            if (Global_Data.sales_btnstring == "Institutional Sales") {
                if (!Global_Data.GLOvel_GORDER_ID.equals("", ignoreCase = true)) {
                    Global_Data.GLOVEL_LONG_DESC = ""
                    Global_Data.GLOVEL_CATEGORY_SELECTION = ""
                    Global_Data.GLOVEL_ITEM_MRP = ""
                    Global_Data.Search_Category_name = ""
                    Global_Data.Order_hashmap.clear()
                    Global_Data.Search_Product_name = ""

                    Global_Data.Orderproduct_remarks.clear()
                    p_remarks.clear()
                    p_detail1.clear()
                    p_detail2.clear()
                    p_detail3.clear()
                    p_detail4.clear()
                    p_detail5.clear()
                    Global_Data.Orderproduct_detail1.clear()
                    Global_Data.Orderproduct_detail2.clear()
                    Global_Data.Orderproduct_detail3.clear()
                    Global_Data.Orderproduct_detail4.clear()
                    Global_Data.Orderproduct_detail5.clear()

                    val i = Intent(this@QuickOrder, ActivityQuote::class.java)
                    //Intent i = new Intent(OnlineCatalogue.this, ActivityQuote.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    startActivity(i)
                    finish()
                    //   }
                    //NewOrderFragment.this.startActivity(i);
                } else {
//                    val toast = Toast.makeText(applicationContext, resources.getString(R.string.Preview_order_validation_message), Toast.LENGTH_SHORT)
//                    toast.setGravity(Gravity.CENTER, 0, 0)
//                    toast.show()
                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Add_the_order_before_checkout),"yes")
                }

            } else {
                if (!Global_Data.GLOvel_GORDER_ID.equals("", ignoreCase = true)) {
                    Global_Data.GLOVEL_LONG_DESC = ""
                    Global_Data.GLOVEL_CATEGORY_SELECTION = ""
                    Global_Data.GLOVEL_ITEM_MRP = ""
                    Global_Data.Search_Category_name = ""
                    Global_Data.Order_hashmap.clear()
                    Global_Data.Search_Product_name = ""

                    Global_Data.Orderproduct_remarks.clear()
                    p_remarks.clear()
                    p_detail1.clear()
                    p_detail2.clear()
                    p_detail3.clear()
                    p_detail4.clear()
                    p_detail5.clear()
                    Global_Data.Orderproduct_detail1.clear()
                    Global_Data.Orderproduct_detail2.clear()
                    Global_Data.Orderproduct_detail3.clear()
                    Global_Data.Orderproduct_detail4.clear()
                    Global_Data.Orderproduct_detail5.clear()


                    val contacts = dbvoc.getCustomerCode(Product_Variant!!.getText().toString().trim())

                    for (cn in contacts) {
                        // All_customers!!.add(cn.customeR_SHOPNAME.trim())
                        var customer_id = cn.cust_Code.trim()

                        var s_name = cn.getCUSTOMER_SHOPNAME()
                        var s_address = cn.getAddress()
                        var c_mobile_number = cn.getMOBILE_NO()
                        var customer_landline = cn.landlinE_NO

                        val sp: SharedPreferences = this@QuickOrder.getApplicationContext().getSharedPreferences("SimpleLogic", 0)
                        val spreedit: SharedPreferences.Editor = sp.edit()
                        spreedit.putString("shopname", s_name)
                        spreedit.putString("c_address", s_address)
                        spreedit.putString("shopcode", customer_id)
                        spreedit.putString("c_mobile_number", c_mobile_number)
                        spreedit.putString("customer_landline", customer_landline)
//                        spreedit.putString("c_credit_profile",item.credit_profile.toString())
//                        spreedit.putString("c_outstanding",item.outstanding.toString())
//                        spreedit.putString("c_overdue",item.overdue.toString())
//                        spreedit.putString("c_address",item.address.toString())
                        spreedit.commit()


                    }

                    val i = Intent(this@QuickOrder, Checkout_quick_order::class.java)
                    //Intent i = new Intent(OnlineCatalogue.this, ActivityQuote.class);
                    Global_Data.quickorderback = "yes"

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    startActivity(i)
                    finish()
                    //   }
                    //NewOrderFragment.this.startActivity(i);
                } else {
//                    val toast = Toast.makeText(applicationContext, resources.getString(R.string.Preview_order_validation_message), Toast.LENGTH_SHORT)
//                    toast.setGravity(Gravity.CENTER, 0, 0)
//                    toast.show()
                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Add_the_order_before_checkout),"yes")
                }
//            }
//            val i = Intent(this@QuickOrder, Checkout_quick_order::class.java)
//          }
//         startActivity(i)
            }
        }


    }


    private inner class Varientsave : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg response: String): String {

            try {

                this@QuickOrder.runOnUiThread(Runnable {
                    val parentView: View? = null

                    if (!Global_Data.Order_hashmap.isEmpty()) {

                        try {
                            for (name in Global_Data.Order_hashmap.keys) {

                                val key = name.toString()
                                val value = Global_Data.Order_hashmap[name]
                                val value_remarks = Global_Data.Orderproduct_remarks[name]
                                val value_detail1 = Global_Data.Orderproduct_detail1[name]
                                val value_detail2 = Global_Data.Orderproduct_detail2[name]
                                val value_detail3 = Global_Data.Orderproduct_detail3[name]
                                val value_detail4 = Global_Data.Orderproduct_detail4[name]
                                val value_detail5 = Global_Data.Orderproduct_detail5[name]
                                //System.out.println(key + " " + value);
                                Log.d("KEY", "Key: $key Value: $value")
                                val item = JSONObject()

                                val key_array = key.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value.toString())) {
                                    val key_value_array = value.toString().split("pq".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    val key_value_price_array = key_value_array[1].split("pprice".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    val key_value_pname_array = key_value_price_array[1].split("pmrp".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    val key_value_pmrp_array = key_value_pname_array[1].split("prp".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    val key_value_schemeid_array = key_value_pmrp_array[1].split("sid".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    val key_value_url_array = key_value_schemeid_array[1].split("url".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_price_array[0])) {
                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value_remarks.toString())) {
                                            p_remarks.add(value_remarks.toString())
                                        } else {
                                            p_remarks.add("")
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value_detail1.toString())) {
                                            p_detail1.add(value_detail1.toString())
                                        } else {
                                            p_detail1.add("")
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value_detail2.toString())) {
                                            p_detail2.add(value_detail2.toString())
                                        } else {
                                            p_detail2.add("")
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value_detail3.toString())) {
                                            p_detail3.add(value_detail3.toString())
                                        } else {
                                            p_detail3.add("")
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value_detail4.toString())) {
                                            p_detail4.add(value_detail4.toString())
                                        } else {
                                            p_detail4.add("")
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(value_detail5.toString())) {
                                            p_detail5.add(value_detail5.toString())
                                        } else {
                                            p_detail5.add("")
                                        }

                                        q_check = "yes"
                                        p_id.add(key_array[1])
                                        p_q.add(key_value_array[0])
                                        p_price.add(key_value_price_array[0])
                                        p_name.add(key_value_pname_array[0])
                                        scheme_id.add(key_value_url_array[0])
                                        p_mrp.add(key_value_pmrp_array[0])
                                        p_rp.add(key_value_schemeid_array[0])
                                        p_url.add(key_value_url_array[1])

                                        Log.d("quantity", "quantity" + key_value_array[0])
                                    }
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                    }
                })

            } catch (ex: Exception) {
                ex.printStackTrace()
                // dialog.dismiss();
            }

            return "Executed"
        }

        override fun onPostExecute(result: String) {

            val spf = this@QuickOrder.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();

            if (!p_id.isEmpty() && q_check.equals("yes", ignoreCase = true)) {

                val randomPIN = System.currentTimeMillis()
                val PINString = randomPIN.toString()
                if (Global_Data.GLOvel_GORDER_ID.equals("", ignoreCase = true)) {
                    if (Global_Data.sales_btnstring.equals("Secondary Sales / Retail Sales", ignoreCase = true)) {
                        Global_Data.GLObalOrder_id = "Ord$PINString"
                        Global_Data.GLOvel_GORDER_ID = "Ord$PINString"
                    } else {
                        Global_Data.GLObalOrder_id = "QNO$PINString"
                        Global_Data.GLOvel_GORDER_ID = "QNO$PINString"
                    }

                    try {
                        val appLocationManager = AppLocationManager(this@QuickOrder)
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.latitude + " " + appLocationManager.longitude)
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE)
                        val PlayServiceManager = PlayService_Location(this@QuickOrder)

                        if (PlayServiceManager.checkPlayServices(this@QuickOrder)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE)

                        } else if (!appLocationManager.latitude.toString().equals("null", ignoreCase = true) && !appLocationManager.latitude.toString().equals(null!!, ignoreCase = true) && !appLocationManager.longitude.toString().equals(null!!, ignoreCase = true) && !appLocationManager.longitude.toString().equals(null!!, ignoreCase = true)) {
                            Global_Data.GLOvel_LATITUDE = appLocationManager.latitude.toString()
                            Global_Data.GLOvel_LONGITUDE = appLocationManager.longitude.toString()
                        }

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                    val checkq = dbvoc.checkOrderExist(Global_Data.GLOvel_CUSTOMER_ID, Global_Data.GLObalOrder_id)

                    if (checkq.size <= 0) {
                        val c = Calendar.getInstance()
                        println("Current time =&gt; " + c.time)

                        val df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        val formattedDaten = df.format(c.time)

                        val spf1 = getSharedPreferences("SimpleLogic", 0)
                        val shopname = spf1.getString("shopname", "")


                        try {

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {
                                val locationAddress = LocationAddress()
                                LocationAddress.getAddressFromLocation(java.lang.Double.valueOf(Global_Data.GLOvel_LATITUDE), java.lang.Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                        applicationContext, Global_Data.GeocoderHandler())
                            }

                        } catch (ex: Exception) {
                            ex.printStackTrace()

                        }

                        loginDataBaseAdapter!!.insertOrders("", Global_Data.GLOvel_GORDER_ID, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, Global_Data.address, "", "", "", "", "", formattedDaten, "", shopname, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, "", "", "", "", "", "", "")
                    }
                }


                var pp: Double? = 0.0
                try {
                    for (k in p_id.indices) {

                        val contactsn = dbvoc.GetOrders_BY_ORDER_ID(Global_Data.GLObalOrder_id, p_id.get(k))

                        if (contactsn.size > 0) {

                            for (cn in contactsn) {

                                //                                if(Global_Data.Varient_value_add_flag.equalsIgnoreCase("yes"))
                                //                                {

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                    dbvoc.update_itemamountandquantity_withremarks(p_q.get(k).toString(), p_price.get(k).toString(), p_id.get(k), Global_Data.GLObalOrder_id, p_remarks.get(k), p_detail1.get(k), p_url.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k))
                                } else if (!p_q.get(k).equals(cn._delivery_product_order_quantity, ignoreCase = true) && !Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                    dbvoc.getDeleteTableorderproduct_byITEM_NUMBER(p_id.get(k), Global_Data.GLObalOrder_id)
                                }

                                //                                }
                                //                                else
                                //                                {
                                //                                    int quantity = Integer.parseInt(cn.get_delivery_product_order_quantity()) + Integer.parseInt(p_q.get(k));
                                //                                    Double amount = Double.valueOf(cn.getAmount()) + Double.valueOf(p_price.get(k));
                                //                                    dbvoc.update_itemamountandquantity(String.valueOf(quantity), String.valueOf(amount), p_id.get(k), Global_Data.GLObalOrder_id);
                                //                                }

                            }
                        } else {

                            val schemeid = scheme_id.get(k)

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                loginDataBaseAdapter!!.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", Global_Data.Search_Category_name, Global_Data.Search_Product_name, p_name.get(k), " ", schemeid, " ", "", p_q.get(k), p_rp.get(k), p_mrp.get(k), p_price.get(k), "", "", Global_Data.order_retailer, " ", p_id.get(k), " ", p_name.get(k), p_remarks.get(k), p_detail1.get(k), p_url.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k))//Reading all

                                // Log.d("pPRIZE","Pprize"+ p_price.get(k));
                            }


                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                            pp = pp?.plus(java.lang.Double.valueOf(p_price.get(k)))
                            //Global_Data.GrandTotal+= Double.valueOf(p_price.get(k));
                        }

                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                online_catalog_save!!.setEnabled(true)
                online_catalog_save!!.setText(resources.getString(R.string.Save))

                if (ttl_price!!.length > 0) {
                    Global_Data.GrandTotal = pp
                    txttotalPreview!!.setText("$ttl_price:$pp")
                } else {
                    Global_Data.GrandTotal = pp
                    txttotalPreview!!.setText(resources.getString(R.string.CTotal) + "₹"+pp!!)
                }

                Global_Data.Varient_value_add_flag = "yes"

                val checkq = dbvoc.getItemName(Global_Data.GLObalOrder_id)

                if (checkq.size <= 0) {
                    q_check = ""
                    Global_Data.Order_hashmap.clear()
                    p_id.clear()
                    p_url.clear()
                    p_q.clear()
                    p_price.clear()
                    p_name.clear()
                    scheme_id.clear()
                    p_mrp.clear()
                    p_rp.clear()
                    Global_Data.Orderproduct_remarks.clear()
                    p_remarks.clear()
                    p_detail1.clear()
                    p_detail2.clear()
                    p_detail3.clear()
                    p_detail4.clear()
                    p_detail5.clear()
                    // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
                    Global_Data.Orderproduct_detail1.clear()
                    Global_Data.Orderproduct_detail2.clear()
                    Global_Data.Orderproduct_detail3.clear()
                    Global_Data.Orderproduct_detail4.clear()
                    Global_Data.Orderproduct_detail5.clear()
                    this@QuickOrder.runOnUiThread(Runnable {
//                        val toast = Toast.makeText(this@QuickOrder, resources.getString(R.string.All_item_delete), Toast.LENGTH_SHORT)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder, resources.getString(R.string.All_item_delete),"yes")
                        // Product_Variant.setText("");

                        val i = Intent(this@QuickOrder, NewOrderActivity::class.java)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)
                        finish()
                    })

                } else {
                    pp = 0.0
                    for (qtr in checkq) {
                        pp += java.lang.Double.valueOf(qtr.amount)
                        //Global_Data.GrandTotal += Double.valueOf(qtr.getAmount());
                    }

                    if (ttl_price!!.length > 0) {
                        Global_Data.GrandTotal = pp
                        txttotalPreview!!.setText("$ttl_price:$pp")
                    } else {
                        Global_Data.GrandTotal = pp
                        txttotalPreview!!.setText(resources.getString(R.string.CTotal) + "₹"+pp)
                    }

                    q_check = ""
                    Global_Data.Order_hashmap.clear()

                    //                    if(!Product_Variant.getText().toString().equalsIgnoreCase(""))
                    //                    {
                    //                        Product_Variant.setText("");
                    //                    }

                    p_id.clear()
                    p_url.clear()
                    p_q.clear()
                    p_price.clear()
                    p_name.clear()
                    scheme_id.clear()
                    p_mrp.clear()
                    p_rp.clear()
                    Global_Data.Orderproduct_remarks.clear()
                    p_remarks.clear()
                    p_detail1.clear()
                    p_detail2.clear()
                    p_detail3.clear()
                    p_detail4.clear()
                    p_detail5.clear()
                  //  val toast = Toast.makeText(this@QuickOrder, resources.getString(R.string.Item_added_successfully), Toast.LENGTH_LONG)
                    Global_Data.Custom_Toast(this@QuickOrder, resources.getString(R.string.Item_added_successfully),"yes")
//                    toast.setGravity(Gravity.CENTER, 0, 0)
//                    toast.show()
                    Global_Data.Orderproduct_detail1.clear()
                    Global_Data.Orderproduct_detail2.clear()
                    Global_Data.Orderproduct_detail3.clear()
                    Global_Data.Orderproduct_detail4.clear()
                    Global_Data.Orderproduct_detail5.clear()
                    //                    catalogue_m = new ArrayList<>();
                    //                    mAdapter = new CatalogueAdapter(getApplicationContext(), catalogue_m, OnlineCatalogue.this);
                    //
                    //                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    //                    recyclerView.setLayoutManager(mLayoutManager);
                    //                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //                    recyclerView.setAdapter(mAdapter);
                    //                    Online_Catalogue();

                    val a = catalogue_m!!.size

                    for (i in 0 until a) {
                        val catalogue_mm = catalogue_m!!.get(i)
                        val contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, catalogue_mm.item_number)
                        if (contactsn.size > 0) {
                            for (cn in contactsn) {
                                catalogue_mm.item_quantity = cn._delivery_product_order_quantity
                                catalogue_mm.item_amount = resources.getString(R.string.PRICE) + cn.amount

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.remarks)) {
                                    catalogue_mm.item_remarks = cn.remarks
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail1)) {
                                    catalogue_mm.detail1 = cn.detail1
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail2)) {
                                    catalogue_mm.detail2 = cn.detail2
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail3)) {
                                    catalogue_mm.detail3 = cn.detail3
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail4)) {
                                    catalogue_mm.detail4 = cn.detail4
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail5)) {
                                    catalogue_mm.detail5 = cn.detail5
                                }

                            }
                        } else {
                            catalogue_mm.item_quantity = ""
                            catalogue_mm.item_amount = ""
                            catalogue_mm.item_remarks = ""
                            catalogue_mm.detail1 = ""
                            catalogue_mm.detail2 = ""
                            catalogue_mm.detail3 = ""
                            catalogue_mm.detail4 = ""
                            catalogue_mm.detail5 = ""
                        }

                        catalogue_m!!.set(i, catalogue_mm)
                    }

                    Global_Data.catalogue_m = catalogue_m
                    //                    mAdapter.notifyDataSetChanged();
                }
            } else {
                Global_Data.Orderproduct_detail1.clear()
                Global_Data.Orderproduct_detail2.clear()
                Global_Data.Orderproduct_detail3.clear()
                Global_Data.Orderproduct_detail4.clear()
                Global_Data.Orderproduct_detail5.clear()
                q_check = ""
                Global_Data.Order_hashmap.clear()
                p_id.clear()
                p_url.clear()
                p_q.clear()
                p_price.clear()
                p_name.clear()
                scheme_id.clear()
                p_mrp.clear()
                p_rp.clear()
                Global_Data.Orderproduct_remarks.clear()
                p_remarks.clear()
                p_detail1.clear()
                p_detail2.clear()
                p_detail3.clear()
                p_detail4.clear()
                p_detail5.clear()
                online_catalog_save!!.setEnabled(true)
                online_catalog_save!!.setText(resources.getString(R.string.Save))
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.Please_enter_quantity), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.please_enter_Customer), "yes")
            }


            this@QuickOrder.runOnUiThread(Runnable {
                //dialog.dismiss();
            })

        }

        override fun onPreExecute() {


            this@QuickOrder.runOnUiThread(Runnable {
                //          dialog.setMessage("Please wait....");
                //          dialog.setTitle("Siyaram App");
                //          dialog.setCancelable(false);
                //          dialog.show();

                online_catalog_save!!.setEnabled(false)
                online_catalog_save!!.setText(resources.getString(R.string.Please_Wait))
                //int pic = R.drawable.round_btngray;
                // buttonPreviewAddMOre.setBackgroundResource(pic);
            })
        }

        override fun onProgressUpdate(vararg values: Void) {}
    }


    private fun onnlinedata() {
//        if (Global_Data.catalogue_m.size > 0) {
//            catalogue_m = ArrayList()
//            catalogue_m = Global_Data.catalogue_m as ArrayList<Catalogue_model>
//            mAdapter = Quick_order_adaptor(applicationContext, Global_Data.catalogue_m, this@QuickOrder)
//
//            val mLayoutManager = LinearLayoutManager(applicationContext)
//            rv!!.setLayoutManager(mLayoutManager)
//            rv!!.setItemAnimator(DefaultItemAnimator())
//            rv!!.setAdapter(mAdapter)
//
//        } else {

        Online_Catalogue("array", "")
        //   }

    }

    private fun Online_Catalogue(service__flag: String, barcode_value: String) {
        var domain = ""
        var domain1: String? = ""

        //dialog = new ProgressDialog(Video_Main_List.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog!!.setMessage(resources.getString(R.string.Please_Wait))
        pDialog!!.setTitle(resources.getString(R.string.app_name))
        pDialog!!.setCancelable(false)
        pDialog!!.show()

        var service_url = ""
        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val Cust_domain = sp.getString("Cust_Service_Url", "")
        val service_url1 = Cust_domain!! + "metal/api/v1/"
        val device_id = sp.getString("devid", "")
        domain = service_url1

        domain1 = Cust_domain

        val spf = this@QuickOrder.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)

        Log.d("email_id", "email_id" + user_email!!)


        val ss = StringBuilder()
        var mStringArray = arrayOfNulls<String>(Global_Data.array_of_pVarient.size)
        mStringArray = Global_Data.array_of_pVarient.toTypedArray<String?>()
        for (i in Global_Data.array_of_pVarient.indices) {

            ss.append(Global_Data.array_of_pVarient[i])
            if (Global_Data.array_of_pVarient.size - 1 != i) {
                ss.append(",")
            }
        }

        Global_Data.ProductVariant = ss.toString()
        try {
            if (service__flag.equals("array", ignoreCase = false)) {
                service_url = domain + "products/get_product_catalogues?email=" + URLEncoder.encode(user_email, "UTF-8") + "&primary_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_variant=" + Global_Data.ProductVariant

            } else {
                service_url = domain + "products/get_product_catalogues?email=" + URLEncoder.encode(user_email, "UTF-8") + "&barcode_number=" + barcode_value;

            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }


        Log.d("Server url", "Server url$service_url")

        var stringRequest: StringRequest? = null
        val finalDomain = domain1
        stringRequest = object : StringRequest(service_url,
                Response.Listener { response ->
                    //showJSON(response);
                    // Log.d("jV", "JV" + response);
                    Log.d("jV", "JV length" + response.length)
                    // JSONObject person = (JSONObject) (response);
                    try {
                        val json = JSONObject(JSONTokener(response))
                        try {
                            var response_result = ""
                            if (json.has("message")) {
                                response_result = json.getString("message")

//                                val toast = Toast.makeText(applicationContext, response_result, Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
Global_Data.Custom_Toast(applicationContext, response_result,"yes")
                                val launch = Intent(this@QuickOrder, NewOrderActivity::class.java)
                                startActivity(launch)
                                finish()
                            } else {

                                val products = json.getJSONArray("products")
                                Global_Data.products = json.getJSONArray("products")
                                //
                                Log.i("volley", "response reg products Length: " + products.length())
                                //
                                Log.d("users", "products$products")
                                //
                                catalogue_m!!.clear()
                                //Global_Data.imageUrlArr1.clear();
                                pp = 0.0

                                if (products.length() > 0) {
                                    for (i in 0 until products.length()) {
                                        val `object` = products.getJSONObject(i)
                                        val catalogue_model = Catalogue_model()
                                        val swipeImageModel = SwipeImage()

                                        catalogue_model.item_name = `object`.getString("name")
                                        catalogue_model.item_number = `object`.getString("code")
                                        catalogue_model.item_rp = `object`.getString("retail_price")
                                        catalogue_model.item_mrp = `object`.getString("mrp")
                                        catalogue_model.item_min_qty = `object`.getString("min_order_qty")
                                        catalogue_model.item_max_qty = `object`.getString("max_order_qty")
                                        catalogue_model.item_pkg_qty1 = `object`.getString("pkg_qty1")
                                        catalogue_model.item_pkg_qty2 = `object`.getString("pkg_qty2")

                                        catalogue_model.scheme_id = `object`.getString("scheme_id")
                                        catalogue_model.scheme_name = `object`.getString("scheme_name")
                                        catalogue_model.scheme_type = `object`.getString("scheme_type")
                                        catalogue_model.scheme_amount = `object`.getString("scheme_amount")
                                        catalogue_model.scheme_description = `object`.getString("scheme_description")
                                        catalogue_model.scheme_buy_qty = `object`.getString("scheme_buy_qty")
                                        catalogue_model.scheme_get_qty = `object`.getString("scheme_get_qty")
                                        catalogue_model.scheme_topsellingproduct = `object`.getString("is_top_selling_product")
                                        catalogue_model.scheme_min_qty = `object`.getString("scheme_min_qty")
                                        catalogue_model.item_description = `object`.getString("description")

                                        val product_catalogues = `object`.getJSONArray("product_catalogues")

                                        //catalogue_model.setImageUrlArr11((List<String>) product_catalogues);
                                        // Global_Data.products = object.getJSONArray("product_catalogues");

                                        val contactsn1 = dbvoc.GetSchemeByCode(`object`.getString("code"))
                                        if (contactsn1.size > 0) {
                                            for (cn in contactsn1) {
                                                catalogue_model.item_scheme = cn.displayName
                                                // list3.add("SCHEME : " + cn.getDisplayName());
                                            }
                                        }

                                        for (j in 0 until product_catalogues.length()) {
                                            val image_object = product_catalogues.getJSONObject(j)
                                            catalogue_model.item_image_url = finalDomain + image_object.getString("thumb_url")

                                            //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
                                            //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
                                        }

                                        // String[] tempArray = new String[product_catalogues.length()];
                                        //                                            for (int j = 0; j < product_catalogues.length(); j++) {
                                        //                                                JSONObject image_object = product_catalogues.getJSONObject(j);
                                        //                                                //swipeImageModel.setImage(finalDomain + image_object.getString("thumb_url"));
                                        //                                                swipeImageModel.setImage(finalDomain + image_object.getString("thumb_url"));
                                        //                                                Global_Data.imageUrlArr1.add((finalDomain + image_object.getString("thumb_url")));
                                        //                                                //tempArray[i] = myArray [i];
                                        //                                                // swipeImageModel.setImage("https://demonuts.com/Demonuts/SampleImages/W-03.JPG");
                                        //                                                //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
                                        //                                                //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
                                        //                                            }

                                        val contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, `object`.getString("code"))
                                        if (contactsn.size > 0) {
                                            for (cn in contactsn) {
                                                catalogue_model.item_quantity = cn._delivery_product_order_quantity
                                                catalogue_model.item_amount = resources.getString(R.string.PRICE) + cn.amount


                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.remarks)) {
                                                    catalogue_model.item_remarks = cn.remarks
                                                } else {
                                                    catalogue_model.item_remarks = ""
                                                }

                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail1)) {
                                                    catalogue_model.detail1 = cn.detail1
                                                }

                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail2)) {
                                                    catalogue_model.detail2 = cn.detail2
                                                }

                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail3)) {
                                                    catalogue_model.detail3 = cn.detail3
                                                }

                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail4)) {
                                                    catalogue_model.detail4 = cn.detail4
                                                }

                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.detail5)) {
                                                    catalogue_model.detail5 = cn.detail5
                                                }


                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.amount)) {
                                                    pp = pp?.plus(java.lang.Double.valueOf(cn.amount))
                                                    // Global_Data.GrandTotal+= Double.valueOf(cn.getAmount());
                                                }
                                            }
                                        } else {
                                            catalogue_model.item_quantity = ""
                                            catalogue_model.item_amount = ""
                                            catalogue_model.item_remarks = ""
                                            catalogue_model.detail1 = ""
                                            catalogue_model.detail2 = ""
                                            catalogue_model.detail3 = ""
                                            catalogue_model.detail4 = ""
                                            catalogue_model.detail5 = ""
                                        }

                                        //swipeimg_catalogue_m.add(swipeImageModel);
                                        catalogue_m!!.add(catalogue_model)


                                        if (service__flag.equals("barcode", ignoreCase = false)) {
                                            Global_Data.catalogue_m.add(0, catalogue_model);
                                            mAdapter!!.notifyItemInserted(0);
                                            rv!!.smoothScrollToPosition(0);

                                        } else {
                                            Global_Data.catalogue_m.add(catalogue_model)
                                        }

                                    }

                                    //Intent launch = new Intent(context,Youtube_Player_Activity.class);
                                    //startActivity(launch);

//                                    if(Global_Data.catalogue_m.size > 0)
//                                    {
//                                        catalogue_m!!.forEachIndexed { i, value ->
//
//
//                                            if (Global_Data.catalogue_m.size-1 == Global_Data.catalogue_m!!.get(i).item_number != value.item_number)
//                                            {
//                                                Global_Data.catalogue_m = catalogue_m
//                                            }
//                                        }
//                                    }
//                                    else
//                                    {
//                                        Global_Data.catalogue_m = catalogue_m
//                                    }

                                    // Global_Data.catalogue_m =  Global_Data.catalogue_m.distinctBy { it.item_number }


                                    //Global_Data.imageUrlArr = swipeimg_catalogue_m;

                                    if (!service__flag.equals("barcode", ignoreCase = false)) {
                                        mAdapter!!.notifyDataSetChanged()

                                        for (i in Global_Data.array_of_pVarient.indices) {
                                            Global_Data.Qorder_item_list.add(Global_Data.array_of_pVarient[i])

                                        }

                                    } else {
                                        Global_Data.Qorder_item_list.add(barcode_value)
                                    }



                                    Global_Data.ProductVariant = "";
                                    pDialog!!.hide()


                                    if (ttl_price?.length!! > 0) {
                                        Global_Data.GrandTotal = pp
                                        txttotalPreview?.setText(ttl_price + ":" + "₹"+pp)
                                    } else {
                                        Global_Data.GrandTotal = pp
                                        txttotalPreview?.setText(resources.getString(R.string.Total) + "₹"+pp)
                                    }

                                } else {
                                    pDialog!!.hide()
                                    // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

//                                    val toast = Toast.makeText(applicationContext, resources.getString(R.string.product_not_found_message), Toast.LENGTH_LONG)
//                                    toast.setGravity(Gravity.CENTER, 0, 0)
//                                    toast.show()

                                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.product_not_found_message),"yes")
//
                                }

                                //finish();

                            }

                            //  finish();
                            // }

                            // output.setText(data);
                        } catch (e: JSONException) {
                            e.printStackTrace()


//                            val toast = Toast.makeText(this@QuickOrder,
//                                    "Service Error",
//                                    Toast.LENGTH_LONG)
                            Global_Data.Custom_Toast(this@QuickOrder,
                                    "Service Error","")
//                            toast.setGravity(Gravity.CENTER, 0, 0)
//                            toast.show()
                            val launch = Intent(this@QuickOrder, MainActivity::class.java)
                            startActivity(launch)
                            finish()

                            pDialog!!.hide()
                        }

                        pDialog!!.hide()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        //  finish();
                        pDialog!!.dismiss()
                    }

                    pDialog!!.dismiss()
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    if (error is TimeoutError || error is NoConnectionError) {
                        //                            Toast.makeText(Image_Gellary.this,
                        //                                    "Network Error",
                        //                                    Toast.LENGTH_LONG).show();

//                        val toast = Toast.makeText(this@QuickOrder,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder,
                                "Network Error","")
                    } else if (error is AuthFailureError) {

//                        val toast = Toast.makeText(this@QuickOrder,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder,
                                "Server AuthFailureError  Error","")
                    } else if (error is ServerError) {

//                        val toast = Toast.makeText(this@QuickOrder,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder,
                                resources.getString(R.string.Server_Errors),"")
                    } else if (error is NetworkError) {

//                        val toast = Toast.makeText(this@QuickOrder,
//                                "Network   Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder,
                                "Network   Error","")
                    } else if (error is ParseError) {


//                        val toast = Toast.makeText(this@QuickOrder,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder,
                                "ParseError   Error","")
                    } else {
                        // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();

//                        val toast = Toast.makeText(this@QuickOrder, error.message, Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@QuickOrder, error.message,"yes")
                    }
                    val launch = Intent(this@QuickOrder, MainActivity::class.java)
                    startActivity(launch)
                    finish()
                    pDialog!!.dismiss()
                    // finish();
                }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(this@QuickOrder)

        val socketTimeout = 300000//30 seconds - change to what you want
        val policy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false)
        requestQueue.cache.clear()
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        Global_Data.spiner_list_modelList.clear()
        Global_Data.array_of_pVarient.clear()
        Global_Data.GLOVEL_LONG_DESC = ""
        Global_Data.GLOVEL_CATEGORY_SELECTION = ""
        Global_Data.GLOVEL_ITEM_MRP = ""
        Global_Data.Search_Category_name = ""
        Global_Data.spiner_list_modelList.clear()
        Global_Data.array_of_pVarient.clear()
        Global_Data.catalogue_m.clear()
        Global_Data.Qorder_item_list.clear()
        Global_Data.Search_Product_name = ""
        Global_Data.quickorder_maponback=""

        // Global_Data.productList.clear();

        if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equals("TRUE", ignoreCase = true)) {
            val i = Intent(this@QuickOrder, Checkout_quick_order::class.java)


            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            startActivity(i)
            finish()
        } else {
            if (!Global_Data.GLOvel_GORDER_ID.equals("", ignoreCase = true)) {

                val alertDialog = AlertDialog.Builder(this@QuickOrder).create() //Read Update
                alertDialog.setTitle(resources.getString(R.string.Warning))
                alertDialog.setMessage(resources.getString(R.string.Order_dialog_message))
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, resources.getString(R.string.No_Button_label)) { dialog, which ->
                    val i = Intent(this@QuickOrder, Checkout_quick_order::class.java)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    startActivity(i)
                    finish()
                }

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, resources.getString(R.string.Cancel_Order)) { dialog, id ->
                    dbvoc = DataBaseHelper(this@QuickOrder)
                    dbvoc.getDeleteTableorder_byOID(Global_Data.GLObalOrder_id)
                    dbvoc.getDeleteTableorderproduct_byOID(Global_Data.GLObalOrder_id)
                    Global_Data.GLOvel_GORDER_ID = ""
                    Global_Data.GLObalOrder_id = ""

                 //   Toast.makeText(this@QuickOrder, resources.getString(R.string.Order_Canceled_Successfully), Toast.LENGTH_SHORT).show()
                    Global_Data.Custom_Toast(this@QuickOrder, resources.getString(R.string.Order_Canceled_Successfully), "")
                    val order_home = Intent(applicationContext, MainActivity::class.java)
                    startActivity(order_home)
                    finish()
                }

                alertDialog.show()
            } else {

                if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equals("TRUE", ignoreCase = true)) {
                    if (!Global_Data.GLObalOrder_id.equals("", ignoreCase = true)) {
//                        val toast = Toast.makeText(applicationContext, resources.getString(R.string.Order_save_successfully), Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Order_save_successfully),"yes")
                    }
                    val i = Intent(this@QuickOrder, Checkout_quick_order::class.java)
                    startActivity(i)
                    finish()
                } else {
                    if (!Global_Data.GLObalOrder_id.equals("", ignoreCase = true)) {

//                        val toast = Toast.makeText(applicationContext, resources.getString(R.string.Order_save_successfully), Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                    }
                    //                    Intent i = new Intent(NewOrderActivity.this, Neworderoptions.class);
                    //                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    //                    startActivity(i);
//                    val order_home = Intent(applicationContext, MainActivity::class.java)
//                    startActivity(order_home)
//                    finish()
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@QuickOrder.getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f)).toInt()
                    val achieved = Math.round(sp.getFloat("Achived", 0f)).toInt()
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float!!).toInt()
                        if (Global_Data.rsstr.length > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew = "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        val age = Math.round(age_float!!).toInt()
                        if (Global_Data.rsstr.length > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                            // todaysTarget.setText();
                        } else {
                            targetNew = "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                val view: View = findViewById(R.id.add)
                // val yourView = findViewById(R.id.add)
                SimpleTooltip.Builder(this)
                        .anchorView(view)
                        .text(targetNew)
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        super.onResume()

        val values: String? = intent.getStringExtra("barcode")
        if (values != null) {

            var service_call_flag = "yes";

            for (value in Global_Data.Qorder_item_list) {
                if (value.equals(values, ignoreCase = false)) {
                    service_call_flag = "";
                    break
                }
            }

            if (service_call_flag.equals("yes", ignoreCase = false)) {
                Online_Catalogue("barcode", values)
            }
//            else{
//
//                Global_Data.Custom_Toast(this@QuickOrder, resources.getString(R.string.Order_Not_Found), "Yes")
//            }

        }
//        else{
//                         Global_Data.Custom_Toast(this@QuickOrder, "Shop name not scann", "Yes")
//
//        }

        val shop_name: String? = intent.getStringExtra("shopname")
        if (shop_name != null) {
            Product_Variant!!.setText(shop_name);
        }

        val spf1 = this.getSharedPreferences("SimpleLogic", 0)
        ttl_price = spf1.getString("var_total_price", "")

        if (ttl_price!!.length > 0) {
            txttotalPreview!!.setText(ttl_price + ":" + "₹"+Global_Data.GrandTotal)
        } else {
            txttotalPreview!!.setText(resources.getString(R.string.CTotal) + "₹"+Global_Data.GrandTotal)
        }
    }

}
