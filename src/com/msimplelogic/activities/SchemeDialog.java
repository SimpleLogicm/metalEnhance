package com.msimplelogic.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msimplelogic.activities.R;

public class SchemeDialog extends Activity {
    TextView schemeName,schemeType,schemeAmount,schemeDescription,schemeMinQty,schemeHeader;
    ImageView schemeDialogClose;
    RelativeLayout rl_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SchemeDialog.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.new_scheme_dialog);


              rl_image = findViewById(R.id.rl_image);
              schemeName = findViewById(R.id.tv_schemename);
              //schemeType = findViewById(R.id.tv_schemetype);
              schemeAmount = findViewById(R.id.tv_schemeamnt);
              schemeDescription = findViewById(R.id.tv_schemedscrpn);
              //schemeDialogClose = findViewById(R.id.schemedialog_close);
              //schemeMinQty = findViewById(R.id.tv_scheme_minqty);
              schemeHeader = findViewById(R.id.header);
        SharedPreferences sp = SchemeDialog.this.getSharedPreferences("SimpleLogic", 0);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);
        if (current_theme== 1){
            schemeHeader.setTextColor(Color.parseColor("#8E91A1"));
            schemeAmount.setTextColor(Color.parseColor("#8E91A1"));
            schemeName.setTextColor(Color.parseColor("#969797"));
            schemeDescription.setTextColor(Color.parseColor("#969797"));
        }



        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String scheme_minqty = extras.getString("SCHEME_MINQTY", null);

//            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_minqty)) {
//                schemeMinQty.setText("Scheme Min Qty : "+scheme_minqty);
//                schemeMinQty.setVisibility(View.VISIBLE);
//            }

//            else {ewrtweewrtwe
//                schemeMinQty.setText("");
//            }

            String scheme_name = extras.getString("SCHEME_NAME", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_name)) {
                schemeName.setText(scheme_name);
            } else {
                schemeName.setText("");
            }

            String scheme_type = extras.getString("SCHEME_TYPE", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_type)) {
                if(scheme_type.equalsIgnoreCase("quantity_scheme"))
                {
                    schemeHeader.setText("Buy "+extras.getString("SCHEME_BUYQTY", null) +" Get "+extras.getString("SCHEME_GETQTY", null)+" Free");
                }else if(scheme_type.equalsIgnoreCase("value_scheme")){
                        schemeHeader.setText("Value Scheme");
                     }else if(scheme_type.equalsIgnoreCase("discount_scheme")){
                    schemeHeader.setText("Discount Scheme");
                }
            } else {
                        schemeHeader.setText("");
                   }

            String scheme_amnt = extras.getString("SCHEME_AMOUNT", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_amnt)) {

                if(scheme_type.equalsIgnoreCase("discount_scheme"))
                {
                    schemeAmount.setVisibility(View.VISIBLE);
                    schemeAmount.setText(scheme_amnt+"%");
                }else if(scheme_type.equalsIgnoreCase("quantity_scheme")){
                    schemeAmount.setVisibility(View.GONE);
                }else{
                    schemeAmount.setVisibility(View.VISIBLE);
                    schemeAmount.setText("₹"+scheme_amnt);
                }

            } else {
                schemeAmount.setText("");
            }

            String scheme_desc = extras.getString("SCHEME_DESCRIPTION", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_desc)) {
                schemeDescription.setText(scheme_desc);
                schemeDescription.setVisibility(View.VISIBLE);
            } else {
                schemeDescription.setText("");
            }
        }

        rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
