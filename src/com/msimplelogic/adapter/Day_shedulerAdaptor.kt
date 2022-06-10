package com.msimplelogic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.msimplelogic.activities.*
import com.msimplelogic.activities.kotlinFiles.Day_sheduler_details
import com.msimplelogic.activities.kotlinFiles.Neworderoptions
import com.msimplelogic.model.Day_shedulerModel
import androidx.core.content.ContextCompat.startActivity
import android.net.Uri
import com.msimplelogic.service.LocationServices.longitude
import com.msimplelogic.service.LocationServices.latitude
import java.util.Locale
import android.location.Geocoder





class Day_shedulerAdaptor (context:Context,private val array:ArrayList<Day_shedulerModel>): androidx.recyclerview.widget.RecyclerView.Adapter<Day_shedulerAdaptor.ViewHolder>() {
var  context: Context = context
    private val inflater: LayoutInflater
    var lattitude:Double?=null
    var longitude:Double?=null
    var dbvoc = DataBaseHelper(context)
    init {
        inflater = LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.raw_daysheduler, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    @SuppressLint("ShowToast")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.shopname.setText(array[position].shopname)
        holder.time_tv.setText(array[position].time)
        holder.distance_tv.setText(array[position].distance)
        Glide.with(context)
                .load(array[position].image)
                .placeholder(R.drawable.download)
                .error(R.drawable.download)
                .into(holder.img_shop)

        holder.map.setOnClickListener {

        }

        holder.call.setOnClickListener {
            if(array[position].call.equals("null"))
            {
                Global_Data.Custom_Toast(context,"Number Not Available","yes")


            }else{
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + array.get(position).call))
                context.startActivity(intent)
            }

        }

holder.map.setOnClickListener {
//    lattitude= array[position].lat?.toDouble()
//    longitude= array[position].long?.toDouble()
//    try {
//        val geo = Geocoder(context.getApplicationContext(), Locale.getDefault())
//        val addresses = geo.getFromLocation(lattitude!!, longitude!!, 1)
//        if (addresses.isEmpty()) {
//            //  yourtextfieldname.setText("Waiting for Location")
//        } else {
//            if (addresses.size > 0) {
//             //   var str =(addresses[0].featureName + ", " + addresses[0].locality + ", " + addresses[0].adminArea + ", " + addresses[0].countryName)
//                var str =(addresses[0].getAddressLine(0))
//               // Toast.makeText(context, "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
//
//
//            }else
//            {
//                Global_Data.Custom_Toast(context,"Address Not Found","yes")
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace() // getFromLocation() may sometimes fail
//    }

    try {
        var latitude = array[position].lat
        var longitude = array[position].long

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(latitude) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(longitude)) {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude))
            context.startActivity(intent)
        }
        else
        {
            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.latlong_not_foundnew), "Yes")
        }



    } catch (e: Exception) {
        e.printStackTrace()
    }

}


    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var time_tv: TextView
        lateinit  var shopname: TextView
        lateinit var distance_tv: TextView
        lateinit  var img_shop: ImageView
        lateinit var contents : LinearLayout
        lateinit var map : ImageView
        lateinit var call : ImageView

        init {

            time_tv = itemView.findViewById(R.id.time_tv) as TextView
            shopname = itemView.findViewById(R.id.shopname) as TextView
            distance_tv = itemView.findViewById(R.id.distance_tv) as TextView
            img_shop = itemView.findViewById(R.id.img_shop) as ImageView
            call = itemView.findViewById(R.id.call) as ImageView
            map = itemView.findViewById(R.id.map) as ImageView
            contents = itemView.findViewById(R.id.contents) as LinearLayout

            itemView.setOnClickListener {

                val sp: SharedPreferences = context.getApplicationContext().getSharedPreferences("SimpleLogic", 0)
                val spreedit: SharedPreferences.Editor = sp.edit()

                val shopName: String? = array[position].shopname
                var custCode: String?=null

                spreedit.putString("shopname",shopName)

                val cont1 : List<Local_Data> = dbvoc.getCustDetails(shopName)
                if (cont1.size > 0) {
                    for (cnt1 in cont1) {
                        try {
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.cust_Code)) {
                                custCode =cnt1.cust_Code
                                spreedit.putString("shopcode",cnt1.cust_Code)
                            }
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1._custadr)) {
                                spreedit.putString("shopadd",cnt1._custadr)
                            }
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.cust_mob)) {
                                spreedit.putString("c_mobile_number",cnt1.cust_mob)
                            }
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.landlinE_NO)) {
                                  spreedit.putString("customer_landline",cnt1.landlinE_NO)
                            }
                           }catch (e:Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }

                val contactlimit = dbvoc.getCreditprofileData(custCode)
                if (contactlimit.size > 0) {
                    for (cnn in contactlimit) {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn._credit_limit)) {
                            val credit_limit = java.lang.Double.valueOf(cnn._credit_limit)
                            spreedit.putString("c_credit_profile", credit_limit.toString())

                        } else{
                            spreedit.putString("c_credit_profile", "0.0")
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn._shedule_outstanding_amount)) {
                            Global_Data.amt_outstanding = java.lang.Double.valueOf(cnn._shedule_outstanding_amount)
                            spreedit.putString("c_outstanding", Global_Data.amt_outstanding.toString())

                        } else {
                            spreedit.putString("c_outstanding", "0.0")
                           // items.outstanding = 0.0
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.ammount_overdue)) {
                            val amt_overdue = java.lang.Double.valueOf(cnn.ammount_overdue)
                            spreedit.putString("c_overdue",amt_overdue.toString())

                        } else {
                            spreedit.putString("c_overdue","0.0")
                        }
                    }
                } else {
                           spreedit.putString("c_credit_profile", "0.0")
                           spreedit.putString("c_outstanding", "0.0")
                           spreedit.putString("c_overdue","0.0")
                       }


                spreedit.commit()



                val i = Intent(context, Neworderoptions::class.java)
              //  i.putExtra("distance",array[position].distance)
                Global_Data.dayscheduleback="yes"
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                context.startActivity(i)

//                val toast = Toast.makeText(context, ""+array[position].shopname, Toast.LENGTH_SHORT)
//                toast.show()
            }
        }
    }
}