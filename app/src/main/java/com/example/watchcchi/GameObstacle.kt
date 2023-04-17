package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import java.util.*

class GameObstacle (_imageView:ImageView, _obstacleAreaLayout:LinearLayout){
    var imageView: ImageView
    private lateinit var obstacleAreaLayout: LinearLayout

    private lateinit var handler: Handler
    private lateinit var animatorSet: AnimatorSet

    // 障害物がすすんだ距離
    private var movedDistance: Float = 0.0F


    // 障害物の左端のx座標
    // private var xPosition: Float

    private var isShow = false

    init{
        handler = Handler(Looper.getMainLooper())
        imageView = _imageView
        obstacleAreaLayout = _obstacleAreaLayout

        /*
        animatorSet = AnimatorSet()
        val animator1 = ObjectAnimator.ofFloat(imageView, "translationX", -380f)
        animator1.duration = 1500
        animator1.interpolator = LinearInterpolator() // 変化の速度は一定
        animatorSet.play(animator1)*/
    }


    // うごき始める
    fun setImage(){
            val runnable = object : Runnable {
                override fun run() {
                    // view追加
                    imageView.setImageResource(R.drawable.obstacle_small)
                    obstacleAreaLayout.addView(imageView, ViewGroup.LayoutParams(30, 20))
                }
            }
            handler.post(runnable)
    }

    // 0.01秒で動かす
    fun move(){
        val runnable = object : Runnable {
            override fun run() {
                println( "move run実行" )
                val animatorSet1 = AnimatorSet()
                val animator1 = ObjectAnimator.ofFloat(imageView, "translationX", -15f - movedDistance)
                animator1.duration = 50
                animator1.interpolator = LinearInterpolator() // 変化の速度は一定
                movedDistance += 15f
                animatorSet1.play(animator1)
                animatorSet1.start()
            }
        }
        handler.post(runnable)
    }

    fun stopMoving(){
        //非表示
        if(!isShow){
            val runnable = object : Runnable {
                override fun run() {
                    imageView.visibility = View.GONE
                }
            }
            handler.post(runnable)
        }else{

        }
    }

    fun show(){
        isShow = true
    }
}