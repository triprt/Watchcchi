package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Vibrator
import android.widget.ImageButton

class AnimationManager constructor(mainActivity:MainActivity){
    // うぉっちっち画像エリア
    private lateinit var imageButton:ImageButton
    private lateinit var vibrator: Vibrator
    private lateinit var watchicchiApp: WatchicchiApp

    init{
        vibrator = mainActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        imageButton = mainActivity?.findViewById<ImageButton>(R.id.watchcchi_image)
        watchicchiApp = mainActivity.application as WatchicchiApp
    }

    /* ここから卵の時 */
    // たまごから生まれる処理 10回タップされたら呼ばれる
    fun eggToHiyoko(){
        //たまごが揺れる
        val animator = ObjectAnimator.ofFloat(imageButton, "translationX", 0f, 3f, -3f, 0f)
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
                imageButton.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、「Runnable」を停止する
                    // 進化処理
                    watchicchiApp.getWatchicchi().envolve()
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }



    /* ひよことニワトリのときに呼ばれる */
    fun walking(image:Int){
        val handler = Handler()
        // 画像切り替え
        imageButton.setImageResource(image)
        val runnable = object : Runnable {
            override fun run() {
                val animatorSet = getWalkingAnimatorSet()
                animatorSet.start()
                handler.postDelayed(this, 6000)
            }
        }
        handler.post(runnable)
    }

    // 歩くアニメーション
    fun getWalkingAnimatorSet(): AnimatorSet {
        val pattern = (0 until 2).random()
        val animatorSet = AnimatorSet()

        when(pattern){
            0 -> {
                // 右に歩く　ジャンプ
                val animator1 = ObjectAnimator.ofFloat(imageButton, "translationX", 40f)
                animator1.duration = 1000

                val animator2 = ObjectAnimator.ofFloat(imageButton, "translationY", -30f)
                animator2.duration = 500

                val animator3 = ObjectAnimator.ofFloat(imageButton, "translationY", 0f)
                animator3.duration = 500

                val animator4 = ObjectAnimator.ofFloat(imageButton, "translationX", 80f)
                animator4.duration = 1000

                val animator5 = ObjectAnimator.ofFloat(imageButton, "scaleX", -1f)
                animator5.duration = 0

                val animator6 = ObjectAnimator.ofFloat(imageButton, "translationX", 0f)
                animator6.duration = 2000

                val animator7 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f)
                animator7.duration = 0
                animatorSet.playSequentially(animator1, animator2, animator3, animator4, animator5, animator6, animator7)


            }
            1 -> {
                val animator1 = ObjectAnimator.ofFloat(imageButton, "scaleX", -1f)
                animator1.duration = 0

                // 左に歩く　ジャンプ
                val animator2 = ObjectAnimator.ofFloat(imageButton, "translationX", -40f)
                animator2.duration = 1000

                val animator3 = ObjectAnimator.ofFloat(imageButton, "translationY", -30f)
                animator3.duration = 500

                val animator4 = ObjectAnimator.ofFloat(imageButton, "translationY", 0f)
                animator4.duration = 500

                val animator5 = ObjectAnimator.ofFloat(imageButton, "translationX", -80f)
                animator5.duration = 1000

                val animator6 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f)
                animator6.duration = 0

                val animator7 = ObjectAnimator.ofFloat(imageButton, "translationX", 0f)
                animator7.duration = 2000
                animatorSet.playSequentially(animator1, animator2, animator3, animator4, animator5, animator6, animator7)
            }
        }
        return animatorSet
    }


    /* ひよこからニワトリ */
    fun hiyokoToNiwatori(){
        // 画像切り替え
        val images = arrayOf(
            R.drawable.hiyoko,
            R.drawable.niwatori,
            R.drawable.hiyoko,
            R.drawable.niwatori,
            R.drawable.hiyoko,
            R.drawable.niwatori,
        )

        val handler = Handler()
        var index = 0
        val runnable = object : Runnable {
            override fun run() {
                // 振動
                vibrator.vibrate(100)
                // 画像切り替え
                imageButton.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、「Runnable」を停止する
                    watchicchiApp.getWatchicchi().envolve()
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
    //
    fun niwatoriLayEgg(){
        // 画像切り替え
        val images = arrayOf(
            R.drawable.hiyoko,
            R.drawable.niwatori,
            R.drawable.hiyoko,
            R.drawable.niwatori,
            R.drawable.hiyoko,
            R.drawable.niwatori,
        )

        val handler = Handler()
        var index = 0
        val runnable = object : Runnable {
            override fun run() {
                // 振動
                vibrator.vibrate(100)
                // 画像切り替え
                imageButton.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、状態を帰る
                    watchicchiApp.getWatchicchi().envolve()
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
}