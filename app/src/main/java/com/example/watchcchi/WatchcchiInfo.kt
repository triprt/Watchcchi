package com.example.watchcchi

import android.app.Activity
import java.time.LocalDate
import android.preference.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class WatchcchiInfo constructor( _activity:Activity){

    // うぉっちっち情報
    lateinit var startDate: LocalDate
    var generation: Int = 1
    val formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy")
    // アクティビティ
    lateinit var activity: Activity

    init{
        activity = _activity

        // SharedPreferencesを取得する
        val pref = PreferenceManager.getDefaultSharedPreferences(_activity)
        val startDateStr = pref.getString("startDateStr", LocalDateTime.now().format(formatter))
        generation = pref.getInt("generation", 1)
        startDate = LocalDate.parse(startDateStr,formatter)
    }

    fun getDayText():String{
        // 日付の差分計算
        return (ChronoUnit.DAYS.between(LocalDate.now(),startDate) + 1).toString() + "日目"
    }

    fun getGenerationText():String{
        return generation.toString() + "世代"
    }
}