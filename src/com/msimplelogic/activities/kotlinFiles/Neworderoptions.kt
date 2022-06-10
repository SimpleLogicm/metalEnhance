package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.ShopImagesAdaptor
import com.msimplelogic.model.ShopImagesModel
import com.msimplelogic.slidingmenu.AddRetailerFragment
import com.msimplelogic.webservice.ConnectionDetector
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.ButtonEnum
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import com.nightonke.boommenu.Util
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_neworderoptions.*
import kotlinx.android.synthetic.main.activity_order__customer_list.toolbar
import kotlinx.android.synthetic.main.content_neworderoptions.*
import org.json.JSONException

class Neworderoptions : BaseActivity() {
    var dbvoc = DataBaseHelper(this)
    var dialog: ProgressDialog? = null
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var shop_name: TextView? = null
    var shop_address: TextView? = null
    var c_mobile_number: TextView? = null
    var customer_landline: TextView? = null
    var c_credit_profile: TextView? = null
    var c_outstanding: TextView? = null
    var btn_Visit: Button? = null
    var Btn_notes: Button? = null
    var btn_customertraits: Button? = null
    var btnedit_cust_details: Button? = null
    var myanimation: Animation? = null
    var rv: RecyclerView? = null
    private var Re_Text = ""
    var list: ArrayList<ShopImagesModel>? = null
    var adaptor: ShopImagesAdaptor? = null
    var sp: SharedPreferences? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_neworderoptions)
        setTitle("")

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        val attrs = intArrayOf(R.attr.colorPrimary)
        val ta: TypedArray = obtainStyledAttributes(attrs)
        val color = ta.getResourceId(0, android.R.color.black)
        ta.recycle()

        loginDataBaseAdapter = LoginDataBaseAdapter(this)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()
        Global_Data.q = ""
        Global_Data.p_code.clear()
        Global_Data.p_mrp.clear()
        Global_Data.p_amount.clear()
        Global_Data.p_qty.clear()
        Global_Data.Order_hashmap.clear()

        cd = ConnectionDetector(this@Neworderoptions)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            imgheader_name.setImageResource(R.drawable.dark_theme_oredroptions_headder)
            imgfooter_details.setImageResource(R.drawable.darkh_theme_orderoption_footer)
        }

        if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("CUSTOMER_SERVICE")) {
            order_toolbar_title.setText(this@Neworderoptions.resources.getString(R.string.Customer_Services))
            setTitle("")
        } else if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("ADD_RETAILER")) {
            order_toolbar_title.setText(this@Neworderoptions.resources.getString(R.string.ADD_RETAILERsmall))
            setTitle("")
        } else if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("QUOTE")) {
            order_toolbar_title.setText(this@Neworderoptions.resources.getString(R.string.Quote))
            setTitle("")
        } else {
            order_toolbar_title.setText(this@Neworderoptions.resources.getString(R.string.ORDERsmall))
            setTitle("")
        }

        btn_Visit = findViewById(R.id.btn_Visit) as Button
        Btn_notes = findViewById(R.id.Btn_notes)
        btn_customertraits = findViewById(R.id.btn_customertraits)
        btnedit_cust_details = findViewById(R.id.btnshop_display)
        myanimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        rv = findViewById(R.id.rv);

        Global_Data.GLObalOrder_id = ""
        Global_Data.GLOvel_GORDER_ID = ""

        val spref: SharedPreferences = getSharedPreferences("SimpleLogic", 0)
        val shopname: String? = spref.getString("shopname", "")
        val shopadd: String? = spref.getString("shopadd", "")
        val shopcode: String? = spref.getString("shopcode", "")
        val c_mobile_numbers: String? = spref.getString("c_mobile_number", "")
        val customer_landline: String? = spref.getString("customer_landline", "")
        val c_credit_profile: String? = spref.getString("c_credit_profile", "")
        val c_outstanding: String? = spref.getString("c_outstanding", "")
        val c_overdue: String? = spref.getString("c_overdue", "")

        Global_Data.cust_str = spref.getString("shopcode", "")
        Global_Data.GLOvel_CUSTOMER_ID = spref.getString("shopcode", "")
        if (shopname != null) {
            Re_Text = shopname
            Global_Data.order_retailer = shopname
        }

        Log.d("CODE", "CODE" + shopcode)

        nt_shop_name.text = shopname
        nt_shop_address.text = shopadd
        nt_mobile_number.text = c_mobile_numbers
        nt_customer_landline.text = customer_landline
        nt_credit_profile.text = c_credit_profile
        nt_outstanding.text = c_outstanding
        nt_overdue.text = c_overdue

        nt_mobile_number.setOnClickListener {
            if (nt_mobile_number.text.toString().equals("Number not available")) {
                Global_Data.Custom_Toast(this@Neworderoptions, "Number not available", "yes");
            } else {
                requestPhoneCallPermission(nt_mobile_number.text.toString())

            }
        }

        nt_customer_landline.setOnClickListener {
            if (nt_customer_landline.text.toString().equals("Number not available")) {
                Global_Data.Custom_Toast(this@Neworderoptions, "Number not available", "yes");

            } else {
                requestPhoneCallPermission(nt_customer_landline.text.toString())

            }

        }

        list = ArrayList<ShopImagesModel>()
        val cont1: List<Local_Data> = dbvoc.getCustomername(Global_Data.GLOvel_CUSTOMER_ID)
        if (cont1.size > 0) {
            for (cnt1 in cont1) {
                try {
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getVisitc_img())) {
                        list!!.add(ShopImagesModel(cnt1.getVisitc_img()));
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getInshop_img())) {

                        list!!.add(ShopImagesModel(cnt1.getInshop_img()));
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getSignboard_img())) {
                        list!!.add(ShopImagesModel(cnt1.getSignboard_img()));
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            rv!!.visibility = View.GONE;
        }

        rv?.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        adaptor = ShopImagesAdaptor(this, list!!)
        rv?.adapter = adaptor

        if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("CUSTOMER_SERVICE")) {

            val builder = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        //
                        Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE"
                        Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = ""
                        val intent = Intent(applicationContext,
                                FeedbackDynamicActivity::class.java)
                        intent.putExtra("CP_NAME", "Feedback")
                        intent.putExtra("RE_TEXT", Re_Text)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left)
                    }
                    .normalText(getResources().getString(R.string.Feedback))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .normalImageRes(R.drawable.feedback)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .pieceColor(Color.WHITE)
                    .buttonWidth(Util.dp2px(300f))

            bmb.addBuilder(builder)

            val builder2 = HamButton.Builder()
                    .listener {
                        Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE"
                        Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = ""
                        val intent = Intent(applicationContext,
                                Customer_Feed::class.java)
                        intent.putExtra("CP_NAME", "Complaints")
                        intent.putExtra("RE_TEXT", Re_Text)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left)
                    }
                    .normalText(getResources().getString(R.string.Complaints))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.complaint)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder2)

            val builder3 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        val intent = Intent(applicationContext, CompetitorAnalysis::class.java)
                        startActivity(intent)
                    }
                    .normalText(getResources().getString(R.string.CompetitorAnalysis))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.competitor_analysis)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder3)

            val builder4 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE"

                        Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = ""
                        val intent = Intent(applicationContext,
                                Customer_Feed::class.java)
                        intent.putExtra("CP_NAME", "Claim")
                        intent.putExtra("RE_TEXT", Re_Text)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left)
                    }
                    .normalText(getResources().getString(R.string.Claims))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.claims)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder4)

            val builder5 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE"

                        Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = ""
                        val intent = Intent(applicationContext,
                                Customer_Feed::class.java)
                        intent.putExtra("CP_NAME", "Image")
                        intent.putExtra("RE_TEXT", Re_Text)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left)
                    }
                    .normalText("Media")
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.picturevideo_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder5)

