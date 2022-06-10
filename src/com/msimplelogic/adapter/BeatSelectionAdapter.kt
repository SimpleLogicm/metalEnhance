package com.msimplelogic.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.DataClass.BeatDataModel
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.Order_CustomerList
import java.util.*

class BeatSelectionAdapter(ctx: Context, private val imageModelArrayList: ArrayList<BeatDataModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<BeatSelectionAdapter.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeatSelectionAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.beatselectionadapter, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeatSelectionAdapter.MyViewHolder, position: Int) {

        holder.time.setText(imageModelArrayList[position].name)
        holder.beat_code.setText(imageModelArrayList[position].code)

        holder.time.setOnClickListener { view ->
            val sp: SharedPreferences = context.getApplicationContext().getSharedPreferences("SimpleLogic", 0)
            val spreedit: SharedPreferences.Editor = sp.edit()
            spreedit.putString("Beat_NAme",imageModelArrayList[position].name)
            spreedit.putString("Beat_Code",imageModelArrayList[position].code)
            spreedit.commit()
            val intent = Intent(context,Order_CustomerList::class.java)
//            intent.putExtra("Beat_NAme",imageModelArrayList[position].name)
//            intent.putExtra("Beat_Code",imageModelArrayList[position].code)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var time: TextView
        var beat_code: TextView

        init {

            time = itemView.findViewById(R.id.tv) as TextView
            beat_code = itemView.findViewById(R.id.beat_code) as TextView

        }

    }
}