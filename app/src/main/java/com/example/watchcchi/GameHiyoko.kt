package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import java.util.*

class GameHiyoko constructor(_imageView:ImageView) {

    var imageView: ImageView
    var isJumping:Boolean = false
    private var timer : Timer
    private lateinit var handler:Handler

    init{
        handler = Handler(Looper.getMainLooper())
        imageView = _imageView
        timer = Timer()
    }

    // 歩き始める
    fun startWalking(){
        stopWalking()
        //trueなら1、falseなら2を表示
        var isImage1 = false
        timer = Timer()
        timer.schedule(object : TimerTask() {
            // 250ms秒おきに画像を切り替える処理を実行
            override fun run() {
                val runnable = object : Runnable {
                    override fun run() {
                        if(isImage1){
                            imageView.setImageResource(R.drawable.game_niwatori_walking1)
                        }else{
                            imageView.setImageResource(R.drawable.game_niwatori_walking2)
                        }
                        isImage1 = !isImage1
                    }
                }
                handler.post(runnable)
            }
        }, 0, 200)
    }

    fun stopWalking(){
        //タイマー停止
        timer.cancel()
    }

    fun jump(){
        // 一度歩くのを止める
        stopWalking()
        // ジャンプアニメーションセット
        val jumpAnimatorSet = AnimatorSet()
        val animator1 = ObjectAnimator.ofFloat(imageView, "translationY", -80f)
        animator1.duration = 500
        val animator2 = ObjectAnimator.ofFloat(imageView, "translationY", 0f)
        animator2.duration = 400
        jumpAnimatorSet.playSequentially(animator1,animator2)
        // 着地（walkingスターと）
        val runnableLand = object : Runnable {
            override fun run() {
                // walking
                startWalking()
                isJumping = false
            }
        }

        // ジャンプ
        val runnableJump = object : Runnable {
            override fun run() {
                isJumping = true
                // 画像切り替え
                imageView.setImageResource(R.drawable.game_niwatori_jumping)
                jumpAnimatorSet?.start()
                //アニメーションに1sかかるので、1s待ってからwalking再開
                handler.postDelayed(runnableLand, 900)
            }
        }
        handler.post(runnableJump)
    }
}