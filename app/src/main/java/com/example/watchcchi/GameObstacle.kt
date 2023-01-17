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

    // 障害物の左端のx座標
    // private var xPosition: Float

    private var isShow = false

    init{
        handler = Handler(Looper.getMainLooper())
        imageView = _imageView
        obstacleAreaLayout = _obstacleAreaLayout

        animatorSet = AnimatorSet()
        val animator1 = ObjectAnimator.ofFloat(imageView, "translationX", -374f)
        animator1.duration = 1500
        animator1.interpolator = LinearInterpolator() // 変化の速度は一定
        animatorSet.play(animator1)
    }


    // うごき始める
    fun startMoving(){
            val runnable = object : Runnable {
                override fun run() {
                    // view追加
                    imageView.setImageResource(R.drawable.obstacle_small)
                    obstacleAreaLayout.addView(imageView, ViewGroup.LayoutParams(20, 20))
                    animatorSet.start()
                    println("障害物アニメスタート")
                }
            }
            handler.post(runnable)
    }

    fun add(){
        val runnable = object : Runnable {
            override fun run() {
                // view追加
                println( "add run実行" )
                imageView.setImageResource(R.drawable.obstacle_small)
                obstacleAreaLayout.addView(imageView, ViewGroup.LayoutParams(20, 20))
            }
        }
        handler.post(runnable)
    }

    // 0.01秒で動かす
    fun move(){
        val runnable = object : Runnable {
            override fun run() {
                println( "move run実行" )

                /* Addに移動
                val frameAnimatorSet = AnimatorSet()
                val animator1 = ObjectAnimator.ofFloat(imageView, "translationX", -500f)
                animator1.duration = 100 // いったん0.1秒で
                animator1.interpolator = LinearInterpolator() // 変化の速度は一定
                frameAnimatorSet.play(animator1)
                frameAnimatorSet.start()
                 */

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
        }

    }

    fun show(){
        isShow = true
    }
}