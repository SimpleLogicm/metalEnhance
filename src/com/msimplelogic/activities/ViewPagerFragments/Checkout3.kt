package com.msimplelogic.activities.ViewPagerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.msimplelogic.activities.Global_Data

import com.msimplelogic.activities.R
import kotlinx.android.synthetic.main.check_out_page3.*
import kotlinx.android.synthetic.main.check_out_page3.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Checkout3.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Checkout3.newInstance] factory method to
 * create an instance of this fragment.
 */
class Checkout3 : Fragment() {
    var tv_btn_clear: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.check_out_page3, container, false)
        view.tv_btn_clear.findViewById<TextView>(R.id.tv_btn_clear)

        view.tv_btn_clear.setOnTouchListener(View.OnTouchListener { b, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                return@OnTouchListener true
            }
            if (event.action == MotionEvent.ACTION_DOWN) {
                signature_pad.clear()

                Toast.makeText(activity, Global_Data.PlannerName, Toast.LENGTH_LONG).show()
            }
            false
        })

        return view
    }


}
