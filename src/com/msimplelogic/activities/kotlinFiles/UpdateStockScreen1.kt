package com.msimplelogic.activities.kotlinFiles

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.AutoCompleteAdapterStock
import com.msimplelogic.adapter.SpinnerListAdapter
import com.msimplelogic.model.SpinerListModel
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_update_stock_screen1.*
import kotlinx.android.synthetic.main.content_update_stock_screen1.*
import kotlinx.android.synthetic.main.content_update_stock_screen1.hedder_theame
import kotlinx.android.synthetic.main.content_visit__schedule.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class UpdateStockScreen1 : BaseActivity() {
    private val results = ArrayList<String>()
    private val results1 = ArrayList<String>()
    private val results2 = ArrayList<String>()
    private val result_product = ArrayList<String>()
    var adapter_state1: ArrayAdapter<String>? = null
    var adapter_state2: ArrayAdapter<String>? = null
    var adapter_state3: ArrayAdapter<String>? = null
    var dbvoc = DataBaseHelper(this)
    var categ_name: String? = null
    var subcateg_name: String? = null
    var check = 0
    var str_variant: String? = null
    var product_code = ""
    var check_product = 0
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var context: Context? = null
    var AutoSearchAdapter: AutoCompleteAdapterStock? = null
    var snlist: MutableList<SpinerListModel> = ArrayList()
    var spinner_list_adapter: SpinnerListAdapter? = null
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_stock_screen1)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        Global_Data.Order_hashmap.clear()

        cd = ConnectionDetector(this@UpdateStockScreen1)
        context = this@UpdateStockScreen1;
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        results2.clear()
        val contacts2 = dbvoc.allVariant
        for (cn in contacts2) {
            results2.add(cn.stateName)
            result_product.add(cn.stateName)
        }

        val adapter = ArrayAdapter(this,
                R.layout.autocomplete,
                results2)
        up_stock_Product_Variant.setThreshold(1) // will start working from
        up_stock_Product_Variant.setAdapter(adapter) // setting the adapter

        spinner_recycleview_stock.setLayoutManager(LinearLayoutManager(context))
        AutoSearchAdapter = AutoCompleteAdapterStock(this, R.layout.autocomplete)
        up_stock_Product_Variant.setAdapter(AutoSearchAdapter)

        val cont1 = dbvoc.allProducta
        if (cont1.size > 0) {
            Global_Data.spiner_list_modelList.clear()
            for (cnt1 in cont1) {
                val spiner_list_model = SpinerListModel()
                spiner_list_model.primary_category = ""
                spiner_list_model.product_variant = cnt1.product_variant
                spiner_list_model.sub_category = ""
                spiner_list_model.code = cnt1.code
                spiner_list_model.setSelected(false)
                snlist.add(spiner_list_model)
                Global_Data.spiner_list_modelList.add(spiner_list_model)
            }
        }

        spinner_recycleview_stock.setLayoutManager(LinearLayoutManager(context))
        spinner_list_adapter = SpinnerListAdapter(context, snlist)
        spinner_recycleview_stock.setAdapter(spinner_list_adapter)


        //Reading all
        val contacts12 = dbvoc.HSS_DescriptionITEM()
        results1.add(resources.getString(R.string.Select_Categories))
        for (cn in contacts12) {
            if (!cn.stateName.equals("", ignoreCase = true) && !cn.stateName.equals(" ", ignoreCase = true)) {
                val str_categ = "" + cn.stateName
                results1.add(str_categ)
            }
        }

        adapter_state1 = ArrayAdapter(this, R.layout.spinner_item, results1)
        adapter_state1!!.setDropDownViewResource(R.layout.spinner_item)
        up_stock_spnCategory.setAdapter(adapter_state1)

        results.add(resources.getString(R.string.Select_Product))
        adapter_state2 = ArrayAdapter(this, R.layout.spinner_item, results)
        adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
        up_stock_spnProduct.setAdapter(adapter_state2)


        list_ok_stock.setOnClickListener(View.OnClickListener {
            var data = ""
            Global_Data.array_of_pVarient.clear()
            Global_Data.ProductVariant = "";
            for (i in Global_Data.spiner_list_modelList.indices) {
                val singleStudent = Global_Data.spiner_list_modelList[i]
                if (singleStudent.isSelected() == true) {
                    data = singleStudent.code
                    Log.d("Values", "Values" + data + " " + singleStudent.isSelected())
                    try {
                        data = URLEncoder.encode(data, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                    Global_Data.array_of_pVarient.add(data)
                }
                Log.d("Values", "Values" + data + " " + singleStudent.isSelected())
            }
            if (Global_Data.array_of_pVarient.size > 0) { //Global_Data.Search_business_unit_name = "";
                Global_Data.Search_Category_name =""
                Global_Data.Search_Product_name = ""
                val i = Intent(applicationContext, UpdateStockScreen2::class.java)
                startActivity(i)
            } else {
                Global_Data.Custom_Toast(applicationContext, "Please select product variant.", "yes")
            }
        })

        list_cancel_stock.setOnClickListener(View.OnClickListener {
            list_container_stock.visibility = View.GONE
            //hidden_buttonlayout_stock.visibility = View.GONE
            up_stock_spnCategory.visibility = View.VISIBLE
            up_stock_spnProduct.visibility = View.VISIBLE
            up_stock_search.visibility = View.VISIBLE
        })


        up_stock_Product_Variant.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= up_stock_Product_Variant.getRight() - up_stock_Product_Variant.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()) {
                    val view: View = this@UpdateStockScreen1.getCurrentFocus()!!
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    if (!up_stock_Product_Variant.getText().toString().equals("", ignoreCase = true)) {
                        up_stock_Product_Variant.setText("")
                        list_container_stock.visibility = View.GONE
                        //hidden_buttonlayout_stock.visibility = View.GONE
                        up_stock_spnCategory.visibility = View.VISIBLE
                        up_stock_spnProduct.visibility = View.VISIBLE
                        up_stock_search.visibility = View.VISIBLE
                    } else {
                        list_container_stock.visibility = View.VISIBLE
                        //hidden_buttonlayout_stock.visibility = View.GONE
                        up_stock_spnCategory.visibility = View.GONE
                        up_stock_spnProduct.visibility = View.GONE
                        up_stock_search.visibility = View.GONE
                    }
                    //autoCompleteTextView1.setText("");
// Product_Variant.showDropDown();
                    return@OnTouchListener true
                }
            }
            false
        })


        up_stock_Product_Variant.setOnItemClickListener(OnItemClickListener { parent, arg1, pos, id ->
            //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();
            Global_Data.hideSoftKeyboard(this@UpdateStockScreen1)
            val customer_name = ""
            val address_type = ""
            //                if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0)
//                {
//                    s = autoCompleteTextView1.getText().toString().trim().split(":");
//                    customer_name = s[0].trim();
//                    address_type = s[1].trim();
//                }
//                else
//                {
//                    customer_name = autoCompleteTextView1.getText().toString().trim();
//                }
        })

        up_stock_Product_Variant.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (up_stock_Product_Variant.getText().toString().trim({ it <= ' ' }).length == 0) {
                    up_stock_Product_Variant.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0)
                    list_container_stock.visibility = View.GONE
                    //hidden_buttonlayout_stock.visibility = View.GONE
                    up_stock_spnCategory.visibility = View.VISIBLE
                    up_stock_spnProduct.visibility = View.VISIBLE
                    up_stock_search.visibility = View.VISIBLE
                } else {
                    up_stock_Product_Variant.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_product, 0)
                }
            }
        })

        up_stock_spnCategory.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, arg1: View,
                                        pos: Int, arg3: Long) { //				// TODO Auto-generated method stub
                check = check + 1
                if (check > 1) {
                    if (parent.getItemAtPosition(pos).toString()
                                    .equals(resources.getString(R.string.Select_Categories), ignoreCase = true)) {
                        categ_name = ""
                        subcateg_name = ""
                        results.clear()
                        results.add(resources.getString(R.string.Select_Product))
                        adapter_state2 = ArrayAdapter(applicationContext, R.layout.spinner_item, results)
                        adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
                        up_stock_spnProduct.setAdapter(adapter_state2)
                        results2.clear()
                        val adapter = ArrayAdapter(this@UpdateStockScreen1,
                                R.layout.autocomplete,
                                result_product)
                        up_stock_Product_Variant.setThreshold(1) // will start working from
                        // first character
                        up_stock_Product_Variant.setAdapter(adapter) // setting the adapter
                        // data into the
// AutoCompleteTextView
                        up_stock_Product_Variant.setText("")

                        Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Select_Category), "yes")
                    } else {
                        Global_Data.GLOVEL_CATEGORY_SELECTION = parent.getItemAtPosition(pos).toString()
                        //Intent intent = new Intent(getApplicationContext(), Filter_List.class);
                        Global_Data.GLOVEL_CATEGORY_NAME = parent.getItemAtPosition(pos).toString()
                        val contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim { it <= ' ' })
                        for (cn in contacts2) {
                            Global_Data.GLOVEL_CATEGORY_ID = cn.cust_Code

                        }
                        results.clear()
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);
                        val contacts22 = dbvoc.HSS_DescriptionITEM1_category_name(parent.getItemAtPosition(pos).toString().trim { it <= ' ' })
                        results.add(resources.getString(R.string.Select_Product))
                        results.add(resources.getString(R.string.select_all))
                        for (cn in contacts22) {
                            val str_product = "" + cn.stateName
                            //Global_Data.local_pwd = ""+cn.getPwd();
                            results.add(str_product)
                            println("Local Values:-" + Global_Data.local_user)
                            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
                        }
                        Global_Data.Search_Category_name = ""
                        Global_Data.Search_Product_name = "";
                        Global_Data.array_of_pVarient.clear()
                        Global_Data.ProductVariant = "";

                        adapter_state2 = ArrayAdapter(applicationContext, R.layout.spinner_item, results)
                        adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
                        up_stock_spnProduct.setAdapter(adapter_state2)
                        // up_stock_spnProduct.setOnItemSelectedListener(NewOrderActivity.this);
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)) {
                            val spinnerPosition = adapter_state2!!.getPosition(subcateg_name)
                            up_stock_spnProduct.setSelection(spinnerPosition)
                        }
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) { // TODO Auto-generated method stub
            }
        })

        up_stock_spnProduct.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, arg1: View,
                                        pos: Int, arg3: Long) {
                check_product = check_product + 1
                if (check_product > 1) {
                    if (up_stock_spnCategory.getSelectedItem().toString().equals(resources.getString(R.string.Select_Categories), ignoreCase = true)) {

                        Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Select_Category),"yes")
                    } else
                    {
                        if (up_stock_spnProduct.getSelectedItem().toString().equals(resources.getString(R.string.select_all), ignoreCase = true)) {

                            isInternetPresent = cd!!.isConnectingToInternet()
                            if (isInternetPresent) {

                                Global_Data.Search_Category_name = up_stock_spnCategory.getSelectedItem().toString();
                                Global_Data.Search_Product_name = "";
                                Global_Data.array_of_pVarient.clear()
                                Global_Data.ProductVariant = "";
                                val i = Intent(context, UpdateStockScreen2::class.java)
                                startActivity(i)
                                finish()

                            } else {

                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
                            }
                        }
                        else
                        if (!up_stock_spnProduct.getSelectedItem().toString().equals(resources.getString(R.string.Select_Product), ignoreCase = true)) {

                            isInternetPresent = cd!!.isConnectingToInternet()
                            if (isInternetPresent) {

                                Global_Data.Search_Category_name = up_stock_spnCategory.getSelectedItem().toString();
                                Global_Data.Search_Product_name = up_stock_spnProduct.getSelectedItem().toString();
                                Global_Data.array_of_pVarient.clear()
                                Global_Data.ProductVariant = "";
                                val i = Intent(context, UpdateStockScreen2::class.java)
                                startActivity(i)
                                finish()

                            } else {

                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
                            }
                        }
                 }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
            }
        })

        up_stock_search.setOnClickListener {

            isInternetPresent = cd!!.isConnectingToInternet()
            if (isInternetPresent) {

                if (up_stock_spnCategory.getSelectedItem().toString().equals(resources.getString(R.string.Select_Categories), ignoreCase = true)) {
                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Select_Category), "Yes")
                }
                else
                if (up_stock_spnProduct.getSelectedItem().toString().equals(resources.getString(R.string.Select_Product), ignoreCase = true)) {
                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Select_Product), "Yes")
                }
                else
                {
                    if (up_stock_spnCategory.getSelectedItem().toString().equals(resources.getString(R.string.select_all), ignoreCase = true)) {
                        Global_Data.Search_Category_name = up_stock_spnCategory.getSelectedItem().toString();
                        Global_Data.Search_Product_name = "";
                        Global_Data.array_of_pVarient.clear()
                        Global_Data.ProductVariant = "";

                    }
                    else
                    {
                        Global_Data.Search_Category_name = up_stock_spnCategory.getSelectedItem().toString();
                        Global_Data.Search_Product_name = up_stock_spnProduct.getSelectedItem().toString();
                        Global_Data.array_of_pVarient.clear()
                        Global_Data.ProductVariant = "";
                    }


                    val i = Intent(UpdateStockScreen1@ this, UpdateStockScreen2::class.java)
                    startActivity(i)
                    finish()
                }


            } else {

                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp: SharedPreferences = this@UpdateStockScreen1.getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f))
                    val achieved = Math.round(sp.getFloat("Achived", 0f))
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    targetNew = if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                        }
                    } else {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                val yourView = findViewById<View>(R.id.add)
                SimpleTooltip.Builder(this)
                        .anchorView(yourView)
                        .text(targetNew)
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {
        val i = Intent(UpdateStockScreen1@ this, UpdateStockScreen2::class.java)
        startActivity(i)
        finish()
    }

}
