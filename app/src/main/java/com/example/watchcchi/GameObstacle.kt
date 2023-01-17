package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import java.util.*

class GameObstacle (_imageView:ImageView, _obstacleAreaLayout:LinearLayout){
    private var imageView: ImageView
    private lateinit var obstacleAreaLayout: LinearLayout

    private lateinit var handler: Handler
    private lateinit var animatorSet: AnimatorSet

    init{
        handler = Handler(Looper.getMainLooper())
        imageView = _imageView
        obstacleAreaLayout = _obstacleAreaLayout
        // ジャンプアニメーションセット
        animatorSet = AnimatorSet()
        val animator1 = ObjectAnimator.ofFloat(imageView, "translationX", -400f)
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
                    println("追加")
                    animatorSet.start()
                    println("障害物アニメスタート")
                }
            }
            handler.post(runnable)
    }

    fun stopMoving(){
        //タイマー停止
        animatorSet.cancel()
    }
}