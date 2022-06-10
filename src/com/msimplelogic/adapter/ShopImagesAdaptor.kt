package com.msimplelogic.adapter

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.msimplelogic.activities.R
import com.msimplelogic.model.ShopImagesModel

class ShopImagesAdaptor (context: Context, private val array: ArrayList<ShopImagesModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<ShopImagesAdaptor.ViewHolder>()  {

    var context: Context = context
    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopImagesAdaptor.ViewHolder {
        val view2 = inflater.inflate(R.layout.row_shopimage_adaptor, parent, false)

        return ViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.tv_times!!.setText(array[position].time)
//        holder.tv_dates!!.setText(array[position].date)
        holder.Img?.let {
            Glide.with(context)
                .load(array[position].Imagepath)
                .into(it)
        }

        holder.Img!!.setOnClickListener {

            image_zoom_dialog(array[position].Imagepath)
        }
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var Img:ImageView?=null
//        var tv_dates:TextView?=null


        init {
            Img= itemView.findViewById(R.id.img_shop)
//            tv_times= itemView.findViewById(R.id.tv_times)


        }
    }

    fun image_zoom_dialog(hm_url: String?) {
        var dialogcustom: Dialog? = Dialog(context)
        dialogcustom!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogcustom.getWindow()!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        dialogcustom.setContentView(R.layout.collection_image_dialog)
        val Collection_zoom_image = dialogcustom.findViewById(R.id.Collection_zoom_image) as ImageView
        //        Glide.with(context).load(hm_url)
//                .placeholder(R.drawable.loa)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(Collection_zoom_image);
        Glide.with(context).load(hm_url)
                .thumbnail(Glide.with(context).load("file:///android_asset/loading.gif"))
                .fitCenter() // .crossFade()
                .into(Collection_zoom_image)
        val collection_zoom_delete = dialogcustom.findViewById(R.id.collection_zoom_delete) as Button
        val collection_zoom_ok = dialogcustom.findViewById(R.id.collection_zoom_ok) as Button
        collection_zoom_delete.visibility = View.GONE
//        collection_zoom_delete.setOnClickListener {
//            val file = File(hm_url)
//            if (file.exists()) {
//                file.delete()
//            }
//
//            dialogcustom.dismiss()
//
//        }
        collection_zoom_ok.setOnClickListener { dialogcustom.dismiss() }
        dialogcustom.setCancelable(false)
        dialogcustom.show()
    }
}