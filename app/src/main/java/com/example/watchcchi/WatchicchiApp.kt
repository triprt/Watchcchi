package com.example.watchcchi

import android.app.Application
import android.preference.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 定数

// お腹が空くまでの時間
val HUNGER_INTERVAL_HOUR = 1
// 満腹
val FULL_HUNGER_LEVEL = 3
// 仲良し
val FULL_FRIENDSHIP_LEVEL = 1

class WatchicchiApp:Application() {


    enum class EvolveLevel(val id: Int) {
        EGG(0),
        BIRTH(1),
        HIYOKO_WITH_EGG(2),
        HIYOKO(3),
        HIYOKO_TO_NIWATORI(4),
        NIWATORI(5),
        NIWATORI_LAY_EGG(6),
    }

    private lateinit var  watchcchiInfo : WatchcchiInfo
    private lateinit var  watchcchi : Watchcchi


    override fun onCreate() {
        super.onCreate()

        println("watchicchiApp 起動 onCreate")

        // 初回起動時であれば初期値セット
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        if(pref.getBoolean("isFirstLaunch", true)) {
            val editor = pref.edit()
            // 進化レベル0
            editor.putInt("evolveLevel", 0)
            // 始めた日付
            val formatter = DateTimeFormatter.ofPattern("d MM, yyyy")
            editor.putString("startDateStr", LocalDateTime.now().format(formatter))
            // 最初は1世代
            editor.putInt("generation", 1)
            // おなかはぺこぺこ
            editor.putInt("hungerLevel", 0)
            // 仲良しレベル0
            editor.putInt("friendshipLevel", 0)
            // ご飯回数も0
            editor.putInt("feedCount", 1)
            // 音鳴るかフラグ
            editor.putBoolean("isSoundOn", true)
            // おなか更新時間はいらないかな？

            // 初回起動フラグをfalseに
            editor.putBoolean("isFirstLaunch", false)
            editor.apply()
        }


        //　うぉっちっち状態管理クラス
        watchcchi = Watchcchi(this)
        // 世代や日数など
        watchcchiInfo = WatchcchiInfo(this)

        println("起動時処理完了")
    }

    fun getWatchicchi(): Watchcchi{
        return watchcchi
    }

    fun getWatchicchiInfo(): WatchcchiInfo{
        return watchcchiInfo
    }

}