//            val builder6 = HamButton.Builder()
//                    // .normalImageRes(R.drawable.notes)
//                    .normalText(getResources().getString(R.string.Video))
//                    .textSize(16)
//                    .listener { index ->
//                        Global_Data.GLOVEL_ORDER_REJECT_FLAG = "FALSE"
//
//                        Global_Data.PREVIOUS_ORDER_BACK_FLAG_REURN = ""
//                        val intent = Intent(applicationContext,
//                                Customer_Feed::class.java)
//                        intent.putExtra("CP_NAME", "video")
//                        intent.putExtra("RE_TEXT", Re_Text)
//                        startActivity(intent)
//                        overridePendingTransition(R.anim.slide_in_right,
//                                R.anim.slide_out_left)
//                    }

//                    .textGravity(Gravity.CENTER_VERTICAL)
//                    .normalColorRes(R.color.primarycolor)
//                    .pieceColor(Color.WHITE)
//                    .buttonWidth(Util.dp2px(300f))
//            bmb.addBuilder(builder6)
            //bmb.setButtonEnum(ButtonEnum.Ham)
            bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_5_4)
            bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5)
        } else if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("ADD_RETAILER")) {
            val builder = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        val intent = Intent(applicationContext, AddRetailerFragment::class.java)
                        startActivity(intent)
                    }
                    .normalText("Add Retailer")
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.add_retailer_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder)

            val builder2 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        val intent = Intent(applicationContext, Customer_info_main::class.java)
                        startActivity(intent)
                    }
                    .normalText("View Customer")
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.view_customer_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder2)
            //bmb.setButtonEnum(ButtonEnum.Ham)
            bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2)
            bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2)
        } else if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("QUOTE")) {
            val builder = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        Global_Data.q = "Yes"
                        val intent = Intent(applicationContext, NewOrderActivity::class.java)
                        startActivity(intent)
                    }
                    .normalText(getResources().getString(R.string.New_Quote))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.new_quote_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder)

            val builder2 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        val intent = Intent(applicationContext, Status_Activity::class.java)
                        startActivity(intent)
                    }
                    .normalText(getResources().getString(R.string.Quote_status))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.quote_status_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder2)
            //bmb.setButtonEnum(ButtonEnum.Ham)
            bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2)
            bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2)
        } else {


            val builder = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        val intent = Intent(applicationContext, NewOrderActivity::class.java)
                        startActivity(intent)
                    }
                    .normalText(getResources().getString(R.string.New_Order))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.new_order_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder)

            val builder2 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        Global_Data.Number.clear()
                        val intent = Intent(applicationContext, ReturnOrder1::class.java)
                        startActivity(intent)
                    }
                    .normalText(getResources().getString(R.string.Sales_Return))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.new_order_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder2)

