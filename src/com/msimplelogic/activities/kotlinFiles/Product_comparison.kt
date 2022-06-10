package com.msimplelogic.activities.kotlinFiles

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msimplelogic.activities.*

import com.msimplelogic.adapter.Product_comparison_adaptor
import com.msimplelogic.model.Product_comparison_Model
import com.msimplelogic.webservice.ConnectionDetector
import cpm.simplelogic.helper.ContactInfo
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

import kotlinx.android.synthetic.main.activity_product_comparison.fab
import kotlinx.android.synthetic.main.activity_product_comparison.toolbar
import kotlinx.android.synthetic.main.content_beat_selection.*
import kotlinx.android.synthetic.main.content_product_comparison.*
import kotlinx.android.synthetic.main.content_update_stock_screen1.*
import java.util.ArrayList

class Product_comparison : BaseActivity() {
    internal var categ_name: String? = null
    internal var subcateg_name: String? = null
    internal var check = 0
    var str_variant: String? = null
    internal var product_code = ""
    internal var check_product = 0
    internal var check_ProductSpec = 0
    internal var Product_Variant: AutoCompleteTextView? = null
    internal var spnCategory: Spinner? = null
    internal var spnProductSpec: Spinner? = null
    internal var spnScheme: Spinner? = null
    internal var spnProduct: Spinner? = null
    var sp: SharedPreferences? = null
    internal var dataAdapterCategory: ArrayAdapter<String>? = null
    internal var dataAdapterProductSpec: ArrayAdapter<String>? = null
    internal var dataAdapterProduct: ArrayAdapter<String>? = null

    //ArrayList productList = new ArrayList();
    internal var listProduct: List<String>? = null
    internal var listProductSpec: List<String>? = null
    internal var listScheme: List<String>? = null

    private val results = java.util.ArrayList<String>()
    private val results1 = java.util.ArrayList<String>()
    private val results2 = java.util.ArrayList<String>()
    private val result_product = java.util.ArrayList<String>()

    internal var adapter_state1: ArrayAdapter<String>? = null
    internal var adapter_state2: ArrayAdapter<String>? = null
    internal var adapter_state3: ArrayAdapter<String>? = null
    internal var dbvoc = DataBaseHelper(this)
    internal var recList: RecyclerView? = null
    internal var rlout_price: RelativeLayout? = null
    internal var rlout_stock: RelativeLayout? = null
    var cd: ConnectionDetector? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_comparison)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spnCategory = findViewById<View>(R.id.spnCategory) as Spinner
        spnProduct = findViewById<View>(R.id.spnProduct) as Spinner
        Product_Variant = findViewById<View>(R.id.Product_Variant) as AutoCompleteTextView
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theamep.setImageResource(R.drawable.dark_hedder)


        }

        recList = findViewById<View>(R.id.cardList) as RecyclerView
        recList?.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recList?.setLayoutManager(llm)

        recList?.setVisibility(View.INVISIBLE)

        results2.clear()
        val contacts2 = dbvoc.allVariant
        for (cn in contacts2) {
            results2.add(cn.stateName)
            result_product.add(cn.stateName)
        }

        val adapter = ArrayAdapter(this,
                R.layout.autocomplete,
                results2)
        Product_Variant?.setThreshold(1)// will start working from
        // first character
        Product_Variant?.setAdapter(adapter)// setting the adapter
        // data into the
        // AutoCompleteTextView

        Product_Variant?.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= Product_Variant!!.getRight() - Product_Variant?.getCompoundDrawables()?.get(DRAWABLE_RIGHT)!!.bounds.width()) {

                    val view = this@Product_comparison.getCurrentFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.getWindowToken(), 0)
                    }
                    //autoCompleteTextView1.setText("");
                    Product_Variant?.showDropDown()
                    return@OnTouchListener true
                }
            }
            false
        })

        Product_Variant?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, arg1, pos, id ->
            //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

            Global_Data.hideSoftKeyboard(this@Product_comparison)
            //                editTextQuantity.setFocusableInTouchMode(true);
            //                editTextQuantity.setEnabled(true);

            val cont = dbvoc.getProductByCat(Product_Variant?.getText().toString().trim { it <= ' ' })
            //results2.add("Select Variant");
            for (cn1 in cont) {
                val str_var = "" + cn1.stateName
                //str_var1 = ""+cn1.getMRP();
                val str_var2 = "" + cn1._Description
                val str_var3 = "" + cn1._Claims
                Global_Data.amnt = "" + cn1._Description
                Global_Data.amnt1 = "" + cn1._Claims
                product_code = cn1.code

                categ_name = cn1.category
                subcateg_name = cn1.subcateg


            }

            adapter_state1?.setDropDownViewResource(R.layout.spinner_item)
            spnCategory?.setAdapter(adapter_state1)
            //spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

            adapter_state2?.setDropDownViewResource(R.layout.spinner_item)
            spnProduct?.setAdapter(adapter_state2)
            //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
                val spinnerPosition = adapter_state1?.getPosition(categ_name)
                spinnerPosition?.let { spnCategory?.setSelection(it) }
            }
        })

        Global_Data.GLOVEL_SubCategory_Button = ""


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
        adapter_state1?.setDropDownViewResource(R.layout.spinner_item)
        spnCategory?.setAdapter(adapter_state1)
        //	spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

        if (!Global_Data.GLOVEL_CATEGORY_SELECTION.equals("", ignoreCase = true)) {
            Log.d("Globel cat", "in")
            adapter_state1?.getPosition(Global_Data.GLOVEL_CATEGORY_SELECTION)?.let { spnCategory?.setSelection(it) }
            Global_Data.GLOVEL_CATEGORY_SELECTION = ""

        }


        results.add(resources.getString(R.string.Select_Product))
        adapter_state2 = ArrayAdapter(this, R.layout.spinner_item, results)
        adapter_state2?.setDropDownViewResource(R.layout.spinner_item)
        spnProduct?.setAdapter(adapter_state2)


        listProduct = ArrayList()
        dataAdapterProduct = ArrayAdapter(this, R.layout.spinner_item, listProduct as ArrayList<String>)

        listProductSpec = ArrayList()
        dataAdapterProductSpec = ArrayAdapter(
                this, R.layout.spinner_item, listProductSpec as ArrayList<String>)

