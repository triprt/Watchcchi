package com.example.watchcchi

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import com.example.watchcchi.databinding.ActivityMainBinding
import java.lang.Thread.sleep


// こけこっこー
// http://mimimi-sound.com/?s=%E3%81%B2%E3%82%88%E3%81%93
//こけ
//https://soundeffect-lab.info/agreement/
//ひよこ
// https://taira-komori.jpn.org/welcome.html



class MainActivity : Activity(){

    private lateinit var binding: ActivityMainBinding
    private var  tapSum = 0
    private lateinit var  hiyokoMediaPlayer: MediaPlayer
    private lateinit var  niwatoriMediaPlayer: MediaPlayer
    private lateinit var watchicchiApp: WatchicchiApp
    private lateinit var hiyokoImageButton:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 変数設定など
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 何度も定義するのはあれなのでここで定義
        hiyokoMediaPlayer = MediaPlayer.create(this, R.raw.piyo)
        niwatoriMediaPlayer = MediaPlayer.create(this, R.raw.kokekokko)

        // ボタンタップ時のリスナー
        val statusButton =  findViewById<ImageButton>(R.id.status_button)
        statusButton.setOnClickListener {
            // 新しい画面に遷移する処理
            val intent = Intent(applicationContext, StatusActivity::class.java)
            startActivity(intent)
        }

        hiyokoImageButton = findViewById<ImageButton>(R.id.watchcchi_image)
        hiyokoImageButton.setOnClickListener {
            // タップされた時の処理
            onClickHiyokoImageButton()
        }
        watchicchiApp = this.application as WatchicchiApp
        watchicchiApp.getWatchicchi().setUpActivity(this)
        println("MainActivity onCreate() 完了")
    }


    // viewDidAppearてきな？
    override fun onResume(){
        super.onResume()
        println("=========main onResume()=====================")

        // 進化レベルに応じてボタンの表示・非表示切り替え
        changeStatusButtonVisibility()

        // 最初に呼ぶ関数
        watchicchiApp.getWatchicchi().setHiyokoImage()
    }

    fun changeStatusButtonVisibility(){
        val statusButton =  findViewById<ImageButton>(R.id.status_button)

        // ひよこと鶏の時だけボタン表示
        if (watchicchiApp.getWatchicchi().getEvolveLevel() == WatchicchiApp.EvolveLevel.HIYOKO
            ||
            watchicchiApp.getWatchicchi().getEvolveLevel() == WatchicchiApp.EvolveLevel.NIWATORI
        ){
            statusButton.visibility = View.VISIBLE
        }else{
            statusButton.visibility = View.INVISIBLE
        }
    }



    // 画面タッチ処理はoverride fun onTouchEventの中に書く
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // タップして離れたら
        if ( event?.action == android.view.MotionEvent.ACTION_UP) {
        }
        return super.onTouchEvent(event)
    }

    // ひよこ画像エリアタップ
    fun onClickHiyokoImageButton(){
        when (watchicchiApp.getWatchicchi().getEvolveLevel()) {
            WatchicchiApp.EvolveLevel.EGG  -> {
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(50)
                tapSum++
                if (tapSum == 10) {
                    // 進化
                    watchicchiApp.getWatchicchi().envolve()
                    tapSum = 0
                }
            }
            WatchicchiApp.EvolveLevel.HIYOKO_WITH_EGG -> {
                // 進化
                watchicchiApp.getWatchicchi().envolve()
            }
            WatchicchiApp.EvolveLevel.HIYOKO -> {
                // 鳴き声
                crow(R.drawable.hiyoko, R.drawable.hiyoko_open_mouth, hiyokoMediaPlayer)
            }
            WatchicchiApp.EvolveLevel.NIWATORI -> {
                // 鳴き声
                crow(R.drawable.niwatori, R.drawable.niwatori_open_mouth, niwatoriMediaPlayer)
            }
        }
    }

    // 鳴き声の処理
    fun crow(oriImage:Int, openImage:Int, mediaPlayer:MediaPlayer){
        hiyokoImageButton.setImageResource(openImage)

        // 画像をもどすためにハンドラー
        var handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                // 画像切り替え
                hiyokoImageButton.setImageResource(oriImage)
            }
        }
        handler.postDelayed(runnable, 400)

        if (isSoundOn()) {
            mediaPlayer.start()
        }
    }

    // 音鳴らすかを取得
    private fun isSoundOn():Boolean{
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        return pref.getBoolean("isSoundOn",false)
    }

    // 音鳴らすか保存


    // アプリ終了時、うぉっちっちの状態を保存
    override fun onDestroy() {
        super.onDestroy()
        println( "アプリ終了")
    }
}