package com.example.watchcchi

import android.app.Activity
import android.preference.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class FriendShip constructor( _mainActivity: MainActivity)  {
    lateinit var mainActivity: Activity
    private var level = 0
    private var feedCount = 0
    private lateinit var watchicchiApp: WatchicchiApp

    init{
        mainActivity = _mainActivity
        watchicchiApp = mainActivity.application as WatchicchiApp
        // 保存された値を取得 hungerLevel セット
        val pref = PreferenceManager.getDefaultSharedPreferences(mainActivity)
        level = pref.getInt("friendshipLevel",0)
        feedCount = pref.getInt("feedCount",0)
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
        if(feedCount == FULL_HUNGER_LEVEL && level < FULL_FRIENDSHIP_LEVEL){
            level += 1
            feedCount = 0
            println("仲良し度 プラス " + level)

        }
        // 6になったら進化する
        if(level == FULL_FRIENDSHIP_LEVEL) {
            // 次にMainActivityが開かれた際に進化を実行するので
            // レベルだけあげておく
            watchicchiApp.getWatchicchi().nextEvolveLevel()
            feedCount = 0
            level = 0
       }
        saveToDefaultSharedPreferences()
    }

    // 値保存
    private fun saveToDefaultSharedPreferences(){
        val pref = PreferenceManager.getDefaultSharedPreferences(mainActivity)
        val editor = pref.edit()
        editor.putInt ("friendshipLevel", level)
        editor.putInt ("feedCount", feedCount)
        editor.apply()
    }

    // 画面に表示させる
    fun getFriendShipText():String{
        return when(level){
            in 4..FULL_FRIENDSHIP_LEVEL -> "● ● ● ●"
            3 -> "● ● ● ○"
            2 -> "● ● ○ ○"
            1 -> "● ○ ○ ○"
            else -> "○ ○ ○ ○"
        }
    }
}