package com.msimplelogic.activities.ViewPagerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.msimplelogic.activities.R
import kotlinx.android.synthetic.main.check_out_page2.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Checkout2.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Checkout2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Checkout2 : Fragment() {

    var btn_Cash: TextView? = null
    var Btn_Cheque: TextView? = null
    var Btn_neft: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.check_out_page2, container, false)
//        return inflater.inflate(R.layout.check_out_page2, container, false)

        view.btn_Cash?.findViewById<TextView>(R.id.btn_Cash)
        view.Btn_Cheque?.findViewById<TextView>(R.id.Btn_Cheque)
        view.Btn_neft?.findViewById<TextView>(R.id.Btn_neft)

        view.btn_Cash?.setOnClickListener {
            view.btn_Cash?.setBackgroundResource(R.drawable.card_border_black)
            view.Btn_neft?.setBackgroundResource(android.R.color.transparent)
            view.Btn_Cheque?.setBackgroundResource(android.R.color.transparent)
//            Global_Data.Custom_Toast(view.context,"Clicked","")

        }
        view.Btn_Cheque?.setOnClickListener {
            view.Btn_Cheque?.setBackgroundResource(R.drawable.card_border_black)
            view.btn_Cash?.setBackgroundResource(android.R.color.transparent)
            view.Btn_neft?.setBackgroundResource(android.R.color.transparent)


        }
        view.Btn_neft?.setOnClickListener {
            view.Btn_neft?.setBackgroundResource(R.drawable.card_border_black)
            view.btn_Cash?.setBackgroundResource(android.R.color.transparent)
            view.Btn_Cheque?.setBackgroundResource(android.R.color.transparent)

        }
        return view

    }


}
