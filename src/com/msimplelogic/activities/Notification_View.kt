package com.msimplelogic.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import cpm.simplelogic.helper.Config


class Notification_View : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val RECOVERY_DIALOG_REQUEST = 1

    // YouTube player view
    private var youTubeView: YouTubePlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_notification__view)

        val extras = intent.extras
        val latitude: String
        val longitude: String

        if (extras != null) {

            try
            {
                latitude = extras!!.getString("latitude")!!
                longitude = extras!!.getString("longitude")!!

//                val intent = Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"))
//                startActivity(intent)

                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=latitude,longitude"))
                startActivity(intent)

            }catch(e:Exception)
            {
                e.printStackTrace()
            }

            // and get whatever type user account id is
        }

       // youTubeView = findViewById(R.id.youtube_view)
        // Initializing video player with developer key
       // youTubeView!!.initialize(Config.DEVELOPER_KEY, this)
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        if (p1!!.isUserRecoverableError()) {
            p1.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(
                    getString(R.string.error_player), p1.toString())
            //  Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

//            val toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG)
//            toast.setGravity(Gravity.CENTER, 0, 0)
//            toast.show()
            Global_Data.Custom_Toast(this, errorMessage,"yes")
        }
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
        if (!p2) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            p1!!.loadVideo(Config.YOUTUBE_VIDEO_CODE)

            // Hiding player controls
            // player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this)
        }
    }

    private fun getYouTubePlayerProvider(): YouTubePlayer.Provider {
        return findViewById(R.id.youtube_view) as YouTubePlayerView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        this.finish()
    }
}
