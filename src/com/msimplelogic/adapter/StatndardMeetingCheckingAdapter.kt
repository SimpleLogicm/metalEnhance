package com.msimplelogic.adapter

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.DataBaseHelper
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.model.UpdateStockModel


class StatndardMeetingCheckingAdapter
(private val mContext: Context, private val catalogue_m: MutableList<UpdateStockModel>) : RecyclerView.Adapter<StatndardMeetingCheckingAdapter.MyViewHolder>() {
    var image_url = ""
    var spv: SharedPreferences? = null
    var customListner: customButtonListener? = null
    var q_check = ""
    var str: String? = null
    var dbvoc: DataBaseHelper? = null
    var updateStockModel: UpdateStockModel? = null
    var price_str: String? = null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var edit_value: EditText
        var ids: TextView

        init {

            edit_value = view.findViewById<EditText>(R.id.pro_statnda_edit)
            ids = view.findViewById<View>(R.id.ids) as TextView



        }
    }

    interface customButtonListener {
        fun onButtonClickListner(position: Int)
    }

    fun setCustomButtonListner(listener: customButtonListener?) {
        customListner = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.statndardmeetingcheckingadapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        updateStockModel = catalogue_m[position]

        holder.ids.text = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.id)
        holder.edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_name))

        Global_Data.Order_hashmap.clear()

        holder.edit_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val edit = HashMap<String, String>()
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.text.toString())) {

                    Global_Data.Order_hashmap.put(holder.ids.text.toString(),s.toString())

                } else {
                    // holder.stock_erroe.visibility = View.GONE
                    Global_Data.Order_hashmap[holder.ids.text.toString()] = ""
                }
            }
        })



    }

    override fun getItemCount(): Int {
        return catalogue_m.size
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }



}