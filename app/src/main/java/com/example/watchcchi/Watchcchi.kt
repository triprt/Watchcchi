package com.example.watchcchi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Vibrator
import android.preference.PreferenceManager
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import java.lang.Thread.sleep




class Watchcchi constructor(_watchicchiApp:WatchicchiApp) {
    // sharePrefのために渡す
    private var watchicchiApp: WatchicchiApp

    // アニメーション
    private var mainActivity:MainActivity? = null
    private var animationManager: AnimationManager? = null
    private var imageButton:ImageButton? = null

    // 現在の進化状態
    private lateinit var evolveLevel:WatchicchiApp.EvolveLevel

    // お腹が空いたかどうかを表す
    private lateinit var hunger: Hunger
    // 仲良し度
    private lateinit var friendShip: FriendShip

    // mainからimageViewをもらって生成
    init{
        // Applicationセット
        watchicchiApp = _watchicchiApp

        // 保存された値を取得
        val pref = PreferenceManager.getDefaultSharedPreferences(watchicchiApp)
        val evolveLevelId = pref.getInt("evolveLevel",0)

        // 状態セット
        evolveLevel = when(evolveLevelId){
            0 -> WatchicchiApp.EvolveLevel.EGG
            1 -> WatchicchiApp.EvolveLevel.BIRTH
            2 -> WatchicchiApp.EvolveLevel.HIYOKO_WITH_EGG
            3 -> WatchicchiApp.EvolveLevel.HIYOKO
            4 -> WatchicchiApp.EvolveLevel.HIYOKO_TO_NIWATORI
            5 -> WatchicchiApp.EvolveLevel.NIWATORI
            6 -> WatchicchiApp.EvolveLevel.NIWATORI_LAY_EGG
            else -> {WatchicchiApp.EvolveLevel.EGG}
        }

        println("レベル取得:" + evolveLevel)
    }

    // mainActivityから呼ぶ
    fun setUpActivity(_mainActivity:MainActivity){
        mainActivity = _mainActivity
        animationManager = AnimationManager(mainActivity!!)

        // うぉっちっちのimageView取得
        imageButton = mainActivity?.findViewById<ImageButton>(R.id.watchcchi_image)

        // 仲良し
        friendShip = FriendShip(mainActivity!!)
        // お腹
        hunger = Hunger(mainActivity!!)
    }

    // 画面表示時の画像・アニメーションセット
    fun setHiyokoImage(){
        when(watchicchiApp.getWatchicchi().getEvolveLevel()){
            WatchicchiApp.EvolveLevel.EGG -> {
                // 卵
                imageButton?.setImageResource(R.drawable.egg)
            }
            WatchicchiApp.EvolveLevel.BIRTH -> {
                // 生まれる処理
                animationManager?.eggToHiyoko()
            }
            WatchicchiApp.EvolveLevel.HIYOKO_WITH_EGG ->{
                // たまご付ひよこの画像
                imageButton?.setImageResource(R.drawable.birth5)
            }
            WatchicchiApp.EvolveLevel.HIYOKO ->{
                // 歩きまわる処理
                animationManager?.walking(R.drawable.hiyoko)
            }
            WatchicchiApp.EvolveLevel.HIYOKO_TO_NIWATORI ->{
                // 進化
                animationManager?.hiyokoToNiwatori()
            }
            WatchicchiApp.EvolveLevel.NIWATORI ->{
                // 鶏が歩き回る処理
                animationManager?.walking(R.drawable.niwatori)
            }
            WatchicchiApp.EvolveLevel.NIWATORI_LAY_EGG ->{
                // 進化
                animationManager?.niwatoriLayEgg()
            }
        }
    }

    fun envolve(){
        nextEvolveLevel()
        setHiyokoImage()
    }


    // 進化
    fun nextEvolveLevel(){
        evolveLevel = when(evolveLevel){
            WatchicchiApp.EvolveLevel.EGG -> WatchicchiApp.EvolveLevel.BIRTH
            WatchicchiApp.EvolveLevel.BIRTH -> WatchicchiApp.EvolveLevel.HIYOKO_WITH_EGG
            WatchicchiApp.EvolveLevel.HIYOKO_WITH_EGG -> WatchicchiApp.EvolveLevel.HIYOKO
            WatchicchiApp.EvolveLevel.HIYOKO -> WatchicchiApp.EvolveLevel.HIYOKO_TO_NIWATORI
            WatchicchiApp.EvolveLevel.HIYOKO_TO_NIWATORI -> WatchicchiApp.EvolveLevel.NIWATORI
            WatchicchiApp.EvolveLevel.NIWATORI -> WatchicchiApp.EvolveLevel.NIWATORI_LAY_EGG
            WatchicchiApp.EvolveLevel.NIWATORI_LAY_EGG -> WatchicchiApp.EvolveLevel.EGG
        }
        if (evolveLevel == WatchicchiApp.EvolveLevel.NIWATORI_LAY_EGG){
            watchicchiApp.getWatchicchiInfo().plusGeneration()
        }
    }

    // 状態取得
    fun getEvolveLevel():WatchicchiApp.EvolveLevel{
        return evolveLevel
    }

    fun getFriendship():FriendShip{
        return  friendShip
    }

    fun getHunger():Hunger{
        return  hunger
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