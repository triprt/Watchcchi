package com.example.watchcchi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StatusActivity : Activity() {
    private lateinit var watchicchiApp: WatchicchiApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        watchicchiApp = this.application as WatchicchiApp

        setContentView(R.layout.activity_status)
        setTapEvent()
        setImageButton()
    }

    // viewDidAppear的なの
    override fun onResume() {
        super.onResume()
        setupText()

    }


    // 変数セット
    private fun setupText(){
        val dayTextView =  findViewById<TextView>(R.id.day_text)
        dayTextView.text = watchicchiApp.getWatchicchiInfo().getDayText()
        val generationTextView =  findViewById<TextView>(R.id.generation_text)
        generationTextView.text = watchicchiApp.getWatchicchiInfo().getGenerationText()

        val onakaTextView =  findViewById<TextView>(R.id.onaka)
        onakaTextView.text = watchicchiApp.getWatchicchi().getOnakaText()
        val nakayoshiTextView =  findViewById<TextView>(R.id.nakayoshi)
        nakayoshiTextView.text = watchicchiApp.getWatchicchi().getFriendShipText()
    }

    // ボタン画像セット
    private fun setImageButton(){
        val soundButton =  findViewById<ImageButton>(R.id.sound)
        if(isSoundOn()){
            soundButton.setBackgroundResource(R.drawable.sound_on)
        }else{
            soundButton.setBackgroundResource(R.drawable.sound_off)
        }
    }
    // 音鳴らすかを取得
    private fun isSoundOn():Boolean{
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        return pref.getBoolean("isSoundOn",false)
    }

    // ボタン押した時の処理
    private fun setTapEvent(){
        val foodButton =  findViewById<ImageButton>(R.id.food)
        foodButton.setOnClickListener {
            // ご飯をあげるactivityを開始
            val intent = Intent(this, EatActivity::class.java)
            startActivity(intent)

        }
        val gameButton =  findViewById<ImageButton>(R.id.game)
        gameButton.setOnClickListener {
            // ゲーム開始

        }
        val soundButton =  findViewById<ImageButton>(R.id.sound)
        soundButton.setOnClickListener {
            // 音のON/Off切り替え
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = pref.edit()
            editor.putBoolean("isSoundOn", !pref.getBoolean("isSoundOn",true))
            editor.apply()
            // 画像切り替え
            setImageButton()

        }

        val backButton =  findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            // もとの画面に戻る
            finish()
        }
    }
}