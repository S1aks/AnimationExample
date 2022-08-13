package com.s1aks.animationexample

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.s1aks.animationexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var numCorner = 0
    private var width = 0f
    private var height = 0f
    private var pathList = mutableListOf<Path>()

    override fun onWindowFocusChanged(hasFocus: Boolean) { // Для взятия актуальных размеров вью
        super.onWindowFocusChanged(hasFocus)
        width = binding.root.width.toFloat()
        height = binding.root.height.toFloat()
        val carSize = resources.getDimension(R.dimen.car_size)
        pathList.add(Path().apply {
            arcTo(
                0f, -(height / 2), (width - carSize), (height / 2),
                180f, -180f, true
            )
        })
        pathList.add(Path().apply {
            arcTo(
                (width / 2 - carSize), 0f, (width * 1.5f - carSize), (height - carSize),
                270f, -180f, true
            )
        })
        pathList.add(Path().apply {
            arcTo(
                0f, (height / 2 - carSize), (width - carSize), (height * 1.5f - carSize),
                0f, -180f, true
            )
        })
        pathList.add(Path().apply {
            arcTo(
                -(width / 2), 0f, (width / 2), (height - carSize),
                90f, -180f, true
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        binding.car.setOnClickListener {
            binding.root.setBackgroundResource(R.color.black)
            Toast.makeText(this, getString(R.string.go_message), Toast.LENGTH_SHORT).show()
            ObjectAnimator.ofFloat(it, View.X, View.Y, pathList[numCorner]).apply {
                duration = 2000
                start()
            }
            if (numCorner < 3) {
                numCorner++
            } else {
                numCorner = 0
            }
        }

        binding.car.tag = IMAGEVIEW_TAG
        binding.car.setOnLongClickListener { view: View ->
            binding.root.setBackgroundResource(R.color.white)
            Toast.makeText(this, getString(R.string.drag_message), Toast.LENGTH_SHORT).show()
            val item = ClipData.Item(IMAGEVIEW_TAG)
            val dataToDrag =
                ClipData(IMAGEVIEW_TAG, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val carShadow = CarDragShadowBuilder(view)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, carShadow, view, 0)
            } else {
                view.startDragAndDrop(dataToDrag, carShadow, view, 0)
            }
            view.visibility = View.INVISIBLE
            true
        }

        binding.root.setOnDragListener(carDragListener)
    }

    private val carDragListener = View.OnDragListener { view, dragEvent ->
        val draggableItem = dragEvent.localState as View
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                true
            }
            DragEvent.ACTION_DROP -> {
                draggableItem.x = dragEvent.x - (draggableItem.width / 2)
                draggableItem.y = dragEvent.y - (draggableItem.height / 2)
                val parent = draggableItem.parent as FrameLayout
                parent.removeView(draggableItem)
                val dropArea = view as FrameLayout
                dropArea.addView(draggableItem)
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                true
            }
            else -> {
                false
            }
        }
    }

    companion object {
        private const val IMAGEVIEW_TAG = "car bitmap"
    }
}