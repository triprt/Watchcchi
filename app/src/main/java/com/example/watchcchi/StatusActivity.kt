package com.example.watchcchi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        nakayoshiTextView.text = watchicchiApp.getWatchicchi().getNakayoshiText()
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
        val backButton =  findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            // もとの画面に戻る
            finish()
        }
    }
}