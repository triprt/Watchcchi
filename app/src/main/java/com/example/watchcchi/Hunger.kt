package com.example.watchcchi

import android.app.Activity
import android.preference.PreferenceManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class Hunger constructor( _activity: Activity)  {
    lateinit var activity:Activity
    private var level = 0 // 6段階
    lateinit var lastUpdateTime : LocalDateTime
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSS")
    private var timer :Timer? = null

    init{
        activity = _activity
        //getSharedPreferencesから持ってくる
        // 保存された値を取得 hungerLevel セット
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        level = pref.getInt("hungerLevel",0)
        println("hungerLevelレベル取得")
        println(level)

        //lastUpdateTime セット
        val lastUpdateTimeStr = pref.getString("hungerLevelLastUpdateTimeStr", LocalDateTime.now().format(formatter))
        lastUpdateTime = LocalDateTime.parse(lastUpdateTimeStr,formatter)
        println(LocalDateTime.now().format(formatter))

        //lastUpdateTimeと今の差分を取得し、hungerLevelをセット
        val dif = ChronoUnit.HOURS.between(lastUpdateTime,LocalDateTime.now()).toDouble()
        minusLevel(Math.floor(dif/2).toInt())

        // 2時間ごとにお腹をすかせる処理を開始
        hungryHandler()
    }

    // 2時間ごとにお腹をすかせる処理
    private fun hungryHandler(){
        // 2時間から経過時間を引く
        var delay:Long = 10 * 1000 - ChronoUnit.MILLIS.between(lastUpdateTime, LocalDateTime.now())
        if (delay < 0){
            delay = 0
        }
        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                // 2時間ごとに実行する処理
                minusLevel(1)
                println("マイナス")
                println(level)

                if(level == 0){
                    timer?.cancel()
                    timer = null
                }
            }
        }, delay, 10 * 1000) // 2時間 = 2 * 60 * 60 * 1000[ms]
    }

    // 満腹度を減らす処理
    private fun minusLevel(num : Int){
        // 0であれば何もしない
         if(level == 0) return
        // num分引いて、マイナスであれば0にする
        level -= num

        if(level < 0) level = 0
        // 更新時間を更新 2*num
        lastUpdateTime = lastUpdateTime.plusHours(num*2L)
        // 0になったら値保存
        if(level == 0){
            saveHunger0TimeToDefaultSharedPreferences(lastUpdateTime)
            // 0になったのでスタート
            watchcchi?.friendShip?.friendShipHandler()
        }
        // 保存もしておく
        saveToDefaultSharedPreferences()
    }

    // 餌があげられるか判定
    fun canFeed():Boolean{
        return level < 6
    }

    // 餌をあげる処理から呼び出す
    fun isEnvolveAndPlusLevel(friendShip:FriendShip):Boolean{
        // 満腹であれば何もしない
        if(level == 6) return false
        // レベルあげる
        level += 1
        println("plusHungerLevel")
        println(level)

        // 時間更新
        lastUpdateTime = LocalDateTime.now()
        // 値保存
        saveToDefaultSharedPreferences()
        // 0だった時はタイマーを開始、0時間をリセット
        if(level == 1) {
            hungryHandler()
            saveHunger0TimeToDefaultSharedPreferences(null)
            watchcchi?.friendShip?.stopFriendShipHandler()
        }
        // ご飯あげた回数をプラス
        return friendShip.isEnvolveAndPlusFeedCount()
    }

    // 値保存
    private fun saveToDefaultSharedPreferences(){
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = pref.edit()
        editor.putInt ("hungerLevel", level)
        editor.putString ("hungerLevelLastUpdateTimeStr",lastUpdateTime.format(formatter) )
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

    // statusActivityから呼ぶ
    fun getOnakaText():String{
        return when(level){
           in 4..6 -> "● ● ● ●"
            3 -> "● ● ● ○"
            2 -> "● ● ○ ○"
            1 -> "● ○ ○ ○"
            else -> "○ ○ ○ ○"
        }
    }


}