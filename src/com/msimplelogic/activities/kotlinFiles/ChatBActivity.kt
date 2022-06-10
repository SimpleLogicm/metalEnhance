package com.msimplelogic.activities.kotlinFiles

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.R
import kotlinx.android.synthetic.main.activity_chat_b.*

class ChatBActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_b)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.hide();

    }

}