//            val builder3 = HamButton.Builder()
//                    // .normalImageRes(R.drawable.notes)
//                    .listener {
//                        val intent = Intent(applicationContext, Status_Activity::class.java)
//                        startActivity(intent)
//                    }
//                    .normalText(getResources().getString(R.string.Quote_status))
//                    .textSize(16)
//                    .textGravity(Gravity.CENTER_VERTICAL)
//                    .normalColorRes(R.color.primarycolor)
//                    .pieceColor(Color.WHITE)
//                    .buttonWidth(Util.dp2px(300f))
//            bmb.addBuilder(builder3)

            val builder3 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        Global_Data.SpeedometerStatus = "yes"
                        val m = Intent(this@Neworderoptions, OutstandingOverdueActivity::class.java)
                        m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        // m.putExtra("ANN_ID", Ann_Model.getAnn_id());
//                m.putExtra("REGION_ID", list.getDescription());
                        startActivity(m)
                    }
                    .normalText(getResources().getString(R.string.View_Outstanding))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.view_outstanding_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder3)

            val builder4 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .listener {
                        val intent = Intent(applicationContext, NoOrderActivity::class.java)
                        startActivity(intent)
                    }
                    .normalText(getResources().getString(R.string.No_Order))
                    .textSize(16)
                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.no_order_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder4)

            val builder5 = HamButton.Builder()
                    // .normalImageRes(R.drawable.notes)
                    .normalText(getResources().getString(R.string.Previous_Order))
                    .textSize(16)
                    .listener { index ->

                        isInternetPresent = cd!!.isConnectingToInternet()
                        if (isInternetPresent) {
                            getPrevious_OrderData()
                        } else {
                            Global_Data.Custom_Toast(this@Neworderoptions, resources.getString(R.string.internet_connection_error), "yes")
                        }
//                        val intent = Intent(applicationContext, Previous_orderNew_S2::class.java)
//                        startActivity(intent)
                    }

                    .textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(color)
                    .pieceColor(Color.WHITE)
                    .normalImageRes(R.drawable.previous_order_ham)
                    .imagePadding(Rect(30, 30, 30, 30))
                    .buttonWidth(Util.dp2px(300f))
            bmb.addBuilder(builder5)
            bmb.setButtonEnum(ButtonEnum.Ham)
            bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_5_4)
            bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5)

        }

