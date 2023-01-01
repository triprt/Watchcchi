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
    var hungerLevel = 0
    lateinit var lastUpdateTime : LocalDateTime
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSS")
    var timer :Timer? = null

    init{
        activity = _activity
        //getSharedPreferencesから持ってくる
        // 保存された値を取得 hungerLevel セット
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        hungerLevel = pref.getInt("hungerLevel",6)
        println("hungerLevelレベル取得")
        println(hungerLevel)

        //lastUpdateTime セット
        val lastUpdateTimeStr = pref.getString("hungerLevelLastUpdateTimeStr", LocalDateTime.now().format(formatter))
        lastUpdateTime = LocalDateTime.parse(lastUpdateTimeStr,formatter)

        //lastUpdateTimeと今の差分を取得し、hungerLevelをセット
        val dif = ChronoUnit.HOURS.between(LocalDateTime.now(),lastUpdateTime).toDouble()
        minusHungryLevel(Math.floor(dif/2).toInt())

        // 2時間ごとにお腹をすかせる処理を開始
        hungryHandler()
    }

    // 2時間ごとにお腹をすかせる処理
    fun hungryHandler(){
        // 2時間から経過時間を引く
        val delay:Long = 10000 - ChronoUnit.MILLIS.between(LocalDateTime.now(),lastUpdateTime)

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                // 2時間ごとに実行する処理
                minusHungryLevel(1)
                println("マイナス")
                println(hungerLevel)

                if(hungerLevel == 0){
                    timer?.cancel()
                    timer = null
                }
            }
        }, delay, 10 * 1000) // 2時間 = 2 * 60 * 60 * 1000[ms]
    }

    // 満腹度を減らす処理
    fun minusHungryLevel(num : Int){
        // 0であれば何もしない
         if(hungerLevel == 0) return
        // num分引いて、マイナスであれば0にする
        hungerLevel -= num

        if(hungerLevel < 0) hungerLevel = 0

        // 更新時間を更新 2*num
        val startDateStr = LocalDateTime.now().format(formatter)
        lastUpdateTime = LocalDateTime.parse(startDateStr,formatter)

        // 保存もしておく
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = pref.edit()
        editor.putInt ("hungerLevel", hungerLevel)
        editor.putString ("hungerLevelLastUpdateTimeStr", LocalDateTime.now().format(formatter))
        editor.apply()
    }

    // 足す1する

    // statusActivityから呼ぶ
    fun getOnakaText():String{
        return when(hungerLevel){
           in 4..6 -> "● ● ● ●"
            3 -> "● ● ● ○"
            2 -> "● ● ○ ○"
            1 -> "● ○ ○ ○"
            else -> "○ ○ ○ ○"
        }

    }
}