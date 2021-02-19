package com.abhijith.stories.ui.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abhijith.stories.R
import com.abhijith.stories.databinding.StoriesActivityVideoBinding

class VideoActivity : AppCompatActivity() {

    lateinit var binding:StoriesActivityVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stories_activity_video)
        playVideo()
    }

    fun playVideo(){
        val str = "https://abhiandroid.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4"
        val uri = Uri.parse(str)

        binding.videoView.setVideoURI(uri)

        binding.videoView.requestFocus()
        binding.videoView.start()

        binding.videoView.setOnPreparedListener {
            Toast.makeText(this, "About to be played", Toast.LENGTH_LONG).show()
        }
    }
}