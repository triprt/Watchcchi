package com.example.watchcchi

import android.app.Activity
import android.preference.PreferenceManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class Hunger constructor( _mainActivity: MainActivity)  {
    lateinit var mainActivity:Activity
    private var level = 0 // 6段階
    lateinit var lastUpdateTime : LocalDateTime
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSS")
    private var timer :Timer? = null
    private lateinit var watchicchiApp: WatchicchiApp

    init{
        mainActivity = _mainActivity
        watchicchiApp = mainActivity.application as WatchicchiApp

        //getSharedPreferencesから持ってくる
        // 保存された値を取得 hungerLevel セット
        val pref = PreferenceManager.getDefaultSharedPreferences(mainActivity)
        level = pref.getInt("hungerLevel",0)
        println("hungerLevelレベル取得")
        println(level)

        //lastUpdateTime セット
        val lastUpdateTimeStr = pref.getString("hungerLevelLastUpdateTimeStr", LocalDateTime.now().format(formatter))
        println("hungerLevelLastUpdateTimeStr" + lastUpdateTimeStr)
        lastUpdateTime = LocalDateTime.parse(lastUpdateTimeStr,formatter)
        println(LocalDateTime.now().format(formatter))

        //lastUpdateTimeと今の差分を取得し、hungerLevelをセット
        var dif = ChronoUnit.HOURS.between(lastUpdateTime,LocalDateTime.now()).toDouble()
        if( dif < 0 ){
            dif = 0.0
        }

        for(i in 1..Math.floor(dif/HUNGER_INTERVAL_HOUR).toInt()){
            minusLevel()
        }
        // 2時間ごとにお腹をすかせる処理を開始
        hungryHandler()
    }

    // 2時間ごとにお腹をすかせる処理
    private fun hungryHandler(){
        // 2時間のミリ秒で表した値
        val TWO_HOURS_MS : Long = HUNGER_INTERVAL_HOUR.toLong() * 60 * 60 * 1000
        // 前回の満腹度更新時間から経過したミリ秒数
        val diffMS : Long = ChronoUnit.MILLIS.between(lastUpdateTime, LocalDateTime.now())
        // 次の2時間定期処理を始めるまで待つ時間（前回の満腹度更新時間から5:30経過していたら、1:30後に定期処理を開始）
        // (例) 05:30 % 02:00 = 01:30
        var shiftTimeMS: Long = diffMS % TWO_HOURS_MS

        if(shiftTimeMS < 0){
            shiftTimeMS = 0
        }

        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                // 2時間ごとに実行する処理
                minusLevel()
                println("満腹度マイナス" + level)
            }
        }, shiftTimeMS, TWO_HOURS_MS)
    }

    // 満腹度を減らす処理
    private fun minusLevel(){
        if(level > 0){
            // レベル下げる
            level -= 1
            // 更新時間を更新 2*num
            lastUpdateTime = lastUpdateTime.plusHours(HUNGER_INTERVAL_HOUR.toLong())
            // 保存もしておく
            saveToDefaultSharedPreferences()
        } else{
            // 満腹度0かつ前回の満腹度更新から、2h以上経過していたら
            if(lastUpdateTime.plusHours(HUNGER_INTERVAL_HOUR.toLong()) > LocalDateTime.now()) {
                watchicchiApp.getWatchicchi().getFriendship().minusLevel()
            }
        }
    }

    // 餌があげられるか判定 ひよこと鶏のときしか食べられないようにする
    fun canFeed():Boolean{
        val envolveLevel = watchicchiApp.getWatchicchi().getEvolveLevel()
        return level < FULL_HUNGER_LEVEL &&
                (envolveLevel == WatchicchiApp.EvolveLevel.HIYOKO || envolveLevel == WatchicchiApp.EvolveLevel.NIWATORI)
    }

    // 餌をあげる処理から呼び出す
    fun plusLevel(){
        // 満腹であれば何もしない
        if(!canFeed()) return
        // レベルあげる
        level += 1
        println("plusHungerLevel" + level)

        // 時間更新
        lastUpdateTime = LocalDateTime.now()
        // 値保存
        saveToDefaultSharedPreferences()

        // ご飯あげた回数をプラス
        watchicchiApp.getWatchicchi().getFriendship().plusFeedCount()
    }

    // 進化時0
    fun resetLevel(){
        level = 0
        // 値保存
        saveToDefaultSharedPreferences()
    }

    // 値保存
    private fun saveToDefaultSharedPreferences(){
        val pref = PreferenceManager.getDefaultSharedPreferences(mainActivity)
        val editor = pref.edit()
        editor.putInt ("hungerLevel", level)
        editor.putString ("hungerLevelLastUpdateTimeStr",lastUpdateTime.format(formatter) )
        editor.apply()
    }

    // statusActivityから呼ぶ
    fun getOnakaText():String{
        return when(level){
           in 4..FULL_HUNGER_LEVEL -> "● ● ● ●"
            3 -> "● ● ● ○"
            2 -> "● ● ○ ○"
            1 -> "● ○ ○ ○"
            else -> "○ ○ ○ ○"
        }
    }
}