package com.s1aks.animationexample

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.view.View
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

        binding.car.setOnClickListener {
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
    }
}