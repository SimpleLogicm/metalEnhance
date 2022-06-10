package com.msimplelogic.slidingmenu;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.msimplelogic.activities.Global_Data;

public class DogsDropdownOnItemClickListener implements OnItemClickListener {
 
    String TAG = "DogsDropdownOnItemClickListener.java";
     
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
 
        // get the context and main activity to access variables
        Context mContext = v.getContext();
        CalendarAct mainActivity = ((CalendarAct) mContext);
         
        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);
         
        // dismiss the pop up
        mainActivity.popupWindowDogs.dismiss();
         
        // get the text and set it as the button text
        String selectedItemText = ((TextView) v).getText().toString();
        //mainActivity.buttonShowDropDown.setText(selectedItemText);
         
        // get the id
        String selectedItemTag = ((TextView) v).getTag().toString();
        Global_Data.Custom_Toast(mContext, "Dog ID is: " + selectedItemTag+","+selectedItemText,"");
        
      
    }
 
}