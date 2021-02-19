package com.abhijith.stories.activity

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.abhijith.stories.model.StoryModel
import com.abhijith.stories.databinding.ActivityMainBinding
import com.abhijith.stories.extension.beVisible
import com.abhijith.stories.extension.setXY
import com.abhijith.stories.extension.startImagePickingActivity
import com.abhijith.stories.extension.toJpegFile
import com.abhijith.stories.helper.SELECT_PICTURE
import com.abhijith.stories.dialog.DataInputDialog


class InstaStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var link = ""

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
                    owner.removeView(v)

                    val dest = view as ConstraintLayout
                    dest.addView(v)

                    v.beVisible()
                    v.setXY(dragEvent.x,dragEvent.y)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(binding.root)

            ibChangeBgColor.setOnClickListener {
                binding.sv.setRandomColors()
            }

            insertSticker.setOnClickListener {
                binding.lltop.visibility = View.VISIBLE
            }

            lltop.setOnDragListener(dragAndDropListener)

            base.setOnDragListener(dragAndDropListener)

            val onLongClickListener: (View) -> Boolean = {
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
                binding.lltop.getChildAt(i)?.setOnLongClickListener(onLongClickListener)
            }
            ibLaunchImagePic.setOnClickListener {
                startImagePickingActivity()
            }

            ibLaunchText.setOnClickListener {
                binding.sv.tv.onTextClicked()
            }

            insertHyperLink.setOnClickListener {
                DataInputDialog(this@InstaStatusActivity, { link = it }, {}).show()
            }

            btnSaveStatus.setOnClickListener {
                val file = binding.sv.getScreenInBitmap().toJpegFile()
                val data = StoryModel.StoryDataImage(System.currentTimeMillis(),file.absolutePath,link)
                StoryModel.insertImageStory(data)
                onBackPressed()
            }

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
    companion object{
        fun startActivity(context: Context) {
            Intent(context, InstaStatusActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}