//        primary_buttonclick.setOnClickListener { view ->
//            primary_buttonclick?.startAnimation(myanimation)
////            val intent = Intent(applicationContext,NewOrderActivity::class.java)
////            startActivity(intent)
//        }


        btn_Visit?.setOnClickListener {
            //   btn_Visit?.startAnimation(myanimation)
            startActivity(Intent(this@Neworderoptions, Visit_Schedule::class.java))
        }
        btn_customertraits?.setOnClickListener {
            //btn_customertraits?.startAnimation(myanimation)
            if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("CUSTOMER_SERVICE")) {
                startActivity(Intent(this@Neworderoptions, CustomerServicesTraits::class.java))
            } else if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("ADD_RETAILER")) {
                //startActivity(Intent(this@Neworderoptions, QuoteAddRetailerTrait::class.java))
                val intent = Intent(this@Neworderoptions, QuoteAddRetailerTrait::class.java)
                intent.putExtra("TYPE", "retailer")
                startActivity(intent)

            } else if (Global_Data.CUSTOMER_SERVICE_FLAG.equals("QUOTE")) {
                //startActivity(Intent(this@Neworderoptions, QuoteAddRetailerTrait::class.java))
                val intent = Intent(this@Neworderoptions, QuoteAddRetailerTrait::class.java)
                intent.putExtra("TYPE", "quotation")
                startActivity(intent)
            } else {
                startActivity(Intent(this@Neworderoptions, Customer_Traits::class.java))
            }
        }
        Btn_notes?.setOnClickListener {
            startActivity(Intent(this@Neworderoptions, Order_notes::class.java))
            //Btn_notes?.startAnimation(myanimation)
        }
        btnedit_cust_details?.setOnClickListener {
            startActivity(Intent(this@Neworderoptions, Edit_cutomer_details::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
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
                val sp = this@Neworderoptions.getSharedPreferences("SimpleLogic", 0)
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

    override fun onBackPressed() { // TODO Auto-generated method stub
//super.onBackPressed();

        if(Global_Data.dayscheduleback!="" && Global_Data.dayscheduleback=="yes"){
            val i = Intent(this@Neworderoptions, Day_sheduler::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            startActivity(i)
            finish()


        }else{
            val i = Intent(this@Neworderoptions, Sales_Dash::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            startActivity(i)
            finish()

        }

           }

    fun getPrevious_OrderData() { //calendarn = Calendar.getInstance();
//year = calendarn.get(Calendar.YEAR);

        dialog = ProgressDialog(this@Neworderoptions, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog!!.setMessage(resources.getString(R.string.Please_Wait_Previous_Sync))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()
        try { //TODO USER EMAIL
            val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
            val Cust_domain = sp.getString("Cust_Service_Url", "")
            val service_url = Cust_domain + "metal/api/v1/"
            val device_id = sp.getString("devid", "")
            val spf: SharedPreferences = this@Neworderoptions.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)
            Log.i("volley", "domain: $service_url")
            Log.i("volley", "email: $user_email")
            var serv = service_url + "customers/previous_order?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email;
            Log.i("serv", "serv: $serv")

            Log.i("previous_order url", "previous_order url " + service_url + "customers/previous_order?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email)
            val jsObjRequest = JsonObjectRequest(service_url + "customers/previous_order?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email, null, Response.Listener { response ->
                Log.i("volley", "response: $response")
                //  Log.i("volley", "response reg Length: " + response.length());
                try {
                    var response_result: String? = ""
                    if (response.has("result")) {
                        response_result = response.getString("result")
//                        val toast = Toast.makeText(applicationContext, response_result, Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(applicationContext, response_result, "yes")
                    } else {
                        dbvoc.getDeleteTable("previous_order_products")
                        dbvoc.getDeleteTable("previous_orders")
                        //JSONArray previous_orders = response.getJSONArray("previous_orders");
                        val previous_order_products = response.getJSONArray("order_products")
                        //Log.i("volley", "response reg previous_orders Length: " + previous_orders.length());
                        Log.i("volley", "response reg previous_order_products Length: " + previous_order_products.length())
                        //	Log.d("States", "previous_orders" + previous_orders.toString());
                        Log.d("States", "previous_order_products$previous_order_products")
                        //
                        if (previous_order_products.length() <= 0) {
                            dialog!!.dismiss()

                            Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Previous_order_not_found), "yes")
                        } else { //								for (int i = 0; i < previous_orders.length(); i++) {
                            //
                            //									JSONObject jsonObject = previous_orders.getJSONObject(i);
                            //
                            //								}
                            for (i in 0 until previous_order_products.length()) {
                                val jsonObject = previous_order_products.getJSONObject(i)
                                Global_Data.Previous_Order_UpdateOrder_ID = jsonObject.getString("order_number").trim { it <= ' ' }
                                Global_Data.Previous_Order_ServiceOrder_ID = jsonObject.getString("order_number").trim { it <= ' ' }
                                loginDataBaseAdapter?.insertPreviousOrderProducts("", "", jsonObject.getString("order_number"), "", "", "", "", "", jsonObject.getString("scheme_code"), " ", "", jsonObject.getString("total_qty"), jsonObject.getString("retail_price"), jsonObject.getString("mrp"), jsonObject.getString("amount"), "", "", "", " ", jsonObject.getString("product_code"), " ", jsonObject.getString("product_name"))
                            }

                            val intent = Intent(this@Neworderoptions, Previous_orderNew_S2::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            //    intent.putExtra("int","mart")
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            finish()
                            dialog!!.dismiss()
                        }
                        dialog!!.dismiss()
                        //finish();
                    }
                    // }
                    // output.setText(data);
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog!!.dismiss()
                }
                dialog!!.dismiss()
            }, Response.ErrorListener { error ->
                Log.i("volley", "error: $error")

                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Server_Error), "yes")
                dialog!!.dismiss()
            })
            val requestQueue = Volley.newRequestQueue(this@Neworderoptions)
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false)
            val socketTimeout = 200000 //30 seconds - change to what you want
            val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            jsObjRequest.retryPolicy = policy
            requestQueue.add(jsObjRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            dialog!!.dismiss()
        }
    }

    fun requestPhoneCallPermission(mobile_number: String) {
        Dexter.withActivity(this@Neworderoptions as Activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$mobile_number")
                        this@Neworderoptions.startActivity(callIntent)
                        return
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied) {
                            showSettingsDialogcall()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun showSettingsDialogcall() {
        val builder = AlertDialog.Builder(this@Neworderoptions)
        builder.setTitle(this@Neworderoptions.resources.getString(R.string.need_permission_message))
        builder.setCancelable(false)
        builder.setMessage(this@Neworderoptions.resources.getString(R.string.need_permission_setting_message))
        builder.setPositiveButton(this@Neworderoptions.resources.getString(R.string.GOTO_SETTINGS)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this@Neworderoptions.packageName, null)
        intent.data = uri
        (this@Neworderoptions as Activity).startActivityForResult(intent, 101)
    }


}

