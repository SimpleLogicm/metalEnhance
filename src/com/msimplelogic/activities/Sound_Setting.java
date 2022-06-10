package com.msimplelogic.activities;

/**
 * Created by sujit on 2/27/2017.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;

import java.util.ArrayList;
import java.util.Locale;

public class Sound_Setting extends Activity implements OnItemSelectedListener {
    Switch switch_appsound;
    Boolean status_addmore = false;
    Button browse_btn, mLanguage;
    int music_column_index;
    MediaPlayer mp;
    Cursor cursor;
    int count;
    private ArrayList<String> Language_List = new ArrayList<String>();
    ArrayAdapter<String> LanguageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_setting);
        cursor = null;
        switch_appsound = findViewById(R.id.switch_appsound);
        browse_btn = findViewById(R.id.browse_btn);
        mLanguage = findViewById(R.id.mLanguage);
        try {
            SharedPreferences sp = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);
            Intent i = getIntent();
            String name = i.getStringExtra("retialer");
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(getResources().getString(R.string.Settings));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);

            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//	       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }



        mp = new MediaPlayer();

        browse_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sound_Setting.this, Sound_Act.class));
            }
        });

        mLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Language_Select_Dialog();
            }
        });

        switch_appsound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    //browse_btn.setVisibility(View.VISIBLE);
                    browse_btn.setVisibility(View.INVISIBLE);
                    status_addmore = false;
                    SharedPreferences snd = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor edt_snd = snd.edit();
                    edt_snd.putBoolean("var_addmore", status_addmore);
                    edt_snd.commit();
                    //textView.setText(switchOn);
                } else {
                    browse_btn.setVisibility(View.INVISIBLE);
                    status_addmore = true;
                    SharedPreferences snd = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor edt_snd = snd.edit();
                    edt_snd.putBoolean("var_addmore", status_addmore);
                    edt_snd.commit();
                    //textView.setText(switchOff);
                }
            }
        });

        SharedPreferences spf1 = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
        Global_Data.app_sound = spf1.getBoolean("var_addmore", false);

        if (Global_Data.app_sound == false) {

            switch_appsound.setChecked(true);

        } else {

            switch_appsound.setChecked(false);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        Intent i = new Intent(Sound_Setting.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                try {
                    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
                    String[] projection = {
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media.DISPLAY_NAME,
                            MediaStore.Audio.Media.DURATION};
                    final String sortOrder = MediaStore.Audio.AudioColumns.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
                    // Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    //Cursor cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);

                    try {
                        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        //cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);
//                        cursor = managedQuery(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                                projection, null, null, null);

//                        int music_column_index = cursor
//                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        cursor.moveToNext();
                        String filename = cursor.getString(music_column_index);
                        Log.d("", "" + filename);
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String _getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void Language_Select_Dialog() {
        final Dialog dialog = new Dialog(Sound_Setting.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.language_dialog);
        // dialog.setTitle(getResources().getString(R.string.PDistributors));

        Language_List.clear();
        Language_List.add(getResources().getString(R.string.Select_Language));
        Language_List.add(getResources().getString(R.string.English));
        Language_List.add(getResources().getString(R.string.Hindi));

        final Spinner spnLanguage = dialog.findViewById(R.id.spnLanguage);
        LanguageAdapter = new ArrayAdapter<String>(Sound_Setting.this, android.R.layout.simple_spinner_item, Language_List);
        LanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguage.setAdapter(LanguageAdapter);

        SharedPreferences spff = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
        String Language = spff.getString("Language", "");

        if (!Language.equalsIgnoreCase("") && !Language.equalsIgnoreCase(null)) {
            int spinnerPosition = LanguageAdapter.getPosition(Language);
            spnLanguage.setSelection(spinnerPosition);

        }


        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Button dialogButtonOk = dialog.findViewById(R.id.dialogButtonOk);
        dialogButtonOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Select_Language))) {
//                    Toast toast = Toast.makeText(Sound_Setting.this,
//                            getResources().getString(R.string.Please_Select_Language), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Sound_Setting.this,
                            getResources().getString(R.string.Please_Select_Language), "yes");
                } else if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Hindi))) {

                    SharedPreferences spf = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Language", "hi");
                    editor.commit();

                    Locale myLocale = new Locale("hi");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);

                    recreate();
                    dialog.dismiss();
                } else if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.English))) {

                    SharedPreferences spf = Sound_Setting.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Language", "en");
                    editor.commit();

                    Locale myLocale = new Locale("en_US");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);

                    recreate();

                    dialog.dismiss();
                }

            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        dialogButtonCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
//while i am getting song from device, playing all songs but i want to play song which selected