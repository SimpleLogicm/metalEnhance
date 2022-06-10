package com.msimplelogic.adapter

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.DataBaseHelper
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.model.UpdateStockModel


class UpdateStockAdapter
(private val mContext: Context, private val catalogue_m: List<UpdateStockModel>) : RecyclerView.Adapter<UpdateStockAdapter.MyViewHolder>() {
    var image_url = ""
    var spv: SharedPreferences? = null
    var customListner: customButtonListener? = null
    var q_check = ""
    var str: String? = null
    var dbvoc: DataBaseHelper? = null
    var updateStockModel: UpdateStockModel? = null
    var price_str: String? = null
    private val p_id = ArrayList<String>()
    private val p_name = ArrayList<String>()
    private val p_mrp = ArrayList<String>()
    private val p_rp = ArrayList<String>()
    private val p_q = ArrayList<String>()
    private val p_price = ArrayList<String>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var plus_btn: ImageView
        var minus_btn: ImageView
        var t_title: TextView
        var mrpv: TextView
        var rpv: TextView
        var stock_product_id: TextView
        var stock_id: TextView
        var text_rp: TextView? = null
        var rp_catalogue: TextView
        var mrp_catalogue: TextView
        var stock_erroe: TextView
        var edit_value: EditText
        var s_disable_edit_value: EditText


        init {

            t_title = view.findViewById<View>(R.id.t_title) as TextView
            mrpv = view.findViewById<View>(R.id.mrpv) as TextView
            rpv = view.findViewById<View>(R.id.rpv) as TextView
            stock_product_id = view.findViewById<View>(R.id.stock_product_id) as TextView
            stock_id = view.findViewById<View>(R.id.stock_id) as TextView
            stock_erroe = view.findViewById<View>(R.id.stock_erroe) as TextView
            plus_btn = view.findViewById<View>(R.id.plus_btn) as ImageView
            minus_btn = view.findViewById<View>(R.id.minus_btn) as ImageView
            edit_value = view.findViewById<View>(R.id.u_stock_edit_value) as EditText
            s_disable_edit_value = view.findViewById<View>(R.id.s_disable_edit_value) as EditText
            rp_catalogue = view.findViewById<View>(R.id.rp_catalogue) as TextView
            mrp_catalogue = view.findViewById<View>(R.id.mrp_catalogue) as TextView

            spv = mContext.getSharedPreferences("valuesset", 0)
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
                .inflate(R.layout.updatestockadapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        updateStockModel = catalogue_m[position]
        holder.t_title.text = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_name)
        holder.mrpv.text = updateStockModel?.item_mrp.toString()
        holder.rpv.text = updateStockModel?.item_rp.toString()
        holder.stock_product_id.text = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_number)
        holder.stock_id.text = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.id)
        holder.s_disable_edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_in_stock))
        val itemQty = Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_current_stock)
        holder.s_disable_edit_value.setText(itemQty)
        holder.edit_value.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(updateStockModel?.item_in_stock))



        val spf1 = mContext.getSharedPreferences("SimpleLogic", 0)
        val rpstr = spf1.getString("var_rp", "")
        val mrpstr = spf1.getString("var_mrp", "")
        if (rpstr!!.length > 0) {
            holder.rp_catalogue.text = "$rpstr : "
        } else {
            holder.rp_catalogue.text = mContext.resources.getString(R.string.RP)
        }
        if (mrpstr!!.length > 0) {
            holder.mrp_catalogue.text = "$mrpstr : "
        } else {
            holder.mrp_catalogue.text = mContext.resources.getString(R.string.MRP)
        }



        holder.plus_btn.setOnClickListener {
            if (holder.edit_value.text.toString().equals("", ignoreCase = true)) {
                holder.edit_value.setText(1.toString())
            } else {
                val s = holder.edit_value.text.toString().toInt() + 1
                if (s <= 9999) {
                    holder.edit_value.setText((holder.edit_value.text.toString().toInt() + 1).toString())
                }
            }
        }
        holder.minus_btn.setOnClickListener {
            if (!holder.edit_value.text.toString().equals("", ignoreCase = true) && holder.edit_value.text.toString().toInt() > 0) {
                holder.edit_value.setText((holder.edit_value.text.toString().toInt() - 1).toString())
            }
        }


        holder.edit_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val edit = HashMap<String, String>()
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_value.text.toString())) {

//                    if(holder.s_disable_edit_value.text.toString() >=  holder.edit_value.text.toString())
//                    {
                        val editor = spv!!.edit()
                        val str = holder.edit_value.text.toString()
                        editor.putString("value", str)
                        editor.commit()

                        Global_Data.Order_hashmap.put(position.toString() + "&" + holder.stock_product_id.text.toString()+ "@" + holder.stock_id.text.toString(),s.toString())
                       // holder.stock_erroe.visibility = View.GONE
//                    }
//                    else
//                    {
//                        holder.edit_value.setText("")
//                        Global_Data.Order_hashmap[position.toString() + "&" + holder.stock_product_id.text.toString()+ "@" + holder.stock_id.text.toString()] = ""
//                        holder.stock_erroe.visibility = View.VISIBLE
//                        holder.stock_erroe.setText(mContext.resources.getString(R.string.Stock_Val))
//
//                    }



                } else {
                   // holder.stock_erroe.visibility = View.GONE
                    Global_Data.Order_hashmap[position.toString() + "&" + holder.stock_product_id.text.toString()+ "@" + holder.stock_id.text.toString()] = ""
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