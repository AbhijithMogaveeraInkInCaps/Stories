package com.abhijith.stories.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhijith.stories.model.StoryModel
import com.abhijith.stories.databinding.ActivityMain2Binding
import com.abhijith.stories.permisson.ShortenMultiplePermissionListener
import com.abhijith.stories.adapter.StoryAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        val multiplePermissionsListener = object : ShortenMultiplePermissionListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    StoryModel.liveData.observe(this@MainActivity, {
                        binding.storyList.adapter = StoryAdapter(this@MainActivity, StoryModel)
                    })
                    binding.apply {
                        btnTextAndImageStories.setOnClickListener {
                            TextStatusActivity.startActivity(this@MainActivity)
                        }
                        btnTextStories.setOnClickListener {
                            InstaStatusActivity.startActivity(this@MainActivity)
                        }
                        btnViewAll.setOnClickListener {
                            StoryDisplayActivity.startActivity(this@MainActivity)
                        }
                    }

                } else {
                    finish()
                }
            }
        }

        Dexter
            .withContext(this)
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