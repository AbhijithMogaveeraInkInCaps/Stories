package com.abhijith.stories.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhijith.stories.R
import com.abhijith.stories.StoryModel
import com.abhijith.stories.permisson.ShortenMultiplePermissionListener
import com.abhijith.stories.databinding.ActivityMain2Binding
import com.abhijith.stories.ui.adapter.StoryAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
//
        binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        val multiplePermissionsListener = object : ShortenMultiplePermissionListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    StoryModel.liveData.observe(this@MainActivity,{
                        binding.storyList.adapter = StoryAdapter(this@MainActivity, StoryModel)
                    })
                    binding.apply {
                        btnTextAndImageStories.setOnClickListener {
                            Intent(this@MainActivity, TextStatusActivity::class.java).apply {
                                startActivity(this)
                            }
                        }
                        btnTextStories.setOnClickListener {
                            Intent(this@MainActivity, InstaStatusActivity::class.java).apply {
                                startActivity(this)
                            }
                        }
                        btnViewAll.setOnClickListener {
                            if(StoryModel.getAllStory().isNotEmpty()){
                                startActivity(Intent(this@MainActivity, TestActivity::class.java))
                            }
                        }
                    }

                } else {
                    finish()
                }
            }
        }
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            .withListener(multiplePermissionsListener)
            .check()

    }
}