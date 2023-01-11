package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Vibrator
import android.preference.PreferenceManager
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import java.lang.Thread.sleep


enum class EvolveLevel(val id: Int) {
    EGG(0),
    BIRTH(1),
    HIYOKO_WITH_EGG(2),
    HIYOKO(3),
    NIWATORI(4)
}


class Watchcchi constructor( _activity:Activity) {
    // うぉっちっち画像エリア
    lateinit var imageButton:ImageButton
    lateinit var vibrator:Vibrator
    lateinit var activity:Activity

    // 現在の進化状態
    private lateinit var evolveLevel:EvolveLevel

    // お腹が空いたかどうかを表す
    lateinit var hunger: Hunger
    // 仲良し度
    lateinit var friendShip: FriendShip

    // mainからimageViewをもらって生成
    init{
        activity = _activity
        // バイブ
        vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // うぉっちっちのimageView取得
        imageButton = activity.findViewById<ImageButton>(R.id.watchcchi_image)

        // 保存された値を取得
        val pref = PreferenceManager.getDefaultSharedPreferences(_activity)
        val evolveLevelId = pref.getInt("evolveLevel",3)
        println("レベル取得")
        println(evolveLevelId)

        // 状態セット
        evolveLevel = when(evolveLevelId){
            0 -> EvolveLevel.EGG
            1 -> EvolveLevel.BIRTH
            2 -> EvolveLevel.HIYOKO_WITH_EGG
            3 -> EvolveLevel.HIYOKO
            4 -> EvolveLevel.NIWATORI
            else -> {EvolveLevel.EGG}
        }

        // 仲良し度をセットしてからお腹をすかせる処理をする必要があるので同期処理
        //lifecycleScope.launch{
            // 仲良し
            friendShip = FriendShip(activity)
            // お腹
        println("==")
        sleep(1000)
        println("AAA")
            hunger = Hunger(activity)
        //}

        // 最初に呼ぶ関数
         when(evolveLevel){
            EvolveLevel.EGG -> {
                // 特に何もしない
            }
            EvolveLevel.BIRTH -> {
                // 生まれる処理
                eggToHiyoko()
            }
            EvolveLevel.HIYOKO_WITH_EGG ->{
                // たまご付ひよこの画像
                imageButton.setImageResource(R.drawable.birth5)
            }
            EvolveLevel.HIYOKO ->{
                // 歩きまわる処理
                walking(R.drawable.hiyoko)
            }
            EvolveLevel.NIWATORI ->{
                // 鶏が歩き回る処理
                walking(R.drawable.niwatori)
            }
        }
    }
    // 進化
    fun nextEvolveLevel(){
        evolveLevel = when(evolveLevel){
            EvolveLevel.EGG -> EvolveLevel.BIRTH
            EvolveLevel.BIRTH -> EvolveLevel.HIYOKO_WITH_EGG
            EvolveLevel.HIYOKO_WITH_EGG -> EvolveLevel.HIYOKO
            EvolveLevel.HIYOKO -> EvolveLevel.NIWATORI
            EvolveLevel.NIWATORI -> EvolveLevel.EGG
        }
    }

    // 状態取得
    fun getEvolveLevel():EvolveLevel{
        return evolveLevel
    }

    /* ここから卵の時に呼ばれる */
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
                    envolve()
                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    fun envolve(){
        when(evolveLevel){
            EvolveLevel.EGG -> {
                // なにもしない？
            }
            EvolveLevel.BIRTH -> {
                // 生まれる処理
                eggToHiyoko()
            }
            EvolveLevel.HIYOKO_WITH_EGG ->{
                //特に処理なし？
            }
            EvolveLevel.HIYOKO ->{
                // 歩きまわる処理
                hiyokoToNiwatori()
            }
            EvolveLevel.NIWATORI ->{
                // 卵をうむ処理
            }
        }
        nextEvolveLevel()
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
                    walking(R.drawable.niwatori)

                    return
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }


    /* ここからひよこのときに呼ばれる */
    fun walking(image:Int){
        val handler = Handler()
        // 画像切り替え
        imageButton.setImageResource(image)
        val runnable = object : Runnable {
            override fun run() {
                if(evolveLevel != EvolveLevel.HIYOKO){
                    return
                }
                val animatorSet = getWalkingAnimatorSet()
                animatorSet.start()
                handler.postDelayed(this, 6000)
            }
        }
        handler.post(runnable)
    }

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

    fun getOnakaText():String{
        return hunger.getOnakaText()
    }

    fun getNakayoshiText():String{
        return friendShip.getOnakaText()
    }

    // ご飯をあげる処理
    fun feed(){
        hunger.plusLevel()
    }
}