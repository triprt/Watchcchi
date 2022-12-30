package com.example.watchcchi

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Vibrator
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService


enum class EvolveLevel{
    EGG,
    BIRTH,
    HIYOKO,
    NIWATORI
}

class Watchcchi constructor(_imageView: ImageView, _vibrator:Vibrator) {
    // うぉっちっち画像エリア
    lateinit var imageView:ImageView

    lateinit var vibrator:Vibrator

    // 現在の進化状態
    var evolveLevel:EvolveLevel = EvolveLevel.EGG

    // お腹が空いたかどうかを表すフラグ
    var isHungry: Boolean = false

    // mainからimageViewをもらって生成
    init{
        imageView = _imageView
        vibrator = _vibrator
    }


    // たまごから生まれる処理 10回タップされたら呼ばれる
    fun eggToHiyoko(){
        // 進化させる
        evolveLevel = EvolveLevel.BIRTH

        //たまごが揺れる
        val animator = ObjectAnimator.ofFloat(imageView, "translationX", 0f, 3f, -3f, 0f)
        animator.duration = 100
        animator.repeatCount = 30
        animator.start()

        // たまご画像切り替え
        val images = arrayOf(
            R.drawable.birth1,
            R.drawable.birth2,
            R.drawable.birth3,
            R.drawable.birth4,
            R.drawable.birth5
        )

        val handler = Handler()
        var index = 0

        val runnable = object : Runnable {
            override fun run() {
                // 振動
                vibrator.vibrate(100)
                // 画像切り替え
                imageView.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、「Runnable」を停止する
                    // 進化させる
                    evolveLevel = EvolveLevel.HIYOKO
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)



    }

    // お腹が減る処理
    fun decreaseHunger() {
        // お腹が減る処理を記述する
        isHungry = true
    }
}