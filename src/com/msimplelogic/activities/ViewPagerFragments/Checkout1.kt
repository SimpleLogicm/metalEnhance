package com.msimplelogic.activities.ViewPagerFragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.DataBaseHelper
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import kotlinx.android.synthetic.main.check_out_page1.view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Checkout1.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Checkout1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Checkout1 : Fragment() {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    var dates:String?=null
    var cc_code:String?=null
    var distributorname:TextView?=null
    var order_type:Spinner?=null
    var order_detail1:EditText?=null
    var detail1str:String?=null
    var yourName : EditText?=null
    var beat_id:String? = null

    var dataAdapter_order_type: ArrayAdapter<String>? = null
    internal var list_order_type: List<String>? = null
    private val results_order_type = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.check_out_page1, container, false)

        view.distributorname?.findViewById<TextView>(R.id.distributorname)
        view.order_detail1?.findViewById<EditText>(R.id.order_detail1)
        view.order_type?.findViewById<Spinner>(R.id.order_type)
        view.yourName?.findViewById<EditText>(R.id.yourName)

         var dbvoc = DataBaseHelper(activity)
        results_order_type.clear()
        val contacts1 = dbvoc.getorder_category()
        results_order_type.add(resources.getString(R.string.Select_Order_Type))
        for (cn in contacts1) {
            if (!cn.getOrder_type_name().equals("", ignoreCase = true) && !cn.getOrder_type_name().equals(" ", ignoreCase = true)) {
                val str_categ = "" + cn.getOrder_type_name()
                results_order_type.add(str_categ)
            }
        }

        dataAdapter_order_type = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, results_order_type)
        dataAdapter_order_type!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.order_type?.adapter = dataAdapter_order_type

        val getbeatid = dbvoc.GetOrders_beatID(Global_Data.GLOvel_GORDER_ID)

        for (cn in getbeatid) {
            beat_id = cn.beaT_ID
        }

        val getdistri_code = dbvoc.getDistributors_code(beat_id)
        for (cn in getdistri_code) {
           var dis_id = cn.distributeR_ID
            val contacts1 = dbvoc.getDistributors_BYID(dis_id)
            for (cnn in contacts1) {
                if (!cnn.stateName.equals("", ignoreCase = true) && !cnn.stateName.equals(" ", ignoreCase = true)) {
                    val str_dis = "" + cnn.stateName
                    view.distributorname.text=str_dis
                }
            }
        }

        val spf1 = activity?.getSharedPreferences("SimpleLogic", 0)
        detail1str = spf1?.getString("var_detail1", "")

        if (detail1str?.length!! > 0) {
            view.order_detail1?.setHint(detail1str)
        } else {
            view.order_detail1.setHint(resources.getString(R.string.Detail1))
        }

        val spf3 = activity?.getSharedPreferences("SimpleLogic", 0)
        val strdetail1_edit = spf3?.getString("var_detail1_edit", "")
        val spf5 =activity?.getSharedPreferences("SimpleLogic", 0)
        val strdetail1_allow = spf5?.getString("var_detail1_allow", "")


        if (strdetail1_edit.equals("true", ignoreCase = true)) {
            view.order_detail1.setEnabled(true)

            if (strdetail1_allow.equals("Text", ignoreCase = true)) {
                view.order_detail1.setInputType(InputType.TYPE_CLASS_TEXT)
            } else if (strdetail1_allow.equals("Integer", ignoreCase = true)) {
                view.order_detail1.setInputType(InputType.TYPE_CLASS_NUMBER)
            } else {
                view.order_detail1.setFocusableInTouchMode(false)
                view.order_detail1.setOnClickListener(View.OnClickListener {


                        val now = Calendar.getInstance()
                        DatePickerDialog(
                                activity!!,
                                DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                                    var mo = month
                                    ++mo
                                    Log.d("Orignal", mo.toString())
                                    var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;

                                    view.order_detail1.setText(date_from)

                                },
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        ).show()

                })


                val contacts = dbvoc.GetOrders_details("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID)

                if (contacts.size > 0) {
                    for (cn in contacts) {
                       view.yourName.setText(cn.order_detail3)
                        view.order_detail1.setText(cn.order_detail1)
                       // order_detail2.setText(cn.order_detail2)
                        cc_code = cn.order_category_type.trim { it <= ' ' }
                    }
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cc_code)) {
                    val cont = dbvoc.get_order_category_name(cc_code)

                    for (cn in cont) {
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.order_type_name.trim { it <= ' ' })) {
                            val spinnerPosition = dataAdapter_order_type?.getPosition(cn.order_type_name.trim { it <= ' ' })
                            view.order_type?.setSelection(spinnerPosition!!)


                        }
                    }
                }


                if (view.order_type != null) {
                    view.order_type.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>,
                                                    view: View, position: Int, id: Long) {

                            val Text: String = view.order_type?.getSelectedItem().toString()
                            Toast.makeText(activity,
                                    Text, Toast.LENGTH_SHORT).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                }

//                val Text: String = view.order_type?.getSelectedItem().toString()
//                Global_Data.PlannerName = Text

            }
        }

        return view

    }





}
