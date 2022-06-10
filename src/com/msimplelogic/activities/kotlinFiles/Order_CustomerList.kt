package com.msimplelogic.activities.kotlinFiles

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.FoldingCellListAdapter
import com.msimplelogic.model.Customer_Model
import com.msimplelogic.slidingmenu.AddRetailerFragment
import com.ramotion.foldingcell.FoldingCell
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_order__customer_list.*
import kotlinx.android.synthetic.main.content_order__customer_list.*

class Order_CustomerList : BaseActivity() {
    var dbvoc = DataBaseHelper(this)
    val testingList: ArrayList<Customer_Model> = ArrayList();
    val testingListCUS: ArrayList<Customer_Model> = ArrayList();
    var beatcode: String? = ""
    var fab:ImageView?=null
    var s  = arrayOf<String>()
    lateinit var adapter:FoldingCellListAdapter
    var All_customers = ArrayList<String>()
    var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState)
    //    super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order__customer_list)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        setTitle("")


        val textView: TextView = findViewById(R.id.beat_name) as TextView
        fab=findViewById(R.id.fab);
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            fab!!.setColorFilter(ContextCompat.getColor(this@Order_CustomerList, R.color.tintplus), PorterDuff.Mode.MULTIPLY)

            hedder_theame.setImageResource(R.drawable.dark_hedder)
        }
        fab!!.setOnClickListener {

            val intent = Intent(applicationContext, AddRetailerFragment::class.java)
            startActivity(intent)
        }


        val spref: SharedPreferences = getSharedPreferences("SimpleLogic", 0)
        val Beat_name: String? = spref.getString("Beat_NAme","")
        val Beat_Code: String? = spref.getString("Beat_Code","")
        textView.setText(" " + Beat_name)
        beatcode = spref.getString("Beat_Code","")

        val items: ArrayList<Customer_Model> = populateDta();
        adapter = FoldingCellListAdapter(this, items)
        mainListView.setAdapter(adapter);

        val adapter1: ArrayAdapter<String> = ArrayAdapter<String>(this@Order_CustomerList,  R.layout.autocomplete,
                All_customers)
        Product_Variant.setThreshold(1) // will start working from
        Product_Variant.setAdapter(adapter1) // setting the adapter


        mainListView.setOnItemClickListener(OnItemClickListener { adapterView, view, pos, l ->

            (view as FoldingCell).toggle(false)
            adapter.registerToggle(pos)

        })

        Product_Variant.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= Product_Variant.getRight() - Product_Variant.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()) {
                    val view: View = this@Order_CustomerList.getCurrentFocus()!!
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    //autoCompleteTextView1.setText("");
                    Product_Variant.showDropDown()
                    return@OnTouchListener true
                }
            }
            false
        })

        Product_Variant.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Product_Variant.getText().toString().trim({ it <= ' ' }).length == 0) {
                    testingList.clear()
                    testingList.addAll(testingListCUS)
                    adapter = FoldingCellListAdapter(this@Order_CustomerList, testingList)
                    mainListView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        Product_Variant.setOnItemClickListener(OnItemClickListener { parent, arg1, pos, id ->
            //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();
            Global_Data.hideSoftKeyboard(this@Order_CustomerList)
            var customer_name = ""
            var address_type = ""
            if (Product_Variant.getText().toString().trim({ it <= ' ' }).indexOf(":") > 0) {
                s = Product_Variant.getText().toString().trim({ it <= ' ' }).split(":".toRegex()).toTypedArray()
                customer_name = s.get(0).trim({ it <= ' ' })
                address_type = s.get(1).trim({ it <= ' ' })
            } else {
                customer_name = Product_Variant.getText().toString().trim({ it <= ' ' })
            }
            //				Global_Data.credit_limit_amount = "";
//				Global_Data.outstandings_amount = "";
            val contacts = dbvoc.getCustomerCode(customer_name)
            if (contacts.size <= 0) {

                Global_Data.Custom_Toast(this@Order_CustomerList,
                        resources.getString(R.string.Customer_Not_Found),"yes")
            } else {
                testingList.clear()
                for (cn in contacts) {
                    val items = Customer_Model()
                    Global_Data.GLOvel_CUSTOMER_ID = cn.cust_Code
                    Global_Data.customer_MobileNumber = cn.mobilE_NO
                    Global_Data.CUSTOMER_NAME_NEW = cn.customeR_NAME
                    Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress()

                    Global_Data.CustLandlineNo = cn.landlinE_NO

                    items.name = cn.getCUSTOMER_SHOPNAME()
                    items.address = cn.getAddress()
                    items.mobile = cn.getMOBILE_NO()
                    items.landline = cn.landlinE_NO
                    val customer_id = cn.cust_Code
                    items.code = cn.cust_Code

                    val contactlimit = dbvoc.getCreditprofileData(customer_id)
                    if (contactlimit.size > 0) {
                        for (cnn in contactlimit) {
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn._credit_limit)) {
                                val credit_limit = java.lang.Double.valueOf(cnn._credit_limit)
                                items.credit_profile = credit_limit

                            } else {
                                items.credit_profile = 0.0
                            }
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn._shedule_outstanding_amount)) {
                                Global_Data.amt_outstanding = java.lang.Double.valueOf(cnn._shedule_outstanding_amount)
                                items.outstanding = Global_Data.amt_outstanding

                            } else {
                                items.outstanding = 0.0
                            }
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.ammount_overdue)) {
                                val amt_overdue = java.lang.Double.valueOf(cnn.ammount_overdue)
                                items.overdue = amt_overdue

                            } else {
                                items.overdue = 0.0
                            }
                        }
                    } else {
                        items.credit_profile = 0.0
                        items.outstanding = 0.0
                        items.overdue = 0.0
                    }
                    testingList.add(items)

                }
                adapter = FoldingCellListAdapter(this, testingList)
                mainListView.setAdapter(adapter);
                adapter.setNotifyOnChange(true)
            }
        })
    }

    fun populateDta(): ArrayList<Customer_Model> {
        val contacts3 = dbvoc.getAllCustomer_BYBeat(beatcode)
        if (contacts3.size <= 0) {
            Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found),"yes")
            val intent = Intent(applicationContext,
                    MainActivity::class.java)
            startActivity(intent)
        } else {
            for (cn in contacts3) {
                val items = Customer_Model()
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getCUSTOMER_SHOPNAME())) {
                    items.name = cn.getCUSTOMER_SHOPNAME()
                }
                else
                {
                    items.name = " "
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getAddress())) {
                    items.address = cn.getAddress()
                }
                else
                {
                    items.address = " "
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getMOBILE_NO())) {
                    items.mobile = cn.getMOBILE_NO()
                }
                else
                {
                    items.mobile = " "
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.landlinE_NO)) {
                    items.landline = cn.landlinE_NO
                }
                else
                {
                    items.landline = " "
                }

                All_customers.add( cn.getCUSTOMER_SHOPNAME())
                val customer_id = cn.cust_Code
                items.code = cn.cust_Code
                val contactlimit = dbvoc.getCreditprofileData(customer_id)
                if (contactlimit.size > 0) {
                    for (cnn in contactlimit) {
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn._credit_limit)) {
                            val credit_limit = java.lang.Double.valueOf(cnn._credit_limit)
                            items.credit_profile = credit_limit

                        } else {
                            items.credit_profile = 0.0
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn._shedule_outstanding_amount)) {
                            Global_Data.amt_outstanding = java.lang.Double.valueOf(cnn._shedule_outstanding_amount)
                            items.outstanding = Global_Data.amt_outstanding

                        } else {
                            items.outstanding = 0.0
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.ammount_overdue)) {
                            val amt_overdue = java.lang.Double.valueOf(cnn.ammount_overdue)
                            items.overdue = amt_overdue

                        } else {
                            items.overdue = 0.0
                        }
                    }
                } else {
                    items.credit_profile = 0.0
                    items.outstanding = 0.0
                    items.overdue = 0.0
                }
                testingListCUS.add(items)
                testingList.add(items)
            }


        }

        return testingList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                super.onBackPressed()            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Order_CustomerList.getSharedPreferences("SimpleLogic", 0)
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


    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

}

