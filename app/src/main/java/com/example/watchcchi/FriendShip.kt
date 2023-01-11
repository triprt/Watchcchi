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
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSS")

    init{
        activity = _activity
        // 保存された値を取得 hungerLevel セット
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        level = pref.getInt("friendshipLevel",3)
        feedCount = pref.getInt("feedCount",1)
        println("friendshipLevelレベル取得 " + level)
        println("feedCount取得 " + feedCount)
    }

    fun minusLevel(){
        // 0であれば何もしない
        if(level == 0) return
        // num分引いて、マイナスであれば0にする
        level -= 1
        // 保存もしておく
        saveToDefaultSharedPreferences()
    }

    // ご飯を6回あげると1プラスされる ６になったら進化するので、進化するかを返す
    fun plusFeedCount(){
        feedCount += 1
        println("feedcount プラス " + feedCount)

        // 6回餌をあげて、レベルが6でない時は
        // 1足して餌あげた回数をリセットする
        if(feedCount == 6 && level < 6){
            level += 1
            feedCount = 0
            println("仲良し度 プラス " + level)

        }
        // 6になったら進化する
        //if(level == 4) {
            // 進化
            watchcchi?.envolve()
            feedCount = 0
            level = 0
       //}
        saveToDefaultSharedPreferences()
    }

    // 値保存
    private fun saveToDefaultSharedPreferences(){
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = pref.edit()
        editor.putInt ("friendshipLevel", level)
        editor.putInt ("feedCount", feedCount)
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
}