//		listScheme = new ArrayList<String>();
//		dataAdapterScheme = new ArrayAdapter<String>(
//				this, R.layout.spinner_item, listScheme);

        val listCategory = ArrayList<String>()
        listCategory.add(resources.getString(R.string.Select_Categories))

        adapter_state1 = ArrayAdapter(this, R.layout.spinner_item, results1)


        spnCategory?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, arg1: View,
                                        pos: Int, arg3: Long) {
                //				// TODO Auto-generated method stub

                check = check + 1
                if (check > 1) {
                    if (parent.getItemAtPosition(pos).toString()
                                    .equals(resources.getString(R.string.Select_Categories), ignoreCase = true)) {
                        categ_name = ""
                        subcateg_name = ""
                        results.clear()
                        results.add(resources.getString(R.string.Select_Product))
                        adapter_state2 = ArrayAdapter(applicationContext, R.layout.spinner_item, results)
                        adapter_state2?.setDropDownViewResource(R.layout.spinner_item)
                        spnProduct?.setAdapter(adapter_state2)
                        recList?.setVisibility(View.INVISIBLE)

                        results2.clear()

                        val adapter = ArrayAdapter(this@Product_comparison,
                                R.layout.autocomplete,
                                result_product)
                        Product_Variant?.setThreshold(1)// will start working from
                        // first character
                        Product_Variant?.setAdapter(adapter)// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant?.setText("")



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
                        for (cn in contacts22) {
                            val str_product = "" + cn.stateName
                            //Global_Data.local_pwd = ""+cn.getPwd();

                            results.add(str_product)
                            println("Local Values:-" + Global_Data.local_user)
                        }

                        adapter_state2 = ArrayAdapter(applicationContext, R.layout.spinner_item, results)
                        adapter_state2?.setDropDownViewResource(R.layout.spinner_item)
                        spnProduct?.setAdapter(adapter_state2)
                        // spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)) {
                            val spinnerPosition = adapter_state2?.getPosition(subcateg_name)
                            if (spinnerPosition != null) {
                                spnProduct?.setSelection(spinnerPosition)
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub

            }
        })

        spnProduct?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, arg1: View,
                                        pos: Int, arg3: Long) {
                //					// TODO Auto-generated method stub
                check_product = check_product + 1
                if (check_product > 1) {

                    if (spnCategory?.getSelectedItem().toString().equals(resources.getString(R.string.Select_Categories), ignoreCase = true)) {


                        Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Please_Select_Category), "yes")

                    } else if (parent.getItemAtPosition(pos).toString()
                                    .equals(resources.getString(R.string.Select_Product), ignoreCase = true)) {
                        val adapter = ArrayAdapter(this@Product_comparison,
                                R.layout.autocomplete,
                                result_product)
                        Product_Variant?.setThreshold(1)// will start working from
                        // first character
                        Product_Variant?.setAdapter(adapter)// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant?.setText("")
                        recList?.setVisibility(View.INVISIBLE)

                    } else {

                        results2.clear()

                        val contacts33 = dbvoc.HSS_DescriptionITEM1_IDD(parent.getItemAtPosition(pos).toString().trim { it <= ' ' })
                        for (cn in contacts33) {
                            Global_Data.GLOVEL_PRODUCT_ID = cn.cust_Code
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.GLOVEL_PRODUCT_ID)) {

                            //List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_ID(Global_Data.GLOVEL_PRODUCT_ID);
                            val contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAME(spnCategory?.getSelectedItem().toString().trim { it <= ' ' }, parent.getItemAtPosition(pos).toString().trim { it <= ' ' })
                            // results2.add("Select Variant");
                            for (cn in contacts3) {
                                str_variant = "" + cn.stateName

                                results2.add(str_variant!!)

                            }

                            val adapter = ArrayAdapter(this@Product_comparison,  R.layout.autocomplete, results2)
                            Product_Variant?.setThreshold(1)// will start working from
                            // first character
                            Product_Variant?.setAdapter(adapter)// setting the adapter
                            // data into the
                            // AutoCompleteTextView

                        }

                        val contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAMENEW(spnCategory?.getSelectedItem().toString().trim { it <= ' ' }, parent.getItemAtPosition(pos).toString().trim { it <= ' ' }, Product_Variant?.getText().toString().trim { it <= ' ' })

                        if (contacts3.size <= 0 && !Product_Variant?.getText().toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {


                            Product_Variant?.setText("")


                            recList?.setVisibility(View.INVISIBLE)
                        } else {
                            if (Product_Variant?.length()!! > 0) {
                                recList?.setVisibility(View.VISIBLE)
                                val result = ArrayList<ContactInfo>()
                                for (cn in contacts3) {

                                    val ci = ContactInfo()
                                    ci.name = cn._product_desc
                                    ci.rp = cn.rp
                                    ci.mrp = cn.mrp

                                    result.add(ci)
                                }

                                val ca = ContactAdapter(this@Product_comparison, result)
                                recList?.setAdapter(ca)
                                ca.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub

            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                super.onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Product_comparison.getSharedPreferences("SimpleLogic", 0)
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


}
