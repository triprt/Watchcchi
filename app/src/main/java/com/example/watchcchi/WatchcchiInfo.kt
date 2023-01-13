package com.example.watchcchi

import android.app.Activity
import java.time.LocalDate
import android.preference.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class WatchcchiInfo constructor(_watchicchiApp:WatchicchiApp){
    // sharePrefのために渡す
    private var watchicchiApp: WatchicchiApp

    // うぉっちっち情報
    private lateinit var startDate: LocalDate
    private var generation: Int = 1
    private val formatter = DateTimeFormatter.ofPattern("d MM, yyyy")

    init{
        // Applicationセット
        watchicchiApp = _watchicchiApp

        // SharedPreferencesを取得する
        val pref = PreferenceManager.getDefaultSharedPreferences(watchicchiApp)
        val startDateStr = pref.getString("startDateStr", LocalDateTime.now().format(formatter))

        val formatter = DateTimeFormatter.ofPattern("d MM, yyyy")
        generation = pref.getInt("generation", 1)
        startDate = LocalDate.parse(startDateStr,formatter)

        println("WatchichiInfo init 完了")
    }

    fun getDayText():String{
        // 日付の差分計算
        return (ChronoUnit.DAYS.between(LocalDate.now(),startDate) + 1).toString() + "日目"
    }

    fun getGenerationText():String{
        return generation.toString() + "世代"
    }

    fun plusGeneration(){
        generation ++
    }
}