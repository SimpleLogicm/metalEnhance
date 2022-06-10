package com.msimplelogic.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.DataBaseHelper
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.model.UpdateStockModel
import java.util.*
import kotlin.collections.HashMap


class PromotionalMeeetingAdapter
(private val mContext: Context, private val catalogue_m: MutableList<UpdateStockModel>) : RecyclerView.Adapter<PromotionalMeeetingAdapter.MyViewHolder>() {
    var image_url = ""
    var spv: SharedPreferences? = null
    var customListner: customButtonListener? = null
    var q_check = ""
    var str: String? = null
    var dbvoc: DataBaseHelper? = null
    var updateStockModel: UpdateStockModel? = null
    var price_str: String? = null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var plus_btn: ImageView
        var minus_btn: ImageView
        var edit_value: AutoCompleteTextView
        var ids: TextView

        init {
            plus_btn = view.findViewById<View>(R.id.add) as ImageView
            minus_btn = view.findViewById<View>(R.id.deletel) as ImageView
            edit_value = view.findViewById<View>(R.id.m_edit_value) as AutoCompleteTextView
          //  edit_value.setBackgroundColor(R.color.black)
            ids = view.findViewById<View>(R.id.ids) as TextView



            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(mContext,
                    R.layout.text_custom_view,
                    Kot_Gloval.list_customers)


            edit_value.setThreshold(1)
            edit_value.setAdapter(adapter)
            //edit_value.setTextColor(Color.BLACK);
         //   autoCompleteTextView1.setTextColor(Color.BLACK);

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
                .inflate(R.layout.promotionalmeeetingadapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        updateStockModel = catalogue_m[position]

        holder.ids.text = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.id)
        holder.edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_name))


        holder.plus_btn.setOnClickListener {

            val random = Random()
            val value: Int = 16 + random.nextInt(5)
            val randomPIN = System.currentTimeMillis()
            val PINString = randomPIN.toString()
            catalogue_m.add(catalogue_m.size - 1, UpdateStockModel("YES"+PINString+value, "", "", "", "", "", ""))
            notifyItemInserted(catalogue_m.size - 1);

            notifyDataSetChanged()


        }
        holder.minus_btn.setOnClickListener {

            //list.remove(index);
            if (catalogue_m.size > 1)
            {
                Global_Data.Order_hashmap[holder.ids.text.toString()] = ""
                catalogue_m.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, catalogue_m.size)
            }

        }


        holder.edit_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val edit = HashMap<String, String>()
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.text.toString())) {

                    Global_Data.Order_hashmap.put(holder.ids.text.toString(),s.toString())
                    catalogue_m[position].item_name = s.toString()

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