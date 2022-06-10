
package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.utils.Utils
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.TaskSwipeAdapter
import com.msimplelogic.model.Product
import com.msimplelogic.swipelistview.BaseSwipeListViewListener
import com.msimplelogic.swipelistview.SwipeListView
import com.msimplelogic.swipelistview.sample.utils.SettingsManager
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.content_task.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import pl.droidsonroids.gif.GifImageView
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class ActivityTask : BaseActivity() {
    var toolbar: Toolbar? = null
    var sp : SharedPreferences?=null
    var SwipeList: ArrayList<HashMap<String, String>>? = null
    var SwipeListSearch: ArrayList<HashMap<String, String>>? = null
    private var swipeListView: SwipeListView? = null
    private var adapter: TaskSwipeAdapter? = null
    private var dataOrder: ArrayList<Product>? = null
    var taskPlusBtn: ImageView? = null
    var txtTasknote: TextView? = null
    var activity_flag = ""
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var TaskList: MutableList<String> = ArrayList()
    var dialog: ProgressDialog? = null
    var gifview:GifImageView?=null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_task)

        Global_Data.newll="yes"

        context = ActivityTask@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        val extras = intent.extras
        val i = this.intent


        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            filterimg.setImageResource(R.drawable.filterordertype_icon_dark)
            img_sortby.setImageResource(R.drawable.sortby_ordertype_dark)
            hedder_theame.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }

        if (extras != null) {
            activity_flag = extras.getString("activity_flag")!!
            Global_Data.PlannerName = activity_flag
            order_toolbar_title.text=resources.getString(R.string.Task11)
            // and get whatever type user account id is
        }
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        assert(supportActionBar != null //null check

        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        swipeListView = findViewById<View>(R.id.task_swipelist) as SwipeListView
        taskPlusBtn = findViewById(R.id.taskplus_btn)
        txtTasknote = findViewById(R.id.txt_tasknote)
        gifview=findViewById(R.id.gifview);
        txtTasknote!!.setText(Global_Data.PlannerName)
        SwipeList = ArrayList()
        SwipeListSearch = ArrayList()

        if(txtTasknote!!.text.toString().equals("Note")){
            order_toolbar_title.text=resources.getString(R.string.Note)

        }else{
            order_toolbar_title.text=resources.getString(R.string.Task11)

        }

        Handler().postDelayed(Runnable {
            gifview!!.visibility=View.GONE
                   }, 3000)

        adapter = TaskSwipeAdapter(this@ActivityTask, SwipeList)

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {

            GetActivityPlannerData()
        } else {
            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
        }

        taskPlusBtn!!.setOnClickListener(View.OnClickListener {
            Global_Data.PlannerUpdate = "";
            startActivity(Intent(this@ActivityTask, ActivityTaskDetails::class.java))
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            swipeListView!!.setMultiChoiceModeListener(object : MultiChoiceModeListener {
                override fun onItemCheckedStateChanged(mode: ActionMode, position: Int,
                                                       id: Long, checked: Boolean) {
                    mode.title = "Selected (" + swipeListView!!.countSelected + ")"
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    val id = item.itemId
                    if (id == R.id.menu_delete) {
                        swipeListView!!.dismissSelected()
                        return true
                    }
                    return false
                }

                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    val inflater = mode.menuInflater
                    inflater.inflate(R.menu.menu_choice_items, menu)
                    return true
                }

                override fun onDestroyActionMode(mode: ActionMode) {
                    swipeListView!!.unselectedChoiceStates()
                }

                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }
            })
        }
        swipeListView!!.setSwipeListViewListener(object : BaseSwipeListViewListener() {
            override fun onOpened(position: Int, toRight: Boolean) {}
            override fun onClosed(position: Int, fromRight: Boolean) {}
            override fun onListChanged() {}
            override fun onMove(position: Int, x: Float) {}
            override fun onStartOpen(position: Int, action: Int, right: Boolean) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action))
            }

            override fun onStartClose(position: Int, right: Boolean) {
                Log.d("swipe", String.format("onStartClose %d", position))
            }

            override fun onClickFrontView(position: Int) {
                Log.d("swipe", String.format("onClickFrontView %d", position))
            }

            override fun onClickBackView(position: Int) {
                Log.d("swipe", String.format("onClickBackView %d", position))
            }

            override fun onDismiss(reverseSortedPositions: IntArray) {
                for (position in reverseSortedPositions) {
                    (dataOrder as ArrayList<Product>?)!!.removeAt(position)
                }
                adapter!!.notifyDataSetChanged()
            }
        })
        // swipeListView!!.adapter = adapter
        reload()

        val adaptersearch: ArrayAdapter<String> = ArrayAdapter<String>(this,
                R.layout.autocomplete, TaskList)
        search_task.setThreshold(1) // will start working from

        search_task.setAdapter(adaptersearch) // setting the adapter



        search_task.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= search_task.getRight() - search_task.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()) {
                    val view: View? = this@ActivityTask.getCurrentFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    if (!search_task.getText().toString().equals("", ignoreCase = true)) {
                        search_task.setText("")
                        search_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0)
                    }
                    //autoCompleteTextView1.setText("");
                    search_task.showDropDown()
                    return@OnTouchListener true
                }
            }
            false
        })

        search_task.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (search_task.getText().toString().trim({ it <= ' ' }).length == 0) {
                    try {

                        adapter = TaskSwipeAdapter(this@ActivityTask, SwipeList)
                        swipeListView!!.adapter = adapter
                        adapter!!.setNotifyOnChange(true)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (search_task.getText().toString().trim({ it <= ' ' }).length == 0) {
                } else {
                    search_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_product, 0)
                }
            }
        })


        search_task.setOnItemClickListener(OnItemClickListener { parent, arg1, pos, id ->
//            Global_Data.hideSoftKeyboard(this@ActivityTask)
            val name: String = search_task.getText().toString()
            SwipeListSearch!!.clear()
            var getData = HashMap<String, String>()
            for (i in SwipeList!!.indices) {
                getData = SwipeList!!.get(i)
                if (name.equals(getData.get(TAG_Activity_name), ignoreCase = true)) {
                    val mapp = HashMap<String, String>()

                    if (Global_Data.PlannerName.equals("Task")) {
                        mapp[TAG_Code] = getData.get(TAG_Code).toString()
                        mapp[TAG_Activity_name] = getData.get(TAG_Activity_name).toString()
                        mapp[TAG_From] = getData.get(TAG_From).toString()
                        mapp[TAG_To] = getData.get(TAG_To).toString()
                        mapp[TAG_Description] = getData.get(TAG_Description).toString()
                        mapp[TAG_Location] = getData.get(TAG_Location).toString()
                        mapp[TAG_Reminder] = getData.get(TAG_Reminder).toString()
                        mapp[TAG_Type] = getData.get(TAG_Type).toString()
                    }
                    else
                    {
                        mapp[TAG_Code] = getData.get(TAG_Code).toString()
                        mapp[TAG_Activity_name] = getData.get(TAG_Activity_name).toString()
                        mapp[TAG_Description] = getData.get(TAG_Description).toString()
                        mapp[TAG_Created_at] = getData.get(TAG_Created_at).toString()

                    }


                    SwipeListSearch!!.add(mapp)

                    adapter = TaskSwipeAdapter(this@ActivityTask, SwipeListSearch)
                    swipeListView!!.adapter = adapter
                    adapter!!.setNotifyOnChange(true)

                    break
                }
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
                return true
            }
        }
        return when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f))
                    val achieved = Math.round(sp.getFloat("Achived", 0f))
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    targetNew = if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                            // todaysTarget.setText();
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
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

    public override fun onDestroy() {
        super.onDestroy()
        hidePDialog()

    }

    private fun hidePDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }


    override fun onBackPressed() { // TODO Auto-generated method stub
        val i = Intent(this@ActivityTask, ActivityPlanner::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }

    private fun reload() {
        val settings = SettingsManager.getInstance()
        swipeListView!!.setSwipeMode(settings.swipeMode)
        swipeListView!!.swipeActionLeft = settings.swipeActionLeft
        swipeListView!!.swipeActionRight = settings.swipeActionRight
        swipeListView!!.setOffsetLeft(Utils.convertDpToPixel(settings.swipeOffsetLeft))
        swipeListView!!.setOffsetRight(Utils.convertDpToPixel(settings.swipeOffsetRight))
        swipeListView!!.setAnimationTime(settings.swipeAnimationTime)
        swipeListView!!.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress)
    }

    companion object {
        const val TAG_Code = "code"
        const val TAG_Activity_name = "activity_name"
        const val TAG_From = "from"
        const val TAG_To = "to"
        const val TAG_Description = "description"
        const val TAG_Location = "location"
        const val TAG_Reminder = "reminder"
        const val TAG_Type = "type"
        const val TAG_Created_at = "created_at"
    }

    fun GetActivityPlannerData() {

        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
        // val shop_code: String = sp.getString("shopcode", null)

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            if (Global_Data.PlannerName.equals("Task")) {
                service_url = service_url + "activity_planners/get_activity_planner?email=" + URLEncoder.encode(user_email, "UTF-8")
            }  else {
                service_url = service_url + "notes/get_notes_details?email=" + URLEncoder.encode(user_email, "UTF-8")+"&type=user"
            }

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }


        Log.d("Server url", "Server url" + service_url)
        var stringRequest: StringRequest? = null
        stringRequest = StringRequest(service_url,
                Response.Listener { response ->
                    Log.d("jV", "JV length" + response.length)
                    try {
                        val json = JSONObject(JSONTokener(response))
                        try {
                            var response_result = ""
                            if (json.has("result")) {
                                response_result = json.getString("result")
                                dialog!!.hide()

                            } else {

                                if (Global_Data.PlannerName.equals("Task")) {
                                    val activities = json.getJSONArray("activities")

                                    Log.i("volley", "response activity_planner Length: " + activities.length())

                                    Log.d("users", "activity_planner$activities")

                                    for (i in 0 until activities.length()) {
                                        val jsonObject = activities.getJSONObject(i)
                                        val mapp = HashMap<String, String>()
                                        mapp[TAG_Code] = jsonObject.getString("code")
                                        mapp[TAG_Activity_name] = jsonObject.getString("name")
                                        mapp[TAG_From] = jsonObject.getString("from_date")
                                        mapp[TAG_To] = jsonObject.getString("to_date")
                                        mapp[TAG_Description] = jsonObject.getString("description")
                                        mapp[TAG_Location] = jsonObject.getString("location")
                                        mapp[TAG_Reminder] = jsonObject.getString("reminder_date")
                                        mapp[TAG_Type] = jsonObject.getString("planner_type")
                                        SwipeList!!.add(mapp)
                                        TaskList.add(jsonObject.getString("name"))


                                    }
                                } else {
                                    val activities = json.getJSONArray("notes")

                                    Log.i("volley", "response notes Length: " + activities.length())

                                    Log.d("users", "notes$activities")

                                    for (i in 0 until activities.length()) {
                                        val jsonObject = activities.getJSONObject(i)
                                        val mapp = HashMap<String, String>()
                                        mapp[TAG_Code] = jsonObject.getString("id")
                                        mapp[TAG_Activity_name] = jsonObject.getString("name")
                                        mapp[TAG_Description] = jsonObject.getString("description")
                                        mapp[TAG_Created_at] = jsonObject.getString("updated_at")

                                        // mapp[TAG_Type] = jsonObject.getString("schedule_date")

                                        SwipeList!!.add(mapp)
                                        TaskList.add(jsonObject.getString("name"))


                                    }
                                }


                                adapter = TaskSwipeAdapter(this@ActivityTask, SwipeList)
                                swipeListView!!.adapter = adapter
                                adapter!!.setNotifyOnChange(true)

                                dialog!!.dismiss()

                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            finish()
                            dialog!!.dismiss()
                        }
                        dialog!!.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        //  finish();
                        dialog!!.dismiss()
                    }
                    dialog!!.dismiss()
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) { //							Toast.makeText(Video_Main_List.this,
//									"Network Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error","yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(context)
        val socketTimeout = 300000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        // requestQueue.se
//requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false)
        requestQueue.cache.clear()
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest)
    }


}