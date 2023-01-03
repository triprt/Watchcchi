package com.example.watchcchi

import android.app.Activity
import android.preference.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class FriendShip constructor( _activity: Activity)  {
    lateinit var activity: Activity
    private var level = 0
    private var feedCount = 0
    // お腹すいた度が0 になった時間
    var hungerLevel0LastTime : LocalDateTime? = null
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSS")
    private var timer : Timer? = null

    init{
        activity = _activity
        // 保存された値を取得 hungerLevel セット
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        level = pref.getInt("friendshipLevel",0)
        feedCount = pref.getInt("feedCount",0)
        println("friendshipLevelレベル取得")
        println(level)
        println("feedCount取得")
        println(feedCount)

        // お腹すいた度から現在の仲良しレベルを計算
        val hungerLevel0LastTimeStr = pref.getString("hungerLevel0LastTimeStr", null)
        // 0になった時の時間があれば減らす処理
        if(hungerLevel0LastTimeStr != null) {
            hungerLevel0LastTime = LocalDateTime.parse(hungerLevel0LastTimeStr,formatter)

            //lastUpdateTimeと今の差分を取得し、hungerLevelをセット
            val dif = ChronoUnit.HOURS.between(hungerLevel0LastTime,LocalDateTime.now()).toDouble()
            minusLevel(Math.floor(dif/2).toInt())
        }
        // タイマースタート
        friendShipHandler()
    }

    private fun minusLevel(num:Int){
        // 0であれば何もしない
        if(level == 0) return

        // num分引いて、マイナスであれば0にする
        var newLevel = level - num
        if(newLevel < 0) newLevel = 0

        // いくつ引いたか
        val dif = level-newLevel
        level = newLevel
        // 保存もしておく
        saveToDefaultSharedPreferences()
        // ひいたら0になった時間を更新
        hungerLevel0LastTime?.plusHours(2L*dif)
        saveHunger0TimeToDefaultSharedPreferences(hungerLevel0LastTime)
    }

    //
    fun friendShipHandler(){
        println("friendShipHandlerスタート")
        // 2時間から経過時間を引く
        if(hungerLevel0LastTime == null) return

        var delay:Long = 10 * 1000 - ChronoUnit.MILLIS.between(hungerLevel0LastTime, LocalDateTime.now())
        if (delay < 0){
            delay = 0
        }
        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                // 2時間ごとに実行する処理
                minusLevel(1)
                println("friendShipマイナス")
                println(level)

                if(level == 0){
                    timer?.cancel()
                    timer = null
                }
            }
        }, delay, 10 * 1000) // 2時間 = 2 * 60 * 60 * 1000[ms]

    }
    // ご飯を6回あげると1プラスされる ６になったら進化するので、進化するかを返す
    fun isEnvolveAndPlusFeedCount():Boolean{
        feedCount += 1
        println("feedcount プラス")
        println(feedCount)

        if(feedCount == 6 && level < 6){
            level += 1
            feedCount = 0
            println("仲良し度 プラス")
            println(level)
        }
        saveToDefaultSharedPreferences()
        // 6になったら進化する
        return level == 6
    }

    // 値保存
    private fun saveToDefaultSharedPreferences(){
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = pref.edit()
        editor.putInt ("friendshipLevel", level)
        editor.putInt ("feedCount", feedCount)
        //editor.putString ("hungerLevelLastUpdateTimeStr",lastUpdateTime.format(formatter) )
        editor.apply()
    }

    // 0になった時の時間を保存
    private fun saveHunger0TimeToDefaultSharedPreferences(localDateTime:LocalDateTime?){
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = pref.edit()
        editor.putInt ("hungerLevel", level)
        editor.putString ("hungerLevel0LastTimeStr", localDateTime?.format(formatter))
        editor.apply()
    }

    // 画面に表示させる
    fun getOnakaText():String{
        return when(level){
            in 4..6 -> "● ● ● ●"
            3 -> "● ● ● ○"
            2 -> "● ● ○ ○"
            1 -> "● ○ ○ ○"
            else -> "○ ○ ○ ○"
        }
    }

    // タイマーストップ
    fun stopFriendShipHandler(){
        timer?.cancel()
        timer = null
    }
}