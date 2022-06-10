package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.model.UserObject
import com.msimplelogic.webservice.ConnectionDetector
import cpm.simplelogic.helper.CheckNullValue
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

class ChangePasswordActivity() : BaseActivity() {
    var oldPassword: EditText? = null
    var newPassword: EditText? = null
    var confirmPassword: EditText? = null
    var dialog: ProgressDialog? = null
    var btnOk: Button? = null
    var btnCancel: Button? = null
    var cd: ConnectionDetector? = null
    private var dbvoc: DataBaseHelper? = null
    var isInternetPresent = false
    private var passwordNotVisible = 1
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepassword_main)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //setTitle("")

        dbvoc = DataBaseHelper(this)
        cd = ConnectionDetector(applicationContext)
        isInternetPresent = cd!!.isConnectingToInternet
        oldPassword = findViewById<View>(R.id.old_password) as EditText
        newPassword = findViewById<View>(R.id.new_password) as EditText
        confirmPassword = findViewById<View>(R.id.confirm_passwrod) as EditText
        btnOk = findViewById<View>(R.id.button_ok) as Button
        btnCancel = findViewById<View>(R.id.button_cancel) as Button
        oldPassword!!.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (oldPassword!!.right - oldPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val view = this@ChangePasswordActivity.currentFocus
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    //autoCompleteTextView1.setText("");
                    if (passwordNotVisible == 1) {
                        oldPassword!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordNotVisible = 0
                        oldPassword!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0)
                    } else {
                        oldPassword!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordNotVisible = 1
                        oldPassword!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0)
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
        newPassword!!.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (newPassword!!.right - newPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        val view = this@ChangePasswordActivity.currentFocus
                        if (view != null) {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        //autoCompleteTextView1.setText("");
                        if (passwordNotVisible == 1) {
                            newPassword!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            passwordNotVisible = 0
                            newPassword!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0)
                        } else {
                            newPassword!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            passwordNotVisible = 1
                            newPassword!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0)
                        }
                        return true
                    }
                }
                return false
            }
        })
        confirmPassword!!.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (confirmPassword!!.right - confirmPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        val view = this@ChangePasswordActivity.currentFocus
                        if (view != null) {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        //autoCompleteTextView1.setText("");
                        if (passwordNotVisible == 1) {
                            confirmPassword!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            passwordNotVisible = 0
                            confirmPassword!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0)
                        } else {
                            confirmPassword!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            passwordNotVisible = 1
                            confirmPassword!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0)
                        }
                        return true
                    }
                }
                return false
            }
        })
        btnOk!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (isInternetPresent) {
                    if (CheckNullValue.findNullValue(oldPassword!!.text.toString().trim { it <= ' ' }) == true) { // Toast.makeText(LoginActivity.this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
//                        val toast = Toast.makeText(this@ChangePasswordActivity, "Please Enter Old Password", Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@ChangePasswordActivity, "Please Enter Old Password","yes")
                    } else if (CheckNullValue.findNullValue(newPassword!!.text.toString().trim { it <= ' ' }) == true) { // Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
//                        val toast = Toast.makeText(this@ChangePasswordActivity, "Please Enter New Password", Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@ChangePasswordActivity, "Please Enter New Password","yes")
                    } else if (CheckNullValue.findNullValue(confirmPassword!!.text.toString().trim { it <= ' ' }) == true) { // Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
//                        val toast = Toast.makeText(this@ChangePasswordActivity, "Please Enter Confirm Password", Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@ChangePasswordActivity, "Please Enter Confirm Password","yes")
                    } else {
                        dialog = ProgressDialog(this@ChangePasswordActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
                        dialog!!.setTitle(resources.getString(R.string.app_name))
                        dialog!!.setCancelable(false)
                        dialog!!.show()
                        if (newPassword!!.length() > 7) {
                            if (newPassword!!.text.toString().trim { it <= ' ' }.equals(confirmPassword!!.text.toString().trim { it <= ' ' }, ignoreCase = true)) {
                                ChangePassword()
                            } else {
//                                val toast = Toast.makeText(this@ChangePasswordActivity, "New password and confirm password not matched", Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(this@ChangePasswordActivity, "New password and confirm password not matched","yes")
                                dialog!!.dismiss()
                            }
                        } else {
//                            val toast = Toast.makeText(this@ChangePasswordActivity, "Password should be minimum 8 characters", Toast.LENGTH_LONG)
//                            toast.setGravity(Gravity.CENTER, 0, 0)
//                            toast.show()
                            Global_Data.Custom_Toast(this@ChangePasswordActivity, "Password should be minimum 8 characters","yes")
                            dialog!!.dismiss()
                        }
                    }
                    //        if (oldPassword.length() > 0) {
//
//
//
//        }
//        else {
//            Toast toast = Toast.makeText(ChangePasswordActivity.this, "Please Enter Old Password", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//            dialog.dismiss();
//        }
                } else {
//                    val toast = Toast.makeText(this@ChangePasswordActivity, "Please Check Internet Connection", Toast.LENGTH_LONG)
//                    toast.setGravity(Gravity.CENTER, 0, 0)
//                    toast.show()
                    Global_Data.Custom_Toast(this@ChangePasswordActivity, "Please Check Internet Connection","yes")
                    //dialog.dismiss();
                }
            }
        })
        btnCancel!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val a = Intent(this@ChangePasswordActivity, MainActivity::class.java)
                startActivity(a)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.msimplelogic.activities.R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            com.msimplelogic.activities.R.id.add -> {
                var targetNew = ""
                val sp = this@ChangePasswordActivity.getSharedPreferences("SimpleLogic", 0)
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

                val view: View = findViewById(com.msimplelogic.activities.R.id.add)
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
        val i = Intent(this@ChangePasswordActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    fun ChangePassword() {
        val spf = getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)
        val domain = resources.getString(R.string.service_domain)
        val queue = Volley.newRequestQueue(this@ChangePasswordActivity)
        val url = domain + "users/change_passowd?&email=" + user_email
        Log.i("url", url+" ");
        val strRequest: StringRequest = object : StringRequest(Method.POST, url,
                object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        var json: JSONObject? = null
                        try {
                            json = JSONObject(JSONTokener(response))
                            var response_result = ""
                            if (json.has("result")) {
                                response_result = json.getString("result")
                            } else {
                                response_result = "data"
                            }
                            if (response_result.equals("The old password you have entered is incorrect", ignoreCase = true)) {
//                                val toast = Toast.makeText(this@ChangePasswordActivity, response_result, Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(this@ChangePasswordActivity, response_result, "yes")
                                dialog!!.dismiss()
                                //                                    Intent a = new Intent(ChangePasswordActivity.this,MainActivity.class);
//                                    startActivity(a);
//                                    finish();
                            } else {
                                val spf = getSharedPreferences("SimpleLogic", 0)
                                val user_email = spf.getString("USER_EMAIL", null)
                                val userNameSpf = getSharedPreferences("SimpleLogic", 0)
                                val userName = userNameSpf.getString("USER_NAME", null)
                                //                            String userPwd=json.getString("password");
//                             System.out.println(userPwd);
                                dbvoc!!.update_user_password(json.getString("password"), user_email)
                                val gson = (application as AppController).gsonObject
                                val userData = UserObject(Global_Data.imei_no, userName, newPassword!!.text.toString().trim { it <= ' ' })
                                val userDataString = gson.toJson(userData)
                                val preff = (application as AppController).shared
                                preff.userData = userDataString
//                                val toast = Toast.makeText(this@ChangePasswordActivity, response_result, Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(this@ChangePasswordActivity, "Password changed successfully","yes")
                                val a = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                                startActivity(a)
                                finish()
                                dialog!!.dismiss()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                       // Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                        Global_Data.Custom_Toast(this@ChangePasswordActivity, error.toString(),"")
                    }
                }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["old_password"] = oldPassword!!.text.toString().trim { it <= ' ' }
                params["new_password"] = newPassword!!.text.toString().trim { it <= ' ' }
                params["confirm_password"] = confirmPassword!!.text.toString().trim { it <= ' ' }
                return params
            }
        }
        queue.add(strRequest)
    }
}