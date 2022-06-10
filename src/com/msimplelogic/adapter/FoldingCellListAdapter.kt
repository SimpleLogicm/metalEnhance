package com.msimplelogic.adapter


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.Sales_Dash
import com.msimplelogic.model.Customer_Model
import com.ramotion.foldingcell.FoldingCell


/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
class FoldingCellListAdapter(context: Context?, objects: List<Customer_Model?>?) : ArrayAdapter<Customer_Model?>(context!!, 0, objects!!) {
    private val unfoldedIndexes = HashSet<Int>()
    var defaultRequestBtnClickListener: View.OnClickListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View { // get item for selected view
        val item = getItem(position)
        // if cell is exists - reuse it, if not - create the new one from resource
        var cell = convertView as FoldingCell?
        val viewHolder: ViewHolder
        if (cell == null) {
            viewHolder = ViewHolder()
            val vi = LayoutInflater.from(context)
            cell = vi.inflate(R.layout.cell, parent, false) as FoldingCell
            viewHolder.shopname = cell.findViewById(R.id.shop_name)
            viewHolder.c_code = cell.findViewById(R.id.c_code)
            viewHolder.s_name = cell.findViewById(R.id.s_name)
            viewHolder.s_address = cell.findViewById(R.id.s_address)
            viewHolder.c_mobile_number = cell.findViewById(R.id.c_mobile_number)
            viewHolder.customer_landline = cell.findViewById(R.id.customer_landline)
            viewHolder.c_credit_profile = cell.findViewById(R.id.c_credit_profile)
            viewHolder.c_outstanding = cell.findViewById(R.id.c_outstanding)
            viewHolder.c_overdue = cell.findViewById(R.id.c_overdue)
            viewHolder.btn_neworder = cell.findViewById(R.id.btn_neworder)


            cell.tag = viewHolder
        } else { // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true)
            } else {
                cell.fold(true)
            }
            viewHolder = cell.tag as ViewHolder
        }
        if (null == item) return cell
        // bind data from selected element to view through view holder
        viewHolder.shopname?.text = item.name
        viewHolder.c_code?.text = item.code.toString()
        viewHolder.s_name?.text = item.name
        viewHolder.s_address?.text = item.address
        if (item.mobile == null) {
            viewHolder.c_mobile_number?.setText("Number not available")
        } else if (item.mobile==" ") {
            viewHolder.c_mobile_number?.setText("Number not available")

        } else {
            viewHolder.c_mobile_number?.text = item.mobile
        }
        var str =item.landline
        if (str==" ") {

            viewHolder.customer_landline?.setText("Number not available")

        } else if(str==""){
            viewHolder.customer_landline?.setText("Number not available")
        }

        else if (item.landline == null) {

            viewHolder.customer_landline?.setText("Number not available")

        } else {
            viewHolder.customer_landline?.text = item.landline
        }
        viewHolder.c_credit_profile?.text = item.credit_profile.toString()
        viewHolder.c_outstanding?.text = item.outstanding.toString()
        viewHolder.c_overdue?.text = item.overdue.toString()

        Global_Data.GLOvel_CUSTOMER_ID = item.code.toString()


        viewHolder.btn_neworder?.setOnClickListener(View.OnClickListener {
            val sp: SharedPreferences = context.getApplicationContext().getSharedPreferences("SimpleLogic", 0)
            val spreedit: SharedPreferences.Editor = sp.edit()
            spreedit.putString("shopname", viewHolder.shopname!!.text.toString())
            Global_Data.customernaaaame=viewHolder.shopname!!.text.toString()
            spreedit.putString("shopadd", viewHolder.s_address!!.text.toString())
            spreedit.putString("shopcode", item.code)
            spreedit.putString("c_mobile_number", viewHolder.c_mobile_number!!.text.toString())
            spreedit.putString("customer_landline", viewHolder.customer_landline!!.text.toString())
            spreedit.putString("c_credit_profile", item.credit_profile.toString())
            spreedit.putString("c_outstanding", item.outstanding.toString())
            spreedit.putString("c_overdue", item.overdue.toString())
            spreedit.putString("c_address", item.address.toString())
            spreedit.commit()
            val intent = Intent(context, Sales_Dash::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        })


        return cell
    }

    // simple methods for register cell state changes
    fun registerToggle(position: Int) {
        if (unfoldedIndexes.contains(position)) registerFold(position) else registerUnfold(position)
    }

    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }

    // View lookup cache
    private class ViewHolder {
        var shopname: TextView? = null
        var c_code: TextView? = null
        var s_name: TextView? = null
        var c_mobile_number: TextView? = null
        var customer_landline: TextView? = null
        var c_credit_profile: TextView? = null
        var c_outstanding: TextView? = null
        var c_overdue: TextView? = null
        var s_address: TextView? = null
        var btn_neworder: ImageView? = null
        //   var fromAddress: TextView? = null
//        var toAddress: TextView? = null
//        var requestsCount: TextView? = null
//        var date: TextView? = null
//        var time: TextView? = null
    }
}