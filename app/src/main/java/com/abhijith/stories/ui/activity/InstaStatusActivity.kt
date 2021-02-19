package com.abhijith.stories.ui.activity

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.abhijith.stories.StoryModel
import com.abhijith.stories.databinding.ActivityMainBinding
import com.abhijith.stories.ui.dialog.DataInputDialog
import java.io.File
import java.io.FileOutputStream


class InstaStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dragAndDropListener by lazy {
        View.OnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.invalidate()
                    return@OnDragListener true
                }
                DragEvent.ACTION_DROP -> {
                    val item = dragEvent.clipData.getItemAt(0)
                    val dragData = item.text
                    Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()
                    val v = dragEvent.localState as View
                    val owner = v.parent as ViewGroup
//                    owner.visibility = View.INVISIBLE
                    owner.removeView(v)

                    val dest = view as ConstraintLayout
                    dest.addView(v)

                    v.visibility = View.VISIBLE
                    v.x = dragEvent.x
                    v.y = dragEvent.y
                    view.invalidate()
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.invalidate()
                    return@OnDragListener true

                }
            }
            false
        }
    }

    private val SELECT_PICTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ibChangeBgColor.setOnClickListener {
            binding.sv.setRandomColors()
        }

        binding.insertSticker.setOnClickListener {
            binding.lltop.visibility = View.VISIBLE
        }

        binding.lltop.setOnDragListener(dragAndDropListener)

        binding.base.setOnDragListener(dragAndDropListener)

        val function: (View) -> Boolean = {
            val clipData = "This is our clip text"
            val item = ClipData.Item(clipData)
            val mineType = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipData, mineType, item)
            val dragShadowBuilder = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(data, dragShadowBuilder, it, 0)
            } else {
                it.startDrag(data, dragShadowBuilder, it, 0)
            }
            binding.lltop.visibility = View.INVISIBLE
            true
        }

        for (i in 0..binding.lltop.childCount) {
            binding.lltop.getChildAt(i)?.setOnLongClickListener(function)
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

        var link = ""

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
            StoryModel.insertImageStory(StoryModel.StoryDataImage(System.currentTimeMillis(),
                    file.absolutePath, link))
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