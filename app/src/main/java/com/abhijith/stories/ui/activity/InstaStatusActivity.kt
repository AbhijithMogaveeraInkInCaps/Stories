package com.abhijith.stories.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abhijith.stories.StoryModel
import com.abhijith.stories.databinding.ActivityMainBinding
import com.abhijith.stories.ui.dialog.DataInputDialog
import java.io.File
import java.io.FileOutputStream


class InstaStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val SELECT_PICTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ibChangeBgColor.setOnClickListener {
            binding.sv.setRandomColors()
        }

        binding.ibLaunchImagePic.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
        }

        binding.ibLaunchText.setOnClickListener {
            binding.sv.tv.onTextClicked()
        }

        var link =""
        binding.insertHyperLink.setOnClickListener {
            DataInputDialog(this, { link = it }, {}).show()
        }

        binding.btnSaveStatus.setOnClickListener {
            val sdCard: File = Environment.getExternalStorageDirectory()
            val dir = File(sdCard.absolutePath + "/story")
            dir.mkdirs()
            val fileName = String.format("%d.jpeg", System.currentTimeMillis())
            val file = File(dir, fileName)
            Log.e("Path", file.absolutePath)
            Toast.makeText(this, file.absolutePath, Toast.LENGTH_SHORT).show()
            val fOut = FileOutputStream(file)
            binding.sv.getScreenInBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()
            StoryModel.insertImageStory(
                StoryModel.StoryDataImage(
                    System.currentTimeMillis(),
                    file.absolutePath,
                    link
                )
            )
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri: Uri = data!!.data!!
                binding.sv.setImage(selectedImageUri)
            }
        }
    }
}