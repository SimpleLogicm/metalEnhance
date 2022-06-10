package com.msimplelogic.activities.kotlinFiles.kotGlobal

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.msimplelogic.model.Order_notes_model
import com.msimplelogic.model.Visit_Schedule_model


class Kot_Gloval {

    companion object {

        var stock_local_image_flag = "";
        var stock_status = "";
        var stock_details = "";
        var stock_pick1 = "";
        var stock__pick2 = "";
        var stock__pick3 = "";
        var activity_flag = "";
        var listfull: ArrayList<Visit_Schedule_model>? = null
        var listfullnotes: ArrayList<Order_notes_model>? = null
        var list_customers = java.util.ArrayList<String>()
        var Listresults = java.util.ArrayList<String>()
        var ListresultsHashMap:HashMap<String,String> = HashMap<String,String>()

        fun getPaddedNumber(number: Int): String {
            return String.format("%02d", number);
        }

        fun hideKeyboard(context: Context,view: View) {
            if (context != null) {
                val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager?.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }
    }

}