package com.example.watchcchi

import android.app.Application
import android.preference.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        // 初回起動時であれば初期値セット
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        if(pref.getBoolean("isFirstLaunch", true)){
            // 進化レベル0
            editor.putInt ("evolveLevel", 0)
            // 始めた日付
            val formatter = DateTimeFormatter.ofPattern("d MM, yyyy")
            editor.putString ("startDateStr", LocalDateTime.now().format(formatter) )
            // 最初は1世代
            editor.putInt("generation", 1)
            // 初回起動フラグをfalseに
            editor.putBoolean ("isFirstLaunch", false)
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