package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.preference.PreferenceManager
import android.widget.ImageButton
import java.util.*

class AnimationManager constructor(mainActivity:MainActivity){
    // うぉっちっち画像エリア
    private lateinit var imageButton:ImageButton
    private lateinit var vibrator: Vibrator
    private lateinit var watchicchiApp: WatchicchiApp
    private lateinit var handler:Handler
    private var timer :Timer? = null
    private var isHiyokoWalking: Boolean = false
    private var isNiwatoriWalking: Boolean = false
    private var animatorSet:AnimatorSet? = null

    init{
        vibrator = mainActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        imageButton = mainActivity?.findViewById<ImageButton>(R.id.watchcchi_image)
        watchicchiApp = mainActivity.application as WatchicchiApp
        handler = Handler(Looper.getMainLooper())

    }

    // 歩いているアニメーションを止める
    private fun resetWalking(){
        animatorSet?.cancel()
        timer?.cancel()
        isHiyokoWalking = false
        isNiwatoriWalking = false
    }

    /* ここから卵の時 */
    // たまごから生まれる処理 10回タップされたら呼ばれる
    fun eggToHiyoko(){
        // アニメーションリセット
        resetWalking()
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
        var index = 0
        val runnable = object : Runnable {
            override fun run() {
                // 振動
                vibrator.vibrate(80)
                // 画像切り替え
                imageButton.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、「Runnable」を停止する
                    // アプリ起動中であれば進化フラグすすめる
                    val pref = PreferenceManager.getDefaultSharedPreferences(watchicchiApp)
                    if(pref.getBoolean("isMainActivityOn", true)){
                        watchicchiApp.getWatchicchi().envolve()
                    }
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }



    /* ひよことニワトリのときに呼ばれる */
    fun walking(image:Int){
        // 既に歩いていたら実行しない
        if(image == R.drawable.hiyoko && isHiyokoWalking
            ||
            image == R.drawable.niwatori && isNiwatoriWalking
        ){
            return
        }


        //
        resetWalking()

        if(image == R.drawable.hiyoko){
            isHiyokoWalking = true
        }else if(image == R.drawable.niwatori){
            isNiwatoriWalking = true
        }

        println("==walking==")
        vibrator.vibrate(80)
        imageButton.setImageResource(image)

        // 6秒おきにhandlerに処理を渡す
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val runnable = object : Runnable {
                    override fun run() {
                        animatorSet = getWalkingAnimatorSet()
                        animatorSet?.start()
                        return
                    }
                }
                handler.post(runnable)
            }
        }, 0, 6000)
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
        resetWalking()
        // 画像切り替え
        val images = arrayOf(
            R.drawable.hiyoko,
            R.drawable.niwatori,
            R.drawable.hiyoko,
            R.drawable.niwatori,
            R.drawable.hiyoko,
            R.drawable.niwatori,
        )
        var index = 0
        val runnable = object : Runnable {
            override fun run() {
                // 振動
                vibrator.vibrate(80)
                // 画像切り替え
                imageButton.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、進化させて「Runnable」を停止する
                    // アプリ起動中であれば進化フラグすすめる
                    val pref = PreferenceManager.getDefaultSharedPreferences(watchicchiApp)
                    if(pref.getBoolean("isMainActivityOn", true)){
                        watchicchiApp.getWatchicchi().envolve()
                    }
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    fun niwatoriLayEgg(){
        resetWalking()
        // 画像切り替え
        val images = arrayOf(
            R.drawable.niwatori,
            R.drawable.niwatori_birth,
            R.drawable.niwatori,
            R.drawable.niwatori_birth,
            R.drawable.niwatori_birth,
        )
        var index = 0
        val runnable = object : Runnable {
            override fun run() {

                // 画像切り替え
                imageButton.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // アプリ起動中であれば進化フラグすすめる
                    val pref = PreferenceManager.getDefaultSharedPreferences(watchicchiApp)
                    if(pref.getBoolean("isMainActivityOn", true)){
                        watchicchiApp.getWatchicchi().envolve()
                    }
                }
                // 振動
                vibrator.vibrate(80)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
}